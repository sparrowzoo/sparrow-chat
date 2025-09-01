package com.sparrow.chat.contact.service;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.AuditWrapBO;
import com.sparrow.chat.contact.bo.FriendApplyBO;
import com.sparrow.chat.contact.protocol.audit.FriendApplyParam;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;
import com.sparrow.chat.contact.protocol.audit.JoinQunParam;
import com.sparrow.chat.contact.protocol.audit.QunAuditParam;
import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.contact.protocol.enums.AuditBusiness;
import com.sparrow.chat.contact.protocol.enums.ContactError;
import com.sparrow.chat.contact.protocol.event.ContactEvent;
import com.sparrow.chat.contact.protocol.event.QunMemberEvent;
import com.sparrow.chat.contact.repository.AuditRepository;
import com.sparrow.chat.contact.repository.ContactRepository;
import com.sparrow.chat.contact.repository.QunRepository;
import com.sparrow.context.SessionContext;
import com.sparrow.exception.Asserts;
import com.sparrow.mq.MQPublisher;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.utility.StringUtility;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
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
    @Inject
    private QunRepository qunRepository;

    @Inject
    private MQPublisher mqPublisher;

    public Long applyFriend(FriendApplyParam friendApplyParam) throws BusinessException {
        Asserts.isTrue(StringUtility.isNullOrEmpty(friendApplyParam.getFriendSecretIdentify()), ContactError.APPLY_FRIEND_CANNOT_BE_NULL);

        Long friendId = this.secretService.parseUserSecretIdentify(friendApplyParam.getFriendSecretIdentify());

        //获取当前登录信息
        LoginUser loginUser = SessionContext.getLoginUser();
        //通过密文标识获取好友的id

        Asserts.isTrue(loginUser.getUserId().equals(friendId), ContactError.CANNOT_APPLY_SELF_FRIEND);
        //构建好友申请的内部逻辑对象
        FriendApplyBO friendApply = new FriendApplyBO(loginUser.getUserId(), friendId, friendApplyParam.getReason());
        //对交申请
        return this.auditRepository.applyFriend(friendApply);
    }

    public void applyJoinQun(JoinQunParam joinQunParam) throws BusinessException {
        Asserts.isTrue(null == joinQunParam.getQunId(), ContactError.QUN_ID_IS_EMPTY);
        QunDTO qunDto = this.qunRepository.qunDetail(joinQunParam.getQunId());
        Asserts.isTrue(qunDto == null, ContactError.QUN_NOT_FOUND);
        LoginUser loginUser = SessionContext.getLoginUser();
        Boolean isMember = this.qunRepository.isMember(joinQunParam.getQunId(), loginUser.getUserId());
        Asserts.isTrue(isMember, ContactError.USER_IS_MEMBER);
        this.auditRepository.joinQun(joinQunParam);
    }

    public AuditWrapBO friendApplyList() throws BusinessException {
        LoginUser loginUser = SessionContext.getLoginUser();
        Long currentUserId = loginUser.getUserId();
        AuditWrapBO auditWrap = this.auditRepository.getFriendList(currentUserId);

        Set<Long> userIds = auditWrap.getUserIds();
        userIds.add(currentUserId);
        Map<Long, UserProfileDTO> userProfiles = this.userProfileAppService.getUserMap(userIds);
        auditWrap.setUserInfoMap(userProfiles);
        return auditWrap;
    }


    public AuditWrapBO qunMemberApplyList() throws BusinessException {
        LoginUser loginUser = SessionContext.getLoginUser();
        Long currentUserId = loginUser.getUserId();

        AuditWrapBO auditWrap = this.auditRepository.getQunMemberList(currentUserId);
        Set<Long> userIds = auditWrap.getUserIds();
        userIds.add(currentUserId);
        Map<Long, UserProfileDTO> userProfiles = this.userProfileAppService.getUserMap(auditWrap.getUserIds());
        Map<Long, QunDTO> qunMap = this.qunRepository.getQunList(auditWrap.getQunIds());
        auditWrap.setUserInfoMap(userProfiles);
        auditWrap.setQunMap(qunMap);
        return auditWrap;
    }

    public void auditFriendApply(FriendAuditParam friendAuditParam) throws Throwable {
        AuditBO auditBO = this.auditRepository.getAudit(friendAuditParam.getAuditId());
        Asserts.isTrue(AuditBusiness.FRIEND != auditBO.getAuditBusiness(), ContactError.AUDIT_BUSINESS_TYPE_NOT_MATCH);
        LoginUser loginUser = SessionContext.getLoginUser();
        Asserts.isTrue(!auditBO.getBusinessId().equals(loginUser.getUserId()), ContactError.AUDIT_USER_IS_NOT_MATCH);
        this.auditRepository.auditFriend(auditBO, friendAuditParam);
        if (friendAuditParam.getAgree()) {
            this.contactRepository.addContact(auditBO);
            this.mqPublisher.publish(new ContactEvent(loginUser.getUserId(), auditBO.getApplyUserId(), auditBO.getApplyTime()));
        }
    }

    public void auditQunApply(QunAuditParam qunAuditParam) throws Throwable {
        AuditBO auditBO = this.auditRepository.getAudit(qunAuditParam.getAuditId());
        Asserts.isTrue(AuditBusiness.GROUP != auditBO.getAuditBusiness(), ContactError.AUDIT_BUSINESS_TYPE_NOT_MATCH);
        LoginUser loginUser = SessionContext.getLoginUser();
        QunDTO existQun = this.qunRepository.qunDetail(auditBO.getBusinessId());
        Asserts.isTrue(existQun == null, ContactError.QUN_NOT_FOUND);
        Asserts.isTrue(!existQun.getOwnerId().equals(loginUser.getUserId()), ContactError.AUDIT_USER_IS_NOT_MATCH);
        this.auditRepository.auditQun(auditBO, qunAuditParam);
        if (qunAuditParam.getIsAgree()) {
            this.qunRepository.joinQun(auditBO);
            this.mqPublisher.publish(new QunMemberEvent(existQun.getId(), auditBO.getApplyUserId()));
        }
    }
}
