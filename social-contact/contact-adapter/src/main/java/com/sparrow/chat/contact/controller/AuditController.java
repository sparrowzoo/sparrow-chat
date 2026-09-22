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

package com.sparrow.chat.contact.controller;

import com.sparrow.authenticator.enums.AuthenticatorError;
import com.sparrow.chat.contact.assembler.ContactAssembler;
import com.sparrow.chat.contact.bo.AuditWrapBO;
import com.sparrow.chat.contact.protocol.audit.FriendApplyParam;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;
import com.sparrow.chat.contact.protocol.audit.JoinQunParam;
import com.sparrow.chat.contact.protocol.audit.QunAuditParam;
import com.sparrow.chat.contact.protocol.vo.AuditWrapVO;
import com.sparrow.chat.contact.service.AuditService;
import com.sparrow.context.SessionContext;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audit")
@Tag(name = "审核")
public class AuditController {
    @Inject
    private ContactAssembler contactAssembler;

    @Inject
    private AuditService auditService;


    @Operation(method = "申请列表")
    @GetMapping("friend-apply-list.json")
    public AuditWrapVO friendApplyList() throws BusinessException {
        AuditWrapBO friendAuditBO = this.auditService.friendApplyList();
        return this.contactAssembler.toAuditVoList(friendAuditBO);
    }

    @GetMapping("qun-member-apply-list.json")
    public AuditWrapVO qunMemberApplyList() throws BusinessException {
        AuditWrapBO friendAuditBO = this.auditService.qunMemberApplyList();
        return this.contactAssembler.toAuditVoList(friendAuditBO);
    }

    @PostMapping("apply-friend.json")
    public Long applyFriend(@RequestBody FriendApplyParam friendApplyParam) throws BusinessException {
        return this.auditService.applyFriend(friendApplyParam);
    }

    @PostMapping("/audit-friend-apply.json")
    public void auditFriendApply(@RequestBody FriendAuditParam friendAuditParam) throws Throwable {
        this.auditService.auditFriendApply(friendAuditParam);
    }

    @PostMapping("audit-qun-apply.json")
    public void auditQunApply(@RequestBody QunAuditParam qunAuditParam) throws Throwable {
        this.auditService.auditQunApply(qunAuditParam);
    }

    /**
     * 1. 从controller 获取loginUser 并放入joinQunParam
     * 2. 在业务里直接使用loginUser 参数不透传
     * 3. 从service 逐层传递
     *
     * @param joinQunParam
     * @throws BusinessException
     */
    @PostMapping("join-qun.json")
    public void applyJoinQun(@RequestBody JoinQunParam joinQunParam) throws BusinessException {
        LoginUser loginUser = SessionContext.getLoginUser();
        Asserts.isTrue(loginUser.isVisitor(), AuthenticatorError.USER_NOT_LOGIN);
        this.auditService.applyJoinQun(joinQunParam);
    }
}
