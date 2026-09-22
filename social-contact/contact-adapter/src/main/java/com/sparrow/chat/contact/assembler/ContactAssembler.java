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

package com.sparrow.chat.contact.assembler;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.AuditWrapBO;
import com.sparrow.chat.contact.bo.ContactsWrapBO;
import com.sparrow.chat.contact.bo.UserProfileBO;
import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.contact.protocol.vo.*;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BeanCopier;
import com.sparrow.protocol.BusinessException;
import com.sparrow.utility.CollectionsUtility;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.*;

@Named
public class ContactAssembler {

    private static final String STATUS_BUSINESS = "audit";

    @Inject
    private QunAssembler qunAssembler;

    @Inject
    private BeanCopier beanCopier;

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
            this.beanCopier.copyProperties(audit, auditVO);
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
            QunDTO qunDTO = friendAuditWrap.getQunMap().get(qunId);
            QunVO qunVO = this.qunAssembler.assembleQun(qunDTO, friendAuditWrap.getUserInfoMap());
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
        for (QunDTO qunDTO : contactsWrap.getQuns()) {
            QunVO qunVO = this.qunAssembler.assembleQun(qunDTO, contactsWrap.getUserMap());
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
