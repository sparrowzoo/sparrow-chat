package com.sparrowzoo.chat.dao.sparrow.dao.contact;

import com.sparrow.protocol.BusinessException;

import java.util.List;

public interface QunServiceApi {
    List<Long> getMemberById(Long qunId) throws BusinessException;
}
