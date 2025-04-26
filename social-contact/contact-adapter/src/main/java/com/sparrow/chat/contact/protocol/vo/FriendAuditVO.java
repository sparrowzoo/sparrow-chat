package com.sparrow.chat.contact.protocol.vo;

import lombok.Data;

@Data
public class FriendAuditVO {
    /**
     * 审核记录ID
     */
    private Long auditId;

    private Long objectId;

    private Integer objectType;


    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    private Long applyTime;
    private Long auditTime;

    private String remark;
}
