package com.sparrow.chat.protocol;

import com.sparrow.chat.commons.Chat;
import java.util.Arrays;

public class ChatSession {
    public ChatSession() {
    }


    public static ChatSession create1To1Session(int me,int target){
        return new ChatSession(Chat.CHAT_TYPE_1_2_1,me,target,null);
    }

    public static ChatSession createQunSession(int me,String sessionKey){
        return new ChatSession(Chat.CHAT_TYPE_1_2_N,me,null,sessionKey);
    }

    private ChatSession(int chatType, int me, Integer target, String sessionKey) {
        this.chatType = chatType;
        this.target = target;
        this.me = me;
        if (chatType == Chat.CHAT_TYPE_1_2_1) {
            this.sessionKey = this.generateKey();
        } else {
            this.sessionKey = sessionKey;
        }
    }

    private int chatType;
    private Integer me;//我方
    private Integer target;
    private String sessionKey;

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public int getMe() {
        return me;
    }

    public void setMe(int me) {
        this.me = me;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String json() {
        return "{" +
            "'chatType':" + chatType +
            ", 'me':" + me +
            ", 'target':" + target +
            ", 'sessionKey':'" + sessionKey + '\'' +
            '}';
    }

    private String generateKey() {
        Integer[] userArray = new Integer[2];
        userArray[0] = this.target;
        userArray[1] = me;
        Arrays.sort(userArray);
        return userArray[0] + "_" + userArray[1];
    }
}
