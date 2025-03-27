DROP TABLE IF EXISTS `qun`;
CREATE TABLE `qun` (
                       `id` int(11) UNSIGNED  NOT NULL AUTO_INCREMENT,
                       `name` varchar(32)  DEFAULT '' COMMENT '群名称'  NOT NULL,
                       `avatar` varchar(256)  DEFAULT '' COMMENT '群头象'  NOT NULL,
                       `announcement` varchar(255)  DEFAULT '' COMMENT '群公告'  NOT NULL,
                       `nationality_id` int(11)  DEFAULT 0 COMMENT '国籍id'  NOT NULL,
                       `organization_id` int(11) UNSIGNED  DEFAULT 0 COMMENT '机构ID'  NOT NULL,
                       `owner_id` int(11) UNSIGNED  DEFAULT 0 COMMENT '群主'  NOT NULL,
                       `category_id` int(11) UNSIGNED  DEFAULT 0 COMMENT '所属类别'  NOT NULL,
                       `remark` varchar(255)  DEFAULT '' COMMENT '备注'  NOT NULL,
                       `status` tinyint(2)  DEFAULT 1 COMMENT '状态'  NOT NULL,
                       `create_user_id` int(11) UNSIGNED  DEFAULT 0 COMMENT '创建人ID'  NOT NULL,
                       `gmt_create` bigint(11)  DEFAULT 0 COMMENT '创建时间'  NOT NULL,
                       `modified_user_id` int(11) unsigned  DEFAULT 0 COMMENT '更新人ID'  NOT NULL,
                       `gmt_modified` bigint(11)  DEFAULT 0 COMMENT '更新时间'  NOT NULL,
                       `create_user_name` varchar(64)  DEFAULT '' COMMENT '创建人'  NOT NULL,
                       `modified_user_name` varchar(64)  DEFAULT '' COMMENT '更新人'  NOT NULL,
                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='qun';
