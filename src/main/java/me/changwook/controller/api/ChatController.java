package me.changwook.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.ChatMessageDTO;
import me.changwook.service.impl.LocalFileStorageService;
import me.changwook.util.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    // S3UploadService 대신 LocalFileStorageService 주입
    private final LocalFileStorageService localFileStorageService;
    private final ResponseFactory responseFactory;
    
    // 채팅방 목록을 임시로 메모리에 저장 (실제 프로젝트에서는 DB 사용)
    private final Map<String, ChatRoom> chatRooms = new HashMap<>();
    
    // 채팅방 정보를 담는 내부 클래스
    public static class ChatRoom {
        private String roomId;
        private String roomName;
        private String description;
        private int participantCount;
        private Date createdAt;
        
        public ChatRoom(String roomId, String roomName, String description) {
            this.roomId = roomId;
            this.roomName = roomName;
            this.description = description;
            this.participantCount = 0;
            this.createdAt = new Date();
        }
        
        // Getters and Setters
        public String getRoomId() { return roomId; }
        public void setRoomId(String roomId) { this.roomId = roomId; }
        
        public String getRoomName() { return roomName; }
        public void setRoomName(String roomName) { this.roomName = roomName; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public int getParticipantCount() { return participantCount; }
        public void setParticipantCount(int participantCount) { this.participantCount = participantCount; }
        
        public Date getCreatedAt() { return createdAt; }
        public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    }
    
    // 초기 채팅방 설정
    {
        chatRooms.put("general", new ChatRoom("general", "일반 채팅", "자유롭게 대화할 수 있는 공간입니다"));
        chatRooms.put("support", new ChatRoom("support", "고객지원", "문의사항이나 도움이 필요할 때 이용해주세요"));
        chatRooms.put("rental", new ChatRoom("rental", "렌터카 정보", "렌터카 관련 정보를 공유하는 공간입니다"));
    }

    /**
     * 채팅방 목록을 반환하는 API
     * 이 메서드가 사용자가 경험한 "모집방 리스트를 찾을 수 없습니다" 에러를 해결합니다
     */
    @GetMapping("/chat/rooms")
    public ResponseEntity<ApiResponseDTO<List<ChatRoom>>> getChatRooms() {
        try {
            List<ChatRoom> roomList = new ArrayList<>(chatRooms.values());
            log.info("채팅방 목록 조회 성공: {} 개의 방", roomList.size());
            return responseFactory.success("채팅방 목록을 성공적으로 가져왔습니다.", roomList);
        } catch (Exception e) {
            log.error("채팅방 목록 조회 실패", e);
            return responseFactory.internalServerError("모집방 리스트를 찾을 수 없습니다.");
        }
    }
    
    /**
     * 특정 채팅방 정보를 반환하는 API
     */
    @GetMapping("/chat/rooms/{roomId}")
    public ResponseEntity<ApiResponseDTO<ChatRoom>> getChatRoom(@PathVariable String roomId) {
        try {
            ChatRoom chatRoom = chatRooms.get(roomId);
            if (chatRoom == null) {
                return responseFactory.notFound("해당 채팅방을 찾을 수 없습니다.");
            }
            return responseFactory.success("채팅방 정보를 성공적으로 가져왔습니다.", chatRoom);
        } catch (Exception e) {
            log.error("채팅방 정보 조회 실패: {}", roomId, e);
            return responseFactory.internalServerError("채팅방 정보를 가져오는 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 새로운 채팅방을 생성하는 API
     */
    @PostMapping("/chat/rooms")
    public ResponseEntity<ApiResponseDTO<ChatRoom>> createChatRoom(
            @RequestParam String roomId,
            @RequestParam String roomName,
            @RequestParam(defaultValue = "") String description) {
        try {
            if (chatRooms.containsKey(roomId)) {
                return responseFactory.conflict("이미 존재하는 채팅방 ID입니다.");
            }
            
            ChatRoom newRoom = new ChatRoom(roomId, roomName, description);
            chatRooms.put(roomId, newRoom);
            log.info("새 채팅방 생성: {}", roomId);
            
            return responseFactory.success("채팅방이 성공적으로 생성되었습니다.", newRoom);
        } catch (Exception e) {
            log.error("채팅방 생성 실패: {}", roomId, e);
            return responseFactory.internalServerError("채팅방 생성 중 오류가 발생했습니다.");
        }
    }

    /**
     * 텍스트 메시지 처리
     * WebSocket을 통한 실시간 메시지 전송을 처리합니다
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message) {
        try {
            if (ChatMessageDTO.MessageType.ENTER.equals(message.getType())) {
                message.setMessage(message.getSender() + "님이 입장하셨습니다.");
                // 참가자 수 증가
                ChatRoom room = chatRooms.get(message.getRoomId());
                if (room != null) {
                    room.setParticipantCount(room.getParticipantCount() + 1);
                }
            } else if (ChatMessageDTO.MessageType.LEAVE.equals(message.getType())) {
                message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
                // 참가자 수 감소
                ChatRoom room = chatRooms.get(message.getRoomId());
                if (room != null) {
                    room.setParticipantCount(Math.max(0, room.getParticipantCount() - 1));
                }
            }
            
            log.info("메시지 전송: 방 ID = {}, 발신자 = {}, 타입 = {}", 
                    message.getRoomId(), message.getSender(), message.getType());
            
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생", e);
        }
    }

    /**
     * 파일 업로드 처리
     * 채팅방에서 이미지나 비디오 파일을 업로드할 때 사용됩니다
     */
    @PostMapping("/chat/upload-file")
    public ResponseEntity<ApiResponseDTO<String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("roomId") String roomId,
            @RequestParam("sender") String sender,
            @RequestParam("messageType") String messageType) {
        try {
            // 채팅방 존재 여부 확인
            if (!chatRooms.containsKey(roomId)) {
                return responseFactory.badRequest("존재하지 않는 채팅방입니다.");
            }
            
            // S3 서비스 대신 로컬 저장 서비스 호출
            String fileUrl = localFileStorageService.saveFile(file);

            ChatMessageDTO message = new ChatMessageDTO();
            message.setType(ChatMessageDTO.MessageType.valueOf(messageType));
            message.setRoomId(roomId);
            message.setSender(sender);
            message.setFileUrl(fileUrl);

            log.info("파일 저장 완료 및 메시지 전송: 방 ID = {}, 파일 URL = {}", roomId, fileUrl);
            messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, message);
            
            return responseFactory.success("파일 업로드가 완료되었습니다.", fileUrl);

        } catch (Exception e) {
            log.error("파일 업로드 오류: 방 ID = {}, 파일명 = {}", roomId, file.getOriginalFilename(), e);
            return responseFactory.internalServerError("파일 업로드 중 오류가 발생했습니다.");
        }
    }
}
