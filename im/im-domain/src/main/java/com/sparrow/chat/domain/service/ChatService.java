package com.sparrow.chat.domain.service;

import com.sparrow.authenticator.AuthenticatorConfigReader;
import com.sparrow.authenticator.enums.AuthenticatorError;
import com.sparrow.chat.domain.bo.CancelProtocol;
import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.bo.Protocol;
import com.sparrow.chat.domain.netty.UserContainer;
import com.sparrow.chat.domain.repository.*;
import com.sparrow.chat.protocol.dto.*;
import com.sparrow.chat.protocol.params.SessionReadParams;
import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.chat.protocol.query.MessageCancelQuery;
import com.sparrow.chat.protocol.query.MessageQuery;
import com.sparrow.chat.protocol.query.SessionQuery;
import com.sparrow.context.SessionContext;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.constant.SparrowError;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
public class ChatService {
    @Inject
    private MessageRepository messageRepository;
    @Inject
    private SessionRepository sessionRepository;
    @Inject
    private ContactRepository contactsRepository;
    @Inject
    private QunRepository qunRepository;
    @Inject
    private SessionMateRepository sessionMateRepository;


    public List<ContactStatusDTO> getContactsStatus(Long userId, List<ChatUserQuery> userQueries) {
        if (CollectionUtils.isEmpty(userQueries)) {
            Map<Long, Long> users = this.contactsRepository.getFriendsByUserId(userId);
            userQueries = new ArrayList<>();
            for (Long friendId : users.keySet()) {
                userQueries.add(new ChatUserQuery(friendId.toString(), LoginUser.CATEGORY_REGISTER));
            }
        }

        List<ContactStatusDTO> contacts = new ArrayList<>();
        UserContainer userContainer = UserContainer.getContainer();
        for (ChatUserQuery userQuery : userQueries) {
            boolean online = UserContainer.getContainer().online(ChatUser.convertFromQuery(userQuery));
            if (!online) {
                continue;
            }
            ContactStatusDTO contactStatus = new ContactStatusDTO();
            contactStatus.setOnline(true);
            contactStatus.setId(userQuery.getId());
            contactStatus.setCategory(userQuery.getCategory());
            contactStatus.setLastActiveTime(userContainer.getLastActiveTime(ChatUser.convertFromQuery(userQuery)));
            contacts.add(contactStatus);
        }
        return contacts;
    }

    public void saveMessage(Protocol protocol, Long ip) {
        //将消息保存至session 每个消息只存留一份，只保留最近100条
        this.messageRepository.saveMessage(protocol, ip);
        this.sessionRepository.saveSession(protocol.getChatSession(), protocol.getSender());
    }

    public void read(SessionReadParams sessionReadParams) throws BusinessException {
        this.sessionRepository.read(sessionReadParams);
    }

    public void cancel(MessageCancelQuery messageCancel) throws BusinessException {
        LoginUser loginUser = SessionContext.getLoginUser();
        Asserts.isTrue(loginUser == null, AuthenticatorError.USER_NOT_LOGIN);
        ChatUser sender = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());
        ChatSession chatSession = ChatSession.parse(messageCancel.getSessionKey());
        CancelProtocol cancelProtocol = new CancelProtocol(messageCancel.getSessionKey(), messageCancel.getClientSendTime());
        //将会话的消息移除
        this.messageRepository.cancel(messageCancel, sender);
        List<Channel> channels = UserContainer.getContainer().getChannels(chatSession, sender);
        for (Channel channel : channels) {
            if (channel == null || !channel.isOpen() || !channel.isActive()) {
                continue;
            }
            channel.writeAndFlush(new BinaryWebSocketFrame(cancelProtocol.toBytes()));
        }
    }

    public List<SessionDTO> fetchSessions() throws BusinessException {
        LoginUser loginUser = SessionContext.getLoginUser();
        Asserts.isTrue(loginUser == null, AuthenticatorError.USER_NOT_LOGIN);
        ChatUser chatUser = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());
        return this.sessionRepository.getSessions(chatUser);
    }

    public List<SessionMetaDTO> querySessions(SessionQuery sessionQuery) throws BusinessException {
        return this.sessionMateRepository.querySessions(sessionQuery);
    }

    public List<MessageDTO> fetchMessages(String sessionKey) throws BusinessException {
        this.isMember(sessionKey);
        this.sessionRepository.read(new SessionReadParams(sessionKey));
        return this.messageRepository.getMessageBySession(sessionKey);
    }

    public HistoryMessageWrap queryHistoryMessages(MessageQuery messageQuery) throws BusinessException {
        LoginUser loginUser = SessionContext.getLoginUser();
        AuthenticatorConfigReader authenticatorConfigReader = ApplicationContext.getContainer().getBean(AuthenticatorConfigReader.class);
        int platformId = authenticatorConfigReader.getPlatformManagerCategory();
        boolean isAdmin = loginUser.getCategory().equals(platformId);
        if (!isAdmin) {
            this.isMember(messageQuery.getSessionKey());
        }
        return this.messageRepository.queryHistoryMessage(messageQuery);
    }

    public void isMember(String sessionKey) throws BusinessException {
        LoginUser loginUser = SessionContext.getLoginUser();
        ChatSession chatSession = ChatSession.parse(sessionKey);
        if (chatSession != null) {
            if (chatSession.isOne2One()) {
                ChatUser chatUser = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());
                Asserts.isTrue(!chatSession.isOne2OneMember(chatUser), SparrowError.SYSTEM_PERMISSION_DENIED);
                return;
            }
            boolean isMember = qunRepository.isQunMember(Long.parseLong(chatSession.getId()), loginUser.getUserId());
            Asserts.isTrue(!isMember, SparrowError.SYSTEM_PERMISSION_DENIED);
        }
    }
}
