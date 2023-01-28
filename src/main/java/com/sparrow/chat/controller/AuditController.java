package com.sparrow.chat.controller;

import com.sparrow.chat.protocol.audit.FriendApplyParam;
import com.sparrow.chat.protocol.audit.FriendAuditParam;
import com.sparrow.chat.protocol.audit.QunApplyParam;
import com.sparrow.chat.protocol.audit.QunAuditParam;
import com.sparrow.chat.service.AuditService;
import com.sparrow.protocol.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit")
public class AuditController {
    @Autowired
    private AuditService auditService;

    @PostMapping("apply-friend")
    public Long applyFriend(FriendApplyParam friendApplyParam) throws BusinessException {
        return this.auditService.applyFriend(friendApplyParam);
    }

    @PostMapping("audit-friend")
    public Boolean auditFriend(FriendAuditParam friendAuditParam) {
        return true;
    }

    @PostMapping("apply-qun")
    public Boolean applyQun(QunApplyParam qunApplyParam) {
        return true;
    }

    @PostMapping("audit-qun")
    public Boolean auditQun(QunAuditParam qunAuditParam) {
        return true;
    }
}
