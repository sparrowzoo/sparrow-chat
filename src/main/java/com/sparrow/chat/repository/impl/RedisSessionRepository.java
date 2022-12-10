package com.sparrow.chat.repository.impl;

import com.sparrow.chat.commons.Chat;
import com.sparrow.chat.commons.PropertyAccessBuilder;
import com.sparrow.chat.commons.RedisKey;
import com.sparrow.chat.protocol.ChatSession;
import com.sparrow.chat.repository.QunRepository;
import com.sparrow.chat.repository.SessionRepository;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisSessionRepository implements SessionRepository {
    @Autowired
    private QunRepository qunRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    private Json json = JsonFactory.getProvider();

    @Override public void saveSession(ChatSession session) {
        this.addNewSessionForUserId(session, session.getMe());
        //1 to 1 推送
        if (session.getChatType() == Chat.CHAT_TYPE_1_2_1) {
            addNewSessionForUserId(session, session.getTarget());
            return;
        }
        List<Integer> userIdList = this.qunRepository.getUserIdList(session.getSessionKey());
        for (Integer userId : userIdList) {
            addNewSessionForUserId(session, userId);
        }
    }

    private void addNewSessionForUserId(ChatSession session, Integer userId) {
        String userSessionKey;
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByUserId(userId);
        userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
        this.redisTemplate.opsForZSet().add(userSessionKey, session.json(), System.currentTimeMillis());
        if (this.redisTemplate.opsForZSet().size(userSessionKey) > Chat.MAX_SESSION_OF_USER) {
            this.redisTemplate.opsForZSet().removeRange(userSessionKey, 0, 0);
        }
        redisTemplate.expire(userSessionKey, Chat.MESSAGE_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override public List<ChatSession> getSessions(Integer userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByUserId(userId);
        String userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
        Set<String> charSessions = this.redisTemplate.opsForZSet().range(userSessionKey, 0, Chat.MAX_SESSION_OF_USER);
        List<ChatSession> chatSessionList = new ArrayList<>(charSessions.size());
        for (String session : charSessions) {
            chatSessionList.add(this.json.parse(session, ChatSession.class));
        }
        return chatSessionList;
    }
}
