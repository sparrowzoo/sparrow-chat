package com.sparrow.chat.infrastructure.converter;

import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.bo.Protocol;
import com.sparrow.chat.im.po.Message;
import com.sparrow.chat.protocol.dto.MessageDTO;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.support.IpSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageConverter {

    @Autowired
    private IpSupport support;

    public MessageDTO convertMessage(Protocol protocol) {
        MessageDTO message = new MessageDTO();
        message.setMessageType(protocol.getMessageType());
        message.setContent(protocol.getContent());
        message.setSession(protocol.getChatSession().toSessionDTO());
        message.setSender(protocol.getSender().toChatUserQuery());
        message.setReceiver(protocol.getReceiver().toChatUserQuery());
        message.setServerTime(protocol.getServerTime());
        message.setClientSendTime(protocol.getClientSendTime());
        return message;
    }

    public MessageDTO convertMessage(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageType(message.getMessageType());
        messageDTO.setSender(new ChatUserQuery(message.getSender(), message.getSenderCategory()));
        messageDTO.setReceiver(new ChatUserQuery(message.getReceiver(), message.getReceiverCategory()));
        messageDTO.setContent(message.getContent());
        messageDTO.setServerTime(message.getServerTime());
        messageDTO.setClientSendTime(message.getClientSendTime());
        messageDTO.setSession(new SessionDTO(message.getChatType(), message.getSessionKey()));
        return messageDTO;
    }

    public List<MessageDTO> convertMessages(List<Message> messages) {
        List<MessageDTO> messageDTOs = new ArrayList<>();
        for (Message message : messages) {
            messageDTOs.add(convertMessage(message));
        }
        return messageDTOs;
    }

    public Message convertPo(Protocol protocol) {
        ChatUser sender = protocol.getSender();
        ChatUser receiver = protocol.getReceiver();
        Message message = new Message();
        message.setMessageType(protocol.getMessageType());
        message.setContent(protocol.getContent());
        message.setSender(sender.getId());
        message.setSenderCategory(sender.getCategory());
        message.setReceiver(receiver.getId());
        message.setReceiverCategory(receiver.getCategory());
        message.setServerTime(protocol.getServerTime());
        message.setClientSendTime(protocol.getClientSendTime());
        message.setChatType(protocol.getChatSession().getChatType());
        message.setSessionKey(protocol.getChatSession().getSessionKey());
        message.setIp(1);
        return message;
    }

}
