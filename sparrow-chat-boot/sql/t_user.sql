/*
 Navicat Premium Data Transfer

 Source Server         : sparrow
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : sparrow

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 25/04/2025 21:24:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` int unsigned NOT NULL AUTO_INCREMENT,
  `category` int NOT NULL DEFAULT '0' COMMENT '用户类别',
  `english_name` varchar(64) NOT NULL DEFAULT '',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '昵称',
  `email` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'E-MAIL',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '密码',
  `gender` tinyint NOT NULL DEFAULT '0' COMMENT '性别',
  `avatar` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '头象',
  `nationality` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '国籍',
  `personal_signature` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '签名',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '手机号码',
  `last_login_time` bigint NOT NULL DEFAULT '0' COMMENT '最近登录时间',
  `activate` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否激活',
  `activate_time` bigint NOT NULL DEFAULT '0' COMMENT '激活时间',
  `channel` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '渠道来源',
  `device` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '设备',
  `device_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '设备id',
  `device_model` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '设备模型',
  `ip` bigint NOT NULL DEFAULT '0' COMMENT 'ip',
  `status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'STATUS',
  `gmt_create` bigint NOT NULL DEFAULT '0' COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='user';

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user` VALUES (1, 1, '', 'zh_harry', 'zh_harry', 'zh_harry@163.com', '393E54E2DECF9DE2B4B91EEEE6EF4E31', 0, '', 'china', '这里是签针 我是大傻瓜这里是签针 我是大傻瓜这里是签针 我是大傻瓜这里是签针 我是大傻瓜', NULL, '', 1743233199028, 0, 0, '', 'Chrome 13', '', '', 0, 1, 1743233199028, 1743233199028);
INSERT INTO `t_user` VALUES (2, 2, '', 'zhangsan', '张三', 'zh_harry222@163.com', '393E54E2DECF9DE2B4B91EEEE6EF4E31', 0, '', 'china', '这里是签针 我是大傻瓜这里是签针 我是大傻瓜这里是签针 我是大傻瓜这里是签针 我是大傻瓜这里是签针 我是大傻瓜', NULL, '', 1743338884451, 0, 0, '', 'Chrome 13', '', '', 0, 1, 1743338884450, 1743338884450);
INSERT INTO `t_user` VALUES (3, 3, '', 'sparrowzoo', 'sparrowzoo(学生客户)', 'sparrowzoo@163.com', '393E54E2DECF9DE2B4B91EEEE6EF4E31', 0, '', 'china', '这里是签针 我是大傻瓜这里是签针 我是大傻瓜', NULL, '', 1743339146549, 0, 0, '', 'Chrome 13', '', '', 0, 1, 1743339146549, 1743339146549);
INSERT INTO `t_user` VALUES (4, 4, '', 'lisi', '李四', 'sparrowzoo@163.com', '393E54E2DECF9DE2B4B91EEEE6EF4E31', 0, '', 'china', '这里是签针 我是大傻瓜这里是签针 我是大傻瓜这里是签针 我是大傻瓜这里是签针 我是大傻瓜', NULL, '', 1743339146549, 0, 0, '', 'Chrome 13', '', '', 0, 1, 1743339146549, 1743339146549);
INSERT INTO `t_user` VALUES (5, 5, '', 'wangwu', '王五(中介)', 'sparrowzoo@163.com', '393E54E2DECF9DE2B4B91EEEE6EF4E31', 0, '', 'china', '这里是签针 我是大傻瓜这里是签针 我是大傻瓜这里是签针 我是大傻瓜', NULL, '', 1743339146549, 0, 0, '', 'Chrome 13', '', '', 0, 1, 1743339146549, 1743339146549);
INSERT INTO `t_user` VALUES (6, 6, '', 'zhangliu', '赵六', 'sparrowzoo@163.com', '393E54E2DECF9DE2B4B91EEEE6EF4E31', 0, '', 'china', '这里是签针 我是大傻瓜这里是签针 我是大傻瓜', NULL, '', 1743339146549, 0, 0, '', 'Chrome 13', '', '', 0, 1, 1743339146549, 1743339146549);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
