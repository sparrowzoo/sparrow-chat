package com.sparrow.chat.domain.service;

import com.sparrow.chat.protocol.dto.SessionMetaDTO;
import com.sparrow.chat.domain.repository.SessionMateRepository;
import com.sparrow.chat.domain.repository.SessionRepository;
import com.sparrow.chat.protocol.query.SessionQuery;
import com.sparrow.concurrent.SparrowThreadFactory;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.support.AuthenticatorConfigReader;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
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
        LoginUser loginUser = ThreadContext.getLoginToken();
        UserProfileDTO userProfile = userProfileService.getByLoginUser(loginUser);
        AuthenticatorConfigReader authenticatorConfigReader = ApplicationContext.getContainer().getBean(AuthenticatorConfigReader.class);
        int platformId = authenticatorConfigReader.getPlatform();
        boolean isAdmin =loginUser.getCategory().equals(platformId);
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
