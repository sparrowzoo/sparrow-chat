package com.sparrow.chat.protocol.query;

import lombok.Data;

@Data
public class MessageQuery {
    private String sessionKey;
    private Long lastMessageId;
    private String content;
    private String beginDate;
    private String endDate;
}
