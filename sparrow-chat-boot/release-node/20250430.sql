DROP TABLE IF EXISTS `t_session_meta`;
CREATE TABLE `t_session_meta`
(
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `session_key` varchar(64) default '' not null comment '会话标识',
    `is_visitor` tinyint default 0 not null comment '是否临时会话',
    `user_category` tinyint default 0 not null comment '用户类别',
    `user_id` int(10) default 0 not null comment '用户ID',
    `user_name` varchar(64) default '' not null comment '用户名',
    `user_nick_name` varchar(64) default '' not null comment '用户昵称',
    `opposite_id` int(10) default 0 not null comment '对方ID',
    `opposite_category` tinyint default 0 not null comment '对方类别',
    `opposite_name` varchar(64) default '' not null comment '对方名',
    `opposite_nick_name` varchar(64) default '' not null comment '对方昵称',
    `gmt_create` bigint default 0 not null comment '创建时间',
    `gmt_modified` bigint default 0 not null comment '更新时间',
    `status` tinyint default 0 not null comment '状态',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4  ROW_FORMAT=DYNAMIC COMMENT='会话元数据';


alter table t_session
add column sync_time bigint default 0 not null comment '同步时间',
add column `status` tinyint default 1 not null comment '状态',
modify column user_id int unsigned default 0 not null comment '用户id'


alter table t_user
add tenant_id int null comment '租户ID';

