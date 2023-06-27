package com.sparrow.chat.contact.protocol.qun;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("移除群成员")
public class RemoveMemberOfQunParam {
    public RemoveMemberOfQunParam(Long qunId, Long memberId) {
        this.qunId = qunId;
        this.memberId = memberId;
    }

    @ApiModelProperty("群ID")
    private Long qunId;
    @ApiModelProperty("成员ID")
    private Long memberId;

    public Long getQunId() {
        return qunId;
    }

    public void setQunId(Long qunId) {
        this.qunId = qunId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
