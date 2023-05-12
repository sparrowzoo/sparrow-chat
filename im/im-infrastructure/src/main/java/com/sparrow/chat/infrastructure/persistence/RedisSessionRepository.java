package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.infrastructure.commons.PropertyAccessBuilder;
import com.sparrow.chat.infrastructure.commons.RedisKey;
import com.sparrow.chat.protocol.ChatSession;
import com.sparrow.chat.repository.ContactRepository;
import com.sparrow.chat.repository.QunRepository;
import com.sparrow.chat.repository.SessionRepository;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static com.sparrow.chat.protocol.constant.Chat.CHAT_TYPE_1_2_1;
import static com.sparrow.chat.protocol.constant.Chat.MAX_SESSION_OF_USER;
import static com.sparrow.chat.protocol.constant.Chat.MESSAGE_EXPIRE_DAYS;

@Component
public class RedisSessionRepository implements SessionRepository {
    @Autowired
    private QunRepository qunRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    private Json json = JsonFactory.getProvider();

    @Override public void saveSession(ChatSession session, Integer currentUserId) {
        this.addNewSessionForUserId(session, currentUserId);
        //1 to 1 推送
        if (session.getChatType() == CHAT_TYPE_1_2_1) {
            Integer oppositeUser = session.getOppositeUser(currentUserId);
            addNewSessionForUserId(session, oppositeUser);
            return;
        }
        List<Integer> userIdList = this.qunRepository.getUserIdList(session.getSessionKey());
        for (Integer userId : userIdList) {
            if (userId.equals(currentUserId)) {
                continue;
            }
            addNewSessionForUserId(session, userId);
        }
    }

    private void addNewSessionForUserId(ChatSession session, Integer userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByUserId(userId);
        String userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
        this.redisTemplate.opsForZSet().add(userSessionKey, session.json(), System.currentTimeMillis());
        if (this.redisTemplate.opsForZSet().size(userSessionKey) > MAX_SESSION_OF_USER) {
            this.redisTemplate.opsForZSet().removeRange(userSessionKey, 0, 0);
        }
        redisTemplate.expire(userSessionKey, MESSAGE_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override public List<ChatSession> getSessions(Integer userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByUserId(userId);
        String userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
        Set<String> charSessions = this.redisTemplate.opsForZSet().range(userSessionKey, 0, MAX_SESSION_OF_USER);
        List<ChatSession> chatSessionList = new ArrayList<>(charSessions.size());
        for (String session : charSessions) {
            ChatSession chatSession = this.json.parse(session, ChatSession.class);
            if (!chatSession.isOne2One()) {
                //通讯录是否存在群
                if (!this.contactRepository.existQunByUserId(userId, chatSession.getSessionKey())) {
                    this.redisTemplate.opsForZSet().remove(userSessionKey, session);
                    continue;
                }
            }
            chatSessionList.add(chatSession);
        }
        return chatSessionList;
    }
}
