package me.changwook.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {

    public enum MessageType {
        ENTER,TALK,IMAGE,VIDEO,LEAVE;
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private String fileUrl;
    private String thumbnailUrl;
}
