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

public class InitQun {

    private static String qunId = "qun-id-1";

    private static int userId = 100;

    @Autowired
    private RedisTemplate redisTemplate;

    private Json json = JsonFactory.getProvider();


    public void buildQunOfContact() {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, Chat.CHAT_TYPE_1_2_N);
        String userQunKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        redisTemplate.opsForList().rightPush(userQunKey, qunId);
    }

    public void initQun() {
        PropertyAccessor qunPropertyAccessor = PropertyAccessBuilder.buildChatPropertyAccessorByQunId(qunId);
        String qunKey = PlaceHolderParser.parse(RedisKey.QUN, qunPropertyAccessor);

        QunDTO qunDto = new QunDTO();
        qunDto.setQunId(qunId);
        qunDto.setQunName("**聊天室");
        qunDto.setNationality("england");
        qunDto.setUnit("school");
        qunDto.setFlagUrl("http://www.baidu.com");
        qunDto.setAnnouncement("Announcement");
        qunDto.setUnitType("school-type");
        qunDto.setUnitIcon("unit icon");
        redisTemplate.opsForValue().set(qunKey, this.json.toString(qunDto));

        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildChatPropertyAccessorByQunId(qunId);
        String userOfQunKey = PlaceHolderParser.parse(RedisKey.USER_ID_OF_QUN, propertyAccessor);

        for (int i = 0; i < 10; i++) {
            redisTemplate.opsForList().rightPush(userOfQunKey, i + "");
        }
    }
}
