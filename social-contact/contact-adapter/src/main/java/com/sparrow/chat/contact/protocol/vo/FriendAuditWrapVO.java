package com.sparrow.chat.contact.protocol.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendAuditWrapVO {

    public FriendAuditWrapVO(Map<String, String> auditStatusDict, List<FriendAuditVO> friendAudits) {
        this.auditStatusDict = auditStatusDict;
        this.friendAudits = friendAudits;
    }

    /**
     * 审核状态的字典
     */
    private Map<String, String> auditStatusDict = new HashMap<>();

    private List<FriendAuditVO> friendAudits;

    public Map<String, String> getAuditStatusDict() {
        return auditStatusDict;
    }

    public void setAuditStatusDict(Map<String, String> auditStatusDict) {
        this.auditStatusDict = auditStatusDict;
    }

    public List<FriendAuditVO> getFriendAudits() {
        return friendAudits;
    }

    public void setFriendAudits(List<FriendAuditVO> friendAudits) {
        this.friendAudits = friendAudits;
    }
}
