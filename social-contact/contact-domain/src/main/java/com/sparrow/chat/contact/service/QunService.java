package com.sparrow.chat.contact.service;

import com.sparrow.chat.contact.bo.*;
import com.sparrow.chat.contact.protocol.enums.Category;
import com.sparrow.chat.contact.protocol.enums.ContactError;
import com.sparrow.chat.contact.protocol.event.QunMemberEvent;
import com.sparrow.chat.contact.protocol.qun.*;
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
public class QunService {
    @Inject
    private QunRepository qunRepository;

    @Inject
    private UserProfileAppService userProfileAppService;

    @Inject
    private InviteFriendSecurity inviteFriendSecurity;

    @Inject
    private MQPublisher mqPublisher;

    public Long createQun(QunCreateParam qunCreateParam) throws BusinessException {
        Asserts.isTrue(StringUtility.isNullOrEmpty(qunCreateParam.getName()), ContactError.QUN_NAME_IS_EMPTY);
        Asserts.isTrue(null == qunCreateParam.getCategoryId(), ContactError.CATEGORY_OF_QUN_EMPTY);
        Asserts.isTrue(null == qunCreateParam.getNationalityId(), ContactError.NATIONALITY_OF_QUN_EMPTY);
        return this.qunRepository.createQun(qunCreateParam);
    }

    public void modify(QunModifyParam qunModifyParam) throws BusinessException {
        Asserts.isTrue(null == qunModifyParam.getQunId(), ContactError.QUN_ID_IS_EMPTY);
        Asserts.isTrue(StringUtility.isNullOrEmpty(qunModifyParam.getName()), ContactError.QUN_NAME_IS_EMPTY);
        this.qunRepository.modifyQun(qunModifyParam);
    }

    public QunDetailWrapBO qunDetail(Long qunId) throws BusinessException {
        QunBO qunBo = this.qunRepository.qunDetail(qunId);
        UserProfileDTO owner = this.userProfileAppService.getUser(qunBo.getOwnerId());
        return new QunDetailWrapBO(qunBo, owner);
    }


    private QunPlazaBO wrapQunPlaza(List<QunBO> qunBOS) throws BusinessException {
        QunPlazaBO qunPlaza = new QunPlazaBO();
        Set<Long> userIds = new HashSet<>();
        //Set<Long> categories = new HashSet<>();
        for (QunBO qun : qunBOS) {
            userIds.add(qun.getOwnerId());
            //categories.add(qun.getCategoryId());
        }
        Map<Long, UserProfileDTO> userProfileMap = this.userProfileAppService.getUserMap(userIds);
        qunPlaza.setUserDicts(userProfileMap);
        qunPlaza.setCategoryDicts(Category.getMap());
        qunPlaza.setQunList(qunBOS);
        return qunPlaza;
    }

    public QunPlazaBO qunPlaza(Long categoryId) throws BusinessException {
        List<QunBO> qunBOs = this.qunRepository.queryQunPlaza(categoryId);
        return this.wrapQunPlaza(qunBOs);
    }

    public QunPlazaBO qunPlaza() throws BusinessException {
        List<QunBO> qunBOs = this.qunRepository.queryQunPlaza();
        return this.wrapQunPlaza(qunBOs);
    }

    public void existQun(Long qunId) throws Throwable {
        QunBO existQun = this.qunRepository.qunDetail(qunId);
        Asserts.isTrue(existQun == null, ContactError.QUN_NOT_FOUND);
        LoginUser loginUser = ThreadContext.getLoginToken();
        Boolean isMember = this.qunRepository.isMember(qunId, loginUser.getUserId());
        Asserts.isTrue(!isMember, ContactError.QUN_ID_IS_EMPTY);
        this.qunRepository.removeMember(new RemoveMemberOfQunParam(qunId, loginUser.getUserId()));
        mqPublisher.publish(new QunMemberEvent(existQun.getId(), loginUser.getUserId()));
    }

