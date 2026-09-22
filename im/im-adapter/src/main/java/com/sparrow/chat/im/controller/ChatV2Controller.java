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

package com.sparrow.chat.im.controller;

import com.sparrow.authenticator.Authenticator;
import com.sparrow.authenticator.HostAuthenticationToken;
import com.sparrow.authenticator.token.BearerToken;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.netty.UserContainer;
import com.sparrow.chat.domain.service.ChatService;
import com.sparrow.chat.domain.service.MessageService;
import com.sparrow.chat.domain.service.UserLoginService;
import com.sparrow.chat.protocol.dto.HistoryMessageWrap;
import com.sparrow.chat.protocol.dto.MessageDTO;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.dto.SessionMetaDTO;
import com.sparrow.chat.protocol.params.SessionReadParams;
import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.chat.protocol.query.MessageCancelQuery;
import com.sparrow.chat.protocol.query.MessageQuery;
import com.sparrow.chat.protocol.query.SessionQuery;
import com.sparrow.context.SessionContext;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat/v2")
@Slf4j
public class ChatV2Controller {
    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private Authenticator authenticator;

    @Autowired
    private UserLoginService loginService;

    @PostMapping("/parse-token.json")
    public LoginUser parseToken(String token) throws BusinessException {
        ClientInformation client = SessionContext.getClientInfo();
        HostAuthenticationToken hostAuthenticationToken = new BearerToken(token, client.getDeviceId());
        return this.authenticator.authenticate(hostAuthenticationToken);
    }

    @GetMapping("/get-current-user.json")
    public LoginUser getCurrentUser() {
        return SessionContext.getLoginUser();
    }


    @PostMapping("/login.json")
    public String login(@RequestBody ChatUserQuery userQuery) throws BusinessException {
        return this.loginService.login(Long.parseLong(userQuery.getId()));
    }

    @PostMapping("/long-login.json")
    public String login2(@RequestBody Long userId) throws BusinessException {
        return this.loginService.login(userId);
    }

    @PostMapping("/is-online.json")
    public Boolean online(ChatUserQuery chatUser) {
        return UserContainer.getContainer().online(ChatUser.convertFromQuery(chatUser));
    }


    @PostMapping("/session/read.json")
    public Boolean readSession(@RequestBody SessionReadParams sessionReadParams) throws BusinessException {
        chatService.read(sessionReadParams);
        return true;
    }

    @GetMapping("/sessions.json")
    public List<SessionDTO> getSessions() throws BusinessException {
        return chatService.fetchSessions();
    }

    @PostMapping("/messages.json")
    public List<MessageDTO> getMessages(@RequestBody String sessionKey) throws BusinessException {
        return chatService.fetchMessages(sessionKey);
    }

    @PostMapping("/query-history-messages.json")
    public HistoryMessageWrap queryHistoryMessages(@RequestBody MessageQuery messageQuery) throws BusinessException {
        return chatService.queryHistoryMessages(messageQuery);
    }

    @PostMapping("/session-list.json")
    public List<SessionMetaDTO> getSessionList(@RequestBody SessionQuery sessionQuery) throws BusinessException {
        return this.messageService.querySessionList(sessionQuery);
    }

    @PostMapping("/cancel.json")
    public Boolean cancel(@RequestBody MessageCancelQuery messageCancel) {
        try {
            chatService.cancel(messageCancel);
        } catch (Exception e) {
            log.error("cancel error", e);
            return false;
        }
        return true;
    }
}
