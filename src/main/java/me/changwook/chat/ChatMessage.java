package me.changwook.chat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.changwook.common.BaseEntity;

/**
 * 채팅 메시지를 저장하는 엔티티 클래스
 * 개별 메시지의 내용, 발신자, 타입 등을 데이터베이스에 영구 저장합니다.
 */
@Entity
@Table(name = "chat_messages", indexes = {
        @Index(name = "idx_chat_message_room_id", columnList = "room_id"),
        @Index(name = "idx_chat_message_created_at", columnList = "created_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId; // 메시지 고유 식별자

    @Column(name = "room_id", nullable = false, length = 100)
    private String roomId; // 메시지가 속한 채팅방 ID

    @Column(name = "sender", nullable = false, length = 100)
    private String sender; // 메시지 발신자

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 20)
    private MessageType messageType; // 메시지 타입 (텍스트, 이미지, 입장, 퇴장 등)

    @Column(name = "message_content", length = 2000)
    private String messageContent; // 메시지 내용

    @Column(name = "file_url", length = 500)
    private String fileUrl; // 첨부 파일 URL (이미지, 동영상 등)

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl; // 썸네일 URL (동영상의 경우)

    /**
     * 메시지 타입을 정의하는 열거형
     * 각 메시지가 어떤 종류인지 구분하기 위해 사용됩니다.
     */
    public enum MessageType {
        ENTER,    // 사용자 입장 메시지
        TALK,     // 일반 텍스트 메시지
        IMAGE,    // 이미지 첨부 메시지
        VIDEO,    // 동영상 첨부 메시지
        LEAVE     // 사용자 퇴장 메시지
    }

    /**
     * 일반 텍스트 메시지를 생성하는 정적 팩토리 메서드
     * 가장 일반적인 채팅 메시지 생성에 사용됩니다.
     */
    public static ChatMessage createTextMessage(String roomId, String sender, String messageContent) {
        ChatMessage message = new ChatMessage();
        message.roomId = roomId;
        message.sender = sender;
        message.messageType = MessageType.TALK;
        message.messageContent = messageContent;
        return message;
    }

    /**
     * 파일 첨부 메시지를 생성하는 정적 팩토리 메서드
     * 이미지나 동영상을 포함한 메시지 생성에 사용됩니다.
     */
    public static ChatMessage createFileMessage(String roomId, String sender, MessageType messageType, 
                                              String fileUrl, String thumbnailUrl) {
        ChatMessage message = new ChatMessage();
        message.roomId = roomId;
        message.sender = sender;
        message.messageType = messageType;
        message.fileUrl = fileUrl;
        message.thumbnailUrl = thumbnailUrl;
        return message;
    }

    /**
     * 시스템 메시지를 생성하는 정적 팩토리 메서드
     * 사용자 입장/퇴장과 같은 시스템 알림 메시지 생성에 사용됩니다.
     */
    public static ChatMessage createSystemMessage(String roomId, String sender, MessageType messageType, String messageContent) {
        ChatMessage message = new ChatMessage();
        message.roomId = roomId;
        message.sender = sender;
        message.messageType = messageType;
        message.messageContent = messageContent;
        return message;
    }

    /**
     * 메시지 내용이 비어있는지 확인하는 메서드
     * 텍스트와 파일 모두 없는 경우 true를 반환합니다.
     */
    public boolean isEmpty() {
        return (messageContent == null || messageContent.trim().isEmpty()) 
                && (fileUrl == null || fileUrl.trim().isEmpty());
    }

    /**
     * 파일 첨부 메시지인지 확인하는 메서드
     * IMAGE나 VIDEO 타입이며 fileUrl이 존재하는 경우 true를 반환합니다.
     */
    public boolean isFileMessage() {
        return (messageType == MessageType.IMAGE || messageType == MessageType.VIDEO) 
                && fileUrl != null && !fileUrl.trim().isEmpty();
    }
}
