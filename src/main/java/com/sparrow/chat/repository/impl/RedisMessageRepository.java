package com.sparrow.chat.repository.impl;

import com.sparrow.chat.assemble.MessageAssemble;
import com.sparrow.chat.commons.Chat;
import com.sparrow.chat.commons.PropertyAccessBuilder;
import com.sparrow.chat.commons.RedisKey;
import com.sparrow.chat.protocol.ChatSession;
import com.sparrow.chat.protocol.MessageDTO;
import com.sparrow.chat.protocol.MessageReadParam;
import com.sparrow.chat.protocol.Protocol;
import com.sparrow.chat.repository.MessageRepository;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageRepository implements MessageRepository {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MessageAssemble messageAssemble;

    private Json json = JsonFactory.getProvider();

    @Override public String saveImageContent(Protocol protocol) {
        if (Chat.IMAGE_MESSAGE != protocol.getMessageType()) {
            return null;
        }

        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildMsgId(protocol.getFromUserId(), System.currentTimeMillis());
        String messageIdKey = PlaceHolderParser.parse(RedisKey.IMAGE_ID_KEY, propertyAccessor);
        redisTemplate.opsForValue().set(messageIdKey, protocol.getContent());
        //转换成 msg id
        protocol.setContent(messageIdKey);
        return messageIdKey;
    }

    @Override public String getImageContent(String imageId) {
        return (String) redisTemplate.opsForValue().get(imageId);
    }

    @Override public void saveMessage(Protocol protocol) {
        this.saveImageContent(protocol);
        MessageDTO message = this.messageAssemble.assembleMessage(protocol);
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionKey(protocol.getSession());
        String messageKey = PlaceHolderParser.parse(RedisKey.MESSAGE_KEY, propertyAccessor);
        redisTemplate.opsForList().rightPush(messageKey, message.json());
        if (redisTemplate.opsForList().size(messageKey) > Chat.MAX_MSG_OF_SESSION) {
            redisTemplate.opsForList().leftPop(messageKey);
        }
        redisTemplate.expire(messageKey, Chat.MESSAGE_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override public void read(MessageReadParam messageRead) {
        ChatSession chatSession = messageRead.getChatType() == Chat.CHAT_TYPE_1_2_1 ?
            ChatSession.create1To1Session(messageRead.getMe(), Integer.parseInt(messageRead.getTarget())) :
            ChatSession.createQunSession(messageRead.getMe(), messageRead.getTarget());
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionAndUserId(chatSession.getSessionKey(), messageRead.getMe());
        String messageReadKey = PlaceHolderParser.parse(RedisKey.MESSAGE_KEY, propertyAccessor);
        redisTemplate.opsForValue().set(messageReadKey, messageRead.getT());
    }

    @Override public Map<String, Long> getLastRead(Integer me, List<String> sessionKeys) {
        List<String> messageReadKeys = new ArrayList<>(sessionKeys.size());
        for (String sessionKey : sessionKeys) {
            PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionAndUserId(sessionKey, me);
            String messageReadKey = PlaceHolderParser.parse(RedisKey.MESSAGE_KEY, propertyAccessor);
            messageReadKeys.add(messageReadKey);
        }
        List<Long> lastReadTimes = redisTemplate.opsForValue().multiGet(messageReadKeys);
        Map<String, Long> lastReadTimeMap = new HashMap<>(sessionKeys.size());
        for (int i = 0; i < sessionKeys.size(); i++) {
            lastReadTimeMap.put(sessionKeys.get(i), lastReadTimes.get(i));
        }
        return lastReadTimeMap;
    }

    @Override public List<MessageDTO> getMessageBySession(String session) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionKey(session);
        String messageKey = PlaceHolderParser.parse(RedisKey.MESSAGE_KEY, propertyAccessor);
        List<String> messages = redisTemplate.opsForList().range(messageKey, 0, Chat.MAX_MSG_OF_SESSION);
        List<MessageDTO> messageDtos = new ArrayList<>(messages.size());
        for (String message : messages) {
            messageDtos.add(this.json.parse(message, MessageDTO.class));
        }
        return messageDtos;
    }
}
