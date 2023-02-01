package com.sparrow.chat.repository;

import com.sparrow.chat.protocol.QunDTO;
import com.sparrow.chat.protocol.UserDTO;
import java.util.List;

public interface ContactRepository {
    List<QunDTO> getQunsByUserId(Integer userId);

    List<UserDTO> getFriendsByUserId(Integer userId);

    List<UserDTO> getUsersByIds(List<Integer> userIds);

    void clearContactCache(Integer userId);
}