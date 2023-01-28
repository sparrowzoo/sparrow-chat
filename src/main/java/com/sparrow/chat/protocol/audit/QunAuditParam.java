package com.sparrow.chat.protocol.audit;

import com.sparrow.protocol.Param;

/**
 * 群的审核
 */
public class QunAuditParam implements Param {
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
    private Boolean status;

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
