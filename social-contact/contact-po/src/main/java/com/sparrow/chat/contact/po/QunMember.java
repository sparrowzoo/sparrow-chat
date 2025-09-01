package com.sparrow.chat.contact.po;

import com.sparrow.protocol.POJO;

import javax.persistence.*;

@Table(name = "t_qun_member")
public class QunMember implements POJO {
    private Long id;
    private Long qunId;
    private Long memberId;
    private Long applyTime;
    private Long auditTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "qun_id", columnDefinition = "int(11) UNSIGNED DEFAULT 0 COMMENT '群ID'", nullable = false, updatable = false)
    public Long getQunId() {
        return qunId;
    }

    public void setQunId(Long qunId) {
        this.qunId = qunId;
    }

    @Column(name = "member_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '群成员ID'", nullable = false, updatable = false)
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Column(name = "apply_time", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '申请时间'", nullable = false, updatable = false)
    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    @Column(name = "audit_time", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '审核时间'", nullable = false, updatable = false)
    public Long getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Long auditTime) {
        this.auditTime = auditTime;
    }
}
