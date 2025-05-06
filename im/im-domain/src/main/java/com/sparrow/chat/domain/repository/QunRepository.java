package com.sparrow.chat.domain.repository;

import com.sparrow.chat.protocol.dto.QunDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface QunRepository {
    List<Long> getUserIdList(String qunId);

    void syncQunMember(Long qunId,List<Long> memberIds);

    boolean isQunMember(Long qunId,Long userId);

    Map<String,QunDTO> getQunMap(Set<String> qunIds);
}
