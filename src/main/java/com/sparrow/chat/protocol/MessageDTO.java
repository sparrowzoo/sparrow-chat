package com.sparrow.chat.protocol;

public class MessageDTO {
    private int messageType;
    private int charType;
    private int fromUserId;
    private int targetUserId;
    private String session;
    private String content;
    private Long sendTime;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getCharType() {
        return charType;
    }

    public void setCharType(int charType) {
        this.charType = charType;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public String json() {
        return "{" +
            "'messageType':" + messageType +
            ", 'chatType':" + charType +
            ", 'fromUserId':" + fromUserId +
            ", 'targetUserId':" + targetUserId +
            ", 'session':'" + session + '\'' +
            ", 'content':'" + content + '\'' +
            ", 'sendTime':" + sendTime +
            '}';
    }
}
