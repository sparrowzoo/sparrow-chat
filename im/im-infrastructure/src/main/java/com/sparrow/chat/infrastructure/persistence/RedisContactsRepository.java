/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.domain.repository.ContactRepository;
import com.sparrow.chat.domain.repository.QunRepository;
import com.sparrow.chat.infrastructure.commons.PropertyAccessBuilder;
import com.sparrow.chat.infrastructure.commons.RedisKey;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import com.sparrow.utility.CollectionsUtility;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.sparrow.chat.protocol.constant.Chat.CHAT_TYPE_1_2_1;
import static com.sparrow.chat.protocol.constant.Chat.CHAT_TYPE_1_2_N;

@Component
public class RedisContactsRepository implements ContactRepository {
    private static Logger logger = LoggerFactory.getLogger(RedisContactsRepository.class);
    @Autowired
    private RedisTemplate redisTemplate;

    @Inject
    private QunRepository qunRepository;
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
    public Map<String, QunDTO> getQunsByUserId(Long userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, CHAT_TYPE_1_2_N);
        String userQunKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        Set<String> qunIds = this.redisTemplate.opsForZSet().range(userQunKey, 0, Integer.MAX_VALUE);
        return this.qunRepository.getQunMap(qunIds);
    }

    @Override
    public Map<Long, Long> getFriendsByUserId(Long userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, CHAT_TYPE_1_2_1);
        String user121ContactKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        //好友列表
        Set<ZSetOperations.TypedTuple<String>> users = this.redisTemplate.opsForZSet().rangeWithScores(user121ContactKey, 0, -1);
        if (CollectionsUtility.isNullOrEmpty(users)) {
            return Collections.emptyMap();
        }
        Map<Long, Long> friendIdAddTimeMap = new HashMap<>(users.size());
        for (ZSetOperations.TypedTuple<String> friend : users) {
            if (friend.getValue() == null) {
                continue;
            }
            long addTime = friend.getScore() == null ? 0 : friend.getScore().longValue();
            friendIdAddTimeMap.put(Long.parseLong(friend.getValue()), addTime);
        }
        return friendIdAddTimeMap;
    }


    @Override
    public void addFriend(Long userId, Long friendId, Long addTime) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, CHAT_TYPE_1_2_1);
        String user121ContactKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        this.redisTemplate.opsForZSet().add(user121ContactKey, friendId, addTime);
    }
}
