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

package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.chat.contact.protocol.dto.FriendDetailDTO;
import com.sparrow.protocol.DTO;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ContactGroupVO implements DTO {
    public ContactGroupVO(Map<Long, ContactVO> userMap, List<QunVO> quns, List<FriendDetailDTO> friendDetails) {
        this.userMap = userMap;
        this.quns = quns;
        if (friendDetails == null) {
            this.contactIds = Collections.emptyList();
        } else {
            this.contactIds = friendDetails.stream().map(FriendDetailDTO::getFriendId).collect(Collectors.toList());
        }
        if (userMap == null) {
            this.userMap = Collections.emptyMap();
        }
    }

    private Map<Long, ContactVO> userMap;
    private List<QunVO> quns;
    //todo 这里的contactIds应该是Map  key是contactId value是 addTime
    private Collection<Long> contactIds;
}
