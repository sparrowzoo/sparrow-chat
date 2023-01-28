package com.sparrow.chat.repository.impl;

import com.sparrow.chat.dao.AuditDAO;
import com.sparrow.chat.enums.AuditStatus;
import com.sparrow.chat.enums.BusinessType;
import com.sparrow.chat.po.Audit;
import com.sparrow.chat.protocol.audit.FriendApplyParam;
import com.sparrow.chat.repository.AuditRepository;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AuditRepositoryImpl implements AuditRepository {
    @Inject
    private AuditDAO auditDao;

    @Override public Long applyFriend(Long currentUserId, FriendApplyParam friendApplyParam) {
        Audit audit = new Audit();
        audit.setUserId(currentUserId);
        audit.setBusinessType(BusinessType.FRIEND);
        audit.setBusinessId(friendApplyParam.getFriendId());
        audit.setApplyReason(friendApplyParam.getReason());
        audit.setCreateTime(System.currentTimeMillis());
        audit.setStatus(AuditStatus.AUDITING);
        return this.auditDao.insert(audit);
    }
}
