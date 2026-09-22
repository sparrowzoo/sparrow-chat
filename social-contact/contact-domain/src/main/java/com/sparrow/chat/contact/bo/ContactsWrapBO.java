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

package com.sparrow.chat.contact.bo;

import com.sparrow.chat.contact.protocol.dto.FriendDetailDTO;
import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.utility.CollectionsUtility;
import lombok.Data;

import java.util.*;

@Data
public class ContactsWrapBO {

    public ContactsWrapBO(List<QunDTO> quns) {
        this.quns = quns;
    }

    private Map<Long, UserProfileDTO> userMap;
    private List<QunDTO> quns;
    private List<FriendDetailDTO> friends;

    private Set<Long> getQunOwnerIds() {
        Set<Long> ids = new HashSet<>();
        for (QunDTO qun : quns) {
            ids.add(qun.getOwnerId());
        }
        return ids;
    }

    private Set<Long> getFriendIds() {
        if (CollectionsUtility.isNullOrEmpty(this.friends)) {
            return Collections.emptySet();
        }
        Set<Long> ids = new HashSet<>();
        for (FriendDetailDTO friend : friends) {
            ids.add(friend.getFriendId());
        }
        return ids;
    }

    public Set<Long> getUserIds(Long currentUserId) {
        Set<Long> contactUserIds = new HashSet<>();
        contactUserIds.add(currentUserId);
        Collection<Long> contactIds = this.getFriendIds();
        if (!CollectionsUtility.isNullOrEmpty(contactIds)) {
            contactUserIds.addAll(contactIds);
        }
        contactUserIds.addAll(this.getQunOwnerIds());
        return contactUserIds;
    }
}
