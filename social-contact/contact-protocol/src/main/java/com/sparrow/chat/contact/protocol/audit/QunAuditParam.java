package com.sparrow.chat.contact.protocol.audit;

import com.sparrow.protocol.Param;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 群的审核
 */
@ApiModel("加群审核")
@Data
public class QunAuditParam implements Param {

    @ApiModelProperty("审核ID")
    private Long auditId;

    @ApiModelProperty("审核理由")
    private String reason;

    @ApiModelProperty("是否同意")
    private Boolean isAgree;
}
