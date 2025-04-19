package com.sparrow.chat.contact;

import com.sparrow.protocol.BusinessException;

import java.util.List;

public interface ContactServiceApi {
    List<Long> getFriends(Long userId) throws BusinessException;
}
