package com.sparrow.chat.assemble;

import com.sparrow.chat.protocol.MessageDTO;
import com.sparrow.chat.protocol.Protocol;
import org.springframework.stereotype.Component;

@Component
public class MessageAssemble {

    public MessageDTO assembleMessage(Protocol protocol) {
        MessageDTO message = new MessageDTO();
        message.setMessageType(protocol.getMessageType());
        message.setCharType(protocol.getCharType());

        message.setContent(protocol.getContent());
        message.setSession(protocol.getSession());
        message.setFromUserId(protocol.getFromUserId());
        message.setTargetUserId(protocol.getTargetUserId());
        message.setCharType(protocol.getCharType());
        message.setSendTime(protocol.getSendTime());
        return message;
    }
}
