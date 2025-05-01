package com.sparrow.chat.domain.bo;

import lombok.Data;

@Data
public class SessionBO {
    private Long id;

    private String userId;

    private String senderName;

    private String senderNickName;

    private String receiverName;

    private String receiverNickName;

    private String groupName;

    private Integer category;

    private String sessionKey;

    private Integer chatType;

    private Long gmtCreate;

    private Long lastReadTime;
}
