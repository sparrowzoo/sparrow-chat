package com.sparrow.chat.contact.po;

import com.sparrow.protocol.POJO;

import javax.persistence.*;

@Table(name = "t_audit")
public class Audit implements POJO {
    /**
     * 主键 ID
     */
    private Long id;
    /**
     * 申请人ID
     */
    private Long applyUserId;
    /**
     * 业务类型  申请的群或者好友ID
     */
    private Integer businessType;
    /**
     * 业务ID  与业务类型对应
     * 如果是群，则为群ID
     * 如果是好友，则为好友ID
     */
    private Long businessId;
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
    private Integer status;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED AUTO_INCREMENT")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "apply_user_id",
            columnDefinition = "int(11)  UNSIGNED DEFAULT 0 COMMENT '用户ID'",
            nullable = false,
            updatable = false)
    public Long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
    }

    @Column(
            name = "business_type",
            columnDefinition = "tinyint(1)  DEFAULT 0 COMMENT '业务类型'",
            nullable = false,
            updatable = false
    )
    public Integer getBusinessType() {
        return businessType;
    }

    @Column(
            name = "business_id",
            columnDefinition = "int(11)  UNSIGNED DEFAULT 0 COMMENT '业务ID'",
            nullable = false,
            updatable = false
    )
    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    @Column(
            name = "audit_user_id",
            columnDefinition = "int(11)  UNSIGNED DEFAULT 0 COMMENT '审核用户ID'",
            nullable = false
    )
    public Long getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(Long auditUserId) {
        this.auditUserId = auditUserId;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    @Column(
            name = "apply_time",
            columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '申请时间'",
            nullable = false
    )
    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }


    @Column(name = "apply_reason", columnDefinition = "varchar(256)  DEFAULT '' COMMENT '申请理由'", nullable = false)
    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    @Column(name = "audit_reason", columnDefinition = "varchar(256)  DEFAULT '' COMMENT '审核理由'", nullable = false)
    public String getAuditReason() {
        return auditReason;
    }

    public void setAuditReason(String auditReason) {
        this.auditReason = auditReason;
    }

    @Column(
            name = "status",
            columnDefinition = "tinyint(1)  DEFAULT 0 COMMENT '审核状态'",
            nullable = false
    )
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(
            name = "audit_time",
            columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '审核时间'",
            nullable = false
    )
    public Long getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Long auditTime) {
        this.auditTime = auditTime;
    }
}
