package com.sparrow.chat.protocol;

public class MessageReadParam {

    private Integer me;
    private Integer chatType;
    private String target;
    private Long t;

    public Integer getMe() {
        return me;
    }

    public void setMe(Integer me) {
        this.me = me;
    }

    public Integer getChatType() {
        return chatType;
    }

    public void setChatType(Integer chatType) {
        this.chatType = chatType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Long getT() {
        return t;
    }

    public void setT(Long t) {
        this.t = t;
    }
}
