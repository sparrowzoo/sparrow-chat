CREATE TABLE `message` (
                           `id` int unsigned NOT NULL AUTO_INCREMENT,
                           `message_type` tinyint NOT NULL DEFAULT '0' COMMENT '消息类型 0-文本 1-图片',
                           `sender` varchar(64) NOT NULL DEFAULT '' COMMENT '发送者',
                           `sender_category` tinyint NOT NULL DEFAULT '0' COMMENT '发送者类型 0-游客 1-用户',
                           `receiver` varchar(64) NOT NULL DEFAULT '' COMMENT '接收者',
                           `receiver_category` tinyint NOT NULL DEFAULT '0' COMMENT '接收者类型 0-游客 1-用户',
                           `session_key` varchar(64) NOT NULL DEFAULT '' COMMENT '会话key',
                           `chat_type` tinyint NOT NULL DEFAULT '0' COMMENT '会话 0-单聊 1-群聊',
                           `content` text COMMENT '消息内容',
                           `server_time` bigint NOT NULL DEFAULT '0' COMMENT '服务器时间',
                           `client_send_time` bigint NOT NULL DEFAULT '0' COMMENT '客户端发送时间',
                           `ip` int unsigned NOT NULL DEFAULT '0' COMMENT 'ip地址',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='message'
