package com.sparrow.chat.domain.bo;

import lombok.Data;

@Data
public class SessionBO {
    private Long id;

    private String sessionKey;

    private Long senderId;

    private Integer senderCategory;

    private String senderName;

    private String senderNickName;

    private Long receiverId;

    private Integer receiverCategory;

    private String receiverName;

    private String receiverNickName;

    private String groupName;

    private Integer chatType;

    private Integer isVisitor;

}
