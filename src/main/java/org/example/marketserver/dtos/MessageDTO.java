package org.example.marketserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;

    public void setId(Long id) {
        this.id = id;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

}
