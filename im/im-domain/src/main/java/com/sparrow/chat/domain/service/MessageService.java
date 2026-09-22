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

package com.sparrow.chat.domain.service;

import com.sparrow.authenticator.AuthenticatorConfigReader;
import com.sparrow.chat.domain.repository.SessionMateRepository;
import com.sparrow.chat.domain.repository.SessionRepository;
import com.sparrow.chat.protocol.dto.SessionMetaDTO;
import com.sparrow.chat.protocol.query.SessionQuery;
import com.sparrow.concurrent.SparrowThreadFactory;
import com.sparrow.context.SessionContext;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Named
@Slf4j
public class MessageService {
    private ScheduledExecutorService sessionSyncExecutor = new ScheduledThreadPoolExecutor(1,
            new SparrowThreadFactory.Builder().namingPattern("session-mata-sync-%d").daemon(true).build());
    @Inject
    private UserProfileAppService userProfileService;
    @Inject
    private SessionRepository sessionRepository;

    @Inject
    private SessionMateRepository sessionMateRepository;

    public List<SessionMetaDTO> querySessionList(SessionQuery sessionQuery) throws BusinessException {
        LoginUser loginUser = SessionContext.getLoginUser();
        UserProfileDTO userProfile = userProfileService.getByLoginUser(loginUser);
        AuthenticatorConfigReader authenticatorConfigReader = ApplicationContext.getContainer().getBean(AuthenticatorConfigReader.class);
        int platformId = authenticatorConfigReader.getPlatformManagerCategory();
        boolean isAdmin = loginUser.getCategory().equals(platformId);
        if (!isAdmin) {
            sessionQuery.setUserId(userProfile.getUserId());
        }
        return this.sessionMateRepository.querySessions(sessionQuery);
    }

    public void startSyncSessionMeta() {
        this.sessionSyncExecutor.scheduleAtFixedRate(() -> {
            try {
                MessageService.this.sessionMateRepository.syncSessions();
            } catch (Exception e) {
                log.error("sync session meta error", e);
            }
        }, 0, 10, java.util.concurrent.TimeUnit.SECONDS);
    }
}
