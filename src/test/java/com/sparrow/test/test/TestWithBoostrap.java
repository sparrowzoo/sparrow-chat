package com.sparrow.test.test;

import com.sparrow.chat.boot.Application;
import com.sparrow.chat.dao.QunDAO;
import com.sparrow.spring.starter.test.SparrowTestExecutionListener;
import org.junit.Assert;
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
public class TestWithBoostrap {
    @Autowired
    private QunDAO qunDAO;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testJson(){
        this.redisTemplate.opsForValue().set("a","test remote redis");
        System.out.println(this.redisTemplate.opsForValue().get("a"));
    }

    @Test
    public void testSpring() {
        System.out.println(qunDAO);
        Assert.assertEquals(1,2);
    }
}
