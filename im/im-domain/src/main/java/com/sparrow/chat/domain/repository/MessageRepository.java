package com.sparrow.chat.domain.repository;

import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.bo.Protocol;
import com.sparrow.chat.protocol.dto.MessageDTO;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.query.MessageCancelQuery;
import com.sparrow.chat.protocol.query.MessageQuery;
import com.sparrow.protocol.BusinessException;

import java.util.List;

public interface MessageRepository {
    void cancel(MessageCancelQuery messageCancel, ChatUser sender) throws BusinessException;

    void saveMessage(Protocol message,Long ip);

    List<MessageDTO> getMessageBySession(String session);

    List<MessageDTO> getHistoryMessage(MessageQuery query);

    /**
     * 填充最后一条消息和未读消息数
     * @param sessionMap
     * @return
     */
    void fillSession(List<SessionDTO> sessionMap);
}
