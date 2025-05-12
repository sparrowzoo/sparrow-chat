package com.sparrow.chat.infrastructure.converter;

import com.sparrow.chat.dao.sparrow.query.session.SessionDBQuery;
import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.protocol.dto.SessionMetaDTO;
import com.sparrow.chat.im.po.Session;
import com.sparrow.chat.im.po.SessionMeta;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.query.SessionQuery;
import com.sparrow.core.Pair;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.enums.StatusRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Named
@Slf4j
public class SessionConverter {
    public SessionDTO convert(Session session) {
        return SessionDTO.parse(session.getSessionKey(), session.getLastReadTime());
    }

    public SessionDBQuery convert(SessionQuery sessionQuery) {
        SessionDBQuery query = new SessionDBQuery();
        BeanUtils.copyProperties(sessionQuery, query);
        return query;
    }

    public List<SessionMetaDTO> poList2BOList(List<SessionMeta> sessions) {
        if (CollectionUtils.isEmpty(sessions)) {
            return Collections.emptyList();
        }
        List<SessionMetaDTO> sessionBOS = new ArrayList<>(sessions.size());
        for (SessionMeta session : sessions) {
            sessionBOS.add(po2DTO(session));
        }
        return sessionBOS;
    }

    public SessionMetaDTO po2DTO(SessionMeta session) {
        if (session == null) {
            return new SessionMetaDTO();
        }
        SessionMetaDTO sessionBO = new SessionMetaDTO();
        BeanUtils.copyProperties(session, sessionBO);
        return sessionBO;
    }

    public List<SessionDTO> convert(List<Session> sessions) {
        if (CollectionUtils.isEmpty(sessions)) {
            return new ArrayList<>();
        }
        List<SessionDTO> sessionDTOs = new ArrayList<>(sessions.size());
        for (Session session : sessions) {
            sessionDTOs.add(convert(session));
        }
        return sessionDTOs;
    }

    public SessionMeta toSessionMeta(Session session, Map<Long, UserProfileDTO> userMap) {
        ChatSession chatSession = ChatSession.parse(session.getSessionKey());
        if (!chatSession.isOne2One()) {
            log.warn("chat session is not one2one, sessionKey={}", session.getSessionKey());
            return null;
        }
        Pair<ChatUser, ChatUser> users = chatSession.get1To1Members();
        ChatUser first = users.getFirst();
        ChatUser second = users.getSecond();
        Boolean isVisitor = first.isVisitor() || second.isVisitor();
        UserProfileDTO firstUserProfile = userMap.get(first.getLongUserId());
        UserProfileDTO secondUserProfile = userMap.get(second.getLongUserId());
        SessionMeta sessionMeta = new SessionMeta();
        sessionMeta.setSessionKey(session.getSessionKey());
        sessionMeta.setUserId(firstUserProfile.getUserId());
        sessionMeta.setUserCategory(firstUserProfile.getCategory());
        sessionMeta.setUserName(firstUserProfile.getUserName());
        sessionMeta.setUserNickName(firstUserProfile.getNickName());
        sessionMeta.setOppositeId(second.getLongUserId());
        sessionMeta.setOppositeCategory(secondUserProfile.getCategory());
        sessionMeta.setOppositeName(secondUserProfile.getUserName());
        sessionMeta.setOppositeNickName(secondUserProfile.getNickName());
        sessionMeta.setIsVisitor(isVisitor);
        sessionMeta.setGmtCreate(session.getGmtCreate());
        sessionMeta.setGmtModified(sessionMeta.getGmtCreate());
        sessionMeta.setStatus(StatusRecord.ENABLE);
        return sessionMeta;
    }

    public Session convert(ChatSession session, ChatUser owner) {
        Session s = new Session();
        s.setUserId(Long.parseLong(owner.getId()));
        s.setCategory(owner.getCategory());
        s.setSessionKey(session.key());
        s.setChatType(session.getChatType());
        s.setGmtCreate(System.currentTimeMillis());
        s.setLastReadTime(System.currentTimeMillis());
        s.setSyncTime(0L);
        s.setStatus(StatusRecord.ENABLE);
        return s;
    }
}
