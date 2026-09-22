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

package com.sparrow.chat.domain.repository;

import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.bo.Protocol;
import com.sparrow.chat.protocol.dto.HistoryMessageWrap;
import com.sparrow.chat.protocol.dto.MessageDTO;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.query.MessageCancelQuery;
import com.sparrow.chat.protocol.query.MessageQuery;
import com.sparrow.protocol.BusinessException;

import java.util.List;

public interface MessageRepository {
    void cancel(MessageCancelQuery messageCancel, ChatUser sender) throws BusinessException;

    void saveMessage(Protocol message,Long ip);

    List<MessageDTO> getMessageBySession(String session);

    List<MessageDTO> getHistoryMessage(MessageQuery query);

    HistoryMessageWrap queryHistoryMessage(MessageQuery query) throws BusinessException;

    /**
     * 填充最后一条消息和未读消息数
     * @param sessionMap
     * @return
     */
    void fillSession(List<SessionDTO> sessionMap);
}
