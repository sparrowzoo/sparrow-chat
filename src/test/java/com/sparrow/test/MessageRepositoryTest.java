package com.sparrow.test;

import com.sparrow.chat.boot.Application;
import com.sparrow.chat.commons.Chat;
import com.sparrow.chat.protocol.MessageDTO;
import com.sparrow.chat.protocol.Protocol;
import com.sparrow.chat.repository.MessageRepository;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void getMsgBySession() {
        List<MessageDTO> messages = this.messageRepository.getMessageBySession("qun");
        Assert.assertEquals(messages.size(), 1);
    }

    @Test
    public void testMessage() {
        for (int i = 0; i < 100; i++) {
            Protocol protocol = new Protocol();
            protocol.setClientSendTime(System.currentTimeMillis());
            protocol.setMessageType(Chat.CHAT_TYPE_1_2_1);
            protocol.setContent("hello" + i);
            protocol.setFromUserId(i);
            protocol.setTargetUserId(4);
            protocol.setSession("qun-0");
            protocol.setServerTime(System.currentTimeMillis());
            this.messageRepository.saveMessage(protocol);
        }
    }
}
