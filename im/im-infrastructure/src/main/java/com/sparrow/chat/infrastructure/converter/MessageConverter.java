package com.sparrow.chat.infrastructure.converter;

import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.bo.Protocol;
import com.sparrow.chat.im.po.Message;
import com.sparrow.chat.protocol.dto.MessageDTO;
import com.sparrow.chat.protocol.query.ChatUserQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageConverter {

    public MessageDTO convertMessage(Protocol protocol) {
        MessageDTO message = new MessageDTO();
        message.setMessageType(protocol.getMessageType());
        message.setContent(protocol.getContent());
        message.setSender(protocol.getSender().toChatUserQuery());
        if (protocol.isOne2One()) {
            message.setReceiver(protocol.getReceiver().toChatUserQuery());
        }
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
        return messageDTO;
    }

    public List<MessageDTO> convertMessages(List<Message> messages) {
        List<MessageDTO> messageDTOs = new ArrayList<>();
        for (Message message : messages) {
            messageDTOs.add(convertMessage(message));
        }
        return messageDTOs;
    }

    public Message convertPo(Protocol protocol,Long ip) {
        ChatUser sender = protocol.getSender();
        ChatUser receiver = protocol.getReceiver();
        Message message = new Message();
        message.setMessageType(protocol.getMessageType());
        message.setContent(protocol.getContent().trim());
        message.setSender(sender.getId());
        message.setSenderCategory(sender.getCategory());
        if (protocol.isOne2One()) {
            message.setReceiver(receiver.getId());
            message.setReceiverCategory(receiver.getCategory());
        } else {
            message.setReceiver("");
            message.setReceiverCategory(0);
        }
        message.setServerTime(protocol.getServerTime());
        message.setClientSendTime(protocol.getClientSendTime());
        message.setChatType(protocol.getChatSession().getChatType());
        message.setSessionKey(protocol.getChatSession().key());
        message.setIp(ip);
        return message;
    }

}
