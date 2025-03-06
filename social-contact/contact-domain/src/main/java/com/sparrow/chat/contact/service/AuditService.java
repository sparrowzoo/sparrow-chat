package com.sparrow.chat.contact.service;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.AuditWrapBO;
import com.sparrow.chat.contact.bo.FriendApplyBO;
import com.sparrow.chat.contact.bo.QunBO;
import com.sparrow.chat.contact.protocol.audit.FriendApplyParam;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;
import com.sparrow.chat.contact.protocol.audit.JoinQunParam;
import com.sparrow.chat.contact.protocol.audit.QunAuditParam;
import com.sparrow.chat.contact.protocol.enums.AuditBusiness;
import com.sparrow.chat.contact.protocol.enums.ContactError;
import com.sparrow.chat.contact.protocol.event.QunMemberEvent;
import com.sparrow.chat.contact.repository.AuditRepository;
import com.sparrow.chat.contact.repository.ContactRepository;
import com.sparrow.chat.contact.repository.QunRepository;
import com.sparrow.exception.Asserts;
import com.sparrow.mq.MQPublisher;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.utility.StringUtility;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named
public class AuditService {
    private UserProfileAppService userProfileAppService;
    @Inject
    private AuditRepository auditRepository;
    @Inject
    private ContactRepository contactRepository;
    @Inject
    private SecretService secretService;
    @Inject
    private QunRepository qunRepository;

    @Inject
    private MQPublisher mqPublisher;

    public Long applyFriend(FriendApplyParam friendApplyParam) throws BusinessException {
        Asserts.isTrue(StringUtility.isNullOrEmpty(friendApplyParam.getFriendSecretIdentify()), ContactError.APPLY_FRIEND_CANNOT_BE_NULL);

        Long friendId = this.secretService.parseUserSecretIdentify(friendApplyParam.getFriendSecretIdentify());

        //获取当前登录信息
        LoginUser loginUser = ThreadContext.getLoginToken();
        //通过密文标识获取好友的id

        Asserts.isTrue(loginUser.getUserId().equals(friendId), ContactError.CANNOT_APPLY_SELF_FRIEND);
        //构建好友申请的内部逻辑对象
        FriendApplyBO friendApply = new FriendApplyBO(loginUser.getUserId(), friendId, friendApplyParam.getReason());
        //对交申请
        return this.auditRepository.applyFriend(friendApply);
    }

    public void applyJoinQun(JoinQunParam joinQunParam) throws BusinessException {
        Asserts.isTrue(null == joinQunParam.getQunId(), ContactError.QUN_ID_IS_EMPTY);
        QunBO qunBO = this.qunRepository.qunDetail(joinQunParam.getQunId());
        Asserts.isTrue(qunBO == null, ContactError.QUN_NOT_FOUND);
        LoginUser loginUser = ThreadContext.getLoginToken();
        Boolean isMember = this.qunRepository.isMember(joinQunParam.getQunId(), loginUser.getUserId());
        Asserts.isTrue(isMember, ContactError.USER_IS_MEMBER);
        this.auditRepository.joinQun(joinQunParam);
    }

    private Set<Long> fetchUserId(Collection<AuditBO> auditBOS) {
        Set<Long> userIds = new HashSet<>(auditBOS.size());
        for (AuditBO audit : auditBOS) {
            userIds.add(audit.getApplyUserId());
        }
        return userIds;
    }

    public AuditWrapBO friendApplyList() throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        Long currentUserId = loginUser.getUserId();
        List<AuditBO> auditBOS = this.auditRepository.getAuditingFriendList(currentUserId);
        Set<Long> applyFriendList = this.fetchUserId(auditBOS);
        Map<Long, UserProfileDTO> userProfiles = this.userProfileAppService.getUserMap(applyFriendList);
        return new AuditWrapBO(auditBOS, userProfiles);
    }


    public AuditWrapBO qunMemberApplyList(Long qunId) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        Long currentUserId = loginUser.getUserId();
        QunBO qunBO = this.qunRepository.qunDetail(qunId);
        Asserts.isTrue(!qunBO.getOwnerId().equals(currentUserId), ContactError.QUN_OWNER_IS_NOT_MATCH);
        List<AuditBO> auditBOS = this.auditRepository.getAuditingQunMemberList(qunId);
        Set<Long> applyFriendList = this.fetchUserId(auditBOS);
        Map<Long, UserProfileDTO> userProfiles = this.userProfileAppService.getUserMap(applyFriendList);
        return new AuditWrapBO(auditBOS, userProfiles);
    }

    public void auditFriendApply(FriendAuditParam friendAuditParam) throws BusinessException {
        AuditBO auditBO = this.auditRepository.getAudit(friendAuditParam.getAuditId());
        Asserts.isTrue(AuditBusiness.FRIEND != auditBO.getAuditBusiness(), ContactError.AUDIT_BUSINESS_TYPE_NOT_MATCH);
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(!auditBO.getBusinessId().equals(loginUser.getUserId()), ContactError.AUDIT_USER_IS_NOT_MATCH);
        this.auditRepository.auditFriend(auditBO, friendAuditParam);
        if (friendAuditParam.getAgree()) {
            this.contactRepository.addContact(auditBO);
        }
    }

    public void auditQunApply(QunAuditParam qunAuditParam) throws Throwable {
        AuditBO auditBO = this.auditRepository.getAudit(qunAuditParam.getAuditId());
        Asserts.isTrue(AuditBusiness.GROUP != auditBO.getAuditBusiness(), ContactError.AUDIT_BUSINESS_TYPE_NOT_MATCH);
        LoginUser loginUser = ThreadContext.getLoginToken();
        QunBO existQun = this.qunRepository.qunDetail(auditBO.getBusinessId());
        Asserts.isTrue(existQun == null, ContactError.QUN_NOT_FOUND);
        Asserts.isTrue(!existQun.getOwnerId().equals(loginUser.getUserId()), ContactError.AUDIT_USER_IS_NOT_MATCH);
        this.auditRepository.auditQun(auditBO, qunAuditParam);
        if (qunAuditParam.getAgree()) {
            this.qunRepository.joinQun(auditBO);
            this.mqPublisher.publish(new QunMemberEvent(existQun.getId(), auditBO.getApplyUserId()));
        }
    }
}
