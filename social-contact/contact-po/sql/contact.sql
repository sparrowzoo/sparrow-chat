DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact` (
                           `id` int(11) UNSIGNED AUTO_INCREMENT NOT NULL AUTO_INCREMENT,
                           `user_id` int(11) UNSIGNED  DEFAULT 0 COMMENT '用户ID'  NOT NULL,
                           `friend_id` int(11) UNSIGNED  DEFAULT 0 COMMENT '好友ID'  NOT NULL,
                           `apply_time` bigint(11)  DEFAULT 0 COMMENT '申请时间'  NOT NULL,
                           `audit_time` bigint(11)  DEFAULT 0 COMMENT '审核时间'  NOT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='contact';
