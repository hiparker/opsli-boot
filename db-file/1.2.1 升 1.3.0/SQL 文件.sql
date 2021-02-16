/*
 Navicat Premium Data Transfer

 Source Server         : 本地数据库连接
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : opsli-boot

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 16/02/2021 22:23:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 代码生成器BUG 修复
ALTER TABLE `creater_table_column` MODIFY COLUMN `field_length` int(11) NULL DEFAULT NULL COMMENT '字段长度' AFTER `field_type`;
ALTER TABLE `creater_table_column` MODIFY COLUMN `field_precision` int(11) NULL DEFAULT NULL COMMENT '字段长度' AFTER `field_type`;

-- ----------------------------
-- Table structure for sys_options
-- ----------------------------
DROP TABLE IF EXISTS `sys_options`;
CREATE TABLE `sys_options` (
  `id` bigint(19) NOT NULL COMMENT '唯一主键',
  `option_code` varchar(100) NOT NULL COMMENT '参数编号',
  `option_name` varchar(200) NOT NULL COMMENT '参数名称',
  `option_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数值',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index` (`option_code`) USING BTREE COMMENT '参数编号唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统参数表';

-- ----------------------------
-- Records of sys_options
-- ----------------------------
BEGIN;
INSERT INTO `sys_options` VALUES (1, 'crypto_asymmetric', '非对称加解密算法', 'RSA', 0, 1, '2021-02-10 23:19:35', 1313694379541635074, '2021-02-16 21:35:58');
INSERT INTO `sys_options` VALUES (2, 'crypto_asymmetric_public_key', '非对称加解密-公钥', 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQS75ZTYUL6IJJBgylgFDtksWZx4OsVK5CBJGXi3n9LON1Jg7KsxkgindCD28gQRIEh6ZP1IuhlMs9N/QteRbM3b1oDKW7Cbr7lFk+EYUmpO5nwD1+IHowiNc0AK+XfICOVF5287wE4LFLqBDnWhV0WNQFjYYpAc7852ZfEwThSQIDAQAB', 0, 1, '2021-02-10 23:19:35', 1313694379541635074, '2021-02-16 21:35:58');
INSERT INTO `sys_options` VALUES (3, 'crypto_asymmetric_private_key', '非对称加解密-私钥', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJBLvllNhQvogkkGDKWAUO2SxZnHg6xUrkIEkZeLef0s43UmDsqzGSCKd0IPbyBBEgSHpk/Ui6GUyz039C15FszdvWgMpbsJuvuUWT4RhSak7mfAPX4gejCI1zQAr5d8gI5UXnbzvATgsUuoEOdaFXRY1AWNhikBzvznZl8TBOFJAgMBAAECgYAJvtCmFi7RK6ysIxhBj6JnmTN24ltJpuJ2zpL8Sc0J+B+LyIk6z9CROqjS5L+PM+kN0BEmCedwTBNTf1VV3BXzbPcX/prE004LXcbv5mjgaHf/PybzJQumYM5MuD9dJYBLc1PUNu2b9eGekEU+vzn0HCnmAkfwU7FCdU7Nh8/ZnQJBAMkY/nzYxue0rdfD4ZybM8chBksp9EA8mwmCW/PosJGJ6YVJ913PbfovYuSG4rwm0Ew6i/1zcXvAFjt5dW+L5EsCQQC3sMq5EF4Bfils0dTdn8Pwtv2t3H6wwaAc9QAExHYtZ5pipFH9NXiWn6KUJYq28mRxxKtfoara/8Ahb5yHY0w7AkEArGnmfyno123cgppqC7gxW3AgEj+FL7IGhs+igOumvxFMCsBQ+rhGpXMNSbuwF/r7KfAkaAgbaytUpGdNXXbGIwJAas7uoXsl3iJYvgCokJFkYmRUzzJlrCt6CTxgXWVK/g2+1FqNnfjofFSoORI3PTdmNkzQBRRA/4Q0WHzIfGS9nwJBAMLXrVKH6uhn67dwXKRCy2Xt54dLEtL43jL+xzWeHJibSkSyImLiAn1n2imSpB6ubJnBuvH19Y0nOXKBP0+VDGA=', 0, 1, '2021-02-10 23:19:35', 1313694379541635074, '2021-02-16 21:35:58');
COMMIT;

-- 菜单
INSERT INTO `sys_menu`(`id`, `parent_id`, `menu_code`, `menu_name`, `icon`, `type`, `url`, `component`, `redirect`, `sort_no`, `hidden`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1360233188433977345, 1, 'system_options', '参数配置', '', '1', 'set', 'views/modules/sys/setManagement/index', NULL, 99, '0', '0', 3, 1313694379541635074, '2021-02-12 22:23:59', 1313694379541635074, '2021-02-16 21:44:06');
INSERT INTO `sys_menu`(`id`, `parent_id`, `menu_code`, `menu_name`, `icon`, `type`, `url`, `component`, `redirect`, `sort_no`, `hidden`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1360233383397810177, 1360233188433977345, 'system_options_update', '更新', '', '2', NULL, NULL, NULL, 1, '0', '0', 2, 1313694379541635074, '2021-02-12 22:24:45', 1313694379541635074, '2021-02-14 01:37:07');

-- 字典
INSERT INTO `sys_dict`(`id`, `type_code`, `type_name`, `iz_lock`, `remark`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1360237407576645633, 'crypto_asymmetric', '非对称加密', '1', NULL, '0', 0, 1313694379541635074, '2021-02-12 22:40:45', 1313694379541635074, '2021-02-12 22:40:45');
INSERT INTO `sys_dict_detail`(`id`, `type_id`, `type_code`, `dict_name`, `dict_value`, `iz_lock`, `sort_no`, `remark`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1360237605254193154, '1360237407576645633', 'crypto_asymmetric', 'RSA', 'RSA', '1', 1, NULL, '0', 1, 1, '2021-02-12 22:41:32', 1, '2021-02-12 22:41:58');
INSERT INTO `sys_dict_detail`(`id`, `type_id`, `type_code`, `dict_name`, `dict_value`, `iz_lock`, `sort_no`, `remark`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1360237693896613890, '1360237407576645633', 'crypto_asymmetric', 'SM2', 'SM2', '1', 2, NULL, '0', 1, 1, '2021-02-12 22:41:53', 1, '2021-02-14 00:51:54');
INSERT INTO `sys_dict_detail`(`id`, `type_id`, `type_code`, `dict_name`, `dict_value`, `iz_lock`, `sort_no`, `remark`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1360238588239667202, '1360237407576645633', 'crypto_asymmetric', 'ECIES', 'ECIES', '1', 3, NULL, '0', 0, 1, '2021-02-12 22:45:26', 1, '2021-02-12 22:45:26');

SET FOREIGN_KEY_CHECKS = 1;
