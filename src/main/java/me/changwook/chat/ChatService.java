package me.changwook.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.chat.dto.ChatMessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 채팅 기능의 비즈니스 로직을 담당하는 서비스 클래스
 * 
 * 이 서비스는 채팅 메시지의 영구 저장과 조회 기능을 제공하여,
 * 사용자가 채팅방을 나갔다가 다시 들어와도 이전 메시지들을 볼 수 있도록 합니다.
 * 트랜잭션 관리, 데이터 검증, 비즈니스 로직 처리를 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    /**
     * 모든 활성 채팅방 목록을 조회합니다.
     * 사용자가 참여할 수 있는 채팅방들을 제공합니다.
     * 
     * @return 활성 상태인 채팅방 목록
     */
    public List<ChatRoom> getAllActiveRooms() {
        return chatRoomRepository.findByIsActiveTrueOrderByCreatedAtAsc();
    }

    /**
     * 특정 채팅방 정보를 조회합니다.
     * 채팅방 존재 여부와 활성 상태를 확인합니다.
     * 
     * @param roomId 조회할 채팅방 ID
     * @return 채팅방 정보 (존재하지 않으면 empty)
     */
    public Optional<ChatRoom> getRoomById(String roomId) {
        return chatRoomRepository.findByRoomIdAndIsActiveTrue(roomId);
    }

    /**
     * 새로운 채팅방을 생성합니다.
     * 중복 ID 검사를 수행하고 초기 설정을 적용합니다.
     * 
     * @param roomId 채팅방 ID
     * @param roomName 채팅방 이름
     * @param description 채팅방 설명
     * @return 생성된 채팅방 정보
     * @throws IllegalArgumentException 이미 존재하는 채팅방 ID인 경우
     */
    @Transactional
    public ChatRoom createRoom(String roomId, String roomName, String description) {
        // 중복 ID 검사
        if (chatRoomRepository.existsByRoomId(roomId)) {
            log.warn("채팅방 생성 실패: 이미 존재하는 ID - {}", roomId);
            throw new IllegalArgumentException("이미 존재하는 채팅방 ID입니다: " + roomId);
        }

        // 입력값 검증
        if (roomName == null || roomName.trim().isEmpty()) {
            throw new IllegalArgumentException("채팅방 이름은 필수입니다");
        }

        ChatRoom chatRoom = ChatRoom.createChatRoom(roomId, roomName.trim(), description);
        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);
        
        log.info("새 채팅방 생성 완료: {} - {}", roomId, roomName);
        return savedRoom;
    }

    /**
     * 특정 채팅방의 모든 메시지를 시간순으로 조회합니다.
     * 사용자가 채팅방에 입장할 때 기존 메시지 히스토리를 제공합니다.
     * 이 메서드가 메시지 지속성의 핵심 기능입니다.
     * 
     * @param roomId 조회할 채팅방 ID
     * @return 해당 채팅방의 모든 메시지 목록
     */
    public List<ChatMessage> getMessagesByRoomId(String roomId) {
        // 채팅방 존재 여부 확인
        if (!chatRoomRepository.existsByRoomId(roomId)) {
            log.warn("존재하지 않는 채팅방의 메시지 조회 시도: {}", roomId);
            throw new IllegalArgumentException("존재하지 않는 채팅방입니다: " + roomId);
        }

        List<ChatMessage> messages = chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
        log.info("채팅방 {} 메시지 조회 완료: {} 개의 메시지", roomId, messages.size());
        return messages;
    }

    /**
     * 특정 채팅방의 최근 메시지들만 제한된 개수로 조회합니다.
     * 큰 채팅방에서 초기 로딩 성능을 개선하기 위해 사용됩니다.
     * 
     * @param roomId 조회할 채팅방 ID
     * @param limit 조회할 메시지 개수 (기본값: 50)
     * @return 최근 메시지 목록
     */
    public List<ChatMessage> getRecentMessages(String roomId, int limit) {
        if (limit <= 0) {
            limit = 50; // 기본값 설정
        }
        
        return chatMessageRepository.findRecentMessagesByRoomId(roomId, limit);
    }

    /**
     * 채팅 메시지를 데이터베이스에 저장합니다.
     * 이 메서드가 메시지 지속성을 가능하게 하는 핵심 기능입니다.
     * 
     * @param messageDTO WebSocket을 통해 받은 메시지 DTO
     * @return 저장된 메시지 엔티티
     */
    @Transactional
    public ChatMessage saveMessage(ChatMessageDTO messageDTO) {
        // 입력값 검증
        if (messageDTO.getRoomId() == null || messageDTO.getRoomId().trim().isEmpty()) {
            throw new IllegalArgumentException("채팅방 ID는 필수입니다");
        }
        
        if (messageDTO.getSender() == null || messageDTO.getSender().trim().isEmpty()) {
            throw new IllegalArgumentException("발신자 정보는 필수입니다");
        }

        // 채팅방 존재 여부 확인 및 자동 생성
        if (!chatRoomRepository.existsByRoomId(messageDTO.getRoomId())) {
            log.warn("존재하지 않는 채팅방에 메시지 저장 시도: {}", messageDTO.getRoomId());
            
            // 기본 채팅방들은 자동 생성
            if ("support".equals(messageDTO.getRoomId()) || 
                "general".equals(messageDTO.getRoomId()) || 
                "notice".equals(messageDTO.getRoomId())) {
                
                String roomName = switch (messageDTO.getRoomId()) {
                    case "support" -> "고객지원";
                    case "general" -> "자유 토론";
                    case "notice" -> "공지사항";
                    default -> messageDTO.getRoomId();
                };
                
                log.info("기본 채팅방 자동 생성: {} - {}", messageDTO.getRoomId(), roomName);
                createRoom(messageDTO.getRoomId(), roomName, "자동 생성된 채팅방입니다.");
            } else {
                throw new IllegalArgumentException("존재하지 않는 채팅방입니다: " + messageDTO.getRoomId());
            }
        }

        ChatMessage chatMessage;
        
        // 메시지 타입에 따른 처리
        switch (messageDTO.getType()) {
            case TALK:
                // 일반 텍스트 메시지
                if (messageDTO.getMessage() == null || messageDTO.getMessage().trim().isEmpty()) {
                    throw new IllegalArgumentException("메시지 내용이 비어있습니다");
                }
                chatMessage = ChatMessage.createTextMessage(
                    messageDTO.getRoomId(), 
                    messageDTO.getSender(), 
                    messageDTO.getMessage()
                );
                break;
                
            case IMAGE:
            case VIDEO:
                // 파일 첨부 메시지
                if (messageDTO.getFileUrl() == null || messageDTO.getFileUrl().trim().isEmpty()) {
                    throw new IllegalArgumentException("파일 URL이 누락되었습니다");
                }
                chatMessage = ChatMessage.createFileMessage(
                    messageDTO.getRoomId(),
                    messageDTO.getSender(),
                    ChatMessage.MessageType.valueOf(messageDTO.getType().name()),
                    messageDTO.getFileUrl(),
                    messageDTO.getThumbnailUrl()
                );
                break;
                
            case ENTER:
            case LEAVE:
                // 시스템 메시지 (입장/퇴장)
                String systemMessage = messageDTO.getMessage() != null ? 
                    messageDTO.getMessage() : 
                    generateSystemMessage(messageDTO.getSender(), messageDTO.getType());
                    
                chatMessage = ChatMessage.createSystemMessage(
                    messageDTO.getRoomId(),
                    messageDTO.getSender(),
                    ChatMessage.MessageType.valueOf(messageDTO.getType().name()),
                    systemMessage
                );
                break;
                
            default:
                throw new IllegalArgumentException("지원하지 않는 메시지 타입입니다: " + messageDTO.getType());
        }

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        log.info("메시지 저장 완료: 방={}, 타입={}, 발신자={}", 
                messageDTO.getRoomId(), messageDTO.getType(), messageDTO.getSender());
        
        return savedMessage;
    }

    /**
     * 사용자의 채팅방 입장을 처리합니다.
     * 참가자 수를 증가시키고 입장 메시지를 저장합니다.
     * 
     * @param roomId 채팅방 ID
     * @param username 사용자명
     */
    @Transactional
    public void enterRoom(String roomId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다: " + roomId));

        // 참가자 수 증가
        chatRoom.incrementParticipantCount();
        chatRoomRepository.save(chatRoom);

        // 입장 메시지 저장
        ChatMessage enterMessage = ChatMessage.createSystemMessage(
                roomId, username, ChatMessage.MessageType.ENTER, username + "님이 입장하셨습니다."
        );
        chatMessageRepository.save(enterMessage);

        log.info("사용자 {} 채팅방 {} 입장 처리 완료", username, roomId);
    }

    /**
     * 사용자의 채팅방 퇴장을 처리합니다.
     * 참가자 수를 감소시키고 퇴장 메시지를 저장합니다.
     * 
     * @param roomId 채팅방 ID
     * @param username 사용자명
     */
    @Transactional
    public void leaveRoom(String roomId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElse(null);

        if (chatRoom != null) {
            // 참가자 수 감소
            chatRoom.decrementParticipantCount();
            chatRoomRepository.save(chatRoom);

            // 퇴장 메시지 저장
            ChatMessage leaveMessage = ChatMessage.createSystemMessage(
                    roomId, username, ChatMessage.MessageType.LEAVE, username + "님이 퇴장하셨습니다."
            );
            chatMessageRepository.save(leaveMessage);

            log.info("사용자 {} 채팅방 {} 퇴장 처리 완료", username, roomId);
        }
    }

    /**
     * 채팅방의 메시지 개수를 조회합니다.
     * 통계 정보 제공에 사용됩니다.
     * 
     * @param roomId 채팅방 ID
     * @return 메시지 개수
     */
    public long getMessageCount(String roomId) {
        return chatMessageRepository.countByRoomId(roomId);
    }

    /**
     * 채팅방에서 키워드로 메시지를 검색합니다.
     * 채팅 내용 검색 기능을 제공합니다.
     * 
     * @param roomId 채팅방 ID
     * @param keyword 검색 키워드
     * @return 검색 결과 메시지 목록
     */
    public List<ChatMessage> searchMessages(String roomId, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("검색 키워드가 비어있습니다");
        }
        
        return chatMessageRepository.findByRoomIdAndMessageContentContainingIgnoreCaseOrderByCreatedAtDesc(
                roomId, keyword.trim()
        );
    }

    /**
     * 시스템 메시지를 생성하는 헬퍼 메서드
     * 
     * @param username 사용자명
     * @param messageType 메시지 타입
     * @return 생성된 시스템 메시지
     */
    private String generateSystemMessage(String username, ChatMessageDTO.MessageType messageType) {
        switch (messageType) {
            case ENTER:
                return username + "님이 입장하셨습니다.";
            case LEAVE:
                return username + "님이 퇴장하셨습니다.";
            default:
                return username + "님의 시스템 메시지";
        }
    }

    /**
     * 채팅방 이름으로 검색합니다.
     * 
     * @param roomName 검색할 채팅방 이름
     * @return 검색 결과 채팅방 목록
     */
    public List<ChatRoom> searchRoomsByName(String roomName) {
        if (roomName == null || roomName.trim().isEmpty()) {
            return getAllActiveRooms();
        }
        
        return chatRoomRepository.findByRoomNameContainingIgnoreCaseAndIsActiveTrue(roomName.trim());
    }

    /**
     * 특정 채팅방의 메시지를 페이지네이션으로 조회합니다.
     * 대용량 채팅방에서 성능 최적화를 위해 사용됩니다.
     * 
     * @param roomId 채팅방 ID
     * @param pageable 페이지 정보
     * @return 페이지네이션된 메시지 목록
     */
    public Page<ChatMessage> getMessagesByRoomId(String roomId, Pageable pageable) {
        return chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId, pageable);
    }
}
