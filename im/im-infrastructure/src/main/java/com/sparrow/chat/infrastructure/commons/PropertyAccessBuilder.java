package com.sparrow.chat.infrastructure.commons;

import com.sparrow.support.PropertyAccessor;

public class PropertyAccessBuilder {
    public static class ChatPropertyAccessor implements PropertyAccessor {
        ChatPropertyAccessor(Builder builder) {
            this.session = builder.session;
            this.userId = builder.userId;
            this.qunId = builder.qunId;
            this.chatType = builder.chatType;
            this.time = builder.time;
        }

        private String session;
        private Long userId;
        private String qunId;
        private Byte chatType;
        private Long time;

        public static class Builder {
            private String session;
            private Long userId;
            private String qunId;
            private Byte chatType;
            private Long time;
            private String userKey;

            public Builder session(String session) {
                this.session = session;
                return this;
            }

            public Builder userId(Long userId) {
                this.userId = userId;
                return this;
            }

            public Builder qunId(String qunId) {
                this.qunId = qunId;
                return this;
            }

            public Builder chatType(Byte chatType) {
                this.chatType = chatType;
                return this;
            }

            public Builder time(Long time) {
                this.time = time;
                return this;
            }

            public PropertyAccessor build() {
                return new ChatPropertyAccessor(this);
            }
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
            if (s.equals("time")) {
                return this.time;
            }
            return "";
        }

    }

    public static PropertyAccessor buildBySessionAndUserId(String sessionKey, Long userId) {
        return new ChatPropertyAccessor.Builder()
            .session(sessionKey)
            .userId(userId).build();
    }

    public static PropertyAccessor buildBySessionKey(String sessionKey) {
        return new ChatPropertyAccessor.Builder()
            .session(sessionKey).build();
    }

    public static PropertyAccessor buildByUserId(Long userId) {
        return new ChatPropertyAccessor.Builder()
            .userId(userId).build();
    }

    public static PropertyAccessor buildByQunId(String qunId) {
        return new ChatPropertyAccessor.Builder()
            .qunId(qunId).build();
    }

    public static PropertyAccessor buildContacts(Long userId, Byte charType) {
        return new ChatPropertyAccessor.Builder()
            .chatType(charType)
            .userId(userId).build();
    }

    public static PropertyAccessor buildMsgId(Long userId, Long time) {
        return new ChatPropertyAccessor.Builder()
            .time(time)
            .userId(userId).build();
    }
}
