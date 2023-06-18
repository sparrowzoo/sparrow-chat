package com.sparrow.chat.contact.protocol.audit;

import com.sparrow.protocol.Param;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 群的审核
 */
@ApiModel("加群审核")
public class QunAuditParam implements Param {

    @ApiModelProperty("审核ID")
    private Long auditId;

    @ApiModelProperty("审核理由")
    private String reason;

    @ApiModelProperty("是否同意")
    private Boolean isAgree;

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }


    public Boolean getAgree() {
        return isAgree;
    }

    public void setAgree(Boolean agree) {
        isAgree = agree;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
