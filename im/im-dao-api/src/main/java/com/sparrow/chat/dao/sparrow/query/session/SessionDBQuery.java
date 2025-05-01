package com.sparrow.chat.dao.sparrow.query.session;

import lombok.Data;

@Data
public class SessionDBQuery {
    private String senderName;
    private String receiverName;
    private String senderNickName;
    private String receiverNickName;
    private String groupName;
    private Long beginDate;
    private Long endDate;
}
