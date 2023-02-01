package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.commons.Chat;
import com.sparrow.chat.commons.PropertyAccessBuilder;
import com.sparrow.chat.commons.RedisKey;
import com.sparrow.chat.protocol.QunDTO;
import com.sparrow.chat.protocol.UserDTO;
import com.sparrow.chat.repository.ContactRepository;
import com.sparrow.chat.repository.QunRepository;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import com.sparrow.utility.CollectionsUtility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisContactsRepository implements ContactRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private QunRepository qunRepository;

    private Json json = JsonFactory.getProvider();

    @Override public List<QunDTO> getQunsByUserId(Integer userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, Chat.CHAT_TYPE_1_2_N);
        String userQunKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        List<String> qunIds = this.redisTemplate.opsForList().range(userQunKey, 0, Integer.MAX_VALUE);
        List<String> qunKeys = new ArrayList<>(qunIds.size());
        Map<String, List<Integer>> qunMembersMap = new HashMap<>(qunIds.size());
        for (String qunId : qunIds) {
            PropertyAccessor qunPropertyAccessor = PropertyAccessBuilder.buildByQunId(qunId);
            String qunKey = PlaceHolderParser.parse(RedisKey.QUN, qunPropertyAccessor);
            qunKeys.add(qunKey);
            List<Integer> usersOfQun = this.qunRepository.getUserIdList(qunId);
            qunMembersMap.put(qunId, usersOfQun);
        }
        List<String> quns = this.redisTemplate.opsForValue().multiGet(qunKeys);
        List<QunDTO> qunDtos = new ArrayList<>(quns.size());
        for (String qun : quns) {
            QunDTO qunDto = this.json.parse(qun, QunDTO.class);
            List<Integer> userIds = qunMembersMap.get(qunDto.getQunId());
            List<UserDTO> userDtos = this.getUsersByIds(userIds);
            qunDto.setMembers(userDtos);
            qunDtos.add(qunDto);
        }
        return qunDtos;
    }

    public void clearContactCache(Integer userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, Chat.CHAT_TYPE_1_2_1);
        String user121ContactKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        this.redisTemplate.delete(user121ContactKey);
    }

    @Override public List<UserDTO> getFriendsByUserId(Integer userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, Chat.CHAT_TYPE_1_2_1);
        String user121ContactKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        //好友列表
        List<String> originUserIds = this.redisTemplate.opsForList().range(user121ContactKey, 0, Integer.MAX_VALUE);
        if (CollectionsUtility.isNullOrEmpty(originUserIds)) {
            return Collections.emptyList();
        }
        List<Integer> userIds = new ArrayList<>(originUserIds.size());
        for (String originUserId : originUserIds) {
            userIds.add(Integer.parseInt(originUserId));
        }
        //通讯录加自己
        userIds.add(userId);
        return getUsersByIds(userIds);
    }

    @Override public List<UserDTO> getUsersByIds(List<Integer> userIds) {
        List<String> userKeys = new ArrayList<>(userIds.size());
        for (Integer userId : userIds) {
            PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByUserId(userId);
            String userKey = PlaceHolderParser.parse(RedisKey.USER, propertyAccessor);
            userKeys.add(userKey);
        }
        //好友列表
        List<String> users = this.redisTemplate.opsForValue().multiGet(userKeys);
        if (CollectionsUtility.isNullOrEmpty(users)) {
            return Collections.emptyList();
        }
        List<UserDTO> userDtos = new ArrayList<>(users.size());
        for (String user : users) {
            userDtos.add(this.json.parse(user, UserDTO.class));
        }
        return userDtos;
    }
}
