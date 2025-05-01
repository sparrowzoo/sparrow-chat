package com.sparrow.chat.domain.service;

import com.sparrow.chat.domain.bo.*;
import com.sparrow.chat.domain.netty.UserContainer;
import com.sparrow.chat.domain.repository.ContactRepository;
import com.sparrow.chat.domain.repository.MessageRepository;
import com.sparrow.chat.domain.repository.QunRepository;
import com.sparrow.chat.domain.repository.SessionRepository;
import com.sparrow.chat.protocol.dto.ContactStatusDTO;
import com.sparrow.chat.protocol.dto.MessageDTO;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.dto.UserDTO;
import com.sparrow.chat.protocol.params.SessionReadParams;
import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.chat.protocol.query.MessageCancelQuery;
import com.sparrow.chat.protocol.query.MessageQuery;
import com.sparrow.chat.protocol.query.SessionQuery;
import com.sparrow.exception.Asserts;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.SparrowError;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChatService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private ContactRepository contactsRepository;
    @Inject
    private QunRepository qunRepository;

    @Inject
    private UserProfileAppService userProfileAppService;


    public List<ContactStatusDTO> getContactsStatus(Long userId, List<ChatUserQuery> userQueries) {
        if (CollectionUtils.isEmpty(userQueries)) {
            List<UserDTO> users = this.contactsRepository.getFriendsByUserId(userId);
            userQueries = new ArrayList<>();
            for (UserDTO user : users) {
                userQueries.add(user.getChatUser());
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
            contactStatus.setOnline(online);
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
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(loginUser == null, SparrowError.USER_NOT_LOGIN);
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
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(loginUser == null, SparrowError.USER_NOT_LOGIN);
        ChatUser chatUser = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());
        return this.sessionRepository.getSessions(chatUser);
    }

    public List<SessionBO> querySessions(SessionQuery sessionQuery) throws BusinessException {
        return this.sessionRepository.querySessions(sessionQuery);
    }

    public List<MessageDTO> fetchMessages(String sessionKey) throws BusinessException {
        this.isMember(sessionKey);
        this.sessionRepository.read(new SessionReadParams(sessionKey));
        return this.messageRepository.getMessageBySession(sessionKey);
    }

    public List<MessageDTO> fetchHistoryMessages(MessageQuery messageQuery) throws BusinessException {
        this.isMember(messageQuery.getSessionKey());
        return this.messageRepository.getHistoryMessage(messageQuery);
    }

    public void isMember(String sessionKey) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();

        ChatSession chatSession = ChatSession.parse(sessionKey);
        if (chatSession.isOne2One()) {
            ChatUser chatUser = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());
            Asserts.isTrue(!chatSession.isOne2OneMember(chatUser), SparrowError.SYSTEM_PERMISSION_DENIED);
            return;
        }
        boolean isMember = qunRepository.isQunMember(Long.parseLong(chatSession.getId()), loginUser.getUserId());
        if (!isMember) {
            UserProfileDTO userProfile = this.userProfileAppService.getByLoginUser(loginUser);
            Asserts.isTrue(!userProfile.getIsManager(), SparrowError.SYSTEM_PERMISSION_DENIED);
        }
        Asserts.isTrue(!isMember, SparrowError.SYSTEM_PERMISSION_DENIED);
    }
}
