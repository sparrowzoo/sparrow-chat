package com.sparrow.chat.contact.protocol.event;

import com.sparrow.mq.MQEvent;
import lombok.Data;

@Data
public class ContactEvent implements MQEvent {
    public ContactEvent(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    private Long userId;

    private Long friendId;
}
