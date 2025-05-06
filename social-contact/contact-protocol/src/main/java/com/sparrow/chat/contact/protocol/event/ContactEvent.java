package com.sparrow.chat.contact.protocol.event;

import com.sparrow.mq.MQEvent;
import lombok.Data;

@Data
public class ContactEvent implements MQEvent {
    public ContactEvent(Long userId) {
        this.userId = userId;
    }

    public ContactEvent(Long userId,Long friendId,Long addTime) {
        this.userId = userId;
        this.friendId = friendId;
        this.addTime = addTime;
    }

    private Long userId;

    private Integer category;

    private Long friendId;

    private Integer friendCategory;

    private Long addTime;
}
