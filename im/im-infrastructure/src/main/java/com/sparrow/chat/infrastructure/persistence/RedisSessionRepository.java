package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.infrastructure.commons.PropertyAccessBuilder;
import com.sparrow.chat.infrastructure.commons.RedisKey;
import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.repository.ContactRepository;
import com.sparrow.chat.domain.repository.QunRepository;
import com.sparrow.chat.domain.repository.SessionRepository;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.LoginUser;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.sparrow.chat.protocol.constant.Chat.*;

@Component
public class RedisSessionRepository implements SessionRepository {
    @Autowired
    private QunRepository qunRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    private Json json = JsonFactory.getProvider();

    @Override public void saveSession(ChatSession session, ChatUser currentUser) {
        this.addNewSessionForUserId(session, currentUser);
        //1 to 1 推送
        if (session.getChatType() == CHAT_TYPE_1_2_1) {
            ChatUser oppositeUser = session.getOppositeUser(currentUser);
            addNewSessionForUserId(session, oppositeUser);
            return;
        }
        List<Long> userIdList = this.qunRepository.getUserIdList(session.getSessionKey());
        for (Long userId : userIdList) {
            addNewSessionForUserId(session, ChatUser.longUserId(userId, LoginUser.CATEGORY_REGISTER));
        }
    }

    private void addNewSessionForUserId(ChatSession session, ChatUser chatUser) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByUserKey(chatUser.key());
        String userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
        this.redisTemplate.opsForZSet().add(userSessionKey, session.json(), System.currentTimeMillis());
        if (this.redisTemplate.opsForZSet().size(userSessionKey) > MAX_SESSION_OF_USER) {
            this.redisTemplate.opsForZSet().removeRange(userSessionKey, 0, 0);
        }
        redisTemplate.expire(userSessionKey, MESSAGE_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override public List<ChatSession> getSessions(ChatUser user) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByUserKey(user.key());
        String userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
        Set<String> charSessions = this.redisTemplate.opsForZSet().range(userSessionKey, 0, MAX_SESSION_OF_USER);
        List<ChatSession> chatSessionList = new ArrayList<>(charSessions.size());
        for (String session : charSessions) {
            ChatSession chatSession = this.json.parse(session, ChatSession.class);
            chatSessionList.add(chatSession);
        }
        return chatSessionList;
    }
}
