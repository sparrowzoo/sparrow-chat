package com.sparrow.im;

import com.sparrow.chat.boot.Application;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.datasource.ConnectionContextHolder;
import com.sparrow.datasource.ConnectionReleaser;
import com.sparrow.spring.starter.test.SparrowTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * DependencyInjectionTestExecutionListener
 * 在测试类中 在容器启动前执行需要定义一个TestExecutionListener
 * SparrowTestExecutionListener
 * 但在测试类中使用@Autowired注入的bean为null
 * 需要配置DependencyInjectionTestExecutionListener 提供支持
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class DruidDatasourceTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ConnectionReleaser connectionReleaser;

    @Test
    public void testDruidDatasource() throws SQLException {
        ConnectionContextHolder connectionHolder = ApplicationContext.getContainer().getBean(ConnectionContextHolder.class);

        Connection connection = dataSource.getConnection();
        connectionHolder.bindConnection(connection);
        Statement statement = connection.createStatement();
        statement.execute("SELECT 1");

        connectionHolder.unbindConnection(statement.getConnection());
        System.out.println("testUserSecret" + dataSource);
        System.out.println("connectionReleaser"+connectionReleaser);
    }
}

