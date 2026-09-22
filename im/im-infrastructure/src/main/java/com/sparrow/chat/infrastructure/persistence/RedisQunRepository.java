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

import com.alibaba.fastjson.JSON;
import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.domain.repository.QunRepository;
import com.sparrow.chat.infrastructure.commons.PropertyAccessBuilder;
import com.sparrow.chat.infrastructure.commons.RedisKey;
import com.sparrow.core.algorithm.collections.KeyCollectionUpsertSplitter;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.StringUtility;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class RedisQunRepository implements QunRepository {

    @Inject
    private RedisTemplate redisTemplate;

    @Override
    public List<Long> getUserIdList(String qunId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByQunId(qunId);
        String userOfQunKey = PlaceHolderParser.parse(RedisKey.MEMBER_OF_QUN, propertyAccessor);
        Set<String> originUserIds = this.redisTemplate.opsForSet().members(userOfQunKey);
        if (CollectionsUtility.isNullOrEmpty(originUserIds)) {
            return null;
        }
        List<Long> userIds = new ArrayList<>(originUserIds.size());
        for (String userId : originUserIds) {
            userIds.add(Long.parseLong(userId));
        }
        return userIds;
    }

    @Override
    public void syncQunMember(Long qunId, List<Long> memberIds) {
        if (CollectionsUtility.isNullOrEmpty(memberIds)) {
            return;
        }
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByQunId(qunId + "");
        String userOfQunKey = PlaceHolderParser.parse(RedisKey.MEMBER_OF_QUN, propertyAccessor);
        List<Long> oldMembers = this.getUserIdList(qunId.toString());
        KeyCollectionUpsertSplitter<Long> keyCollectionUpsertSplitter =
                new KeyCollectionUpsertSplitter<>(oldMembers, memberIds);
        keyCollectionUpsertSplitter.split();
        List<Long> deletingMembers = keyCollectionUpsertSplitter.getDeleteSet();
        List<Long> insertingMembers = keyCollectionUpsertSplitter.getInsertSet();
        if (!CollectionsUtility.isNullOrEmpty(insertingMembers)) {
            String[] insertingIds = new String[insertingMembers.size()];
            for (int i = 0; i < insertingMembers.size(); i++) {
                insertingIds[i] = insertingMembers.get(i).toString();
            }
            this.redisTemplate.opsForSet().add(userOfQunKey, insertingIds);
        }
        if (!CollectionsUtility.isNullOrEmpty(deletingMembers)) {
            String[] deletingIds = new String[deletingMembers.size()];
            for (int i = 0; i < deletingMembers.size(); i++) {
                deletingIds[i] = deletingMembers.get(i).toString();
            }
            this.redisTemplate.opsForSet().remove(userOfQunKey, deletingIds);
        }
    }

    @Override
    public boolean isQunMember(Long qunId, Long userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByQunId(qunId + "");
        String userOfQunKey = PlaceHolderParser.parse(RedisKey.MEMBER_OF_QUN, propertyAccessor);
        return this.redisTemplate.opsForSet().isMember(userOfQunKey,userId.toString());
    }

    @Override
    public Map<String, QunDTO> getQunMap(Set<String> qunIds) {
        List<String> qunKeys = new ArrayList<>(qunIds.size());
        for (String qunId : qunIds) {
            PropertyAccessor qunPropertyAccessor = PropertyAccessBuilder.buildByQunId(qunId.toString());
            String qunKey = PlaceHolderParser.parse(RedisKey.QUN, qunPropertyAccessor);
            qunKeys.add(qunKey);
        }
        List<String> quns = this.redisTemplate.opsForValue().multiGet(qunKeys);
        Map<String,QunDTO> qunDtos = new HashMap<>(quns.size());
        for (String qun : quns) {
            if (StringUtility.isNullOrEmpty(qun)) {
                log.error("qun");
                continue;
            }
            QunDTO qunDto = JSON.parseObject(qun, QunDTO.class);
            qunDtos.put(qunDto.getId().toString(),qunDto);
        }
        return qunDtos;
    }
}
