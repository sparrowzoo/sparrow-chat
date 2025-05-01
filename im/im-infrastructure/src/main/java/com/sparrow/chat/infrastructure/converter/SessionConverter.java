package com.sparrow.chat.infrastructure.converter;

import com.sparrow.chat.dao.sparrow.query.session.SessionDBQuery;
import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.bo.SessionBO;
import com.sparrow.chat.im.po.Session;
import com.sparrow.chat.im.po.SessionMeta;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.query.SessionQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named
public class SessionConverter {
    public SessionDTO convert(Session session) {
        return SessionDTO.parse(session.getSessionKey(), session.getLastReadTime());
    }

    public SessionDBQuery convert(SessionQuery sessionQuery) {
        SessionDBQuery query = new SessionDBQuery();
        BeanUtils.copyProperties(sessionQuery, query);
        return query;
    }

    public List<SessionBO> poList2BOList(List<SessionMeta> sessions) {
        if (CollectionUtils.isEmpty(sessions)) {
            return Collections.emptyList();
        }
        List<SessionBO> sessionBOS = new ArrayList<>(sessions.size());
        for (SessionMeta session : sessions) {
            sessionBOS.add(po2BO(session));
        }
        return sessionBOS;
    }

    public SessionBO po2BO(SessionMeta session) {
        if (session == null) {
            return new SessionBO();
        }
        SessionBO sessionBO = new SessionBO();
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


    public Session convert(ChatSession session, ChatUser owner) {
        Session s = new Session();
        s.setUserId(owner.getId());
        s.setCategory(owner.getCategory());
        s.setSessionKey(session.key());
        s.setChatType(session.getChatType());
        s.setGmtCreate(System.currentTimeMillis());
        s.setLastReadTime(System.currentTimeMillis());
        return s;
    }
}
