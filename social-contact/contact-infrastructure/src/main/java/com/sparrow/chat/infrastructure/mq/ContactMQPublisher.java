/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.sparrow.chat.infrastructure.mq;

import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.contact.protocol.event.QunMemberEvent;
import com.sparrow.chat.contact.repository.QunRepository;
import com.sparrow.concurrent.SparrowThreadFactory;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.messaging.domain.service.pub.sub.mapping.PubSubMappingContainer;
import com.sparrow.messaging.protocol.Event;
import com.sparrow.messaging.protocol.api.Publisher;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.List;
import java.util.concurrent.*;

@Named
@Slf4j
public class ContactMQPublisher implements Publisher, SmartInitializingSingleton {
    @Inject
    private QunRepository qunRepository;
    private ExecutorService executorService;
    private ScheduledExecutorService scheduledExecutorService;
    @Inject
    private PubSubMappingContainer pubSubMappingContainer;

    /**
     * 可以考虑发事务消息
     *
     * @param event
     */
    @Override
    public void publish(Event event) {
        executorService.execute(() -> {
            try {
                pubSubMappingContainer.get(event.getClass().getName()).subscribe(event);
            } catch (Throwable e) {
                log.error("qun member sync error {}", JsonFactory.getProvider().toString(event), e);
            }
        });
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

    @Override
    public void afterSingletonsInstantiated() {
        this.executorService = new ThreadPoolExecutor(1, 1, 10, TimeUnit.MINUTES, new ArrayBlockingQueue<>(128), new SparrowThreadFactory.Builder().daemon(true).namingPattern("qun-sync-thread-%d").build(), new ThreadPoolExecutor.DiscardOldestPolicy());
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                new SparrowThreadFactory.Builder().namingPattern("qun-member-all-sync-%d").daemon(true).build());

    }
}
