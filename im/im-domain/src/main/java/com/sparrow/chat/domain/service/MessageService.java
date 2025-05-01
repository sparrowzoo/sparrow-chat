package com.sparrow.chat.domain.service;

import com.sparrow.chat.domain.bo.SessionBO;
import com.sparrow.chat.domain.repository.SessionRepository;
import com.sparrow.chat.protocol.query.SessionQuery;
import com.sparrow.exception.Asserts;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.SparrowError;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class MessageService {
    @Inject
    private UserProfileAppService userProfileService;
    @Inject
    private SessionRepository sessionRepository;
    public List<SessionBO> querySessionList(SessionQuery sessionQuery) throws BusinessException {
        LoginUser loginUser= ThreadContext.getLoginToken();
        UserProfileDTO userProfile= userProfileService.getByLoginUser(loginUser);
        Asserts.isTrue(!userProfile.getIsManager(), SparrowError.SYSTEM_PERMISSION_DENIED);
        return this.sessionRepository.querySessions(sessionQuery);
    }


}
