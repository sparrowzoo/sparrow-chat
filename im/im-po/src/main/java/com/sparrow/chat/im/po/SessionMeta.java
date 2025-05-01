package com.sparrow.chat.im.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class SessionMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int UNSIGNED")
    private Long id;

    @Column(name = "session_key", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '发送人名'")
    private String sessionKey;

    @Column(name = "sender_id", columnDefinition = "int(10) NOT NULL DEFAULT 0 COMMENT '发送人ID'")
    private Long senderId;

    @Column(name = "sender_category", columnDefinition = "int(10) NOT NULL DEFAULT 0 COMMENT '发送人分类")
    private Long senderCategory;

    @Column(name = "sender_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '发送人名'")
    private String senderName;

    @Column(name = "sender_nick_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '发送人昵称'")
    private String senderNickName;

    @Column(name = "receiver_id", columnDefinition = "int(10) NOT NULL DEFAULT '' COMMENT 'receiver ID'")
    private Long receiverId;

    @Column(name = "receiver_category", columnDefinition = "int(10) NOT NULL DEFAULT 0 COMMENT '接受人分类")
    private Long receiverCategory;

    @Column(name = "receiver_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '接收人名'")
    private String receiverName;

    @Column(name = "receiver_nick_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '接收人昵称'")
    private String receiverNickName;

    @Column(name = "group_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '群名'")
    private String groupName;


    @Column(name = "is_visitor", columnDefinition = "tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否临时会话 1 visitor, 0 register'")
    private Integer isVisitor;

    @Column(name = "chat_type", columnDefinition = "tinyint(2) NOT NULL DEFAULT 0 COMMENT '聊天类型 0 1v1 1 1vn'")
    private Integer chatType;

    @Column(name = "gmt_create", columnDefinition = "bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间'")
    private Long gmtCreate;
}
