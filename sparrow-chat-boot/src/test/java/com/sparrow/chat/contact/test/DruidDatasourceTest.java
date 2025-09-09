package com.sparrow.chat.contact.test;

import com.sparrow.chat.boot.ApplicationBoot;
import com.sparrow.chat.contact.service.SecretService;
import com.sparrow.spring.starter.test.SparrowTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.sql.DataSource;


/**
 * DependencyInjectionTestExecutionListener
 * 在测试类中 在容器启动前执行需要定义一个TestExecutionListener
 * SparrowTestExecutionListener
 * 但在测试类中使用@Autowired注入的bean为null
 * 需要配置DependencyInjectionTestExecutionListener 提供支持
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationBoot.class})
@TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class DruidDatasourceTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testDruidDatasource() {
        System.out.println("testUserSecret" + dataSource);
    }
}

