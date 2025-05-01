package com.sparrow.chat.im.po;

import com.sparrow.protocol.POJO;
import lombok.Data;
import javax.persistence.*;

@Data
@Table(name = "t_session")
public class Session implements POJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int UNSIGNED")
    private Long id;

    @Column(name = "user_id", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '用户id'")
    private String userId;

    @Column(name = "sender_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '发送人名'")
    private String senderName;

    @Column(name = "sender_nick_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '发送人昵称'")
    private String senderNickName;

    @Column(name = "receiver_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '接收人名'")
    private String receiverName;

    @Column(name = "receiver_nick_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '接收人昵称'")
    private String receiverNickName;

    @Column(name = "group_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '群名'")
    private String groupName;


    @Column(name = "category", columnDefinition = "tinyint(2) NOT NULL DEFAULT 0 COMMENT '会话分类 0 visitor, 1 register'")
    private Integer category;

    @Column(name = "session_key", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '会话标识键'")
    private String sessionKey;

    @Column(name = "chat_type", columnDefinition = "tinyint(2) NOT NULL DEFAULT 0 COMMENT '聊天类型 0 1v1 1 1vn'")
    private Integer chatType;

    @Column(name = "gmt_create", columnDefinition = "bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间'")
    private Long gmtCreate;

    @Column(name = "last_read_time", columnDefinition = "bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '最后阅读时间'")
    private Long lastReadTime;

}
