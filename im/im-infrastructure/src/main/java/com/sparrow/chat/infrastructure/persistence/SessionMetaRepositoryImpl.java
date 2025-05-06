package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.dao.sparrow.SessionDao;
import com.sparrow.chat.dao.sparrow.SessionMetaDao;
import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.protocol.dto.SessionMetaDTO;
import com.sparrow.chat.domain.repository.SessionMateRepository;
import com.sparrow.chat.im.po.Session;
import com.sparrow.chat.im.po.SessionMeta;
import com.sparrow.chat.infrastructure.converter.SessionConverter;
import com.sparrow.chat.protocol.query.SessionQuery;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.utility.CollectionsUtility;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named
@Slf4j
public class SessionMetaRepositoryImpl implements SessionMateRepository {
    @Inject
    private SessionConverter sessionConverter;
    @Inject
    private SessionDao sessionDao;
    @Inject
    private SessionMetaDao sessionMetaDao;
    @Inject
    private UserProfileAppService userProfileAppService;

    @Override
    public List<SessionMetaDTO> querySessions(SessionQuery sessionQuery) {
        List<SessionMeta> sessions = this.sessionMetaDao.querySession(this.sessionConverter.convert(sessionQuery));
        return this.sessionConverter.poList2BOList(sessions);
    }

    @Override
    public Map<String,SessionMetaDTO> querySession(Set<String> sessionKeys) throws BusinessException {
       List<SessionMeta> sessions = this.sessionMetaDao.querySession(sessionKeys);
       if (CollectionsUtility.isNullOrEmpty(sessions)) {
           return Collections.emptyMap();
       }
       Map<String,SessionMetaDTO> sessionMap = new HashMap<>();
       for (SessionMeta sessionMeta : sessions) {
           sessionMap.put(sessionMeta.getSessionKey(), this.sessionConverter.po2DTO(sessionMeta));
       }
       return sessionMap;
    }

    private void save(SessionMeta sessionMeta) {
        boolean exists = this.sessionMetaDao.exists(sessionMeta.getSessionKey());
        if (exists) {
            this.sessionMetaDao.update(sessionMeta);
        } else {
            this.sessionMetaDao.insert(sessionMeta);
        }
    }

    @Override
    public void syncSessions() throws BusinessException {
        int limit = 100;
        while (true) {
            List<Session> sessions = this.sessionDao.fetchUnSyncSessions(limit);
            if (CollectionsUtility.isNullOrEmpty(sessions)) {
                break;
            }
            Set<Long> userIds = new HashSet<>();
            for (Session session : sessions) {
                ChatSession chatSession = ChatSession.parse(session.getSessionKey());
                if (chatSession.isOne2One()) {
                    ChatUser currentUser = ChatUser.longUserId(session.getUserId(), session.getCategory());
                    ChatUser oppositeUser = chatSession.getOppositeUser(currentUser);
                    if (!currentUser.isVisitor()) {
                        userIds.add(session.getUserId());
                    }
                    if (!oppositeUser.isVisitor()) {
                        userIds.add(oppositeUser.getLongUserId());
                    }
                }
            }

            Map<Long, UserProfileDTO> userProfileMap = this.userProfileAppService.getUserMap(userIds);
            Set<String> syncedSessionKeys = new HashSet<>();
            for (Session session : sessions) {
                if (syncedSessionKeys.contains(session.getSessionKey())) {
                    session.setSynced(true);
                    this.sessionDao.update(session);
                    continue;
                }
                syncedSessionKeys.add(session.getSessionKey());
                SessionMeta sessionMeta = this.sessionConverter.toSessionMeta(session, userProfileMap);
                try {
                    if (sessionMeta == null) {
                        log.warn("sessionMeta is null, sessionKey={}", session.getSessionKey());
                        continue;
                    }
                    this.save(sessionMeta);
                    session.setSynced(true);
                    this.sessionDao.update(session);
                } catch (Exception e) {
                    log.error("sync session error, sessionKey={}", session.getSessionKey(), e);
                }
            }
            if (sessions.size() < limit) {
                break;
            }
        }
    }
}
