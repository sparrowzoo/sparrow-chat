package com.sparrow.chat.controller;

import com.sparrow.chat.protocol.audit.FriendApplyParam;
import com.sparrow.chat.protocol.audit.FriendAuditParam;
import com.sparrow.chat.protocol.audit.QunApplyParam;
import com.sparrow.chat.protocol.audit.QunAuditParam;
import com.sparrow.chat.repository.AuditRepository;
import com.sparrow.chat.service.AuditService;
import com.sparrow.protocol.BusinessException;
import javax.inject.Inject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit")
public class AuditController {
    @Inject
    private AuditService auditService;

    @PostMapping("/apply-friend")
    public Long applyFriend(@RequestBody FriendApplyParam friendApplyParam) throws BusinessException {
        return this.auditService.applyFriend(friendApplyParam);
    }

    @PostMapping("/audit-friend")
    public Boolean auditFriend(@RequestBody FriendAuditParam friendAuditParam) throws BusinessException {
        this.auditService.auditFriend(friendAuditParam);
        return true;
    }

    @PostMapping("apply-qun")
    public Boolean applyQun(@RequestBody QunApplyParam qunApplyParam) {
        return true;
    }

    @PostMapping("audit-qun")
    public Boolean auditQun(@RequestBody QunAuditParam qunAuditParam) {
        return true;
    }
}
