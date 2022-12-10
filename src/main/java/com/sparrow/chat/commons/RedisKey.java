package com.sparrow.chat.commons;

public class RedisKey {
    /**
     * 会话内的消息 redis list
     */
    public static final String MESSAGE_KEY = "msg:{sessionKey}";

    public static final String IMAGE_ID_KEY = "msg_img_id:{userId}:{time}";

    /**
     * 用户的会话 redis list
     */
    public static final String USER_SESSION_KEY = "user.session:{userId}";

    /**
     * 是否已读
     */
    public static final String USER_SESSION_READ = "user.read:{userId}:{sessionKey}";

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
     * 群信息F
     */
    public static final String QUN = "qun:{qunId}";


}
