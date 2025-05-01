DROP TABLE IF EXISTS `t_customer_service`;
CREATE TABLE `t_customer_service`
(
    `id`                 int unsigned NOT NULL AUTO_INCREMENT,
    `tenant_id`          varchar(64)                       NOT NULL DEFAULT '' COMMENT '租户标识',
    `category`           int unsigned NOT NULL DEFAULT '0' COMMENT '租户类型 平台/中介',
    `server_id`          int unsigned NOT NULL DEFAULT '0' COMMENT '客服ID',
    `server_name`        varchar(64)                       NOT NULL DEFAULT '' COMMENT '客服名',
    `status`             tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态',
    `create_user_id`     int unsigned NOT NULL DEFAULT '0' COMMENT '创建人ID',
    `gmt_create`         bigint                            NOT NULL DEFAULT '0' COMMENT '创建时间',
    `modified_user_id`   int unsigned NOT NULL DEFAULT '0' COMMENT '更新人ID',
    `gmt_modified`       bigint                            NOT NULL DEFAULT '0' COMMENT '更新时间',
    `create_user_name`   varchar(64) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '创建人',
    `modified_user_name` varchar(64) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座席客服'
