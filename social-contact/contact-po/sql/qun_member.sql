DROP TABLE IF EXISTS `qun_member`;
CREATE TABLE `qun_member` (
 `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
 `qun_id` int(11) UNSIGNED DEFAULT 0 COMMENT '群ID'  NOT NULL,
 `member_id` int(11) UNSIGNED  DEFAULT 0 COMMENT '群成员ID'  NOT NULL,
 `apply_time` bigint(11)  DEFAULT 0 COMMENT '申请时间'  NOT NULL,
 `audit_time` bigint(11)  DEFAULT 0 COMMENT '审核时间'  NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='qun_member';
