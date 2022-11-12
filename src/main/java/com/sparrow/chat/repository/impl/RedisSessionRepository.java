package com.sparrow.chat.repository.impl;

import com.sparrow.chat.commons.Chat;
import com.sparrow.chat.commons.PropertyAccessBuilder;
import com.sparrow.chat.commons.RedisKey;
import com.sparrow.chat.protocol.ChatSession;
import com.sparrow.chat.repository.QunRepository;
import com.sparrow.chat.repository.SessionRepository;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisSessionRepository implements SessionRepository {
    @Autowired
    private QunRepository qunRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override public void saveSession(ChatSession session) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildChatPropertyAccessorByUserId(session.getMe());
        String userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);

        this.redisTemplate.opsForList().rightPush(userSessionKey, session.json());
        redisTemplate.expire(userSessionKey,Chat.MESSAGE_EXPIRE_DAYS, TimeUnit.DAYS);

        //1 to 1 推送
        if (session.getChatType() == Chat.CHAT_TYPE_1_2_1) {
            propertyAccessor = PropertyAccessBuilder.buildChatPropertyAccessorByUserId(session.getTarget());
            userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
            this.redisTemplate.opsForList().rightPush(userSessionKey, session.json());
            if (this.redisTemplate.opsForList().size(userSessionKey) > Chat.MAX_SESSION_OF_USER) {
                this.redisTemplate.opsForList().leftPop(userSessionKey);
            }
            redisTemplate.expire(userSessionKey,Chat.MESSAGE_EXPIRE_DAYS, TimeUnit.DAYS);
            return;
        }

        List<Integer> userIdList = this.qunRepository.getUserIdList(session.getSessionKey());

        for (Integer userId : userIdList) {
            propertyAccessor = PropertyAccessBuilder.buildChatPropertyAccessorByUserId(userId);
            userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
            this.redisTemplate.opsForList().rightPush(userSessionKey, session.json());
            if (this.redisTemplate.opsForList().size(userSessionKey) > Chat.MAX_SESSION_OF_USER) {
                this.redisTemplate.opsForList().leftPop(userSessionKey);
            }
            redisTemplate.expire(userSessionKey,Chat.MESSAGE_EXPIRE_DAYS, TimeUnit.DAYS);
        }
    }

    @Override public List<ChatSession> getSessions(Integer userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildChatPropertyAccessorByUserId(userId);
        String userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
        return this.redisTemplate.opsForList().range(userSessionKey, 0, Chat.MAX_SESSION_OF_USER);
    }
}
