package com.sparrow.chat.infrastructure.converter;

import com.sparrow.chat.domain.netty.Protocol;
import com.sparrow.chat.protocol.dto.MessageDTO;
import org.springframework.stereotype.Component;

@Component
public class MessageConverter {

    public MessageDTO assembleMessage(Protocol protocol) {
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
}
