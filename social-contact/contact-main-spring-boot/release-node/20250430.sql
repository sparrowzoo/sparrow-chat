DROP TABLE IF EXISTS `t_session_meta`;
CREATE TABLE `t_session_meta`
(
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `session_key` varchar(64) default '' not null comment '会话标识',
    `chat_type` tinyint default 0 not null comment '会话类型',
    `is_visitor` tinyint default 0 not null comment '是否临时会话',
    `sender_category` tinyint default 0 not null comment '发送人类别',
    `sender_id` int(10) default 0 not null comment '发送者ID',
    `sender_name` varchar(64) default '' not null comment '发送者名称',
    `sender_nick_name` varchar(64) default '' not null comment '发送者昵称',
    `receiver_id` int(10) default 0 not null comment '接收者ID',
    `receiver_category` tinyint default 0 not null comment '发送人类别',
    `receiver_name` varchar(64) default '' not null comment '接收者名称',
    `receiver_nick_name` varchar(64) default '' not null comment '接收者昵称',
    `group_name` varchar(64) default '' not null  comment '群组名称',
    `gmt_create` bigint NOT NULL DEFAULT '0' COMMENT '创建时间',

    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4  ROW_FORMAT=DYNAMIC COMMENT='会话元数据';


