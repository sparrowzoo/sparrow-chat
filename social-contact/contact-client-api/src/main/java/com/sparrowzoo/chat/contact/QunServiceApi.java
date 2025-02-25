package com.sparrowzoo.chat.contact;

import com.sparrow.protocol.BusinessException;

import java.util.List;

public interface QunServiceApi {
    List<Long> getMemberById(Long qunId) throws BusinessException;
}
