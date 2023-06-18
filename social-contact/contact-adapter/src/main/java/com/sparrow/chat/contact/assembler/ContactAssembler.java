package com.sparrow.chat.contact.assembler;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.ContactBO;
import com.sparrow.chat.contact.bo.FriendAuditWrapBO;
import com.sparrow.chat.contact.protocol.vo.FriendAuditVO;
import com.sparrow.chat.contact.protocol.vo.FriendAuditWrapVO;
import com.sparrow.chat.contact.protocol.vo.UserFriendApplyVO;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.EnumUtility;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
public class ContactAssembler {
    private static final String STATUS_BUSINESS = "audit";

    public UserFriendApplyVO toUserFriendApplyVO(ContactBO contactBO) {
        UserFriendApplyVO userFriendApply = new UserFriendApplyVO();
        userFriendApply.setUserSecretIdentify(contactBO.getSecretIdentify());
        userFriendApply.setNickName(contactBO.getUserDto().getNickName());
        userFriendApply.setAvatar(contactBO.getUserDto().getAvatar());
        return userFriendApply;
    }

    public FriendAuditWrapVO toUserFriendApplyVoList(FriendAuditWrapBO friendAuditWrap) {
        List<AuditBO> auditBOS = friendAuditWrap.getAuditList();
        List<FriendAuditVO> userFriendApplyList = new ArrayList<>();
        Map<Long, UserProfileDTO> userDictionaries = friendAuditWrap.getFriendMap();
        for (AuditBO audit : auditBOS) {
            FriendAuditVO friendAuditVO = new FriendAuditVO();
            friendAuditVO.setAuditId(audit.getAuditId());
            friendAuditVO.setAuditStatus(audit.getStatus().ordinal());
            UserProfileDTO applyUser = userDictionaries.get(audit.getApplyUserId());
            if(applyUser!=null) {
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
}
