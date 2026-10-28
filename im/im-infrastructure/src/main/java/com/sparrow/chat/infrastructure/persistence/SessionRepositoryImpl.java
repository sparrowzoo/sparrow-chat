/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.dao.sparrow.SessionDao;
import com.sparrow.chat.dao.sparrow.SessionMetaDao;
import com.sparrow.chat.domain.repository.MessageRepository;
import com.sparrow.chat.domain.repository.QunRepository;
import com.sparrow.chat.domain.repository.SessionRepository;
import com.sparrow.chat.im.po.Session;
import com.sparrow.chat.infrastructure.commons.PropertyAccessBuilder;
import com.sparrow.chat.infrastructure.commons.RedisKey;
import com.sparrow.chat.infrastructure.converter.SessionConverter;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.params.SessionReadParams;
import com.sparrow.context.SessionContext;
import com.sparrow.messaging.protocol.ChatSession;
import com.sparrow.messaging.protocol.ChatUser;
import com.sparrow.messaging.protocol.enums.ChatType;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.enums.DefaultUserCategory;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import com.sparrow.utility.CollectionsUtility;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SessionRepositoryImpl implements SessionRepository {
    @Inject
    private QunRepository qunRepository;

    @Inject
    private RedisTemplate redisTemplate;

    @Inject
    private SessionDao sessionDao;

    @Inject
    private SessionMetaDao sessionMetaDao;

    @Inject
    private SessionConverter sessionConverter;

    @Inject
    private MessageRepository messageRepository;


    @Override
    public void saveSession(ChatSession session, ChatUser currentUser) {
        this.addNewSessionForUserId(session, currentUser);
        this.read(new SessionReadParams(session.key()));
        //1 to 1 推送
        if (ChatType.isOne2One(session.getChatType())) {
            ChatUser oppositeUser = session.getOppositeUser(currentUser);
            addNewSessionForUserId(session, oppositeUser);
            return;
        }
        List<Long> userIdList = this.qunRepository.getUserIdList(session.getId());
        for (Long userId : userIdList) {
            addNewSessionForUserId(session, ChatUser.longUserId(userId, DefaultUserCategory.REGISTER.getIdentity()));
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
        LoginUser loginUser = SessionContext.getLoginUser();
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
        LoginUser loginUser = SessionContext.getLoginUser();
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
