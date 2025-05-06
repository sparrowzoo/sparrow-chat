package com.sparrow.chat.protocol.dto;

import lombok.Data;

@Data
public class SessionMetaDTO {
    private Long id;

    private String sessionKey;

    private Long userId;

    private Integer userCategory;

    private String userName;

    private String userNickName;

    private Long oppositeId;

    private Integer oppositeCategory;

    private String oppositeName;

    private String oppositeNickName;

    private Integer isVisitor;

    private Long gmtCreate;

}
