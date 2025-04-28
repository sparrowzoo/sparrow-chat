package com.sparrow.chat.contact.bo;

import com.sparrow.chat.contact.protocol.enums.AuditBusiness;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.utility.CollectionsUtility;
import lombok.Data;

import java.util.*;

@Data
public class AuditWrapBO {
    public AuditWrapBO(List<AuditBO> auditingList, List<AuditBO> myApplingList) {
        this.auditingList = auditingList;
        this.myApplingList = myApplingList;
    }

    /**
     * 待我审核的申请列表
     */
    private List<AuditBO> auditingList;
    /**
     * 我发起的申请列表
     */
    private List<AuditBO> myApplingList;

    private Map<Long, UserProfileDTO> userInfoMap;
    private Map<Long, QunBO> qunMap;

    public Set<Long> getUserIds() {
        Set<Long> userIds = new HashSet<>();
        if (!CollectionsUtility.isNullOrEmpty(auditingList)) {
            for (AuditBO auditBO : auditingList) {
                userIds.add(auditBO.getApplyUserId());
                userIds.add(auditBO.getBusinessId());
            }
        }

        if (!CollectionsUtility.isNullOrEmpty(myApplingList)) {
            for (AuditBO auditBO : myApplingList) {
                    userIds.add(auditBO.getApplyUserId());
                    userIds.add(auditBO.getBusinessId());
            }
        }
        return userIds;
    }


    public List<Long> getQunIds() {
        List<Long> qunIds = new ArrayList<>();
        if (!CollectionsUtility.isNullOrEmpty(auditingList)) {
            for (AuditBO auditBO : auditingList) {
                if(auditBO.getAuditBusiness() == AuditBusiness.GROUP) {
                    qunIds.add(auditBO.getBusinessId());
                }
            }
        }
        if (!CollectionsUtility.isNullOrEmpty(myApplingList)) {
            for (AuditBO auditBO : myApplingList) {
                if (auditBO.getAuditBusiness() == AuditBusiness.GROUP) {
                    qunIds.add(auditBO.getBusinessId());
                }
            }
        }
        return qunIds;
    }
}
