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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class RedisQunRepository implements QunRepository {

    @Inject
    private RedisTemplate redisTemplate;

    @Override
    public List<Long> getUserIdList(String qunId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByQunId(qunId);
        String userOfQunKey = PlaceHolderParser.parse(RedisKey.USER_ID_OF_QUN, propertyAccessor);
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
        String userOfQunKey = PlaceHolderParser.parse(RedisKey.USER_ID_OF_QUN, propertyAccessor);
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
}
