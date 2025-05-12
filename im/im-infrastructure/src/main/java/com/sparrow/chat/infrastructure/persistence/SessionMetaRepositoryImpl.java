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
import com.sparrow.core.Pair;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.StringUtility;
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
    public Map<String, SessionMetaDTO> querySession(Set<String> sessionKeys) throws BusinessException {
        List<SessionMeta> sessions = this.sessionMetaDao.querySession(sessionKeys);
        if (CollectionsUtility.isNullOrEmpty(sessions)) {
            return Collections.emptyMap();
        }
        Map<String, SessionMetaDTO> sessionMap = new HashMap<>();
        for (SessionMeta sessionMeta : sessions) {
            sessionMap.put(sessionMeta.getSessionKey(), this.sessionConverter.po2DTO(sessionMeta));
        }
        return sessionMap;
    }

    private void save(SessionMeta sessionMeta) {
        SessionMeta exists = this.sessionMetaDao.exists(sessionMeta.getSessionKey());
        if (exists!= null) {
            sessionMeta.setId(exists.getId());
            sessionMeta.setGmtModified(System.currentTimeMillis());
            this.sessionMetaDao.update(sessionMeta);
        } else {
            this.sessionMetaDao.insert(sessionMeta);
        }
    }

    private boolean disableExpired(Session session,ChatSession chatSession) {
        if (!chatSession.isOne2One()) {
            return false;
        }
        boolean isVisitor = chatSession.isVisitor();
        if (!isVisitor) {
            return false;
        }
        long gmtCreate= session.getGmtCreate();
        long currentTime = System.currentTimeMillis();
        int hours24 = 24*60*60*1000;
        if (currentTime - gmtCreate > hours24) {
            session.setStatus(StatusRecord.DISABLE);
            this.sessionMetaDao.disable(session.getSessionKey());
            this.sessionDao.update(session);
            return true;
        }
        return false;
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
                if (StringUtility.isNullOrEmpty(session.getSessionKey())) {
                    log.warn("sessionKey is null, session={}", session);
                    continue;
                }
                ChatSession chatSession = ChatSession.parse(session.getSessionKey());
                if (chatSession == null) {
                    log.warn("chatSession is null, sessionKey={}", session.getSessionKey());
                    continue;
                }
                if (chatSession.isOne2One()) {
                    Pair<ChatUser, ChatUser> chatUsers = chatSession.get1To1Members();
                    if (!chatUsers.getFirst().isVisitor()) {
                        userIds.add(chatUsers.getFirst().getLongUserId());
                    }
                    if (!chatUsers.getSecond().isVisitor()) {
                        userIds.add(chatUsers.getSecond().getLongUserId());
                    }
                }
            }

            Map<Long, UserProfileDTO> userProfileMap = this.userProfileAppService.getUserMap(userIds);
            Set<String> syncedSessionKeys = new HashSet<>();
            for (Session session : sessions) {
                if (syncedSessionKeys.contains(session.getSessionKey())) {
                    session.setSyncTime(System.currentTimeMillis());
                    this.sessionDao.update(session);
                    continue;
                }
                ChatSession chatSession = ChatSession.parse(session.getSessionKey());
                if (chatSession == null) {
                    log.warn("chatSession is null, sessionKey={}", session.getSessionKey());
                    continue;
                }
                boolean isDisable = this.disableExpired(session, chatSession);
                if (isDisable) {
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
                    session.setSyncTime(System.currentTimeMillis());
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
