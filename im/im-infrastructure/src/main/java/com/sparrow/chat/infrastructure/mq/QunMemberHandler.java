package com.sparrow.chat.infrastructure.mq;

import com.sparrow.chat.contact.protocol.event.QunMemberEvent;
import com.sparrow.chat.domain.repository.QunRepository;
import com.sparrow.spring.starter.mq.AbstractSpringMQHandler;
import com.sparrow.chat.dao.sparrow.contact.QunServiceApi;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class QunMemberHandler extends AbstractSpringMQHandler<QunMemberEvent> {
    private QunServiceApi qunService;

    @Inject
    private QunRepository qunRepository;

    @Override
    public void handle(QunMemberEvent qunMemberEvent) throws Throwable {
        List<Long> members = this.qunService.getMemberById(qunMemberEvent.getQunId());
        this.qunRepository.syncQunMember(qunMemberEvent.getQunId(), members);
    }
}
