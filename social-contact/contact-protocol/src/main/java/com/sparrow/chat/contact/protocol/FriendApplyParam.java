package com.sparrow.chat.contact.protocol;

import com.sparrow.protocol.Param;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 好友申请
 */
@ApiModel("申请好友Param")
public class FriendApplyParam implements Param {
    /**
     * 用户的密秘标识
     */
    @ApiModelProperty("好友的密文标识")
    private String friendSecretIdentify;
    /**
     * 申请的理由
     */
    @ApiModelProperty("申请好友的理由")
    private String reason;

    public String getFriendSecretIdentify() {
        return friendSecretIdentify;
    }

    public void setFriendSecretIdentify(String friendSecretIdentify) {
        this.friendSecretIdentify = friendSecretIdentify;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
