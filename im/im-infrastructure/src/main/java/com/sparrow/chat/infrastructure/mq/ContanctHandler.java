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

import com.sparrow.chat.contact.ContactServiceApi;
import com.sparrow.chat.contact.protocol.dto.FriendDetailDTO;
import com.sparrow.chat.contact.protocol.event.ContactEvent;
import com.sparrow.chat.domain.repository.ContactRepository;
import com.sparrow.spring.mq.AbstractSpringMQHandler;
import jakarta.inject.Inject;
import jakarta.inject.Named;

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
