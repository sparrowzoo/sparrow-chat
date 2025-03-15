package com.sparrow.chat.domain.repository;

import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.bo.Protocol;
import com.sparrow.chat.protocol.dto.MessageDTO;
import com.sparrow.chat.protocol.query.MessageCancelQuery;
import com.sparrow.protocol.BusinessException;

import java.util.List;
import java.util.Map;

public interface MessageRepository {
    void cancel(MessageCancelQuery messageCancel, ChatUser sender) throws BusinessException;

    String saveImageContent(Protocol content);

    void saveMessage(Protocol message);

    Map<String, Long> getLastRead(ChatUser me, List<String> sessionKeys);

    List<MessageDTO> getMessageBySession(String session);
}
