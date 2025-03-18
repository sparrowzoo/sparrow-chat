package com.sparrow.chat.domain.bo;

import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.protocol.constant.magic.Symbol;

import static com.sparrow.chat.protocol.constant.Chat.CHAT_TYPE_1_2_1;
import static com.sparrow.chat.protocol.constant.Chat.CHAT_TYPE_1_2_N;

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

    public static ChatSession createSession(Integer chatType,String id) {
        return new ChatSession(chatType, null,null, id);
    }

    public static ChatSession create1To1Session(ChatUser sender, ChatUser receiver) {
        return new ChatSession(CHAT_TYPE_1_2_1, sender, receiver, null);
    }

    public static ChatSession createQunSession(ChatUser sender, String id) {
        return new ChatSession(CHAT_TYPE_1_2_N, sender, null, id);
    }

    private ChatSession(int chatType, ChatUser sender, ChatUser receiver, String id) {
        this.chatType = chatType;
        if (id != null) {
            this.id = id;
            return;
        }
        if (chatType == CHAT_TYPE_1_2_1) {
            this.id = this.generateId(sender, receiver);
        }
    }

    private int chatType;
    private String id;

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String key() {
        return chatType+ id;
    }

    public boolean isOne2One() {
        return this.chatType == CHAT_TYPE_1_2_1;
    }

    private String generateId(ChatUser sender, ChatUser receiver) {
        if (sender == null || receiver == null) {
            return "";
        }
        //保证从小到大排序
        if (sender.getId().compareTo(receiver.getId()) <= 0) {
            return sender.key() + "-" + receiver.key();
        }
        return receiver.key() + "-" + sender.key();
    }

    public ChatSession parse(String sessionKey) {
        if (sessionKey == null) {
            return null;
        }
        int chatType = Integer.parseInt(sessionKey.substring(0, 1));
        String id = sessionKey.substring(1);
        return ChatSession.createSession(chatType, id);
    }

    public ChatUser getOppositeUser(ChatUser currentUser) {
        if (currentUser == null) {
            return null;
        }
        if (this.chatType != CHAT_TYPE_1_2_1) {
            return null;
        }

        String[] userIdArray = this.id.split(Symbol.HORIZON_LINE);
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

    public SessionDTO toSessionDTO() {
        return new SessionDTO(this.chatType, this.getId(), 0L);
    }
}
