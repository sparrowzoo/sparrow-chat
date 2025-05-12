package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.dao.sparrow.SessionDao;
import com.sparrow.chat.dao.sparrow.SessionMetaDao;
import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.repository.MessageRepository;
import com.sparrow.chat.domain.repository.QunRepository;
import com.sparrow.chat.domain.repository.SessionRepository;
import com.sparrow.chat.im.po.Session;
import com.sparrow.chat.infrastructure.commons.PropertyAccessBuilder;
import com.sparrow.chat.infrastructure.commons.RedisKey;
import com.sparrow.chat.infrastructure.converter.SessionConverter;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.params.SessionReadParams;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import com.sparrow.utility.CollectionsUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.sparrow.chat.protocol.constant.Chat.CHAT_TYPE_1_2_1;

@Component
@Slf4j
public class SessionRepositoryImpl implements SessionRepository {
    @Autowired
    private QunRepository qunRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SessionDao sessionDao;

    @Inject
    private SessionMetaDao sessionMetaDao;

    @Autowired
    private SessionConverter sessionConverter;

    @Autowired
    private MessageRepository messageRepository;


    @Override
    public void saveSession(ChatSession session, ChatUser currentUser) {
        this.addNewSessionForUserId(session, currentUser);
        this.read(new SessionReadParams(session.key()));
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
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionAndUserKey(session.key(), chatUser);
        String userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
        Long score = (Long) this.redisTemplate.opsForValue().get(userSessionKey);
        if (score != null) {
            // 已经存在，不再添加
            return;
        }
        if (!sessionDao.exist(chatUser.getId(), chatUser.getCategory(), session.key())) {
            sessionDao.insert(this.sessionConverter.convert(session, chatUser));
        }
        this.redisTemplate.opsForValue().set(userSessionKey, System.currentTimeMillis());
        redisTemplate.expire(userSessionKey, 24, TimeUnit.HOURS);
    }

    @Override
    public List<SessionDTO> getSessions(ChatUser user) {
        List<Session> sessions = this.sessionDao.findByUser(user.getId(), user.getCategory());
        List<SessionDTO> sessionDTOList = this.sessionConverter.convert(sessions);
        this.fillLastReadTime(sessionDTOList);
        this.messageRepository.fillSession(sessionDTOList);
        return sessionDTOList;
    }


    @Override
    public void read(SessionReadParams messageRead) {
        LoginUser loginUser = ThreadContext.getLoginToken();
        ChatUser chatUser = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionAndUserKey(messageRead.getSessionKey(), chatUser);
        String userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
        if (redisTemplate.opsForValue().get(userSessionKey) != null) {
            redisTemplate.opsForValue().set(userSessionKey, System.currentTimeMillis());
            this.sessionDao.read(chatUser.getId(), chatUser.getCategory(), messageRead.getSessionKey());
        }
    }

    @Override
    public void fillLastReadTime(List<SessionDTO> sessionDTOList) {
        LoginUser loginUser = ThreadContext.getLoginToken();
        Set<String> sessionKeySet = new LinkedHashSet<>();
        ChatUser chatUser = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());
        for (SessionDTO session : sessionDTOList) {
            PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildBySessionAndUserKey(session.getSessionKey(), chatUser);
            String userSessionKey = PlaceHolderParser.parse(RedisKey.USER_SESSION_KEY, propertyAccessor);
            sessionKeySet.add(userSessionKey);
        }

        List<Long> lastReadTimeList = redisTemplate.opsForValue().multiGet(sessionKeySet);
        if (CollectionsUtility.isNullOrEmpty(lastReadTimeList)) {
            return;
        }
        for (int i = 0; i < lastReadTimeList.size(); i++) {
            SessionDTO session = sessionDTOList.get(i);
            Long lastReadTime = lastReadTimeList.get(i);
            if (lastReadTime == null) {
                lastReadTime = 0L;
            }
            session.setLastReadTime(lastReadTime);
        }
    }
}
