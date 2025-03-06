package com.sparrow.chat.protocol.param;

import com.sparrow.chat.protocol.ChatUser;

public class MessageReadParam {
    private ChatUser chatUser;
    private String sessionKey;

    public ChatUser getUser() {
        return chatUser;
    }

    public void setUser(ChatUser user) {
        this.chatUser = user;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

}
