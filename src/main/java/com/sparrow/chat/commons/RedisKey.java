package com.sparrow.chat.commons;

public class RedisKey {
    /**
     * 会话内的消息 redis list
     */
    public static final String MESSAGE_KEY = "msg:{sessionKey}";

    /**
     * 用户的会话 redis list
     */
    public static final String USER_SESSION_KEY = "user.session:{userId}";

    /**
     * 是否已读
     */
    public static final String USER_SESSION_READ = "user.read:{user_id}:{sessionKey}";

    /**
     * 群内用户
     */
    public static final String USER_ID_OF_QUN = "user.qun:{qun_id}";

    /**
     * 通讯录
     */
    public static final String USER_CONTACTS = "contacts:{user_id}:{chat_type}";

    /**
     * 用户信息
     */
    public static final String USER = "user:{user_id}";

    /**
     * 群信息F
     */
    public static final String QUN = "qun:{qun_id}";


}
