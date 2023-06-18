package com.sparrow.chat.contact.service;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.FriendApplyBO;
import com.sparrow.chat.contact.bo.FriendAuditWrapBO;
import com.sparrow.chat.contact.protocol.FriendApplyParam;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;
import com.sparrow.chat.contact.protocol.enums.AuditBusiness;
import com.sparrow.chat.contact.protocol.enums.ContactError;
import com.sparrow.chat.contact.repository.AuditRepository;
import com.sparrow.chat.contact.repository.ContactRepository;
import com.sparrow.exception.Asserts;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named
public class AuditService {

    @Inject
    private UserProfileAppService userProfileAppService;
    @Inject
    private AuditRepository auditRepository;

    @Inject
    private ContactRepository contactRepository;

    @Inject
    private SecretService secretService;


    public Long applyFriend(FriendApplyParam friendApplyParam) throws BusinessException {
        //获取当前登录信息
        LoginUser loginUser = ThreadContext.getLoginToken();
        //通过密文标识获取好友的id
        Long friendId = this.secretService.parseUserSecretIdentify(friendApplyParam.getFriendSecretIdentify());
        //构建好友申请的内部逻辑对象
        FriendApplyBO friendApply = new FriendApplyBO(loginUser.getUserId(), friendId, friendApplyParam.getReason());
        //对交申请
        return this.auditRepository.applyFriend(friendApply);
    }

    private Set<Long> fetchUserId(Collection<AuditBO> auditBOS) {
        Set<Long> userIds = new HashSet<>(auditBOS.size());
        for (AuditBO audit : auditBOS) {
            userIds.add(audit.getApplyUserId());
        }
        return userIds;
    }

    public FriendAuditWrapBO friendApplyList() throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        Long currentUserId = loginUser.getUserId();
        List<AuditBO> auditBOS = this.auditRepository.getFriendAuditList(currentUserId);
        Set<Long> applyFriendList = this.fetchUserId(auditBOS);
        List<UserProfileDTO> userProfiles = this.userProfileAppService.getUserList(applyFriendList);
        return new FriendAuditWrapBO(auditBOS, userProfiles);
    }

    public void auditFriendApply(FriendAuditParam friendAuditParam) throws BusinessException {
        AuditBO auditBO = this.auditRepository.getAudit(friendAuditParam.getAuditId());
        Asserts.isTrue(AuditBusiness.FRIEND != auditBO.getAuditBusiness(), ContactError.AUDIT_BUSINESS_TYPE_NOT_MATCH);
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(!auditBO.getBusinessId().equals(loginUser.getUserId()), ContactError.AUDIT_USER_IS_NOT_MATCH);
        //审核用户申请
        //todo 分布式事务 mq 消息事务
        this.auditRepository.auditFriend(auditBO, friendAuditParam);
        if (friendAuditParam.getAgree()) {
            this.contactRepository.addContact(auditBO);
            //todo 发同步消息
        }
    }
}
