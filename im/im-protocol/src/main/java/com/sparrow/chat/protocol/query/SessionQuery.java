package com.sparrow.chat.protocol.query;

import lombok.Data;

@Data
public class SessionQuery {
    private Long userId;
    private String userName;
    private String userNickName;
    private String beginDate;
    private String endDate;
}
