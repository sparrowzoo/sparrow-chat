package com.sparrow.chat.infrastructure.converter;

import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.im.po.Session;
import org.springframework.util.CollectionUtils;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
public class SessionConverter {
    public SessionDTO convert(Session session) {
        return SessionDTO.parse(session.getSessionKey(), session.getLastReadTime());
    }

    public List<SessionDTO> convert(List<Session> sessions) {
        if (CollectionUtils.isEmpty(sessions)) {
            return null;
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
