package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.infrastructure.commons.PropertyAccessBuilder;
import com.sparrow.chat.infrastructure.commons.RedisKey;
import com.sparrow.chat.repository.QunRepository;
import com.sparrow.core.algorithm.collections.KeyCollectionUpsertSplitter;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import com.sparrow.utility.CollectionsUtility;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;

@Component
public class RedisQunRepository implements QunRepository {

    @Inject
    private RedisTemplate redisTemplate;

    @Override
    public List<Integer> getUserIdList(String qunId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByQunId(qunId);
        String userOfQunKey = PlaceHolderParser.parse(RedisKey.USER_ID_OF_QUN, propertyAccessor);
        Set<String> originUserIds = this.redisTemplate.opsForSet().members(userOfQunKey);
        if (CollectionsUtility.isNullOrEmpty(originUserIds)) {
            return Collections.emptyList();
        }
        List<Integer> userIds = new ArrayList<>(originUserIds.size());
        for (String userId : originUserIds) {
            userIds.add(Integer.parseInt(userId));
        }
        return userIds;
    }

    @Override
    public void syncQunMember(Long qunId, List<Integer> memberIds) {
        if (CollectionsUtility.isNullOrEmpty(memberIds)) {
            return;
        }
        List<Integer> oldMembers = this.getUserIdList(qunId.toString());
        KeyCollectionUpsertSplitter<Integer> keyCollectionUpsertSplitter = new KeyCollectionUpsertSplitter<>(oldMembers, memberIds);
        keyCollectionUpsertSplitter.split();
        Collection<Integer> deletingMembers = keyCollectionUpsertSplitter.getDeleteSet();
        Collection<Integer> insertingMembers = keyCollectionUpsertSplitter.getInsertSet();
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByQunId(qunId + "");
        String userOfQunKey = PlaceHolderParser.parse(RedisKey.USER_ID_OF_QUN, propertyAccessor);
        if(!CollectionsUtility.isNullOrEmpty(insertingMembers)) {
            for (Integer member : insertingMembers) {
                this.redisTemplate.opsForSet().add(userOfQunKey, member.toString());
            }
        }
        if(!CollectionsUtility.isNullOrEmpty(deletingMembers)) {
            for (Integer member : deletingMembers) {
                this.redisTemplate.opsForSet().remove(userOfQunKey,member.toString());
            }
        }
    }
}
