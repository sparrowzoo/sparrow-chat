package com.sparrow.chat.contact;

import com.sparrow.chat.contact.protocol.dto.FriendDetailDTO;
import com.sparrow.protocol.BusinessException;

import java.util.List;

public interface ContactServiceApi {
    List<FriendDetailDTO> getFriends(Long userId) throws BusinessException;
}
