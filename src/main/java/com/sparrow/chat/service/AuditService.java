package com.sparrow.chat.service;

import com.sparrow.chat.protocol.audit.FriendApplyParam;
import com.sparrow.chat.repository.AuditRepository;
import com.sparrow.constant.User;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.SparrowError;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AuditService {
    @Inject
    private AuditRepository auditRepository;

    public Long applyFriend(FriendApplyParam friendApplyParam) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(loginUser == null || User.VISITOR_ID.equals(loginUser.getUserId()), SparrowError.USER_NOT_LOGIN);
        Asserts.isTrue(friendApplyParam.getFriendId() == null, SparrowError.GLOBAL_PARAMETER_NULL);
        return this.auditRepository.applyFriend(loginUser.getUserId(), friendApplyParam);
    }
}
