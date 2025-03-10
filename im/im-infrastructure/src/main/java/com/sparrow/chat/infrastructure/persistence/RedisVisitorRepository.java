package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.infrastructure.commons.RedisKey;
import com.sparrow.chat.domain.repository.VisitorRepository;
import com.sparrow.constant.DateTime;
import com.sparrow.utility.DateTimeUtility;
import org.springframework.data.redis.core.RedisTemplate;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class RedisVisitorRepository implements VisitorRepository {
    @Inject
    private RedisTemplate redisTemplate;

    @Override
    public Long getVisitorId() {
        long visitorNumber = redisTemplate.opsForValue().increment(RedisKey.VISITOR_ID);
        String mmdd = DateTimeUtility.getFormatTime(System.currentTimeMillis(), DateTime.FORMAT_MMDD);
        return Long.parseLong(mmdd + "" + visitorNumber);
    }
}
