package com.sparrow.chat.commons;

import com.sparrow.support.PropertyAccessor;

public class PropertyAccessBuilder {
    public static class ChatPropertyAccessor implements PropertyAccessor {
        private String session;
        private Integer userId;
        private String qunId;
        private Byte chatType;

        public ChatPropertyAccessor(String sessionKey, Integer userId, String qunId, Byte chatType) {
            this.session = sessionKey;
            this.userId = userId;
            this.qunId = qunId;
            this.chatType = chatType;
        }

        @Override public Object getProperty(String s) {
            if (s.equals("sessionKey")) {
                return this.session;
            }
            if (s.equals("userId")) {
                return this.userId;
            }
            if (s.equals("qunId")) {
                return this.qunId;
            }
            if (s.equals("chatType")) {
                return this.chatType;
            }
            return "";
        }
    }

    public static PropertyAccessor buildChatPropertyAccessor(String sessionKey, Integer userId) {
        return new ChatPropertyAccessor(sessionKey, userId, null, null);
    }

    public static PropertyAccessor buildChatPropertyAccessorBySessionKey(String sessionKey) {
        return new ChatPropertyAccessor(sessionKey, null, null, null);
    }

    public static PropertyAccessor buildChatPropertyAccessorByUserId(Integer userId) {
        return new ChatPropertyAccessor(null, userId, null, null);
    }

    public static PropertyAccessor buildChatPropertyAccessorByQunId(String qunId) {
        return new ChatPropertyAccessor(null, null, qunId, null);
    }

    public static PropertyAccessor buildContacts(Integer userId, Byte charType) {
        return new ChatPropertyAccessor(null, userId, null, charType);
    }
}
