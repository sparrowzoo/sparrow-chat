package com.sparrow.test;

import com.alibaba.fastjson.JSON;
import com.sparrow.chat.boot.Application;
import com.sparrow.chat.commons.Chat;
import com.sparrow.chat.commons.PropertyAccessBuilder;
import com.sparrow.chat.commons.RedisKey;
import com.sparrow.chat.protocol.ChatSession;
import com.sparrow.chat.protocol.MessageReadParam;
import com.sparrow.chat.repository.SessionRepository;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SessionTest {
    @Autowired
    private SessionRepository sessionRepository;

    private static String qunId = "qun-id-1";

    @Test
    public void sessionTest() {
        ChatSession chatSession = ChatSession.create1To1Session(100, 1);
        this.sessionRepository.saveSession(chatSession);

        chatSession = ChatSession.createQunSession(100, qunId);
        this.sessionRepository.saveSession(chatSession);
    }

    @Test
    public void getSessions() {
        List<ChatSession> sessions = this.sessionRepository.getSessions(100);
        Assert.assertEquals(sessions.size(), 1);
    }

    @Test
    public void readSession() {
        MessageReadParam messageReadParam = new MessageReadParam();
        messageReadParam.setUserId(1);
        messageReadParam.setChatType((int) Chat.CHAT_TYPE_1_2_1);
        messageReadParam.setSessionKey("1_2");
        System.out.println(JSON.toJSONString(messageReadParam));
    }
}
