package com.sparrow.chat.contact.bo;

import com.sparrow.passport.protocol.dto.UserProfileDTO;

import java.util.List;
import java.util.Map;

public class QunMemberWrapBO {

    public QunMemberWrapBO(List<QunMemberBO> qunMemberBOS, Map<Long, UserProfileDTO> userProfileDTOMap) {
        this.qunMemberBOS = qunMemberBOS;
        this.userProfileDTOMap = userProfileDTOMap;
    }

    private List<QunMemberBO> qunMemberBOS;
    private Map<Long, UserProfileDTO> userProfileDTOMap;

    public List<QunMemberBO> getQunMemberBOS() {
        return qunMemberBOS;
    }

    public void setQunMemberBOS(List<QunMemberBO> qunMemberBOS) {
        this.qunMemberBOS = qunMemberBOS;
    }

    public Map<Long, UserProfileDTO> getUserProfileDTOMap() {
        return userProfileDTOMap;
    }

    public void setUserProfileDTOMap(Map<Long, UserProfileDTO> userProfileDTOMap) {
        this.userProfileDTOMap = userProfileDTOMap;
    }
}
