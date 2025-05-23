package com.sparrow.chat.infrastructure.mq;

import com.sparrow.chat.contact.ContactServiceApi;
import com.sparrow.chat.contact.protocol.dto.FriendDetailDTO;
import com.sparrow.chat.contact.protocol.event.ContactEvent;
import com.sparrow.chat.domain.repository.ContactRepository;
import com.sparrow.spring.starter.mq.AbstractSpringMQHandler;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class ContanctHandler extends AbstractSpringMQHandler<ContactEvent> {
    @Inject
    private ContactServiceApi contactServiceApi;

    @Inject
    private ContactRepository contactRepository;

    @Override
    public void handle(ContactEvent contactEvent) throws Throwable {
        List<FriendDetailDTO> members = this.contactServiceApi.getFriends(contactEvent.getUserId());
        for (FriendDetailDTO member : members) {
            this.contactRepository.addFriend(contactEvent.getUserId(), member.getFriendId(),member.getAddTime());
        }
    }
}
