package com.sparrow.chat.contact.assembler;

import com.sparrow.chat.contact.bo.ContactBO;
import com.sparrow.chat.contact.protocol.vo.UserFriendApplyVO;

public class ContactAssembler {

    public UserFriendApplyVO toUserFriendApplyVO(ContactBO contactBO) {
        UserFriendApplyVO userFriendApply = new UserFriendApplyVO();
        userFriendApply.setUserSecretIdentify(contactBO.getSecretIdentify());
        userFriendApply.setNickName(contactBO.getUserDto().getNickName());
        userFriendApply.setAvatar(contactBO.getUserDto().getAvatar());
        return userFriendApply;
    }
}
