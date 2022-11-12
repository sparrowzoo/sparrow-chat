package com.sparrow.chat.boot;

import com.sparrow.chat.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

@SpringBootApplication(scanBasePackages = "com.sparrow.*")
public class Application {
    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.addListeners(new ApplicationListener<ApplicationStartingEvent>() {
            @Override public void onApplicationEvent(ApplicationStartingEvent event) {

            }
        });

        springApplication.addListeners(new ApplicationListener<ContextRefreshedEvent>() {
            @Override
            public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
                try {
                    WebSocketServer.start(args);
                } catch (Exception e) {
                    log.error("start error", e);
                }
                log.info("application startup at {}", contextRefreshedEvent.getTimestamp());
            }
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

