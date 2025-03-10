package com.sparrow.chat.domain.bo;

public class MessageKey {
    public MessageKey(ChatUser fromUser, Long clientSendTime) {
        this.fromUserId = fromUser.getId();
        this.userRole = fromUser.getCategory();
        this.clientSendTime = clientSendTime;
    }

    private String fromUserId;
    private Integer userRole;
    private Long clientSendTime;

    public String key() {
        return fromUserId + "-" + this.clientSendTime + "-" + this.userRole;
    }
}
