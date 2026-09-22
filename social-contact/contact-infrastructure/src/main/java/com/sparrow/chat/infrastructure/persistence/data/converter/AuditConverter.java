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

package com.sparrow.chat.infrastructure.persistence.data.converter;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.FriendApplyBO;
import com.sparrow.chat.contact.po.Audit;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;
import com.sparrow.chat.contact.protocol.audit.JoinQunParam;
import com.sparrow.chat.contact.protocol.audit.QunAuditParam;
import com.sparrow.chat.contact.protocol.enums.AuditBusiness;
import com.sparrow.context.SessionContext;
import com.sparrow.protocol.BeanCopier;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.protocol.enums.AuditStatus;
import com.sparrow.utility.CollectionsUtility;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named
public class AuditConverter {
    @Inject
    private BeanCopier beanCopier;

    public Audit friendApply2AuditPo(FriendApplyBO friendApply) {
        Audit audit = new Audit();
        beanCopier.copyProperties(friendApply, audit);
        audit.setApplyUserId(friendApply.getCurrentUserId());
        audit.setBusinessType(AuditBusiness.FRIEND.getBusiness());
        audit.setBusinessId(friendApply.getFriendId());
        audit.setApplyReason(friendApply.getReason());
        audit.setAuditReason(Symbol.EMPTY);
        audit.setStatus(AuditStatus.APPROVE.getIdentity());
        audit.setAuditUserId(0L);
        audit.setAuditTime(0L);
        audit.setApplyTime(System.currentTimeMillis());
        return audit;
    }

    public Audit joinQun2AuditPo(JoinQunParam joinQunParam) {
        Audit audit = new Audit();
        beanCopier.copyProperties(joinQunParam, audit);
        LoginUser loginUser = SessionContext.getLoginUser();
        audit.setApplyUserId(loginUser.getUserId());
        audit.setBusinessType(AuditBusiness.GROUP.getBusiness());
        audit.setBusinessId(joinQunParam.getQunId());
        audit.setApplyReason(joinQunParam.getReason());
        audit.setAuditReason(Symbol.EMPTY);
        audit.setStatus(AuditStatus.APPROVE.getIdentity());
        audit.setAuditUserId(0L);
        audit.setAuditTime(0L);
        audit.setApplyTime(System.currentTimeMillis());
        return audit;
    }

    public AuditBO audit2AuditBO(Audit audit) {
        AuditBO auditBO = new AuditBO();
        beanCopier.copyProperties(audit, auditBO);
        auditBO.setAuditId(audit.getId());
        auditBO.setAuditBusiness(AuditBusiness.getInstance(audit.getBusinessType()));
        return auditBO;
    }

    public List<AuditBO> auditList2AuditBOList(List<Audit> audits) {
        if (CollectionsUtility.isNullOrEmpty(audits)) {
            return Collections.emptyList();
        }
        List<AuditBO> auditBos = new ArrayList<>(audits.size());
        for (Audit audit : audits) {
            AuditBO auditBO = this.audit2AuditBO(audit);
            auditBos.add(auditBO);
        }
        return auditBos;
    }

    public Audit convert2po(AuditBO auditBO, QunAuditParam qunAuditParam) {
        LoginUser loginUser = SessionContext.getLoginUser();
        Audit audit = new Audit();
        audit.setId(auditBO.getAuditId());
        audit.setApplyUserId(auditBO.getApplyUserId());
        audit.setBusinessId(auditBO.getBusinessId());
        audit.setAuditUserId(loginUser.getUserId());
        audit.setApplyReason(auditBO.getApplyReason());
        audit.setAuditReason(qunAuditParam.getReason());
        audit.setStatus(qunAuditParam.getIsAgree() ? AuditStatus.APPROVE.getIdentity() : AuditStatus.REJECT.getIdentity());
        audit.setAuditTime(System.currentTimeMillis());
        audit.setBusinessType(AuditBusiness.GROUP.getBusiness());
        audit.setApplyTime(auditBO.getApplyTime());
        return audit;
    }

    public Audit convert2po(AuditBO auditBO, FriendAuditParam friendAuditParam) {
        LoginUser loginUser = SessionContext.getLoginUser();
        Audit audit = new Audit();
        audit.setId(auditBO.getAuditId());
        audit.setApplyUserId(auditBO.getApplyUserId());
        audit.setBusinessId(auditBO.getBusinessId());
        audit.setAuditUserId(loginUser.getUserId());
        audit.setApplyReason(auditBO.getApplyReason());
        audit.setAuditReason(friendAuditParam.getReason());
        audit.setStatus(friendAuditParam.getAgree() ? AuditStatus.APPROVE.getIdentity() : AuditStatus.REJECT.getIdentity());
        audit.setAuditTime(System.currentTimeMillis());
        audit.setBusinessType(AuditBusiness.FRIEND.getBusiness());
        audit.setApplyTime(auditBO.getApplyTime());
        return audit;
    }
}
