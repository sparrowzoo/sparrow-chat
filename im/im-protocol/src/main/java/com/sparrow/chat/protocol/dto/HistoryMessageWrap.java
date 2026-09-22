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

package com.sparrow.chat.protocol.dto;

import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.protocol.constant.Chat;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class HistoryMessageWrap {
    private List<MessageDTO> historyMessages;
    private Map<String, QunDTO> qunMaps;
    private Map<Long, UserProfileDTO> userMaps;

    public Set<String> qunIds() {
        Set<String> keys = new HashSet<>();
        for (MessageDTO messageDTO : this.historyMessages) {
            if (messageDTO.getChatType() == Chat.CHAT_TYPE_1_2_N) {
                keys.add(messageDTO.getSessionKey());
            }
        }
        return keys;
    }

    public Set<Long> userIds() {
        Set<Long> ids = new HashSet<>();
        for (MessageDTO messageDTO : this.historyMessages) {
            ids.add(Long.parseLong(messageDTO.getSender().getId()));
            if (messageDTO.getReceiver() != null) {
                ids.add(Long.parseLong(messageDTO.getReceiver().getId()));
            }
        }
        return ids;
    }

}
