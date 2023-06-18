package com.sparrow.chat.contact.protocol.qun;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("邀请好友")
public class InviteFriendParam {
    @ApiModelProperty("群ID")
    private Long qunId;

    @ApiModelProperty("好友ID")
    private Long friendId;

    public Long getQunId() {
        return qunId;
    }

    public void setQunId(Long qunId) {
        this.qunId = qunId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
}
