package com.sparrow.chat.contact.bo;

import com.sparrow.passport.protocol.dto.UserProfileDTO;

import java.util.List;
import java.util.Map;

public class AuditWrapBO {

    public AuditWrapBO(List<AuditBO> auditList, Map<Long,UserProfileDTO> userProfiles) {
        this.auditList = auditList;
        this.friendMap = userProfiles;
    }

    /**
     * 好友的申请记录
     */
    private List<AuditBO> auditList;
    /**
     * 好友的基本信息
     * <p>
     * key:好友的id
     * <p>
     * value：好友的基本信息
     */
    private Map<Long, UserProfileDTO> friendMap;


    public List<AuditBO> getAuditList() {
        return auditList;
    }

    public Map<Long, UserProfileDTO> getFriendMap() {
        return friendMap;
    }
}
