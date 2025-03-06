package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.domain.netty.Protocol;
import com.sparrow.chat.infrastructure.commons.ConfigKey;
import com.sparrow.chat.infrastructure.commons.PropertyAccessBuilder;
import com.sparrow.chat.infrastructure.commons.RedisKey;
import com.sparrow.chat.infrastructure.converter.MessageConverter;
import com.sparrow.chat.protocol.ChatUser;
import com.sparrow.chat.protocol.MessageKey;
import com.sparrow.chat.protocol.dto.MessageDTO;
import com.sparrow.chat.protocol.param.MessageCancelParam;
import com.sparrow.chat.protocol.param.MessageReadParam;
import com.sparrow.chat.repository.MessageRepository;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.sparrow.chat.protocol.constant.Chat.*;

@Component
public class RedisMessageRepository implements MessageRepository {
    private static Logger logger = LoggerFactory.getLogger(RedisMessageRepository.class);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MessageConverter messageConverter;

    private Json json = JsonFactory.getProvider();

    private String generateImageId(ChatUser user) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String rootPhysicalPath = ConfigUtility.getValue(ConfigKey.IMAGE_PHYSICAL_ROOT_PATH);
        String physicalUrl = rootPhysicalPath +
                File.separator +
                year + File.separator +
                month + File.separator +
                day + File.separator +
                user.key() + File.separator +
                calendar.getTimeInMillis() + Extension.JPG;
        return physicalUrl;
    }

    @Override
    public void cancel(MessageCancelParam messageCancel) throws BusinessException {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionKey(messageCancel.getSessionKey());
        String redisKey = PlaceHolderParser.parse(RedisKey.MESSAGE_KEY, propertyAccessor);
        String msgKey = new MessageKey(messageCancel.getSender(), messageCancel.getClientSendTime()).key();
        String msg = (String) redisTemplate.opsForHash().get(redisKey, msgKey);
        MessageDTO message = this.json.parse(msg, MessageDTO.class);
        if (message == null) {
            throw new BusinessException(SparrowError.GLOBAL_REQUEST_ID_NOT_EXIST);
        }
        if (!message.getSender().equals(messageCancel.getSender())) {
            throw new BusinessException(SparrowError.GLOBAL_PARAMETER_IS_ILLEGAL);
        }
        redisTemplate.opsForHash().delete(redisKey, msgKey);
        redisTemplate.opsForList().remove("l" + redisKey, 1, msgKey);
    }

    @Override
    public String saveImageContent(Protocol protocol) {
        if (IMAGE_MESSAGE != protocol.getMessageType()) {
            return null;
        }
        String physicalUrl = this.generateImageId(protocol.getSender());
        FileUtility.getInstance().generateImage(protocol.getContentBytes(), physicalUrl);
        String rootPhysicalPath = ConfigUtility.getValue(ConfigKey.IMAGE_PHYSICAL_ROOT_PATH);
        String rootWebPath = ConfigUtility.getValue(ConfigKey.IMAGE_WEB_ROOT_PATH);

        String webUrl = physicalUrl.replace(rootPhysicalPath, rootWebPath);
        //转换成 msg id
        protocol.setContent(webUrl);
        return webUrl;
    }

    public List<MessageDTO> getMessages(String messageKey) {
        Map<String, String> messages = redisTemplate.opsForHash().entries(messageKey);
        List<MessageDTO> messageList = new ArrayList<>(messages.size());
        for (String key : messages.keySet()) {
            String message = messages.get(key);
            try {
                messageList.add(this.json.parse(message, MessageDTO.class));
            } catch (Exception e) {
                logger.error("parse json error key:{},message:{}", key, message, e);
            }
        }
        messageList.sort(Comparator.comparing(MessageDTO::getServerTime));
        return messageList;
    }

    @Override
    public void saveMessage(Protocol protocol) {
        this.saveImageContent(protocol);
        MessageDTO message = this.messageConverter.assembleMessage(protocol);
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionKey(protocol.getChatSession().getSessionKey());
        String messageKey = PlaceHolderParser.parse(RedisKey.MESSAGE_KEY, propertyAccessor);
        String lkey = "l" + messageKey;
        redisTemplate.opsForList().rightPush(lkey, message.getKey());
        redisTemplate.opsForHash().put(messageKey, message.getKey(), this.json.toString(message));
        if (redisTemplate.opsForHash().size(messageKey) > MAX_MSG_OF_SESSION) {
            String firstKey = (String) redisTemplate.opsForList().leftPop(lkey);
            redisTemplate.opsForHash().delete(messageKey, firstKey);
        }
        redisTemplate.expire(messageKey, MESSAGE_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override
    public void read(MessageReadParam messageRead) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionAndUserKey(messageRead.getSessionKey(), messageRead.getUser());
        String sessionReadKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_READ, propertyAccessor);
        redisTemplate.opsForValue().set(sessionReadKey, System.currentTimeMillis() + "");
    }

    @Override
    public Map<String, Long> getLastRead(ChatUser me, List<String> sessionKeys) {
        List<String> messageReadKeys = new ArrayList<>(sessionKeys.size());
        for (String sessionKey : sessionKeys) {
            PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionAndUserKey(sessionKey, me);
            String messageReadKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_READ, propertyAccessor);
            messageReadKeys.add(messageReadKey);
        }
        List<String> lastReadTimes = redisTemplate.opsForValue().multiGet(messageReadKeys);
        Map<String, Long> lastReadTimeMap = new HashMap<>(sessionKeys.size());
        for (int i = 0; i < sessionKeys.size(); i++) {
            String lastReadTime = lastReadTimes.get(i);
            if (lastReadTime != null) {
                lastReadTimeMap.put(sessionKeys.get(i), Long.parseLong(lastReadTime));
            }
        }
        return lastReadTimeMap;
    }

    @Override
    public List<MessageDTO> getMessageBySession(String session) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionKey(session);
        String messageKey = PlaceHolderParser.parse(RedisKey.MESSAGE_KEY, propertyAccessor);
        return this.getMessages(messageKey);
    }
}
