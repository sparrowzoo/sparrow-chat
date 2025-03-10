package com.sparrow.chat.domain.repository;

import com.sparrow.chat.protocol.dto.QunDTO;
import com.sparrow.chat.protocol.dto.UserDTO;
import java.util.Collection;
import java.util.List;

public interface ContactRepository {
    List<QunDTO> getQunsByUserId(Long userId);

    Boolean existQunByUserId(Long userId,String qunId);

    List<UserDTO> getFriendsByUserId(Long userId);

    List<UserDTO> getUsersByIds(Collection<Long> userIds);

    void addFriend(Long userId,Long friendId);
}
