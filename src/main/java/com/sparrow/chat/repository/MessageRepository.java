package com.sparrow.chat.repository;

import com.sparrow.chat.protocol.MessageDTO;
import com.sparrow.chat.protocol.MessageReadParam;
import com.sparrow.chat.protocol.Protocol;
import java.util.List;
import java.util.Map;

public interface MessageRepository {
    String saveImageContent(Protocol content);

    String getImageContent(String imageId);

    void saveMessage(Protocol message);

    void read(MessageReadParam messageRead);

    Map<String, Long> getLastRead(Integer me, List<String> sessionKeys);

    List<MessageDTO> getMessageBySession(String session);
}
