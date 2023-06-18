package com.sparrow.chat.contact.bo;

public class FriendApplyBO {
    public FriendApplyBO(Long currentUserId, Long friendId, String reason) {
        this.currentUserId = currentUserId;
        this.friendId = friendId;
        this.reason = reason;
    }

    private Long currentUserId;
    private Long friendId;

    private String reason;

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

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
