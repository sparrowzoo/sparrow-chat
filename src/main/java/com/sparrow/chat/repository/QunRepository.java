package com.sparrow.chat.repository;

import com.sparrow.chat.protocol.QunDTO;
import java.util.List;

public interface QunRepository {
    List<Integer> getUserIdList(String qunId);

    QunDTO getQun(Long qunId);
}
