package com.sparrow.chat.repository;

import java.util.List;

public interface QunRepository {
    List<Long> getUserIdList(String qunId);

    void syncQunMember(Long qunId,List<Long> memberIds);
}
