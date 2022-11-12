package com.sparrow.chat.repository.impl;

import com.sparrow.chat.commons.PropertyAccessBuilder;
import com.sparrow.chat.commons.RedisKey;
import com.sparrow.chat.repository.QunRepository;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisQunRepository implements QunRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override public List<Integer> getUserIdList(String qunId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildChatPropertyAccessorBySessionKey(qunId);
        String userOfQunKey = PlaceHolderParser.parse(RedisKey.USER_ID_OF_QUN, propertyAccessor);
        return this.redisTemplate.opsForList().range(userOfQunKey, 0, Integer.MAX_VALUE);
    }
}
