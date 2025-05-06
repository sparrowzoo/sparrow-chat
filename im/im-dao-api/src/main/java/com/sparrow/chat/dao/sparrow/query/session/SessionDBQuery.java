package com.sparrow.chat.dao.sparrow.query.session;

import lombok.Data;

@Data
public class SessionDBQuery {
    private Long userId;
    private String userName;
    private String userNickName;
    private Long beginDate;
    private Long endDate;
}
