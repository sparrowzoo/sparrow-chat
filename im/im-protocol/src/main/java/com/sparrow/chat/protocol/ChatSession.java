package com.sparrow.chat.protocol;

import com.sparrow.protocol.constant.magic.Symbol;

import static com.sparrow.chat.protocol.constant.Chat.*;

/**
 * 会话不应该与用户相关
 * 方便session查重
 * <p>
 * 方便1对1 聊天的使用
 */
public class ChatSession {
    public ChatSession() {
    }

    /**
     * 生成会话key
     *
     * @param sender
     * @param sessionKey
     * @return
     */
    public static ChatSession create1To1CancelSession(ChatUser sender, String sessionKey) {
        return new ChatSession(CHAT_TYPE_1_2_1, sender, null, sessionKey);
    }

    public static ChatSession create1To1Session(ChatUser sender, ChatUser receiver) {
        return new ChatSession(CHAT_TYPE_1_2_1, sender, receiver, null);
    }

    public static ChatSession createQunSession(ChatUser sender, String sessionKey) {
        return new ChatSession(CHAT_TYPE_1_2_N, sender, null, sessionKey);
    }

    private ChatSession(int chatType, ChatUser sender, ChatUser receiver, String sessionKey) {
        this.chatType = chatType;
        if (sessionKey != null) {
            this.sessionKey = sessionKey;
            return;
        }
        if (chatType == CHAT_TYPE_1_2_1) {
            this.sessionKey = this.generateKey(sender, receiver);
        }
    }

    private int chatType;
    private String sessionKey;

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String json() {
        return "{" +
                "'chatType':" + chatType +
                ",'sessionKey':'" + sessionKey + '\'' +
                '}';
    }

    public boolean isOne2One() {
        return this.chatType == CHAT_TYPE_1_2_1;
    }

    private String generateKey(ChatUser sender, ChatUser receiver) {
        if (sender == null || receiver == null) {
            return "";
        }
        //保证从小到大排序
        if (sender.getId().compareTo(receiver.getId()) <= 0) {
            return sender.key() + "-" + receiver.key();
        }
        return receiver.key() + "-" + sender.key();
    }

    public ChatUser getOppositeUser(ChatUser currentUser) {
        if (currentUser == null) {
            return null;
        }
        if (this.chatType != CHAT_TYPE_1_2_1) {
            return null;
        }

        String[] userIdArray = this.sessionKey.split(Symbol.HORIZON_LINE);
        if (userIdArray.length != 2) {
            return null;
        }
        ChatUser userId1 = ChatUser.parse(userIdArray[0]);
        ChatUser userId2 = ChatUser.parse(userIdArray[1]);
        if (currentUser.equals(userId1)) {
            return userId2;
        }
        if (currentUser.equals(userId2)) {
            return userId1;
        }
        return null;
    }
}
