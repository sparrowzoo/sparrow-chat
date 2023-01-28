package com.sparrow.chat.protocol.audit;

import com.sparrow.protocol.Param;

/**
 * 好友申请
 */
public class FriendApplyParam implements Param {
    /**
     * 好友的ID
     */
    private Long friendId;
    /**
     * 申请的理由
     */
    private String reason;

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
