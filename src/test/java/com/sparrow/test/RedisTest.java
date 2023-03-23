package com.sparrow.test;

import com.alibaba.fastjson.JSON;
import com.sparrow.chat.boot.Application;
import com.sparrow.chat.infrastructure.persistence.RedisContactsRepository;
import com.sparrow.chat.protocol.QunDTO;
import com.sparrow.chat.protocol.UserDTO;
import com.sparrow.spring.starter.test.SparrowTestExecutionListener;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
/**
 * @TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
 * 以test 容器启动前执行
 *
 * 在test 容器中
 * ApplicationListener 失效
 */
@TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisContactsRepository redisContactsRepository;

    @Test
    public void testUser() {
        String user = (String) this.redisTemplate.opsForValue().get("user:1");
        UserDTO userDto = JSON.parseObject(user, UserDTO.class);
        System.out.println(user);
    }

    @Test
    public void testContact() {
        //List<UserDTO> users = redisContactsRepository.getFriendsByUserId(6);
        List<QunDTO> quns = redisContactsRepository.getQunsByUserId(6);
    }
}
