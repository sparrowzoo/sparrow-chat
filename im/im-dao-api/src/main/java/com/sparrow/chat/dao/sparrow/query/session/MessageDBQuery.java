package com.sparrow.chat.dao.sparrow.query.session;

import lombok.Data;

@Data
public class MessageDBQuery {
    private String sessionKey;
    private Long lastReadTime;
    private String content;
}
