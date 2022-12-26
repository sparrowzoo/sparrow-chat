package com.sparrow.chat.init;

import com.sparrow.chat.commons.Chat;
import com.sparrow.chat.commons.PropertyAccessBuilder;
import com.sparrow.chat.commons.RedisKey;
import com.sparrow.chat.protocol.QunDTO;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class InitQun {

    @Autowired
    private RedisTemplate redisTemplate;

    private Json json = JsonFactory.getProvider();


    public void buildQunOfContact(Integer userId,String qunId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, Chat.CHAT_TYPE_1_2_N);
        String userQunKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        redisTemplate.opsForList().rightPush(userQunKey, qunId);
    }

    public void initQun(String qunId) {
        PropertyAccessor qunPropertyAccessor = PropertyAccessBuilder.buildByQunId(qunId);
        String qunKey = PlaceHolderParser.parse(RedisKey.QUN, qunPropertyAccessor);

        QunDTO qunDto = new QunDTO();
        qunDto.setQunId(qunId);
        qunDto.setQunName("聊天室"+qunId);
        qunDto.setNationality("england");
        qunDto.setUnit("school");
        qunDto.setFlagUrl("http://r.sparrowzoo.net/images/flag.jpg");
        qunDto.setAnnouncement("Announcement");
        qunDto.setUnitType("school-type");
        qunDto.setUnitIcon("http://r.sparrowzoo.net/images/unit_icon.jpg");
        redisTemplate.opsForValue().set(qunKey, this.json.toString(qunDto));

        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByQunId(qunId);
        String userOfQunKey = PlaceHolderParser.parse(RedisKey.USER_ID_OF_QUN, propertyAccessor);

        for (int i = 0; i < 100; i++) {
            redisTemplate.opsForList().rightPush(userOfQunKey, i + "");
        }
    }
}
