package com.sparrow.chat.init;

import com.sparrow.chat.commons.Chat;
import com.sparrow.chat.commons.PropertyAccessBuilder;
import com.sparrow.chat.commons.RedisKey;
import com.sparrow.chat.protocol.UserDTO;
import com.sparrow.chat.repository.ContactRepository;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.support.PlaceHolderParser;
import com.sparrow.support.PropertyAccessor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class InitUser {
    int userCount = 100;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ContactRepository contactsRepository;

    private Json json = JsonFactory.getProvider();

    public void initUser() {
        List<Integer> userIds = new ArrayList<>(userCount);
        for (int i = 0; i < userCount; i++) {
            int userId = i;
            userIds.add(userId);
            PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildByUserId(userId);
            String userKey = PlaceHolderParser.parse(RedisKey.USER, propertyAccessor);
            UserDTO user = new UserDTO();
            user.setAvatar("http://r.sparrowzoo.net/images/user.png");
            user.setUserId(userId);
            user.setFlagUrl("http://r.sparrowzoo.net/images/flag.jpg");
            user.setNationality("china");
            user.setUserName("zhangsan" + userId);
            user.setUnit("school");
            user.setUnitType("school");
            user.setUnitIcon("icon");
            redisTemplate.opsForValue().set(userKey, this.json.toString(user));
        }

        List<UserDTO> users = contactsRepository.getUsersByIds(userIds);
    }

    public void initFriends(Integer userId) {
        PropertyAccessor propertyAccessor = PropertyAccessBuilder.buildContacts(userId, Chat.CHAT_TYPE_1_2_1);
        String user121ContactKey = PlaceHolderParser.parse(RedisKey.USER_CONTACTS, propertyAccessor);
        for (int i = 0; i < 100; i++) {
            if (i == userId) {
                continue;
            }
            this.redisTemplate.opsForList().rightPush(user121ContactKey, i + "");
        }
    }
}
