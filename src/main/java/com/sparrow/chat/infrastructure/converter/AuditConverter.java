package com.sparrow.chat.infrastructure.converter;

import com.sparrow.chat.enums.AuditStatus;
import com.sparrow.chat.enums.BusinessType;
import com.sparrow.chat.po.Audit;
import com.sparrow.chat.po.Qun;
import com.sparrow.chat.protocol.audit.FriendApplyParam;
import com.sparrow.chat.protocol.audit.QunApplyParam;
import javax.inject.Named;

@Named
public class AuditConverter {
    
    public Audit applyFriend2Po(Long currentUserId, FriendApplyParam friendApplyParam) {
        Audit audit = new Audit();
        audit.setUserId(currentUserId);
        audit.setBusinessType(BusinessType.FRIEND);
        audit.setBusinessId(friendApplyParam.getFriendId());
        audit.setAuditUserId(friendApplyParam.getFriendId());
        audit.setApplyReason(friendApplyParam.getReason());
        audit.setCreateTime(System.currentTimeMillis());
        audit.setStatus(AuditStatus.AUDITING);
        audit.setAuditReason("");
        audit.setAuditTime(0L);
        return audit;
    }

    public Audit applyQun2Po(Long currentUserId, QunApplyParam qunApplyParam) {
        Audit audit = new Audit();
        audit.setUserId(currentUserId);
        audit.setBusinessType(BusinessType.FRIEND);
        audit.setBusinessId(qunApplyParam.getQunId());
        audit.setAuditUserId(0L);
        audit.setApplyReason(qunApplyParam.getReason());
        audit.setCreateTime(System.currentTimeMillis());
        audit.setStatus(AuditStatus.AUDITING);
        audit.setAuditReason("");
        audit.setAuditTime(0L);
        return audit;
    }
}
