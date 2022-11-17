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
        List<MessageDTO> messages = this.messageRepository.getMessageBySession("100_101");
        Assert.assertEquals(messages.size(), 1);
    }

    @Test
    public void testMessage() {
        ByteBuf buf = new PooledByteBufAllocator().buffer();
        buf.writeByte(Chat.CHAT_TYPE_1_2_1);
        buf.writeByte(Chat.TEXT_MESSAGE);
        buf.writeInt(100);
        buf.writeInt(101);
        String msg = "hello zhangsan";
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        Protocol protocol = new Protocol(buf);
        this.messageRepository.saveMessage(protocol);
    }
}
