package com.sparrow.chat.contact.assembler;

import com.sparrow.chat.contact.bo.*;
import com.sparrow.chat.contact.protocol.enums.Nationality;
import com.sparrow.chat.contact.protocol.vo.*;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.BeanUtility;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.EnumUtility;

import javax.inject.Named;
import java.util.*;

@Named
public class ContactAssembler {
    private static final String STATUS_BUSINESS = "audit";

    public UserFriendApplyVO toUserFriendApplyVO(UserProfileBO contactBO) {
        UserFriendApplyVO userFriendApply = new UserFriendApplyVO();
        userFriendApply.setUserSecretIdentify(contactBO.getSecretIdentify());
        userFriendApply.setNickName(contactBO.getUserDto().getNickName());
        userFriendApply.setAvatar(contactBO.getUserDto().getAvatar());
        return userFriendApply;
    }

    public FriendAuditWrapVO toUserFriendApplyVoList(AuditWrapBO friendAuditWrap) {
        List<AuditBO> auditBOS = friendAuditWrap.getAuditList();
        List<FriendAuditVO> userFriendApplyList = new ArrayList<>();
        Map<Long, UserProfileDTO> userDictionaries = friendAuditWrap.getFriendMap();
        for (AuditBO audit : auditBOS) {
            FriendAuditVO friendAuditVO = new FriendAuditVO();
            friendAuditVO.setAuditId(audit.getAuditId());
            friendAuditVO.setAuditStatus(audit.getStatus().ordinal());
            UserProfileDTO applyUser = userDictionaries.get(audit.getApplyUserId());
            if (applyUser != null) {
                friendAuditVO.setAvatar(applyUser.getAvatar());
                friendAuditVO.setNickName(applyUser.getNickName());
                userFriendApplyList.add(friendAuditVO);
            }
        }
//        Map<Integer, String> auditStatusDict = new HashMap<>();
//        auditStatusDict.put(StatusRecord.ENABLE.ordinal(), "通过");
//        auditStatusDict.put(StatusRecord.DISABLE.ordinal(), "拒绝");

        /**
         * 1. 枚举变的时侯，这部分的逻辑不需要修改
         * 2. 枚举国际化自动支持
         * 3. 支持任务枚举的map 字典
         */
        Map<String, String> auditStatusDict = EnumUtility.getOrdinalValueMap(StatusRecord.class, STATUS_BUSINESS);
        return new FriendAuditWrapVO(auditStatusDict, userFriendApplyList);
    }

    private List<QunVO> assembleMyQun(ContactsWrapBO contactsWrap) {
        if (CollectionsUtility.isNullOrEmpty(contactsWrap.getQuns())) {
            return Collections.emptyList();
        }
        List<QunVO> qunVOS = new ArrayList<>(contactsWrap.getQuns().size());
        for (QunBO qunBO : contactsWrap.getQuns()) {
            QunVO qunVO = new QunVO();
            BeanUtility.copyProperties(qunBO, qunVO);
            qunVO.setQunId(qunBO.getId());
            qunVO.setQunName(qunBO.getName());
            qunVOS.add(qunVO);
        }
        return qunVOS;
    }

    private List<UserVO> assembleMyContact(ContactsWrapBO contactsWrap) {
        if (CollectionsUtility.isNullOrEmpty(contactsWrap.getUsers())) {
            return Collections.emptyList();
        }
        List<UserVO> userVOS = new ArrayList<>(contactsWrap.getUsers().size());
        for (UserProfileDTO userProfileDTO : contactsWrap.getUsers()) {
            UserVO userVO = new UserVO();
            BeanUtility.copyProperties(userProfileDTO, userVO);
            userVOS.add(userVO);
        }
        return userVOS;
    }


    public ContactVO assembleVO(ContactsWrapBO contactsWrap) {
        List<QunVO> qunVOS = this.assembleMyQun(contactsWrap);
        List<UserVO> userVOS = this.assembleMyContact(contactsWrap);
        return new ContactVO(qunVOS, userVOS);
    }

    public List<UserVO> assembleUserListVO(Collection<UserProfileDTO> profileDTOS) {
        if (CollectionsUtility.isNullOrEmpty(profileDTOS)) {
            return Collections.emptyList();
        }
        List<UserVO> userVOS = new ArrayList<>(profileDTOS.size());
        for (UserProfileDTO userProfile : profileDTOS) {
            UserVO userVO = new UserVO();
            BeanUtility.copyProperties(userProfile, userVO);
            userVO.setFlagUrl(Nationality.CHINA.getFlag());
            userVOS.add(userVO);
        }
        return userVOS;
    }
}
