package com.sparrow.chat.chat.user;

import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;

public class ChatSessionTest {
    public static void main(String[] args) {
        ChatUser sender=ChatUser.stringUserId("1234567890", 1);
        ChatUser receiver=ChatUser.stringUserId("9876543210", 0);
        ChatSession chatSession =ChatSession.create1To1Session(sender,receiver);
        String key=chatSession.key();
        System.out.println(key);
        ChatSession chatSession1=ChatSession.parse(key);
        System.out.println(chatSession1.getChatType());
        ChatUser oppositeUser= chatSession1.getOppositeUser(sender);
        System.out.println(oppositeUser.getId());
        System.out.println(oppositeUser.getCategory());
    }
}
