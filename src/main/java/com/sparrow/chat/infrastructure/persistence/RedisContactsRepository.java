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
import com.sparrow.utility.StringUtility;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisContactsRepository implements ContactRepository {

    private static Logger logger= LoggerFactory.getLogger(RedisContactsRepository.class);
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
            if(StringUtility.isNullOrEmpty(qun)){
                logger.error("qun");
                continue;
            }
            QunDTO qunDto = this.json.parse(qun, QunDTO.class);
            List<Integer> userIds = qunMembersMap.get(qunDto.getQunId());
            List<UserDTO> userDtos = this.getUsersByIds(userIds);
            qunDto.setMembers(userDtos);
            qunDtos.add(qunDto);
        }
        return qunDtos;
    }

    @Override public List<UserDTO> getFriendsByUserId(Integer userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, Chat.CHAT_TYPE_1_2_1);
        String user121ContactKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        //好友列表
        Set<ZSetOperations.TypedTuple<String>> users = this.redisTemplate.opsForZSet().rangeWithScores(user121ContactKey, 0, -1);
        if (CollectionsUtility.isNullOrEmpty(users)) {
            return Collections.emptyList();
        }
        Map<Integer, Long> friendIdAddTimeMap = new HashMap<>(users.size());
        for (ZSetOperations.TypedTuple<String> friend : users) {
            if (friend.getValue() == null) {
                continue;
            }
            long addTime = friend.getScore() == null ? 0 : friend.getScore().longValue();
            friendIdAddTimeMap.put(Integer.valueOf(friend.getValue()), addTime);
        }
        //通讯录加自己
        friendIdAddTimeMap.put(userId, 0L);
        List<UserDTO> userDtos = getUsersByIds(friendIdAddTimeMap.keySet());
        for (UserDTO userDto : userDtos) {
            userDto.setAddTime(friendIdAddTimeMap.get(userDto.getUserId()));
        }
        Collections.sort(userDtos);
        return userDtos;
    }

    @Override public List<UserDTO> getUsersByIds(Collection<Integer> userIds) {
        List<String> userKeys = new ArrayList<>(userIds.size());
        for (Integer userId : userIds) {
            PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByUserId(userId);
            String userKey = PlaceHolderParser.parse(RedisKey.USER, propertyAccessor);
            userKeys.add(userKey)   ;
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

    @Override public void addFriend(Integer userId, Integer friendId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, Chat.CHAT_TYPE_1_2_1);
        String user121ContactKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        this.redisTemplate.opsForZSet().add(user121ContactKey, friendId, System.currentTimeMillis());
    }
}
