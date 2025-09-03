package com.sparrow.chat.contact.po;

import com.sparrow.protocol.POJO;
import lombok.Data;

import javax.persistence.*;

@Table(name = "t_audit")
@Data
public class Audit implements POJO {
    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED AUTO_INCREMENT")
    private Long id;
    /**
     * 申请人ID
     */
    @Column(name = "apply_user_id",
            columnDefinition = "int(11)  UNSIGNED DEFAULT 0 COMMENT '用户ID'",
            nullable = false,
            updatable = false)
    private Long applyUserId;
    /**
     * 业务类型  申请的群或者好友ID
     */
    @Column(
            name = "business_type",
            columnDefinition = "tinyint(1)  DEFAULT 0 COMMENT '业务类型'",
            nullable = false,
            updatable = false
    )
    private Integer businessType;
    /**
     * 业务ID  与业务类型对应
     * 如果是群，则为群ID
     * 如果是好友，则为好友ID
     */

    @Column(
            name = "business_id",
            columnDefinition = "int(11)  UNSIGNED DEFAULT 0 COMMENT '业务ID'",
            nullable = false,
            updatable = false
    )
    private Long businessId;
    /**
     * 申请时间
     */
    @Column(
            name = "apply_time",
            columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '申请时间'",
            nullable = false
    )
    private Long applyTime;
    /**
     * 审核时间
     */

    @Column(
            name = "audit_time",
            columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '审核时间'",
            nullable = false
    )
    private Long auditTime;
    /**
     * 审核人ID
     */
    @Column(
            name = "audit_user_id",
            columnDefinition = "int(11)  UNSIGNED DEFAULT 0 COMMENT '审核用户ID'",
            nullable = false
    )
    private Long auditUserId;
    /**
     * 申请的理由
     */
    @Column(name = "apply_reason", columnDefinition = "varchar(256)  DEFAULT '' COMMENT '申请理由'", nullable = false)
    private String applyReason;
    /**
     * 审核的理由
     */
    @Column(name = "audit_reason", columnDefinition = "varchar(256)  DEFAULT '' COMMENT '审核理由'", nullable = false)
    private String auditReason;
    /**
     * 审核的状态
     */
    @Column(
            name = "status",
            columnDefinition = "tinyint(1)  DEFAULT 0 COMMENT '审核状态'",
            nullable = false
    )
    private Integer status;

}
