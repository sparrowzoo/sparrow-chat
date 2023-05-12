package com.sparrow.chat.repository;

import com.sparrow.chat.protocol.dto.QunDTO;
import com.sparrow.chat.protocol.dto.UserDTO;
import java.util.Collection;
import java.util.List;

public interface ContactRepository {
    List<QunDTO> getQunsByUserId(Integer userId);

    Boolean existQunByUserId(Integer userId,String qunId);

    List<UserDTO> getFriendsByUserId(Integer userId);

    List<UserDTO> getUsersByIds(Collection<Integer> userIds);

    void addFriend(Integer userId,Integer friendId);
}
