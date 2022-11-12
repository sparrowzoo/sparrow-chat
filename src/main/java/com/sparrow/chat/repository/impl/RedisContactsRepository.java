package com.sparrow.chat.repository.impl;

import com.sparrow.chat.commons.Chat;
import com.sparrow.chat.commons.PropertyAccessBuilder;
import com.sparrow.chat.commons.RedisKey;
import com.sparrow.chat.protocol.QunDTO;
import com.sparrow.chat.protocol.UserDTO;
import com.sparrow.chat.repository.ContactsRepository;
import com.sparrow.chat.repository.QunRepository;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisContactsRepository implements ContactsRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private QunRepository qunRepository;

    @Override public List<QunDTO> getQunsByUserId(Integer userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, Chat.CHAT_TYPE_1_2_N);
        String user12nContactKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        List<String> qunIds = this.redisTemplate.opsForList().range(user12nContactKey, 0, Integer.MAX_VALUE);
        List<String> qunKeys = new ArrayList<>(qunIds.size());
        Map<String, List<Integer>> qunMembersMap = new HashMap<>(qunIds.size());
        for (String qunId : qunIds) {
            PropertyAccessor qunPropertyAccessor = PropertyAccessBuilder.buildChatPropertyAccessorByQunId(qunId);
            String qunKey = PlaceHolderParser.parse(RedisKey.QUN, qunPropertyAccessor);
            qunKeys.add(qunKey);
            List<Integer> usersOfQun = this.qunRepository.getUserIdList(qunId);
            qunMembersMap.put(qunId, usersOfQun);
        }
        List<QunDTO> qunDtos = this.redisTemplate.opsForValue().multiGet(qunKeys);
        for (QunDTO qun : qunDtos) {
            List<Integer> userIds = qunMembersMap.get(qun.getQunId());
            List<UserDTO> userDtos = this.getUsersByIds(userIds);
            qun.setMembers(userDtos);
        }
        return qunDtos;
    }

    @Override public List<UserDTO> getFriendsByUserId(Integer userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, Chat.CHAT_TYPE_1_2_1);
        String user121ContactKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        //好友列表
        List<Integer> userIds = this.redisTemplate.opsForList().range(user121ContactKey, 0, Integer.MAX_VALUE);
        if (userIds == null) {
            return new ArrayList<>();
        }
        return getUsersByIds(userIds);
    }

    @Override public List<UserDTO> getUsersByIds(List<Integer> userIds) {
        List<String> userKeys = new ArrayList<>(userIds.size());
        for (Integer userId : userIds) {
            PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildChatPropertyAccessorByUserId(userId);
            String userKey = PlaceHolderParser.parse(RedisKey.USER, propertyAccessor);
            userKeys.add(userKey);
        }
        //好友列表
        return this.redisTemplate.opsForValue().multiGet(userKeys);
    }
}
