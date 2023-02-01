package com.sparrow.chat.service;

import com.sparrow.chat.enums.AuditStatus;
import com.sparrow.chat.enums.BusinessType;
import com.sparrow.chat.po.Audit;
import com.sparrow.chat.protocol.QunDTO;
import com.sparrow.chat.protocol.audit.FriendApplyParam;
import com.sparrow.chat.protocol.audit.FriendAuditParam;
import com.sparrow.chat.protocol.audit.QunApplyParam;
import com.sparrow.chat.protocol.error.ChatError;
import com.sparrow.chat.repository.AuditRepository;
import com.sparrow.chat.repository.QunRepository;
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

    @Inject
    private QunRepository qunRepository;


    public Integer auditQun(FriendAuditParam friendAuditParam) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        //验证用户的登录状态
        Asserts.isTrue(loginUser == null || User.VISITOR_ID.equals(loginUser.getUserId()), SparrowError.USER_NOT_LOGIN);
        Audit audit = this.auditRepository.getAudit(friendAuditParam.getAuditId());
        //验证申请记录存在
        Asserts.isTrue(audit == null, ChatError.AUDIT_NOT_EXIST);
        //验证申请记录为群申请类型
        Asserts.isTrue(!BusinessType.QUN.equals(audit.getBusinessType()), ChatError.AUDIT_BUSINESS_TYPE_NOT_MATCH);

        QunDTO qunDto=this.qunRepository.getQun(audit.getBusinessId());
        //验证申请的群主为当前用户
        Asserts.isTrue(!qunDto.getCreateUserId().equals(loginUser.getUserId().intValue()), ChatError.AUDIT_USER_ID_NOT_MATCH_QUN_OWNER);
        //审核状态为待审核
        Asserts.isTrue(!AuditStatus.AUDITING.equals(audit.getStatus()), ChatError.AUDIT_USER_ID_NOT_MATCH_FRIEND_ID);

        return this.auditRepository.auditFriend(audit, friendAuditParam);
    }

    public Integer auditFriend(FriendAuditParam friendAuditParam) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        //验证用户的登录状态
        Asserts.isTrue(loginUser == null || User.VISITOR_ID.equals(loginUser.getUserId()), SparrowError.USER_NOT_LOGIN);
        Audit audit = this.auditRepository.getAudit(friendAuditParam.getAuditId());
        //验证申请记录存在
        Asserts.isTrue(audit == null, ChatError.AUDIT_NOT_EXIST);
        //验证申请记录为好友申请类型
        Asserts.isTrue(!BusinessType.FRIEND.equals(audit.getBusinessType()), ChatError.AUDIT_BUSINESS_TYPE_NOT_MATCH);
        //验证申请的好友为当前用户
        Asserts.isTrue(!audit.getBusinessId().equals(loginUser.getUserId()), ChatError.AUDIT_USER_ID_NOT_MATCH_FRIEND_ID);
        //审核状态为待审核
        Asserts.isTrue(!AuditStatus.AUDITING.equals(audit.getStatus()), ChatError.AUDIT_USER_ID_NOT_MATCH_FRIEND_ID);

        return this.auditRepository.auditFriend(audit, friendAuditParam);
    }

    public Long applyFriend(FriendApplyParam friendApplyParam) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(loginUser == null || User.VISITOR_ID.equals(loginUser.getUserId()), SparrowError.USER_NOT_LOGIN);
        Asserts.isTrue(friendApplyParam.getFriendId() == null, ChatError.APPLY_FRIEND_IS_NULL);

        return this.auditRepository.applyFriend(loginUser.getUserId(), friendApplyParam);
    }

    public Long applyQun(QunApplyParam qunApplyParam) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(loginUser == null || User.VISITOR_ID.equals(loginUser.getUserId()), SparrowError.USER_NOT_LOGIN);
        Asserts.isTrue(qunApplyParam.getQunId() == null, ChatError.APPLY_QUN_ID_IS_NULL);
        return this.auditRepository.applyQun(loginUser.getUserId(), qunApplyParam);
    }
}
