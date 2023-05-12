package com.sparrow.chat.boot;

import com.sparrow.chat.domain.netty.WebSocketServer;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * NativeLibraryLoader netty_transport_native_kqueue_x86_64 cannot be loaded 注意日志的级别是debug，所以直接无视就行 参考：
 * https://github.com/netty/netty/issues/8179
 */
@SpringBootApplication(scanBasePackages = "com.sparrow.*", exclude = {DataSourceAutoConfiguration.class})
public class Application {
    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.addListeners(new ApplicationListener<ApplicationStartingEvent>() {
            @Override public void onApplicationEvent(ApplicationStartingEvent event) {
                Container container = ApplicationContext.getContainer();
                ContainerBuilder builder = new ContainerBuilder()
                    .scanBasePackage("com.sparrow")
                    .initController(false)
                    .initSingletonBean(false)
                    .initInterceptor(false);
                container.init(builder);
            }
        });

        springApplication.addListeners((ApplicationListener<ContextRefreshedEvent>) contextRefreshedEvent -> {
            try {
                WebSocketServer.start(args);
            } catch (Exception e) {
                log.error("start error", e);
            }
            log.info("application startup at {}", contextRefreshedEvent.getTimestamp());
        });
        springApplication.addListeners(new ApplicationListener<ContextClosedEvent>() {
            @Override
            public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
                log.info("application closed at at {}", contextClosedEvent.getTimestamp());
            }
        });
        springApplication.run(args);
    }
}

