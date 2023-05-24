package com.sparrow.chat.contact.protocol.audit;

import com.sparrow.protocol.Param;

/**
 * 好友的审核
 */
public class FriendAuditParam implements Param {
    /**
     * 审核主键ID
     */
    private Long auditId;

    /**
     * 审核原因
     */
    private String reason;
    /**
     * 审核的状态
     */
    private Boolean agree;

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Boolean getAgree() {
        return agree;
    }

    public void setAgree(Boolean agree) {
        this.agree = agree;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
