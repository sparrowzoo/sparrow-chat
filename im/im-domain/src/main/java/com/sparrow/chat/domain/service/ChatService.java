package com.sparrow.chat.domain.service;

import com.sparrow.chat.domain.bo.CancelProtocol;
import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.bo.Protocol;
import com.sparrow.chat.domain.netty.UserContainer;
import com.sparrow.chat.domain.repository.ContactRepository;
import com.sparrow.chat.domain.repository.MessageRepository;
import com.sparrow.chat.domain.repository.SessionRepository;
import com.sparrow.chat.protocol.dto.*;
import com.sparrow.chat.protocol.params.SessionReadParams;
import com.sparrow.chat.protocol.query.MessageCancelQuery;
import com.sparrow.chat.protocol.query.MessageQuery;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.SparrowError;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
        List<Channel> channels= UserContainer.getContainer().getChannels(chatSession, sender);
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

    public List<MessageDTO> fetchMessages(String sessionKey) throws BusinessException {
        this.sessionRepository.canAccessSession(sessionKey);
        return this.messageRepository.getMessageBySession(sessionKey);
    }

    public List<MessageDTO> fetchHistoryMessages(MessageQuery messageQuery) throws BusinessException {
        this.sessionRepository.canAccessSession(messageQuery.getSessionKey());
        return this.messageRepository.getHistoryMessage(messageQuery.getSessionKey(), messageQuery.getLastReadTime());
    }
}
