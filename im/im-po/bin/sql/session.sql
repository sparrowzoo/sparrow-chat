CREATE TABLE `session` (
                           `id` int unsigned NOT NULL AUTO_INCREMENT,
                           `user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户id',
                           `category` tinyint NOT NULL DEFAULT '0' COMMENT '会话分类 0 visitor, 1 register',
                           `session_key` varchar(64) NOT NULL DEFAULT '' COMMENT '会话标识键',
                           `chat_type` tinyint NOT NULL DEFAULT '0' COMMENT '聊天类型 0 1v1 1 1vn',
                           `gmt_create` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
                           `last_read_time` bigint unsigned NOT NULL DEFAULT '0' COMMENT '最后阅读时间',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='session'
