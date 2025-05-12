package com.sparrow.chat.domain.repository;

import com.sparrow.chat.contact.protocol.dto.QunDTO;

import java.util.Map;

public interface ContactRepository {
    Map<String,QunDTO> getQunsByUserId(Long userId);

    Boolean existQunByUserId(Long userId,String qunId);
    Map<Long,Long> getFriendsByUserId(Long userId);
    void addFriend(Long userId,Long friendId,Long addTime);
}
