package com.sparrow.chat.protocol;

public class MessageKey {
    public MessageKey(Integer fromUserId, Long clientSendTime) {
        this.fromUserId = fromUserId;
        this.clientSendTime = clientSendTime;
    }

    private Integer fromUserId;
    private Long clientSendTime;

    public String key(){
        return fromUserId+"-"+this.clientSendTime;
    }
}
