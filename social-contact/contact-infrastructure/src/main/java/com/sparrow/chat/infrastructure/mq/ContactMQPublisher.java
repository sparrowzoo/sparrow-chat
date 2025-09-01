package com.sparrow.chat.infrastructure.mq;

import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.contact.protocol.event.QunMemberEvent;
import com.sparrow.chat.contact.repository.QunRepository;
import com.sparrow.concurrent.SparrowThreadFactory;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.mq.EventHandlerMappingContainer;
import com.sparrow.mq.MQEvent;
import com.sparrow.mq.MQPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.concurrent.*;

@Named
@Slf4j
public class ContactMQPublisher implements MQPublisher, InitializingBean {
    @Inject
    private QunRepository qunRepository;

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
                log.error("qun member sync error {}", JsonFactory.getProvider().toString(event), e);
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.qunMemberSyncExecutorService = new ThreadPoolExecutor(1, 1, 10, TimeUnit.MINUTES, new ArrayBlockingQueue<>(128), new SparrowThreadFactory.Builder().daemon(true).namingPattern("qun-sync-thread-%d").build(), new ThreadPoolExecutor.DiscardOldestPolicy());
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                new SparrowThreadFactory.Builder().namingPattern("qun-member-all-sync-%d").daemon(true).build());

    }

    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                List<QunDTO> qunBOS = qunRepository.queryQunPlaza();
                for (QunDTO qunDTO : qunBOS) {
                    try {
                        publish(new QunMemberEvent(qunDTO.getId(), null));
                    } catch (Throwable e) {
                        log.error("all sync publish error {}", qunDTO.getId(), e);
                    }
                }
            }
        }, 0, 5, TimeUnit.MINUTES);
    }
}
