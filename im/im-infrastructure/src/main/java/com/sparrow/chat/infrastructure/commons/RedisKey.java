package com.sparrow.chat.infrastructure.commons;

public class RedisKey {
    /**
     * 会话内的消息 zset hash
     */
    public static final String SESSION_MESSAGE_KEY = "msg:{sessionKey}";

    /**
     * 用户的会话 zset
     */
    public static final String USER_SESSION_KEY = "session:{userKey}";

    /**
     * 群内用户 zset
     */
    public static final String MEMBER_OF_QUN = "qun:member:{qunId}";

    /**
     * 通讯录zset
     */
    public static final String USER_CONTACTS = "contacts:{chatType}:{userId}";
    /**
     * 群信息 string
     */
    public static final String QUN = "qun:{qunId}";

    public static final String VISITOR_ID = "visitor.id.seed";

}
