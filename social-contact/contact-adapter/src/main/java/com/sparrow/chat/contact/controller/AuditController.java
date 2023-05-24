package com.sparrow.chat.contact.controller;

import com.sparrow.chat.contact.protocol.audit.FriendApplyParam;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;
import com.sparrow.chat.contact.protocol.vo.FriendAuditVO;

import java.util.List;

public class AuditController {

    /**
     * 好友申请列表
     *
     * @return
     */
    public List<FriendAuditVO> friendApplyList() {
        //todo list
        return null;
    }

    /**
     * 确认申请好友
     *
     * @param friendApplyParam
     * @return
     */
    public Boolean applyFriend(FriendApplyParam friendApplyParam) {
        return true;
    }

    /**
     * 审核好友申请
     *
     * @param friendAuditParam
     * @return
     */
    public Boolean auditFriendApply(FriendAuditParam friendAuditParam) {
        return true;
    }
}
