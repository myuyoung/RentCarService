package me.changwook.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.ChatMessageDTO;
import me.changwook.service.impl.LocalFileStorageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    // S3UploadService 대신 LocalFileStorageService 주입
    private final LocalFileStorageService localFileStorageService;

    // 텍스트 메시지 처리 (변경 없음)
    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message) {
        if (ChatMessageDTO.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    // 파일 업로드 처리 (서비스 호출 부분만 변경)
    @PostMapping("/chat/upload-file")
    public void uploadFile(@RequestParam("file") MultipartFile file,
                           @RequestParam("roomId") String roomId,
                           @RequestParam("sender") String sender,
                           @RequestParam("messageType") String messageType) {
        try {
            // S3 서비스 대신 로컬 저장 서비스 호출
            String fileUrl = localFileStorageService.saveFile(file);

            ChatMessageDTO message = new ChatMessageDTO();
            message.setType(ChatMessageDTO.MessageType.valueOf(messageType));
            message.setRoomId(roomId);
            message.setSender(sender);
            message.setFileUrl(fileUrl);

            log.info("File saved locally and message sent to /sub/chat/room/{}", roomId);
            messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, message);

        } catch (Exception e) {
            log.error("File upload error", e);
            // 에러 처리 로직
        }
    }
}
