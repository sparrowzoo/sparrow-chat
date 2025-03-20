package com.sparrow.chat.protocol.query;

import lombok.Data;

@Data
public class MessageQuery {
    private String sessionKey;
    private Long lastReadTime;
}
