package com.sparrow.chat.po;

import com.sparrow.chat.enums.AuditStatus;
import com.sparrow.chat.enums.BusinessType;
import com.sparrow.protocol.MethodOrder;
import com.sparrow.protocol.POJO;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "audit")
public class Audit implements POJO {

    private Long id;
    private Long userId;
    private BusinessType businessType;
    private Long businessId;
    private Long auditTime;
    private Long auditUserId;
    private String applyReason;
    private String auditReason;
    private AuditStatus status;
    private Long createTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED AUTO_INCREMENT")
    @MethodOrder(order = 1)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @MethodOrder(order = 2)
    @Column(name = "user_id",
        columnDefinition = "int(11)  UNSIGNED DEFAULT 0 COMMENT '用户ID'",
        nullable = false,
        updatable = false)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @MethodOrder(order = 3)
    @Column(
        name = "business_type",
        columnDefinition = "tinyint(1)  DEFAULT 0 COMMENT '业务类型'",
        nullable = false,
        updatable = false
    )
    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    @MethodOrder(order = 4)
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

    @MethodOrder(order = 5)
    @Column(
        name = "audit_user_id",
        columnDefinition = "int(11)  UNSIGNED DEFAULT 0 COMMENT '审核用户ID'",
        nullable = false,
        updatable = false
    )
    public Long getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(Long auditUserId) {
        this.auditUserId = auditUserId;
    }

    @MethodOrder(order = 6)
    @Column(name = "apply_reason", columnDefinition = "varchar(256)  DEFAULT '' COMMENT '申请理由'", nullable = false)
    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    @MethodOrder(order = 7)
    @Column(name = "audit_reason", columnDefinition = "varchar(256)  DEFAULT '' COMMENT '审核理由'", nullable = false)
    public String getAuditReason() {
        return auditReason;
    }

    public void setAuditReason(String auditReason) {
        this.auditReason = auditReason;
    }

    @MethodOrder(order = 8)
    @Column(
        name = "status",
        columnDefinition = "tinyint(1)  DEFAULT 0 COMMENT '审核状态'",
        nullable = false,
        updatable = false
    )
    public AuditStatus getStatus() {
        return status;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    @MethodOrder(order = 9)
    @Column(
        name = "audit_time",
        columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '审核时间'",
        nullable = false,
        updatable = false
    )
    public Long getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Long auditTime) {
        this.auditTime = auditTime;
    }

    @MethodOrder(order = 10)
    @Column(
        name = "create_time",
        columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '创建时间'",
        nullable = false,
        updatable = false
    )
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
