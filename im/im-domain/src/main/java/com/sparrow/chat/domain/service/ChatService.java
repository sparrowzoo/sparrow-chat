package com.sparrow.chat.domain.service;

import com.sparrow.chat.domain.bo.CancelProtocol;
import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.bo.Protocol;
import com.sparrow.chat.domain.netty.UserContainer;
import com.sparrow.chat.domain.repository.ContactRepository;
import com.sparrow.chat.domain.repository.MessageRepository;
import com.sparrow.chat.domain.repository.SessionRepository;
import com.sparrow.chat.protocol.constant.Chat;
import com.sparrow.chat.protocol.dto.ContactsDTO;
import com.sparrow.chat.protocol.dto.QunDTO;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.dto.UserDTO;
import com.sparrow.chat.protocol.query.MessageCancelQuery;
import com.sparrow.chat.protocol.query.MessageReadQuery;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.SparrowError;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ChatService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private ContactRepository contactsRepository;

    public ContactsDTO getContacts(Long userId) {
        List<QunDTO> groups = this.contactsRepository.getQunsByUserId(userId);
        List<UserDTO> users = this.contactsRepository.getFriendsByUserId(userId);
        return new ContactsDTO(groups, users);
    }

    public void saveMessage(Protocol protocol) {
        //将消息保存至session 每个消息只存留一份，只保留最近100条
        this.messageRepository.saveMessage(protocol);
        this.sessionRepository.saveSession(protocol.getChatSession(), protocol.getSender());
    }

    public void read(MessageReadQuery messageReadQuery) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        ChatUser chatUser = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());
        this.sessionRepository.read(messageReadQuery, chatUser);
    }

    public void cancel(MessageCancelQuery messageCancel) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(loginUser == null, SparrowError.USER_NOT_LOGIN);
        ChatUser sender = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());

        CancelProtocol cancelProtocol = new CancelProtocol(messageCancel.getSessionKey(), messageCancel.getClientSendTime());
        //将会话的消息移除
        this.messageRepository.cancel(messageCancel, sender);
        List<Channel> channels;
        if (Chat.CHAT_TYPE_1_2_1 == messageCancel.getChatType()) {
            ChatSession chatSession = ChatSession.create1To1CancelSession(sender, messageCancel.getSessionKey());
            channels = UserContainer.getContainer().getChannels(chatSession, sender);
        } else {
            ChatSession chatSession = ChatSession.createQunSession(sender, messageCancel.getSessionKey());
            channels = UserContainer.getContainer().getChannels(chatSession, sender);
        }
        for (Channel channel : channels) {
            if (channel == null || !channel.isOpen() || !channel.isActive()) {
                continue;
            }
            channel.writeAndFlush(new BinaryWebSocketFrame(cancelProtocol.toBytes()));
        }
    }

    public List<SessionDTO> fetchSessions(ChatUser user) {
        List<ChatSession> chatSessions = this.sessionRepository.getSessions(user);
        List<SessionDTO> sessions = new ArrayList<>(chatSessions.size());
        List<String> sessionKeys = new ArrayList<>(chatSessions.size());
        for (ChatSession session : chatSessions) {
            sessionKeys.add(session.getSessionKey());
            sessions.add(new SessionDTO(session.getChatType(), session.getSessionKey()));
        }
        Map<String, Long> lastReadMap = this.messageRepository.getLastRead(user, sessionKeys);
        for (SessionDTO session : sessions) {
            String sessionKey = session.getSessionKey();
            if (lastReadMap.containsKey(sessionKey)) {
                session.setLastReadTime(lastReadMap.get(sessionKey));
            }
        }
        return sessions;
    }
}
