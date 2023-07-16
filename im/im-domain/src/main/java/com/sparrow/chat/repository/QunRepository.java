package com.sparrow.chat.repository;

import java.util.List;

public interface QunRepository {
    List<Integer> getUserIdList(String qunId);

    void syncQunMember(Long qunId,List<Integer> memberIds);
}
