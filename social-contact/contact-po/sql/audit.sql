DROP TABLE IF EXISTS `audit`;
CREATE TABLE `audit` (
                         `id` int(11) UNSIGNED AUTO_INCREMENT NOT NULL AUTO_INCREMENT,
                         `apply_user_id` int(11)  UNSIGNED DEFAULT 0 COMMENT '用户ID'  NOT NULL,
                         `business_type` tinyint(1)  DEFAULT 0 COMMENT '业务类型'  NOT NULL,
                         `business_id` int(11)  UNSIGNED DEFAULT 0 COMMENT '业务ID'  NOT NULL,
                         `audit_user_id` int(11)  UNSIGNED DEFAULT 0 COMMENT '审核用户ID'  NOT NULL,
                         `apply_time` bigint(11)  DEFAULT 0 COMMENT '申请时间'  NOT NULL,
                         `apply_reason` varchar(256)  DEFAULT '' COMMENT '申请理由'  NOT NULL,
                         `audit_reason` varchar(256)  DEFAULT '' COMMENT '审核理由'  NOT NULL,
                         `status` tinyint(1)  DEFAULT 0 COMMENT '审核状态'  NOT NULL,
                         `audit_time` bigint(11)  DEFAULT 0 COMMENT '审核时间'  NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='audit';
