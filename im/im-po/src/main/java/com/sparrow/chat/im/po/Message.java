package com.sparrow.chat.im.po;

import com.sparrow.protocol.POJO;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class Message implements POJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int UNSIGNED")
    /**
     * ID 主键自增
     */
    private Long id;
    /**
     * 消息类型
     * <p>
     * TEXT_MESSAGE = 0
     * IMAGE_MESSAGE = 1
     */
    @Column(name = "message_type", columnDefinition = "tinyint(2) not null default 0 comment '消息类型 0-文本 1-图片'")
    private Integer messageType;
    /**
     * 发送者
     */
    @Column(name = "sender", columnDefinition = "varchar(64) not null default '' comment '发送者'")
    private String sender;
    /**
     * 发送者类型
     */
    @Column(name = "sender_category", columnDefinition = "tinyint(2) not null default 0 comment '发送者类型 0-游客 1-用户'")
    private Integer senderCategory;
    /**
     * 接收者
     */
    @Column(name = "receiver", columnDefinition = "varchar(64) not null default '' comment '接收者'")
    private String receiver;
    /**
     * 接收者类型
     */
    @Column(name = "receiver_category", columnDefinition = "tinyint not null default 0 comment '接收者类型 0-游客 1-用户'")
    private Integer receiverCategory;

    /**
     * 会话key
     */
    @Column(name = "session_key", columnDefinition = "varchar(64) not null default '' comment '会话key'")
    private String sessionKey;
    /**
     * 会话 0-单聊 1-群聊
     */
    @Column(name = "chat_type", columnDefinition = "tinyint not null default 0 comment '会话 0-单聊 1-群聊'")
    private Integer chatType;
    /**
     * 消息内容
     */
    @Column(name = "content", columnDefinition = "text  null  comment '消息内容'")
    private String content;
    /**
     * 服务器时间
     */
    @Column(name = "server_time", columnDefinition = "bigint not null default 0 comment '服务器时间'")
    private Long serverTime;
    /**
     * 客户端发送时间
     */
    @Column(name = "client_send_time", columnDefinition = "bigint not null default 0 comment '客户端发送时间'")
    private Long clientSendTime;

    @Column(name = "ip", columnDefinition = "int UNSIGNED not null default 0 comment 'ip地址'")
    private Integer ip;
}
