package com.sparrow.chat.protocol;

public class MessageDTO {
    private int messageType;
    private int chatType;
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

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
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
            ", 'chatType':" + chatType +
            ", 'fromUserId':" + fromUserId +
            ", 'targetUserId':" + targetUserId +
            ", 'session':'" + session + '\'' +
            ", 'content':'" + content + '\'' +
            ", 'sendTime':" + sendTime +
            '}';
    }
}
