package com.sparrow.chat.infrastructure.commons;

public class RedisKey {
    /**
     * 会话内的消息 redis list
     */
    public static final String SESSION_MESSAGE_KEY = "session:msg:{sessionKey}";

    /**
     * 用户的会话 redis list
     */
    public static final String USER_SESSION_KEY = "user.session:{userKey}";

    /**
     * 是否已读
     */
    public static final String USER_SESSION_READ = "user.read:{userKey}:{sessionKey}";

    /**
     * 群内用户
     */
    public static final String USER_ID_OF_QUN = "user.qun:{qunId}";

    /**
     * 通讯录
     */
    public static final String USER_CONTACTS = "contacts:{chatType}:{userId}";

    /**
     * 用户信息
     */
    public static final String USER = "user:{userId}";

    /**
     * 群信息
     */
    public static final String QUN = "qun:{qunId}";

    public static final String VISITOR_ID = "visitor.id.seed";

}
