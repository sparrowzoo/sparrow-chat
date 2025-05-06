package com.sparrow.chat.contact.po;

import com.sparrow.protocol.POJO;
import lombok.Data;

import javax.persistence.*;

@Table(name = "t_contact")
@Data
public class Contact implements POJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED AUTO_INCREMENT")
    private Long id;
    @Column(
            name = "user_id",
            columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '用户ID'",
            nullable = false,
            updatable = false
    )
    private Long userId;

    @Column(
            name = "friend_id",
            columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '好友ID'",
            nullable = false,
            updatable = false
    )
    private Long friendId;
    @Column(
            name = "apply_time",
            columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '申请时间'",
            nullable = false,
            updatable = false
    )
    private Long applyTime;
    @Column(
            name = "audit_time",
            columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '审核时间'",
            nullable = false,
            updatable = false
    )
    private Long auditTime;
}
