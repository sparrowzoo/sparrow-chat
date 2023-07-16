package com.sparrowzoo.chat.contact;

import com.sparrow.protocol.BusinessException;

import java.util.List;

public interface QunServiceApi {
    List<Integer> getMemberById(Long qunId) throws BusinessException;
}
