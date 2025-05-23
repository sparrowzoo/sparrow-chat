package com.sparrow.chat.contact.bo;

import com.sparrow.chat.contact.protocol.enums.AuditBusiness;
import com.sparrow.protocol.BO;
import com.sparrow.protocol.enums.StatusRecord;
import lombok.Data;

@Data
public class AuditBO implements BO {
    private Long auditId;
    private AuditBusiness auditBusiness;
    /**
     * 业务ID  与业务类型对应
     * 如果是群，则为群ID
     * 如果是好友，则为好友ID
     */
    private Long businessId;
    /**
     * 申请人ID
     */
    private Long applyUserId;
    /**
     * 申请时间
     */
    private Long applyTime;
    /**
     * 审核时间
     */
    private Long auditTime;
    /**
     * 审核人ID
     */
    private Long auditUserId;
    /**
     * 申请的理由
     */
    private String applyReason;
    /**
     * 审核的理由
     */
    private String auditReason;
    /**
     * 审核的状态
     */
    private StatusRecord status;

}
