package com.sparrow.chat.infrastructure.mq;

import com.sparrow.chat.contact.bo.QunBO;
import com.sparrow.chat.contact.protocol.event.QunMemberEvent;
import com.sparrow.chat.contact.repository.QunRepository;
import com.sparrow.concurrent.SparrowThreadFactory;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.mq.EventHandlerMappingContainer;
import com.sparrow.mq.MQEvent;
import com.sparrow.mq.MQPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.concurrent.*;

@Named
public class ContactMQPublisher implements MQPublisher, InitializingBean {
    @Inject
    private QunRepository qunRepository;
    private static Logger logger = LoggerFactory.getLogger(ContactMQPublisher.class);

    private ExecutorService qunMemberSyncExecutorService;

    private ScheduledExecutorService scheduledExecutorService;


    @Inject
    private EventHandlerMappingContainer queueHandlerMappingContainer;

    /**
     * 可以考虑发事务消息
     *
     * @param event
     */
    @Override
    public void publish(MQEvent event) {
        qunMemberSyncExecutorService.execute(() -> {
            try {
                queueHandlerMappingContainer.get(event.getClass().getName()).handle(event);
            } catch (Throwable e) {
                logger.error("qun member sync error {}", JsonFactory.getProvider().toString(event), e);
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.qunMemberSyncExecutorService = new ThreadPoolExecutor(1, 1, 10, TimeUnit.MINUTES, new ArrayBlockingQueue<>(128), new SparrowThreadFactory.Builder().daemon(true).namingPattern("qun-sync-thread-%d").build(), new ThreadPoolExecutor.DiscardOldestPolicy());
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                new SparrowThreadFactory.Builder().namingPattern("qun-member-all-sync-%d").daemon(true).build());
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                List<QunBO> qunBOS =null;// qunRepository.queryQunPlaza();
                for (QunBO qunBO : qunBOS) {
                    try {
                        publish(new QunMemberEvent(qunBO.getId(), null));
                    } catch (Throwable e) {
                        logger.error("all sync publish error {}", qunBO.getId(), e);
                    }
                }
            }
        }, 60, 5, TimeUnit.SECONDS);
    }
}
