package com.sparrow.chat.infrastructure.persistence;

import com.alibaba.fastjson.JSON;
import com.sparrow.chat.dao.sparrow.MessageDao;
import com.sparrow.chat.dao.sparrow.query.session.MessageDBQuery;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.bo.MessageKey;
import com.sparrow.chat.domain.bo.Protocol;
import com.sparrow.chat.domain.repository.MessageRepository;
import com.sparrow.chat.domain.repository.QunRepository;
import com.sparrow.chat.im.po.Message;
import com.sparrow.chat.infrastructure.commons.PropertyAccessBuilder;
import com.sparrow.chat.infrastructure.commons.RedisKey;
import com.sparrow.chat.infrastructure.converter.MessageConverter;
import com.sparrow.chat.protocol.dto.HistoryMessageWrap;
import com.sparrow.chat.protocol.dto.MessageDTO;
import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.query.MessageCancelQuery;
import com.sparrow.chat.protocol.query.MessageQuery;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.exception.Asserts;
import com.sparrow.json.Json;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.sparrow.chat.protocol.constant.Chat.MAX_MSG_OF_SESSION;
import static com.sparrow.chat.protocol.constant.Chat.MESSAGE_EXPIRE_DAYS;

@Component
public class MessageRepositoryImpl implements MessageRepository {
    private static Logger logger = LoggerFactory.getLogger(MessageRepositoryImpl.class);

    private static final String MESSAGE_ZSET_PRFIX = "z:";

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MessageConverter messageConverter;
    @Autowired
    private MessageDao messageDao;
    @Inject
    private QunRepository qunRepository;
    @Inject
    private UserProfileAppService userProfileAppService;
    private Json json = JsonFactory.getProvider();

    @Override
    public void cancel(MessageCancelQuery messageCancel, ChatUser sender) throws BusinessException {
        Asserts.isTrue(sender == null, SparrowError.USER_NOT_LOGIN);
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionKey(messageCancel.getSessionKey());
        String redisKey = PlaceHolderParser.parse(RedisKey.SESSION_MESSAGE_KEY, propertyAccessor);
        String msgKey = new MessageKey(sender, messageCancel.getClientSendTime()).key();
        String msg = (String) redisTemplate.opsForHash().get(redisKey, msgKey);
        MessageDTO message = this.json.parse(msg, MessageDTO.class);
        Asserts.isTrue(message == null, SparrowError.GLOBAL_REQUEST_ID_NOT_EXIST);
        Asserts.isTrue(!sender.equals(message.getSender()), SparrowError.GLOBAL_PARAMETER_IS_ILLEGAL);
        redisTemplate.opsForHash().delete(redisKey, msgKey);
        redisTemplate.opsForZSet().remove(MESSAGE_ZSET_PRFIX + redisKey, 1, msgKey);
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
        long t = System.currentTimeMillis();
        for (int i = 0; i < messageList.size(); i++) {
            messageList.get(i).setMessageId(t + i);
        }
        return messageList;
    }

    @Override
    public void saveMessage(Protocol protocol, Long ip) {
        MessageDTO message = this.messageConverter.convertMessage(protocol);
        this.messageDao.insert(this.messageConverter.convertPo(protocol, ip));
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionKey(protocol.getChatSession().key());
        String messageKey = PlaceHolderParser.parse(RedisKey.SESSION_MESSAGE_KEY, propertyAccessor);
        String lkey = MESSAGE_ZSET_PRFIX + messageKey;
        MessageKey msgKey = new MessageKey(protocol.getSender(), protocol.getClientSendTime());
        redisTemplate.opsForZSet().add(lkey, msgKey.key(), System.currentTimeMillis());
        redisTemplate.opsForHash().put(messageKey, msgKey.key(), this.json.toString(message));
        if (redisTemplate.opsForHash().size(messageKey) > MAX_MSG_OF_SESSION) {
            Set<String> firstKey = redisTemplate.opsForZSet().range(lkey, 0, 0);
            if (firstKey != null && firstKey.iterator().hasNext()) {
                redisTemplate.opsForHash().delete(messageKey, firstKey.iterator().next());
            }
        }
        redisTemplate.expire(messageKey, MESSAGE_EXPIRE_DAYS, TimeUnit.DAYS);
    }


    @Override
    public List<MessageDTO> getMessageBySession(String session) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionKey(session);
        String messageKey = PlaceHolderParser.parse(RedisKey.SESSION_MESSAGE_KEY, propertyAccessor);
        return this.getMessages(messageKey);
    }

    @Override
    public List<MessageDTO> getHistoryMessage(MessageQuery query) {
        MessageDBQuery dbQuery = this.messageConverter.convertMessageQuery(query);
        List<Message> messages = this.messageDao.getHistoryMessage(dbQuery);
        return this.messageConverter.convertMessages(messages);
    }

    @Override
    public HistoryMessageWrap queryHistoryMessage(MessageQuery query) throws BusinessException {
        MessageDBQuery dbQuery = this.messageConverter.convertMessageQuery(query);
        List<Message> messages = this.messageDao.getHistoryMessage(dbQuery);
        List<MessageDTO> messageDtos = this.messageConverter.convertMessages(messages);
        HistoryMessageWrap wrap = new HistoryMessageWrap();
        wrap.setHistoryMessages(messageDtos);
        Map<String, QunDTO> qunMap = this.qunRepository.getQunMap(wrap.qunIds());
        wrap.setQunMaps(qunMap);
        Set<Long> userIds = wrap.userIds();
        wrap.setUserMaps(this.userProfileAppService.getUserMap(userIds));
        return wrap;
    }

    @Override
    public void fillSession(List<SessionDTO> sessionDTOList) {
        for (SessionDTO session : sessionDTOList) {
            PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionKey(session.getSessionKey());
            String messageKey = PlaceHolderParser.parse(RedisKey.SESSION_MESSAGE_KEY, propertyAccessor);
            String lkey = MESSAGE_ZSET_PRFIX + messageKey;
            Long unreadCount = redisTemplate.opsForZSet().count(lkey, session.getLastReadTime() + 1, -1);
            if (unreadCount == null) {
                unreadCount = 0L;
            }
            session.setUnreadCount(unreadCount.intValue());
            Set<String> lastMessageKeys = redisTemplate.opsForZSet().reverseRange(lkey, 0, 0);
            if (lastMessageKeys != null && lastMessageKeys.iterator().hasNext()) {
                String lastMessageKey = lastMessageKeys.iterator().next();
                String lastMessage = (String) redisTemplate.opsForHash().get(messageKey, lastMessageKey);
                session.setLastMessage(JSON.parseObject(lastMessage, MessageDTO.class));
            }
        }
    }
}