    public void removeMember(RemoveMemberOfQunParam removeMemberOfQunParam) throws Throwable {
        QunBO existQun = this.qunRepository.qunDetail(removeMemberOfQunParam.getQunId());
        Asserts.isTrue(existQun == null, ContactError.QUN_NOT_FOUND);
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(!existQun.getOwnerId().equals(loginUser.getUserId()), ContactError.QUN_OWNER_IS_NOT_MATCH);
        this.qunRepository.removeMember(removeMemberOfQunParam);
        mqPublisher.publish(new QunMemberEvent(existQun.getId(), removeMemberOfQunParam.getMemberId()));
    }

    public void dissolve(Long qunId) throws BusinessException {
        QunBO existQun = this.qunRepository.qunDetail(qunId);
        Asserts.isTrue(existQun == null, ContactError.QUN_NOT_FOUND);
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(!existQun.getOwnerId().equals(loginUser.getUserId()), ContactError.QUN_OWNER_IS_NOT_MATCH);
        this.qunRepository.dissolve(qunId);
        //todo 推消息 走mq
    }

    public void transfer(TransferOwnerOfQunParam transferOwnerOfQun) throws BusinessException {
        QunBO existQun = this.qunRepository.qunDetail(transferOwnerOfQun.getQunId());
        Asserts.isTrue(existQun == null, ContactError.QUN_NOT_FOUND);
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(!existQun.getOwnerId().equals(loginUser.getUserId()), ContactError.QUN_OWNER_IS_NOT_MATCH);
        UserProfileDTO newOwner = this.userProfileAppService.getUser(transferOwnerOfQun.getNewOwnerId());
        Asserts.isTrue(newOwner == null, ContactError.USER_IDENTIFY_INFO_EMPTY);
        Boolean isMember = this.qunRepository.isMember(transferOwnerOfQun.getQunId(), transferOwnerOfQun.getNewOwnerId());
        Asserts.isTrue(!isMember, ContactError.USER_IS_NOT_MEMBER);
        this.qunRepository.transfer(existQun, transferOwnerOfQun.getNewOwnerId());
        //todo 推消息 mq
    }

    public String inviteFriend(InviteFriendParam inviteFriendParam) throws BusinessException {
        QunBO existQun = this.qunRepository.qunDetail(inviteFriendParam.getQunId());
        Asserts.isTrue(existQun == null, ContactError.QUN_NOT_FOUND);
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(!existQun.getOwnerId().equals(loginUser.getUserId()), ContactError.QUN_OWNER_IS_NOT_MATCH);
        UserProfileDTO newMember = this.userProfileAppService.getUser(inviteFriendParam.getFriendId());
        Asserts.isTrue(newMember == null, ContactError.USER_IDENTIFY_INFO_EMPTY);
        Boolean isMember = this.qunRepository.isMember(inviteFriendParam.getQunId(), inviteFriendParam.getFriendId());
        Asserts.isTrue(isMember, ContactError.USER_IS_MEMBER);
        return this.inviteFriendSecurity.encryptInviteFriend(inviteFriendParam);
    }

    public List<QunMemberBO> getMemberIdsById(Long qunId) throws BusinessException {
        return this.qunRepository.qunMembers(qunId);
    }

    public QunMemberWrapBO getMembersById(Long qunId) throws BusinessException {
        List<QunMemberBO> qunMemberBOs = this.qunRepository.qunMembers(qunId);
        Set<Long> userIds = new HashSet<>();
        //Set<Long> categories = new HashSet<>();
        for (QunMemberBO qun : qunMemberBOs) {
            userIds.add(qun.getMemberId());
            //categories.add(qun.getCategoryId());
        }
        Map<Long, UserProfileDTO> userProfileMap = this.userProfileAppService.getUserMap(userIds);
        return new QunMemberWrapBO(qunMemberBOs, userProfileMap);
    }
}
