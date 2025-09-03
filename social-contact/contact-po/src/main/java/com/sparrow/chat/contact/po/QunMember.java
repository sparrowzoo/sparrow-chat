package com.sparrow.chat.contact.po;

import com.sparrow.protocol.POJO;
import lombok.Data;

import javax.persistence.*;

@Table(name = "t_qun_member")
@Data
public class QunMember implements POJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED")
    private Long id;
    @Column(name = "qun_id", columnDefinition = "int(11) UNSIGNED DEFAULT 0 COMMENT '群ID'", nullable = false, updatable = false)
    private Long qunId;
    @Column(name = "member_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '群成员ID'", nullable = false, updatable = false)
    private Long memberId;
    @Column(name = "apply_time", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '申请时间'", nullable = false, updatable = false)
    private Long applyTime;
    @Column(name = "audit_time", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '审核时间'", nullable = false, updatable = false)
    private Long auditTime;
}
