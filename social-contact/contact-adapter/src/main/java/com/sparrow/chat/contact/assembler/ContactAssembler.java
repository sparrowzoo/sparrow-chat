package com.sparrow.chat.contact.assembler;

import com.sparrow.chat.contact.bo.*;
import com.sparrow.chat.contact.protocol.vo.*;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.utility.BeanUtility;
import com.sparrow.utility.CollectionsUtility;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named
public class ContactAssembler {
    private static final String STATUS_BUSINESS = "audit";

    @Inject
    private QunAssembler qunAssembler;

    @Inject
    private UserAssembler userAssembler;


    public UserFriendApplyVO toUserFriendApplyVO(UserProfileBO contactBO) {
        UserFriendApplyVO userFriendApply = new UserFriendApplyVO();
        userFriendApply.setUserSecretIdentify(contactBO.getSecretIdentify());
        userFriendApply.setNickName(contactBO.getUserDto().getNickName());
        userFriendApply.setAvatar(contactBO.getUserDto().getAvatar());
        return userFriendApply;
    }

    private List<AuditVO> toAuditVoList(List<AuditBO> audits) {
        if (CollectionsUtility.isNullOrEmpty(audits)) {
            return Collections.emptyList();
        }
        List<AuditVO> auditVos = new ArrayList<>();
        for (AuditBO audit : audits) {
            AuditVO auditVO = new AuditVO();
            BeanUtility.copyProperties(audit, auditVO);
            auditVO.setAuditBusiness(audit.getAuditBusiness().getBusiness());
            auditVO.setStatus(audit.getStatus().ordinal());
            auditVos.add(auditVO);
        }
        return auditVos;
    }

    public AuditWrapVO toAuditVoList(AuditWrapBO friendAuditWrap) throws BusinessException {
        List<AuditVO> auditingList = this.toAuditVoList(friendAuditWrap.getAuditingList());
        List<AuditVO> applyingList = this.toAuditVoList(friendAuditWrap.getMyApplingList());
        AuditWrapVO auditVo = new AuditWrapVO();
        auditVo.setAuditingList(auditingList);
        auditVo.setMyApplyingList(applyingList);
        Map<Long, ContactVO> contactMap = new HashMap<>();
        for (Long userId : friendAuditWrap.getUserInfoMap().keySet()) {
            contactMap.put(userId, userAssembler.userDto2ContactVo(friendAuditWrap.getUserInfoMap().get(userId)));
        }
        auditVo.setContactMap(contactMap);
        if (friendAuditWrap.getQunMap() == null) {
            return auditVo;
        }
        Map<Long, QunVO> qunVOMap = new HashMap<>();
        for (Long qunId : friendAuditWrap.getQunMap().keySet()) {
            QunBO qunBO = friendAuditWrap.getQunMap().get(qunId);
            ContactVO contact = auditVo.getContactMap().get(qunBO.getOwnerId());
            QunVO qunVO = this.qunAssembler.assembleQun(qunBO, contact);
            qunVOMap.put(qunId, qunVO);
        }
        auditVo.setQunMap(qunVOMap);
        return auditVo;
    }

    private List<QunVO> assembleMyQun(ContactsWrapBO contactsWrap) throws BusinessException {
        if (CollectionsUtility.isNullOrEmpty(contactsWrap.getQuns())) {
            return Collections.emptyList();
        }
        List<QunVO> qunVOS = new ArrayList<>(contactsWrap.getQuns().size());
        for (QunBO qunBO : contactsWrap.getQuns()) {
            UserProfileDTO qunOwner = contactsWrap.getUserMap().get(qunBO.getOwnerId());
            ContactVO qunOwnerVO = this.userAssembler.userDto2ContactVo(qunOwner);
            QunVO qunVO = this.qunAssembler.assembleQun(qunBO, qunOwnerVO);
            qunVOS.add(qunVO);
        }
        return qunVOS;
    }

    private Map<Long, ContactVO> assembleUserMap(ContactsWrapBO contactsWrap) {
        if (contactsWrap.getUserMap() == null) {
            return Collections.emptyMap();
        }
        Map<Long, ContactVO> userMap = new HashMap<>(contactsWrap.getUserMap().size());
        for (Long userId : contactsWrap.getUserMap().keySet()) {
            UserProfileDTO userProfileDTO = contactsWrap.getUserMap().get(userId);
            userMap.put(userId, userAssembler.userDto2ContactVo(userProfileDTO));
        }
        return userMap;
    }


    public ContactGroupVO assembleVO(ContactsWrapBO contactsWrap) throws BusinessException {
        List<QunVO> qunVOS = this.assembleMyQun(contactsWrap);
        Map<Long, ContactVO> userMap = this.assembleUserMap(contactsWrap);
        return new ContactGroupVO(userMap, qunVOS, contactsWrap.getFriends());
    }


    public List<ContactVO> assembleUserListVO(Collection<UserProfileDTO> profileDTOS) {
        if (CollectionsUtility.isNullOrEmpty(profileDTOS)) {
            return Collections.emptyList();
        }
        List<ContactVO> userVOS = new ArrayList<>(profileDTOS.size());
        for (UserProfileDTO userProfile : profileDTOS) {
            userVOS.add(userAssembler.userDto2ContactVo(userProfile));
        }
        return userVOS;
    }
}
