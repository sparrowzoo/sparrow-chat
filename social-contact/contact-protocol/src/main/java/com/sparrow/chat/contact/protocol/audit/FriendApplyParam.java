package com.sparrow.chat.contact.protocol.audit;

import com.sparrow.protocol.Param;

/**
 * 好友申请
 */
public class FriendApplyParam implements Param {
    /**
     * 用户的密秘标识
     */
    private String friendSecretIdentify;
    /**
     * 申请的理由
     */
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
