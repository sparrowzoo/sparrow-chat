package com.sparrow.chat.contact.protocol.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AuditWrapVO {

    /**
     * 待我审核的申请列表
     */
    private List<AuditVO> auditingList;
    /**
     * 我发起的申请列表
     */
    private List<AuditVO> myApplyingList;
    private Map<Long, ContactVO> contactMap;
    private Map<Long, QunVO> qunMap;
}
