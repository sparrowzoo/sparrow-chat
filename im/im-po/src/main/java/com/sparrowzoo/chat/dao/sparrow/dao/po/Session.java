package com.sparrowzoo.chat.dao.sparrow.dao.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED AUTO_INCREMENT")
    private Long id;

    @Column(name = "user_id", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '用户id")
    private String userId;


    @Column(name = "category", columnDefinition = "tinyint(2) NOT NULL DEFAULT 0 COMMENT '会话分类 0 visitor, 1 register'")
    private Integer category;

    @Column(name = "session_key", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '会话标识键'")
    private String sessionKey;

    @Column(name = "chat_type", columnDefinition = "tinyint(2) NOT NULL DEFAULT 0 COMMENT '聊天类型 0 1v1 1 1vn'")
    private Integer chatType;

    @Column(name = "gmt_create", columnDefinition = "bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间'")
    private Long gmtCreate;

    @Column(name = "last_read_time", columnDefinition = "bigint(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最后阅读时间'")
    private Long lastReadTime;

}
