package com.sparrow.chat.protocol.query;

import lombok.Data;

@Data
public class MessageCancelQuery {
    private Long clientSendTime;
    private String sessionKey;
}
