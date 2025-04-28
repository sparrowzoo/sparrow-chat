package com.sparrow.chat.contact.protocol.audit;

import com.sparrow.protocol.Param;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 加群
 * group room qun
 */
@ApiModel("加群参数")
@Data
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
}
