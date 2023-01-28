package com.sparrow.chat.repository.impl;

import com.sparrow.chat.assemble.MessageAssemble;
import com.sparrow.chat.commons.Chat;
import com.sparrow.chat.commons.ConfigKey;
import com.sparrow.chat.commons.MessageKey;
import com.sparrow.chat.commons.PropertyAccessBuilder;
import com.sparrow.chat.commons.RedisKey;
import com.sparrow.chat.protocol.MessageCancelParam;
import com.sparrow.chat.protocol.MessageDTO;
import com.sparrow.chat.protocol.MessageReadParam;
import com.sparrow.chat.protocol.Protocol;
import com.sparrow.chat.repository.MessageRepository;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.FileUtility;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
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

    private String generateImageId(Integer userId) {
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
            userId + File.separator +
            calendar.getTimeInMillis() + Extension.JPG;
        return physicalUrl;
    }

    @Override public void cancel(MessageCancelParam messageCancel) throws BusinessException {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionKey(messageCancel.getSessionKey());
        String redisKey = PlaceHolderParser.parse(RedisKey.MESSAGE_KEY, propertyAccessor);
        String msgKey = new MessageKey(messageCancel.getFromUserId(), messageCancel.getClientSendTime()).key();
        String msg = (String) redisTemplate.opsForHash().get(redisKey, msgKey);
        MessageDTO message = this.json.parse(msg, MessageDTO.class);
        if (message == null) {
            throw new BusinessException(SparrowError.GLOBAL_REQUEST_ID_NOT_EXIST);
        }
        if (message.getFromUserId() != messageCancel.getFromUserId()) {
            throw new BusinessException(SparrowError.GLOBAL_PARAMETER_IS_ILLEGAL);
        }
        redisTemplate.opsForHash().delete(redisKey, msgKey);
        redisTemplate.opsForList().remove("l" + redisKey, 1, msgKey);
    }

    @Override public String saveImageContent(Protocol protocol) {
        if (Chat.IMAGE_MESSAGE != protocol.getMessageType()) {
            return null;
        }
        String physicalUrl = this.generateImageId(protocol.getFromUserId());
        FileUtility.getInstance().generateImage(protocol.getContent(), physicalUrl);
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
            messageList.add(this.json.parse(messages.get(key), MessageDTO.class));
        }
        messageList.sort(Comparator.comparing(MessageDTO::getServerTime));
        return messageList;
    }

    @Override public void saveMessage(Protocol protocol) {
        this.saveImageContent(protocol);
        MessageDTO message = this.messageAssemble.assembleMessage(protocol);
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionKey(protocol.getChatSession().getSessionKey());
        String messageKey = PlaceHolderParser.parse(RedisKey.MESSAGE_KEY, propertyAccessor);
        String lkey = "l" + messageKey;
        redisTemplate.opsForList().rightPush(lkey, message.getKey());
        redisTemplate.opsForHash().put(messageKey, message.getKey(), message.json());
        if (redisTemplate.opsForHash().size(messageKey) > Chat.MAX_MSG_OF_SESSION) {
            String firstKey = (String) redisTemplate.opsForList().leftPop(lkey);
            redisTemplate.opsForHash().delete(messageKey, firstKey);
        }
        redisTemplate.expire(messageKey, Chat.MESSAGE_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override public void read(MessageReadParam messageRead) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionAndUserId(messageRead.getSessionKey(), messageRead.getUserId());
        String sessionReadKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_READ, propertyAccessor);
        redisTemplate.opsForValue().set(sessionReadKey, System.currentTimeMillis() + "");
    }

    @Override public Map<String, Long> getLastRead(Integer me, List<String> sessionKeys) {
        List<String> messageReadKeys = new ArrayList<>(sessionKeys.size());
        for (String sessionKey : sessionKeys) {
            PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionAndUserId(sessionKey, me);
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

    @Override public List<MessageDTO> getMessageBySession(String session) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionKey(session);
        String messageKey = PlaceHolderParser.parse(RedisKey.MESSAGE_KEY, propertyAccessor);
        return this.getMessages(messageKey);
    }
}
