package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.domain.repository.ContactRepository;
import com.sparrow.chat.infrastructure.commons.PropertyAccessBuilder;
import com.sparrow.chat.infrastructure.commons.RedisKey;
import com.sparrow.chat.protocol.dto.QunDTO;
import com.sparrow.chat.protocol.dto.UserDTO;
import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.sparrow.chat.protocol.constant.Chat.CHAT_TYPE_1_2_1;
import static com.sparrow.chat.protocol.constant.Chat.CHAT_TYPE_1_2_N;

@Component
public class RedisContactsRepository implements ContactRepository {
    private static Logger logger = LoggerFactory.getLogger(RedisContactsRepository.class);
    @Autowired
    private RedisTemplate redisTemplate;

    private Json json = JsonFactory.getProvider();

    public Boolean existQunByUserId(Long userId, String qunId) {
        if (qunId == null) {
            return false;
        }
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, CHAT_TYPE_1_2_N);
        String userQunKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        Double score = this.redisTemplate.opsForZSet().score(userQunKey, qunId);
        return score != null;
    }

    @Override
    public List<QunDTO> getQunsByUserId(Long userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, CHAT_TYPE_1_2_N);
        String userQunKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        Set<String> qunIds = this.redisTemplate.opsForZSet().range(userQunKey, 0, Integer.MAX_VALUE);
        List<String> qunKeys = new ArrayList<>(qunIds.size());
        for (String qunId : qunIds) {
            PropertyAccessor qunPropertyAccessor = PropertyAccessBuilder.buildByQunId(qunId);
            String qunKey = PlaceHolderParser.parse(RedisKey.QUN, qunPropertyAccessor);
            qunKeys.add(qunKey);
        }
        List<String> quns = this.redisTemplate.opsForValue().multiGet(qunKeys);
        List<QunDTO> qunDtos = new ArrayList<>(quns.size());
        for (String qun : quns) {
            if (StringUtility.isNullOrEmpty(qun)) {
                logger.error("qun");
                continue;
            }
            QunDTO qunDto = this.json.parse(qun, QunDTO.class);
            qunDtos.add(qunDto);
        }
        return qunDtos;
    }

    @Override
    public List<UserDTO> getFriendsByUserId(Long userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, CHAT_TYPE_1_2_1);
        String user121ContactKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        //好友列表
        Set<ZSetOperations.TypedTuple<String>> users = this.redisTemplate.opsForZSet().rangeWithScores(user121ContactKey, 0, -1);
        if (CollectionsUtility.isNullOrEmpty(users)) {
            return Collections.emptyList();
        }
        Map<Long, Long> friendIdAddTimeMap = new HashMap<>(users.size());
        for (ZSetOperations.TypedTuple<String> friend : users) {
            if (friend.getValue() == null) {
                continue;
            }
            long addTime = friend.getScore() == null ? 0 : friend.getScore().longValue();
            friendIdAddTimeMap.put(Long.parseLong(friend.getValue()), addTime);
        }
        //通讯录加自己
        friendIdAddTimeMap.put(userId, 0L);
        List<UserDTO> userDtos = getUsersByIds(friendIdAddTimeMap.keySet());
        for (UserDTO userDto : userDtos) {
            if (userDto == null) {
                logger.error("user dto is null");
                continue;
            }
            ChatUserQuery chatUser=userDto.getChatUser();
            userDto.setAddTime(friendIdAddTimeMap.get(Long.valueOf(chatUser.getId())));
        }
        Collections.sort(userDtos);
        return userDtos;
    }

    @Override
    public List<UserDTO> getUsersByIds(Collection<Long> userIds) {
        List<String> userKeys = new ArrayList<>(userIds.size());
        for (Long userId : userIds) {
            PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByUserId(userId);
            String userKey = PlaceHolderParser.parse(RedisKey.USER, propertyAccessor);
            userKeys.add(userKey);
        }
        //好友列表
        List<String> users = this.redisTemplate.opsForValue().multiGet(userKeys);
        if (CollectionsUtility.isNullOrEmpty(users)) {
            return null;
        }
        List<UserDTO> userDtos = new ArrayList<>(users.size());
        for (String user : users) {
            if (!StringUtility.isNullOrEmpty(user)) {
                userDtos.add(this.json.parse(user, UserDTO.class));
            }
        }
        return userDtos;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, CHAT_TYPE_1_2_1);
        String user121ContactKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        this.redisTemplate.opsForZSet().add(user121ContactKey, friendId, System.currentTimeMillis());
    }
}
