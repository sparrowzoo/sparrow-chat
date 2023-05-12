package com.sparrow.chat.repository;

import com.sparrow.chat.protocol.param.MessageCancelParam;
import com.sparrow.chat.protocol.dto.MessageDTO;
import com.sparrow.chat.protocol.param.MessageReadParam;
import com.sparrow.chat.domain.netty.Protocol;
import com.sparrow.protocol.BusinessException;
import java.util.List;
import java.util.Map;

public interface MessageRepository {
    void cancel(MessageCancelParam messageCancel) throws BusinessException;

    String saveImageContent(Protocol content);

    void saveMessage(Protocol message);

    void read(MessageReadParam messageRead);

    Map<String, Long> getLastRead(Integer me, List<String> sessionKeys);

    List<MessageDTO> getMessageBySession(String session);
}
