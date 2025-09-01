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

import javax.inject.Inject;
import javax.inject.Named;
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
