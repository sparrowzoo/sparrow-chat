package com.sparrow.chat.contact.protocol.audit;

import com.sparrow.protocol.Param;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 好友的审核
 */
@ApiModel("好友审核")
public class FriendAuditParam implements Param {

    @ApiModelProperty("审核主键ID")
    private Long auditId;

    @ApiModelProperty("审核原因")
    private String reason;

    @ApiModelProperty("是否同意")
    private Boolean isAgree;

    public Boolean getAgree() {
        return isAgree;
    }

    public void setAgree(Boolean agree) {
        isAgree = agree;
    }

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
