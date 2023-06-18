package com.sparrow.chat.contact.protocol.qun;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("移除群成员")
public class TransferOwnerOfQunParam {
    @ApiModelProperty("群ID")
    private Long qunId;
    @ApiModelProperty("新群主")
    private Long newOwnerId;

    public Long getQunId() {
        return qunId;
    }

    public void setQunId(Long qunId) {
        this.qunId = qunId;
    }

    public Long getNewOwnerId() {
        return newOwnerId;
    }

    public void setNewOwnerId(Long newOwnerId) {
        this.newOwnerId = newOwnerId;
    }
}
