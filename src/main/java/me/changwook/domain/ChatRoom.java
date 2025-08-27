package me.changwook.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 채팅방 정보를 저장하는 엔티티 클래스
 * 채팅방의 기본 정보와 메타데이터를 관리합니다.
 */
@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @Column(name = "room_id", length = 100)
    private String roomId; // 채팅방의 고유 식별자

    @Column(name = "room_name", nullable = false, length = 200)
    private String roomName; // 채팅방 이름

    @Column(name = "description", length = 500)
    private String description; // 채팅방 설명

    @Column(name = "participant_count")
    private Integer participantCount; // 현재 참가자 수

    @Column(name = "is_active")
    private Boolean isActive; // 채팅방 활성 상태

    /**
     * 채팅방을 생성하는 정적 팩토리 메서드
     * 이 메서드를 통해 일관된 방식으로 채팅방 객체를 생성할 수 있습니다.
     */
    public static ChatRoom createChatRoom(String roomId, String roomName, String description) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = roomId;
        chatRoom.roomName = roomName;
        chatRoom.description = description;
        chatRoom.participantCount = 0;
        chatRoom.isActive = true;
        return chatRoom;
    }

    /**
     * 참가자 수를 증가시키는 메서드
     * 사용자가 채팅방에 입장할 때 호출됩니다.
     */
    public void incrementParticipantCount() {
        this.participantCount = this.participantCount == null ? 1 : this.participantCount + 1;
    }

    /**
     * 참가자 수를 감소시키는 메서드
     * 사용자가 채팅방에서 퇴장할 때 호출됩니다.
     */
    public void decrementParticipantCount() {
        this.participantCount = Math.max(0, this.participantCount == null ? 0 : this.participantCount - 1);
    }

    /**
     * 채팅방을 비활성화하는 메서드
     * 필요한 경우 채팅방을 임시적으로 사용 중단할 수 있습니다.
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * 채팅방을 활성화하는 메서드
     */
    public void activate() {
        this.isActive = true;
    }
}
