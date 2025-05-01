package com.sparrow.chat.protocol.query;

import lombok.Data;

@Data
public class SessionQuery {
    private String senderName;
    private String receiverName;
    private String senderNickName;
    private String receiverNickName;
    private String groupName;
    private Long beginDate;
    private Long endDate;
}
