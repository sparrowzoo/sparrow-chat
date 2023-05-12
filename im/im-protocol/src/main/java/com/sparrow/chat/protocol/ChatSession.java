package com.sparrow.chat.protocol;

import com.sparrow.protocol.constant.magic.Symbol;
import java.util.Arrays;

import static com.sparrow.chat.protocol.constant.Chat.CHAT_TYPE_1_2_1;
import static com.sparrow.chat.protocol.constant.Chat.CHAT_TYPE_1_2_N;
import static com.sparrow.chat.protocol.constant.Chat.USER_NOT_FOUND;

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
    public static ChatSession create1To1CancelSession(int sender, String sessionKey) {
        return new ChatSession(CHAT_TYPE_1_2_1, sender, null, sessionKey);
    }

    public static ChatSession create1To1Session(int sender, int receiver) {
        return new ChatSession(CHAT_TYPE_1_2_1, sender, receiver, null);
    }

    public static ChatSession createQunSession(int sender, String sessionKey) {
        return new ChatSession(CHAT_TYPE_1_2_N, sender, null, sessionKey);
    }

    private ChatSession(int chatType, int sender, Integer receiver, String sessionKey) {
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

    private String generateKey(Integer sender, Integer receiver) {
        Integer[] userArray = new Integer[2];
        userArray[0] = sender;
        userArray[1] = receiver;
        Arrays.sort(userArray);
        return userArray[0] + "_" + userArray[1];
    }

    public Integer getOppositeUser(Integer currentUserId) {
        if (this.chatType != CHAT_TYPE_1_2_1) {
            return USER_NOT_FOUND;
        }

        String[] userIdArray = this.sessionKey.split(Symbol.UNDERLINE);
        if (userIdArray.length != 2) {
            return USER_NOT_FOUND;
        }
        Integer userId1 = Integer.parseInt(userIdArray[0]);
        Integer userId2 = Integer.parseInt(userIdArray[1]);
        if (userId1.equals(currentUserId)) {
            return userId2;
        }
        if (userId2.equals(currentUserId)) {
            return userId1;
        }
        return USER_NOT_FOUND;
    }
}
