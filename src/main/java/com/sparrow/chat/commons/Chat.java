package com.sparrow.chat.commons;

public class Chat {
    public static final int USER_NOT_FOUND = -1;
    public static final int TEXT_MESSAGE = 0;
    public static final int IMAGE_MESSAGE = 1;
    public static final byte CHAT_TYPE_1_2_1 = 0;
    public static final byte CHAT_TYPE_1_2_N = 1;

    public static final int MAX_SESSION_OF_USER = 100;
    public static final int MAX_MSG_OF_SESSION = 100;

    public static final int MESSAGE_EXPIRE_DAYS = 30;
}
