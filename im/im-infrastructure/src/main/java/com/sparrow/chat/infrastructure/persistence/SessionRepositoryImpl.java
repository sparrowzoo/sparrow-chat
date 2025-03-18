package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.dao.sparrow.SessionDao;
import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.repository.QunRepository;
import com.sparrow.chat.domain.repository.SessionRepository;
import com.sparrow.chat.im.po.Session;
import com.sparrow.chat.infrastructure.commons.PropertyAccessBuilder;
import com.sparrow.chat.infrastructure.commons.RedisKey;
import com.sparrow.chat.infrastructure.converter.SessionConverter;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.params.SessionReadParams;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.sparrow.chat.protocol.constant.Chat.*;

@Component
public class SessionRepositoryImpl implements SessionRepository {
    @Autowired
    private QunRepository qunRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private SessionConverter sessionConverter;

    @Override
    public void saveSession(ChatSession session, ChatUser currentUser) {
        this.addNewSessionForUserId(session, currentUser);
        //1 to 1 推送
        if (session.getChatType() == CHAT_TYPE_1_2_1) {
            ChatUser oppositeUser = session.getOppositeUser(currentUser);
            addNewSessionForUserId(session, oppositeUser);
            return;
        }
        List<Long> userIdList = this.qunRepository.getUserIdList(session.getId());
        for (Long userId : userIdList) {
            addNewSessionForUserId(session, ChatUser.longUserId(userId, LoginUser.CATEGORY_REGISTER));
        }
    }


    private void addNewSessionForUserId(ChatSession session, ChatUser chatUser) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByUserKey(chatUser.key());
        String userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
        Double score = this.redisTemplate.opsForZSet().score(userSessionKey, session.key());
        if (score != null) {
            // 已经存在，不再添加
            return;
        }
        if (!sessionDao.exist(chatUser.getId(), chatUser.getCategory(), session.getId())) {
            sessionDao.insert(this.sessionConverter.convert(session, chatUser));
        }
        this.redisTemplate.opsForZSet().add(userSessionKey, session.key(), System.currentTimeMillis());
        if (this.redisTemplate.opsForZSet().size(userSessionKey) > MAX_SESSION_OF_USER) {
            this.redisTemplate.opsForZSet().removeRange(userSessionKey, 0, 0);
        }
        redisTemplate.expire(userSessionKey, MESSAGE_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override
    public List<SessionDTO> getSessions(ChatUser user) {
        List<Session> sessions = this.sessionDao.findByUser(user.getId(), user.getCategory());
        return this.sessionConverter.convert(sessions);
    }

    public void canAccessSession(String sessionKey,int chatType) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        ChatUser chatUser = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByUserKey(chatUser.key());
        String userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
        ChatSession chatSession =ChatSession.createSession(chatType,sessionKey);
        Double score = this.redisTemplate.opsForZSet().score(userSessionKey, chatSession.key());
        Asserts.isTrue(score == null, SparrowError.SYSTEM_ILLEGAL_REQUEST);
    }

    @Override
    public void read(SessionReadParams messageRead) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        ChatUser chatUser = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());
        this.canAccessSession(messageRead.getSessionKey(), messageRead.getChatType());
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionAndUserKey(messageRead.getSessionKey(), chatUser);
        String sessionReadKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_READ, propertyAccessor);
        redisTemplate.opsForValue().set(sessionReadKey, System.currentTimeMillis() + "");
        this.sessionDao.read(chatUser.getId(), chatUser.getCategory(), messageRead.getSessionKey());
    }
}
