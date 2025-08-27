package me.changwook.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.ChatMessageDTO;
import me.changwook.domain.ChatMessage;
import me.changwook.domain.ChatRoom;
import me.changwook.service.impl.ChatService;
import me.changwook.service.impl.LocalFileStorageService;
import me.changwook.util.ResponseFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 채팅 기능을 담당하는 컨트롤러 클래스 (수정 버전)
 * 
 * 기존 메모리 기반 채팅에서 데이터베이스 기반 채팅으로 업그레이드되었습니다.
 * 이제 메시지들이 영구 저장되어 사용자가 나갔다가 다시 들어와도 이전 메시지들을 볼 수 있습니다.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final LocalFileStorageService localFileStorageService;
    private final ResponseFactory responseFactory;
    // 새로 추가된 ChatService - 메시지 지속성의 핵심
    private final ChatService chatService;
    
    // 기본 채팅방들을 데이터베이스에 자동으로 생성하는 초기화 블록
    // 이제 메모리가 아닌 데이터베이스에서 채팅방을 관리합니다
    {
        initializeDefaultRooms();
    }
    
    /**
     * 애플리케이션 시작 시 기본 채팅방들을 데이터베이스에 생성합니다.
     * 이미 존재하는 경우에는 생성하지 않습니다.
     */
    private void initializeDefaultRooms() {
        try {
            // 기본 채팅방들을 데이터베이스에 생성 (중복 생성 방지)
            if (chatService.getRoomById("general").isEmpty()) {
                chatService.createRoom("general", "일반 채팅", "자유롭게 대화할 수 있는 공간입니다");
            }
            if (chatService.getRoomById("support").isEmpty()) {
                chatService.createRoom("support", "고객지원", "문의사항이나 도움이 필요할 때 이용해주세요");
            }
            if (chatService.getRoomById("rental").isEmpty()) {
                chatService.createRoom("rental", "렌터카 정보", "렌터카 관련 정보를 공유하는 공간입니다");
            }
            log.info("기본 채팅방 초기화 완료");
        } catch (Exception e) {
            log.error("기본 채팅방 초기화 중 오류 발생", e);
        }
    }

    /**
     * 채팅방 목록을 반환하는 API (데이터베이스 기반으로 수정)
     * 이제 데이터베이스에서 실제 채팅방 정보를 조회합니다.
     */
    @GetMapping("/chat/rooms")
    public ResponseEntity<ApiResponseDTO<List<ChatRoom>>> getChatRooms() {
        try {
            List<ChatRoom> roomList = chatService.getAllActiveRooms();
            log.info("채팅방 목록 조회 성공: {} 개의 방", roomList.size());
            return responseFactory.success("채팅방 목록을 성공적으로 가져왔습니다.", roomList);
        } catch (Exception e) {
            log.error("채팅방 목록 조회 실패", e);
            return responseFactory.internalServerError("모집방 리스트를 찾을 수 없습니다.");
        }
    }
    
    /**
     * 특정 채팅방 정보를 반환하는 API (데이터베이스 기반으로 수정)
     */
    @GetMapping("/chat/rooms/{roomId}")
    public ResponseEntity<ApiResponseDTO<ChatRoom>> getChatRoom(@PathVariable String roomId) {
        try {
            Optional<ChatRoom> chatRoom = chatService.getRoomById(roomId);
            if (chatRoom.isEmpty()) {
                return responseFactory.notFound("해당 채팅방을 찾을 수 없습니다.");
            }
            return responseFactory.success("채팅방 정보를 성공적으로 가져왔습니다.", chatRoom.get());
        } catch (Exception e) {
            log.error("채팅방 정보 조회 실패: {}", roomId, e);
            return responseFactory.internalServerError("채팅방 정보를 가져오는 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 새로운 채팅방을 생성하는 API (데이터베이스 기반으로 수정)
     */
    @PostMapping("/chat/rooms")
    public ResponseEntity<ApiResponseDTO<ChatRoom>> createChatRoom(
            @RequestParam String roomId,
            @RequestParam String roomName,
            @RequestParam(defaultValue = "") String description) {
        try {
            ChatRoom newRoom = chatService.createRoom(roomId, roomName, description);
            log.info("새 채팅방 생성: {}", roomId);
            
            return responseFactory.success("채팅방이 성공적으로 생성되었습니다.", newRoom);
        } catch (IllegalArgumentException e) {
            log.warn("채팅방 생성 실패 - 잘못된 요청: {}", e.getMessage());
            return responseFactory.conflict(e.getMessage());
        } catch (Exception e) {
            log.error("채팅방 생성 실패: {}", roomId, e);
            return responseFactory.internalServerError("채팅방 생성 중 오류가 발생했습니다.");
        }
    }

    /**
     * 메시지 처리 메서드 (메시지 지속성 기능 추가)
     * 이제 WebSocket 전송과 동시에 데이터베이스에도 저장됩니다!
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message) {
        try {
            // 시스템 메시지 내용 설정
            if (ChatMessageDTO.MessageType.ENTER.equals(message.getType())) {
                message.setMessage(message.getSender() + "님이 입장하셨습니다.");
                // 데이터베이스에서 참가자 수 관리
                chatService.enterRoom(message.getRoomId(), message.getSender());
            } else if (ChatMessageDTO.MessageType.LEAVE.equals(message.getType())) {
                message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
                // 데이터베이스에서 참가자 수 관리
                chatService.leaveRoom(message.getRoomId(), message.getSender());
            }
            
            // 핵심 기능: 메시지를 데이터베이스에 영구 저장
            // 이것이 메시지 지속성을 가능하게 하는 핵심 코드입니다
            ChatMessage savedMessage = chatService.saveMessage(message);
            
            log.info("메시지 저장 및 전송 완료: 방 ID = {}, 발신자 = {}, 타입 = {}, DB ID = {}", 
                    message.getRoomId(), message.getSender(), message.getType(), savedMessage.getMessageId());
            
            // 실시간 전송 (기존 기능 유지)
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생: 방 ID = {}, 발신자 = {}", 
                    message.getRoomId(), message.getSender(), e);
        }
    }

    /**
     * 파일 업로드 처리 (메시지 지속성 기능 추가)
     * 이제 파일 메시지도 데이터베이스에 저장됩니다
     */
    @PostMapping("/chat/upload-file")
    public ResponseEntity<ApiResponseDTO<String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("roomId") String roomId,
            @RequestParam("sender") String sender,
            @RequestParam("messageType") String messageType) {
        try {
            // 채팅방 존재 여부 확인 (데이터베이스에서)
            if (chatService.getRoomById(roomId).isEmpty()) {
                return responseFactory.badRequest("존재하지 않는 채팅방입니다.");
            }
            
            // 파일 저장
            String fileUrl = localFileStorageService.saveFile(file);

            ChatMessageDTO message = new ChatMessageDTO();
            message.setType(ChatMessageDTO.MessageType.valueOf(messageType));
            message.setRoomId(roomId);
            message.setSender(sender);
            message.setFileUrl(fileUrl);

            // 파일 메시지도 데이터베이스에 저장
            ChatMessage savedMessage = chatService.saveMessage(message);
            
            log.info("파일 메시지 저장 완료: 방 ID = {}, 파일 URL = {}, DB ID = {}", 
                    roomId, fileUrl, savedMessage.getMessageId());
            
            // 실시간 전송
            messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, message);
            
            return responseFactory.success("파일 업로드가 완료되었습니다.", fileUrl);

        } catch (Exception e) {
            log.error("파일 업로드 오류: 방 ID = {}, 파일명 = {}", roomId, file.getOriginalFilename(), e);
            return responseFactory.internalServerError("파일 업로드 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 🎯 핵심 기능: 채팅방의 기존 메시지 히스토리를 조회하는 API
     * 사용자가 채팅방에 입장할 때 이전 메시지들을 불러오는 데 사용됩니다.
     * 이 API가 메시지 지속성 문제를 해결하는 핵심 기능입니다!
     */
    @GetMapping("/chat/rooms/{roomId}/messages")
    public ResponseEntity<ApiResponseDTO<List<ChatMessage>>> getChatMessages(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "50") int limit) {
        try {
            // 채팅방 존재 여부 확인
            if (chatService.getRoomById(roomId).isEmpty()) {
                return responseFactory.notFound("존재하지 않는 채팅방입니다.");
            }
            
            List<ChatMessage> messages;
            if (limit > 0) {
                // 최근 메시지만 조회 (성능 최적화)
                messages = chatService.getRecentMessages(roomId, limit);
            } else {
                // 모든 메시지 조회
                messages = chatService.getMessagesByRoomId(roomId);
            }
            
            log.info("채팅방 {} 메시지 조회 완료: {} 개의 메시지", roomId, messages.size());
            return responseFactory.success("메시지 목록을 성공적으로 가져왔습니다.", messages);
            
        } catch (Exception e) {
            log.error("메시지 조회 실패: 방 ID = {}", roomId, e);
            return responseFactory.internalServerError("메시지를 가져오는 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 채팅방 메시지를 페이지네이션으로 조회하는 API
     * 메시지가 많은 채팅방에서 성능 최적화를 위해 사용됩니다.
     */
    @GetMapping("/chat/rooms/{roomId}/messages/page")
    public ResponseEntity<ApiResponseDTO<Page<ChatMessage>>> getChatMessagesWithPaging(
            @PathVariable String roomId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        try {
            // 채팅방 존재 여부 확인
            if (chatService.getRoomById(roomId).isEmpty()) {
                return responseFactory.notFound("존재하지 않는 채팅방입니다.");
            }
            
            Page<ChatMessage> messagePage = chatService.getMessagesByRoomId(roomId, pageable);
            
            log.info("채팅방 {} 페이지 메시지 조회 완료: 페이지 {}/{}, {} 개의 메시지", 
                    roomId, messagePage.getNumber() + 1, messagePage.getTotalPages(), messagePage.getContent().size());
            
            return responseFactory.success("메시지 페이지를 성공적으로 가져왔습니다.", messagePage);
            
        } catch (Exception e) {
            log.error("메시지 페이지 조회 실패: 방 ID = {}", roomId, e);
            return responseFactory.internalServerError("메시지 페이지를 가져오는 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 채팅방 내에서 키워드로 메시지를 검색하는 API
     */
    @GetMapping("/chat/rooms/{roomId}/messages/search")
    public ResponseEntity<ApiResponseDTO<List<ChatMessage>>> searchMessages(
            @PathVariable String roomId,
            @RequestParam String keyword) {
        try {
            // 채팅방 존재 여부 확인
            if (chatService.getRoomById(roomId).isEmpty()) {
                return responseFactory.notFound("존재하지 않는 채팅방입니다.");
            }
            
            List<ChatMessage> searchResults = chatService.searchMessages(roomId, keyword);
            
            log.info("채팅방 {} 메시지 검색 완료: 키워드 '{}', {} 개의 결과", roomId, keyword, searchResults.size());
            return responseFactory.success("검색 결과를 성공적으로 가져왔습니다.", searchResults);
            
        } catch (IllegalArgumentException e) {
            return responseFactory.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("메시지 검색 실패: 방 ID = {}, 키워드 = {}", roomId, keyword, e);
            return responseFactory.internalServerError("메시지 검색 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 채팅방의 메시지 개수를 조회하는 API
     */
    @GetMapping("/chat/rooms/{roomId}/messages/count")
    public ResponseEntity<ApiResponseDTO<Long>> getMessageCount(@PathVariable String roomId) {
        try {
            // 채팅방 존재 여부 확인
            if (chatService.getRoomById(roomId).isEmpty()) {
                return responseFactory.notFound("존재하지 않는 채팅방입니다.");
            }
            
            long messageCount = chatService.getMessageCount(roomId);
            
            log.info("채팅방 {} 메시지 개수 조회: {} 개", roomId, messageCount);
            return responseFactory.success("메시지 개수를 성공적으로 가져왔습니다.", messageCount);
            
        } catch (Exception e) {
            log.error("메시지 개수 조회 실패: 방 ID = {}", roomId, e);
            return responseFactory.internalServerError("메시지 개수를 가져오는 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 채팅방 이름으로 검색하는 API
     */
    @GetMapping("/chat/rooms/search")
    public ResponseEntity<ApiResponseDTO<List<ChatRoom>>> searchRooms(
            @RequestParam(required = false) String roomName) {
        try {
            List<ChatRoom> searchResults = chatService.searchRoomsByName(roomName);
            
            log.info("채팅방 검색 완료: 키워드 '{}', {} 개의 결과", roomName, searchResults.size());
            return responseFactory.success("검색 결과를 성공적으로 가져왔습니다.", searchResults);
            
        } catch (Exception e) {
            log.error("채팅방 검색 실패: 키워드 = {}", roomName, e);
            return responseFactory.internalServerError("채팅방 검색 중 오류가 발생했습니다.");
        }
    }
}
