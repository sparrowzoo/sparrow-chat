package com.sparrow.chat.protocol.query;

import lombok.Data;

@Data
public class MessageQuery {
    private Integer chatType;
    private String sessionKey;
    private Long lastReadTime;
}
