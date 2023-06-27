package com.sparrow.chat.contact.protocol.audit;

import com.sparrow.protocol.Param;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 加群
 */
@ApiModel("加群参数")
public class JoinQunParam implements Param {
    /**
     * 用户的密秘标识
     */
    @ApiModelProperty("群id")
    private Long qunId;
    /**
     * 申请的理由
     */
    @ApiModelProperty("加群的理由")
    private String reason;

    public Long getQunId() {
        return qunId;
    }

    public void setQunId(Long qunId) {
        this.qunId = qunId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
