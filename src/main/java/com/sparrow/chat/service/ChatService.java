package com.sparrow.chat.service;

import com.sparrow.chat.commons.Chat;
import com.sparrow.chat.protocol.ChatSession;
import com.sparrow.chat.protocol.ContactsDTO;
import com.sparrow.chat.protocol.MessageDTO;
import com.sparrow.chat.protocol.MessageReadParam;
import com.sparrow.chat.protocol.Protocol;
import com.sparrow.chat.protocol.QunDTO;
import com.sparrow.chat.protocol.SessionDTO;
import com.sparrow.chat.protocol.UserDTO;
import com.sparrow.chat.repository.ContactsRepository;
import com.sparrow.chat.repository.MessageRepository;
import com.sparrow.chat.repository.SessionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private ContactsRepository contactsRepository;

    public ContactsDTO getContacts(Integer userId) {
        List<QunDTO> quns = this.contactsRepository.getQunsByUserId(userId);
        List<UserDTO> users = this.contactsRepository.getFriendsByUserId(userId);
        return new ContactsDTO(quns, users);
    }

    void saveMessage(Protocol protocol) {
        //将消息保存至session 每个消息只存留一份，只保留最近100条
        this.messageRepository.saveMessage(protocol);
        ChatSession chatSession = protocol.getCharType() == Chat.CHAT_TYPE_1_2_1 ?
            ChatSession.create1To1Session(protocol.getFromUserId(), protocol.getTargetUserId()) :
            ChatSession.createQunSession(protocol.getFromUserId(), protocol.getSession());
        this.sessionRepository.saveSession(chatSession);
    }

    public void read(MessageReadParam messageRead) {
        this.messageRepository.read(messageRead);
    }

    public List<SessionDTO> fetchSessions(Integer userId) {
        List<ChatSession> chatSessions = this.sessionRepository.getSessions(userId);
        List<SessionDTO> sessions = new ArrayList<>(chatSessions.size());
        List<String> sessionKeys = new ArrayList<>(chatSessions.size());
        for (ChatSession session : chatSessions) {
            List<MessageDTO> messages = this.messageRepository.getMessageBySession(session.getSessionKey());
            sessionKeys.add(session.getSessionKey());
            sessions.add(new SessionDTO(session, messages));
        }
        Map<String, Long> lastReadMap = this.messageRepository.getLastRead(userId, sessionKeys);
        for (SessionDTO session : sessions) {
            String sessionKey = session.getChatSession().getSessionKey();
            session.setLastReadTime(lastReadMap.get(sessionKey));
        }
        return sessions;
    }
}
