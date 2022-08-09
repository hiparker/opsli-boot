/*
 Source Server         : OPSLI快速开发平台
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : 127.0.0.1:3306
 Source Schema         : opsli-boot

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 08/08/2022 12:57:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_logs
-- ----------------------------
DROP TABLE IF EXISTS `gen_logs`;
CREATE TABLE `gen_logs` (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `table_id` bigint(19) NOT NULL COMMENT '归属表ID',
  `table_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表类型',
  `package_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '生成包名',
  `module_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '生成模块名',
  `sub_module_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成子模块名',
  `code_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代码标题',
  `code_title_brief` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代码标题简介',
  `author_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者名',
  `template_id` bigint(19) NOT NULL COMMENT '模板ID',
  `version` int(11) NOT NULL COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `table` (`id`,`table_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码生成器 - 生成日志';

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `table_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表名称',
  `old_table_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表名称',
  `table_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表类型',
  `comments` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述',
  `jdbc_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '数据库类型 { MySQL\\Oracle\\SQLServer ...}',
  `iz_sync` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '同步',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `version` int(11) NOT NULL COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `creater_table_name` (`table_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码生成器 - 表信息';

-- ----------------------------
-- Records of gen_table
-- ----------------------------
BEGIN;
INSERT INTO `gen_table` VALUES (1340630022558056449, 'test_car', 'test_car', '0', '测试汽车', 'mysql', '1', NULL, 1, 1313694379541635074, '2020-12-20 20:08:00', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table` VALUES (1356152016136482817, 'test_car_copy1', 'test_car_copy1', '0', '测试', 'mysql', '1', NULL, 0, 1313694379541635074, '2021-02-01 16:06:51', 1313694379541635074, '2021-02-01 16:06:51');
INSERT INTO `gen_table` VALUES (1359428685312028674, 'other_crypto_asymmetric', 'other_crypto_asymmetric', '0', '非对称加密表', 'mysql', '1', NULL, 12, 1313694379541635074, '2021-02-10 17:07:10', 1313694379541635074, '2021-05-26 20:39:14');
INSERT INTO `gen_table` VALUES (1397541427197468673, 'test_user', 'test_user', '0', '某系统用户', 'mysql', '0', NULL, 1, 1313694379541635074, '2021-05-26 21:13:36', 1313694379541635074, '2021-05-26 21:29:06');
INSERT INTO `gen_table` VALUES (1504350321445097473, 'sys_login_log', 'sys_login_log', '0', '登录信息表', 'mysql', '0', NULL, 1, 1465171199435362305, '2022-03-17 14:54:01', 1465171199435362305, '2022-03-17 14:58:42');
INSERT INTO `gen_table` VALUES (1520935845472522241, 'aaaa', 'aaaa', '0', 'aadsasd', 'mysql', '0', NULL, 0, 1465171199435362305, '2022-05-02 09:18:58', 1465171199435362305, '2022-05-02 09:18:58');
INSERT INTO `gen_table` VALUES (1551878342172237826, 'operation_log', 'operation_log', '0', '业务操作日志', 'mysql', '0', NULL, 1, 1, '2022-07-26 18:33:24', 1, '2022-07-26 19:06:55');
COMMIT;

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column` (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `table_id` bigint(19) NOT NULL COMMENT '归属表ID',
  `field_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `field_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段类型',
  `field_length` int(11) DEFAULT NULL COMMENT '字段长度',
  `field_precision` int(11) DEFAULT NULL COMMENT '字段精度',
  `field_comments` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段描述',
  `iz_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否主键',
  `iz_not_null` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否可为空',
  `iz_show_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否列表字段',
  `iz_show_form` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否表单显示',
  `query_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '检索类别',
  `java_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Java数据类型',
  `show_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）',
  `dict_type_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典类型',
  `sort` smallint(6) NOT NULL COMMENT '排序（升序）',
  `validate_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '验证类别',
  `version` int(11) NOT NULL COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `creater_table_column_sort` (`sort`) USING BTREE,
  KEY `creater_table_column_table_id` (`table_id`,`field_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码生成器 - 表结构\r\n';

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
BEGIN;
INSERT INTO `gen_table_column` VALUES (1340630728203567106, 1340630022558056449, 'id', 'bigint', 19, 0, '主键', '1', '1', '0', '0', NULL, 'String', NULL, NULL, 0, '', 0, 1313694379541635074, '2020-12-20 20:10:48', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table_column` VALUES (1340630728220344321, 1340630022558056449, 'car_name', 'varchar', 20, 0, '汽车名称', '0', '1', '1', '1', 'EQ', 'String', '0', NULL, 1, 'IS_GENERAL_WITH_CHINESE', 0, 1313694379541635074, '2020-12-20 20:10:48', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table_column` VALUES (1340630728232927234, 1340630022558056449, 'car_type', 'varchar', 20, 0, '汽车类型', '0', '1', '1', '1', 'LIKE', 'String', '0', NULL, 2, 'IS_GENERAL_WITH_CHINESE', 0, 1313694379541635074, '2020-12-20 20:10:48', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table_column` VALUES (1340630728241315842, 1340630022558056449, 'car_brand', 'varchar', 50, 0, '汽车品牌', '0', '0', '1', '1', 'LIKE', 'String', '0', NULL, 3, 'IS_GENERAL_WITH_CHINESE', 0, 1313694379541635074, '2020-12-20 20:10:48', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table_column` VALUES (1340630728249704449, 1340630022558056449, 'produce_data', 'date', 0, 0, '生产日期', '0', '1', '1', '1', 'RANGE', 'Date', '4', NULL, 4, '', 0, 1313694379541635074, '2020-12-20 20:10:48', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table_column` VALUES (1340630728258093057, 1340630022558056449, 'iz_usable', 'char', 1, 0, '是否启用', '0', '1', '1', '1', 'EQ', 'String', '2', 'no_yes', 5, '', 0, 1313694379541635074, '2020-12-20 20:10:48', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table_column` VALUES (1340630728270675970, 1340630022558056449, 'tenant_id', 'bigint', 19, 0, '多租户ID', '0', '0', '0', '0', NULL, 'String', NULL, NULL, 6, '', 0, 1313694379541635074, '2020-12-20 20:10:48', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table_column` VALUES (1340630728274870274, 1340630022558056449, 'deleted', 'char', 1, 0, '删除标记:0未删除，1删除', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 7, '', 0, 1313694379541635074, '2020-12-20 20:10:48', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table_column` VALUES (1340630728283258881, 1340630022558056449, 'version', 'int', 10, 0, '版本号(乐观锁)', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 8, '', 0, 1313694379541635074, '2020-12-20 20:10:48', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table_column` VALUES (1340630728291647489, 1340630022558056449, 'create_by', 'bigint', 19, 0, '创建用户', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 9, '', 0, 1313694379541635074, '2020-12-20 20:10:48', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table_column` VALUES (1340630728295841793, 1340630022558056449, 'create_time', 'datetime', 0, 0, '创建日期', '0', '1', '0', '0', NULL, 'Date', NULL, NULL, 10, '', 0, 1313694379541635074, '2020-12-20 20:10:48', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table_column` VALUES (1340630728304230401, 1340630022558056449, 'update_by', 'bigint', 19, 0, '修改用户', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 11, '', 0, 1313694379541635074, '2020-12-20 20:10:48', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table_column` VALUES (1340630728308424706, 1340630022558056449, 'update_time', 'datetime', 0, 0, '修改日期', '0', '1', '0', '0', NULL, 'Date', NULL, NULL, 12, '', 0, 1313694379541635074, '2020-12-20 20:10:48', 1313694379541635074, '2020-12-20 20:10:48');
INSERT INTO `gen_table_column` VALUES (1356152016623022081, 1356152016136482817, 'id', 'bigint', 19, 0, '主键', '1', '1', NULL, NULL, NULL, 'String', NULL, NULL, 0, NULL, 0, 1313694379541635074, '2021-02-01 16:06:52', 1313694379541635074, '2021-02-01 16:06:52');
INSERT INTO `gen_table_column` VALUES (1356152016715296769, 1356152016136482817, 'car_name', 'varchar', 20, NULL, '汽车名称', '0', '1', NULL, NULL, NULL, 'String', NULL, NULL, 1, NULL, 0, 1313694379541635074, '2021-02-01 16:06:52', 1313694379541635074, '2021-02-01 16:06:52');
INSERT INTO `gen_table_column` VALUES (1356152016753045506, 1356152016136482817, 'car_type', 'varchar', 20, NULL, '汽车类型', '0', '1', NULL, NULL, NULL, 'String', NULL, NULL, 2, NULL, 0, 1313694379541635074, '2021-02-01 16:06:52', 1313694379541635074, '2021-02-01 16:06:52');
INSERT INTO `gen_table_column` VALUES (1356152016794988546, 1356152016136482817, 'car_brand', 'varchar', 50, NULL, '汽车品牌', '0', '0', NULL, NULL, NULL, 'String', NULL, NULL, 3, NULL, 0, 1313694379541635074, '2021-02-01 16:06:52', 1313694379541635074, '2021-02-01 16:06:52');
INSERT INTO `gen_table_column` VALUES (1356152016824348673, 1356152016136482817, 'produce_data', 'date', NULL, NULL, '生产日期', '0', '1', NULL, NULL, NULL, 'String', NULL, NULL, 4, NULL, 0, 1313694379541635074, '2021-02-01 16:06:52', 1313694379541635074, '2021-02-01 16:06:52');
INSERT INTO `gen_table_column` VALUES (1356152016874680321, 1356152016136482817, 'iz_usable', 'char', 1, NULL, '是否启用', '0', '1', NULL, NULL, NULL, 'String', NULL, NULL, 5, NULL, 0, 1313694379541635074, '2021-02-01 16:06:52', 1313694379541635074, '2021-02-01 16:06:52');
INSERT INTO `gen_table_column` VALUES (1356152016916623361, 1356152016136482817, 'tenant_id', 'bigint', 19, 0, '多租户ID', '0', '0', NULL, NULL, NULL, 'String', NULL, NULL, 6, NULL, 0, 1313694379541635074, '2021-02-01 16:06:52', 1313694379541635074, '2021-02-01 16:06:52');
INSERT INTO `gen_table_column` VALUES (1356152016958566401, 1356152016136482817, 'deleted', 'char', 1, NULL, '删除标记:0未删除，1删除', '0', '1', NULL, NULL, NULL, 'String', NULL, NULL, 7, NULL, 0, 1313694379541635074, '2021-02-01 16:06:52', 1313694379541635074, '2021-02-01 16:06:52');
INSERT INTO `gen_table_column` VALUES (1356152016992120833, 1356152016136482817, 'version', 'int', 10, 0, '版本号(乐观锁)', '0', '1', NULL, NULL, NULL, 'String', NULL, NULL, 8, NULL, 0, 1313694379541635074, '2021-02-01 16:06:52', 1313694379541635074, '2021-02-01 16:06:52');
INSERT INTO `gen_table_column` VALUES (1356152017050841090, 1356152016136482817, 'create_by', 'bigint', 19, 0, '创建用户', '0', '1', NULL, NULL, NULL, 'String', NULL, NULL, 9, NULL, 0, 1313694379541635074, '2021-02-01 16:06:52', 1313694379541635074, '2021-02-01 16:06:52');
INSERT INTO `gen_table_column` VALUES (1356152017096978433, 1356152016136482817, 'create_time', 'datetime', NULL, NULL, '创建日期', '0', '1', NULL, NULL, NULL, 'String', NULL, NULL, 10, NULL, 0, 1313694379541635074, '2021-02-01 16:06:52', 1313694379541635074, '2021-02-01 16:06:52');
INSERT INTO `gen_table_column` VALUES (1356152017138921473, 1356152016136482817, 'update_by', 'bigint', 19, 0, '修改用户', '0', '1', NULL, NULL, NULL, 'String', NULL, NULL, 11, NULL, 0, 1313694379541635074, '2021-02-01 16:06:52', 1313694379541635074, '2021-02-01 16:06:52');
INSERT INTO `gen_table_column` VALUES (1356152017180864513, 1356152016136482817, 'update_time', 'datetime', NULL, NULL, '修改日期', '0', '1', NULL, NULL, NULL, 'String', NULL, NULL, 12, NULL, 0, 1313694379541635074, '2021-02-01 16:06:52', 1313694379541635074, '2021-02-01 16:06:52');
INSERT INTO `gen_table_column` VALUES (1397532779008155650, 1359428685312028674, 'id', 'bigint', 19, 0, '唯一主键', '1', '1', '0', '0', NULL, 'String', NULL, NULL, 0, '', 0, 1313694379541635074, '2021-05-26 20:39:14', 1313694379541635074, '2021-05-26 20:39:14');
INSERT INTO `gen_table_column` VALUES (1397532779071070210, 1359428685312028674, 'public_key', 'blob', 2000, 0, '公钥', '0', '1', '1', '1', '', 'Byte[]', '1', NULL, 1, '', 0, 1313694379541635074, '2021-05-26 20:39:14', 1313694379541635074, '2021-05-26 20:39:14');
INSERT INTO `gen_table_column` VALUES (1397532779104624641, 1359428685312028674, 'private_key', 'varchar', 2000, 0, '私钥', '0', '1', '1', '1', '', 'String', '1', NULL, 2, '', 0, 1313694379541635074, '2021-05-26 20:39:14', 1313694379541635074, '2021-05-26 20:39:14');
INSERT INTO `gen_table_column` VALUES (1397532779121401857, 1359428685312028674, 'version', 'int', 10, 0, '版本（乐观锁）', '0', '1', '0', '0', NULL, 'Integer', NULL, NULL, 3, '', 0, 1313694379541635074, '2021-05-26 20:39:14', 1313694379541635074, '2021-05-26 20:39:14');
INSERT INTO `gen_table_column` VALUES (1397532779154956290, 1359428685312028674, 'crypto_type', 'varchar', 100, 0, '加解密类别', '0', '1', '1', '1', 'EQ', 'Integer', '0', NULL, 4, 'IS_INTEGER,IS_LETTER,IS_IP,IS_UPPER_CASE,IS_LOWER_CASE', 0, 1313694379541635074, '2021-05-26 20:39:14', 1313694379541635074, '2021-05-26 20:39:14');
INSERT INTO `gen_table_column` VALUES (1397532779171733506, 1359428685312028674, 'create_by', 'bigint', 19, 0, '创建用户', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 5, '', 0, 1313694379541635074, '2021-05-26 20:39:14', 1313694379541635074, '2021-05-26 20:39:14');
INSERT INTO `gen_table_column` VALUES (1397532779188510722, 1359428685312028674, 'create_time', 'datetime', 0, 0, '创建日期', '0', '1', '0', '0', NULL, 'Date', NULL, NULL, 6, '', 0, 1313694379541635074, '2021-05-26 20:39:14', 1313694379541635074, '2021-05-26 20:39:14');
INSERT INTO `gen_table_column` VALUES (1397532779205287937, 1359428685312028674, 'update_by', 'bigint', 19, 0, '修改用户', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 7, '', 0, 1313694379541635074, '2021-05-26 20:39:14', 1313694379541635074, '2021-05-26 20:39:14');
INSERT INTO `gen_table_column` VALUES (1397532779230453761, 1359428685312028674, 'update_time', 'datetime', 0, 0, '修改日期', '0', '1', '0', '0', NULL, 'Date', NULL, NULL, 8, '', 0, 1313694379541635074, '2021-05-26 20:39:14', 1313694379541635074, '2021-05-26 20:39:14');
INSERT INTO `gen_table_column` VALUES (1397545330655850497, 1397541427197468673, 'id', 'bigint', 19, 0, '主键', '1', '1', '0', '0', NULL, 'String', NULL, NULL, 0, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1397545330727153666, 1397541427197468673, 'name', 'varchar', 50, 0, '名称', '0', '0', '0', '0', NULL, 'String', NULL, NULL, 1, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1397545330752319489, 1397541427197468673, 'money', 'double', 6, 2, '金钱', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 2, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1397545330769096706, 1397541427197468673, 'age', 'smallint', 5, 0, '年龄', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 3, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1397545330785873921, 1397541427197468673, 'birth', 'date', 0, 0, '生日', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 4, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1397545330811039746, 1397541427197468673, 'iz_usable', 'char', 1, 0, '是否启用', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 5, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1397545330827816962, 1397541427197468673, 'tenant_id', 'bigint', 19, 0, '多租户ID', '0', '0', '0', '0', NULL, 'String', NULL, NULL, 6, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1397545330844594178, 1397541427197468673, 'deleted', 'char', 1, 0, '删除标记:0未删除，1删除', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 7, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1397545330861371393, 1397541427197468673, 'version', 'int', 10, 0, '版本号(乐观锁)', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 8, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1397545330869760002, 1397541427197468673, 'create_by', 'bigint', 19, 0, '创建用户', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 9, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1397545330886537218, 1397541427197468673, 'create_time', 'datetime', 0, 0, '创建日期', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 10, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1397545330903314433, 1397541427197468673, 'update_by', 'bigint', 19, 0, '修改用户', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 11, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1397545330920091649, 1397541427197468673, 'update_time', 'datetime', 0, 0, '修改日期', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 12, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1397545330928480257, 1397541427197468673, 'ts', 'timestamp', 0, 0, '时间戳', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 13, '', 0, 1313694379541635074, '2021-05-26 21:29:07', 1313694379541635074, '2021-05-26 21:29:07');
INSERT INTO `gen_table_column` VALUES (1504351504435957761, 1504350321445097473, 'id', 'bigint', 19, 0, '唯一主键', '1', '1', '0', '0', NULL, 'String', NULL, NULL, 0, '', 0, 1465171199435362305, '2022-03-17 14:58:43', 1465171199435362305, '2022-03-17 14:58:43');
INSERT INTO `gen_table_column` VALUES (1504351504498872322, 1504350321445097473, 'org_ids', 'varchar', 500, 0, '父级主键集合', '0', '0', '0', '0', NULL, 'String', NULL, NULL, 1, '', 0, 1465171199435362305, '2022-03-17 14:58:43', 1465171199435362305, '2022-03-17 14:58:43');
INSERT INTO `gen_table_column` VALUES (1504351504565981185, 1504350321445097473, 'type', 'char', 1, 0, '日志类型', '0', '0', '0', '0', NULL, 'String', NULL, NULL, 2, '', 0, 1465171199435362305, '2022-03-17 14:58:43', 1465171199435362305, '2022-03-17 14:58:43');
INSERT INTO `gen_table_column` VALUES (1504351504633090050, 1504350321445097473, 'remote_addr', 'varchar', 255, 0, '操作IP地址', '0', '0', '1', '0', '', 'String', '0', NULL, 4, '', 0, 1465171199435362305, '2022-03-17 14:58:43', 1465171199435362305, '2022-03-17 14:58:43');
INSERT INTO `gen_table_column` VALUES (1504351504691810306, 1504350321445097473, 'user_agent', 'varchar', 255, 0, '用户代理', '0', '0', '1', '0', '', 'String', '0', NULL, 5, '', 0, 1465171199435362305, '2022-03-17 14:58:43', 1465171199435362305, '2022-03-17 14:58:43');
INSERT INTO `gen_table_column` VALUES (1504351504821833730, 1504350321445097473, 'username', 'varchar', 32, 0, '登录账户', '0', '1', '1', '0', 'LIKE', 'String', '0', NULL, 6, '', 0, 1465171199435362305, '2022-03-17 14:58:43', 1465171199435362305, '2022-03-17 14:58:43');
INSERT INTO `gen_table_column` VALUES (1504351504884748290, 1504350321445097473, 'tenant_id', 'bigint', 19, 0, '多租户ID', '0', '0', '0', '0', NULL, 'String', NULL, NULL, 7, '', 0, 1465171199435362305, '2022-03-17 14:58:43', 1465171199435362305, '2022-03-17 14:58:43');
INSERT INTO `gen_table_column` VALUES (1504351505027354625, 1504350321445097473, 'version', 'int', 10, 0, '版本', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 8, '', 0, 1465171199435362305, '2022-03-17 14:58:43', 1465171199435362305, '2022-03-17 14:58:43');
INSERT INTO `gen_table_column` VALUES (1504351505073491969, 1504350321445097473, 'create_by', 'bigint', 19, 0, '创建者', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 9, '', 0, 1465171199435362305, '2022-03-17 14:58:43', 1465171199435362305, '2022-03-17 14:58:43');
INSERT INTO `gen_table_column` VALUES (1504351505203515394, 1504350321445097473, 'create_time', 'datetime', 0, 0, '创建时间', '0', '1', '1', '0', '', 'String', '0', NULL, 10, '', 0, 1465171199435362305, '2022-03-17 14:58:43', 1465171199435362305, '2022-03-17 14:58:43');
INSERT INTO `gen_table_column` VALUES (1504351505270624257, 1504350321445097473, 'update_by', 'bigint', 19, 0, '修改人', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 11, '', 0, 1465171199435362305, '2022-03-17 14:58:43', 1465171199435362305, '2022-03-17 14:58:43');
INSERT INTO `gen_table_column` VALUES (1504351505337733122, 1504350321445097473, 'update_time', 'datetime', 0, 0, '修改时间', '0', '1', '0', '0', NULL, 'String', NULL, NULL, 12, '', 0, 1465171199435362305, '2022-03-17 14:58:43', 1465171199435362305, '2022-03-17 14:58:43');
INSERT INTO `gen_table_column` VALUES (1520935845673848833, 1520935845472522241, 'id', 'integer', 0, 0, 'qqqq', '0', '0', '1', '1', 'EQ', 'String', '0', '', 0, '', 0, 1465171199435362305, '2022-05-02 09:18:58', 1465171199435362305, '2022-05-02 09:18:58');
INSERT INTO `gen_table_column` VALUES (1551886778654023681, 1551878342172237826, 'id', 'bigint', 19, 0, '日志ID', '1', '1', '0', '0', NULL, 'String', NULL, NULL, 0, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886778691772417, 1551878342172237826, 'level', 'varchar', 8, 0, '日志等级', '0', '0', '1', '1', 'EQ', 'String', '2', 'level', 1, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886778729521153, 1551878342172237826, 'module_id', 'varchar', 20, 0, '被操作的系统模块', '0', '0', '1', '1', 'EQ', 'String', '2', 'model_type', 2, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886778763075585, 1551878342172237826, 'method', 'varchar', 100, 0, '方法名', '0', '0', '0', '1', '', 'String', '0', NULL, 3, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886778805018625, 1551878342172237826, 'args', 'text', 20000, 0, '参数', '0', '0', '0', '1', '', 'String', '0', NULL, 4, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886778842767362, 1551878342172237826, 'user_id', 'bigint', 19, 0, '操作人id', '0', '0', '0', '0', '', 'String', '', NULL, 5, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886778884710401, 1551878342172237826, 'user_name', 'varchar', 32, 0, '操作账号', '0', '0', '1', '1', '', 'String', '0', NULL, 6, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886778918264833, 1551878342172237826, 'real_name', 'varchar', 50, 0, '操作名称', '0', '0', '1', '1', '', 'String', '0', NULL, 7, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886778947624961, 1551878342172237826, 'description', 'varchar', 255, 0, '日志描述', '0', '0', '1', '1', '', 'String', '0', NULL, 8, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886778976985089, 1551878342172237826, 'operation_type', 'varchar', 20, 0, '操作类型', '0', '0', '1', '1', 'EQ', 'String', '2', 'operation_type', 9, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886779014733826, 1551878342172237826, 'run_time', 'bigint', 19, 0, '方法运行时间', '0', '0', '1', '0', '', 'String', '0', NULL, 10, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886779052482562, 1551878342172237826, 'return_value', 'text', 20000, 0, '方法返回值', '0', '0', '0', '1', '', 'String', '0', NULL, 11, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886779094425602, 1551878342172237826, 'log_type', 'varchar', 8, 0, '日志请求类型', '0', '0', '0', '1', '', 'String', '2', 'log_type', 12, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886779132174337, 1551878342172237826, 'version', 'int', 10, 0, '版本（乐观锁）', '0', '0', '0', '0', NULL, 'String', NULL, NULL, 13, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886779157340161, 1551878342172237826, 'create_by', 'bigint', 19, 0, '创建者', '0', '0', '0', '0', NULL, 'String', NULL, NULL, 14, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886779182505985, 1551878342172237826, 'create_time', 'datetime', 0, 0, '创建时间', '0', '0', '0', '0', NULL, 'String', NULL, NULL, 15, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886779220254721, 1551878342172237826, 'update_by', 'bigint', 19, 0, '修改人', '0', '0', '0', '0', NULL, 'String', NULL, NULL, 16, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
INSERT INTO `gen_table_column` VALUES (1551886779270586369, 1551878342172237826, 'update_time', 'datetime', 0, 0, '修改时间', '0', '0', '0', '0', NULL, 'String', NULL, NULL, 17, '', 0, 1, '2022-07-26 19:06:55', 1, '2022-07-26 19:06:55');
COMMIT;

-- ----------------------------
-- Table structure for gen_template
-- ----------------------------
DROP TABLE IF EXISTS `gen_template`;
CREATE TABLE `gen_template` (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `temp_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `table_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表类型',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `version` int(10) NOT NULL COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码生成器 - 模板';

-- ----------------------------
-- Records of gen_template
-- ----------------------------
BEGIN;
INSERT INTO `gen_template` VALUES (1398253704724828162, 'Form表单', '0', '默认Form表单', 48, 1313694379541635074, '2021-05-28 20:23:56', 1, '2022-08-07 00:10:00');
COMMIT;

-- ----------------------------
-- Table structure for gen_template_detail
-- ----------------------------
DROP TABLE IF EXISTS `gen_template_detail`;
CREATE TABLE `gen_template_detail` (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `parent_id` bigint(19) NOT NULL COMMENT '父级ID',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型 0 后端  / 1 前端',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路径',
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `file_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件内容',
  `ignore_file_name` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '忽略文件名',
  `version` int(11) NOT NULL COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `creater_table_name` (`path`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码生成器 - 表信息';

-- ----------------------------
-- Records of gen_template_detail
-- ----------------------------
BEGIN;
INSERT INTO `gen_template_detail` VALUES (1555949317360721921, 1398253704724828162, '0', '${packageName}/${moduleName}/${subModuleName}/entity', '${model.tableHumpName}Entity.java', '#if(data.subModuleName != null && data.subModuleName != \"\")\npackage #(data.packageName+\".\"+data.moduleName+\".\"+data.subModuleName).entity;\n#else\npackage #(data.packageName+\".\"+data.moduleName).entity;\n#end\n\n#for(pkg : data.model.entityPkgList)\nimport #(pkg);\n#end\nimport com.baomidou.mybatisplus.annotation.FieldStrategy;\nimport com.baomidou.mybatisplus.annotation.TableField;\nimport com.baomidou.mybatisplus.annotation.TableLogic;\nimport lombok.Data;\nimport lombok.EqualsAndHashCode;\nimport org.opsli.core.base.entity.BaseEntity;\n\n/**\n * #(data.codeTitle) Entity\n *\n * @author #(data.authorName)\n * @date #(currTime)\n */\n@Data\n@EqualsAndHashCode(callSuper = false)\npublic class #(data.model.tableHumpName) extends BaseEntity {\n\n\n    #for(column : data.model.columnList)\n    ### 不等于 删除字段 和 不等于 租户字段放入上边\n    #if(column.fieldHumpName != \"deleted\" && column.fieldHumpName != \"tenantId\")\n    /** #(column.fieldComments) */\n    #if(!column.izNotNull)\n    @TableField(updateStrategy = FieldStrategy.IGNORED)\n    #end\n    private #(column.javaType) #(column.fieldHumpName);\n\n    #end\n    #end\n\n    // ========================================\n\n    ### 专门处理 删除字段 和 租户字段\n    #for(column : data.model.columnList)\n    #if(column.fieldHumpName == \"deleted\")\n    /** 逻辑删除字段 */\n    @TableLogic\n    private Integer deleted;\n    #else if(column.fieldHumpName == \"tenantId\")\n    /** 多租户字段 */\n    private String tenantId;\n    #end\n\n    #end\n\n}', '1', 0, 1, '2022-08-07 00:10:00', 1, '2022-08-07 00:10:00');
INSERT INTO `gen_template_detail` VALUES (1555949317415247873, 1398253704724828162, '0', '${packageName}/${moduleName}/${subModuleName}/mapper', '${model.tableHumpName}Mapper.java', '#if(data.subModuleName != null && data.subModuleName != \"\")\npackage #(data.packageName+\".\"+data.moduleName+\".\"+data.subModuleName).mapper;\n#else\npackage #(data.packageName+\".\"+data.moduleName).mapper;\n#end\n\nimport com.baomidou.mybatisplus.core.mapper.BaseMapper;\nimport org.apache.ibatis.annotations.Mapper;\nimport org.apache.ibatis.annotations.Param;\n#if(data.subModuleName != null && data.subModuleName != \"\")\nimport #(data.packageName+\".\"+data.moduleName+\".\"+data.subModuleName).entity.#(data.model.tableHumpName);\n#else\nimport #(data.packageName+\".\"+data.moduleName).entity.#(data.model.tableHumpName);\n#end\n\n/**\n * #(data.codeTitle) Mapper\n *\n * @author #(data.authorName)\n * @date #(currTime)\n */\n@Mapper\npublic interface #(data.model.tableHumpName)Mapper extends BaseMapper<#(data.model.tableHumpName)> {\n\n}', '0', 0, 1, '2022-08-07 00:10:00', 1, '2022-08-07 00:10:00');
INSERT INTO `gen_template_detail` VALUES (1555949317452996610, 1398253704724828162, '0', '${packageName}/${moduleName}/${subModuleName}/mapper/xml', '${model.tableHumpName}Mapper.xml', '<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n#if(data.subModuleName != null && data.subModuleName != \"\")\n<mapper namespace=\"#(data.packageName+\'.\'+data.moduleName+\'.\'+data.subModuleName).mapper.#(data.model.tableHumpName)Mapper\">\n#else\n<mapper namespace=\"#(data.packageName+\'.\'+data.moduleName).mapper.#(data.model.tableHumpName)Mapper\">\n#end\n\n\n</mapper>', '0', 0, 1, '2022-08-07 00:10:00', 1, '2022-08-07 00:10:00');
INSERT INTO `gen_template_detail` VALUES (1555949317494939650, 1398253704724828162, '0', 'org/opsli/api/wrapper/${moduleName}/${subModuleName}', '${model.tableHumpName}Model.java', '#if(data.subModuleName != null && data.subModuleName != \"\")\npackage #(apiPath).wrapper.#(data.moduleName+\".\"+data.subModuleName);\n#else\npackage #(apiPath).wrapper.#(data.moduleName);\n#end\n\n#for(pkg : data.model.entityPkgList)\nimport #(pkg);\n#end\nimport com.alibaba.excel.annotation.ExcelProperty;\nimport io.swagger.annotations.ApiModelProperty;\nimport lombok.Data;\nimport lombok.EqualsAndHashCode;\nimport #(apiPath).base.warpper.ApiWrapper;\nimport org.opsli.common.annotation.validator.Validator;\nimport org.opsli.common.annotation.validator.ValidatorLenMax;\nimport org.opsli.common.annotation.validator.ValidatorLenMin;\nimport org.opsli.common.enums.ValidatorType;\nimport org.opsli.plugins.excel.annotation.ExcelInfo;\nimport com.fasterxml.jackson.annotation.JsonFormat;\nimport org.springframework.format.annotation.DateTimeFormat;\n\n/**\n* #(data.codeTitle) Model\n*\n* @author #(data.authorName)\n* @date #(currTime)\n*/\n@Data\n@EqualsAndHashCode(callSuper = false)\npublic class #(data.model.tableHumpName)Model extends ApiWrapper {\n\n    #for(column : data.model.columnList)\n    ### 不等于 删除字段 和 不等于 租户字段放入上边\n    #if(column.fieldHumpName != \"deleted\" && column.fieldHumpName != \"tenantId\")\n    /** #(column.fieldComments) */\n    @ApiModelProperty(value = \"#(column.fieldComments)\")\n    @ExcelProperty(value = \"#(column.fieldComments)\", order = #(column.sort))\n    #if(column.dictTypeCode != null && column.dictTypeCode != \"\")\n    @ExcelInfo( dictType = \"#(column.dictTypeCode)\" )\n    #else\n    @ExcelInfo\n    #end\n    #if(column.validateTypeAndCommaList != null && column.validateTypeAndCommaList.size() > 0)\n    @Validator({\n        #for(typeAndComma : column.validateTypeAndCommaList)\n        ValidatorType.#(typeAndComma)\n        #end\n    })\n    #end\n    #if(column.fieldLength != null && column.fieldLength > 0)\n    #if(column.fieldPrecision != null && column.fieldPrecision > 0)\n    @ValidatorLenMax(#(column.fieldLength+column.fieldPrecision))\n    #else\n    @ValidatorLenMax(#(column.fieldLength))\n    #end\n    #end\n    ### 日期处理\n    #if(column.javaType == \"Date\")\n    #if(column.showType == \"4\")\n    @JsonFormat(timezone = \"GMT+8\", pattern = \"yyyy-MM-dd\")\n    @DateTimeFormat(pattern = \"yyyy-MM-dd\")\n    #else\n    @JsonFormat(timezone = \"GMT+8\", pattern = \"yyyy-MM-dd HH:mm:ss\")\n    @DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n    #end\n    #end\n    private #(column.javaType) #(column.fieldHumpName);\n\n    #end\n    #end\n\n\n}', '0', 0, 1, '2022-08-07 00:10:00', 1, '2022-08-07 00:10:00');
INSERT INTO `gen_template_detail` VALUES (1555949317528494081, 1398253704724828162, '0', 'org/opsli/api/web/${moduleName}/${subModuleName}', '${model.tableHumpName}RestApi.java', '#if(data.subModuleName != null && data.subModuleName != \"\")\npackage #(apiPath).web.#(data.moduleName+\".\"+data.subModuleName);\n#else\npackage #(apiPath).web.#(data.moduleName);\n#end\n\nimport org.opsli.api.base.result.ResultWrapper;\nimport org.springframework.web.bind.annotation.*;\nimport org.springframework.web.multipart.MultipartHttpServletRequest;\nimport javax.servlet.http.HttpServletRequest;\nimport javax.servlet.http.HttpServletResponse;\n\n#if(data.subModuleName != null && data.subModuleName != \"\")\nimport #(apiPath).wrapper.#(data.moduleName+\".\"+data.subModuleName).#(data.model.tableHumpName)Model;\n#else\nimport #(apiPath).wrapper.#(data.moduleName).#(data.model.tableHumpName)Model;\n#end\n\n\n/**\n * #(data.codeTitle) Api\n *\n * 对外 API 直接 暴露 @GetMapping 或者 @PostMapping\n * 对内也推荐 单机版 不需要设置 Mapping 但是调用方法得从Controller写起\n *\n * 这样写法虽然比较绕，但是当单体项目想要改造微服务架构时 时非常容易的\n *\n * @author #(data.authorName)\n * @date #(currTime)\n */\npublic interface #(data.model.tableHumpName)RestApi {\n\n    /** 标题 */\n    String TITLE = \"#(data.codeTitle)\";\n    /** 子标题 */\n    String SUB_TITLE = \"#(data.codeTitleBrief)\";\n\n    /**\n    * #(data.codeTitle) 查一条\n    * @param model 模型\n    * @return ResultWrapper\n    */\n    @GetMapping(\"/get\")\n    ResultWrapper<#(data.model.tableHumpName)Model> get(#(data.model.tableHumpName)Model model);\n\n    /**\n    * #(data.codeTitle) 查询分页\n    * @param pageNo 当前页\n    * @param pageSize 每页条数\n    * @param request request\n    * @return ResultWrapper\n    */\n    @GetMapping(\"/findPage\")\n    ResultWrapper<?> findPage(\n        @RequestParam(name = \"pageNo\", defaultValue = \"1\") Integer pageNo,\n        @RequestParam(name = \"pageSize\", defaultValue = \"10\") Integer pageSize,\n        HttpServletRequest request\n    );\n\n    /**\n    * #(data.codeTitle) 新增\n    * @param model 模型\n    * @return ResultWrapper\n    */\n    @PostMapping(\"/insert\")\n    ResultWrapper<?> insert(@RequestBody #(data.model.tableHumpName)Model model);\n\n    /**\n    * #(data.codeTitle) 修改\n    * @param model 模型\n    * @return ResultWrapper\n    */\n    @PostMapping(\"/update\")\n    ResultWrapper<?> update(@RequestBody #(data.model.tableHumpName)Model model);\n\n    /**\n    * #(data.codeTitle) 删除\n    * @param id ID\n    * @return ResultWrapper\n    */\n    @PostMapping(\"/del\")\n    ResultWrapper<?> del(String id);\n\n    /**\n    * #(data.codeTitle) 批量删除\n    * @param ids ID 数组\n    * @return ResultWrapper\n    */\n    @PostMapping(\"/delAll\")\n    ResultWrapper<?> delAll(String ids);\n\n    /**\n     * #(data.codeTitle) Excel 导出认证\n     *\n     * @param type 类型\n     * @param request request\n     */\n    @GetMapping(\"/excel/auth/{type}\")\n    ResultWrapper<String> exportExcelAuth(\n            @PathVariable(\"type\") String type,\n            HttpServletRequest request);\n\n    /**\n     * #(data.codeTitle) Excel 导出\n     *\n     * @param certificate 凭证\n     * @param response response\n     */\n    @GetMapping(\"/excel/export/{certificate}\")\n    void exportExcel(\n            @PathVariable(\"certificate\") String certificate,\n            HttpServletResponse response);\n\n    /**\n    * #(data.codeTitle) Excel 导入\n    * @param request 文件流 request\n    * @return ResultWrapper\n    */\n    @PostMapping(\"/importExcel\")\n    ResultWrapper<?> importExcel(MultipartHttpServletRequest request);\n\n}', '0', 0, 1, '2022-08-07 00:10:00', 1, '2022-08-07 00:10:00');
INSERT INTO `gen_template_detail` VALUES (1555949317570437121, 1398253704724828162, '0', '${packageName}/${moduleName}/${subModuleName}/web', '${model.tableHumpName}RestController.java', '#if(data.subModuleName != null && data.subModuleName != \"\")\npackage #(data.packageName+\".\"+data.moduleName+\".\"+data.subModuleName).web;\n#else\npackage #(data.packageName+\".\"+data.moduleName).web;\n#end\n\nimport java.util.Optional;\nimport cn.hutool.core.convert.Convert;\nimport io.swagger.annotations.Api;\nimport io.swagger.annotations.ApiOperation;\nimport lombok.extern.slf4j.Slf4j;\nimport #(apiPath).base.result.ResultWrapper;\nimport org.opsli.common.annotation.ApiRestController;\nimport org.opsli.core.base.controller.BaseRestController;\nimport org.opsli.core.persistence.Page;\nimport org.opsli.core.persistence.querybuilder.QueryBuilder;\nimport org.opsli.core.persistence.querybuilder.WebQueryBuilder;\nimport org.springframework.web.multipart.MultipartHttpServletRequest;\nimport javax.servlet.http.HttpServletRequest;\nimport javax.servlet.http.HttpServletResponse;\nimport org.springframework.security.access.prepost.PreAuthorize;\nimport org.opsli.core.log.enums.*;\nimport org.opsli.core.log.annotation.OperateLogger;\n\n#if(data.subModuleName != null && data.subModuleName != \"\")\nimport #(data.packageName+\".\"+data.moduleName+\".\"+data.subModuleName).entity.#(data.model.tableHumpName);\nimport #(apiPath).wrapper.#(data.moduleName+\".\"+data.subModuleName).#(data.model.tableHumpName)Model;\nimport #(data.packageName+\".\"+data.moduleName+\".\"+data.subModuleName).service.I#(data.model.tableHumpName)Service;\nimport #(apiPath).web.#(data.moduleName+\".\"+data.subModuleName).#(data.model.tableHumpName)RestApi;\n#else\nimport #(data.packageName+\".\"+data.moduleName).entity.#(data.model.tableHumpName);\nimport #(apiPath).wrapper.#(data.moduleName).#(data.model.tableHumpName)Model;\nimport #(data.packageName+\".\"+data.moduleName).service.I#(data.model.tableHumpName)Service;\nimport #(apiPath).web.#(data.moduleName).#(data.model.tableHumpName)RestApi;\n#end\n\n/**\n * #(data.codeTitle) Controller\n *\n * @author #(data.authorName)\n * @date #(currTime)\n */\n@Api(tags = #(data.model.tableHumpName)RestApi.TITLE)\n@Slf4j\n#if(data.subModuleName != null && data.subModuleName != \"\")\n@ApiRestController(\"/{ver}/#(data.moduleName)/#(data.subModuleName)\")\n#else\n@ApiRestController(\"/{ver}/#(data.moduleName)\")\n#end\npublic class #(data.model.tableHumpName)RestController extends BaseRestController<#(data.model.tableHumpName), #(data.model.tableHumpName)Model, I#(data.model.tableHumpName)Service>\n    implements #(data.model.tableHumpName)RestApi {\n\n\n    /**\n     * #(data.codeTitleBrief) 查一条\n     * @param model 模型\n     * @return ResultWrapper\n     */\n    @ApiOperation(value = \"获得单条#(data.codeTitleBrief)\", notes = \"获得单条#(data.codeTitleBrief) - ID\")\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_select\')\")\n    #else\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_select\')\")\n    #end\n    @Override\n    public ResultWrapper<#(data.model.tableHumpName)Model> get(#(data.model.tableHumpName)Model model) {\n        // 如果系统内部调用 则直接查数据库\n        if(model != null && model.getIzApi() != null && model.getIzApi()){\n            model = IService.get(model);\n        }\n        return ResultWrapper.getSuccessResultWrapper(model);\n    }\n\n    /**\n     * #(data.codeTitleBrief) 查询分页\n     * @param pageNo 当前页\n     * @param pageSize 每页条数\n     * @param request request\n     * @return ResultWrapper\n     */\n    @ApiOperation(value = \"获得分页数据\", notes = \"获得分页数据 - 查询构造器\")\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_select\')\")\n    #else\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_select\')\")\n    #end\n    @Override\n    public ResultWrapper<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {\n\n        QueryBuilder<#(data.model.tableHumpName)> queryBuilder = new WebQueryBuilder<>(IService.getEntityClass(), request.getParameterMap());\n        Page<#(data.model.tableHumpName), #(data.model.tableHumpName)Model> page = new Page<>(pageNo, pageSize);\n        page.setQueryWrapper(queryBuilder.build());\n        page = IService.findPage(page);\n\n        return ResultWrapper.getSuccessResultWrapper(page.getPageData());\n    }\n\n    /**\n     * #(data.codeTitleBrief) 新增\n     * @param model 模型\n     * @return ResultWrapper\n     */\n    @ApiOperation(value = \"新增#(data.codeTitleBrief)数据\", notes = \"新增#(data.codeTitleBrief)数据\")\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_insert\')\")\n    #else\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_insert\')\")\n    #end\n    @OperateLogger(description = \"新增#(data.codeTitleBrief)数据\",\n            module = ModuleEnum.MODULE_UNKNOWN, operationType = OperationTypeEnum.INSERT, db = true)\n    @Override\n    public ResultWrapper<?> insert(#(data.model.tableHumpName)Model model) {\n        // 调用新增方法\n        IService.insert(model);\n        return ResultWrapper.getSuccessResultWrapperByMsg(\"新增#(data.codeTitleBrief)成功\");\n    }\n\n    /**\n     * #(data.codeTitleBrief) 修改\n     * @param model 模型\n     * @return ResultWrapper\n     */\n    @ApiOperation(value = \"修改#(data.codeTitleBrief)数据\", notes = \"修改#(data.codeTitleBrief)数据\")\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_update\')\")\n    #else\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_update\')\")\n    #end    \n    @OperateLogger(description = \"修改#(data.codeTitleBrief)数据\",\n            module = ModuleEnum.MODULE_UNKNOWN, operationType = OperationTypeEnum.UPDATE, db = true)\n    @Override\n    public ResultWrapper<?> update(#(data.model.tableHumpName)Model model) {\n        // 调用修改方法\n        IService.update(model);\n        return ResultWrapper.getSuccessResultWrapperByMsg(\"修改#(data.codeTitleBrief)成功\");\n    }\n\n\n    /**\n     * #(data.codeTitleBrief) 删除\n     * @param id ID\n     * @return ResultVo\n     */\n    @ApiOperation(value = \"删除#(data.codeTitleBrief)数据\", notes = \"删除#(data.codeTitleBrief)数据\")\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_delete\')\")\n    #else\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_delete\')\")\n    #end   \n    @OperateLogger(description = \"删除#(data.codeTitleBrief)数据\",\n            module = ModuleEnum.MODULE_UNKNOWN, operationType = OperationTypeEnum.DELETE, db = true)\n    @Override\n    public ResultWrapper<?> del(String id){\n        IService.delete(id);\n        return ResultWrapper.getSuccessResultWrapperByMsg(\"删除#(data.codeTitleBrief)成功\");\n    }\n\n    /**\n     * #(data.codeTitleBrief) 批量删除\n     * @param ids ID 数组\n     * @return ResultVo\n     */\n    @ApiOperation(value = \"批量删除#(data.codeTitleBrief)数据\", notes = \"批量删除#(data.codeTitleBrief)数据\")\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_delete\')\")\n    #else\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_delete\')\")\n    #end   \n    @OperateLogger(description = \"批量删除#(data.codeTitleBrief)数据\",\n            module = ModuleEnum.MODULE_UNKNOWN, operationType = OperationTypeEnum.DELETE, db = true)\n    @Override\n    public ResultWrapper<?> delAll(String ids){\n        String[] idArray = Convert.toStrArray(ids);\n        IService.deleteAll(idArray);\n        return ResultWrapper.getSuccessResultWrapperByMsg(\"批量删除#(data.codeTitleBrief)成功\");\n    }\n\n    /**\n     * #(data.codeTitleBrief) Excel 导出认证\n     *\n     * @param type 类型\n     * @param request request\n     */\n    @ApiOperation(value = \"Excel 导出认证\", notes = \"Excel 导出认证\")\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    @PreAuthorize(\"hasAnyAuthority(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_export\', \'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_import\')\")\n    #else\n    @PreAuthorize(\"hasAnyAuthority(\'#(data.moduleName.toLowerCase())_export\', \'#(data.moduleName.toLowerCase())_import\')\")\n    #end\n    @Override\n    public ResultWrapper<String> exportExcelAuth(String type, HttpServletRequest request) {\n        Optional<String> certificateOptional =\n                super.excelExportAuth(type, #(data.model.tableHumpName)RestApi.SUB_TITLE, request);\n        if(!certificateOptional.isPresent()){\n            return ResultWrapper.getErrorResultWrapper();\n        }\n        return ResultWrapper.getSuccessResultWrapper(certificateOptional.get());\n    }\n\n\n    /**\n     * #(data.codeTitleBrief) Excel 导出\n     * @param response response\n     */\n    @ApiOperation(value = \"导出Excel\", notes = \"导出Excel\")\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_export\')\")\n    #else\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_export\')\")\n    #end\n    @OperateLogger(description = \"#(data.codeTitleBrief) 导出Excel\",\n            module = ModuleEnum.MODULE_UNKNOWN, operationType = OperationTypeEnum.SELECT, db = true)\n    @Override\n    public void exportExcel(String certificate, HttpServletResponse response) {\n        // 导出Excel\n        super.excelExport(certificate, response);\n    }\n\n    /**\n     * #(data.codeTitleBrief) Excel 导入\n     * 注：这里 RequiresPermissions 引入的是 Shiro原生鉴权注解\n     * @param request 文件流 request\n     * @return ResultVo\n     */\n    @ApiOperation(value = \"导入Excel\", notes = \"导入Excel\")\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_import\')\")\n    #else\n    @PreAuthorize(\"hasAuthority(\'#(data.moduleName.toLowerCase())_import\')\")\n    #end   \n    @OperateLogger(description = \"#(data.codeTitleBrief) Excel 导入\",\n            module = ModuleEnum.MODULE_UNKNOWN, operationType = OperationTypeEnum.INSERT, db = true)\n    @Override\n    public ResultWrapper<?> importExcel(MultipartHttpServletRequest request) {\n        return super.importExcel(request);\n    }\n\n}', '0', 0, 1, '2022-08-07 00:10:00', 1, '2022-08-07 00:10:00');
INSERT INTO `gen_template_detail` VALUES (1555949317608185857, 1398253704724828162, '0', '${packageName}/${moduleName}/${subModuleName}/service/impl', '${model.tableHumpName}ServiceImpl.java', '#if(data.subModuleName != null && data.subModuleName != \"\")\npackage #(data.packageName+\".\"+data.moduleName+\".\"+data.subModuleName).service.impl;\n#else\npackage #(data.packageName+\".\"+data.moduleName).service.impl;\n#end\n\n\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.stereotype.Service;\nimport org.springframework.transaction.annotation.Transactional;\nimport org.opsli.core.base.service.impl.CrudServiceImpl;\n\n#if(data.subModuleName != null && data.subModuleName != \"\")\nimport #(data.packageName+\".\"+data.moduleName+\".\"+data.subModuleName).entity.#(data.model.tableHumpName);\nimport #(apiPath).wrapper.#(data.moduleName+\".\"+data.subModuleName).#(data.model.tableHumpName)Model;\nimport #(data.packageName+\".\"+data.moduleName+\".\"+data.subModuleName).service.I#(data.model.tableHumpName)Service;\nimport #(data.packageName+\".\"+data.moduleName+\".\"+data.subModuleName).mapper.#(data.model.tableHumpName)Mapper;\n#else\nimport #(data.packageName+\".\"+data.moduleName).entity.#(data.model.tableHumpName);\nimport #(apiPath).wrapper.#(data.moduleName).#(data.model.tableHumpName)Model;\nimport #(data.packageName+\".\"+data.moduleName).service.I#(data.model.tableHumpName)Service;\nimport #(data.packageName+\".\"+data.moduleName).mapper.#(data.model.tableHumpName)Mapper;\n#end\n\n\n/**\n * #(data.codeTitle) Service Impl\n *\n * @author #(data.authorName)\n * @date #(currTime)\n */\n@Service\npublic class #(data.model.tableHumpName)ServiceImpl extends CrudServiceImpl<#(data.model.tableHumpName)Mapper, #(data.model.tableHumpName), #(data.model.tableHumpName)Model>\n    implements I#(data.model.tableHumpName)Service {\n\n    @Autowired(required = false)\n    private #(data.model.tableHumpName)Mapper mapper;\n\n}', '0', 0, 1, '2022-08-07 00:10:00', 1, '2022-08-07 00:10:00');
INSERT INTO `gen_template_detail` VALUES (1555949317641740289, 1398253704724828162, '0', '${packageName}/${moduleName}/${subModuleName}/service', 'I${model.tableHumpName}Service.java', '#if(data.subModuleName != null && data.subModuleName != \"\")\npackage #(data.packageName+\".\"+data.moduleName+\".\"+data.subModuleName).service;\n#else\npackage #(data.packageName+\".\"+data.moduleName).service;\n#end\n\nimport org.opsli.core.base.service.interfaces.CrudServiceInterface;\n\n\n#if(data.subModuleName != null && data.subModuleName != \"\")\nimport #(data.packageName+\".\"+data.moduleName+\".\"+data.subModuleName).entity.#(data.model.tableHumpName);\nimport #(apiPath).wrapper.#(data.moduleName+\".\"+data.subModuleName).#(data.model.tableHumpName)Model;\n#else\nimport #(data.packageName+\".\"+data.moduleName).entity.#(data.model.tableHumpName);\nimport #(apiPath).wrapper.#(data.moduleName).#(data.model.tableHumpName)Model;\n#end\n\n/**\n * #(data.codeTitle) Service\n *\n * @author #(data.authorName)\n * @date #(currTime)\n */\npublic interface I#(data.model.tableHumpName)Service extends CrudServiceInterface<#(data.model.tableHumpName), #(data.model.tableHumpName)Model> {\n\n}', '0', 0, 1, '2022-08-07 00:10:00', 1, '2022-08-07 00:10:00');
INSERT INTO `gen_template_detail` VALUES (1555949317671100418, 1398253704724828162, '1', 'src/api/${moduleName}/${subModuleName}', '${model.tableHumpName}ManagementApi.js', 'import request from \"@/utils/request\";\nimport { downloadFileByData } from \"@/utils/download\";\n\nexport function getList(data) {\n  return request({\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    url: \"/api/v1/#(data.moduleName)/#(data.subModuleName)/findPage\",\n    #else\n    url: \"/api/v1/#(data.moduleName)/findPage\",\n    #end\n    method: \"get\",\n    params: data,\n  });\n}\n\nexport function doInsert(data) {\n  return request({\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    url: \"/api/v1/#(data.moduleName)/#(data.subModuleName)/insert\",\n    #else\n    url: \"/api/v1/#(data.moduleName)/insert\",\n    #end\n    method: \"post\",\n    data,\n  });\n}\n\nexport function doUpdate(data) {\n  return request({\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    url: \"/api/v1/#(data.moduleName)/#(data.subModuleName)/update\",\n    #else\n    url: \"/api/v1/#(data.moduleName)/update\",\n    #end\n    method: \"post\",\n    data,\n  });\n}\n\nexport function doDelete(data) {\n  return request({\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    url: \"/api/v1/#(data.moduleName)/#(data.subModuleName)/del\",\n    #else\n    url: \"/api/v1/#(data.moduleName)/del\",\n    #end\n    method: \"post\",\n    params: data,\n  });\n}\n\nexport function doDeleteAll(data) {\n  return request({\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    url: \"/api/v1/#(data.moduleName)/#(data.subModuleName)/delAll\",\n    #else\n    url: \"/api/v1/#(data.moduleName)/delAll\",\n    #end\n    method: \"post\",\n    params: data,\n  });\n}\n\n/**\n * 导出Excel 目前只支持一层参数传递\n * @param params 参数\n * @returns file\n */\n export async function doExportExcel(params) {\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    let authURL = \"/api/v1/#(data.moduleName)/#(data.subModuleName)/excel/auth/export\";\n    #else\n    let authURL = \"/api/v1/#(data.moduleName)/excel/auth/export\";\n    #end\n\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    let downloadURL = \"/api/v1/#(data.moduleName)/#(data.subModuleName)/excel/export/\";\n    #else\n    let downloadURL = \"/api/v1/#(data.moduleName)/excel/export/\";\n    #end\n\n\n    // 认证\n    const { data } = await request({\n      url: authURL,\n      method: \"get\",\n      params: params,\n    });\n  \n    if (data) {\n      // 下载文件\n      downloadFileByData(downloadURL + data, params);\n    }\n  }\n  \n  /**\n   * 下载模版\n   * @returns file\n   */\n  export async function doDownloadTemplate() {\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    let authURL = \"/api/v1/#(data.moduleName)/#(data.subModuleName)/excel/auth/import-template-export\";\n    #else\n    let authURL = \"/api/v1/#(data.moduleName)/excel/auth/import-template-export\";\n    #end\n\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    let downloadURL = \"/api/v1/#(data.moduleName)/#(data.subModuleName)/excel/export/\";\n    #else\n    let downloadURL = \"/api/v1/#(data.moduleName)/excel/export/\";\n    #end\n\n    // 认证\n    const { data } = await request({\n      url: authURL,\n      method: \"get\",\n    });\n  \n    if (data) {\n      // 下载文件\n      downloadFileByData(downloadURL + data, {});\n    }\n  }\n\n\n/**\n * 导入Excel\n * @returns file\n */\nexport function doImportExcel(data) {\n  return request({\n    #if(data.subModuleName != null && data.subModuleName != \"\")\n    url: \"/api/v1/#(data.moduleName)/#(data.subModuleName)/importExcel\",\n    #else\n    url: \"/api/v1/#(data.moduleName)/importExcel\",\n    #end\n    method: \"post\",\n    // 最长超时时间 3 分钟\n    timeout: 180000,\n    headers: {\n      \"Content-Type\": \"multipart/form-data\"\n    },\n    data,\n  });\n}', '0', 0, 1, '2022-08-07 00:10:00', 1, '2022-08-07 00:10:00');
INSERT INTO `gen_template_detail` VALUES (1555949317700460545, 1398253704724828162, '1', 'src/views/modules/${moduleName}/${subModuleName}/components', '${model.tableHumpName}ManagementEdit.vue', '<template>\n  <el-dialog\n    :title=\"title\"\n    :visible.sync=\"dialogFormVisible\"\n    :close-on-click-modal=\"false\"\n    width=\"800px\"\n    @close=\"close\"\n  >\n    <el-form ref=\"form\" :model=\"form\" :rules=\"rules\" label-width=\"105px\">\n      <el-row :gutter=\"10\" >\n      #for(column : data.model.formList)\n        ### 文本框\n        #if(column.showType == \"0\")\n        <el-col :xs=\"24\" :sm=\"24\" :md=\"24\" :lg=\"12\" :xl=\"12\">\n          <el-form-item label=\"#(column.fieldComments)\" prop=\"#(column.fieldHumpName)\">\n            <el-input v-model=\"form.#(column.fieldHumpName)\" autocomplete=\"off\"></el-input>\n          </el-form-item>\n        </el-col>\n        ### 文本域\n        #else if(column.showType == \"1\")\n        <el-col :xs=\"24\" :sm=\"24\" :md=\"24\" :lg=\"12\" :xl=\"12\">\n          <el-form-item label=\"#(column.fieldComments)\" prop=\"#(column.fieldHumpName)\">\n            <el-input type=\"textarea\" v-model=\"form.#(column.fieldHumpName)\" autocomplete=\"off\"></el-input>\n          </el-form-item>\n        </el-col>\n        ### 字典\n        #else if(column.showType == \"2\")\n        <el-col :xs=\"24\" :sm=\"24\" :md=\"24\" :lg=\"12\" :xl=\"12\">\n          <el-form-item label=\"#(column.fieldComments)\" prop=\"#(column.fieldHumpName)\">\n            <el-select v-model=\"form.#(column.fieldHumpName)\" clearable\n                       placeholder=\"请选择\" style=\"width: 100%\">\n              <el-option\n                      v-for=\"item in dict.#(column.dictTypeCode)\"\n                      :key=\"item.dictValue\"\n                      :label=\"item.dictName\"\n                      :value=\"item.dictValue\"\n              ></el-option>\n            </el-select>\n          </el-form-item>\n        </el-col>\n        ### 日期时间\n        #else if(column.showType == \"3\")\n        <el-col :xs=\"24\" :sm=\"24\" :md=\"24\" :lg=\"12\" :xl=\"12\">\n          <el-form-item label=\"#(column.fieldComments)\" prop=\"#(column.fieldHumpName)\">\n            <el-date-picker\n                    v-model=\"form.#(column.fieldHumpName)\"\n                    type=\"datetime\"\n                    placeholder=\"选择#(column.fieldComments)\"\n                    style=\"width: 100%\"\n            ></el-date-picker>\n          </el-form-item>\n        </el-col>\n        ### 日期\n        #else if(column.showType == \"4\")\n        <el-col :xs=\"24\" :sm=\"24\" :md=\"24\" :lg=\"12\" :xl=\"12\">\n          <el-form-item label=\"#(column.fieldComments)\" prop=\"#(column.fieldHumpName)\">\n            <el-date-picker\n                    v-model=\"form.#(column.fieldHumpName)\"\n                    type=\"date\"\n                    placeholder=\"选择#(column.fieldComments)\"\n                    style=\"width: 100%\"\n            ></el-date-picker>\n          </el-form-item>\n        </el-col>\n        #end\n\n      #end\n      </el-row>\n\n    </el-form>\n    <div slot=\"footer\" class=\"dialog-footer\">\n      <el-button @click=\"close\">取 消</el-button>\n      <el-button type=\"primary\" @click=\"save\">确 定</el-button>\n    </div>\n  </el-dialog>\n</template>\n\n<script>\n  #if(data.subModuleName != null && data.subModuleName != \"\")\n  import { doInsert, doUpdate } from \"@/api/#(data.moduleName)/#(data.subModuleName)/#(data.model.tableHumpName)ManagementApi\";\n  #else\n  import { doInsert, doUpdate } from \"@/api/#(data.moduleName)/#(data.model.tableHumpName)ManagementApi\";\n  #end\n  import { isNull } from \"@/utils/validate\";\n  import { formateDate } from \"@/utils/format\";\n  import { validatorRule } from \"@/utils/validateRlue\";\n\n  export default {\n    name: \"#(data.model.tableHumpName)ManagementEdit\",\n    data() {\n\n      return {\n        form: {\n          // 设置默认值\n          version: 0\n        },\n        dict: {},\n        rules: {\n          #for(columnList : data.model.formList)\n          #for(column : columnList)\n            #if(column.validateTypeList != null && column.validateTypeList.size() > 0)\n              #(column.fieldHumpName): [\n                #for(typeNotComma : column.validateTypeList)\n                #if(typeNotComma == \"IS_NOT_NULL\")\n                { required: true, trigger: \"blur\", message: \"#(column.fieldComments)非空\" },\n                #end\n                #end\n                #for(typeNotComma : column.validateTypeList)\n                #if(typeNotComma != \"IS_NOT_NULL\")\n                { required: false, trigger: \"blur\", validator: validatorRule.#(typeNotComma) },\n                #end\n                #end\n              ],\n            #end\n          #end\n          #end\n        },\n        title: \"\",\n        dialogFormVisible: false,\n      };\n    },\n    created() {\n\n    },\n    mounted() {\n      // 加载字典值\n      #for(column : data.model.columnList)\n      #if(column.dictTypeCode != null && column.dictTypeCode != \"\")\n      this.dict.#(column.dictTypeCode) = this.$getDictList(\"#(column.dictTypeCode)\");\n      #end\n      #end\n    },\n    methods: {\n      showEdit(row) {\n        if (!row) {\n          this.title = \"添加\";\n        } else {\n          this.title = \"编辑\";\n          this.form = Object.assign({}, row);\n        }\n        this.dialogFormVisible = true;\n      },\n      close() {\n        this.dialogFormVisible = false;\n        this.$refs[\"form\"].resetFields();\n        this.form = this.$options.data().form;\n      },\n      save() {\n        this.$refs[\"form\"].validate(async (valid) => {\n          if (valid) {\n            // 处理数据\n            this.handlerFormData(this.form);\n\n            // 修改\n            if (!isNull(this.form.id)) {\n              const { msg } = await doUpdate(this.form);\n  			  this.$baseMessage(msg, \"success\");\n            } else {\n              const { msg } = await doInsert(this.form);\n              this.$baseMessage(msg, \"success\");\n            }\n\n            await this.$emit(\"fetchData\");\n            this.close();\n          } else {\n            return false;\n          }\n        });\n      },\n      // 处理 form数据\n      handlerFormData(formData){\n        if(!isNull(formData)){\n          for(let key in formData){\n            // 对于时间类进行处理\n            if(\"[object Date]\" === Object.prototype.toString.call(formData[key])){\n              formData[key] = formateDate(formData[key], \'yyyy-MM-dd hh:mm:ss\');\n            }\n          }\n        }\n      },\n    },\n  };\n</script>\n', '0', 0, 1, '2022-08-07 00:10:00', 1, '2022-08-07 00:10:00');
INSERT INTO `gen_template_detail` VALUES (1555949317734014977, 1398253704724828162, '1', 'src/views/modules/${moduleName}/${subModuleName}/components', '${model.tableHumpName}ManagementImport.vue', '<template>\n  <el-dialog\n    :title=\"title\"\n    :visible.sync=\"dialogFormVisible\"\n    :close-on-click-modal=\"false\"\n    width=\"800px\"\n    class=\"import-excel\"\n    @close=\"close\"\n  >\n    <el-upload\n      ref=\"excelImport\"\n      drag\n      accept=\".xls,.xlsx\"\n      style=\"width: 100%\"\n      :action=\"importExcelUrl\"\n      :multiple=\"false\"\n      :before-upload=\"beforeUpload\"\n      :http-request=\"handleImport\"\n      :on-success=\"onSuccess\"\n      :on-error=\"onError\"\n      :on-progress=\"onProcess\"\n    >\n      <i class=\"el-icon-upload\"></i>\n      <div class=\"el-upload__text\">将文件拖到此处，或<em>点击导入</em></div>\n      <div class=\"el-upload__tip\" slot=\"tip\">只能上传xls/xlsx文件，且不超过5MB</div>\n    </el-upload>\n\n    <div slot=\"footer\" class=\"dialog-footer\">\n      <el-button type=\"primary\" @click=\"downloadExcelTemplate\">下载模版</el-button>\n      <el-button @click=\"close\">关 闭</el-button>\n    </div>\n  </el-dialog>\n</template>\n\n<script>\n  #if(data.subModuleName != null && data.subModuleName != \"\")\n  import { doDownloadTemplate, doImportExcel } from \"@/api/#(data.moduleName)/#(data.subModuleName)/#(data.model.tableHumpName)ManagementApi\";\n  #else\n  import { doDownloadTemplate, doImportExcel } from \"@/api/#(data.moduleName)/#(data.model.tableHumpName)ManagementApi\";\n  #end\n  import {isNull} from \"@/utils/validate\";\n  import {random} from \"@/utils\";\n\n  export default {\n    name: \"#(data.model.tableHumpName)ManagementImport\",\n    data() {\n      return {\n        title: \"导入Excel\",\n        importExcelUrl: \'\',\n        dialogFormVisible: false,\n        loadProgress: 0, // 动态显示进度条\n        progressFlag: false, // 关闭进度条,\n        progressMap: {}\n      };\n    },\n    created() {},\n    mounted() {},\n    methods: {\n      show() {\n        this.dialogFormVisible = true;\n      },\n      close() {\n        this.dialogFormVisible = false;\n        this.$refs[\"excelImport\"].clearFiles();\n      },\n      // 下载模版\n      downloadExcelTemplate() {\n        doDownloadTemplate();\n      },\n      // 上传成功\n      onSuccess(response, file, fileList){\n        this.successProcess(file.uid);\n        this.$emit(\"fetchData\");\n      },\n      // 上传失败\n      onError(err, file, fileList){\n        this.errorProcess(file.uid);\n      },\n      // 进度条\n      onProcess(event, file, fileList) {\n        file.status = \'uploading\';\n        file.percentage = 0;\n        this.progressMap[file.uid] = {\n          file: file,\n        }\n        this.autoLoadingProcess(file.uid);\n      },\n\n      // 导入文件限制验证\n      beforeUpload(file) {\n        let testMsg = file.name.substring(file.name.lastIndexOf(\'.\')+1)\n        const extension = testMsg === \'xls\'\n        const extension2 = testMsg === \'xlsx\'\n        const isLt2M = file.size / 1024 / 1024 < 5\n        if(!extension && !extension2) {\n          this.$baseMessage(\'上传文件只能是 xls、xlsx格式!\', \"warning\");\n        }\n        if(!isLt2M) {\n          this.$baseMessage(\'上传文件大小不能超过 5MB!\', \"warning\");\n        }\n        return (extension || extension2) && isLt2M\n      },\n      // 自定义导入\n      handleImport(params){\n        if(!isNull(params)){\n          let blobObject = new Blob([params.file]);\n          let formData = new window.FormData()\n          formData.append(\"file\", blobObject);\n          const ret = doImportExcel(formData);\n          ret.then((v) => {\n            const {msg,data} = v;\n            this.$baseMessage(msg, \"success\");\n            // 成功\n            params.onSuccess();\n          }).catch( (e) =>{\n            // 失败\n            params.onError();\n          });\n          // 上传进度\n          params.onProgress();\n        }else{\n          params.onError();\n        }\n      },\n\n      // ==============\n\n      successProcess(fileUid) {\n        let tmp = this.progressMap[fileUid];\n        if(tmp !== null && tmp !== undefined){\n          try {\n            window.clearTimeout(tmp.timer);\n          }catch (e){}\n          tmp.file.status = \'success\';\n          tmp.file.percentage = 100;\n          delete this.progressMap[fileUid];\n        }\n      },\n      errorProcess(fileUid) {\n        let tmp = this.progressMap[fileUid];\n        if(tmp !== null && tmp !== undefined){\n          try {\n            window.clearTimeout(tmp.timer);\n          }catch (e){}\n          tmp.file.status = \'fail\';\n          delete this.progressMap[fileUid];\n        }\n      },\n      autoLoadingProcess(fileUid) {\n        const that = this;\n        let tmp = this.progressMap[fileUid];\n        if(tmp !== null && tmp !== undefined){\n          if(tmp.file.percentage >= 99) {\n            try {\n              window.clearTimeout(tmp.timer);\n            }catch (e){}\n          }else {\n            // 如果大于 99 则 停止\n            if(tmp.file.percentage + random(1, 12) > 99){\n              tmp.file.percentage = 99;\n            }else{\n              // 进度随机增长 1 - 12\n              tmp.file.percentage += random(1, 12);\n            }\n\n            // 递归增加百分比 递归时间为 随机 1-5秒\n            tmp.timer = window.setTimeout(function (){\n              that.autoLoadingProcess(fileUid);\n            }, random(1000, 5000));\n          }\n        }\n      },\n\n    },\n  };\n</script>\n', '0', 0, 1, '2022-08-07 00:10:00', 1, '2022-08-07 00:10:00');
INSERT INTO `gen_template_detail` VALUES (1555949317771763714, 1398253704724828162, '1', 'src/views/modules/${moduleName}/${subModuleName}', 'index.vue', '<template>\n  <div class=\"tenantManagement-container\">\n\n    <el-collapse-transition>\n    <div class=\"more-query\" v-show=\"this.moreQueryFlag\">\n      <!-- 更多查找 -->\n      <vab-query-form>\n        <vab-query-form-left-panel :span=\"24\">\n          <el-form :inline=\"true\" :model=\"queryForm\" @submit.native.prevent>\n            #for(column : data.model.moreQueryList)\n\n            ### 字典\n            #if(column.showType == \"2\")\n            <el-form-item>\n              <el-select v-model=\"queryForm.#(column.fieldHumpName+\'_\'+column.queryType)\" placeholder=\"请选择#(column.fieldComments)\" clearable style=\"width: 100%\">\n                <el-option\n                      v-for=\"item in dict.#(column.dictTypeCode)\"\n                      :key=\"item.dictValue\"\n                      :label=\"item.dictName\"\n                      :value=\"item.dictValue\"\n                ></el-option>\n              </el-select>\n            </el-form-item>\n            #else if(column.showType == \"3\")\n            ### 时间\n            <el-form-item>\n              <el-date-picker\n                      v-model=\"#(column.fieldHumpName)DatePicker\"\n                      type=\"datetimerange\"\n                      :picker-options=\"pickerOptions\"\n                      range-separator=\"至\"\n                      start-placeholder=\"开始#(column.fieldComments)\"\n                      end-placeholder=\"结束#(column.fieldComments)\"\n                      align=\"right\">\n              </el-date-picker>\n            </el-form-item>\n            #else if(column.showType == \"4\")\n            ### 日期\n            <el-form-item>\n              <el-date-picker\n                      v-model=\"#(column.fieldHumpName)DatePicker\"\n                      type=\"daterange\"\n                      align=\"right\"\n                      range-separator=\"至\"\n                      start-placeholder=\"开始#(column.fieldComments)\"\n                      end-placeholder=\"结束#(column.fieldComments)\"\n              ></el-date-picker>\n            </el-form-item>\n            #else\n            #if(column.queryType == \"EQ\" || column.queryType == \"LIKE\")\n            <el-form-item>\n              <el-input\n                      v-model.trim=\"queryForm.#(column.fieldHumpName)_#(column.queryType)\"\n                      placeholder=\"请输入#(column.fieldComments)\"\n                      clearable\n              />\n            </el-form-item>\n            #else if(column.queryType == \"RANGE\")\n            <el-col :span=\"12\" >\n            <el-form-item style=\"text-align: center\">\n              <el-input\n                      v-model.trim=\"queryForm.#(column.fieldHumpName)_BEGIN\"\n                      placeholder=\"#(column.fieldComments)开始\"\n                      clearable\n                      style=\"float: left;width: calc(50% - 6px)\"\n              />\n              <div style=\"float:left;width: 12px\">-</div>\n              <el-input\n                      v-model.trim=\"queryForm.#(column.fieldHumpName)_END\"\n                      placeholder=\"#(column.fieldComments)结束\"\n                      clearable\n                      style=\"float: right;width: calc(50% - 6px)\"\n              />\n            </el-form-item>\n            </el-col>\n            #end\n            #end\n            #end\n\n\n          </el-form>\n        </vab-query-form-left-panel>\n\n      </vab-query-form>\n      <el-divider></el-divider>\n    </div>\n    </el-collapse-transition>\n\n    <!-- 主要操作  -->\n    <vab-query-form>\n      <vab-query-form-left-panel :span=\"10\">\n        <el-button\n            #if(data.subModuleName != null && data.subModuleName != \"\")\n            v-if=\"$perms(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_insert\')\"\n            #else\n            v-if=\"$perms(\'#(data.moduleName.toLowerCase())_insert\')\"\n            #end\n            icon=\"el-icon-plus\"\n            type=\"primary\"\n            @click=\"handleInsert\"\n        > 添加 </el-button>\n\n        <el-button\n            #if(data.subModuleName != null && data.subModuleName != \"\")\n            v-if=\"$perms(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_import\')\"\n            #else\n            v-if=\"$perms(\'#(data.moduleName.toLowerCase())_import\')\"\n            #end\n            icon=\"el-icon-upload2\"\n            type=\"warning\"\n            @click=\"handleImportExcel\"\n        > 导入 </el-button>\n\n        <el-button\n            #if(data.subModuleName != null && data.subModuleName != \"\")\n            v-if=\"$perms(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_export\')\"\n            #else\n            v-if=\"$perms(\'#(data.moduleName.toLowerCase())_export\')\"\n            #end\n            icon=\"el-icon-download\"\n            type=\"warning\"\n            @click=\"handleExportExcel\"\n        > 导出 </el-button>\n\n        <el-button\n            #if(data.subModuleName != null && data.subModuleName != \"\")\n            v-if=\"$perms(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_delete\')\"\n            #else\n            v-if=\"$perms(\'#(data.moduleName.toLowerCase())_delete\')\"\n            #end\n            :disabled=\"!selectRows.length > 0\"\n            icon=\"el-icon-delete\"\n            type=\"danger\"\n            @click=\"handleDelete\"\n        > 批量删除 </el-button>\n\n      </vab-query-form-left-panel>\n      <vab-query-form-right-panel :span=\"14\">\n        <el-form :inline=\"true\" :model=\"queryForm\" @submit.native.prevent>\n          ### 代码生成器 简要只展示2个\n          #for(column : data.model.briefQueryList)\n\n          ### 字典\n          #if(column.showType == \"2\")\n          <el-form-item>\n            <el-select v-model=\"queryForm.#(column.fieldHumpName+\'_\'+column.queryType)\" placeholder=\"请选择#(column.fieldComments)\" clearable style=\"width: 100%\">\n              <el-option\n                      v-for=\"item in dict.#(column.dictTypeCode)\"\n                      :key=\"item.dictValue\"\n                      :label=\"item.dictName\"\n                      :value=\"item.dictValue\"\n              ></el-option>\n            </el-select>\n          </el-form-item>\n          #else if(column.showType == \"3\")\n          ### 时间\n          <el-form-item>\n            <el-date-picker\n                    v-model=\"#(column.fieldHumpName)DatePicker\"\n                    type=\"datetimerange\"\n                    :picker-options=\"pickerOptions\"\n                    range-separator=\"至\"\n                    start-placeholder=\"开始#(column.fieldComments)\"\n                    end-placeholder=\"结束#(column.fieldComments)\"\n                    align=\"right\">\n            </el-date-picker>\n          </el-form-item>\n          #else if(column.showType == \"4\")\n          ### 日期\n          <el-form-item>\n            <el-date-picker\n                    v-model=\"#(column.fieldHumpName)DatePicker\"\n                    type=\"daterange\"\n                    align=\"right\"\n                    range-separator=\"至\"\n                    start-placeholder=\"开始#(column.fieldComments)\"\n                    end-placeholder=\"结束#(column.fieldComments)\"\n            ></el-date-picker>\n          </el-form-item>\n          #else\n          #if(column.queryType == \"EQ\" || column.queryType == \"LIKE\")\n          <el-form-item>\n            <el-input\n                    v-model.trim=\"queryForm.#(column.fieldHumpName)_#(column.queryType)\"\n                    placeholder=\"请输入#(column.fieldComments)\"\n                    clearable\n            />\n          </el-form-item>\n          #else if(column.queryType == \"RANGE\")\n          <el-col :span=\"12\" >\n          <el-form-item style=\"text-align: center\">\n            <el-input\n                    v-model.trim=\"queryForm.#(column.fieldHumpName)_BEGIN\"\n                    placeholder=\"#(column.fieldComments)开始\"\n                    clearable\n                    style=\"float: left;width: calc(50% - 6px)\"\n            />\n            <div style=\"float:left;width: 12px\">-</div>\n            <el-input\n                    v-model.trim=\"queryForm.#(column.fieldHumpName)_END\"\n                    placeholder=\"#(column.fieldComments)结束\"\n                    clearable\n                    style=\"float: right;width: calc(50% - 6px)\"\n            />\n          </el-form-item>\n          </el-col>\n          #end\n          #end\n          #end\n\n          <el-form-item>\n            <el-button icon=\"el-icon-search\" type=\"primary\" @click=\"queryData\">\n              查询\n            </el-button>\n\n            #if(data.model.moreQueryList != null && data.model.moreQueryList.size() > 0)\n            <el-button icon=\"el-icon-search\" @click=\"moreQuery\">\n              更多\n            </el-button>\n            #end\n\n          </el-form-item>\n        </el-form>\n      </vab-query-form-right-panel>\n    </vab-query-form>\n\n    <el-table\n      v-loading=\"listLoading\"\n      :data=\"list\"\n      :element-loading-text=\"elementLoadingText\"\n      @selection-change=\"setSelectRows\"\n    >\n      <el-table-column show-overflow-tooltip type=\"selection\"></el-table-column>\n\n      <el-table-column show-overflow-tooltip label=\"序号\" width=\"95\">\n        <template slot-scope=\"scope\">\n          {{(queryForm.pageNo - 1) * queryForm.pageSize + scope.$index + 1}}\n        </template>\n      </el-table-column>\n\n      #for(column : data.model.columnList)\n      ### 字典\n      #if(column.showType == \"2\" && column.izShowList == \"1\")\n      <el-table-column\n              show-overflow-tooltip\n              prop=\"#(column.fieldHumpName)\"\n              label=\"#(column.fieldComments)\"\n      >\n\n        <template slot-scope=\"scope\">\n          <span>\n            {{ $getDictNameByValue(\'#(column.dictTypeCode)\', scope.row.#(column.fieldHumpName)) }}\n          </span>\n        </template>\n\n      </el-table-column>\n\n      #else\n      #if(column.izShowList == \"1\")\n      <el-table-column\n              show-overflow-tooltip\n              prop=\"#(column.fieldHumpName)\"\n              label=\"#(column.fieldComments)\"\n      ></el-table-column>\n\n      #end\n      #end\n      #end\n\n      <el-table-column\n        show-overflow-tooltip\n        label=\"操作\"\n        width=\"200\"\n        #if(data.subModuleName != null && data.subModuleName != \"\")\n        v-if=\"$perms(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_update\') || $perms(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_delete\')\"\n        #else\n        v-if=\"$perms(\'#(data.moduleName.toLowerCase())_update\') || $perms(\'#(data.moduleName.toLowerCase())_delete\')\"\n        #end\n      >\n        <template v-slot=\"scope\">\n          <el-button\n            #if(data.subModuleName != null && data.subModuleName != \"\")\n            v-if=\"$perms(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_update\')\"\n            #else\n            v-if=\"$perms(\'#(data.moduleName.toLowerCase())_update\')\"\n            #end\n            type=\"text\"\n            @click=\"handleUpdate(scope.row)\"\n          > 编辑 </el-button>\n          \n          <el-divider direction=\"vertical\"></el-divider>\n          \n          <el-button\n            #if(data.subModuleName != null && data.subModuleName != \"\")\n            v-if=\"$perms(\'#(data.moduleName.toLowerCase())_#(data.subModuleName.toLowerCase())_delete\')\"\n            #else\n            v-if=\"$perms(\'#(data.moduleName.toLowerCase())_delete\')\"\n            #end\n            type=\"text\"\n            @click=\"handleDelete(scope.row)\"\n          > 删除 </el-button>\n        </template>\n\n      </el-table-column>\n    </el-table>\n    <el-pagination\n      background\n      :current-page=\"queryForm.pageNo\"\n      :page-size=\"queryForm.pageSize\"\n      :layout=\"layout\"\n      :total=\"total\"\n      @size-change=\"handleSizeChange\"\n      @current-change=\"handleCurrentChange\"\n    ></el-pagination>\n\n    <edit ref=\"edit\" @fetchData=\"fetchData\"></edit>\n    <import ref=\"import\" @fetchData=\"fetchData\" ></import>\n\n  </div>\n</template>\n\n<script>\n  #if(data.subModuleName != null && data.subModuleName != \"\")\n  import { getList, doDelete, doDeleteAll, doExportExcel } from \"@/api/#(data.moduleName)/#(data.subModuleName)/#(data.model.tableHumpName)ManagementApi\";\n  #else\n  import { getList, doDelete, doDeleteAll, doExportExcel } from \"@/api/#(data.moduleName)/#(data.model.tableHumpName)ManagementApi\";\n  #end\n  import Edit from \"./components/#(data.model.tableHumpName)ManagementEdit\";\n  import Import from \"./components/#(data.model.tableHumpName)ManagementImport\";\n\n  import { vueButtonClickBan } from \"@/utils\";\n  import { isNotNull } from \"@/utils/valiargs\";\n  import { formateDate } from \"@/utils/format\";\n\n  export default {\n    name: \"#(data.model.tableHumpName)Management\",\n    components: { Edit, Import },\n    data() {\n      return {\n        list: null,\n        listLoading: true,\n        layout: \"total, prev, pager, next, sizes, jumper\",\n        total: 0,\n        selectRows: \"\",\n        elementLoadingText: \"正在加载...\",\n        moreQueryFlag: false,\n        queryForm: {\n          pageNo: 1,\n          pageSize: 10,\n          ### 代码生成器 简要2个\n          #for(column : data.model.briefQueryList)\n          ### 字典\n          #if(column.showType == \"2\")\n          #(column.fieldHumpName)_EQ: \"\",\n          #else if(column.showType == \"3\" || column.showType == \"4\")\n          ### 日期\n          #(column.fieldHumpName)_BEGIN: \"\",\n          #(column.fieldHumpName)_END: \"\",\n          #else\n          #if(column.queryType == \"EQ\" || column.queryType == \"LIKE\")\n          #(column.fieldHumpName)_#(column.queryType): \"\",\n          #else if(column.queryType == \"RANGE\")\n          #(column.fieldHumpName)_BEGIN: \"\",\n          #(column.fieldHumpName)_END: \"\",\n          #end\n          #end\n          #end\n          ### 代码生成器 更多\n          #for(column : data.model.moreQueryList)\n          ### 字典\n          #if(column.showType == \"2\")\n          #(column.fieldHumpName)_EQ: \"\",\n          #else if(column.showType == \"3\" || column.showType == \"4\")\n          ### 日期\n          #(column.fieldHumpName)_BEGIN: \"\",\n          #(column.fieldHumpName)_END: \"\",\n          #else\n          #if(column.queryType == \"EQ\" || column.queryType == \"LIKE\")\n          #(column.fieldHumpName)_#(column.queryType): \"\",\n          #else if(column.queryType == \"RANGE\")\n          #(column.fieldHumpName)_BEGIN: \"\",\n          #(column.fieldHumpName)_END: \"\",\n          #end\n          #end\n          #end\n        },\n        ### 代码生成器 简要2个\n        #for(column : data.model.briefQueryList)\n        ### 日期\n        #if(column.showType == \"3\" || column.showType == \"4\")\n        #(column.fieldHumpName)DatePicker: [],\n        #end\n        #end\n        ### 代码生成器 更多\n        #for(column : data.model.moreQueryList)\n        ### 日期\n        #if(column.showType == \"3\" || column.showType == \"4\")\n        #(column.fieldHumpName)DatePicker: [],\n        #end\n        #end\n        dict:{},\n        pickerOptions: {\n          shortcuts: [{\n            text: \'最近一周\',\n            onClick(picker) {\n              const end = new Date();\n              const start = new Date();\n              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);\n              picker.$emit(\'pick\', [start, end]);\n            }\n          }, {\n            text: \'最近一个月\',\n            onClick(picker) {\n              const end = new Date();\n              const start = new Date();\n              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);\n              picker.$emit(\'pick\', [start, end]);\n            }\n          }, {\n            text: \'最近三个月\',\n            onClick(picker) {\n              const end = new Date();\n              const start = new Date();\n              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);\n              picker.$emit(\'pick\', [start, end]);\n            }\n          }]\n        },\n      };\n    },\n    created() {\n      this.fetchData();\n    },\n    mounted() {\n      #for(column : data.model.columnList)\n      #if(column.dictTypeCode != null && column.dictTypeCode != \"\")\n      this.dict.#(column.dictTypeCode) = this.$getDictList(\"#(column.dictTypeCode)\");\n      #end\n      #end\n    },\n    methods: {\n      setSelectRows(val) {\n        this.selectRows = val;\n      },\n      handleInsert(row) {\n        this.$refs[\"edit\"].showEdit();\n      },\n      handleUpdate(row) {\n        if (row.id) {\n          this.$refs[\"edit\"].showEdit(row);\n        }\n      },\n      handleDelete(row) {\n        if (row.id) {\n          this.$baseConfirm(\"你确定要删除当前项吗\", null, async () => {\n            const { msg } = await doDelete({ id: row.id });\n            this.$baseMessage(msg, \"success\");\n            await this.fetchData();\n          });\n        } else {\n          if (this.selectRows.length > 0) {\n            const ids = this.selectRows.map((item) => item.id).join();\n            this.$baseConfirm(\"你确定要删除选中项吗\", null, async () => {\n              const { msg } = await doDeleteAll({ ids });\n              this.$baseMessage(msg, \"success\");\n              await this.fetchData();\n            });\n          } else {\n            this.$baseMessage(\"未选中任何行\", \"error\");\n            return false;\n          }\n        }\n      },\n      // 导出excel\n      handleExportExcel(el){\n        // 导出按钮防抖处理 默认限制为10秒\n        vueButtonClickBan(el, 10);\n\n        // 执行导出\n        doExportExcel(this.queryForm);\n      },\n      // 导入excel\n      handleImportExcel(){\n        this.$refs[\"import\"].show();\n      },\n\n\n      handleSizeChange(val) {\n        this.queryForm.pageSize = val;\n        this.fetchData();\n      },\n      handleCurrentChange(val) {\n        this.queryForm.pageNo = val;\n        this.fetchData();\n      },\n      moreQuery(){\n        this.moreQueryFlag = !this.moreQueryFlag;\n      },\n      queryData() {\n        ### 代码生成器 简要2个\n        #for(column : data.model.briefQueryList)\n        ### 日期\n        #if(column.showType == \"3\" || column.showType == \"4\")\n        if(isNotNull(this.#(column.fieldHumpName)DatePicker) && this.#(column.fieldHumpName)DatePicker.length === 2){\n          this.queryForm.#(column.fieldHumpName)_BEGIN =\n                  this.#(column.fieldHumpName)DatePicker.length === 0 ? \"\" : formateDate(this.#(column.fieldHumpName)DatePicker[0], \'yyyy-MM-dd hh:mm:ss\');\n          this.queryForm.#(column.fieldHumpName)_END =\n                  this.#(column.fieldHumpName)DatePicker.length === 0 ? \"\" : formateDate(this.#(column.fieldHumpName)DatePicker[1], \'yyyy-MM-dd hh:mm:ss\');\n        }else{\n          this.queryForm.#(column.fieldHumpName)_BEGIN = \"\";\n          this.queryForm.#(column.fieldHumpName)_END = \"\";\n        }        #end\n        #end\n        ### 代码生成器 更多\n        #for(column : data.model.moreQueryList)\n        ### 日期\n        #if(column.showType == \"3\" || column.showType == \"4\")\n        if(isNotNull(this.#(column.fieldHumpName)DatePicker) && this.#(column.fieldHumpName)DatePicker.length === 2){\n          this.queryForm.#(column.fieldHumpName)_BEGIN =\n                  this.#(column.fieldHumpName)DatePicker.length === 0 ? \"\" : formateDate(this.#(column.fieldHumpName)DatePicker[0], \'yyyy-MM-dd hh:mm:ss\');\n          this.queryForm.#(column.fieldHumpName)_END =\n                  this.#(column.fieldHumpName)DatePicker.length === 0 ? \"\" : formateDate(this.#(column.fieldHumpName)DatePicker[1], \'yyyy-MM-dd hh:mm:ss\');\n        }else{\n          this.queryForm.#(column.fieldHumpName)_BEGIN = \"\";\n          this.queryForm.#(column.fieldHumpName)_END = \"\";\n        }\n        #end\n        #end\n\n        this.queryForm.pageNo = 1;\n        this.fetchData();\n      },\n      async fetchData() {\n        this.listLoading = true;\n        const { data } = await getList(this.queryForm);\n        if(isNotNull(data)){\n          this.list = data.rows;\n          this.total = data.total;\n        }\n        setTimeout(() => {\n            this.listLoading = false;\n        }, 300);\n      },\n    },\n  };\n</script>\n', '0', 0, 1, '2022-08-07 00:10:00', 1, '2022-08-07 00:10:00');
COMMIT;

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` bigint(20) NOT NULL COMMENT '日志ID',
  `level` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日志等级',
  `module_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '被操作的系统模块',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '方法名',
  `args` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '参数',
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作人id',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作账号',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日志描述',
  `operation_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作类型',
  `run_time` bigint(20) DEFAULT NULL COMMENT '方法运行时间',
  `return_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '方法返回值',
  `tenant_id` bigint(19) DEFAULT NULL COMMENT '多租户ID',
  `log_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日志请求类型',
  `version` int(11) DEFAULT '0' COMMENT '版本（乐观锁）',
  `create_by` bigint(19) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(19) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_module_id` (`module_id`) USING BTREE,
  KEY `idx_method` (`method`) USING BTREE,
  KEY `idx_operation_type` (`operation_type`) USING BTREE,
  KEY `idx_log_type` (`log_type`) USING BTREE,
  KEY `idx_user` (`user_id`) USING BTREE,
  KEY `idx_tenant` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='业务操作日志';

-- ----------------------------
-- Table structure for sys_area
-- ----------------------------
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area` (
  `id` bigint(19) NOT NULL COMMENT '唯一主键',
  `parent_id` bigint(19) DEFAULT NULL COMMENT '上级ID',
  `area_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地域编号',
  `area_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '地域名称',
  `area_type` int(5) NOT NULL COMMENT '地域类型',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标记:0未删除，1删除',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `area` (`area_code`) USING BTREE,
  KEY `parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='地区表';

-- ----------------------------
-- Records of sys_area
-- ----------------------------
BEGIN;
INSERT INTO `sys_area` VALUES (86, 0, '86', '中国', 0, '0', 3, 1, '2020-12-28 17:43:30', 1, '2021-01-25 17:36:59', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110000, 86, '110000', '北京市', 1, '0', 1, 1, '2020-12-28 17:43:30', 1, '2022-07-25 19:57:19', '2022-07-25 19:57:19');
INSERT INTO `sys_area` VALUES (110101, 110000, '110101', '东城区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110102, 110000, '110102', '西城区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110105, 110000, '110105', '朝阳区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110106, 110000, '110106', '丰台区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110107, 110000, '110107', '石景山区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110108, 110000, '110108', '海淀区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110109, 110000, '110109', '门头沟区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110111, 110000, '110111', '房山区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110112, 110000, '110112', '通州区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110113, 110000, '110113', '顺义区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110114, 110000, '110114', '昌平区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110115, 110000, '110115', '大兴区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110116, 110000, '110116', '怀柔区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110117, 110000, '110117', '平谷区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110118, 110000, '110118', '密云区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (110119, 110000, '110119', '延庆区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120000, 86, '120000', '天津市', 1, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120101, 120000, '120101', '和平区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120102, 120000, '120102', '河东区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120103, 120000, '120103', '河西区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120104, 120000, '120104', '南开区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120105, 120000, '120105', '河北区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120106, 120000, '120106', '红桥区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120110, 120000, '120110', '东丽区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120111, 120000, '120111', '西青区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120112, 120000, '120112', '津南区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120113, 120000, '120113', '北辰区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120114, 120000, '120114', '武清区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120115, 120000, '120115', '宝坻区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120116, 120000, '120116', '滨海新区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120117, 120000, '120117', '宁河区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120118, 120000, '120118', '静海区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (120119, 120000, '120119', '蓟州区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130000, 86, '130000', '河北省', 1, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130100, 130000, '130100', '石家庄市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130102, 130100, '130102', '长安区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130104, 130100, '130104', '桥西区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130105, 130100, '130105', '新华区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130107, 130100, '130107', '井陉矿区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130108, 130100, '130108', '裕华区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130109, 130100, '130109', '藁城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130110, 130100, '130110', '鹿泉区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130111, 130100, '130111', '栾城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130121, 130100, '130121', '井陉县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130123, 130100, '130123', '正定县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130125, 130100, '130125', '行唐县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130126, 130100, '130126', '灵寿县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130127, 130100, '130127', '高邑县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130128, 130100, '130128', '深泽县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130129, 130100, '130129', '赞皇县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130130, 130100, '130130', '无极县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130131, 130100, '130131', '平山县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130132, 130100, '130132', '元氏县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130133, 130100, '130133', '赵县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130181, 130100, '130181', '辛集市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130183, 130100, '130183', '晋州市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130184, 130100, '130184', '新乐市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130200, 130000, '130200', '唐山市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130202, 130200, '130202', '路南区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130203, 130200, '130203', '路北区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130204, 130200, '130204', '古冶区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130205, 130200, '130205', '开平区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130207, 130200, '130207', '丰南区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130208, 130200, '130208', '丰润区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130209, 130200, '130209', '曹妃甸区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130224, 130200, '130224', '滦南县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130225, 130200, '130225', '乐亭县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130227, 130200, '130227', '迁西县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130229, 130200, '130229', '玉田县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130281, 130200, '130281', '遵化市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130283, 130200, '130283', '迁安市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130284, 130200, '130284', '滦州市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130300, 130000, '130300', '秦皇岛市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130302, 130300, '130302', '海港区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130303, 130300, '130303', '山海关区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130304, 130300, '130304', '北戴河区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130306, 130300, '130306', '抚宁区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130321, 130300, '130321', '青龙满族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130322, 130300, '130322', '昌黎县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130324, 130300, '130324', '卢龙县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130400, 130000, '130400', '邯郸市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130402, 130400, '130402', '邯山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130403, 130400, '130403', '丛台区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130404, 130400, '130404', '复兴区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130406, 130400, '130406', '峰峰矿区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130407, 130400, '130407', '肥乡区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130408, 130400, '130408', '永年区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130423, 130400, '130423', '临漳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130424, 130400, '130424', '成安县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130425, 130400, '130425', '大名县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130426, 130400, '130426', '涉县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130427, 130400, '130427', '磁县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130430, 130400, '130430', '邱县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130431, 130400, '130431', '鸡泽县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130432, 130400, '130432', '广平县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130433, 130400, '130433', '馆陶县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130434, 130400, '130434', '魏县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130435, 130400, '130435', '曲周县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130481, 130400, '130481', '武安市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130500, 130000, '130500', '邢台市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130502, 130500, '130502', '襄都区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130503, 130500, '130503', '信都区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130505, 130500, '130505', '任泽区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130506, 130500, '130506', '南和区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130522, 130500, '130522', '临城县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130523, 130500, '130523', '内丘县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130524, 130500, '130524', '柏乡县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130525, 130500, '130525', '隆尧县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130528, 130500, '130528', '宁晋县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130529, 130500, '130529', '巨鹿县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130530, 130500, '130530', '新河县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130531, 130500, '130531', '广宗县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130532, 130500, '130532', '平乡县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130533, 130500, '130533', '威县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130534, 130500, '130534', '清河县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130535, 130500, '130535', '临西县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130581, 130500, '130581', '南宫市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130582, 130500, '130582', '沙河市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130600, 130000, '130600', '保定市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130602, 130600, '130602', '竞秀区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130606, 130600, '130606', '莲池区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130607, 130600, '130607', '满城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130608, 130600, '130608', '清苑区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130609, 130600, '130609', '徐水区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130623, 130600, '130623', '涞水县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130624, 130600, '130624', '阜平县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130626, 130600, '130626', '定兴县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130627, 130600, '130627', '唐县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130628, 130600, '130628', '高阳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130629, 130600, '130629', '容城县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130630, 130600, '130630', '涞源县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130631, 130600, '130631', '望都县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130632, 130600, '130632', '安新县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130633, 130600, '130633', '易县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130634, 130600, '130634', '曲阳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130635, 130600, '130635', '蠡县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130636, 130600, '130636', '顺平县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130637, 130600, '130637', '博野县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130638, 130600, '130638', '雄县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130681, 130600, '130681', '涿州市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130682, 130600, '130682', '定州市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130683, 130600, '130683', '安国市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130684, 130600, '130684', '高碑店市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130700, 130000, '130700', '张家口市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130702, 130700, '130702', '桥东区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130703, 130700, '130703', '桥西区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130705, 130700, '130705', '宣化区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130706, 130700, '130706', '下花园区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130708, 130700, '130708', '万全区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130709, 130700, '130709', '崇礼区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130722, 130700, '130722', '张北县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130723, 130700, '130723', '康保县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130724, 130700, '130724', '沽源县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130725, 130700, '130725', '尚义县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130726, 130700, '130726', '蔚县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130727, 130700, '130727', '阳原县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130728, 130700, '130728', '怀安县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130730, 130700, '130730', '怀来县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130731, 130700, '130731', '涿鹿县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130732, 130700, '130732', '赤城县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130800, 130000, '130800', '承德市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130802, 130800, '130802', '双桥区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130803, 130800, '130803', '双滦区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130804, 130800, '130804', '鹰手营子矿区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130821, 130800, '130821', '承德县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130822, 130800, '130822', '兴隆县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130824, 130800, '130824', '滦平县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130825, 130800, '130825', '隆化县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130826, 130800, '130826', '丰宁满族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130827, 130800, '130827', '宽城满族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130828, 130800, '130828', '围场满族蒙古族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130881, 130800, '130881', '平泉市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130900, 130000, '130900', '沧州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130902, 130900, '130902', '新华区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130903, 130900, '130903', '运河区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130921, 130900, '130921', '沧县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130922, 130900, '130922', '青县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130923, 130900, '130923', '东光县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130924, 130900, '130924', '海兴县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130925, 130900, '130925', '盐山县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130926, 130900, '130926', '肃宁县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130927, 130900, '130927', '南皮县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130928, 130900, '130928', '吴桥县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130929, 130900, '130929', '献县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130930, 130900, '130930', '孟村回族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130981, 130900, '130981', '泊头市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130982, 130900, '130982', '任丘市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130983, 130900, '130983', '黄骅市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (130984, 130900, '130984', '河间市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131000, 130000, '131000', '廊坊市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131002, 131000, '131002', '安次区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131003, 131000, '131003', '广阳区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131022, 131000, '131022', '固安县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131023, 131000, '131023', '永清县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131024, 131000, '131024', '香河县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131025, 131000, '131025', '大城县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131026, 131000, '131026', '文安县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131028, 131000, '131028', '大厂回族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131081, 131000, '131081', '霸州市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131082, 131000, '131082', '三河市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131100, 130000, '131100', '衡水市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131102, 131100, '131102', '桃城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131103, 131100, '131103', '冀州区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131121, 131100, '131121', '枣强县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131122, 131100, '131122', '武邑县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131123, 131100, '131123', '武强县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131124, 131100, '131124', '饶阳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131125, 131100, '131125', '安平县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131126, 131100, '131126', '故城县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131127, 131100, '131127', '景县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131128, 131100, '131128', '阜城县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (131182, 131100, '131182', '深州市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140000, 86, '140000', '山西省', 1, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140100, 140000, '140100', '太原市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140105, 140100, '140105', '小店区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140106, 140100, '140106', '迎泽区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140107, 140100, '140107', '杏花岭区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140108, 140100, '140108', '尖草坪区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140109, 140100, '140109', '万柏林区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140110, 140100, '140110', '晋源区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140121, 140100, '140121', '清徐县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140122, 140100, '140122', '阳曲县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140123, 140100, '140123', '娄烦县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140181, 140100, '140181', '古交市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140200, 140000, '140200', '大同市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140212, 140200, '140212', '新荣区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140213, 140200, '140213', '平城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140214, 140200, '140214', '云冈区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140215, 140200, '140215', '云州区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140221, 140200, '140221', '阳高县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140222, 140200, '140222', '天镇县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140223, 140200, '140223', '广灵县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140224, 140200, '140224', '灵丘县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140225, 140200, '140225', '浑源县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140226, 140200, '140226', '左云县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140300, 140000, '140300', '阳泉市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140302, 140300, '140302', '城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140303, 140300, '140303', '矿区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140311, 140300, '140311', '郊区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140321, 140300, '140321', '平定县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140322, 140300, '140322', '盂县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140400, 140000, '140400', '长治市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140403, 140400, '140403', '潞州区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140404, 140400, '140404', '上党区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140405, 140400, '140405', '屯留区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140406, 140400, '140406', '潞城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140423, 140400, '140423', '襄垣县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140425, 140400, '140425', '平顺县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140426, 140400, '140426', '黎城县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140427, 140400, '140427', '壶关县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140428, 140400, '140428', '长子县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140429, 140400, '140429', '武乡县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140430, 140400, '140430', '沁县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140431, 140400, '140431', '沁源县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140500, 140000, '140500', '晋城市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140502, 140500, '140502', '城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140521, 140500, '140521', '沁水县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140522, 140500, '140522', '阳城县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140524, 140500, '140524', '陵川县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140525, 140500, '140525', '泽州县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140581, 140500, '140581', '高平市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140600, 140000, '140600', '朔州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140602, 140600, '140602', '朔城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140603, 140600, '140603', '平鲁区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140621, 140600, '140621', '山阴县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140622, 140600, '140622', '应县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140623, 140600, '140623', '右玉县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140681, 140600, '140681', '怀仁市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140700, 140000, '140700', '晋中市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140702, 140700, '140702', '榆次区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140703, 140700, '140703', '太谷区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140721, 140700, '140721', '榆社县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140722, 140700, '140722', '左权县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140723, 140700, '140723', '和顺县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140724, 140700, '140724', '昔阳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140725, 140700, '140725', '寿阳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140727, 140700, '140727', '祁县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140728, 140700, '140728', '平遥县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140729, 140700, '140729', '灵石县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140781, 140700, '140781', '介休市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140800, 140000, '140800', '运城市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140802, 140800, '140802', '盐湖区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140821, 140800, '140821', '临猗县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140822, 140800, '140822', '万荣县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140823, 140800, '140823', '闻喜县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140824, 140800, '140824', '稷山县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140825, 140800, '140825', '新绛县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140826, 140800, '140826', '绛县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140827, 140800, '140827', '垣曲县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140828, 140800, '140828', '夏县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140829, 140800, '140829', '平陆县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140830, 140800, '140830', '芮城县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140881, 140800, '140881', '永济市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140882, 140800, '140882', '河津市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140900, 140000, '140900', '忻州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140902, 140900, '140902', '忻府区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140921, 140900, '140921', '定襄县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140922, 140900, '140922', '五台县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140923, 140900, '140923', '代县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140924, 140900, '140924', '繁峙县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140925, 140900, '140925', '宁武县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140926, 140900, '140926', '静乐县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140927, 140900, '140927', '神池县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140928, 140900, '140928', '五寨县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140929, 140900, '140929', '岢岚县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140930, 140900, '140930', '河曲县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140931, 140900, '140931', '保德县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140932, 140900, '140932', '偏关县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (140981, 140900, '140981', '原平市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141000, 140000, '141000', '临汾市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141002, 141000, '141002', '尧都区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141021, 141000, '141021', '曲沃县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141022, 141000, '141022', '翼城县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141023, 141000, '141023', '襄汾县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141024, 141000, '141024', '洪洞县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141025, 141000, '141025', '古县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141026, 141000, '141026', '安泽县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141027, 141000, '141027', '浮山县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141028, 141000, '141028', '吉县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141029, 141000, '141029', '乡宁县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141030, 141000, '141030', '大宁县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141031, 141000, '141031', '隰县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141032, 141000, '141032', '永和县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141033, 141000, '141033', '蒲县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141034, 141000, '141034', '汾西县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141081, 141000, '141081', '侯马市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141082, 141000, '141082', '霍州市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141100, 140000, '141100', '吕梁市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141102, 141100, '141102', '离石区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141121, 141100, '141121', '文水县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141122, 141100, '141122', '交城县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141123, 141100, '141123', '兴县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141124, 141100, '141124', '临县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141125, 141100, '141125', '柳林县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141126, 141100, '141126', '石楼县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141127, 141100, '141127', '岚县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141128, 141100, '141128', '方山县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141129, 141100, '141129', '中阳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141130, 141100, '141130', '交口县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141181, 141100, '141181', '孝义市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (141182, 141100, '141182', '汾阳市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150000, 86, '150000', '内蒙古自治区', 1, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150100, 150000, '150100', '呼和浩特市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150102, 150100, '150102', '新城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150103, 150100, '150103', '回民区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150104, 150100, '150104', '玉泉区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150105, 150100, '150105', '赛罕区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150121, 150100, '150121', '土默特左旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150122, 150100, '150122', '托克托县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150123, 150100, '150123', '和林格尔县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150124, 150100, '150124', '清水河县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150125, 150100, '150125', '武川县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150200, 150000, '150200', '包头市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150202, 150200, '150202', '东河区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150203, 150200, '150203', '昆都仑区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150204, 150200, '150204', '青山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150205, 150200, '150205', '石拐区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150206, 150200, '150206', '白云鄂博矿区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150207, 150200, '150207', '九原区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150221, 150200, '150221', '土默特右旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150222, 150200, '150222', '固阳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150223, 150200, '150223', '达尔罕茂明安联合旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150300, 150000, '150300', '乌海市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150302, 150300, '150302', '海勃湾区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150303, 150300, '150303', '海南区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150304, 150300, '150304', '乌达区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150400, 150000, '150400', '赤峰市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150402, 150400, '150402', '红山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150403, 150400, '150403', '元宝山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150404, 150400, '150404', '松山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150421, 150400, '150421', '阿鲁科尔沁旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150422, 150400, '150422', '巴林左旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150423, 150400, '150423', '巴林右旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150424, 150400, '150424', '林西县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150425, 150400, '150425', '克什克腾旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150426, 150400, '150426', '翁牛特旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150428, 150400, '150428', '喀喇沁旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150429, 150400, '150429', '宁城县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150430, 150400, '150430', '敖汉旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150500, 150000, '150500', '通辽市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150502, 150500, '150502', '科尔沁区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150521, 150500, '150521', '科尔沁左翼中旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150522, 150500, '150522', '科尔沁左翼后旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150523, 150500, '150523', '开鲁县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150524, 150500, '150524', '库伦旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150525, 150500, '150525', '奈曼旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150526, 150500, '150526', '扎鲁特旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150581, 150500, '150581', '霍林郭勒市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150600, 150000, '150600', '鄂尔多斯市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150602, 150600, '150602', '东胜区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150603, 150600, '150603', '康巴什区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150621, 150600, '150621', '达拉特旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150622, 150600, '150622', '准格尔旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150623, 150600, '150623', '鄂托克前旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150624, 150600, '150624', '鄂托克旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150625, 150600, '150625', '杭锦旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150626, 150600, '150626', '乌审旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150627, 150600, '150627', '伊金霍洛旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150700, 150000, '150700', '呼伦贝尔市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150702, 150700, '150702', '海拉尔区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150703, 150700, '150703', '扎赉诺尔区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150721, 150700, '150721', '阿荣旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150722, 150700, '150722', '莫力达瓦达斡尔族自治旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150723, 150700, '150723', '鄂伦春自治旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150724, 150700, '150724', '鄂温克族自治旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150725, 150700, '150725', '陈巴尔虎旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150726, 150700, '150726', '新巴尔虎左旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150727, 150700, '150727', '新巴尔虎右旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150781, 150700, '150781', '满洲里市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150782, 150700, '150782', '牙克石市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150783, 150700, '150783', '扎兰屯市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150784, 150700, '150784', '额尔古纳市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150785, 150700, '150785', '根河市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150800, 150000, '150800', '巴彦淖尔市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150802, 150800, '150802', '临河区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150821, 150800, '150821', '五原县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150822, 150800, '150822', '磴口县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150823, 150800, '150823', '乌拉特前旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150824, 150800, '150824', '乌拉特中旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150825, 150800, '150825', '乌拉特后旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150826, 150800, '150826', '杭锦后旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150900, 150000, '150900', '乌兰察布市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150902, 150900, '150902', '集宁区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150921, 150900, '150921', '卓资县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150922, 150900, '150922', '化德县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150923, 150900, '150923', '商都县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150924, 150900, '150924', '兴和县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150925, 150900, '150925', '凉城县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150926, 150900, '150926', '察哈尔右翼前旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150927, 150900, '150927', '察哈尔右翼中旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150928, 150900, '150928', '察哈尔右翼后旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150929, 150900, '150929', '四子王旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (150981, 150900, '150981', '丰镇市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152200, 150000, '152200', '兴安盟', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152201, 152200, '152201', '乌兰浩特市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152202, 152200, '152202', '阿尔山市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152221, 152200, '152221', '科尔沁右翼前旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152222, 152200, '152222', '科尔沁右翼中旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152223, 152200, '152223', '扎赉特旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152224, 152200, '152224', '突泉县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152500, 150000, '152500', '锡林郭勒盟', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152501, 152500, '152501', '二连浩特市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152502, 152500, '152502', '锡林浩特市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152522, 152500, '152522', '阿巴嘎旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152523, 152500, '152523', '苏尼特左旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152524, 152500, '152524', '苏尼特右旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152525, 152500, '152525', '东乌珠穆沁旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152526, 152500, '152526', '西乌珠穆沁旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152527, 152500, '152527', '太仆寺旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152528, 152500, '152528', '镶黄旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152529, 152500, '152529', '正镶白旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152530, 152500, '152530', '正蓝旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152531, 152500, '152531', '多伦县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152900, 150000, '152900', '阿拉善盟', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152921, 152900, '152921', '阿拉善左旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152922, 152900, '152922', '阿拉善右旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (152923, 152900, '152923', '额济纳旗', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210000, 86, '210000', '辽宁省', 1, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210100, 210000, '210100', '沈阳市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210102, 210100, '210102', '和平区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210103, 210100, '210103', '沈河区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210104, 210100, '210104', '大东区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210105, 210100, '210105', '皇姑区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210106, 210100, '210106', '铁西区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210111, 210100, '210111', '苏家屯区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210112, 210100, '210112', '浑南区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210113, 210100, '210113', '沈北新区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210114, 210100, '210114', '于洪区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210115, 210100, '210115', '辽中区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210123, 210100, '210123', '康平县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210124, 210100, '210124', '法库县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210181, 210100, '210181', '新民市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210200, 210000, '210200', '大连市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210202, 210200, '210202', '中山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210203, 210200, '210203', '西岗区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210204, 210200, '210204', '沙河口区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210211, 210200, '210211', '甘井子区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210212, 210200, '210212', '旅顺口区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210213, 210200, '210213', '金州区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210214, 210200, '210214', '普兰店区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210224, 210200, '210224', '长海县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210281, 210200, '210281', '瓦房店市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210283, 210200, '210283', '庄河市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210300, 210000, '210300', '鞍山市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210302, 210300, '210302', '铁东区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210303, 210300, '210303', '铁西区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210304, 210300, '210304', '立山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210311, 210300, '210311', '千山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210321, 210300, '210321', '台安县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210323, 210300, '210323', '岫岩满族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210381, 210300, '210381', '海城市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210400, 210000, '210400', '抚顺市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210402, 210400, '210402', '新抚区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210403, 210400, '210403', '东洲区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210404, 210400, '210404', '望花区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210411, 210400, '210411', '顺城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210421, 210400, '210421', '抚顺县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210422, 210400, '210422', '新宾满族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210423, 210400, '210423', '清原满族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210500, 210000, '210500', '本溪市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210502, 210500, '210502', '平山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210503, 210500, '210503', '溪湖区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210504, 210500, '210504', '明山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210505, 210500, '210505', '南芬区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210521, 210500, '210521', '本溪满族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210522, 210500, '210522', '桓仁满族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210600, 210000, '210600', '丹东市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210602, 210600, '210602', '元宝区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210603, 210600, '210603', '振兴区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210604, 210600, '210604', '振安区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210624, 210600, '210624', '宽甸满族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210681, 210600, '210681', '东港市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210682, 210600, '210682', '凤城市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210700, 210000, '210700', '锦州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210702, 210700, '210702', '古塔区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210703, 210700, '210703', '凌河区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210711, 210700, '210711', '太和区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210726, 210700, '210726', '黑山县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210727, 210700, '210727', '义县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210781, 210700, '210781', '凌海市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210782, 210700, '210782', '北镇市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210800, 210000, '210800', '营口市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210802, 210800, '210802', '站前区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210803, 210800, '210803', '西市区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210804, 210800, '210804', '鲅鱼圈区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210811, 210800, '210811', '老边区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210881, 210800, '210881', '盖州市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210882, 210800, '210882', '大石桥市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210900, 210000, '210900', '阜新市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210902, 210900, '210902', '海州区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210903, 210900, '210903', '新邱区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210904, 210900, '210904', '太平区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210905, 210900, '210905', '清河门区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210911, 210900, '210911', '细河区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210921, 210900, '210921', '阜新蒙古族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (210922, 210900, '210922', '彰武县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211000, 210000, '211000', '辽阳市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211002, 211000, '211002', '白塔区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211003, 211000, '211003', '文圣区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211004, 211000, '211004', '宏伟区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211005, 211000, '211005', '弓长岭区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211011, 211000, '211011', '太子河区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211021, 211000, '211021', '辽阳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211081, 211000, '211081', '灯塔市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211100, 210000, '211100', '盘锦市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211102, 211100, '211102', '双台子区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211103, 211100, '211103', '兴隆台区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211104, 211100, '211104', '大洼区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211122, 211100, '211122', '盘山县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211200, 210000, '211200', '铁岭市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211202, 211200, '211202', '银州区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211204, 211200, '211204', '清河区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211221, 211200, '211221', '铁岭县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211223, 211200, '211223', '西丰县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211224, 211200, '211224', '昌图县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211281, 211200, '211281', '调兵山市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211282, 211200, '211282', '开原市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211300, 210000, '211300', '朝阳市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211302, 211300, '211302', '双塔区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211303, 211300, '211303', '龙城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211321, 211300, '211321', '朝阳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211322, 211300, '211322', '建平县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211324, 211300, '211324', '喀喇沁左翼蒙古族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211381, 211300, '211381', '北票市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211382, 211300, '211382', '凌源市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211400, 210000, '211400', '葫芦岛市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211402, 211400, '211402', '连山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211403, 211400, '211403', '龙港区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211404, 211400, '211404', '南票区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211421, 211400, '211421', '绥中县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211422, 211400, '211422', '建昌县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (211481, 211400, '211481', '兴城市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220000, 86, '220000', '吉林省', 1, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220100, 220000, '220100', '长春市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220102, 220100, '220102', '南关区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220103, 220100, '220103', '宽城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220104, 220100, '220104', '朝阳区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220105, 220100, '220105', '二道区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220106, 220100, '220106', '绿园区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220112, 220100, '220112', '双阳区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220113, 220100, '220113', '九台区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220122, 220100, '220122', '农安县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220182, 220100, '220182', '榆树市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220183, 220100, '220183', '德惠市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220184, 220100, '220184', '公主岭市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220200, 220000, '220200', '吉林市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220202, 220200, '220202', '昌邑区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220203, 220200, '220203', '龙潭区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220204, 220200, '220204', '船营区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220211, 220200, '220211', '丰满区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220221, 220200, '220221', '永吉县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220281, 220200, '220281', '蛟河市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220282, 220200, '220282', '桦甸市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220283, 220200, '220283', '舒兰市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220284, 220200, '220284', '磐石市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220300, 220000, '220300', '四平市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220302, 220300, '220302', '铁西区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220303, 220300, '220303', '铁东区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220322, 220300, '220322', '梨树县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220323, 220300, '220323', '伊通满族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220382, 220300, '220382', '双辽市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220400, 220000, '220400', '辽源市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220402, 220400, '220402', '龙山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220403, 220400, '220403', '西安区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220421, 220400, '220421', '东丰县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220422, 220400, '220422', '东辽县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220500, 220000, '220500', '通化市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220502, 220500, '220502', '东昌区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220503, 220500, '220503', '二道江区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220521, 220500, '220521', '通化县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220523, 220500, '220523', '辉南县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220524, 220500, '220524', '柳河县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220581, 220500, '220581', '梅河口市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220582, 220500, '220582', '集安市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220600, 220000, '220600', '白山市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220602, 220600, '220602', '浑江区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220605, 220600, '220605', '江源区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220621, 220600, '220621', '抚松县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220622, 220600, '220622', '靖宇县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220623, 220600, '220623', '长白朝鲜族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220681, 220600, '220681', '临江市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220700, 220000, '220700', '松原市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220702, 220700, '220702', '宁江区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220721, 220700, '220721', '前郭尔罗斯蒙古族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220722, 220700, '220722', '长岭县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220723, 220700, '220723', '乾安县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220781, 220700, '220781', '扶余市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220800, 220000, '220800', '白城市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220802, 220800, '220802', '洮北区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220821, 220800, '220821', '镇赉县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220822, 220800, '220822', '通榆县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220881, 220800, '220881', '洮南市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (220882, 220800, '220882', '大安市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (222400, 220000, '222400', '延边朝鲜族自治州', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (222401, 222400, '222401', '延吉市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (222402, 222400, '222402', '图们市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (222403, 222400, '222403', '敦化市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (222404, 222400, '222404', '珲春市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (222405, 222400, '222405', '龙井市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (222406, 222400, '222406', '和龙市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (222424, 222400, '222424', '汪清县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (222426, 222400, '222426', '安图县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230000, 86, '230000', '黑龙江省', 1, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230100, 230000, '230100', '哈尔滨市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230102, 230100, '230102', '道里区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230103, 230100, '230103', '南岗区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230104, 230100, '230104', '道外区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230108, 230100, '230108', '平房区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230109, 230100, '230109', '松北区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230110, 230100, '230110', '香坊区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230111, 230100, '230111', '呼兰区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230112, 230100, '230112', '阿城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230113, 230100, '230113', '双城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230123, 230100, '230123', '依兰县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230124, 230100, '230124', '方正县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230125, 230100, '230125', '宾县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230126, 230100, '230126', '巴彦县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230127, 230100, '230127', '木兰县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230128, 230100, '230128', '通河县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230129, 230100, '230129', '延寿县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230183, 230100, '230183', '尚志市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230184, 230100, '230184', '五常市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230200, 230000, '230200', '齐齐哈尔市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230202, 230200, '230202', '龙沙区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230203, 230200, '230203', '建华区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230204, 230200, '230204', '铁锋区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230205, 230200, '230205', '昂昂溪区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230206, 230200, '230206', '富拉尔基区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230207, 230200, '230207', '碾子山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230208, 230200, '230208', '梅里斯达斡尔族区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230221, 230200, '230221', '龙江县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230223, 230200, '230223', '依安县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230224, 230200, '230224', '泰来县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230225, 230200, '230225', '甘南县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230227, 230200, '230227', '富裕县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230229, 230200, '230229', '克山县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230230, 230200, '230230', '克东县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230231, 230200, '230231', '拜泉县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230281, 230200, '230281', '讷河市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230300, 230000, '230300', '鸡西市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230302, 230300, '230302', '鸡冠区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230303, 230300, '230303', '恒山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230304, 230300, '230304', '滴道区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230305, 230300, '230305', '梨树区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230306, 230300, '230306', '城子河区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230307, 230300, '230307', '麻山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230321, 230300, '230321', '鸡东县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230381, 230300, '230381', '虎林市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230382, 230300, '230382', '密山市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230400, 230000, '230400', '鹤岗市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230402, 230400, '230402', '向阳区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230403, 230400, '230403', '工农区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230404, 230400, '230404', '南山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230405, 230400, '230405', '兴安区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230406, 230400, '230406', '东山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230407, 230400, '230407', '兴山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230421, 230400, '230421', '萝北县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230422, 230400, '230422', '绥滨县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230500, 230000, '230500', '双鸭山市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230502, 230500, '230502', '尖山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230503, 230500, '230503', '岭东区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230505, 230500, '230505', '四方台区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230506, 230500, '230506', '宝山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230521, 230500, '230521', '集贤县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230522, 230500, '230522', '友谊县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230523, 230500, '230523', '宝清县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230524, 230500, '230524', '饶河县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230600, 230000, '230600', '大庆市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230602, 230600, '230602', '萨尔图区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230603, 230600, '230603', '龙凤区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230604, 230600, '230604', '让胡路区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230605, 230600, '230605', '红岗区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230606, 230600, '230606', '大同区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230621, 230600, '230621', '肇州县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230622, 230600, '230622', '肇源县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230623, 230600, '230623', '林甸县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230624, 230600, '230624', '杜尔伯特蒙古族自治县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230700, 230000, '230700', '伊春市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230717, 230700, '230717', '伊美区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230718, 230700, '230718', '乌翠区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230719, 230700, '230719', '友好区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230722, 230700, '230722', '嘉荫县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230723, 230700, '230723', '汤旺县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230724, 230700, '230724', '丰林县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230725, 230700, '230725', '大箐山县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230726, 230700, '230726', '南岔县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230751, 230700, '230751', '金林区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230781, 230700, '230781', '铁力市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230800, 230000, '230800', '佳木斯市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230803, 230800, '230803', '向阳区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230804, 230800, '230804', '前进区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230805, 230800, '230805', '东风区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230811, 230800, '230811', '郊区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230822, 230800, '230822', '桦南县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230826, 230800, '230826', '桦川县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230828, 230800, '230828', '汤原县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230881, 230800, '230881', '同江市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230882, 230800, '230882', '富锦市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230883, 230800, '230883', '抚远市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230900, 230000, '230900', '七台河市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230902, 230900, '230902', '新兴区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230903, 230900, '230903', '桃山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230904, 230900, '230904', '茄子河区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (230921, 230900, '230921', '勃利县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231000, 230000, '231000', '牡丹江市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231002, 231000, '231002', '东安区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231003, 231000, '231003', '阳明区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231004, 231000, '231004', '爱民区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231005, 231000, '231005', '西安区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231025, 231000, '231025', '林口县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231081, 231000, '231081', '绥芬河市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231083, 231000, '231083', '海林市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231084, 231000, '231084', '宁安市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231085, 231000, '231085', '穆棱市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231086, 231000, '231086', '东宁市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231100, 230000, '231100', '黑河市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231102, 231100, '231102', '爱辉区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231123, 231100, '231123', '逊克县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231124, 231100, '231124', '孙吴县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231181, 231100, '231181', '北安市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231182, 231100, '231182', '五大连池市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231183, 231100, '231183', '嫩江市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231200, 230000, '231200', '绥化市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231202, 231200, '231202', '北林区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231221, 231200, '231221', '望奎县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231222, 231200, '231222', '兰西县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231223, 231200, '231223', '青冈县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231224, 231200, '231224', '庆安县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231225, 231200, '231225', '明水县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231226, 231200, '231226', '绥棱县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231281, 231200, '231281', '安达市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231282, 231200, '231282', '肇东市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (231283, 231200, '231283', '海伦市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (232700, 230000, '232700', '大兴安岭地区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (232701, 232700, '232701', '漠河市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (232721, 232700, '232721', '呼玛县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (232722, 232700, '232722', '塔河县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310000, 86, '310000', '上海市', 1, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310101, 310000, '310101', '黄浦区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310104, 310000, '310104', '徐汇区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310105, 310000, '310105', '长宁区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310106, 310000, '310106', '静安区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310107, 310000, '310107', '普陀区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310109, 310000, '310109', '虹口区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310110, 310000, '310110', '杨浦区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310112, 310000, '310112', '闵行区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310113, 310000, '310113', '宝山区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310114, 310000, '310114', '嘉定区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310115, 310000, '310115', '浦东新区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310116, 310000, '310116', '金山区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310117, 310000, '310117', '松江区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310118, 310000, '310118', '青浦区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310120, 310000, '310120', '奉贤区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (310151, 310000, '310151', '崇明区', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320000, 86, '320000', '江苏省', 1, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320100, 320000, '320100', '南京市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320102, 320100, '320102', '玄武区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320104, 320100, '320104', '秦淮区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320105, 320100, '320105', '建邺区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320106, 320100, '320106', '鼓楼区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320111, 320100, '320111', '浦口区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320113, 320100, '320113', '栖霞区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320114, 320100, '320114', '雨花台区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320115, 320100, '320115', '江宁区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320116, 320100, '320116', '六合区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320117, 320100, '320117', '溧水区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320118, 320100, '320118', '高淳区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320200, 320000, '320200', '无锡市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320205, 320200, '320205', '锡山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320206, 320200, '320206', '惠山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320211, 320200, '320211', '滨湖区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320213, 320200, '320213', '梁溪区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320214, 320200, '320214', '新吴区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320281, 320200, '320281', '江阴市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320282, 320200, '320282', '宜兴市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320300, 320000, '320300', '徐州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320302, 320300, '320302', '鼓楼区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320303, 320300, '320303', '云龙区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320305, 320300, '320305', '贾汪区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320311, 320300, '320311', '泉山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320312, 320300, '320312', '铜山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320321, 320300, '320321', '丰县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320322, 320300, '320322', '沛县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320324, 320300, '320324', '睢宁县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320381, 320300, '320381', '新沂市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320382, 320300, '320382', '邳州市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320400, 320000, '320400', '常州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320402, 320400, '320402', '天宁区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320404, 320400, '320404', '钟楼区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320411, 320400, '320411', '新北区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320412, 320400, '320412', '武进区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320413, 320400, '320413', '金坛区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320481, 320400, '320481', '溧阳市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320500, 320000, '320500', '苏州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320505, 320500, '320505', '虎丘区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320506, 320500, '320506', '吴中区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320507, 320500, '320507', '相城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320508, 320500, '320508', '姑苏区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320509, 320500, '320509', '吴江区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320581, 320500, '320581', '常熟市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320582, 320500, '320582', '张家港市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320583, 320500, '320583', '昆山市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320585, 320500, '320585', '太仓市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320600, 320000, '320600', '南通市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320602, 320600, '320602', '崇川区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320611, 320600, '320611', '港闸区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320612, 320600, '320612', '通州区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320623, 320600, '320623', '如东县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320681, 320600, '320681', '启东市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320682, 320600, '320682', '如皋市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320684, 320600, '320684', '海门市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320685, 320600, '320685', '海安市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320700, 320000, '320700', '连云港市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320703, 320700, '320703', '连云区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320706, 320700, '320706', '海州区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320707, 320700, '320707', '赣榆区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320722, 320700, '320722', '东海县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320723, 320700, '320723', '灌云县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320724, 320700, '320724', '灌南县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320800, 320000, '320800', '淮安市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320803, 320800, '320803', '淮安区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320804, 320800, '320804', '淮阴区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320812, 320800, '320812', '清江浦区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320813, 320800, '320813', '洪泽区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320826, 320800, '320826', '涟水县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320830, 320800, '320830', '盱眙县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320831, 320800, '320831', '金湖县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320900, 320000, '320900', '盐城市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320902, 320900, '320902', '亭湖区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320903, 320900, '320903', '盐都区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320904, 320900, '320904', '大丰区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320921, 320900, '320921', '响水县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320922, 320900, '320922', '滨海县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320923, 320900, '320923', '阜宁县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320924, 320900, '320924', '射阳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320925, 320900, '320925', '建湖县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (320981, 320900, '320981', '东台市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321000, 320000, '321000', '扬州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321002, 321000, '321002', '广陵区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321003, 321000, '321003', '邗江区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321012, 321000, '321012', '江都区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321023, 321000, '321023', '宝应县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321081, 321000, '321081', '仪征市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321084, 321000, '321084', '高邮市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321100, 320000, '321100', '镇江市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321102, 321100, '321102', '京口区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321111, 321100, '321111', '润州区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321112, 321100, '321112', '丹徒区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321181, 321100, '321181', '丹阳市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321182, 321100, '321182', '扬中市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321183, 321100, '321183', '句容市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321200, 320000, '321200', '泰州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321202, 321200, '321202', '海陵区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321203, 321200, '321203', '高港区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321204, 321200, '321204', '姜堰区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321281, 321200, '321281', '兴化市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321282, 321200, '321282', '靖江市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321283, 321200, '321283', '泰兴市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321300, 320000, '321300', '宿迁市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321302, 321300, '321302', '宿城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321311, 321300, '321311', '宿豫区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321322, 321300, '321322', '沭阳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321323, 321300, '321323', '泗阳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (321324, 321300, '321324', '泗洪县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330000, 86, '330000', '浙江省', 1, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330100, 330000, '330100', '杭州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330102, 330100, '330102', '上城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330103, 330100, '330103', '下城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330104, 330100, '330104', '江干区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330105, 330100, '330105', '拱墅区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330106, 330100, '330106', '西湖区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330108, 330100, '330108', '滨江区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330109, 330100, '330109', '萧山区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330110, 330100, '330110', '余杭区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330111, 330100, '330111', '富阳区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330112, 330100, '330112', '临安区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330122, 330100, '330122', '桐庐县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330127, 330100, '330127', '淳安县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330182, 330100, '330182', '建德市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330200, 330000, '330200', '宁波市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330203, 330200, '330203', '海曙区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330205, 330200, '330205', '江北区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330206, 330200, '330206', '北仑区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330211, 330200, '330211', '镇海区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330212, 330200, '330212', '鄞州区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330213, 330200, '330213', '奉化区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330225, 330200, '330225', '象山县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330226, 330200, '330226', '宁海县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330281, 330200, '330281', '余姚市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330282, 330200, '330282', '慈溪市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330300, 330000, '330300', '温州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330302, 330300, '330302', '鹿城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330303, 330300, '330303', '龙湾区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330304, 330300, '330304', '瓯海区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330305, 330300, '330305', '洞头区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330324, 330300, '330324', '永嘉县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330326, 330300, '330326', '平阳县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330327, 330300, '330327', '苍南县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330328, 330300, '330328', '文成县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330329, 330300, '330329', '泰顺县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330381, 330300, '330381', '瑞安市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330382, 330300, '330382', '乐清市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330383, 330300, '330383', '龙港市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330400, 330000, '330400', '嘉兴市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330402, 330400, '330402', '南湖区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330411, 330400, '330411', '秀洲区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330421, 330400, '330421', '嘉善县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330424, 330400, '330424', '海盐县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330481, 330400, '330481', '海宁市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330482, 330400, '330482', '平湖市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330483, 330400, '330483', '桐乡市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330500, 330000, '330500', '湖州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330502, 330500, '330502', '吴兴区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330503, 330500, '330503', '南浔区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330521, 330500, '330521', '德清县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330522, 330500, '330522', '长兴县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330523, 330500, '330523', '安吉县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330600, 330000, '330600', '绍兴市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330602, 330600, '330602', '越城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330603, 330600, '330603', '柯桥区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330604, 330600, '330604', '上虞区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330624, 330600, '330624', '新昌县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330681, 330600, '330681', '诸暨市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330683, 330600, '330683', '嵊州市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330700, 330000, '330700', '金华市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330702, 330700, '330702', '婺城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330703, 330700, '330703', '金东区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330723, 330700, '330723', '武义县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330726, 330700, '330726', '浦江县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330727, 330700, '330727', '磐安县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330781, 330700, '330781', '兰溪市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330782, 330700, '330782', '义乌市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330783, 330700, '330783', '东阳市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330784, 330700, '330784', '永康市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330800, 330000, '330800', '衢州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330802, 330800, '330802', '柯城区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330803, 330800, '330803', '衢江区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330822, 330800, '330822', '常山县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330824, 330800, '330824', '开化县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330825, 330800, '330825', '龙游县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330881, 330800, '330881', '江山市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330900, 330000, '330900', '舟山市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330902, 330900, '330902', '定海区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330903, 330900, '330903', '普陀区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330921, 330900, '330921', '岱山县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (330922, 330900, '330922', '嵊泗县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331000, 330000, '331000', '台州市', 2, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331002, 331000, '331002', '椒江区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331003, 331000, '331003', '黄岩区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331004, 331000, '331004', '路桥区', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331022, 331000, '331022', '三门县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331023, 331000, '331023', '天台县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331024, 331000, '331024', '仙居县', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331081, 331000, '331081', '温岭市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331082, 331000, '331082', '临海市', 3, '0', 0, 1, '2020-12-28 17:43:30', 1, '2020-12-28 17:43:30', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331083, 331000, '331083', '玉环市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331100, 330000, '331100', '丽水市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331102, 331100, '331102', '莲都区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331121, 331100, '331121', '青田县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331122, 331100, '331122', '缙云县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331123, 331100, '331123', '遂昌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331124, 331100, '331124', '松阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331125, 331100, '331125', '云和县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331126, 331100, '331126', '庆元县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331127, 331100, '331127', '景宁畲族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (331181, 331100, '331181', '龙泉市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340000, 86, '340000', '安徽省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340100, 340000, '340100', '合肥市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340102, 340100, '340102', '瑶海区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340103, 340100, '340103', '庐阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340104, 340100, '340104', '蜀山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340111, 340100, '340111', '包河区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340121, 340100, '340121', '长丰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340122, 340100, '340122', '肥东县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340123, 340100, '340123', '肥西县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340124, 340100, '340124', '庐江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340181, 340100, '340181', '巢湖市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340200, 340000, '340200', '芜湖市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340202, 340200, '340202', '镜湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340203, 340200, '340203', '弋江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340207, 340200, '340207', '鸠江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340208, 340200, '340208', '三山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340221, 340200, '340221', '芜湖县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340222, 340200, '340222', '繁昌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340223, 340200, '340223', '南陵县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340281, 340200, '340281', '无为市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340300, 340000, '340300', '蚌埠市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340302, 340300, '340302', '龙子湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340303, 340300, '340303', '蚌山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340304, 340300, '340304', '禹会区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340311, 340300, '340311', '淮上区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340321, 340300, '340321', '怀远县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340322, 340300, '340322', '五河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340323, 340300, '340323', '固镇县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340400, 340000, '340400', '淮南市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340402, 340400, '340402', '大通区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340403, 340400, '340403', '田家庵区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340404, 340400, '340404', '谢家集区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340405, 340400, '340405', '八公山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340406, 340400, '340406', '潘集区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340421, 340400, '340421', '凤台县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340422, 340400, '340422', '寿县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340500, 340000, '340500', '马鞍山市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340503, 340500, '340503', '花山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340504, 340500, '340504', '雨山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340506, 340500, '340506', '博望区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340521, 340500, '340521', '当涂县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340522, 340500, '340522', '含山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340523, 340500, '340523', '和县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340600, 340000, '340600', '淮北市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340602, 340600, '340602', '杜集区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340603, 340600, '340603', '相山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340604, 340600, '340604', '烈山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340621, 340600, '340621', '濉溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340700, 340000, '340700', '铜陵市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340705, 340700, '340705', '铜官区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340706, 340700, '340706', '义安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340711, 340700, '340711', '郊区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340722, 340700, '340722', '枞阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340800, 340000, '340800', '安庆市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340802, 340800, '340802', '迎江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340803, 340800, '340803', '大观区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340811, 340800, '340811', '宜秀区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340822, 340800, '340822', '怀宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340825, 340800, '340825', '太湖县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340826, 340800, '340826', '宿松县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340827, 340800, '340827', '望江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340828, 340800, '340828', '岳西县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340881, 340800, '340881', '桐城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (340882, 340800, '340882', '潜山市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341000, 340000, '341000', '黄山市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341002, 341000, '341002', '屯溪区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341003, 341000, '341003', '黄山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341004, 341000, '341004', '徽州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341021, 341000, '341021', '歙县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341022, 341000, '341022', '休宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341023, 341000, '341023', '黟县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341024, 341000, '341024', '祁门县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341100, 340000, '341100', '滁州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341102, 341100, '341102', '琅琊区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341103, 341100, '341103', '南谯区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341122, 341100, '341122', '来安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341124, 341100, '341124', '全椒县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341125, 341100, '341125', '定远县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341126, 341100, '341126', '凤阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341181, 341100, '341181', '天长市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341182, 341100, '341182', '明光市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341200, 340000, '341200', '阜阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341202, 341200, '341202', '颍州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341203, 341200, '341203', '颍东区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341204, 341200, '341204', '颍泉区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341221, 341200, '341221', '临泉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341222, 341200, '341222', '太和县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341225, 341200, '341225', '阜南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341226, 341200, '341226', '颍上县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341282, 341200, '341282', '界首市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341300, 340000, '341300', '宿州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341302, 341300, '341302', '埇桥区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341321, 341300, '341321', '砀山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341322, 341300, '341322', '萧县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341323, 341300, '341323', '灵璧县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341324, 341300, '341324', '泗县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341500, 340000, '341500', '六安市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341502, 341500, '341502', '金安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341503, 341500, '341503', '裕安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341504, 341500, '341504', '叶集区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341522, 341500, '341522', '霍邱县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341523, 341500, '341523', '舒城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341524, 341500, '341524', '金寨县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341525, 341500, '341525', '霍山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341600, 340000, '341600', '亳州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341602, 341600, '341602', '谯城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341621, 341600, '341621', '涡阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341622, 341600, '341622', '蒙城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341623, 341600, '341623', '利辛县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341700, 340000, '341700', '池州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341702, 341700, '341702', '贵池区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341721, 341700, '341721', '东至县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341722, 341700, '341722', '石台县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341723, 341700, '341723', '青阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341800, 340000, '341800', '宣城市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341802, 341800, '341802', '宣州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341821, 341800, '341821', '郎溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341823, 341800, '341823', '泾县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341824, 341800, '341824', '绩溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341825, 341800, '341825', '旌德县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341881, 341800, '341881', '宁国市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (341882, 341800, '341882', '广德市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350000, 86, '350000', '福建省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350100, 350000, '350100', '福州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350102, 350100, '350102', '鼓楼区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350103, 350100, '350103', '台江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350104, 350100, '350104', '仓山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350105, 350100, '350105', '马尾区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350111, 350100, '350111', '晋安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350112, 350100, '350112', '长乐区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350121, 350100, '350121', '闽侯县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350122, 350100, '350122', '连江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350123, 350100, '350123', '罗源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350124, 350100, '350124', '闽清县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350125, 350100, '350125', '永泰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350128, 350100, '350128', '平潭县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350181, 350100, '350181', '福清市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350200, 350000, '350200', '厦门市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350203, 350200, '350203', '思明区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350205, 350200, '350205', '海沧区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350206, 350200, '350206', '湖里区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350211, 350200, '350211', '集美区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350212, 350200, '350212', '同安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350213, 350200, '350213', '翔安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350300, 350000, '350300', '莆田市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350302, 350300, '350302', '城厢区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350303, 350300, '350303', '涵江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350304, 350300, '350304', '荔城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350305, 350300, '350305', '秀屿区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350322, 350300, '350322', '仙游县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350400, 350000, '350400', '三明市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350402, 350400, '350402', '梅列区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350403, 350400, '350403', '三元区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350421, 350400, '350421', '明溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350423, 350400, '350423', '清流县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350424, 350400, '350424', '宁化县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350425, 350400, '350425', '大田县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350426, 350400, '350426', '尤溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350427, 350400, '350427', '沙县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350428, 350400, '350428', '将乐县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350429, 350400, '350429', '泰宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350430, 350400, '350430', '建宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350481, 350400, '350481', '永安市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350500, 350000, '350500', '泉州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350502, 350500, '350502', '鲤城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350503, 350500, '350503', '丰泽区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350504, 350500, '350504', '洛江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350505, 350500, '350505', '泉港区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350521, 350500, '350521', '惠安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350524, 350500, '350524', '安溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350525, 350500, '350525', '永春县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350526, 350500, '350526', '德化县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350527, 350500, '350527', '金门县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350581, 350500, '350581', '石狮市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350582, 350500, '350582', '晋江市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350583, 350500, '350583', '南安市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350600, 350000, '350600', '漳州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350602, 350600, '350602', '芗城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350603, 350600, '350603', '龙文区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350622, 350600, '350622', '云霄县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350623, 350600, '350623', '漳浦县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350624, 350600, '350624', '诏安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350625, 350600, '350625', '长泰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350626, 350600, '350626', '东山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350627, 350600, '350627', '南靖县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350628, 350600, '350628', '平和县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350629, 350600, '350629', '华安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350681, 350600, '350681', '龙海市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350700, 350000, '350700', '南平市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350702, 350700, '350702', '延平区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350703, 350700, '350703', '建阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350721, 350700, '350721', '顺昌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350722, 350700, '350722', '浦城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350723, 350700, '350723', '光泽县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350724, 350700, '350724', '松溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350725, 350700, '350725', '政和县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350781, 350700, '350781', '邵武市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350782, 350700, '350782', '武夷山市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350783, 350700, '350783', '建瓯市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350800, 350000, '350800', '龙岩市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350802, 350800, '350802', '新罗区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350803, 350800, '350803', '永定区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350821, 350800, '350821', '长汀县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350823, 350800, '350823', '上杭县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350824, 350800, '350824', '武平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350825, 350800, '350825', '连城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350881, 350800, '350881', '漳平市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350900, 350000, '350900', '宁德市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350902, 350900, '350902', '蕉城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350921, 350900, '350921', '霞浦县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350922, 350900, '350922', '古田县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350923, 350900, '350923', '屏南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350924, 350900, '350924', '寿宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350925, 350900, '350925', '周宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350926, 350900, '350926', '柘荣县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350981, 350900, '350981', '福安市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (350982, 350900, '350982', '福鼎市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360000, 86, '360000', '江西省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360100, 360000, '360100', '南昌市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360102, 360100, '360102', '东湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360103, 360100, '360103', '西湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360104, 360100, '360104', '青云谱区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360111, 360100, '360111', '青山湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360112, 360100, '360112', '新建区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360113, 360100, '360113', '红谷滩区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360121, 360100, '360121', '南昌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360123, 360100, '360123', '安义县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360124, 360100, '360124', '进贤县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360200, 360000, '360200', '景德镇市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360202, 360200, '360202', '昌江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360203, 360200, '360203', '珠山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360222, 360200, '360222', '浮梁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360281, 360200, '360281', '乐平市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360300, 360000, '360300', '萍乡市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360302, 360300, '360302', '安源区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360313, 360300, '360313', '湘东区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360321, 360300, '360321', '莲花县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360322, 360300, '360322', '上栗县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360323, 360300, '360323', '芦溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360400, 360000, '360400', '九江市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360402, 360400, '360402', '濂溪区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360403, 360400, '360403', '浔阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360404, 360400, '360404', '柴桑区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360423, 360400, '360423', '武宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360424, 360400, '360424', '修水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360425, 360400, '360425', '永修县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360426, 360400, '360426', '德安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360428, 360400, '360428', '都昌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360429, 360400, '360429', '湖口县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360430, 360400, '360430', '彭泽县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360481, 360400, '360481', '瑞昌市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360482, 360400, '360482', '共青城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360483, 360400, '360483', '庐山市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360500, 360000, '360500', '新余市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360502, 360500, '360502', '渝水区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360521, 360500, '360521', '分宜县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360600, 360000, '360600', '鹰潭市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360602, 360600, '360602', '月湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360603, 360600, '360603', '余江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360681, 360600, '360681', '贵溪市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360700, 360000, '360700', '赣州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360702, 360700, '360702', '章贡区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360703, 360700, '360703', '南康区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360704, 360700, '360704', '赣县区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360722, 360700, '360722', '信丰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360723, 360700, '360723', '大余县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360724, 360700, '360724', '上犹县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360725, 360700, '360725', '崇义县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360726, 360700, '360726', '安远县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360728, 360700, '360728', '定南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360729, 360700, '360729', '全南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360730, 360700, '360730', '宁都县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360731, 360700, '360731', '于都县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360732, 360700, '360732', '兴国县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360733, 360700, '360733', '会昌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360734, 360700, '360734', '寻乌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360735, 360700, '360735', '石城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360781, 360700, '360781', '瑞金市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360783, 360700, '360783', '龙南市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360800, 360000, '360800', '吉安市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360802, 360800, '360802', '吉州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360803, 360800, '360803', '青原区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360821, 360800, '360821', '吉安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360822, 360800, '360822', '吉水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360823, 360800, '360823', '峡江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360824, 360800, '360824', '新干县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360825, 360800, '360825', '永丰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360826, 360800, '360826', '泰和县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360827, 360800, '360827', '遂川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360828, 360800, '360828', '万安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360829, 360800, '360829', '安福县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360830, 360800, '360830', '永新县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360881, 360800, '360881', '井冈山市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360900, 360000, '360900', '宜春市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360902, 360900, '360902', '袁州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360921, 360900, '360921', '奉新县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360922, 360900, '360922', '万载县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360923, 360900, '360923', '上高县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360924, 360900, '360924', '宜丰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360925, 360900, '360925', '靖安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360926, 360900, '360926', '铜鼓县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360981, 360900, '360981', '丰城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360982, 360900, '360982', '樟树市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (360983, 360900, '360983', '高安市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361000, 360000, '361000', '抚州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361002, 361000, '361002', '临川区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361003, 361000, '361003', '东乡区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361021, 361000, '361021', '南城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361022, 361000, '361022', '黎川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361023, 361000, '361023', '南丰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361024, 361000, '361024', '崇仁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361025, 361000, '361025', '乐安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361026, 361000, '361026', '宜黄县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361027, 361000, '361027', '金溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361028, 361000, '361028', '资溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361030, 361000, '361030', '广昌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361100, 360000, '361100', '上饶市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361102, 361100, '361102', '信州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361103, 361100, '361103', '广丰区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361104, 361100, '361104', '广信区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361123, 361100, '361123', '玉山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361124, 361100, '361124', '铅山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361125, 361100, '361125', '横峰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361126, 361100, '361126', '弋阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361127, 361100, '361127', '余干县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361128, 361100, '361128', '鄱阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361129, 361100, '361129', '万年县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361130, 361100, '361130', '婺源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (361181, 361100, '361181', '德兴市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370000, 86, '370000', '山东省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370100, 370000, '370100', '济南市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370102, 370100, '370102', '历下区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370103, 370100, '370103', '市中区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370104, 370100, '370104', '槐荫区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370105, 370100, '370105', '天桥区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370112, 370100, '370112', '历城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370113, 370100, '370113', '长清区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370114, 370100, '370114', '章丘区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370115, 370100, '370115', '济阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370116, 370100, '370116', '莱芜区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370117, 370100, '370117', '钢城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370124, 370100, '370124', '平阴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370126, 370100, '370126', '商河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370200, 370000, '370200', '青岛市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370202, 370200, '370202', '市南区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370203, 370200, '370203', '市北区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370211, 370200, '370211', '黄岛区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370212, 370200, '370212', '崂山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370213, 370200, '370213', '李沧区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370214, 370200, '370214', '城阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370215, 370200, '370215', '即墨区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370281, 370200, '370281', '胶州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370283, 370200, '370283', '平度市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370285, 370200, '370285', '莱西市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370300, 370000, '370300', '淄博市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370302, 370300, '370302', '淄川区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370303, 370300, '370303', '张店区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370304, 370300, '370304', '博山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370305, 370300, '370305', '临淄区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370306, 370300, '370306', '周村区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370321, 370300, '370321', '桓台县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370322, 370300, '370322', '高青县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370323, 370300, '370323', '沂源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370400, 370000, '370400', '枣庄市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370402, 370400, '370402', '市中区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370403, 370400, '370403', '薛城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370404, 370400, '370404', '峄城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370405, 370400, '370405', '台儿庄区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370406, 370400, '370406', '山亭区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370481, 370400, '370481', '滕州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370500, 370000, '370500', '东营市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370502, 370500, '370502', '东营区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370503, 370500, '370503', '河口区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370505, 370500, '370505', '垦利区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370522, 370500, '370522', '利津县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370523, 370500, '370523', '广饶县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370600, 370000, '370600', '烟台市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370602, 370600, '370602', '芝罘区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370611, 370600, '370611', '福山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370612, 370600, '370612', '牟平区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370613, 370600, '370613', '莱山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370634, 370600, '370634', '长岛县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370681, 370600, '370681', '龙口市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370682, 370600, '370682', '莱阳市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370683, 370600, '370683', '莱州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370684, 370600, '370684', '蓬莱市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370685, 370600, '370685', '招远市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370686, 370600, '370686', '栖霞市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370687, 370600, '370687', '海阳市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370700, 370000, '370700', '潍坊市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370702, 370700, '370702', '潍城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370703, 370700, '370703', '寒亭区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370704, 370700, '370704', '坊子区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370705, 370700, '370705', '奎文区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370724, 370700, '370724', '临朐县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370725, 370700, '370725', '昌乐县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370781, 370700, '370781', '青州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370782, 370700, '370782', '诸城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370783, 370700, '370783', '寿光市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370784, 370700, '370784', '安丘市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370785, 370700, '370785', '高密市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370786, 370700, '370786', '昌邑市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370800, 370000, '370800', '济宁市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370811, 370800, '370811', '任城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370812, 370800, '370812', '兖州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370826, 370800, '370826', '微山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370827, 370800, '370827', '鱼台县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370828, 370800, '370828', '金乡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370829, 370800, '370829', '嘉祥县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370830, 370800, '370830', '汶上县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370831, 370800, '370831', '泗水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370832, 370800, '370832', '梁山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370881, 370800, '370881', '曲阜市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370883, 370800, '370883', '邹城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370900, 370000, '370900', '泰安市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370902, 370900, '370902', '泰山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370911, 370900, '370911', '岱岳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370921, 370900, '370921', '宁阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370923, 370900, '370923', '东平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370982, 370900, '370982', '新泰市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (370983, 370900, '370983', '肥城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371000, 370000, '371000', '威海市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371002, 371000, '371002', '环翠区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371003, 371000, '371003', '文登区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371082, 371000, '371082', '荣成市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371083, 371000, '371083', '乳山市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371100, 370000, '371100', '日照市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371102, 371100, '371102', '东港区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371103, 371100, '371103', '岚山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371121, 371100, '371121', '五莲县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371122, 371100, '371122', '莒县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371300, 370000, '371300', '临沂市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371302, 371300, '371302', '兰山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371311, 371300, '371311', '罗庄区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371312, 371300, '371312', '河东区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371321, 371300, '371321', '沂南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371322, 371300, '371322', '郯城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371323, 371300, '371323', '沂水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371324, 371300, '371324', '兰陵县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371325, 371300, '371325', '费县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371326, 371300, '371326', '平邑县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371327, 371300, '371327', '莒南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371328, 371300, '371328', '蒙阴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371329, 371300, '371329', '临沭县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371400, 370000, '371400', '德州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371402, 371400, '371402', '德城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371403, 371400, '371403', '陵城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371422, 371400, '371422', '宁津县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371423, 371400, '371423', '庆云县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371424, 371400, '371424', '临邑县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371425, 371400, '371425', '齐河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371426, 371400, '371426', '平原县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371427, 371400, '371427', '夏津县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371428, 371400, '371428', '武城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371481, 371400, '371481', '乐陵市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371482, 371400, '371482', '禹城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371500, 370000, '371500', '聊城市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371502, 371500, '371502', '东昌府区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371503, 371500, '371503', '茌平区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371521, 371500, '371521', '阳谷县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371522, 371500, '371522', '莘县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371524, 371500, '371524', '东阿县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371525, 371500, '371525', '冠县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371526, 371500, '371526', '高唐县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371581, 371500, '371581', '临清市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371600, 370000, '371600', '滨州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371602, 371600, '371602', '滨城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371603, 371600, '371603', '沾化区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371621, 371600, '371621', '惠民县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371622, 371600, '371622', '阳信县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371623, 371600, '371623', '无棣县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371625, 371600, '371625', '博兴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371681, 371600, '371681', '邹平市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371700, 370000, '371700', '菏泽市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371702, 371700, '371702', '牡丹区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371703, 371700, '371703', '定陶区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371721, 371700, '371721', '曹县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371722, 371700, '371722', '单县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371723, 371700, '371723', '成武县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371724, 371700, '371724', '巨野县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371725, 371700, '371725', '郓城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371726, 371700, '371726', '鄄城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (371728, 371700, '371728', '东明县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410000, 86, '410000', '河南省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410100, 410000, '410100', '郑州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410102, 410100, '410102', '中原区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410103, 410100, '410103', '二七区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410104, 410100, '410104', '管城回族区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410105, 410100, '410105', '金水区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410106, 410100, '410106', '上街区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410108, 410100, '410108', '惠济区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410122, 410100, '410122', '中牟县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410181, 410100, '410181', '巩义市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410182, 410100, '410182', '荥阳市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410183, 410100, '410183', '新密市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410184, 410100, '410184', '新郑市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410185, 410100, '410185', '登封市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410200, 410000, '410200', '开封市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410202, 410200, '410202', '龙亭区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410203, 410200, '410203', '顺河回族区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410204, 410200, '410204', '鼓楼区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410205, 410200, '410205', '禹王台区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410212, 410200, '410212', '祥符区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410221, 410200, '410221', '杞县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410222, 410200, '410222', '通许县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410223, 410200, '410223', '尉氏县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410225, 410200, '410225', '兰考县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410300, 410000, '410300', '洛阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410302, 410300, '410302', '老城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410303, 410300, '410303', '西工区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410304, 410300, '410304', '瀍河回族区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410305, 410300, '410305', '涧西区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410306, 410300, '410306', '吉利区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410311, 410300, '410311', '洛龙区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410322, 410300, '410322', '孟津县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410323, 410300, '410323', '新安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410324, 410300, '410324', '栾川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410325, 410300, '410325', '嵩县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410326, 410300, '410326', '汝阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410327, 410300, '410327', '宜阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410328, 410300, '410328', '洛宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410329, 410300, '410329', '伊川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410381, 410300, '410381', '偃师市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410400, 410000, '410400', '平顶山市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410402, 410400, '410402', '新华区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410403, 410400, '410403', '卫东区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410404, 410400, '410404', '石龙区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410411, 410400, '410411', '湛河区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410421, 410400, '410421', '宝丰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410422, 410400, '410422', '叶县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410423, 410400, '410423', '鲁山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410425, 410400, '410425', '郏县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410481, 410400, '410481', '舞钢市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410482, 410400, '410482', '汝州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410500, 410000, '410500', '安阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410502, 410500, '410502', '文峰区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410503, 410500, '410503', '北关区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410505, 410500, '410505', '殷都区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410506, 410500, '410506', '龙安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410522, 410500, '410522', '安阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410523, 410500, '410523', '汤阴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410526, 410500, '410526', '滑县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410527, 410500, '410527', '内黄县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410581, 410500, '410581', '林州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410600, 410000, '410600', '鹤壁市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410602, 410600, '410602', '鹤山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410603, 410600, '410603', '山城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410611, 410600, '410611', '淇滨区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410621, 410600, '410621', '浚县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410622, 410600, '410622', '淇县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410700, 410000, '410700', '新乡市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410702, 410700, '410702', '红旗区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410703, 410700, '410703', '卫滨区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410704, 410700, '410704', '凤泉区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410711, 410700, '410711', '牧野区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410721, 410700, '410721', '新乡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410724, 410700, '410724', '获嘉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410725, 410700, '410725', '原阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410726, 410700, '410726', '延津县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410727, 410700, '410727', '封丘县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410781, 410700, '410781', '卫辉市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410782, 410700, '410782', '辉县市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410783, 410700, '410783', '长垣市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410800, 410000, '410800', '焦作市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410802, 410800, '410802', '解放区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410803, 410800, '410803', '中站区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410804, 410800, '410804', '马村区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410811, 410800, '410811', '山阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410821, 410800, '410821', '修武县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410822, 410800, '410822', '博爱县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410823, 410800, '410823', '武陟县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410825, 410800, '410825', '温县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410882, 410800, '410882', '沁阳市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410883, 410800, '410883', '孟州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410900, 410000, '410900', '濮阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410902, 410900, '410902', '华龙区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410922, 410900, '410922', '清丰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410923, 410900, '410923', '南乐县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410926, 410900, '410926', '范县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410927, 410900, '410927', '台前县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (410928, 410900, '410928', '濮阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411000, 410000, '411000', '许昌市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411002, 411000, '411002', '魏都区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411003, 411000, '411003', '建安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411024, 411000, '411024', '鄢陵县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411025, 411000, '411025', '襄城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411081, 411000, '411081', '禹州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411082, 411000, '411082', '长葛市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411100, 410000, '411100', '漯河市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411102, 411100, '411102', '源汇区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411103, 411100, '411103', '郾城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411104, 411100, '411104', '召陵区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411121, 411100, '411121', '舞阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411122, 411100, '411122', '临颍县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411200, 410000, '411200', '三门峡市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411202, 411200, '411202', '湖滨区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411203, 411200, '411203', '陕州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411221, 411200, '411221', '渑池县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411224, 411200, '411224', '卢氏县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411281, 411200, '411281', '义马市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411282, 411200, '411282', '灵宝市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411300, 410000, '411300', '南阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411302, 411300, '411302', '宛城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411303, 411300, '411303', '卧龙区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411321, 411300, '411321', '南召县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411322, 411300, '411322', '方城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411323, 411300, '411323', '西峡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411324, 411300, '411324', '镇平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411325, 411300, '411325', '内乡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411326, 411300, '411326', '淅川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411327, 411300, '411327', '社旗县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411328, 411300, '411328', '唐河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411329, 411300, '411329', '新野县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411330, 411300, '411330', '桐柏县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411381, 411300, '411381', '邓州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411400, 410000, '411400', '商丘市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411402, 411400, '411402', '梁园区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411403, 411400, '411403', '睢阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411421, 411400, '411421', '民权县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411422, 411400, '411422', '睢县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411423, 411400, '411423', '宁陵县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411424, 411400, '411424', '柘城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411425, 411400, '411425', '虞城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411426, 411400, '411426', '夏邑县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411481, 411400, '411481', '永城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411500, 410000, '411500', '信阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411502, 411500, '411502', '浉河区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411503, 411500, '411503', '平桥区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411521, 411500, '411521', '罗山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411522, 411500, '411522', '光山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411523, 411500, '411523', '新县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411524, 411500, '411524', '商城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411525, 411500, '411525', '固始县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411526, 411500, '411526', '潢川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411527, 411500, '411527', '淮滨县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411528, 411500, '411528', '息县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411600, 410000, '411600', '周口市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411602, 411600, '411602', '川汇区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411603, 411600, '411603', '淮阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411621, 411600, '411621', '扶沟县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411622, 411600, '411622', '西华县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411623, 411600, '411623', '商水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411624, 411600, '411624', '沈丘县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411625, 411600, '411625', '郸城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411627, 411600, '411627', '太康县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411628, 411600, '411628', '鹿邑县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411681, 411600, '411681', '项城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411700, 410000, '411700', '驻马店市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411702, 411700, '411702', '驿城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411721, 411700, '411721', '西平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411722, 411700, '411722', '上蔡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411723, 411700, '411723', '平舆县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411724, 411700, '411724', '正阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411725, 411700, '411725', '确山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411726, 411700, '411726', '泌阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411727, 411700, '411727', '汝南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411728, 411700, '411728', '遂平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (411729, 411700, '411729', '新蔡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (419001, 410000, '419001', '济源市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420000, 86, '420000', '湖北省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420100, 420000, '420100', '武汉市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420102, 420100, '420102', '江岸区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420103, 420100, '420103', '江汉区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420104, 420100, '420104', '硚口区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420105, 420100, '420105', '汉阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420106, 420100, '420106', '武昌区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420107, 420100, '420107', '青山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420111, 420100, '420111', '洪山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420112, 420100, '420112', '东西湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420113, 420100, '420113', '汉南区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420114, 420100, '420114', '蔡甸区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420115, 420100, '420115', '江夏区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420116, 420100, '420116', '黄陂区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420117, 420100, '420117', '新洲区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420200, 420000, '420200', '黄石市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420202, 420200, '420202', '黄石港区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420203, 420200, '420203', '西塞山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420204, 420200, '420204', '下陆区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420205, 420200, '420205', '铁山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420222, 420200, '420222', '阳新县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420281, 420200, '420281', '大冶市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420300, 420000, '420300', '十堰市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420302, 420300, '420302', '茅箭区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420303, 420300, '420303', '张湾区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420304, 420300, '420304', '郧阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420322, 420300, '420322', '郧西县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420323, 420300, '420323', '竹山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420324, 420300, '420324', '竹溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420325, 420300, '420325', '房县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420381, 420300, '420381', '丹江口市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420500, 420000, '420500', '宜昌市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420502, 420500, '420502', '西陵区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420503, 420500, '420503', '伍家岗区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420504, 420500, '420504', '点军区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420505, 420500, '420505', '猇亭区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420506, 420500, '420506', '夷陵区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420525, 420500, '420525', '远安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420526, 420500, '420526', '兴山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420527, 420500, '420527', '秭归县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420528, 420500, '420528', '长阳土家族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420529, 420500, '420529', '五峰土家族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420581, 420500, '420581', '宜都市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420582, 420500, '420582', '当阳市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420583, 420500, '420583', '枝江市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420600, 420000, '420600', '襄阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420602, 420600, '420602', '襄城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420606, 420600, '420606', '樊城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420607, 420600, '420607', '襄州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420624, 420600, '420624', '南漳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420625, 420600, '420625', '谷城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420626, 420600, '420626', '保康县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420682, 420600, '420682', '老河口市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420683, 420600, '420683', '枣阳市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420684, 420600, '420684', '宜城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420700, 420000, '420700', '鄂州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420702, 420700, '420702', '梁子湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420703, 420700, '420703', '华容区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420704, 420700, '420704', '鄂城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420800, 420000, '420800', '荆门市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420802, 420800, '420802', '东宝区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420804, 420800, '420804', '掇刀区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420822, 420800, '420822', '沙洋县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420881, 420800, '420881', '钟祥市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420882, 420800, '420882', '京山市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420900, 420000, '420900', '孝感市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420902, 420900, '420902', '孝南区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420921, 420900, '420921', '孝昌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420922, 420900, '420922', '大悟县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420923, 420900, '420923', '云梦县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420981, 420900, '420981', '应城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420982, 420900, '420982', '安陆市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (420984, 420900, '420984', '汉川市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421000, 420000, '421000', '荆州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421002, 421000, '421002', '沙市区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421003, 421000, '421003', '荆州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421022, 421000, '421022', '公安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421023, 421000, '421023', '监利县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421024, 421000, '421024', '江陵县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421081, 421000, '421081', '石首市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421083, 421000, '421083', '洪湖市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421087, 421000, '421087', '松滋市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421100, 420000, '421100', '黄冈市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421102, 421100, '421102', '黄州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421121, 421100, '421121', '团风县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421122, 421100, '421122', '红安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421123, 421100, '421123', '罗田县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421124, 421100, '421124', '英山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421125, 421100, '421125', '浠水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421126, 421100, '421126', '蕲春县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421127, 421100, '421127', '黄梅县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421181, 421100, '421181', '麻城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421182, 421100, '421182', '武穴市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421200, 420000, '421200', '咸宁市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421202, 421200, '421202', '咸安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421221, 421200, '421221', '嘉鱼县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421222, 421200, '421222', '通城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421223, 421200, '421223', '崇阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421224, 421200, '421224', '通山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421281, 421200, '421281', '赤壁市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421300, 420000, '421300', '随州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421303, 421300, '421303', '曾都区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421321, 421300, '421321', '随县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (421381, 421300, '421381', '广水市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (422800, 420000, '422800', '恩施土家族苗族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (422801, 422800, '422801', '恩施市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (422802, 422800, '422802', '利川市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (422822, 422800, '422822', '建始县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (422823, 422800, '422823', '巴东县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (422825, 422800, '422825', '宣恩县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (422826, 422800, '422826', '咸丰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (422827, 422800, '422827', '来凤县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (422828, 422800, '422828', '鹤峰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (429004, 420000, '429004', '仙桃市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (429005, 420000, '429005', '潜江市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (429006, 420000, '429006', '天门市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (429021, 420000, '429021', '神农架林区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430000, 86, '430000', '湖南省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430100, 430000, '430100', '长沙市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430102, 430100, '430102', '芙蓉区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430103, 430100, '430103', '天心区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430104, 430100, '430104', '岳麓区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430105, 430100, '430105', '开福区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430111, 430100, '430111', '雨花区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430112, 430100, '430112', '望城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430121, 430100, '430121', '长沙县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430181, 430100, '430181', '浏阳市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430182, 430100, '430182', '宁乡市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430200, 430000, '430200', '株洲市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430202, 430200, '430202', '荷塘区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430203, 430200, '430203', '芦淞区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430204, 430200, '430204', '石峰区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430211, 430200, '430211', '天元区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430212, 430200, '430212', '渌口区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430223, 430200, '430223', '攸县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430224, 430200, '430224', '茶陵县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430225, 430200, '430225', '炎陵县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430281, 430200, '430281', '醴陵市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430300, 430000, '430300', '湘潭市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430302, 430300, '430302', '雨湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430304, 430300, '430304', '岳塘区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430321, 430300, '430321', '湘潭县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430381, 430300, '430381', '湘乡市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430382, 430300, '430382', '韶山市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430400, 430000, '430400', '衡阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430405, 430400, '430405', '珠晖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430406, 430400, '430406', '雁峰区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430407, 430400, '430407', '石鼓区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430408, 430400, '430408', '蒸湘区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430412, 430400, '430412', '南岳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430421, 430400, '430421', '衡阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430422, 430400, '430422', '衡南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430423, 430400, '430423', '衡山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430424, 430400, '430424', '衡东县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430426, 430400, '430426', '祁东县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430481, 430400, '430481', '耒阳市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430482, 430400, '430482', '常宁市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430500, 430000, '430500', '邵阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430502, 430500, '430502', '双清区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430503, 430500, '430503', '大祥区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430511, 430500, '430511', '北塔区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430522, 430500, '430522', '新邵县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430523, 430500, '430523', '邵阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430524, 430500, '430524', '隆回县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430525, 430500, '430525', '洞口县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430527, 430500, '430527', '绥宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430528, 430500, '430528', '新宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430529, 430500, '430529', '城步苗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430581, 430500, '430581', '武冈市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430582, 430500, '430582', '邵东市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430600, 430000, '430600', '岳阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430602, 430600, '430602', '岳阳楼区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430603, 430600, '430603', '云溪区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430611, 430600, '430611', '君山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430621, 430600, '430621', '岳阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430623, 430600, '430623', '华容县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430624, 430600, '430624', '湘阴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430626, 430600, '430626', '平江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430681, 430600, '430681', '汨罗市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430682, 430600, '430682', '临湘市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430700, 430000, '430700', '常德市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430702, 430700, '430702', '武陵区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430703, 430700, '430703', '鼎城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430721, 430700, '430721', '安乡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430722, 430700, '430722', '汉寿县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430723, 430700, '430723', '澧县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430724, 430700, '430724', '临澧县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430725, 430700, '430725', '桃源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430726, 430700, '430726', '石门县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430781, 430700, '430781', '津市市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430800, 430000, '430800', '张家界市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430802, 430800, '430802', '永定区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430811, 430800, '430811', '武陵源区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430821, 430800, '430821', '慈利县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430822, 430800, '430822', '桑植县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430900, 430000, '430900', '益阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430902, 430900, '430902', '资阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430903, 430900, '430903', '赫山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430921, 430900, '430921', '南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430922, 430900, '430922', '桃江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430923, 430900, '430923', '安化县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (430981, 430900, '430981', '沅江市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431000, 430000, '431000', '郴州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431002, 431000, '431002', '北湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431003, 431000, '431003', '苏仙区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431021, 431000, '431021', '桂阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431022, 431000, '431022', '宜章县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431023, 431000, '431023', '永兴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431024, 431000, '431024', '嘉禾县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431025, 431000, '431025', '临武县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431026, 431000, '431026', '汝城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431027, 431000, '431027', '桂东县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431028, 431000, '431028', '安仁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431081, 431000, '431081', '资兴市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431100, 430000, '431100', '永州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431102, 431100, '431102', '零陵区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431103, 431100, '431103', '冷水滩区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431121, 431100, '431121', '祁阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431122, 431100, '431122', '东安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431123, 431100, '431123', '双牌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431124, 431100, '431124', '道县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431125, 431100, '431125', '江永县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431126, 431100, '431126', '宁远县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431127, 431100, '431127', '蓝山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431128, 431100, '431128', '新田县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431129, 431100, '431129', '江华瑶族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431200, 430000, '431200', '怀化市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431202, 431200, '431202', '鹤城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431221, 431200, '431221', '中方县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431222, 431200, '431222', '沅陵县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431223, 431200, '431223', '辰溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431224, 431200, '431224', '溆浦县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431225, 431200, '431225', '会同县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431226, 431200, '431226', '麻阳苗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431227, 431200, '431227', '新晃侗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431228, 431200, '431228', '芷江侗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431229, 431200, '431229', '靖州苗族侗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431230, 431200, '431230', '通道侗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431281, 431200, '431281', '洪江市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431300, 430000, '431300', '娄底市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431302, 431300, '431302', '娄星区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431321, 431300, '431321', '双峰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431322, 431300, '431322', '新化县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431381, 431300, '431381', '冷水江市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (431382, 431300, '431382', '涟源市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (433100, 430000, '433100', '湘西土家族苗族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (433101, 433100, '433101', '吉首市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (433122, 433100, '433122', '泸溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (433123, 433100, '433123', '凤凰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (433124, 433100, '433124', '花垣县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (433125, 433100, '433125', '保靖县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (433126, 433100, '433126', '古丈县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (433127, 433100, '433127', '永顺县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (433130, 433100, '433130', '龙山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440000, 86, '440000', '广东省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440100, 440000, '440100', '广州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440103, 440100, '440103', '荔湾区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440104, 440100, '440104', '越秀区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440105, 440100, '440105', '海珠区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440106, 440100, '440106', '天河区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440111, 440100, '440111', '白云区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440112, 440100, '440112', '黄埔区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440113, 440100, '440113', '番禺区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440114, 440100, '440114', '花都区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440115, 440100, '440115', '南沙区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440117, 440100, '440117', '从化区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440118, 440100, '440118', '增城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440200, 440000, '440200', '韶关市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440203, 440200, '440203', '武江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440204, 440200, '440204', '浈江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440205, 440200, '440205', '曲江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440222, 440200, '440222', '始兴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440224, 440200, '440224', '仁化县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440229, 440200, '440229', '翁源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440232, 440200, '440232', '乳源瑶族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440233, 440200, '440233', '新丰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440281, 440200, '440281', '乐昌市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440282, 440200, '440282', '南雄市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440300, 440000, '440300', '深圳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440303, 440300, '440303', '罗湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440304, 440300, '440304', '福田区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440305, 440300, '440305', '南山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440306, 440300, '440306', '宝安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440307, 440300, '440307', '龙岗区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440308, 440300, '440308', '盐田区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440309, 440300, '440309', '龙华区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440310, 440300, '440310', '坪山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440311, 440300, '440311', '光明区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440400, 440000, '440400', '珠海市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440402, 440400, '440402', '香洲区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440403, 440400, '440403', '斗门区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440404, 440400, '440404', '金湾区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440500, 440000, '440500', '汕头市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440507, 440500, '440507', '龙湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440511, 440500, '440511', '金平区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440512, 440500, '440512', '濠江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440513, 440500, '440513', '潮阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440514, 440500, '440514', '潮南区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440515, 440500, '440515', '澄海区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440523, 440500, '440523', '南澳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440600, 440000, '440600', '佛山市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440604, 440600, '440604', '禅城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440605, 440600, '440605', '南海区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440606, 440600, '440606', '顺德区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440607, 440600, '440607', '三水区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440608, 440600, '440608', '高明区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440700, 440000, '440700', '江门市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440703, 440700, '440703', '蓬江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440704, 440700, '440704', '江海区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440705, 440700, '440705', '新会区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440781, 440700, '440781', '台山市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440783, 440700, '440783', '开平市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440784, 440700, '440784', '鹤山市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440785, 440700, '440785', '恩平市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440800, 440000, '440800', '湛江市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440802, 440800, '440802', '赤坎区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440803, 440800, '440803', '霞山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440804, 440800, '440804', '坡头区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440811, 440800, '440811', '麻章区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440823, 440800, '440823', '遂溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440825, 440800, '440825', '徐闻县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440881, 440800, '440881', '廉江市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440882, 440800, '440882', '雷州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440883, 440800, '440883', '吴川市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440900, 440000, '440900', '茂名市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440902, 440900, '440902', '茂南区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440904, 440900, '440904', '电白区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440981, 440900, '440981', '高州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440982, 440900, '440982', '化州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (440983, 440900, '440983', '信宜市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441200, 440000, '441200', '肇庆市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441202, 441200, '441202', '端州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441203, 441200, '441203', '鼎湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441204, 441200, '441204', '高要区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441223, 441200, '441223', '广宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441224, 441200, '441224', '怀集县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441225, 441200, '441225', '封开县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441226, 441200, '441226', '德庆县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441284, 441200, '441284', '四会市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441300, 440000, '441300', '惠州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441302, 441300, '441302', '惠城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441303, 441300, '441303', '惠阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441322, 441300, '441322', '博罗县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441323, 441300, '441323', '惠东县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441324, 441300, '441324', '龙门县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441400, 440000, '441400', '梅州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441402, 441400, '441402', '梅江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441403, 441400, '441403', '梅县区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441422, 441400, '441422', '大埔县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441423, 441400, '441423', '丰顺县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441424, 441400, '441424', '五华县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441426, 441400, '441426', '平远县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441427, 441400, '441427', '蕉岭县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441481, 441400, '441481', '兴宁市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441500, 440000, '441500', '汕尾市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441502, 441500, '441502', '城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441521, 441500, '441521', '海丰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441523, 441500, '441523', '陆河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441581, 441500, '441581', '陆丰市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441600, 440000, '441600', '河源市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441602, 441600, '441602', '源城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441621, 441600, '441621', '紫金县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441622, 441600, '441622', '龙川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441623, 441600, '441623', '连平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441624, 441600, '441624', '和平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441625, 441600, '441625', '东源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441700, 440000, '441700', '阳江市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441702, 441700, '441702', '江城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441704, 441700, '441704', '阳东区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441721, 441700, '441721', '阳西县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441781, 441700, '441781', '阳春市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441800, 440000, '441800', '清远市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441802, 441800, '441802', '清城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441803, 441800, '441803', '清新区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441821, 441800, '441821', '佛冈县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441823, 441800, '441823', '阳山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441825, 441800, '441825', '连山壮族瑶族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441826, 441800, '441826', '连南瑶族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441881, 441800, '441881', '英德市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441882, 441800, '441882', '连州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (441900, 440000, '441900', '东莞市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (442000, 440000, '442000', '中山市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445100, 440000, '445100', '潮州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445102, 445100, '445102', '湘桥区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445103, 445100, '445103', '潮安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445122, 445100, '445122', '饶平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445200, 440000, '445200', '揭阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445202, 445200, '445202', '榕城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445203, 445200, '445203', '揭东区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445222, 445200, '445222', '揭西县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445224, 445200, '445224', '惠来县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445281, 445200, '445281', '普宁市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445300, 440000, '445300', '云浮市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445302, 445300, '445302', '云城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445303, 445300, '445303', '云安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445321, 445300, '445321', '新兴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445322, 445300, '445322', '郁南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (445381, 445300, '445381', '罗定市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450000, 86, '450000', '广西壮族自治区', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450100, 450000, '450100', '南宁市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450102, 450100, '450102', '兴宁区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450103, 450100, '450103', '青秀区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450105, 450100, '450105', '江南区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450107, 450100, '450107', '西乡塘区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450108, 450100, '450108', '良庆区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450109, 450100, '450109', '邕宁区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450110, 450100, '450110', '武鸣区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450123, 450100, '450123', '隆安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450124, 450100, '450124', '马山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450125, 450100, '450125', '上林县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450126, 450100, '450126', '宾阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450127, 450100, '450127', '横县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450200, 450000, '450200', '柳州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450202, 450200, '450202', '城中区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450203, 450200, '450203', '鱼峰区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450204, 450200, '450204', '柳南区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450205, 450200, '450205', '柳北区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450206, 450200, '450206', '柳江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450222, 450200, '450222', '柳城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450223, 450200, '450223', '鹿寨县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450224, 450200, '450224', '融安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450225, 450200, '450225', '融水苗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450226, 450200, '450226', '三江侗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450300, 450000, '450300', '桂林市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450302, 450300, '450302', '秀峰区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450303, 450300, '450303', '叠彩区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450304, 450300, '450304', '象山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450305, 450300, '450305', '七星区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450311, 450300, '450311', '雁山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450312, 450300, '450312', '临桂区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450321, 450300, '450321', '阳朔县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450323, 450300, '450323', '灵川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450324, 450300, '450324', '全州县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450325, 450300, '450325', '兴安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450326, 450300, '450326', '永福县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450327, 450300, '450327', '灌阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450328, 450300, '450328', '龙胜各族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450329, 450300, '450329', '资源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450330, 450300, '450330', '平乐县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450332, 450300, '450332', '恭城瑶族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450381, 450300, '450381', '荔浦市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450400, 450000, '450400', '梧州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450403, 450400, '450403', '万秀区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450405, 450400, '450405', '长洲区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450406, 450400, '450406', '龙圩区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450421, 450400, '450421', '苍梧县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450422, 450400, '450422', '藤县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450423, 450400, '450423', '蒙山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450481, 450400, '450481', '岑溪市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450500, 450000, '450500', '北海市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450502, 450500, '450502', '海城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450503, 450500, '450503', '银海区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450512, 450500, '450512', '铁山港区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450521, 450500, '450521', '合浦县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450600, 450000, '450600', '防城港市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450602, 450600, '450602', '港口区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450603, 450600, '450603', '防城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450621, 450600, '450621', '上思县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450681, 450600, '450681', '东兴市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450700, 450000, '450700', '钦州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450702, 450700, '450702', '钦南区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450703, 450700, '450703', '钦北区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450721, 450700, '450721', '灵山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450722, 450700, '450722', '浦北县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450800, 450000, '450800', '贵港市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450802, 450800, '450802', '港北区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450803, 450800, '450803', '港南区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450804, 450800, '450804', '覃塘区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450821, 450800, '450821', '平南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450881, 450800, '450881', '桂平市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450900, 450000, '450900', '玉林市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450902, 450900, '450902', '玉州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450903, 450900, '450903', '福绵区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450921, 450900, '450921', '容县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450922, 450900, '450922', '陆川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450923, 450900, '450923', '博白县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450924, 450900, '450924', '兴业县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (450981, 450900, '450981', '北流市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451000, 450000, '451000', '百色市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451002, 451000, '451002', '右江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451003, 451000, '451003', '田阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451022, 451000, '451022', '田东县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451024, 451000, '451024', '德保县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451026, 451000, '451026', '那坡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451027, 451000, '451027', '凌云县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451028, 451000, '451028', '乐业县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451029, 451000, '451029', '田林县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451030, 451000, '451030', '西林县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451031, 451000, '451031', '隆林各族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451081, 451000, '451081', '靖西市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451082, 451000, '451082', '平果市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451100, 450000, '451100', '贺州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451102, 451100, '451102', '八步区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451103, 451100, '451103', '平桂区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451121, 451100, '451121', '昭平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451122, 451100, '451122', '钟山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451123, 451100, '451123', '富川瑶族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451200, 450000, '451200', '河池市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451202, 451200, '451202', '金城江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451203, 451200, '451203', '宜州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451221, 451200, '451221', '南丹县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451222, 451200, '451222', '天峨县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451223, 451200, '451223', '凤山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451224, 451200, '451224', '东兰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451225, 451200, '451225', '罗城仫佬族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451226, 451200, '451226', '环江毛南族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451227, 451200, '451227', '巴马瑶族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451228, 451200, '451228', '都安瑶族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451229, 451200, '451229', '大化瑶族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451300, 450000, '451300', '来宾市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451302, 451300, '451302', '兴宾区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451321, 451300, '451321', '忻城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451322, 451300, '451322', '象州县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451323, 451300, '451323', '武宣县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451324, 451300, '451324', '金秀瑶族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451381, 451300, '451381', '合山市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451400, 450000, '451400', '崇左市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451402, 451400, '451402', '江州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451421, 451400, '451421', '扶绥县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451422, 451400, '451422', '宁明县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451423, 451400, '451423', '龙州县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451424, 451400, '451424', '大新县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451425, 451400, '451425', '天等县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (451481, 451400, '451481', '凭祥市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (460000, 86, '460000', '海南省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (460100, 460000, '460100', '海口市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (460105, 460100, '460105', '秀英区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (460106, 460100, '460106', '龙华区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (460107, 460100, '460107', '琼山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (460108, 460100, '460108', '美兰区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (460200, 460000, '460200', '三亚市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (460202, 460200, '460202', '海棠区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (460203, 460200, '460203', '吉阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (460204, 460200, '460204', '天涯区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (460205, 460200, '460205', '崖州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (460300, 460000, '460300', '三沙市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (460400, 460000, '460400', '儋州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469001, 460000, '469001', '五指山市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469002, 460000, '469002', '琼海市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469005, 460000, '469005', '文昌市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469006, 460000, '469006', '万宁市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469007, 460000, '469007', '东方市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469021, 460000, '469021', '定安县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469022, 460000, '469022', '屯昌县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469023, 460000, '469023', '澄迈县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469024, 460000, '469024', '临高县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469025, 460000, '469025', '白沙黎族自治县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469026, 460000, '469026', '昌江黎族自治县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469027, 460000, '469027', '乐东黎族自治县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469028, 460000, '469028', '陵水黎族自治县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469029, 460000, '469029', '保亭黎族苗族自治县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (469030, 460000, '469030', '琼中黎族苗族自治县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500000, 86, '500000', '重庆市', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500101, 500000, '500101', '万州区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500102, 500000, '500102', '涪陵区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500103, 500000, '500103', '渝中区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500104, 500000, '500104', '大渡口区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500105, 500000, '500105', '江北区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500106, 500000, '500106', '沙坪坝区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500107, 500000, '500107', '九龙坡区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500108, 500000, '500108', '南岸区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500109, 500000, '500109', '北碚区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500110, 500000, '500110', '綦江区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500111, 500000, '500111', '大足区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500112, 500000, '500112', '渝北区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500113, 500000, '500113', '巴南区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500114, 500000, '500114', '黔江区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500115, 500000, '500115', '长寿区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500116, 500000, '500116', '江津区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500117, 500000, '500117', '合川区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500118, 500000, '500118', '永川区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500119, 500000, '500119', '南川区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500120, 500000, '500120', '璧山区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500151, 500000, '500151', '铜梁区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500152, 500000, '500152', '潼南区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500153, 500000, '500153', '荣昌区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500154, 500000, '500154', '开州区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500155, 500000, '500155', '梁平区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500156, 500000, '500156', '武隆区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500229, 500000, '500229', '城口县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500230, 500000, '500230', '丰都县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500231, 500000, '500231', '垫江县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500233, 500000, '500233', '忠县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500235, 500000, '500235', '云阳县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500236, 500000, '500236', '奉节县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500237, 500000, '500237', '巫山县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500238, 500000, '500238', '巫溪县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500240, 500000, '500240', '石柱土家族自治县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500241, 500000, '500241', '秀山土家族苗族自治县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500242, 500000, '500242', '酉阳土家族苗族自治县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (500243, 500000, '500243', '彭水苗族土家族自治县', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510000, 86, '510000', '四川省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510100, 510000, '510100', '成都市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510104, 510100, '510104', '锦江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510105, 510100, '510105', '青羊区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510106, 510100, '510106', '金牛区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510107, 510100, '510107', '武侯区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510108, 510100, '510108', '成华区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510112, 510100, '510112', '龙泉驿区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510113, 510100, '510113', '青白江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510114, 510100, '510114', '新都区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510115, 510100, '510115', '温江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510116, 510100, '510116', '双流区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510117, 510100, '510117', '郫都区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510118, 510100, '510118', '新津区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510121, 510100, '510121', '金堂县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510129, 510100, '510129', '大邑县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510131, 510100, '510131', '蒲江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510181, 510100, '510181', '都江堰市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510182, 510100, '510182', '彭州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510183, 510100, '510183', '邛崃市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510184, 510100, '510184', '崇州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510185, 510100, '510185', '简阳市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510300, 510000, '510300', '自贡市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510302, 510300, '510302', '自流井区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510303, 510300, '510303', '贡井区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510304, 510300, '510304', '大安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510311, 510300, '510311', '沿滩区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510321, 510300, '510321', '荣县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510322, 510300, '510322', '富顺县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510400, 510000, '510400', '攀枝花市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510402, 510400, '510402', '东区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510403, 510400, '510403', '西区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510411, 510400, '510411', '仁和区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510421, 510400, '510421', '米易县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510422, 510400, '510422', '盐边县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510500, 510000, '510500', '泸州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510502, 510500, '510502', '江阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510503, 510500, '510503', '纳溪区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510504, 510500, '510504', '龙马潭区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510521, 510500, '510521', '泸县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510522, 510500, '510522', '合江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510524, 510500, '510524', '叙永县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510525, 510500, '510525', '古蔺县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510600, 510000, '510600', '德阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510603, 510600, '510603', '旌阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510604, 510600, '510604', '罗江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510623, 510600, '510623', '中江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510681, 510600, '510681', '广汉市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510682, 510600, '510682', '什邡市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510683, 510600, '510683', '绵竹市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510700, 510000, '510700', '绵阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510703, 510700, '510703', '涪城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510704, 510700, '510704', '游仙区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510705, 510700, '510705', '安州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510722, 510700, '510722', '三台县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510723, 510700, '510723', '盐亭县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510725, 510700, '510725', '梓潼县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510726, 510700, '510726', '北川羌族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510727, 510700, '510727', '平武县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510781, 510700, '510781', '江油市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510800, 510000, '510800', '广元市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510802, 510800, '510802', '利州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510811, 510800, '510811', '昭化区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510812, 510800, '510812', '朝天区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510821, 510800, '510821', '旺苍县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510822, 510800, '510822', '青川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510823, 510800, '510823', '剑阁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510824, 510800, '510824', '苍溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510900, 510000, '510900', '遂宁市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510903, 510900, '510903', '船山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510904, 510900, '510904', '安居区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510921, 510900, '510921', '蓬溪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510923, 510900, '510923', '大英县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (510981, 510900, '510981', '射洪市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511000, 510000, '511000', '内江市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511002, 511000, '511002', '市中区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511011, 511000, '511011', '东兴区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511024, 511000, '511024', '威远县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511025, 511000, '511025', '资中县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511083, 511000, '511083', '隆昌市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511100, 510000, '511100', '乐山市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511102, 511100, '511102', '市中区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511111, 511100, '511111', '沙湾区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511112, 511100, '511112', '五通桥区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511113, 511100, '511113', '金口河区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511123, 511100, '511123', '犍为县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511124, 511100, '511124', '井研县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511126, 511100, '511126', '夹江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511129, 511100, '511129', '沐川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511132, 511100, '511132', '峨边彝族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511133, 511100, '511133', '马边彝族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511181, 511100, '511181', '峨眉山市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511300, 510000, '511300', '南充市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511302, 511300, '511302', '顺庆区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511303, 511300, '511303', '高坪区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511304, 511300, '511304', '嘉陵区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511321, 511300, '511321', '南部县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511322, 511300, '511322', '营山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511323, 511300, '511323', '蓬安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511324, 511300, '511324', '仪陇县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511325, 511300, '511325', '西充县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511381, 511300, '511381', '阆中市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511400, 510000, '511400', '眉山市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511402, 511400, '511402', '东坡区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511403, 511400, '511403', '彭山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511421, 511400, '511421', '仁寿县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511423, 511400, '511423', '洪雅县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511424, 511400, '511424', '丹棱县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511425, 511400, '511425', '青神县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511500, 510000, '511500', '宜宾市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511502, 511500, '511502', '翠屏区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511503, 511500, '511503', '南溪区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511504, 511500, '511504', '叙州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511523, 511500, '511523', '江安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511524, 511500, '511524', '长宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511525, 511500, '511525', '高县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511526, 511500, '511526', '珙县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511527, 511500, '511527', '筠连县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511528, 511500, '511528', '兴文县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511529, 511500, '511529', '屏山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511600, 510000, '511600', '广安市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511602, 511600, '511602', '广安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511603, 511600, '511603', '前锋区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511621, 511600, '511621', '岳池县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511622, 511600, '511622', '武胜县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511623, 511600, '511623', '邻水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511681, 511600, '511681', '华蓥市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511700, 510000, '511700', '达州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511702, 511700, '511702', '通川区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511703, 511700, '511703', '达川区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511722, 511700, '511722', '宣汉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511723, 511700, '511723', '开江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511724, 511700, '511724', '大竹县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511725, 511700, '511725', '渠县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511781, 511700, '511781', '万源市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511800, 510000, '511800', '雅安市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511802, 511800, '511802', '雨城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511803, 511800, '511803', '名山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511822, 511800, '511822', '荥经县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511823, 511800, '511823', '汉源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511824, 511800, '511824', '石棉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511825, 511800, '511825', '天全县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511826, 511800, '511826', '芦山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511827, 511800, '511827', '宝兴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511900, 510000, '511900', '巴中市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511902, 511900, '511902', '巴州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511903, 511900, '511903', '恩阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511921, 511900, '511921', '通江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511922, 511900, '511922', '南江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (511923, 511900, '511923', '平昌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (512000, 510000, '512000', '资阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (512002, 512000, '512002', '雁江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (512021, 512000, '512021', '安岳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (512022, 512000, '512022', '乐至县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513200, 510000, '513200', '阿坝藏族羌族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513201, 513200, '513201', '马尔康市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513221, 513200, '513221', '汶川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513222, 513200, '513222', '理县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513223, 513200, '513223', '茂县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513224, 513200, '513224', '松潘县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513225, 513200, '513225', '九寨沟县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513226, 513200, '513226', '金川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513227, 513200, '513227', '小金县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513228, 513200, '513228', '黑水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513230, 513200, '513230', '壤塘县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513231, 513200, '513231', '阿坝县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513232, 513200, '513232', '若尔盖县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513233, 513200, '513233', '红原县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513300, 510000, '513300', '甘孜藏族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513301, 513300, '513301', '康定市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513322, 513300, '513322', '泸定县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513323, 513300, '513323', '丹巴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513324, 513300, '513324', '九龙县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513325, 513300, '513325', '雅江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513326, 513300, '513326', '道孚县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513327, 513300, '513327', '炉霍县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513328, 513300, '513328', '甘孜县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513329, 513300, '513329', '新龙县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513330, 513300, '513330', '德格县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513331, 513300, '513331', '白玉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513332, 513300, '513332', '石渠县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513333, 513300, '513333', '色达县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513334, 513300, '513334', '理塘县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513335, 513300, '513335', '巴塘县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513336, 513300, '513336', '乡城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513337, 513300, '513337', '稻城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513338, 513300, '513338', '得荣县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513400, 510000, '513400', '凉山彝族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513401, 513400, '513401', '西昌市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513422, 513400, '513422', '木里藏族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513423, 513400, '513423', '盐源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513424, 513400, '513424', '德昌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513425, 513400, '513425', '会理县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513426, 513400, '513426', '会东县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513427, 513400, '513427', '宁南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513428, 513400, '513428', '普格县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513429, 513400, '513429', '布拖县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513430, 513400, '513430', '金阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513431, 513400, '513431', '昭觉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513432, 513400, '513432', '喜德县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513433, 513400, '513433', '冕宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513434, 513400, '513434', '越西县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513435, 513400, '513435', '甘洛县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513436, 513400, '513436', '美姑县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (513437, 513400, '513437', '雷波县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520000, 86, '520000', '贵州省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520100, 520000, '520100', '贵阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520102, 520100, '520102', '南明区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520103, 520100, '520103', '云岩区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520111, 520100, '520111', '花溪区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520112, 520100, '520112', '乌当区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520113, 520100, '520113', '白云区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520115, 520100, '520115', '观山湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520121, 520100, '520121', '开阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520122, 520100, '520122', '息烽县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520123, 520100, '520123', '修文县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520181, 520100, '520181', '清镇市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520200, 520000, '520200', '六盘水市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520201, 520200, '520201', '钟山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520203, 520200, '520203', '六枝特区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520221, 520200, '520221', '水城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520281, 520200, '520281', '盘州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520300, 520000, '520300', '遵义市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520302, 520300, '520302', '红花岗区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520303, 520300, '520303', '汇川区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520304, 520300, '520304', '播州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520322, 520300, '520322', '桐梓县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520323, 520300, '520323', '绥阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520324, 520300, '520324', '正安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520325, 520300, '520325', '道真仡佬族苗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520326, 520300, '520326', '务川仡佬族苗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520327, 520300, '520327', '凤冈县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520328, 520300, '520328', '湄潭县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520329, 520300, '520329', '余庆县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520330, 520300, '520330', '习水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520381, 520300, '520381', '赤水市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520382, 520300, '520382', '仁怀市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520400, 520000, '520400', '安顺市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520402, 520400, '520402', '西秀区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520403, 520400, '520403', '平坝区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520422, 520400, '520422', '普定县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520423, 520400, '520423', '镇宁布依族苗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520424, 520400, '520424', '关岭布依族苗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520425, 520400, '520425', '紫云苗族布依族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520500, 520000, '520500', '毕节市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520502, 520500, '520502', '七星关区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520521, 520500, '520521', '大方县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520522, 520500, '520522', '黔西县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520523, 520500, '520523', '金沙县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520524, 520500, '520524', '织金县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520525, 520500, '520525', '纳雍县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520526, 520500, '520526', '威宁彝族回族苗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520527, 520500, '520527', '赫章县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520600, 520000, '520600', '铜仁市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520602, 520600, '520602', '碧江区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520603, 520600, '520603', '万山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520621, 520600, '520621', '江口县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520622, 520600, '520622', '玉屏侗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520623, 520600, '520623', '石阡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520624, 520600, '520624', '思南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520625, 520600, '520625', '印江土家族苗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520626, 520600, '520626', '德江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520627, 520600, '520627', '沿河土家族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (520628, 520600, '520628', '松桃苗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522300, 520000, '522300', '黔西南布依族苗族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522301, 522300, '522301', '兴义市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522302, 522300, '522302', '兴仁市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522323, 522300, '522323', '普安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522324, 522300, '522324', '晴隆县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522325, 522300, '522325', '贞丰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522326, 522300, '522326', '望谟县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522327, 522300, '522327', '册亨县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522328, 522300, '522328', '安龙县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522600, 520000, '522600', '黔东南苗族侗族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522601, 522600, '522601', '凯里市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522622, 522600, '522622', '黄平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522623, 522600, '522623', '施秉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522624, 522600, '522624', '三穗县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522625, 522600, '522625', '镇远县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522626, 522600, '522626', '岑巩县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522627, 522600, '522627', '天柱县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522628, 522600, '522628', '锦屏县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522629, 522600, '522629', '剑河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522630, 522600, '522630', '台江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522631, 522600, '522631', '黎平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522632, 522600, '522632', '榕江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522633, 522600, '522633', '从江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522634, 522600, '522634', '雷山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522635, 522600, '522635', '麻江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522636, 522600, '522636', '丹寨县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522700, 520000, '522700', '黔南布依族苗族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522701, 522700, '522701', '都匀市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522702, 522700, '522702', '福泉市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522722, 522700, '522722', '荔波县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522723, 522700, '522723', '贵定县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522725, 522700, '522725', '瓮安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522726, 522700, '522726', '独山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522727, 522700, '522727', '平塘县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522728, 522700, '522728', '罗甸县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522729, 522700, '522729', '长顺县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522730, 522700, '522730', '龙里县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522731, 522700, '522731', '惠水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (522732, 522700, '522732', '三都水族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530000, 86, '530000', '云南省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530100, 530000, '530100', '昆明市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530102, 530100, '530102', '五华区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530103, 530100, '530103', '盘龙区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530111, 530100, '530111', '官渡区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530112, 530100, '530112', '西山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530113, 530100, '530113', '东川区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530114, 530100, '530114', '呈贡区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530115, 530100, '530115', '晋宁区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530124, 530100, '530124', '富民县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530125, 530100, '530125', '宜良县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530126, 530100, '530126', '石林彝族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530127, 530100, '530127', '嵩明县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530128, 530100, '530128', '禄劝彝族苗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530129, 530100, '530129', '寻甸回族彝族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530181, 530100, '530181', '安宁市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530300, 530000, '530300', '曲靖市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530302, 530300, '530302', '麒麟区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530303, 530300, '530303', '沾益区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530304, 530300, '530304', '马龙区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530322, 530300, '530322', '陆良县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530323, 530300, '530323', '师宗县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530324, 530300, '530324', '罗平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530325, 530300, '530325', '富源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530326, 530300, '530326', '会泽县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530381, 530300, '530381', '宣威市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530400, 530000, '530400', '玉溪市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530402, 530400, '530402', '红塔区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530403, 530400, '530403', '江川区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530423, 530400, '530423', '通海县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530424, 530400, '530424', '华宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530425, 530400, '530425', '易门县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530426, 530400, '530426', '峨山彝族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530427, 530400, '530427', '新平彝族傣族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530428, 530400, '530428', '元江哈尼族彝族傣族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530481, 530400, '530481', '澄江市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530500, 530000, '530500', '保山市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530502, 530500, '530502', '隆阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530521, 530500, '530521', '施甸县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530523, 530500, '530523', '龙陵县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530524, 530500, '530524', '昌宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530581, 530500, '530581', '腾冲市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530600, 530000, '530600', '昭通市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530602, 530600, '530602', '昭阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530621, 530600, '530621', '鲁甸县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530622, 530600, '530622', '巧家县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530623, 530600, '530623', '盐津县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530624, 530600, '530624', '大关县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530625, 530600, '530625', '永善县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530626, 530600, '530626', '绥江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530627, 530600, '530627', '镇雄县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530628, 530600, '530628', '彝良县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530629, 530600, '530629', '威信县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530681, 530600, '530681', '水富市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530700, 530000, '530700', '丽江市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530702, 530700, '530702', '古城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530721, 530700, '530721', '玉龙纳西族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530722, 530700, '530722', '永胜县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530723, 530700, '530723', '华坪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530724, 530700, '530724', '宁蒗彝族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530800, 530000, '530800', '普洱市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530802, 530800, '530802', '思茅区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530821, 530800, '530821', '宁洱哈尼族彝族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530822, 530800, '530822', '墨江哈尼族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530823, 530800, '530823', '景东彝族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530824, 530800, '530824', '景谷傣族彝族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530825, 530800, '530825', '镇沅彝族哈尼族拉祜族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530826, 530800, '530826', '江城哈尼族彝族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530827, 530800, '530827', '孟连傣族拉祜族佤族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530828, 530800, '530828', '澜沧拉祜族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530829, 530800, '530829', '西盟佤族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530900, 530000, '530900', '临沧市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530902, 530900, '530902', '临翔区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530921, 530900, '530921', '凤庆县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530922, 530900, '530922', '云县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530923, 530900, '530923', '永德县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530924, 530900, '530924', '镇康县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530925, 530900, '530925', '双江拉祜族佤族布朗族傣族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530926, 530900, '530926', '耿马傣族佤族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (530927, 530900, '530927', '沧源佤族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532300, 530000, '532300', '楚雄彝族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532301, 532300, '532301', '楚雄市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532322, 532300, '532322', '双柏县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532323, 532300, '532323', '牟定县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532324, 532300, '532324', '南华县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532325, 532300, '532325', '姚安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532326, 532300, '532326', '大姚县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532327, 532300, '532327', '永仁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532328, 532300, '532328', '元谋县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532329, 532300, '532329', '武定县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532331, 532300, '532331', '禄丰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532500, 530000, '532500', '红河哈尼族彝族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532501, 532500, '532501', '个旧市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532502, 532500, '532502', '开远市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532503, 532500, '532503', '蒙自市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532504, 532500, '532504', '弥勒市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532523, 532500, '532523', '屏边苗族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532524, 532500, '532524', '建水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532525, 532500, '532525', '石屏县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532527, 532500, '532527', '泸西县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532528, 532500, '532528', '元阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532529, 532500, '532529', '红河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532530, 532500, '532530', '金平苗族瑶族傣族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532531, 532500, '532531', '绿春县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532532, 532500, '532532', '河口瑶族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532600, 530000, '532600', '文山壮族苗族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532601, 532600, '532601', '文山市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532622, 532600, '532622', '砚山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532623, 532600, '532623', '西畴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532624, 532600, '532624', '麻栗坡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532625, 532600, '532625', '马关县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532626, 532600, '532626', '丘北县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532627, 532600, '532627', '广南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532628, 532600, '532628', '富宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532800, 530000, '532800', '西双版纳傣族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532801, 532800, '532801', '景洪市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532822, 532800, '532822', '勐海县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532823, 532800, '532823', '勐腊县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532900, 530000, '532900', '大理白族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532901, 532900, '532901', '大理市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532922, 532900, '532922', '漾濞彝族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532923, 532900, '532923', '祥云县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532924, 532900, '532924', '宾川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532925, 532900, '532925', '弥渡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532926, 532900, '532926', '南涧彝族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532927, 532900, '532927', '巍山彝族回族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532928, 532900, '532928', '永平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532929, 532900, '532929', '云龙县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532930, 532900, '532930', '洱源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532931, 532900, '532931', '剑川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (532932, 532900, '532932', '鹤庆县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533100, 530000, '533100', '德宏傣族景颇族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533102, 533100, '533102', '瑞丽市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533103, 533100, '533103', '芒市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533122, 533100, '533122', '梁河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533123, 533100, '533123', '盈江县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533124, 533100, '533124', '陇川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533300, 530000, '533300', '怒江傈僳族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533301, 533300, '533301', '泸水市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533323, 533300, '533323', '福贡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533324, 533300, '533324', '贡山独龙族怒族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533325, 533300, '533325', '兰坪白族普米族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533400, 530000, '533400', '迪庆藏族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533401, 533400, '533401', '香格里拉市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533422, 533400, '533422', '德钦县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (533423, 533400, '533423', '维西傈僳族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540000, 86, '540000', '西藏自治区', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540100, 540000, '540100', '拉萨市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540102, 540100, '540102', '城关区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540103, 540100, '540103', '堆龙德庆区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540104, 540100, '540104', '达孜区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540121, 540100, '540121', '林周县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540122, 540100, '540122', '当雄县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540123, 540100, '540123', '尼木县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540124, 540100, '540124', '曲水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540127, 540100, '540127', '墨竹工卡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540200, 540000, '540200', '日喀则市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540202, 540200, '540202', '桑珠孜区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540221, 540200, '540221', '南木林县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540222, 540200, '540222', '江孜县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540223, 540200, '540223', '定日县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540224, 540200, '540224', '萨迦县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540225, 540200, '540225', '拉孜县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540226, 540200, '540226', '昂仁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540227, 540200, '540227', '谢通门县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540228, 540200, '540228', '白朗县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540229, 540200, '540229', '仁布县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540230, 540200, '540230', '康马县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540231, 540200, '540231', '定结县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540232, 540200, '540232', '仲巴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540233, 540200, '540233', '亚东县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540234, 540200, '540234', '吉隆县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540235, 540200, '540235', '聂拉木县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540236, 540200, '540236', '萨嘎县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540237, 540200, '540237', '岗巴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540300, 540000, '540300', '昌都市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540302, 540300, '540302', '卡若区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540321, 540300, '540321', '江达县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540322, 540300, '540322', '贡觉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540323, 540300, '540323', '类乌齐县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540324, 540300, '540324', '丁青县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540325, 540300, '540325', '察雅县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540326, 540300, '540326', '八宿县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540327, 540300, '540327', '左贡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540328, 540300, '540328', '芒康县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540329, 540300, '540329', '洛隆县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540330, 540300, '540330', '边坝县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540400, 540000, '540400', '林芝市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540402, 540400, '540402', '巴宜区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540421, 540400, '540421', '工布江达县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540422, 540400, '540422', '米林县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540423, 540400, '540423', '墨脱县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540424, 540400, '540424', '波密县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540425, 540400, '540425', '察隅县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540426, 540400, '540426', '朗县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540500, 540000, '540500', '山南市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540502, 540500, '540502', '乃东区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540521, 540500, '540521', '扎囊县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540522, 540500, '540522', '贡嘎县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540523, 540500, '540523', '桑日县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540524, 540500, '540524', '琼结县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540525, 540500, '540525', '曲松县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540526, 540500, '540526', '措美县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540527, 540500, '540527', '洛扎县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540528, 540500, '540528', '加查县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540529, 540500, '540529', '隆子县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540530, 540500, '540530', '错那县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540531, 540500, '540531', '浪卡子县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540600, 540000, '540600', '那曲市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540602, 540600, '540602', '色尼区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540621, 540600, '540621', '嘉黎县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540622, 540600, '540622', '比如县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540623, 540600, '540623', '聂荣县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540624, 540600, '540624', '安多县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540625, 540600, '540625', '申扎县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540626, 540600, '540626', '索县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540627, 540600, '540627', '班戈县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540628, 540600, '540628', '巴青县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540629, 540600, '540629', '尼玛县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (540630, 540600, '540630', '双湖县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (542500, 540000, '542500', '阿里地区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (542521, 542500, '542521', '普兰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (542522, 542500, '542522', '札达县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (542523, 542500, '542523', '噶尔县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (542524, 542500, '542524', '日土县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (542525, 542500, '542525', '革吉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (542526, 542500, '542526', '改则县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (542527, 542500, '542527', '措勤县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610000, 86, '610000', '陕西省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610100, 610000, '610100', '西安市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610102, 610100, '610102', '新城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610103, 610100, '610103', '碑林区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610104, 610100, '610104', '莲湖区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610111, 610100, '610111', '灞桥区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610112, 610100, '610112', '未央区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610113, 610100, '610113', '雁塔区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610114, 610100, '610114', '阎良区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610115, 610100, '610115', '临潼区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610116, 610100, '610116', '长安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610117, 610100, '610117', '高陵区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610118, 610100, '610118', '鄠邑区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610122, 610100, '610122', '蓝田县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610124, 610100, '610124', '周至县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610200, 610000, '610200', '铜川市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610202, 610200, '610202', '王益区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610203, 610200, '610203', '印台区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610204, 610200, '610204', '耀州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610222, 610200, '610222', '宜君县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610300, 610000, '610300', '宝鸡市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610302, 610300, '610302', '渭滨区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610303, 610300, '610303', '金台区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610304, 610300, '610304', '陈仓区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610322, 610300, '610322', '凤翔县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610323, 610300, '610323', '岐山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610324, 610300, '610324', '扶风县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610326, 610300, '610326', '眉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610327, 610300, '610327', '陇县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610328, 610300, '610328', '千阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610329, 610300, '610329', '麟游县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610330, 610300, '610330', '凤县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610331, 610300, '610331', '太白县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610400, 610000, '610400', '咸阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610402, 610400, '610402', '秦都区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610403, 610400, '610403', '杨陵区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610404, 610400, '610404', '渭城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610422, 610400, '610422', '三原县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610423, 610400, '610423', '泾阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610424, 610400, '610424', '乾县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610425, 610400, '610425', '礼泉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610426, 610400, '610426', '永寿县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610428, 610400, '610428', '长武县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610429, 610400, '610429', '旬邑县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610430, 610400, '610430', '淳化县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610431, 610400, '610431', '武功县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610481, 610400, '610481', '兴平市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610482, 610400, '610482', '彬州市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610500, 610000, '610500', '渭南市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610502, 610500, '610502', '临渭区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610503, 610500, '610503', '华州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610522, 610500, '610522', '潼关县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610523, 610500, '610523', '大荔县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610524, 610500, '610524', '合阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610525, 610500, '610525', '澄城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610526, 610500, '610526', '蒲城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610527, 610500, '610527', '白水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610528, 610500, '610528', '富平县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610581, 610500, '610581', '韩城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610582, 610500, '610582', '华阴市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610600, 610000, '610600', '延安市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610602, 610600, '610602', '宝塔区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610603, 610600, '610603', '安塞区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610621, 610600, '610621', '延长县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610622, 610600, '610622', '延川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610625, 610600, '610625', '志丹县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610626, 610600, '610626', '吴起县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610627, 610600, '610627', '甘泉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610628, 610600, '610628', '富县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610629, 610600, '610629', '洛川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610630, 610600, '610630', '宜川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610631, 610600, '610631', '黄龙县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610632, 610600, '610632', '黄陵县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610681, 610600, '610681', '子长市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610700, 610000, '610700', '汉中市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610702, 610700, '610702', '汉台区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610703, 610700, '610703', '南郑区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610722, 610700, '610722', '城固县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610723, 610700, '610723', '洋县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610724, 610700, '610724', '西乡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610725, 610700, '610725', '勉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610726, 610700, '610726', '宁强县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610727, 610700, '610727', '略阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610728, 610700, '610728', '镇巴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610729, 610700, '610729', '留坝县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610730, 610700, '610730', '佛坪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610800, 610000, '610800', '榆林市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610802, 610800, '610802', '榆阳区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610803, 610800, '610803', '横山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610822, 610800, '610822', '府谷县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610824, 610800, '610824', '靖边县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610825, 610800, '610825', '定边县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610826, 610800, '610826', '绥德县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610827, 610800, '610827', '米脂县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610828, 610800, '610828', '佳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610829, 610800, '610829', '吴堡县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610830, 610800, '610830', '清涧县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610831, 610800, '610831', '子洲县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610881, 610800, '610881', '神木市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610900, 610000, '610900', '安康市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610902, 610900, '610902', '汉滨区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610921, 610900, '610921', '汉阴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610922, 610900, '610922', '石泉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610923, 610900, '610923', '宁陕县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610924, 610900, '610924', '紫阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610925, 610900, '610925', '岚皋县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610926, 610900, '610926', '平利县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610927, 610900, '610927', '镇坪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610928, 610900, '610928', '旬阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (610929, 610900, '610929', '白河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (611000, 610000, '611000', '商洛市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (611002, 611000, '611002', '商州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (611021, 611000, '611021', '洛南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (611022, 611000, '611022', '丹凤县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (611023, 611000, '611023', '商南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (611024, 611000, '611024', '山阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (611025, 611000, '611025', '镇安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (611026, 611000, '611026', '柞水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620000, 86, '620000', '甘肃省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620100, 620000, '620100', '兰州市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620102, 620100, '620102', '城关区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620103, 620100, '620103', '七里河区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620104, 620100, '620104', '西固区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620105, 620100, '620105', '安宁区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620111, 620100, '620111', '红古区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620121, 620100, '620121', '永登县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620122, 620100, '620122', '皋兰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620123, 620100, '620123', '榆中县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620200, 620000, '620200', '嘉峪关市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620300, 620000, '620300', '金昌市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620302, 620300, '620302', '金川区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620321, 620300, '620321', '永昌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620400, 620000, '620400', '白银市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620402, 620400, '620402', '白银区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620403, 620400, '620403', '平川区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620421, 620400, '620421', '靖远县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620422, 620400, '620422', '会宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620423, 620400, '620423', '景泰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620500, 620000, '620500', '天水市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620502, 620500, '620502', '秦州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620503, 620500, '620503', '麦积区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620521, 620500, '620521', '清水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620522, 620500, '620522', '秦安县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620523, 620500, '620523', '甘谷县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620524, 620500, '620524', '武山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620525, 620500, '620525', '张家川回族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620600, 620000, '620600', '武威市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620602, 620600, '620602', '凉州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620621, 620600, '620621', '民勤县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620622, 620600, '620622', '古浪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620623, 620600, '620623', '天祝藏族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620700, 620000, '620700', '张掖市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620702, 620700, '620702', '甘州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620721, 620700, '620721', '肃南裕固族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620722, 620700, '620722', '民乐县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620723, 620700, '620723', '临泽县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620724, 620700, '620724', '高台县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620725, 620700, '620725', '山丹县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620800, 620000, '620800', '平凉市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620802, 620800, '620802', '崆峒区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620821, 620800, '620821', '泾川县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620822, 620800, '620822', '灵台县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620823, 620800, '620823', '崇信县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620825, 620800, '620825', '庄浪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620826, 620800, '620826', '静宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620881, 620800, '620881', '华亭市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620900, 620000, '620900', '酒泉市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620902, 620900, '620902', '肃州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620921, 620900, '620921', '金塔县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620922, 620900, '620922', '瓜州县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620923, 620900, '620923', '肃北蒙古族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620924, 620900, '620924', '阿克塞哈萨克族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620981, 620900, '620981', '玉门市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (620982, 620900, '620982', '敦煌市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621000, 620000, '621000', '庆阳市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621002, 621000, '621002', '西峰区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621021, 621000, '621021', '庆城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621022, 621000, '621022', '环县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621023, 621000, '621023', '华池县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621024, 621000, '621024', '合水县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621025, 621000, '621025', '正宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621026, 621000, '621026', '宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621027, 621000, '621027', '镇原县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621100, 620000, '621100', '定西市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621102, 621100, '621102', '安定区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621121, 621100, '621121', '通渭县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621122, 621100, '621122', '陇西县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621123, 621100, '621123', '渭源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621124, 621100, '621124', '临洮县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621125, 621100, '621125', '漳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621126, 621100, '621126', '岷县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621200, 620000, '621200', '陇南市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621202, 621200, '621202', '武都区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621221, 621200, '621221', '成县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621222, 621200, '621222', '文县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621223, 621200, '621223', '宕昌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621224, 621200, '621224', '康县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621225, 621200, '621225', '西和县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621226, 621200, '621226', '礼县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621227, 621200, '621227', '徽县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (621228, 621200, '621228', '两当县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (622900, 620000, '622900', '临夏回族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (622901, 622900, '622901', '临夏市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (622921, 622900, '622921', '临夏县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (622922, 622900, '622922', '康乐县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (622923, 622900, '622923', '永靖县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (622924, 622900, '622924', '广河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (622925, 622900, '622925', '和政县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (622926, 622900, '622926', '东乡族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (622927, 622900, '622927', '积石山保安族东乡族撒拉族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (623000, 620000, '623000', '甘南藏族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (623001, 623000, '623001', '合作市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (623021, 623000, '623021', '临潭县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (623022, 623000, '623022', '卓尼县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (623023, 623000, '623023', '舟曲县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (623024, 623000, '623024', '迭部县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (623025, 623000, '623025', '玛曲县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (623026, 623000, '623026', '碌曲县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (623027, 623000, '623027', '夏河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630000, 86, '630000', '青海省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630100, 630000, '630100', '西宁市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630102, 630100, '630102', '城东区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630103, 630100, '630103', '城中区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630104, 630100, '630104', '城西区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630105, 630100, '630105', '城北区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630106, 630100, '630106', '湟中区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630121, 630100, '630121', '大通回族土族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630123, 630100, '630123', '湟源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630200, 630000, '630200', '海东市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630202, 630200, '630202', '乐都区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630203, 630200, '630203', '平安区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630222, 630200, '630222', '民和回族土族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630223, 630200, '630223', '互助土族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630224, 630200, '630224', '化隆回族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (630225, 630200, '630225', '循化撒拉族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632200, 630000, '632200', '海北藏族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632221, 632200, '632221', '门源回族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632222, 632200, '632222', '祁连县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632223, 632200, '632223', '海晏县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632224, 632200, '632224', '刚察县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632300, 630000, '632300', '黄南藏族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632301, 632300, '632301', '同仁市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632322, 632300, '632322', '尖扎县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632323, 632300, '632323', '泽库县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632324, 632300, '632324', '河南蒙古族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632500, 630000, '632500', '海南藏族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632521, 632500, '632521', '共和县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632522, 632500, '632522', '同德县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632523, 632500, '632523', '贵德县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632524, 632500, '632524', '兴海县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632525, 632500, '632525', '贵南县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632600, 630000, '632600', '果洛藏族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632621, 632600, '632621', '玛沁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632622, 632600, '632622', '班玛县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632623, 632600, '632623', '甘德县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632624, 632600, '632624', '达日县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632625, 632600, '632625', '久治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632626, 632600, '632626', '玛多县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632700, 630000, '632700', '玉树藏族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632701, 632700, '632701', '玉树市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632722, 632700, '632722', '杂多县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632723, 632700, '632723', '称多县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632724, 632700, '632724', '治多县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632725, 632700, '632725', '囊谦县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632726, 632700, '632726', '曲麻莱县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632800, 630000, '632800', '海西蒙古族藏族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632801, 632800, '632801', '格尔木市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632802, 632800, '632802', '德令哈市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632803, 632800, '632803', '茫崖市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632821, 632800, '632821', '乌兰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632822, 632800, '632822', '都兰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (632823, 632800, '632823', '天峻县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640000, 86, '640000', '宁夏回族自治区', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640100, 640000, '640100', '银川市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640104, 640100, '640104', '兴庆区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640105, 640100, '640105', '西夏区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640106, 640100, '640106', '金凤区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640121, 640100, '640121', '永宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640122, 640100, '640122', '贺兰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640181, 640100, '640181', '灵武市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640200, 640000, '640200', '石嘴山市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640202, 640200, '640202', '大武口区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640205, 640200, '640205', '惠农区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640221, 640200, '640221', '平罗县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640300, 640000, '640300', '吴忠市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640302, 640300, '640302', '利通区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640303, 640300, '640303', '红寺堡区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640323, 640300, '640323', '盐池县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640324, 640300, '640324', '同心县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640381, 640300, '640381', '青铜峡市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640400, 640000, '640400', '固原市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640402, 640400, '640402', '原州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640422, 640400, '640422', '西吉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640423, 640400, '640423', '隆德县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640424, 640400, '640424', '泾源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640425, 640400, '640425', '彭阳县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640500, 640000, '640500', '中卫市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640502, 640500, '640502', '沙坡头区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640521, 640500, '640521', '中宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (640522, 640500, '640522', '海原县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650000, 86, '650000', '新疆维吾尔自治区', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650100, 650000, '650100', '乌鲁木齐市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650102, 650100, '650102', '天山区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650103, 650100, '650103', '沙依巴克区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650104, 650100, '650104', '新市区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650105, 650100, '650105', '水磨沟区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650106, 650100, '650106', '头屯河区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650107, 650100, '650107', '达坂城区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650109, 650100, '650109', '米东区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650121, 650100, '650121', '乌鲁木齐县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650200, 650000, '650200', '克拉玛依市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650202, 650200, '650202', '独山子区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650203, 650200, '650203', '克拉玛依区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650204, 650200, '650204', '白碱滩区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650205, 650200, '650205', '乌尔禾区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650400, 650000, '650400', '吐鲁番市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650402, 650400, '650402', '高昌区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650421, 650400, '650421', '鄯善县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650422, 650400, '650422', '托克逊县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650500, 650000, '650500', '哈密市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650502, 650500, '650502', '伊州区', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650521, 650500, '650521', '巴里坤哈萨克自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (650522, 650500, '650522', '伊吾县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652300, 650000, '652300', '昌吉回族自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652301, 652300, '652301', '昌吉市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652302, 652300, '652302', '阜康市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652323, 652300, '652323', '呼图壁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652324, 652300, '652324', '玛纳斯县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652325, 652300, '652325', '奇台县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652327, 652300, '652327', '吉木萨尔县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652328, 652300, '652328', '木垒哈萨克自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652700, 650000, '652700', '博尔塔拉蒙古自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652701, 652700, '652701', '博乐市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652702, 652700, '652702', '阿拉山口市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652722, 652700, '652722', '精河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652723, 652700, '652723', '温泉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652800, 650000, '652800', '巴音郭楞蒙古自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652801, 652800, '652801', '库尔勒市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652822, 652800, '652822', '轮台县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652823, 652800, '652823', '尉犁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652824, 652800, '652824', '若羌县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652825, 652800, '652825', '且末县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652826, 652800, '652826', '焉耆回族自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652827, 652800, '652827', '和静县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652828, 652800, '652828', '和硕县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652829, 652800, '652829', '博湖县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652900, 650000, '652900', '阿克苏地区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652901, 652900, '652901', '阿克苏市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652902, 652900, '652902', '库车市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652922, 652900, '652922', '温宿县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652924, 652900, '652924', '沙雅县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652925, 652900, '652925', '新和县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652926, 652900, '652926', '拜城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652927, 652900, '652927', '乌什县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652928, 652900, '652928', '阿瓦提县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (652929, 652900, '652929', '柯坪县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653000, 650000, '653000', '克孜勒苏柯尔克孜自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653001, 653000, '653001', '阿图什市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653022, 653000, '653022', '阿克陶县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653023, 653000, '653023', '阿合奇县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653024, 653000, '653024', '乌恰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653100, 650000, '653100', '喀什地区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653101, 653100, '653101', '喀什市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653121, 653100, '653121', '疏附县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653122, 653100, '653122', '疏勒县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653123, 653100, '653123', '英吉沙县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653124, 653100, '653124', '泽普县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653125, 653100, '653125', '莎车县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653126, 653100, '653126', '叶城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653127, 653100, '653127', '麦盖提县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653128, 653100, '653128', '岳普湖县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653129, 653100, '653129', '伽师县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653130, 653100, '653130', '巴楚县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653131, 653100, '653131', '塔什库尔干塔吉克自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653200, 650000, '653200', '和田地区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653201, 653200, '653201', '和田市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653221, 653200, '653221', '和田县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653222, 653200, '653222', '墨玉县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653223, 653200, '653223', '皮山县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653224, 653200, '653224', '洛浦县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653225, 653200, '653225', '策勒县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653226, 653200, '653226', '于田县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (653227, 653200, '653227', '民丰县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654000, 650000, '654000', '伊犁哈萨克自治州', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654002, 654000, '654002', '伊宁市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654003, 654000, '654003', '奎屯市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654004, 654000, '654004', '霍尔果斯市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654021, 654000, '654021', '伊宁县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654022, 654000, '654022', '察布查尔锡伯自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654023, 654000, '654023', '霍城县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654024, 654000, '654024', '巩留县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654025, 654000, '654025', '新源县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654026, 654000, '654026', '昭苏县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654027, 654000, '654027', '特克斯县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654028, 654000, '654028', '尼勒克县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654200, 650000, '654200', '塔城地区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654201, 654200, '654201', '塔城市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654202, 654200, '654202', '乌苏市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654221, 654200, '654221', '额敏县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654223, 654200, '654223', '沙湾县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654224, 654200, '654224', '托里县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654225, 654200, '654225', '裕民县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654226, 654200, '654226', '和布克赛尔蒙古自治县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654300, 650000, '654300', '阿勒泰地区', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654301, 654300, '654301', '阿勒泰市', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654321, 654300, '654321', '布尔津县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654322, 654300, '654322', '富蕴县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654323, 654300, '654323', '福海县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654324, 654300, '654324', '哈巴河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654325, 654300, '654325', '青河县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (654326, 654300, '654326', '吉木乃县', 3, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (659001, 650000, '659001', '石河子市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (659002, 650000, '659002', '阿拉尔市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (659003, 650000, '659003', '图木舒克市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (659004, 650000, '659004', '五家渠市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (659005, 650000, '659005', '北屯市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (659006, 650000, '659006', '铁门关市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (659007, 650000, '659007', '双河市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (659008, 650000, '659008', '可克达拉市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (659009, 650000, '659009', '昆玉市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (659010, 650000, '659010', '胡杨河市', 2, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (710000, 86, '710000', '台湾省', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (810000, 86, '810000', '香港特别行政区', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
INSERT INTO `sys_area` VALUES (820000, 86, '820000', '澳门特别行政区', 1, '0', 0, 1, '2020-12-28 17:43:31', 1, '2020-12-28 17:43:31', '2021-04-08 23:50:42');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(19) NOT NULL COMMENT '字典主键',
  `type_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典编号',
  `type_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `iz_lock` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否内置 0否  1是',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标记:0未删除，1删除',
  `version` int(11) NOT NULL COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(19) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `type_code_typename_unique` (`type_code`,`type_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` VALUES (1308396497528434689, '010110', '测试字典123', '1', '测试修改 - 123123', '1', 2, 1, '2020-09-22 08:23:28', 1, '2020-09-22 10:59:29', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1308782322607058946, 'test_type', '测试类型', '1', '测试类型', '0', 23, 1, '2020-09-23 09:56:36', 1, '2020-11-21 17:55:50', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1314920925140922369, 'test', '测试', '0', NULL, '0', 3, 1313694379541635074, '2020-10-10 21:29:13', 1313694379541635074, '2020-10-10 21:57:32', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1314939057985335297, 'no_yes', '否是', '1', '用与 否是 判断', '0', 0, 1313694379541635074, '2020-10-10 22:41:16', 1313694379541635074, '2020-10-10 22:41:16', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1314939286306467841, 'menu_type', '菜单类型', '1', NULL, '0', 2, 1313694379541635074, '2020-10-10 22:42:11', 1, '2020-10-11 09:56:11', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1327879501833408513, 'table_type', '代码生成器v表类型', '1', '代码生成器', '0', 3, 1313694379541635074, '2020-11-15 15:41:59', 1, '2020-11-16 09:59:49', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1327979234979590146, 'jdbc_type', '代码生成器v数据库类型', '1', '代码生成器', '0', 1, 1313694379541635074, '2020-11-15 22:18:17', 1, '2020-11-16 09:59:45', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1328148882811367425, 'mysql_data_type', '代码生成器vMySQL数据类型', '1', '代码生成器\n', '0', 3, 1313694379541635074, '2020-11-16 09:32:24', 1, '2020-11-16 09:59:55', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1328155300805324801, 'show_type', '代码生成器v显示类型', '1', '代码生成器\n', '0', 2, 1, '2020-11-16 09:57:54', 1, '2020-11-16 09:59:59', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1328591039258980353, 'validate_type', '代码生成器v验证类别', '1', '代码生成器', '0', 1, 1313694379541635074, '2020-11-17 14:49:22', 1, '2020-11-17 14:49:43', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1329005625682427905, 'java_data_type', '代码生成器vJava数据类型', '1', '代码生成器', '0', 1, 1313694379541635074, '2020-11-18 18:16:48', 1, '2020-11-18 18:17:09', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1330086884696694786, 'query_type', '代码生成器v检索类别', '1', '代码生成器', '0', 0, 1, '2020-11-21 17:53:20', 1, '2020-11-21 17:53:20', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1332662182483107842, 'org_type', '组织机构类型', '1', NULL, '0', 0, 1, '2020-11-28 20:26:39', 1, '2020-11-28 20:26:39', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1346735875094847489, '123123', '测试', '0', NULL, '1', 0, 1313694379541635074, '2021-01-06 16:30:28', 1313694379541635074, '2021-01-06 16:30:28', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1380172509525946369, 'crypto_asymmetric', '非对称加密', '1', NULL, '0', 0, 1313694379541635074, '2021-04-08 22:55:43', 1313694379541635074, '2021-04-08 22:55:43', '2021-04-08 23:50:52');
INSERT INTO `sys_dict` VALUES (1388562098925977601, 'storage_type', '存储位置', '1', NULL, '0', 1, 1313694379541635074, '2021-05-02 02:32:57', 1, '2021-05-02 02:34:39', '2021-05-02 02:34:38');
INSERT INTO `sys_dict` VALUES (1389291635615805442, 'password_level', '密码强度', '1', NULL, '0', 0, 1313694379541635074, '2021-05-04 02:51:52', 1313694379541635074, '2021-05-04 02:51:52', '2021-05-04 02:51:52');
INSERT INTO `sys_dict` VALUES (1448557351479685121, 'role_data_scope', '角色数据范围', '1', NULL, '0', 0, 1, '2021-10-14 15:52:40', 1, '2021-10-14 15:52:40', '2021-10-14 15:50:56');
INSERT INTO `sys_dict` VALUES (1463430609630846978, 'menu_role_label', '菜单角色类型标签', '1', NULL, '0', 0, 1, '2021-11-24 16:53:41', 1, '2021-11-24 16:53:41', '2021-11-24 16:51:41');
INSERT INTO `sys_dict` VALUES (1551869799540985858, 'login_from', '登陆来源', '1', NULL, '0', 0, 1, '2022-07-26 17:59:27', 1, '2022-07-26 17:59:27', '2022-07-26 17:59:27');
INSERT INTO `sys_dict` VALUES (1551894955382558721, 'log_type', '日志类型', '1', NULL, '0', 0, 1, '2022-07-26 19:39:25', 1, '2022-07-26 19:39:25', '2022-07-26 19:39:24');
INSERT INTO `sys_dict` VALUES (1551895408610660353, 'log_level', '日志等级', '1', NULL, '0', 1, 1, '2022-07-26 19:41:13', 1, '2022-07-27 11:18:18', '2022-07-27 11:18:17');
INSERT INTO `sys_dict` VALUES (1551896061743484929, 'log_operation_type', '日志操作类型', '1', NULL, '0', 0, 1, '2022-07-26 19:43:49', 1, '2022-07-26 19:43:49', '2022-07-26 19:43:48');
INSERT INTO `sys_dict` VALUES (1551896925380038657, 'log_model_type', '日志模块类型', '1', NULL, '0', 5, 1, '2022-07-26 19:47:15', 1, '2022-07-27 10:21:11', '2022-07-27 10:21:11');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_detail
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_detail`;
CREATE TABLE `sys_dict_detail` (
  `id` bigint(19) NOT NULL COMMENT '字典明细主键',
  `type_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型ID',
  `type_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型code 冗余字段',
  `dict_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `dict_value` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典值',
  `iz_lock` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否内置 0否  1是',
  `sort_no` int(11) NOT NULL COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除状态',
  `version` int(11) NOT NULL COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(19) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `dict_detail` (`type_code`,`dict_value`,`dict_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典表-明细';

-- ----------------------------
-- Records of sys_dict_detail
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_detail` VALUES (1308456445335597058, '1308396497528434689', '010110', 'abc', '0', '1', 0, '测试', '1', 1, 1, '2020-09-22 12:21:41', 1, '2020-09-22 12:22:11', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1308637708302524418, '1308396497528434689', '010110', '销售部', '0', '1', 0, '测试', '1', 0, 1, '2020-09-23 00:21:58', 1, '2020-09-23 00:21:58', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1308637974456291330, '1308396497528434689', '010110', '销售部', '0', '1', 0, '测试', '1', 0, 1, '2020-09-23 00:23:01', 1, '2020-09-23 00:23:01', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1308638141389590529, '1308396497528434689', '010110', '销售部', '0', '1', 0, '测试', '1', 0, 1, '2020-09-23 00:23:41', 1, '2020-09-23 00:23:41', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1308715719302586370, '1308396497528434689', '010110', '销售部', '0', '1', 0, '测试', '1', 0, 1, '2020-09-23 05:31:57', 1, '2020-09-23 05:31:57', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1308716901383294978, '1308396497528434689', '010110', 'abc', '0', '1', 0, '测试', '1', 1, 1, '2020-09-23 05:36:39', 1, '2020-09-23 05:37:26', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1308782561162293250, '1308782322607058946', 'test_type', '销售部', '0', '1', 0, '测试', '0', 2, 1, '2020-09-23 09:57:33', 1, '2020-11-21 17:55:50', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1308783750910078977, '1308782322607058946', 'test_type', '售后部', '1', '1', 0, '测试', '0', 16, 1, '2020-09-23 10:02:17', 1, '2020-11-21 17:55:50', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1308783873136291842, '1308782322607058946', 'test_type', '产品部', '2', '1', 0, '测试', '0', 16, 1, '2020-09-23 10:02:46', 1, '2020-11-21 17:55:50', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1308783908678823937, '1308782322607058946', 'test_type', '研发部', '3', '1', 0, '测试', '0', 16, 1, '2020-09-23 10:02:55', 1, '2020-11-21 17:55:50', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1308783942631714817, '1308782322607058946', 'test_type', '测试部', '4', '1', 0, '测试', '0', 16, 1, '2020-09-23 10:03:03', 1, '2020-11-21 17:55:50', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1308783992590069761, '1308782322607058946', 'test_type', '售前部', '5', '1', 0, '测试', '0', 16, 1, '2020-09-23 10:03:15', 1, '2020-11-21 17:55:50', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1308784042569396225, '1308782322607058946', 'test_type', '设计部', '6', '1', 0, '测试', '0', 16, 1, '2020-09-23 10:03:26', 1, '2020-11-21 17:55:50', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1314938241580838913, '1314920925140922369', 'test', '测试', '1', '0', 3, NULL, '0', 1, 1313694379541635074, '2020-10-10 22:38:02', 1313694379541635074, '2020-10-10 22:40:16', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1314938442731270146, '1314920925140922369', 'test', '测试2', '2', '0', 2, NULL, '0', 2, 1313694379541635074, '2020-10-10 22:38:50', 1313694379541635074, '2020-10-11 10:21:30', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1314939126788698114, '1314939057985335297', 'no_yes', '否', '0', '1', 1, NULL, '0', 0, 1313694379541635074, '2020-10-10 22:41:33', 1313694379541635074, '2020-10-10 22:41:33', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1314939176172433409, '1314939057985335297', 'no_yes', '是', '1', '1', 2, NULL, '0', 0, 1313694379541635074, '2020-10-10 22:41:45', 1313694379541635074, '2020-10-10 22:41:45', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1314939361581641730, '1314939286306467841', 'menu_type', '菜单', '1', '1', 1, NULL, '0', 3, 1313694379541635074, '2020-10-10 22:42:29', 1, '2020-10-11 09:56:11', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1314939393605152770, '1314939286306467841', 'menu_type', '按钮', '2', '1', 2, NULL, '0', 2, 1313694379541635074, '2020-10-10 22:42:36', 1, '2020-10-11 09:56:11', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1314939432264052738, '1314939286306467841', 'menu_type', '外链', '3', '1', 3, NULL, '0', 2, 1313694379541635074, '2020-10-10 22:42:46', 1, '2020-10-11 09:56:11', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1327880400806977537, '1327879501833408513', 'table_type', '单表', '0', '1', 1, NULL, '0', 1, 1, '2020-11-15 15:45:33', 1, '2020-11-16 09:59:49', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1327880503319961602, '1327879501833408513', 'table_type', '树表', '1', '1', 2, NULL, '1', 1, 1, '2020-11-15 15:45:57', 1, '2020-11-16 09:59:49', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1327979358606700545, '1327979234979590146', 'jdbc_type', 'MySQL', 'mysql', '1', 1, NULL, '0', 2, 1, '2020-11-15 22:18:46', 1, '2020-11-16 09:59:45', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149020426481665, '1328148882811367425', 'mysql_data_type', 'tinyint', 'tinyint', '1', 1, NULL, '0', 3, 1, '2020-11-16 09:32:57', 1, '2020-11-16 09:59:55', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149045797826561, '1328148882811367425', 'mysql_data_type', 'smallint', 'smallint', '1', 2, NULL, '0', 4, 1, '2020-11-16 09:33:03', 1, '2020-11-16 09:59:55', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149070607134721, '1328148882811367425', 'mysql_data_type', 'mediumint', 'mediumint', '1', 3, NULL, '0', 3, 1, '2020-11-16 09:33:09', 1, '2020-11-16 09:59:55', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149095005401090, '1328148882811367425', 'mysql_data_type', 'int', 'int', '1', 4, NULL, '0', 3, 1, '2020-11-16 09:33:15', 1, '2020-11-16 09:59:55', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149122557784065, '1328148882811367425', 'mysql_data_type', 'bigint', 'bigint', '1', 5, NULL, '0', 3, 1, '2020-11-16 09:33:21', 1, '2020-11-16 09:59:55', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149155734728706, '1328148882811367425', 'mysql_data_type', 'float', 'float', '1', 6, NULL, '0', 3, 1, '2020-11-16 09:33:29', 1, '2020-11-16 09:59:55', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149182955761665, '1328148882811367425', 'mysql_data_type', 'double', 'double', '1', 7, NULL, '0', 3, 1, '2020-11-16 09:33:36', 1, '2020-11-16 09:59:55', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149212169089026, '1328148882811367425', 'mysql_data_type', 'char', 'char', '1', 9, NULL, '0', 4, 1, '2020-11-16 09:33:43', 1, '2020-11-18 17:36:44', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149233459376130, '1328148882811367425', 'mysql_data_type', 'varchar', 'varchar', '1', 10, NULL, '0', 4, 1, '2020-11-16 09:33:48', 1, '2020-11-18 17:36:52', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149257891196929, '1328148882811367425', 'mysql_data_type', 'tinytext', 'tinytext', '1', 11, NULL, '0', 4, 1, '2020-11-16 09:33:54', 1, '2020-11-18 17:36:58', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149280972451842, '1328148882811367425', 'mysql_data_type', 'text', 'text', '1', 12, NULL, '0', 4, 1, '2020-11-16 09:33:59', 1, '2020-11-18 17:37:09', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149313922904065, '1328148882811367425', 'mysql_data_type', 'mediumtext', 'mediumtext', '1', 13, NULL, '0', 4, 1, '2020-11-16 09:34:07', 1, '2020-11-18 17:37:12', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149342096044034, '1328148882811367425', 'mysql_data_type', 'longtext', 'longtext', '1', 14, NULL, '0', 4, 1, '2020-11-16 09:34:14', 1, '2020-11-18 17:37:16', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149374299910145, '1328148882811367425', 'mysql_data_type', 'date', 'date', '1', 15, NULL, '0', 4, 1, '2020-11-16 09:34:21', 1, '2020-11-18 17:37:19', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328149989512032258, '1328148882811367425', 'mysql_data_type', 'time', 'time', '1', 16, NULL, '0', 3, 1, '2020-11-16 09:36:48', 1, '2020-11-18 17:37:23', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328150026975555585, '1328148882811367425', 'mysql_data_type', 'datetime', 'datetime', '1', 17, NULL, '0', 3, 1, '2020-11-16 09:36:57', 1, '2020-11-18 17:37:25', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328150062320955393, '1328148882811367425', 'mysql_data_type', 'timestamp', 'timestamp', '1', 18, NULL, '0', 3, 1, '2020-11-16 09:37:05', 1, '2020-11-18 17:37:28', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328156042349883394, '1328155300805324801', 'show_type', '文本框', '0', '1', 1, NULL, '0', 1, 1, '2020-11-16 10:00:51', 1, '2020-11-16 10:00:56', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328156097270099970, '1328155300805324801', 'show_type', '文本域', '1', '1', 2, NULL, '0', 1, 1, '2020-11-16 10:01:04', 1, '2020-11-16 10:01:08', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328156163892424705, '1328155300805324801', 'show_type', '字典选择', '2', '1', 3, NULL, '0', 0, 1, '2020-11-16 10:01:20', 1, '2020-11-16 10:01:20', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328363850953424898, '1327979234979590146', 'jdbc_type', 'Oracle', 'oracle', '0', 2, NULL, '1', 0, 1, '2020-11-16 23:46:37', 1, '2020-11-16 23:46:37', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328591278510469121, '1328591039258980353', 'validate_type', '不能为空', 'IS_NOT_NULL', '1', 1, NULL, '0', 0, 1, '2020-11-17 14:50:19', 1, '2020-11-17 14:50:19', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1328591456541896705, '1328591039258980353', 'validate_type', '字母数字和下划线', 'IS_GENERAL', '1', 16, NULL, '0', 2, 1, '2020-11-17 14:51:02', 1, '2021-05-24 19:28:32', '2021-05-24 19:28:31');
INSERT INTO `sys_dict_detail` VALUES (1328591535440949250, '1328591039258980353', 'validate_type', '数字', 'IS_NUMBER', '1', 3, NULL, '1', 0, 1, '2020-11-17 14:51:21', 1, '2020-11-17 14:51:21', '2021-05-24 19:27:11');
INSERT INTO `sys_dict_detail` VALUES (1328591581578293249, '1328591039258980353', 'validate_type', '纯字母', 'IS_LETTER', '1', 5, NULL, '0', 1, 1, '2020-11-17 14:51:32', 1, '2021-05-24 19:26:07', '2021-05-24 19:26:07');
INSERT INTO `sys_dict_detail` VALUES (1328591630144139266, '1328591039258980353', 'validate_type', '大写', 'IS_UPPER_CASE', '1', 6, NULL, '0', 1, 1, '2020-11-17 14:51:43', 1, '2021-05-24 19:26:11', '2021-05-24 19:26:10');
INSERT INTO `sys_dict_detail` VALUES (1328591723169607682, '1328591039258980353', 'validate_type', '小写', 'IS_LOWER_CASE', '1', 7, NULL, '0', 1, 1, '2020-11-17 14:52:06', 1, '2021-05-24 19:26:17', '2021-05-24 19:26:16');
INSERT INTO `sys_dict_detail` VALUES (1328591774579191810, '1328591039258980353', 'validate_type', 'Ipv4', 'IS_IPV4', '1', 9, NULL, '0', 2, 1, '2020-11-17 14:52:18', 1, '2021-05-24 19:26:37', '2021-05-24 19:26:36');
INSERT INTO `sys_dict_detail` VALUES (1328591832049545218, '1328591039258980353', 'validate_type', '金额', 'IS_MONEY', '1', 11, NULL, '0', 1, 1, '2020-11-17 14:52:31', 1, '2021-05-24 19:27:32', '2021-05-24 19:27:31');
INSERT INTO `sys_dict_detail` VALUES (1328591886462251009, '1328591039258980353', 'validate_type', '邮箱', 'IS_EMAIL', '1', 12, NULL, '0', 1, 1, '2020-11-17 14:52:44', 1, '2021-05-24 19:27:36', '2021-05-24 19:27:36');
INSERT INTO `sys_dict_detail` VALUES (1328591938366763010, '1328591039258980353', 'validate_type', '手机号', 'IS_MOBILE', '1', 13, NULL, '0', 1, 1, '2020-11-17 14:52:57', 1, '2021-05-24 19:27:43', '2021-05-24 19:27:43');
INSERT INTO `sys_dict_detail` VALUES (1328592000949972993, '1328591039258980353', 'validate_type', '18位身份证', 'IS_CITIZENID', '1', 14, NULL, '0', 1, 1, '2020-11-17 14:53:12', 1, '2021-05-24 19:28:07', '2021-05-24 19:28:06');
INSERT INTO `sys_dict_detail` VALUES (1328592049868140546, '1328591039258980353', 'validate_type', '邮编', 'IS_ZIPCODE', '1', 18, NULL, '0', 1, 1, '2020-11-17 14:53:23', 1, '2021-05-24 19:29:09', '2021-05-24 19:29:09');
INSERT INTO `sys_dict_detail` VALUES (1328592085637165058, '1328591039258980353', 'validate_type', 'URL', 'URL', '1', 19, NULL, '0', 1, 1, '2020-11-17 14:53:32', 1, '2021-05-24 19:29:14', '2021-05-24 19:29:14');
INSERT INTO `sys_dict_detail` VALUES (1328592136035921921, '1328591039258980353', 'validate_type', '汉字', 'IS_CHINESE', '1', 15, NULL, '0', 1, 1, '2020-11-17 14:53:44', 1, '2021-05-24 19:28:19', '2021-05-24 19:28:18');
INSERT INTO `sys_dict_detail` VALUES (1328592345788870658, '1328591039258980353', 'validate_type', '汉字或字母或数字或下划线', 'IS_GENERAL_WITH_CHINESE', '1', 17, NULL, '0', 1, 1, '2020-11-17 14:54:34', 1, '2021-05-24 19:28:39', '2021-05-24 19:28:39');
INSERT INTO `sys_dict_detail` VALUES (1328592395894026242, '1328591039258980353', 'validate_type', 'MAC地址', 'IS_MAC', '1', 20, NULL, '0', 1, 1, '2020-11-17 14:54:46', 1, '2021-05-24 19:29:20', '2021-05-24 19:29:19');
INSERT INTO `sys_dict_detail` VALUES (1328592440106184705, '1328591039258980353', 'validate_type', '中国车牌', 'IS_PLATE_NUMBER', '1', 21, NULL, '0', 1, 1, '2020-11-17 14:54:56', 1, '2021-05-24 19:29:26', '2021-05-24 19:29:26');
INSERT INTO `sys_dict_detail` VALUES (1328995908293754881, '1328148882811367425', 'mysql_data_type', 'decimal', 'decimal', '1', 8, NULL, '0', 1, 1, '2020-11-18 17:38:11', 1, '2020-11-18 17:38:17', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1329006016235044865, '1329005625682427905', 'java_data_type', 'String', 'String', '1', 1, NULL, '0', 0, 1, '2020-11-18 18:18:21', 1, '2020-11-18 18:18:21', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1329006101010317313, '1329005625682427905', 'java_data_type', 'Byte', 'Byte', '1', 2, NULL, '0', 0, 1, '2020-11-18 18:18:41', 1, '2020-11-18 18:18:41', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1329006140738764802, '1329005625682427905', 'java_data_type', 'Short', 'Short', '1', 3, NULL, '0', 0, 1, '2020-11-18 18:18:50', 1, '2020-11-18 18:18:50', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1329006178210676738, '1329005625682427905', 'java_data_type', 'Integer', 'Integer', '1', 4, NULL, '0', 0, 1, '2020-11-18 18:18:59', 1, '2020-11-18 18:18:59', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1329006216005550081, '1329005625682427905', 'java_data_type', 'Long', 'Long', '1', 5, NULL, '0', 0, 1, '2020-11-18 18:19:08', 1, '2020-11-18 18:19:08', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1329006253049643010, '1329005625682427905', 'java_data_type', 'Float', 'Float', '1', 6, NULL, '0', 0, 1, '2020-11-18 18:19:17', 1, '2020-11-18 18:19:17', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1329006288294379521, '1329005625682427905', 'java_data_type', 'Double', 'Double', '1', 7, NULL, '0', 0, 1, '2020-11-18 18:19:26', 1, '2020-11-18 18:19:26', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1329006351808724993, '1329005625682427905', 'java_data_type', 'Character', 'Character', '1', 8, NULL, '0', 0, 1, '2020-11-18 18:19:41', 1, '2020-11-18 18:19:41', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1329006390220161025, '1329005625682427905', 'java_data_type', 'Boolean', 'Boolean', '1', 9, NULL, '0', 0, 1, '2020-11-18 18:19:50', 1, '2020-11-18 18:19:50', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1329012836630634497, '1329005625682427905', 'java_data_type', 'Date', 'Date', '1', 10, NULL, '0', 3, 1, '2020-11-18 18:45:27', 1, '2020-11-18 19:25:43', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1329025038242664450, '1328155300805324801', 'show_type', '时间控件', '3', '1', 4, NULL, '0', 1, 1, '2020-11-18 19:33:56', 1, '2020-11-27 14:21:24', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1330086999800979457, '1330086884696694786', 'query_type', '全值匹配', 'EQ', '1', 1, NULL, '0', 0, 1, '2020-11-21 17:53:47', 1, '2020-11-21 17:53:47', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1330087053454516225, '1330086884696694786', 'query_type', '模糊匹配', 'LIKE', '1', 2, NULL, '0', 0, 1, '2020-11-21 17:54:00', 1, '2020-11-21 17:54:00', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1330087452634816514, '1330086884696694786', 'query_type', '范围匹配', 'RANGE', '1', 3, NULL, '0', 0, 1, '2020-11-21 17:55:35', 1, '2020-11-21 17:55:35', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1332207946348371970, '1328155300805324801', 'show_type', '日期控件', '4', '1', 5, NULL, '0', 1, 1, '2020-11-27 14:21:40', 1, '2020-11-27 15:41:36', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1332662237399130113, '1332662182483107842', 'org_type', '公司', '1', '1', 1, NULL, '0', 0, 1, '2020-11-28 20:26:52', 1, '2020-11-28 20:26:52', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1332662266016866306, '1332662182483107842', 'org_type', '部门', '2', '1', 2, NULL, '0', 0, 1, '2020-11-28 20:26:59', 1, '2020-11-28 20:26:59', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1332662299038621697, '1332662182483107842', 'org_type', '岗位', '3', '1', 3, NULL, '0', 0, 1, '2020-11-28 20:27:06', 1, '2020-11-28 20:27:06', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1346735934725267457, '1346735875094847489', '123123', '123', '123', '0', 1, NULL, '1', 0, 1313694379541635074, '2021-01-06 16:30:43', 1313694379541635074, '2021-01-06 16:30:43', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1346735949665378306, '1346735875094847489', '123123', '345', '345', '0', 1, NULL, '1', 0, 1313694379541635074, '2021-01-06 16:30:46', 1313694379541635074, '2021-01-06 16:30:46', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1346735962680303617, '1346735875094847489', '123123', '567', '567', '0', 1, NULL, '1', 0, 1313694379541635074, '2021-01-06 16:30:49', 1313694379541635074, '2021-01-06 16:30:49', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1346735975552622594, '1346735875094847489', '123123', '789', '789', '0', 1, NULL, '1', 0, 1313694379541635074, '2021-01-06 16:30:52', 1313694379541635074, '2021-01-06 16:30:52', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1346735992405336065, '1346735875094847489', '123123', '1244', '124314', '0', 1, NULL, '1', 0, 1313694379541635074, '2021-01-06 16:30:56', 1313694379541635074, '2021-01-06 16:30:56', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1346736008574377985, '1346735875094847489', '123123', '12332423', '12312312', '0', 1, NULL, '1', 0, 1313694379541635074, '2021-01-06 16:31:00', 1313694379541635074, '2021-01-06 16:31:00', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1346736026190454785, '1346735875094847489', '123123', '123123', '12312333', '0', 1, NULL, '1', 0, 1313694379541635074, '2021-01-06 16:31:04', 1313694379541635074, '2021-01-06 16:31:04', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1358741824809709569, '1308782322607058946', 'test_type', '售后部1', '11', '0', 1, NULL, '1', 0, 1, '2021-02-08 19:37:50', 1, '2021-02-08 19:37:50', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1380172691483242497, '1380172509525946369', 'crypto_asymmetric', 'RSA', 'RSA', '1', 1, NULL, '0', 0, 1, '2021-04-08 22:56:27', 1, '2021-04-08 22:56:27', '2021-04-08 23:50:57');
INSERT INTO `sys_dict_detail` VALUES (1388562258682822658, '1388562098925977601', 'storage_type', '本地', 'local', '1', 1, NULL, '0', 1, 1, '2021-05-02 02:33:35', 1, '2021-05-02 02:34:39', '2021-05-02 02:34:38');
INSERT INTO `sys_dict_detail` VALUES (1389291796828073985, '1389291635615805442', 'password_level', '低', '0', '1', 1, NULL, '0', 0, 1, '2021-05-04 02:52:31', 1, '2021-05-04 02:52:31', '2021-05-04 02:52:30');
INSERT INTO `sys_dict_detail` VALUES (1389291836380360706, '1389291635615805442', 'password_level', '中', '1', '1', 2, NULL, '0', 0, 1, '2021-05-04 02:52:40', 1, '2021-05-04 02:52:40', '2021-05-04 02:52:40');
INSERT INTO `sys_dict_detail` VALUES (1389291876108808193, '1389291635615805442', 'password_level', '高', '2', '1', 3, NULL, '0', 0, 1, '2021-05-04 02:52:50', 1, '2021-05-04 02:52:50', '2021-05-04 02:52:49');
INSERT INTO `sys_dict_detail` VALUES (1389291921797361666, '1389291635615805442', 'password_level', '很高', '3', '1', 4, NULL, '0', 0, 1, '2021-05-04 02:53:01', 1, '2021-05-04 02:53:01', '2021-05-04 02:53:00');
INSERT INTO `sys_dict_detail` VALUES (1389291969885057026, '1389291635615805442', 'password_level', '非常高', '4', '1', 5, NULL, '0', 0, 1, '2021-05-04 02:53:12', 1, '2021-05-04 02:53:12', '2021-05-04 02:53:12');
INSERT INTO `sys_dict_detail` VALUES (1396787751738630146, '1328591039258980353', 'validate_type', 'Ipv6', 'IS_IPV6', '1', 10, NULL, '0', 1, 1, '2021-05-24 19:18:46', 1, '2021-05-24 19:26:47', '2021-05-24 19:26:46');
INSERT INTO `sys_dict_detail` VALUES (1396787839189868545, '1328591039258980353', 'validate_type', 'Ip', 'IS_IP', '1', 8, NULL, '0', 1, 1, '2021-05-24 19:19:07', 1, '2021-05-24 19:26:30', '2021-05-24 19:26:30');
INSERT INTO `sys_dict_detail` VALUES (1396789224945967105, '1328591039258980353', 'validate_type', '整数', 'IS_INTEGER', '1', 2, NULL, '0', 1, 1, '2021-05-24 19:24:37', 1, '2021-05-24 19:25:43', '2021-05-24 19:25:43');
INSERT INTO `sys_dict_detail` VALUES (1396789343724462082, '1328591039258980353', 'validate_type', '浮点数', 'IS_DECIMAL', '1', 3, NULL, '0', 1, 1, '2021-05-24 19:25:05', 1, '2021-05-24 19:25:50', '2021-05-24 19:25:49');
INSERT INTO `sys_dict_detail` VALUES (1396789437249052674, '1328591039258980353', 'validate_type', '质数_素数', 'IS_PRIMES', '1', 4, NULL, '0', 1, 1, '2021-05-24 19:25:28', 1, '2021-05-24 19:25:54', '2021-05-24 19:25:54');
INSERT INTO `sys_dict_detail` VALUES (1396790553273655298, '1328591039258980353', 'validate_type', '安全密码', 'IS_SECURITY_PASSWORD', '1', 22, NULL, '0', 0, 1, '2021-05-24 19:29:54', 1, '2021-05-24 19:29:54', '2021-05-24 19:29:53');
INSERT INTO `sys_dict_detail` VALUES (1400399500568121346, '1388562098925977601', 'storage_type', '又拍云', 'upYun', '1', 2, NULL, '0', 0, 1, '2021-06-03 18:30:34', 1, '2021-06-03 18:30:34', '2021-06-03 18:30:33');
INSERT INTO `sys_dict_detail` VALUES (1448557451719356417, '1448557351479685121', 'role_data_scope', '仅本人数据', '0', '1', 1, NULL, '0', 0, 1, '2021-10-14 15:53:04', 1, '2021-10-14 15:53:04', '2021-10-14 15:51:20');
INSERT INTO `sys_dict_detail` VALUES (1448557556665036802, '1448557351479685121', 'role_data_scope', '本部门数据', '1', '1', 2, NULL, '0', 0, 1, '2021-10-14 15:53:29', 1, '2021-10-14 15:53:29', '2021-10-14 15:51:45');
INSERT INTO `sys_dict_detail` VALUES (1448557649011027969, '1448557351479685121', 'role_data_scope', '本部门及以下数据', '2', '1', 3, NULL, '0', 0, 1, '2021-10-14 15:53:51', 1, '2021-10-14 15:53:51', '2021-10-14 15:52:07');
INSERT INTO `sys_dict_detail` VALUES (1448557740778205185, '1448557351479685121', 'role_data_scope', '全部数据', '3', '1', 4, NULL, '0', 0, 1, '2021-10-14 15:54:13', 1, '2021-10-14 15:54:13', '2021-10-14 15:52:29');
INSERT INTO `sys_dict_detail` VALUES (1463430673044529154, '1463430609630846978', 'menu_role_label', '系统模块', '0', '1', 1, NULL, '0', 0, 1, '2021-11-24 16:53:56', 1, '2021-11-24 16:53:56', '2021-11-24 16:51:57');
INSERT INTO `sys_dict_detail` VALUES (1463430782889156609, '1463430609630846978', 'menu_role_label', '功能模块', '1', '1', 2, NULL, '0', 0, 1, '2021-11-24 16:54:23', 1, '2021-11-24 16:54:23', '2021-11-24 16:52:23');
INSERT INTO `sys_dict_detail` VALUES (1551870261627457538, '1551869799540985858', 'login_from', 'PC', '0', '1', 1, NULL, '0', 0, 1, '2022-07-26 18:01:17', 1, '2022-07-26 18:01:17', '2022-07-26 18:01:17');
INSERT INTO `sys_dict_detail` VALUES (1551870336856494081, '1551869799540985858', 'login_from', 'Android', '1', '1', 2, NULL, '0', 1, 1, '2022-07-26 18:01:35', 1, '2022-07-26 18:02:18', '2022-07-26 18:02:18');
INSERT INTO `sys_dict_detail` VALUES (1551870413763252225, '1551869799540985858', 'login_from', 'IOS', '2', '1', 3, NULL, '0', 0, 1, '2022-07-26 18:01:54', 1, '2022-07-26 18:01:54', '2022-07-26 18:01:53');
INSERT INTO `sys_dict_detail` VALUES (1551870759940132866, '1551869799540985858', 'login_from', 'H5', '3', '1', 4, NULL, '0', 0, 1, '2022-07-26 18:03:16', 1, '2022-07-26 18:03:16', '2022-07-26 18:03:16');
INSERT INTO `sys_dict_detail` VALUES (1551870831604011009, '1551869799540985858', 'login_from', '未知', '-1', '1', 99, NULL, '0', 0, 1, '2022-07-26 18:03:33', 1, '2022-07-26 18:03:33', '2022-07-26 18:03:33');
INSERT INTO `sys_dict_detail` VALUES (1551895152049278977, '1551894955382558721', 'log_type', 'WEB', '0', '1', 1, NULL, '0', 0, 1, '2022-07-26 19:40:12', 1, '2022-07-26 19:40:12', '2022-07-26 19:40:11');
INSERT INTO `sys_dict_detail` VALUES (1551895211306405889, '1551894955382558721', 'log_type', '客户端', '1', '1', 2, NULL, '0', 0, 1, '2022-07-26 19:40:26', 1, '2022-07-26 19:40:26', '2022-07-26 19:40:25');
INSERT INTO `sys_dict_detail` VALUES (1551895274808168449, '1551894955382558721', 'log_type', '后台', '2', '1', 3, NULL, '0', 0, 1, '2022-07-26 19:40:41', 1, '2022-07-26 19:40:41', '2022-07-26 19:40:40');
INSERT INTO `sys_dict_detail` VALUES (1551895331154448386, '1551894955382558721', 'log_type', '程序自动', '3', '1', 4, NULL, '0', 0, 1, '2022-07-26 19:40:54', 1, '2022-07-26 19:40:54', '2022-07-26 19:40:54');
INSERT INTO `sys_dict_detail` VALUES (1551895490605109250, '1551895408610660353', 'log_level', 'DEBUG', '-1', '1', 1, NULL, '0', 1, 1, '2022-07-26 19:41:32', 1, '2022-07-27 11:18:18', '2022-07-27 11:18:17');
INSERT INTO `sys_dict_detail` VALUES (1551895541041614850, '1551895408610660353', 'log_level', 'INFO', '0', '1', 2, NULL, '0', 1, 1, '2022-07-26 19:41:44', 1, '2022-07-27 11:18:18', '2022-07-27 11:18:17');
INSERT INTO `sys_dict_detail` VALUES (1551895601477341185, '1551895408610660353', 'log_level', 'WARN', '1', '1', 3, NULL, '0', 1, 1, '2022-07-26 19:41:59', 1, '2022-07-27 11:18:18', '2022-07-27 11:18:17');
INSERT INTO `sys_dict_detail` VALUES (1551895666078011394, '1551895408610660353', 'log_level', 'ERROR', '2', '1', 4, NULL, '0', 1, 1, '2022-07-26 19:42:14', 1, '2022-07-27 11:18:18', '2022-07-27 11:18:18');
INSERT INTO `sys_dict_detail` VALUES (1551895739352502274, '1551895408610660353', 'log_level', 'TRACE', '3', '1', 5, NULL, '0', 1, 1, '2022-07-26 19:42:32', 1, '2022-07-27 11:18:18', '2022-07-27 11:18:18');
INSERT INTO `sys_dict_detail` VALUES (1551896512073322498, '1551896061743484929', 'log_operation_type', '未知', 'unknown', '1', 1, NULL, '0', 0, 1, '2022-07-26 19:45:36', 1, '2022-07-26 19:45:36', '2022-07-26 19:45:35');
INSERT INTO `sys_dict_detail` VALUES (1551896567580741633, '1551896061743484929', 'log_operation_type', '删除', 'delete', '1', 2, NULL, '0', 0, 1, '2022-07-26 19:45:49', 1, '2022-07-26 19:45:49', '2022-07-26 19:45:49');
INSERT INTO `sys_dict_detail` VALUES (1551896632240132097, '1551896061743484929', 'log_operation_type', '查询', 'select', '1', 3, NULL, '0', 0, 1, '2022-07-26 19:46:05', 1, '2022-07-26 19:46:05', '2022-07-26 19:46:04');
INSERT INTO `sys_dict_detail` VALUES (1551896695058223106, '1551896061743484929', 'log_operation_type', '更新', 'update', '1', 4, NULL, '0', 0, 1, '2022-07-26 19:46:20', 1, '2022-07-26 19:46:20', '2022-07-26 19:46:19');
INSERT INTO `sys_dict_detail` VALUES (1551896736737021953, '1551896061743484929', 'log_operation_type', '新增', 'insert', '1', 5, NULL, '0', 0, 1, '2022-07-26 19:46:30', 1, '2022-07-26 19:46:30', '2022-07-26 19:46:29');
INSERT INTO `sys_dict_detail` VALUES (1551898038988824578, '1551896925380038657', 'log_model_type', '公共模块', '00', '1', 1, NULL, '0', 5, 1, '2022-07-26 19:51:40', 1, '2022-07-27 10:21:20', '2022-07-27 10:21:20');
INSERT INTO `sys_dict_detail` VALUES (1551898084056621058, '1551896925380038657', 'log_model_type', '用户模块', '01', '1', 2, NULL, '0', 5, 1, '2022-07-26 19:51:51', 1, '2022-07-27 10:21:24', '2022-07-27 10:21:23');
INSERT INTO `sys_dict_detail` VALUES (1551898133155143682, '1551896925380038657', 'log_model_type', '角色模块', '02', '1', 3, NULL, '0', 5, 1, '2022-07-26 19:52:02', 1, '2022-07-27 10:21:26', '2022-07-27 10:21:26');
INSERT INTO `sys_dict_detail` VALUES (1551898192265469953, '1551896925380038657', 'log_model_type', '菜单模块', '03', '1', 4, NULL, '0', 5, 1, '2022-07-26 19:52:17', 1, '2022-07-27 10:21:28', '2022-07-27 10:21:28');
INSERT INTO `sys_dict_detail` VALUES (1551898257474314242, '1551896925380038657', 'log_model_type', '组织模块', '04', '1', 5, NULL, '0', 5, 1, '2022-07-26 19:52:32', 1, '2022-07-27 10:21:30', '2022-07-27 10:21:30');
INSERT INTO `sys_dict_detail` VALUES (1551898350382342146, '1551896925380038657', 'log_model_type', '字典模块', '05', '1', 6, NULL, '0', 5, 1, '2022-07-26 19:52:54', 1, '2022-07-27 10:21:32', '2022-07-27 10:21:32');
INSERT INTO `sys_dict_detail` VALUES (1551898407601037313, '1551896925380038657', 'log_model_type', '租户模块', '06', '1', 7, NULL, '0', 5, 1, '2022-07-26 19:53:08', 1, '2022-07-27 10:21:32', '2022-07-27 10:21:32');
INSERT INTO `sys_dict_detail` VALUES (1551898482796519426, '1551896925380038657', 'log_model_type', '地区模块', '07', '1', 8, NULL, '0', 5, 1, '2022-07-26 19:53:26', 1, '2022-07-27 10:21:32', '2022-07-27 10:21:32');
INSERT INTO `sys_dict_detail` VALUES (1551898551625048066, '1551896925380038657', 'log_model_type', '监控模块', '08', '1', 9, NULL, '0', 5, 1, '2022-07-26 19:53:42', 1, '2022-07-27 10:21:32', '2022-07-27 10:21:32');
INSERT INTO `sys_dict_detail` VALUES (1551898616208941057, '1551896925380038657', 'log_model_type', '代码生成器', '09', '1', 10, NULL, '0', 5, 1, '2022-07-26 19:53:58', 1, '2022-07-27 10:21:33', '2022-07-27 10:21:32');
INSERT INTO `sys_dict_detail` VALUES (1551898680184659970, '1551896925380038657', 'log_model_type', '测试模块', '100', '0', 11, NULL, '0', 5, 1, '2022-07-26 19:54:13', 1, '2022-07-27 10:21:33', '2022-07-27 10:21:32');
INSERT INTO `sys_dict_detail` VALUES (1551898752452517889, '1551896925380038657', 'log_model_type', '测试用户模块', '101', '0', 12, NULL, '0', 5, 1, '2022-07-26 19:54:30', 1, '2022-07-27 10:21:33', '2022-07-27 10:21:33');
INSERT INTO `sys_dict_detail` VALUES (1551898808907849730, '1551896925380038657', 'log_model_type', '测试汽车模块', '102', '0', 13, NULL, '0', 5, 1, '2022-07-26 19:54:44', 1, '2022-07-27 10:21:33', '2022-07-27 10:21:33');
INSERT INTO `sys_dict_detail` VALUES (1555949790771814402, '1551896925380038657', 'log_model_type', '未知I请配置模块', '-1', '0', 500, NULL, '0', 1, 1, '2022-08-07 00:11:53', 1, '2022-08-07 00:12:14', '2022-08-07 00:12:13');
COMMIT;

-- ----------------------------
-- Table structure for sys_login_logs
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_logs`;
CREATE TABLE `sys_login_logs` (
  `id` bigint(19) NOT NULL COMMENT '唯一主键',
  `org_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '父级主键集合',
  `type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '日志类型 1登录日志 2退出日志',
  `remote_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户代理',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账户',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
  `login_from` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登陆来源',
  `tenant_id` bigint(19) DEFAULT NULL COMMENT '多租户ID',
  `version` int(11) NOT NULL COMMENT '版本',
  `create_by` bigint(19) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(19) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_username` (`username`) USING BTREE,
  KEY `idx_loginfrom` (`login_from`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='登录信息表';

-- ----------------------------
-- Table structure for sys_logs
-- ----------------------------
DROP TABLE IF EXISTS `sys_logs`;
CREATE TABLE `sys_logs` (
  `id` bigint(19) NOT NULL COMMENT '唯一主键',
  `type` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '1' COMMENT '日志类型',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '日志标题',
  `remote_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '请求URI',
  `method` varchar(5) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '操作方式',
  `timeout` bigint(20) DEFAULT NULL COMMENT '执行时间',
  `params` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '操作提交的数据',
  `exception` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '异常信息',
  `version` int(11) NOT NULL COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(19) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  `org_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '父级主键集合',
  `tenant_id` bigint(19) DEFAULT NULL COMMENT '多租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_log_create_by` (`create_by`) USING BTREE,
  KEY `sys_log_request_uri` (`request_uri`) USING BTREE,
  KEY `sys_log_type` (`type`) USING BTREE,
  KEY `sys_log_create_date` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日志表';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(19) NOT NULL COMMENT '功能主键',
  `parent_id` bigint(19) NOT NULL DEFAULT '0' COMMENT '父级主键',
  `parent_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '父级主键集合',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `permissions` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '菜单标签 0-系统菜单 1-功能菜单',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型: 1-菜单 2-按钮 3-链接',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'url地址',
  `component` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件',
  `redirect` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '重定向',
  `sort_no` int(11) NOT NULL COMMENT '排序',
  `always_show` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否总是显示 0是  1否',
  `hidden` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否隐藏 0是  1否',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除状态',
  `version` int(11) NOT NULL COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `pid` (`parent_id`) USING BTREE COMMENT '上级id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统功能表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1, 0, '0,1', '系统配置', NULL, 'cog', '0', '1', '/system', 'Layout', NULL, 3, '0', '0', '0', 2, 1, '2020-04-14 19:07:31', 1, '2021-05-04 20:34:03', '2022-04-29 17:22:11');
INSERT INTO `sys_menu` VALUES (2, 1, '0,1,2', '菜单管理', NULL, NULL, '0', '1', 'menu', 'views/modules/system/menuManagement/index', NULL, 1, '0', '0', '0', 0, 1, '2020-04-14 19:07:31', 1, '2020-10-07 23:49:36', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (3, 1460639200696160257, '0,1460639200696160257,3', '用户管理', NULL, '<vab-icon :icon=\"[\'fas\', \'users\']\"></vab-icon>', '0,1', '1', 'user', 'views/modules/system/userManagement/index', NULL, 2, '0', '0', '0', 1, 1, '2020-04-14 19:07:31', 1, '2021-11-17 00:02:41', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (4, 1460639200696160257, '0,1460639200696160257,4', '角色管理', NULL, NULL, '0,1', '1', 'role', 'views/modules/system/roleManagement/index', NULL, 3, '0', '0', '0', 1, 1, '2020-09-24 14:01:31', 1, '2021-11-17 00:02:49', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1312756531833356289, 1, '0,1,1312756531833356289', '字典管理', NULL, NULL, '0', '1', 'dict', 'views/modules/system/dictManagement/index', NULL, 6, '0', '0', '0', 2, 1, '2020-10-04 09:08:42', 1, '2021-04-29 13:02:37', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1313789204920131585, 3, '0,1460639200696160257,3,1313789204920131585', '增加', 'system_user_insert', NULL, '0,1', '2', NULL, NULL, NULL, 2, '0', '0', '0', 0, 1, '2020-10-07 05:32:10', 1, '2020-10-07 10:42:13', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313789308506857474, 3, '0,1460639200696160257,3,1313789308506857474', '修改', 'system_user_update', NULL, '0,1', '2', NULL, NULL, NULL, 3, '0', '0', '0', 0, 1, '2020-10-07 05:32:35', 1, '2020-10-07 06:42:26', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313789400169177089, 3, '0,1460639200696160257,3,1313789400169177089', '删除', 'system_user_delete', NULL, '0,1', '2', NULL, NULL, NULL, 4, '0', '0', '0', 0, 1, '2020-10-07 05:32:57', 1, '2020-10-07 06:42:38', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313789529840279554, 3, '0,1460639200696160257,3,1313789529840279554', '导出', 'system_user_export', NULL, '0,1', '2', NULL, NULL, NULL, 5, '0', '0', '0', 0, 1, '2020-10-07 05:33:28', 1, '2020-10-07 06:42:43', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313806847370620930, 3, '0,1460639200696160257,3,1313806847370620930', '查看', 'system_user_select', NULL, '0,1', '2', NULL, NULL, NULL, 1, '0', '0', '0', 0, 1, '2020-10-07 06:42:16', 1, '2020-10-07 06:42:16', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313864645827678210, 3, '0,1460639200696160257,3,1313864645827678210', '授权角色', 'system_user_setRole', '', '0,1', '2', NULL, NULL, NULL, 6, '0', '0', '0', 1, 1, '2020-10-07 10:31:57', 1, '2021-11-30 15:44:40', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313864777918894082, 3, '0,1460639200696160257,3,1313864777918894082', '修改密码', 'system_user_updatePassword', NULL, '0,1', '2', NULL, NULL, NULL, 7, '0', '0', '0', 0, 1, '2020-10-07 10:32:28', 1, '2020-10-07 10:32:28', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313866576193212418, 2, '0,1,2,1313866576193212418', '增加', 'system_menu_insert', NULL, '0', '2', NULL, NULL, NULL, 2, '0', '0', '0', 0, 1, '2020-10-07 10:39:37', 1, '2020-10-07 10:42:03', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313866652533739522, 2, '0,1,2,1313866652533739522', '修改', 'system_menu_update', NULL, '0', '2', NULL, NULL, NULL, 3, '0', '0', '0', 0, 1, '2020-10-07 10:39:55', 1, '2020-10-07 10:41:01', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313866789838475265, 2, '0,1,2,1313866789838475265', '删除', 'system_menu_delete', NULL, '0', '2', NULL, NULL, NULL, 4, '0', '0', '0', 0, 1, '2020-10-07 10:40:28', 1, '2020-10-07 10:41:11', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313866828526735361, 2, '0,1,2,1313866828526735361', '查看', 'system_menu_select', NULL, '0', '2', NULL, NULL, NULL, 1, '0', '0', '0', 2, 1, '2020-10-07 10:40:37', 1, '2021-04-09 23:51:37', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313867061172195330, 4, '0,1460639200696160257,4,1313867061172195330', '查看', 'system_role_select', NULL, '0,1', '2', NULL, NULL, NULL, 1, '0', '0', '0', 0, 1, '2020-10-07 10:41:33', 1, '2020-10-07 10:41:33', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313867122731995137, 4, '0,1460639200696160257,4,1313867122731995137', '增加', 'system_role_insert', NULL, '0,1', '2', NULL, NULL, NULL, 2, '0', '0', '0', 0, 1, '2020-10-07 10:41:47', 1, '2020-10-07 10:42:25', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313867360502894594, 4, '0,1460639200696160257,4,1313867360502894594', '修改', 'system_role_update', NULL, '0,1', '2', NULL, NULL, NULL, 3, '0', '0', '0', 0, 1, '2020-10-07 10:42:44', 1, '2020-10-07 10:42:44', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313867409949544450, 4, '0,1460639200696160257,4,1313867409949544450', '删除', 'system_role_delete', NULL, '0,1', '2', NULL, NULL, NULL, 4, '0', '0', '0', 0, 1, '2020-10-07 10:42:56', 1, '2020-10-07 10:42:56', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313867556498526209, 1312756531833356289, '0,1,1312756531833356289,1313867556498526209', '查看', 'system_dict_select', NULL, '0', '2', NULL, NULL, NULL, 1, '0', '0', '0', 0, 1, '2020-10-07 10:43:31', 1, '2020-10-07 10:43:31', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313867617949274113, 1312756531833356289, '0,1,1312756531833356289,1313867617949274113', '增加', 'system_dict_insert', NULL, '0', '2', NULL, NULL, NULL, 2, '0', '0', '0', 0, 1, '2020-10-07 10:43:45', 1, '2020-10-07 10:43:51', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313867682814185474, 1312756531833356289, '0,1,1312756531833356289,1313867682814185474', '修改', 'system_dict_update', NULL, '0', '2', NULL, NULL, NULL, 3, '0', '0', '0', 0, 1, '2020-10-07 10:44:01', 1, '2020-10-07 10:44:01', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313867732508299265, 1312756531833356289, '0,1,1312756531833356289,1313867732508299265', '删除', 'system_dict_delete', NULL, '0', '2', NULL, NULL, NULL, 4, '0', '0', '0', 0, 1, '2020-10-07 10:44:13', 1, '2020-10-07 10:44:13', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1313885644824522754, 4, '0,1460639200696160257,4,1313885644824522754', '设置菜单权限', 'system_role_setMenuPerms', NULL, '0,1', '2', NULL, NULL, NULL, 5, '0', '0', '0', 2, 1, '2020-10-07 11:55:23', 1, '2021-10-14 14:31:37', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314066547072872450, 0, '0,1314066547072872450', '首页', NULL, 'home', '0,1', '1', '/', 'Layout', 'index', 1, '0', '0', '0', 13, 1, '2020-10-07 23:54:14', 1, '2021-11-25 13:46:11', '2022-04-29 17:22:11');
INSERT INTO `sys_menu` VALUES (1314066863436640258, 1314066547072872450, '0,1314066547072872450,1314066863436640258', '首页', NULL, 'home', '0,1', '1', 'index', 'views/index/index', NULL, 1, '0', '0', '0', 1, 1, '2020-10-07 23:55:29', 1, '2021-11-24 16:58:05', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1314068325453574145, 0, '0,1314068325453574145', '开发工具', NULL, 'tools', '0', '1', '/deve', 'Layout', NULL, 100, '0', '0', '0', 1, 1, '2020-10-08 00:01:18', 1, '2021-11-26 18:56:20', '2022-04-29 17:22:11');
INSERT INTO `sys_menu` VALUES (1314071137365307394, 1314068325453574145, '0,1314068325453574145,1314071137365307394', '组件', NULL, '', '0', '1', 'vab', 'EmptyLayout', NULL, 3, '0', '0', '0', 1, 1, '2020-10-08 00:12:28', 1, '2020-11-15 16:36:56', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1314074765178187777, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314074765178187777', '外链', NULL, NULL, '0', '3', 'https://github.com/hiparker/opsli-boot?utm_source=gold_browser_extension', NULL, NULL, 1, '0', '0', '0', 0, 1, '2020-10-08 00:26:53', 1, '2020-10-08 00:30:21', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314075128635600897, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314075128635600897', '图标', NULL, NULL, '0', '1', 'icon', 'EmptyLayout', NULL, 2, '0', '0', '0', 0, 1, '2020-10-08 00:28:20', 1, '2020-10-08 00:48:16', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314075267769053186, 1314075128635600897, '0,1314068325453574145,1314071137365307394,1314075128635600897,1314075267769053186', '常规图标', NULL, NULL, '0', '1', 'awesomeIcon', 'views/vab/icon/index', NULL, 1, '0', '0', '0', 0, 1, '2020-10-08 00:28:53', 1, '2020-10-08 00:48:19', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1314075542684708865, 1314075128635600897, '0,1314068325453574145,1314071137365307394,1314075128635600897,1314075542684708865', '小清新图标', NULL, NULL, '0', '1', 'remixIcon', 'views/vab/icon/remixIcon', NULL, 2, '0', '0', '0', 0, 1, '2020-10-08 00:29:58', 1, '2020-10-08 00:48:22', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1314075764852797442, 1314075128635600897, '0,1314068325453574145,1314071137365307394,1314075128635600897,1314075764852797442', '多彩图标', NULL, NULL, '0', '1', 'colorfulIcon', 'views/vab/icon/colorfulIcon', NULL, 3, '0', '0', '0', 0, 1, '2020-10-08 00:30:51', 1, '2020-10-08 00:48:25', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1314075970382082050, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314075970382082050', '表格', NULL, NULL, '0', '1', 'table', 'EmptyLayout', NULL, 3, '0', '0', '0', 0, 1, '2020-10-08 00:31:40', 1, '2020-10-08 00:48:31', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314076169481498625, 1314075970382082050, '0,1314068325453574145,1314071137365307394,1314075970382082050,1314076169481498625', '综合表格', NULL, NULL, '0', '1', 'comprehensiveTable', 'views/vab/table/index', NULL, 1, '0', '0', '0', 0, 1, '2020-10-08 00:32:28', 1, '2020-10-08 00:48:34', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1314076280542474242, 1314075970382082050, '0,1314068325453574145,1314071137365307394,1314075970382082050,1314076280542474242', '行内编辑', NULL, NULL, '0', '1', 'inlineEditTable', 'views/vab/table/inlineEditTable', NULL, 2, '0', '0', '0', 0, 1, '2020-10-08 00:32:54', 1, '2020-10-08 00:32:54', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1314076678317682689, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314076678317682689', '地图', NULL, NULL, '0', '1', 'map', 'views/vab/map/index', NULL, 4, '0', '0', '0', 0, 1, '2020-10-08 00:34:29', 1, '2020-10-08 00:34:29', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314077008057085954, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314077008057085954', 'WebSocket', NULL, NULL, '0', '1', 'websocket', 'views/vab/webSocket/index', NULL, 5, '0', '0', '0', 0, 1, '2020-10-08 00:35:48', 1, '2020-10-08 00:35:48', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314077108560998402, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314077108560998402', '表单', NULL, NULL, '0', '1', 'form', 'views/vab/form/index', NULL, 6, '0', '0', '0', 0, 1, '2020-10-08 00:36:12', 1, '2020-10-08 00:36:12', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314077229235318786, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314077229235318786', '常用组件', NULL, NULL, '0', '1', 'element', 'views/vab/element/index', NULL, 7, '0', '0', '0', 0, 1, '2020-10-08 00:36:40', 1, '2020-10-08 00:36:52', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314077399507283970, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314077399507283970', '树', NULL, NULL, '0', '1', 'tree', 'views/vab/tree/index', NULL, 8, '0', '0', '0', 0, 1, '2020-10-08 00:37:21', 1, '2020-10-08 00:37:21', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314077518340304897, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314077518340304897', '卡片', NULL, NULL, '0', '1', 'card', 'views/vab/card/index', NULL, 9, '0', '0', '0', 0, 1, '2020-10-08 00:37:49', 1, '2020-10-08 00:37:49', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314077631905280001, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314077631905280001', '滚动侦测', NULL, NULL, '0', '1', 'betterscroll', 'views/vab/betterScroll/index', NULL, 10, '0', '0', '0', 0, 1, '2020-10-08 00:38:16', 1, '2020-10-08 00:38:16', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314077729003417602, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314077729003417602', '验证码', NULL, NULL, '0', '1', 'verify', 'views/vab/verify/index', NULL, 11, '0', '0', '0', 0, 1, '2020-10-08 00:38:40', 1, '2020-10-08 00:38:40', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314120834868060162, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314120834868060162', '放大镜', NULL, NULL, '0', '1', 'magnifier', 'views/vab/magnifier/index', NULL, 12, '0', '0', '0', 0, 1, '2020-10-08 03:29:57', 1, '2020-10-08 03:30:45', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314121004749955073, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314121004749955073', '图表', NULL, NULL, '0', '1', 'echarts', 'views/vab/echarts/index', NULL, 13, '0', '0', '0', 0, 1, '2020-10-08 03:30:37', 1, '2020-10-08 03:30:54', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314121200103858178, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314121200103858178', 'Loading', NULL, NULL, '0', '1', 'loading', 'views/vab/loading/index', NULL, 14, '0', '0', '0', 0, 1, '2020-10-08 03:31:24', 1, '2020-10-08 03:31:24', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314121675192672257, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314121675192672257', '视频播放器', NULL, NULL, '0', '1', 'player', 'views/vab/player/index', NULL, 15, '0', '0', '0', 0, 1, '2020-10-08 03:33:17', 1, '2020-10-08 03:33:17', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314121808793837570, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314121808793837570', 'Markdown编辑器', NULL, NULL, '0', '1', 'markdownEditor', 'views/vab/markdownEditor/index', NULL, 16, '0', '0', '0', 0, 1, '2020-10-08 03:33:49', 1, '2020-10-08 03:33:49', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314121928784486402, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314121928784486402', '富文本编辑器', NULL, NULL, '0', '1', 'editor', 'views/vab/editor/index', NULL, 17, '0', '0', '0', 0, 1, '2020-10-08 03:34:18', 1, '2020-10-08 03:35:21', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314122020136427521, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314122020136427521', '二维码', NULL, NULL, '0', '1', 'qrCode', 'views/vab/qrCode/index', NULL, 18, '0', '0', '0', 0, 1, '2020-10-08 03:34:39', 1, '2020-10-08 03:35:26', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314122123047870466, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314122123047870466', '返回顶部', NULL, NULL, '0', '1', 'backToTop', 'views/vab/backToTop/index', NULL, 20, '0', '0', '0', 0, 1, '2020-10-08 03:35:04', 1, '2020-10-08 03:37:41', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314122353273217025, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314122353273217025', '图像拖拽比对', NULL, NULL, '0', '1', 'imgComparison', 'views/vab/imgComparison/index', NULL, 19, '0', '0', '0', 0, 1, '2020-10-08 03:35:59', 1, '2020-10-08 03:35:59', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314122457908518914, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314122457908518914', '代码生成机', NULL, NULL, '0', '1', 'codeGenerator', 'views/vab/codeGenerator/index', NULL, 21, '0', '0', '0', 1, 1, '2020-10-08 03:36:24', 1313694379541635074, '2020-10-11 17:21:34', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314122556776652802, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314122556776652802', 'markdown阅读器', NULL, NULL, '0', '1', 'markdown', 'views/vab/markdown/index', NULL, 22, '0', '0', '0', 0, 1, '2020-10-08 03:36:47', 1, '2020-10-08 03:37:58', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314122628184678401, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314122628184678401', '小组件', NULL, NULL, '0', '1', 'smallComponents', 'views/vab/smallComponents/index', NULL, 23, '0', '0', '0', 0, 1, '2020-10-08 03:37:04', 1, '2020-10-08 03:38:03', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314122717041008641, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314122717041008641', '上传', NULL, NULL, '0', '1', 'upload', 'views/vab/upload/index', NULL, 24, '0', '0', '0', 0, 1, '2020-10-08 03:37:26', 1, '2020-10-08 03:38:10', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314123071354839041, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314123071354839041', 'Sticky吸附', NULL, NULL, '0', '1', 'sticky', 'views/vab/sticky/index', NULL, 25, '0', '0', '0', 0, 1, '2020-10-08 03:38:50', 1, '2020-10-08 03:38:59', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314123272790482945, 1314071137365307394, '0,1314068325453574145,1314071137365307394,1314123272790482945', '错误日志模拟', NULL, NULL, '0', '1', 'errorLog', 'views/vab/errorLog/index', NULL, 26, '0', '0', '0', 0, 1, '2020-10-08 03:39:38', 1, '2020-10-08 03:39:38', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314123690283114498, 1314068325453574145, '0,1314068325453574145,1314123690283114498', '商城', NULL, NULL, '0', '1', 'mall', 'EmptyLayout', NULL, 4, '0', '0', '0', 1, 1, '2020-10-08 03:41:18', 1, '2020-11-15 16:37:13', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1314123894637993985, 1314123690283114498, '0,1314068325453574145,1314123690283114498,1314123894637993985', '支付', NULL, NULL, '0', '1', 'pay', 'views/mall/pay/index', NULL, 1, '0', '0', '0', 0, 1, '2020-10-08 03:42:06', 1, '2020-10-08 03:42:06', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314123990633029633, 1314123690283114498, '0,1314068325453574145,1314123690283114498,1314123990633029633', '商品列表', NULL, NULL, '0', '1', 'goodsList', 'views/mall/goodsList/index', NULL, 2, '0', '0', '0', 0, 1, '2020-10-08 03:42:29', 1, '2020-10-08 03:43:01', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314124102365093890, 1314123690283114498, '0,1314068325453574145,1314123690283114498,1314124102365093890', '商品详情', NULL, NULL, '0', '1', 'goodsDetail', 'views/mall/goodsDetail/index', NULL, 3, '0', '0', '0', 0, 1, '2020-10-08 03:42:56', 1, '2020-10-08 03:42:56', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1314610817013919745, 0, '0,1314610817013919745', '运维工具', NULL, 'laptop-code', '0', '1', '/devops', 'Layout', 'noRedirect', 99, '0', '0', '0', 1, 1, '2020-10-09 11:56:58', 1, '2020-11-13 11:05:54', '2022-04-29 17:22:11');
INSERT INTO `sys_menu` VALUES (1314616518671085570, 1504776412970254338, '0,1504776412970254338,1314616518671085570', '行为日志', NULL, NULL, '0,1', '1', 'logs', 'views/modules/system/operationLogsManagement/index', NULL, 2, '0', '0', '0', 3, 1, '2020-10-09 12:19:37', 1, '2022-07-26 19:33:51', '2022-07-26 19:33:51');
INSERT INTO `sys_menu` VALUES (1314782679522099201, 1314616518671085570, '0,1504776412970254338,1314616518671085570,1314782679522099201', '查看', 'system_op_logs_select', NULL, '0,1', '2', NULL, NULL, NULL, 1, '0', '0', '0', 2, 1, '2020-10-09 23:19:53', 1, '2022-07-26 20:09:12', '2022-07-26 20:09:11');
INSERT INTO `sys_menu` VALUES (1314786106243301378, 1314068325453574145, '0,1314068325453574145,1314786106243301378', '系统接口', NULL, NULL, '0', '3', 'http://${BASE_PATH}/doc.html', NULL, NULL, 2, '0', '0', '0', 1, 1, '2020-10-09 23:33:30', 1, '2020-11-15 16:37:23', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1314799744349913090, 1314610817013919745, '0,1314610817013919745,1314799744349913090', '数据库监控', NULL, NULL, '0', '3', 'http://${BASE_PATH}/druid', NULL, NULL, 3, '0', '0', '0', 1, 1, '2020-10-10 00:27:42', 1313694379541635074, '2021-01-18 11:45:37', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1314884045724717057, 1312756531833356289, '0,1,1312756531833356289,1314884045724717057', '设置字典', 'system_dict_setDict', NULL, '0', '2', NULL, NULL, NULL, 1, '0', '0', '0', 0, 1, '2020-10-10 19:02:41', 1, '2020-10-10 19:02:41', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1315201380721446914, 1460639200696160257, '0,1460639200696160257,1315201380721446914', '租户管理', NULL, NULL, '0', '1', 'tenant', 'views/modules/system/tenantManagement/index', NULL, 5, '0', '0', '0', 5, 1, '2020-10-11 16:03:39', 1, '2021-11-29 17:57:53', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1315201734892670977, 1315201380721446914, '0,1460639200696160257,1315201380721446914,1315201734892670977', '查看', 'system_tenant_select', NULL, '0', '2', NULL, NULL, NULL, 1, '0', '0', '0', 0, 1, '2020-10-11 16:05:04', 1, '2020-10-11 16:05:04', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1315201809668722690, 1315201380721446914, '0,1460639200696160257,1315201380721446914,1315201809668722690', '增加', 'system_tenant_insert', NULL, '0', '2', NULL, NULL, NULL, 2, '0', '0', '0', 0, 1, '2020-10-11 16:05:21', 1, '2020-10-11 16:05:21', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1315201864219840513, 1315201380721446914, '0,1460639200696160257,1315201380721446914,1315201864219840513', '修改', 'system_tenant_update', NULL, '0', '2', NULL, NULL, NULL, 3, '0', '0', '0', 0, 1, '2020-10-11 16:05:34', 1, '2020-10-11 16:05:34', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1315201925477650433, 1315201380721446914, '0,1460639200696160257,1315201380721446914,1315201925477650433', '删除', 'system_tenant_delete', NULL, '0', '2', NULL, NULL, NULL, 4, '0', '0', '0', 1, 1, '2020-10-11 16:05:49', 1, '2020-10-11 16:12:27', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1327085543511293954, 0, '0,1327085543511293954', '测试模块', NULL, 'box', '0,1', '1', '/gentest', 'Layout', NULL, 4, '0', '0', '0', 79, 1, '2020-11-13 11:07:04', 1, '2022-08-07 00:18:15', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1327085856930660353, 1327085543511293954, '0,1327085543511293954,1327085856930660353', '业务测试', NULL, '', '0,1', '1', 'test', 'views/modules/test/index', '', 1, '0', '0', '0', 12, 1, '2020-11-13 11:08:19', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1327086205548625921, 1327085856930660353, '0,1327085543511293954,1327085856930660353,1327086205548625921', '查看', 'gentest_test_select', '', '0,1', '2', NULL, NULL, NULL, 1, '0', '0', '0', 8, 1, '2020-11-13 11:09:42', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1327086298750255105, 1327085856930660353, '0,1327085543511293954,1327085856930660353,1327086298750255105', '增加', 'gentest_test_insert', '', '0,1', '2', NULL, NULL, NULL, 2, '0', '0', '0', 8, 1, '2020-11-13 11:10:04', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1327086378794352642, 1327085856930660353, '0,1327085543511293954,1327085856930660353,1327086378794352642', '修改', 'gentest_test_update', '', '0,1', '2', NULL, NULL, NULL, 3, '0', '0', '0', 6, 1, '2020-11-13 11:10:23', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1327086433609711617, 1327085856930660353, '0,1327085543511293954,1327085856930660353,1327086433609711617', '删除', 'gentest_test_delete', '', '0,1', '2', NULL, NULL, NULL, 4, '0', '0', '0', 7, 1, '2020-11-13 11:10:37', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1327893773049262082, 1397807288445526017, '0,1314068325453574145,1397807288445526017,1327893773049262082', '代码生成器', NULL, '', '0', '1', 'creater', 'views/modules/generator/table/index', NULL, 1, '0', '0', '0', 3, 1, '2020-11-15 16:38:41', 1, '2021-05-27 14:50:14', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1327894701135491073, 1327893773049262082, '0,1314068325453574145,1397807288445526017,1327893773049262082,1327894701135491073', '查看', 'dev_generator_select', '', '0', '2', NULL, NULL, NULL, 1, '0', '0', '0', 2, 1, '2020-11-15 16:42:22', 1, '2020-11-15 16:43:08', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1327894767283859457, 1327893773049262082, '0,1314068325453574145,1397807288445526017,1327893773049262082,1327894767283859457', '新增', 'dev_generator_insert', '', '0', '2', NULL, NULL, NULL, 2, '0', '0', '0', 0, 1, '2020-11-15 16:42:38', 1, '2020-11-15 16:42:38', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1327894837093855234, 1327893773049262082, '0,1314068325453574145,1397807288445526017,1327893773049262082,1327894837093855234', '修改', 'dev_generator_update', '', '0', '2', NULL, NULL, NULL, 3, '0', '0', '0', 1, 1, '2020-11-15 16:42:55', 1, '2020-11-15 16:43:14', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1327894965179510785, 1327893773049262082, '0,1314068325453574145,1397807288445526017,1327893773049262082,1327894965179510785', '删除', 'dev_generator_delete', '', '0', '2', NULL, NULL, NULL, 4, '0', '0', '0', 0, 1, '2020-11-15 16:43:25', 1, '2020-11-15 16:43:25', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1327895061598171137, 1327893773049262082, '0,1314068325453574145,1397807288445526017,1327893773049262082,1327895061598171137', '生成', 'dev_generator_create', '', '0', '2', NULL, NULL, NULL, 7, '0', '0', '0', 1, 1, '2020-11-15 16:43:48', 1, '2021-05-04 20:19:47', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1327903778221699074, 1327893773049262082, '0,1314068325453574145,1397807288445526017,1327893773049262082,1327903778221699074', '同步', 'dev_generator_sync', '', '0', '2', NULL, NULL, NULL, 5, '0', '0', '0', 2, 1, '2020-11-15 17:18:27', 1, '2021-05-04 20:19:32', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1329374800267452417, 1327893773049262082, '0,1314068325453574145,1397807288445526017,1327893773049262082,1329374800267452417', '导入数据表', 'dev_generator_import', '', '0', '2', NULL, NULL, NULL, 6, '0', '0', '0', 2, 1313694379541635074, '2020-11-19 18:43:46', 1, '2021-05-04 20:19:40', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1330365141900591105, 1327085543511293954, '0,1327085543511293954,1330365141900591105', '某系统用户', NULL, '', '0,1', '1', 'user', 'views/modules/gentest/user/index', NULL, 2, '0', '0', '0', 9, 1313694379541635074, '2020-11-22 12:19:01', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1330365525440331778, 1330365141900591105, '0,1327085543511293954,1330365141900591105,1330365525440331778', '查看', 'gentest_user_select', '', '0,1', '2', NULL, NULL, NULL, 1, '0', '0', '0', 6, 1313694379541635074, '2020-11-22 12:20:33', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1330365570587820033, 1330365141900591105, '0,1327085543511293954,1330365141900591105,1330365570587820033', '新增', 'gentest_user_insert', '', '0,1', '2', NULL, NULL, NULL, 2, '0', '0', '0', 7, 1313694379541635074, '2020-11-22 12:20:44', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1330365615181660162, 1330365141900591105, '0,1327085543511293954,1330365141900591105,1330365615181660162', '修改', 'gentest_user_update', '', '0,1', '2', NULL, NULL, NULL, 3, '0', '0', '0', 7, 1313694379541635074, '2020-11-22 12:20:54', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1330365717015166977, 1330365141900591105, '0,1327085543511293954,1330365141900591105,1330365717015166977', '删除', 'gentest_user_delete', '', '0,1', '2', NULL, NULL, NULL, 4, '0', '0', '0', 6, 1313694379541635074, '2020-11-22 12:21:19', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1332662450423635969, 1460639200696160257, '0,1460639200696160257,1332662450423635969', '组织管理', NULL, '', '0,1', '1', 'org', 'views/modules/system/orgManagement/index', NULL, 4, '0', '0', '0', 4, 1, '2020-11-28 20:27:43', 1, '2021-11-17 00:02:31', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1332662689314414594, 1332662450423635969, '0,1460639200696160257,1332662450423635969,1332662689314414594', '查看', 'system_org_select', '', '0,1', '2', NULL, NULL, NULL, 1, '0', '0', '0', 0, 1, '2020-11-28 20:28:39', 1, '2020-11-28 20:28:39', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1332662758860169217, 1332662450423635969, '0,1460639200696160257,1332662450423635969,1332662758860169217', '增加', 'system_org_insert', '', '0,1', '2', NULL, NULL, NULL, 2, '0', '0', '0', 0, 1, '2020-11-28 20:28:56', 1, '2020-11-28 20:28:56', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1332662809711910913, 1332662450423635969, '0,1460639200696160257,1332662450423635969,1332662809711910913', '修改', 'system_org_update', '', '0,1', '2', NULL, NULL, NULL, 3, '0', '0', '0', 0, 1, '2020-11-28 20:29:08', 1, '2020-11-28 20:29:08', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1332662858294534146, 1332662450423635969, '0,1460639200696160257,1332662450423635969,1332662858294534146', '删除', 'system_org_delete', '', '0,1', '2', NULL, NULL, NULL, 4, '0', '0', '0', 0, 1, '2020-11-28 20:29:20', 1, '2020-11-28 20:29:20', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1335439751687208961, 1, '0,1,1335439751687208961', '地域管理', NULL, '', '0', '1', 'area', 'views/modules/system/areaManagement/index', NULL, 8, '0', '0', '0', 3, 1313694379541635074, '2020-12-06 12:23:43', 1, '2021-04-29 13:02:44', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1335439904372457474, 1335439751687208961, '0,1,1335439751687208961,1335439904372457474', '新增', 'system_area_insert', '', '0', '2', NULL, NULL, NULL, 2, '0', '0', '0', 1, 1313694379541635074, '2020-12-06 12:24:19', 1313694379541635074, '2020-12-06 12:24:50', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1335440004809261058, 1335439751687208961, '0,1,1335439751687208961,1335440004809261058', '查看', 'system_area_select', '', '0', '2', NULL, NULL, NULL, 1, '0', '0', '0', 0, 1313694379541635074, '2020-12-06 12:24:43', 1313694379541635074, '2020-12-06 12:24:43', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1335440081128816642, 1335439751687208961, '0,1,1335439751687208961,1335440081128816642', '修改', 'system_area_update', '', '0', '2', NULL, NULL, NULL, 3, '0', '0', '0', 1, 1313694379541635074, '2020-12-06 12:25:01', 1313694379541635074, '2020-12-06 12:25:07', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1335440153140822017, 1335439751687208961, '0,1,1335439751687208961,1335440153140822017', '删除', 'system_area_delete', '', '0', '2', NULL, NULL, NULL, 4, '0', '0', '0', 0, 1313694379541635074, '2020-12-06 12:25:18', 1313694379541635074, '2020-12-06 12:25:18', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1337719928086458369, 1330365141900591105, '0,1327085543511293954,1330365141900591105,1337719928086458369', '导出', 'gentest_user_export', '', '0,1', '2', NULL, NULL, NULL, 5, '0', '0', '0', 6, 1313694379541635074, '2020-12-12 19:24:19', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1337720128930705409, 1330365141900591105, '0,1327085543511293954,1330365141900591105,1337720128930705409', '导入', 'gentest_user_import', '', '0,1', '2', NULL, NULL, NULL, 6, '0', '0', '0', 6, 1313694379541635074, '2020-12-12 19:25:07', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1337796232345407489, 1327085856930660353, '0,1327085543511293954,1327085856930660353,1337796232345407489', '导出', 'gentest_test_export', '', '0,1', '2', NULL, NULL, NULL, 5, '0', '0', '0', 6, 1313694379541635074, '2020-12-13 00:27:31', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1337796311940714498, 1327085856930660353, '0,1327085543511293954,1327085856930660353,1337796311940714498', '导入', 'gentest_test_import', '', '0,1', '2', NULL, NULL, NULL, 6, '0', '0', '0', 6, 1313694379541635074, '2020-12-13 00:27:50', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1340626549594677250, 1327085543511293954, '0,1327085543511293954,1340626549594677250', '汽车信息', NULL, '', '0,1', '1', 'carinfo', 'views/modules/gentest/carinfo/index', NULL, 3, '0', '0', '0', 9, 1313694379541635074, '2020-12-20 19:54:12', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1340626612895113217, 1340626549594677250, '0,1327085543511293954,1340626549594677250,1340626612895113217', '查看', 'gentest_carinfo_select', '', '0,1', '2', NULL, NULL, NULL, 1, '0', '0', '0', 6, 1313694379541635074, '2020-12-20 19:54:27', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1340626666078887937, 1340626549594677250, '0,1327085543511293954,1340626549594677250,1340626666078887937', '新增', 'gentest_carinfo_insert', '', '0,1', '2', NULL, NULL, NULL, 2, '0', '0', '0', 6, 1313694379541635074, '2020-12-20 19:54:39', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1340626895356321793, 1340626549594677250, '0,1327085543511293954,1340626549594677250,1340626895356321793', '修改', 'gentest_carinfo_update', '', '0,1', '2', NULL, NULL, NULL, 3, '0', '0', '0', 6, 1313694379541635074, '2020-12-20 19:55:34', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1340626939119689729, 1340626549594677250, '0,1327085543511293954,1340626549594677250,1340626939119689729', '删除', 'gentest_carinfo_delete', '', '0,1', '2', NULL, NULL, NULL, 4, '0', '0', '0', 6, 1313694379541635074, '2020-12-20 19:55:45', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1340626988251766786, 1340626549594677250, '0,1327085543511293954,1340626549594677250,1340626988251766786', '导入', 'gentest_carinfo_import', '', '0,1', '2', NULL, NULL, NULL, 5, '0', '0', '0', 6, 1313694379541635074, '2020-12-20 19:55:56', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1340627032942075906, 1340626549594677250, '0,1327085543511293954,1340626549594677250,1340627032942075906', '导出', 'gentest_carinfo_export', '', '0,1', '2', NULL, NULL, NULL, 6, '0', '0', '0', 7, 1313694379541635074, '2020-12-20 19:56:07', 1, '2022-04-29 16:36:49', '2022-08-07 00:18:15');
INSERT INTO `sys_menu` VALUES (1351012936860155906, 1314610817013919745, '0,1314610817013919745,1351012936860155906', '系统监控', NULL, '', '0', '1', 'sysmonitor', 'views/modules/system/monitorManagement/index', NULL, 1, '0', '0', '0', 1, 1313694379541635074, '2021-01-18 11:45:59', 1313694379541635074, '2021-01-18 11:47:15', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1351013587816136705, 1351012936860155906, '0,1314610817013919745,1351012936860155906,1351013587816136705', '查看', 'devops_sysmonitor_select', '', '0', '2', NULL, NULL, NULL, 1, '0', '0', '0', 0, 1, '2021-01-18 11:48:35', 1, '2021-01-18 11:48:35', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1360233188433977345, 1, '0,1,1360233188433977345', '系统设置', NULL, '', '0', '1', 'set', 'views/modules/system/setManagement/index', NULL, 99, '0', '0', '0', 4, 1313694379541635074, '2021-02-12 22:23:59', 1, '2021-04-29 13:01:24', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1360233383397810177, 1360233188433977345, '0,1,1360233188433977345,1360233383397810177', '更新', 'system_options_update', '', '0', '2', NULL, NULL, NULL, 1, '0', '0', '0', 2, 1313694379541635074, '2021-02-12 22:24:45', 1313694379541635074, '2021-02-14 01:37:07', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1370051609388388353, 3, '0,1460639200696160257,3,1370051609388388353', '重置密码', 'system_user_resetPassword', '', '0,1', '2', NULL, NULL, NULL, 10, '0', '0', '0', 1, 1, '2021-03-12 00:38:53', 1, '2021-03-12 00:39:04', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1370404146704654337, 3, '0,1460639200696160257,3,1370404146704654337', '变更账户状态', 'system_user_enable', '', '0,1', '2', NULL, NULL, NULL, 11, '0', '0', '0', 2, 1, '2021-03-12 23:59:44', 1, '2021-04-08 23:59:33', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1380173787882696705, 1315201380721446914, '0,1460639200696160257,1315201380721446914,1380173787882696705', '变更租户状态', 'system_tenant_enable', '', '0', '2', NULL, NULL, NULL, 5, '0', '0', '0', 0, 1, '2021-04-08 23:00:48', 1, '2021-04-08 23:00:48', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1387633500164599809, 1, '0,1,1387633500164599809', '参数配置', NULL, '', '0', '1', 'options', 'views/modules/system/optionsManagement/index', NULL, 7, '0', '0', '0', 2, 1, '2021-04-29 13:03:02', 1, '2021-04-29 13:03:39', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1387633799226863618, 1387633500164599809, '0,1,1387633500164599809,1387633799226863618', '查看', 'system_options_select', NULL, '0', '2', NULL, NULL, NULL, 1, '0', '0', '0', 2, 1, '2021-04-29 13:04:13', 1, '2021-04-29 13:07:10', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1387633960401383426, 1387633500164599809, '0,1,1387633500164599809,1387633960401383426', '新增', 'system_options_insert', NULL, '0', '2', NULL, NULL, NULL, 2, '0', '0', '0', 1, 1, '2021-04-29 13:04:52', 1, '2021-04-29 13:07:15', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1387634089447534594, 1387633500164599809, '0,1,1387633500164599809,1387634089447534594', '修改', 'system_options_update', NULL, '0', '2', NULL, NULL, NULL, 3, '0', '0', '0', 1, 1, '2021-04-29 13:05:23', 1, '2021-04-29 13:07:19', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1387634157474951169, 1387633500164599809, '0,1,1387633500164599809,1387634157474951169', '删除', 'system_options_delete', NULL, '0', '2', NULL, NULL, NULL, 4, '0', '0', '0', 1, 1, '2021-04-29 13:05:39', 1, '2021-04-29 13:07:23', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1389555212654223361, 1327893773049262082, '0,1314068325453574145,1397807288445526017,1327893773049262082,1389555212654223361', '生成菜单', 'dev_generator_createMenu', NULL, '0', '2', NULL, NULL, NULL, 8, '0', '0', '0', 1, 1, '2021-05-04 20:19:14', 1, '2021-05-04 20:19:52', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1397807288445526017, 1314068325453574145, '0,1314068325453574145,1397807288445526017', '开发向导', NULL, '', '0', '1', 'generator', 'EmptyLayout', NULL, 1, '1', '0', '0', 2, 1, '2021-05-27 14:50:02', 1, '2021-05-27 15:02:49', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1397807399338729473, 1397807288445526017, '0,1314068325453574145,1397807288445526017,1397807399338729473', '代码模板', NULL, NULL, '0', '1', 'template', 'views/modules/generator/template/index', NULL, 2, '0', '0', '0', 2, 1, '2021-05-27 14:50:29', 1, '2021-05-27 14:50:54', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1397807399363895298, 1397807399338729473, '0,1314068325453574145,1397807288445526017,1397807399338729473,1397807399363895298', '查看', 'generator_template_select', NULL, '0', '2', NULL, NULL, NULL, 1, '0', '0', '0', 0, 1, '2021-05-27 14:50:29', 1, '2021-05-27 14:50:29', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1397807399389061121, 1397807399338729473, '0,1314068325453574145,1397807288445526017,1397807399338729473,1397807399389061121', '新增', 'generator_template_insert', NULL, '0', '2', NULL, NULL, NULL, 2, '0', '0', '0', 0, 1, '2021-05-27 14:50:29', 1, '2021-05-27 14:50:29', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1397807399414226945, 1397807399338729473, '0,1314068325453574145,1397807288445526017,1397807399338729473,1397807399414226945', '修改', 'generator_template_update', NULL, '0', '2', NULL, NULL, NULL, 3, '0', '0', '0', 0, 1, '2021-05-27 14:50:29', 1, '2021-05-27 14:50:29', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1397807399439392770, 1397807399338729473, '0,1314068325453574145,1397807288445526017,1397807399338729473,1397807399439392770', '删除', 'generator_template_delete', NULL, '0', '2', NULL, NULL, NULL, 4, '0', '0', '0', 0, 1, '2021-05-27 14:50:29', 1, '2021-05-27 14:50:29', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1397807399456169985, 1397807399338729473, '0,1314068325453574145,1397807288445526017,1397807399338729473,1397807399456169985', '导入', 'generator_template_import', NULL, '0', '2', NULL, NULL, NULL, 5, '0', '0', '0', 0, 1, '2021-05-27 14:50:29', 1, '2021-05-27 14:50:29', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1397807399481335810, 1397807399338729473, '0,1314068325453574145,1397807288445526017,1397807399338729473,1397807399481335810', '导出', 'generator_template_export', NULL, '0', '2', NULL, NULL, NULL, 6, '0', '0', '0', 0, 1, '2021-05-27 14:50:29', 1, '2021-05-27 14:50:29', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1399667141430452225, 1397807399338729473, '0,1314068325453574145,1397807288445526017,1397807399338729473,1399667141430452225', '复制', 'generator_template_copy', NULL, '0', '2', NULL, NULL, NULL, 7, '0', '0', '0', 0, 1, '2021-06-01 18:00:26', 1, '2021-06-01 18:00:26', '2022-04-29 17:30:23');
INSERT INTO `sys_menu` VALUES (1448537070279237634, 4, '0,1460639200696160257,4,1448537070279237634', '设置数据权限', 'system_role_setDataPerms', NULL, '0,1', '2', NULL, NULL, NULL, 6, '0', '0', '0', 0, 1, '2021-10-14 14:32:05', 1, '2021-10-14 14:32:05', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1460629524738764802, 1315201380721446914, '0,1460639200696160257,1315201380721446914,1460629524738764802', '设置管理用户', 'system_set_tenant_admin', NULL, '0', '2', NULL, NULL, NULL, 6, '0', '0', '0', 0, 1, '2021-11-16 23:23:11', 1, '2021-11-16 23:23:11', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1460639200696160257, 0, '0,1460639200696160257', '组织机构', NULL, 'users', '0,1', '1', '/org', 'Layout', NULL, 2, '1', '0', '0', 5, 1, '2021-11-17 00:01:37', 1, '2021-11-30 17:10:45', '2022-04-29 17:22:11');
INSERT INTO `sys_menu` VALUES (1465587677695479810, 3, '0,1460639200696160257,3,1465587677695479810', '授权组织', 'system_user_setOrg', NULL, '0,1', '2', NULL, NULL, NULL, 8, '0', '0', '0', 1, 1, '2021-11-30 15:45:06', 1, '2021-11-30 15:45:29', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1465621050623209474, 1315201380721446914, '0,1460639200696160257,1315201380721446914,1465621050623209474', '增加管理用户', 'system_user_insert', NULL, '0', '2', NULL, NULL, NULL, 7, '0', '0', '0', 0, 1, '2021-11-30 17:57:43', 1, '2021-11-30 17:57:43', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1465621206781341698, 1315201380721446914, '0,1460639200696160257,1315201380721446914,1465621206781341698', '修改管理用户', 'system_user_update', NULL, '0', '2', NULL, NULL, NULL, 8, '0', '0', '0', 0, 1, '2021-11-30 17:58:20', 1, '2021-11-30 17:58:20', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1465621319830417409, 1315201380721446914, '0,1460639200696160257,1315201380721446914,1465621319830417409', '删除管理用户', 'system_user_delete', NULL, '0', '2', NULL, NULL, NULL, 9, '0', '0', '0', 0, 1, '2021-11-30 17:58:47', 1, '2021-11-30 17:58:47', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1465621468124229634, 1315201380721446914, '0,1460639200696160257,1315201380721446914,1465621468124229634', '修改管理用户密码', 'system_user_updatePassword', NULL, '0', '2', NULL, NULL, NULL, 10, '0', '0', '0', 0, 1, '2021-11-30 17:59:23', 1, '2021-11-30 17:59:23', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1465621601087860738, 1315201380721446914, '0,1460639200696160257,1315201380721446914,1465621601087860738', '重置管理用户密码', 'system_user_resetPassword', NULL, '0', '2', NULL, NULL, NULL, 11, '0', '0', '0', 0, 1, '2021-11-30 17:59:54', 1, '2021-11-30 17:59:54', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1465621733564952578, 1315201380721446914, '0,1460639200696160257,1315201380721446914,1465621733564952578', '变更管理用户状态', 'system_user_enable', NULL, '0', '2', NULL, NULL, NULL, 12, '0', '0', '0', 0, 1, '2021-11-30 18:00:26', 1, '2021-11-30 18:00:26', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1465621843787067394, 1315201380721446914, '0,1460639200696160257,1315201380721446914,1465621843787067394', '授权管理用户角色', 'system_user_setRole', NULL, '0', '2', NULL, NULL, NULL, 13, '0', '0', '0', 0, 1, '2021-11-30 18:00:52', 1, '2021-11-30 18:00:52', '2022-04-29 17:29:44');
INSERT INTO `sys_menu` VALUES (1504776412970254338, 0, '0,1504776412970254338', '日志监控', NULL, 'file-contract', '0,1', '1', '/log', 'Layout', NULL, 98, '0', '0', '0', 1, 1, '2022-03-18 19:07:09', 1, '2022-03-18 19:07:22', '2022-04-29 17:22:11');
INSERT INTO `sys_menu` VALUES (1504779965155655682, 1504776412970254338, '0,1504776412970254338,1504779965155655682', '登录日志', NULL, '', '0,1', '1', 'login-logs', 'views/modules/system/loginLogsManagement/index', NULL, 1, '0', '0', '0', 1, 1, '2022-03-18 19:21:16', 1, '2022-03-18 19:21:40', '2022-04-29 17:28:43');
INSERT INTO `sys_menu` VALUES (1504780214448308226, 1504779965155655682, '0,1504776412970254338,1504779965155655682,1504780214448308226', '查看', 'devops_login_logs_select', NULL, '0,1', '2', NULL, NULL, NULL, 1, '0', '0', '0', 0, 1, '2022-03-18 19:22:15', 1, '2022-03-18 19:22:15', '2022-04-29 17:29:44');
COMMIT;

-- ----------------------------
-- Table structure for sys_options
-- ----------------------------
DROP TABLE IF EXISTS `sys_options`;
CREATE TABLE `sys_options` (
  `id` bigint(19) NOT NULL COMMENT '唯一主键',
  `option_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数编号',
  `option_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数名称',
  `option_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '参数值',
  `iz_lock` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否内置 0否  1是',
  `iz_exclude` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否忽略',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index` (`option_code`) USING BTREE COMMENT '参数编号唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统参数表';

-- ----------------------------
-- Records of sys_options
-- ----------------------------
BEGIN;
INSERT INTO `sys_options` VALUES (1, 'crypto_asymmetric', '非对称加解密算法', '0yjcKZp44TutirOVOkzk3A==', '1', '0', NULL, 1, 1, '2021-02-10 23:19:35', 1, '2022-08-07 12:54:02', '2022-08-07 12:54:01');
INSERT INTO `sys_options` VALUES (2, 'crypto_asymmetric_public_key', '非对称加解密-公钥', 'iOsZOBkazTbd2Fr+IQwuo6ppJXx9aLqya9YtR+ruAMRqzQOjWORVrMUJPxF2TTo8TsBSyZ3hktN+4ubkH0/gsdXf+PlvGsa5JrLKJH+RbJoA3cQBaN3TXMXhfzYmwlMM2+js9qNdavqAmWWZWOy/hc1nXG4RwADOFHuxN/RHbMnYN80KhmdhMSn2bexu6k/A6esuEqDP6GYdOucl1FSW3/j1cxoZX3yr5p4Dl99CNtdpa8YUDY63D3s25W4pDrsLFbR/sYrW9Yn22gKv34bpcqwHgfj2EoZQvxqP8Jubw90=', '1', '0', NULL, 1, 1, '2021-02-10 23:19:35', 1, '2022-08-07 12:54:02', '2022-08-07 12:54:01');
INSERT INTO `sys_options` VALUES (3, 'crypto_asymmetric_private_key', '非对称加解密-私钥', 'eIRdV/SBEzQrjJOLwmgl0r7GVh4NTcGRR64Q9ML6KLYmd4gtAT+WMI75aZCOA8NcB8EPbJYSfVLYy0tP4PtxNdtNmazGBTrmpzv3L/F8dmSuLwxPXyegbjXk6AejafKfhfq7i04ljiJU2j66RXZTyGFs/H4CDZ3SYd3idEt6cDJHGjT7koDJBLn5HxrS+5pE/TLqUCfpY1ja4bGFMyY55lttYDLi2f42poTkSJ9iPYvRU0RAIehJSH2HjIAMzHM5UcNWcM+6Gjglp7bpXtHbryGURg+n7qKF0Q/rlWZwGWbtEqdUE6Fj5FlKPrGFKnmmZ5Ck0RBwojtbTBAEX9W8qGiRO3y6jGd0MdulZPpf7+PY3o0FH53vsZJ/DQP0jcml0eh/P/0x803ppebrPI6MU4mwnFUCCeiHUsozsDelHucx6/UuGQCGxTFqiRUyeJZsY/MmhxpgqbGRWr/y5WyEMSMeFdsfU1V9ixZ2eXnYX+g0iKZb0rLyUka9vo5IazdV8VqERZGXVnaRg6HPD1kfL2C6E3ANN2j9PRgJPGuS3ukqsOZKDy/8AsihWSblzIRO4jOfXnFYiMpkhsVgq2txg6+v50U4mzxHCCLkZtvzhg2FSGbpQOViiX78vBRYPhGlQP7ft7w2oSu8sIe0uMwQBdrE4OWAUNfJC3sxqK6P8dFqLgLTEFqmt8iHkM2S4e3He73AY0kKCs4YgRVbPxqQW+FsZSnWgknwLNvTEB4e7FvnoLa8gjcDTB/mC7thGbZO2nGHmlEeGxegYHOpsEG6qOxX0sBMZ40MmDcSm0Aqwe0sADKoN9V7Hzz0COmNjH55URAVap6yXj9G1bnmf9Evn0XeBpKmQ32ocmrrzLoCNBgWxUNo9WaeYP03mE88gLJYCBXPUyRVL98wrz/NHsUfAwFN067CQyTFayegpFzHZVaGBRdf8iVCYCs/AkTlBbxIVUeSblmDZ8/sJq8Cgsoq0UbxwjpXOaRMHUXlz2aMGSVZwWicRuvsjSRzxGz5BDAHIHqkC21JYht8GhP2VXvQZ/XcEjHV3BRVWOHC8G57MhycFQRk5I+FMx591Wg9v3TbiBd/AMREGLUHFqgdvxKiE6uZSfzCF69DDr2DWatX3RgsZu3BZ02YRYC5tVH8qJEA', '1', '0', NULL, 1, 1, '2021-02-10 23:19:35', 1, '2022-08-07 12:54:02', '2022-08-07 12:54:01');
INSERT INTO `sys_options` VALUES (4, 'def_pass', '系统默认密码', '47XKQ7v2BI/mOY3h9j79Ew==', '1', '0', NULL, 3, 1, '2021-02-10 23:19:35', 1, '2022-08-07 12:53:21', '2022-08-07 12:53:21');
INSERT INTO `sys_options` VALUES (5, 'def_role', '系统默认角色编号', 'c21tuf3eJ7eE2r81xj4aaw==', '1', '0', NULL, 3, 1, '2021-02-10 23:19:35', 1, '2022-08-07 12:53:21', '2022-08-07 12:53:21');
INSERT INTO `sys_options` VALUES (6, 'email_account', '邮箱账号', NULL, '1', '0', NULL, 41, 1, '2021-04-29 16:38:55', 1, '2022-08-07 12:54:04', '2022-08-08 10:10:58');
INSERT INTO `sys_options` VALUES (7, 'email_password', '邮箱密码', NULL, '1', '1', NULL, 41, 1, '2021-04-29 16:38:55', 1, '2022-08-07 12:54:04', '2022-08-08 10:10:58');
INSERT INTO `sys_options` VALUES (8, 'email_addresser', '发件人', 'XmjpKcluqKOKFisw5Hl6aHCaLn/7fdpcqBDmdd57rf/XIHkGnK9WmntC6u8iWfH7LGbtwWdNmEWAubVR/KiRAA==', '1', '0', NULL, 41, 1, '2021-04-29 16:38:55', 1, '2022-08-07 12:54:04', '2022-08-07 20:31:12');
INSERT INTO `sys_options` VALUES (9, 'email_smtp', 'SMTP地址', 'pZdMUIPJzJPdAbAqQoWSZg==', '1', '0', NULL, 32, 1, '2021-04-29 16:55:59', 1, '2022-08-07 12:54:04', '2022-08-07 20:31:13');
INSERT INTO `sys_options` VALUES (10, 'email_port', 'SMTP端口', 'iqxeoj6oqSmCiJqf6zNb7A==', '1', '0', NULL, 24, 1, '2021-04-29 17:08:28', 1, '2022-08-07 12:54:04', '2022-08-07 20:31:15');
INSERT INTO `sys_options` VALUES (11, 'email_ssl_enable', '开启SSL认证', 'uCeCEIDGlVfiABZ8OrEFJg==', '1', '0', NULL, 20, 1313694379541635074, '2021-04-29 17:28:52', 1, '2022-08-07 12:54:04', '2022-08-07 20:31:17');
INSERT INTO `sys_options` VALUES (12, 'storage_local_domain', '本地存储域名', '21n5x1+eHLId4XnbQdW4PltTlIYieOntdoH3SdYWN7csZu3BZ02YRYC5tVH8qJEA', '1', '0', NULL, 1, 1313694379541635074, '2021-04-30 21:54:43', 1, '2022-08-07 12:54:06', '2022-08-07 20:31:18');
INSERT INTO `sys_options` VALUES (13, 'storage_local_path_prefix', '本地存储路径前缀', 'LGbtwWdNmEWAubVR/KiRAA==', '1', '0', NULL, 1, 1313694379541635074, '2021-04-30 21:54:43', 1, '2022-08-07 12:54:06', '2022-08-07 20:31:20');
INSERT INTO `sys_options` VALUES (14, 'storage_type', '存储类型', 'zKDmOi1fneStyHdrCUsDBg==', '1', '0', NULL, 3, 1313694379541635074, '2021-05-01 11:02:38', 1, '2022-08-07 12:53:21', '2022-08-07 20:31:21');
INSERT INTO `sys_options` VALUES (15, 'storage_upyun_domain', '又拍云存储域名', '1t5v8R17Whg2LdV/97+34UUP/k6hqUGR1+jyauJA62o=', '1', '0', NULL, 4, 1, '2021-06-03 18:41:31', 1, '2022-08-07 12:54:08', '2022-08-07 20:31:23');
INSERT INTO `sys_options` VALUES (16, 'storage_upyun_path_prefix', '又拍云存储路径前缀', 'LGbtwWdNmEWAubVR/KiRAA==', '1', '0', NULL, 4, 1, '2021-06-03 18:41:31', 1, '2022-08-07 12:54:08', '2022-08-07 20:31:24');
INSERT INTO `sys_options` VALUES (17, 'storage_upyun_bucket_name', '又拍云存储桶名称', '8WojmNgerjWeE8LsopgEfA==', '1', '0', NULL, 4, 1, '2021-06-03 18:41:31', 1, '2022-08-07 12:54:08', '2022-08-07 20:31:26');
INSERT INTO `sys_options` VALUES (18, 'storage_upyun_username', '又拍云存储用户名', 'uDBoSc5NeWtHLmXhPmWqLw==', '1', '0', NULL, 4, 1, '2021-06-03 18:41:31', 1, '2022-08-07 12:54:08', '2022-08-07 20:31:27');
INSERT INTO `sys_options` VALUES (19, 'storage_upyun_password', '又拍云存储密码', NULL, '1', '1', NULL, 4, 1, '2021-06-03 18:41:31', 1, '2022-08-07 12:54:08', '2022-08-08 10:10:58');
INSERT INTO `sys_options` VALUES (50, 'sms_aliyun_access_key', '短信-阿里云凭证', 'eYMZVyqB6OPppUc20USOLiX3YIFm5PQcLTwGgJWw0ec=', '1', '0', NULL, 9, 1, '2021-06-03 18:41:31', 1, '2022-08-08 00:10:58', '2022-08-08 00:10:57');
INSERT INTO `sys_options` VALUES (51, 'sms_aliyun_access_key_secret', '短信-阿里云密钥', NULL, '1', '1', NULL, 9, 1, '2021-06-03 18:41:31', 1, '2022-08-08 00:10:58', '2022-08-08 10:10:58');
INSERT INTO `sys_options` VALUES (52, 'sms_aliyun_captcha_template_code', '短信-阿里云-验证码模版编号', 'YwE88I//MWVwKk2xHlF7oQ==', '1', '0', NULL, 9, 1, '2021-06-03 18:41:31', 1, '2022-08-08 00:10:58', '2022-08-08 00:10:57');
INSERT INTO `sys_options` VALUES (53, 'sms_aliyun_captcha_sign', '短信-阿里云-验证码签名', 'lIBj5h8sXsmqMcGBTur1YvUYeprjul8G3LUuyz/mhyg=', '1', '0', NULL, 9, 1, '2021-06-03 18:41:31', 1, '2022-08-08 00:10:58', '2022-08-08 00:10:57');
COMMIT;

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org` (
  `id` bigint(19) NOT NULL COMMENT '字典主键',
  `parent_id` bigint(19) NOT NULL DEFAULT '0' COMMENT '父级主键',
  `parent_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '父级主键集合',
  `org_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '组织机构组',
  `org_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组织机构编号',
  `org_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组织机构名称',
  `sort_no` int(11) NOT NULL COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '多租户ID',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标记:0未删除，1删除',
  `version` int(11) NOT NULL COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(19) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `org_index` (`org_code`,`org_name`) USING BTREE,
  KEY `pid` (`parent_id`) USING BTREE COMMENT '上级id',
  KEY `pids` (`parent_ids`) USING BTREE COMMENT '上级id集合',
  KEY `org_ids` (`org_ids`) USING BTREE COMMENT '组织id集合'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='组织机构表';

-- ----------------------------
-- Records of sys_org
-- ----------------------------
BEGIN;
INSERT INTO `sys_org` VALUES (1332710973848449026, 0, '0', '0,1332710973848449026', '0011', 'A公司', 1, NULL, 1, '0', 11, 1313694379541635074, '2020-11-28 23:40:31', 1, '2021-06-08 11:38:29', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1332878633177477122, 0, '0', '0,1332878633177477122', '0013', 'C公司', 3, NULL, 1, '0', 10, 1313694379541635074, '2020-11-29 10:46:44', 1, '2021-06-07 19:52:59', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1332879851278856193, 1332710973848449026, '0,1332710973848449026', '0,1332710973848449026,1332879851278856193', '0011_123', 'A公司B部门', 1, NULL, 1, '0', 6, 1313694379541635074, '2020-11-29 10:51:35', 1313694379541635074, '2021-02-25 13:31:17', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1332880775317581826, 1332879851278856193, '0,1332710973848449026,1332879851278856193', '0,1332710973848449026,1332879851278856193,1332880775317581826', '0011_123_003', '测试岗', 1, NULL, 1, '0', 5, 1313694379541635074, '2020-11-29 10:55:15', 1313694379541635074, '2021-02-25 13:30:51', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1336209704187879425, 0, '0', '0,1336209704187879425', '0012', 'B公司', 2, NULL, 1, '0', 3, 1313694379541635074, '2020-12-08 15:23:14', 1, '2021-01-11 19:19:51', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1401861234604605441, 0, '0', '0,1401861234604605441', '12334', 'D公司', 1, NULL, 1, '0', 0, 1, '2021-06-07 19:18:58', 1, '2021-06-07 19:18:58', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1401861575953842177, 1401861234604605441, '0,1401861234604605441', '0,1401861234604605441,1401861575953842177', '12334_111', 'D公司的XXX部门', 1, NULL, 1, '0', 0, 1, '2021-06-07 19:20:20', 1, '2021-06-07 19:20:20', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1401861991370293250, 1401873907685687297, '0,1401861234604605441,1401861575953842177,1401873907685687297', '0,1401861234604605441,1401861575953842177,1401873907685687297,1401861991370293250', '12334_111_111', '不知道哪儿个岗位', 1, NULL, 1, '0', 12, 1, '2021-06-07 19:21:59', 1, '2021-10-14 16:26:18', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1401873858511667201, 0, '0', '0,1401873858511667201', '123123', 'E公司', 1, NULL, 1, '0', 1, 1, '2021-06-07 20:09:08', 1313694379541635074, '2021-11-30 18:04:56', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1401873907685687297, 1401861575953842177, '0,1401861234604605441,1401861575953842177', '0,1401861234604605441,1401861575953842177,1401873907685687297', '123123_123', '123', 1, '123333', 1, '0', 1, 1, '2021-06-07 20:09:20', 1, '2021-10-14 16:26:18', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1448921834744115202, 1401873907685687297, '0,1401861234604605441,1401861575953842177,1401873907685687297', '0,1401861234604605441,1401861575953842177,1401873907685687297,1448921834744115202', '123123_123_1212', '啊啊啊啊', 2, NULL, 1, '0', 0, 1, '2021-10-15 16:01:00', 1, '2021-10-15 16:01:00', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1448921889865658369, 1401873907685687297, '0,1401861234604605441,1401861575953842177,1401873907685687297', '0,1401861234604605441,1401861575953842177,1401873907685687297,1448921889865658369', '123123_123_12312344', '测试测试', 3, NULL, 1, '0', 0, 1, '2021-10-15 16:01:13', 1, '2021-10-15 16:01:13', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1465586952575815681, 0, '0', '0,1465586952575815681', 'xxxx', 'XXXX集团', 1, NULL, 0, '0', 0, 1, '2021-11-30 15:42:13', 1, '2021-11-30 15:42:13', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1465587033538465793, 1465586952575815681, '0,1465586952575815681', '0,1465586952575815681,1465587033538465793', 'xxxx_bj', '北京子公司', 1, NULL, 0, '0', 0, 1, '2021-11-30 15:42:33', 1, '2021-11-30 15:42:33', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1465587103902109698, 1465586952575815681, '0,1465586952575815681', '0,1465586952575815681,1465587103902109698', 'xxxx_sh', '上海子公司', 2, NULL, 0, '0', 0, 1, '2021-11-30 15:42:50', 1, '2021-11-30 15:42:50', '2021-12-01 13:03:45');
INSERT INTO `sys_org` VALUES (1465973116898017281, 1401861575953842177, '0,1401861234604605441,1401861575953842177', '0,1401861234604605441,1401861575953842177,1465973116898017281', '12334_111_13334', 'XXX开发组', 2, NULL, 1, '0', 0, 1465886867659096066, '2021-12-01 17:16:42', 1465886867659096066, '2021-12-01 17:16:42', '2021-12-01 17:14:30');
INSERT INTO `sys_org` VALUES (1465996676186124290, 1401861575953842177, '0,1401861234604605441,1401861575953842177', '0,1401861234604605441,1401861575953842177,1465996676186124290', '12334_111_43223', '测试组', 3, NULL, 1, '0', 0, 1465886867659096066, '2021-12-01 18:50:19', 1465886867659096066, '2021-12-01 18:50:19', '2021-12-01 18:48:05');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(19) NOT NULL COMMENT '角色主键',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `label` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '标签',
  `data_scope` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '授权数据范围',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '多租户ID',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除标记:0未删除，1删除',
  `version` int(11) NOT NULL COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `role_code` (`role_code`,`role_name`,`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (2, '001', '管理员', '0', '3', '', 0, '0', 14, 1, '2018-12-09 17:48:13', 1, '2021-11-30 15:50:36', '2021-11-30 15:48:21');
INSERT INTO `sys_role` VALUES (1448924556381843458, '001', '职员', '1', '0', NULL, 1, '0', 1, 1313694379541635074, '2021-10-15 16:11:49', 1313694379541635074, '2021-10-15 17:16:27', '2021-11-29 18:13:29');
INSERT INTO `sys_role` VALUES (1448924616192618497, '002', '业务员', '1', '1', NULL, 1, '0', 1, 1313694379541635074, '2021-10-15 16:12:03', 1313694379541635074, '2021-10-15 17:16:37', '2021-11-29 18:13:31');
INSERT INTO `sys_role` VALUES (1448924680386441217, '003', '部门负责人', '1', '2', NULL, 1, '0', 2, 1313694379541635074, '2021-10-15 16:12:18', 1313694379541635074, '2021-10-15 17:16:56', '2021-11-29 18:13:32');
INSERT INTO `sys_role` VALUES (1448924738452385794, '004', '管理员', '1', '3', NULL, 1, '0', 3, 1313694379541635074, '2021-10-15 16:12:32', 1313694379541635074, '2021-11-30 18:04:50', '2021-11-30 18:02:35');
INSERT INTO `sys_role` VALUES (1463431580473810945, '1111', '默认租户角色', '1', '3', NULL, 0, '0', 1, 1, '2021-11-24 16:57:33', 1, '2021-11-29 11:42:18', '2021-11-29 11:40:02');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu_ref
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu_ref`;
CREATE TABLE `sys_role_menu_ref` (
  `id` bigint(19) NOT NULL COMMENT '用户角色关联',
  `menu_id` bigint(19) NOT NULL COMMENT '用户主键',
  `role_id` bigint(19) NOT NULL COMMENT '角色主键',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_role_menu` (`menu_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色功能关联表';

-- ----------------------------
-- Records of sys_role_menu_ref
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu_ref` VALUES (1505730836962099213, 1, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1312770352622878721, 1, 1312770323526991874);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375172, 3, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200001, 3, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403012, 3, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1312770352614490113, 4, 1312770323526991874);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403023, 4, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075793, 1312756531833356289, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375174, 1313789204920131585, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200003, 1313789204920131585, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403014, 1313789204920131585, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375175, 1313789308506857474, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200004, 1313789308506857474, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403015, 1313789308506857474, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375176, 1313789400169177089, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200005, 1313789400169177089, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403016, 1313789400169177089, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375177, 1313789529840279554, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200006, 1313789529840279554, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403017, 1313789529840279554, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375173, 1313806847370620930, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200002, 1313806847370620930, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403013, 1313806847370620930, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375178, 1313864645827678210, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200007, 1313864645827678210, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403018, 1313864645827678210, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375179, 1313864777918894082, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200008, 1313864777918894082, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403019, 1313864777918894082, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403024, 1313867061172195330, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403025, 1313867122731995137, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403026, 1313867360502894594, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403027, 1313867409949544450, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075794, 1313867556498526209, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075796, 1313867617949274113, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075797, 1313867682814185474, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075798, 1313867732508299265, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403028, 1313885644824522754, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075777, 1314066547072872450, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974880428034, 1314066547072872450, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960386, 1314066547072872450, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375170, 1314066547072872450, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325360091137, 1314066547072872450, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316080488449, 1314066547072872450, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075778, 1314066863436640258, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974880428035, 1314066863436640258, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960387, 1314066863436640258, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375171, 1314066863436640258, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325360091138, 1314066863436640258, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403010, 1314066863436640258, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075820, 1314068325453574145, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990356, 1314071137365307394, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990357, 1314074765178187777, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990358, 1314075128635600897, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990359, 1314075267769053186, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990360, 1314075542684708865, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990361, 1314075764852797442, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990362, 1314075970382082050, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990363, 1314076169481498625, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990364, 1314076280542474242, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990365, 1314076678317682689, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990366, 1314077008057085954, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990367, 1314077108560998402, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990368, 1314077229235318786, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990369, 1314077399507283970, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990370, 1314077518340304897, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990371, 1314077631905280001, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990372, 1314077729003417602, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990373, 1314120834868060162, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990374, 1314121004749955073, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990375, 1314121200103858178, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990376, 1314121675192672257, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990377, 1314121808793837570, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990378, 1314121928784486402, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990379, 1314122020136427521, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836962099201, 1314122123047870466, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990380, 1314122353273217025, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836962099202, 1314122457908518914, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836962099203, 1314122556776652802, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836962099204, 1314122628184678401, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836962099205, 1314122717041008641, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836962099206, 1314123071354839041, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836962099207, 1314123272790482945, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836962099208, 1314123690283114498, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836962099209, 1314123894637993985, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836962099210, 1314123990633029633, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836962099211, 1314124102365093890, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075816, 1314610817013919745, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075814, 1314616518671085570, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511897, 1314616518671085570, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075815, 1314782679522099201, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511898, 1314782679522099201, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990355, 1314786106243301378, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075819, 1314799744349913090, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075795, 1314884045724717057, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075779, 1315201380721446914, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075780, 1315201734892670977, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075781, 1315201809668722690, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075782, 1315201864219840513, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075783, 1315201925477650433, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974880428036, 1327085543511293954, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960388, 1327085543511293954, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375188, 1327085543511293954, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200017, 1327085543511293954, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403035, 1327085543511293954, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974880428037, 1327085856930660353, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960389, 1327085856930660353, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375189, 1327085856930660353, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200018, 1327085856930660353, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403036, 1327085856930660353, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974880428038, 1327086205548625921, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960390, 1327086205548625921, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375190, 1327086205548625921, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200019, 1327086205548625921, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511874, 1327086205548625921, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974880428039, 1327086298750255105, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960391, 1327086298750255105, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375191, 1327086298750255105, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200020, 1327086298750255105, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511875, 1327086298750255105, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342594, 1327086378794352642, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960392, 1327086378794352642, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375192, 1327086378794352642, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200021, 1327086378794352642, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511876, 1327086378794352642, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342595, 1327086433609711617, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960393, 1327086433609711617, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375193, 1327086433609711617, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200022, 1327086433609711617, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511877, 1327086433609711617, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990338, 1327893773049262082, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990339, 1327894701135491073, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990340, 1327894767283859457, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990341, 1327894837093855234, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990342, 1327894965179510785, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990345, 1327895061598171137, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990343, 1327903778221699074, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990344, 1329374800267452417, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342598, 1330365141900591105, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960396, 1330365141900591105, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375196, 1330365141900591105, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200025, 1330365141900591105, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511880, 1330365141900591105, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342599, 1330365525440331778, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960397, 1330365525440331778, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375197, 1330365525440331778, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200026, 1330365525440331778, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511881, 1330365525440331778, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342600, 1330365570587820033, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960398, 1330365570587820033, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375198, 1330365570587820033, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200027, 1330365570587820033, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511882, 1330365570587820033, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342601, 1330365615181660162, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960399, 1330365615181660162, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375199, 1330365615181660162, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200028, 1330365615181660162, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511883, 1330365615181660162, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342602, 1330365717015166977, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960400, 1330365717015166977, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375200, 1330365717015166977, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200029, 1330365717015166977, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511884, 1330365717015166977, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375183, 1332662450423635969, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200012, 1332662450423635969, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403030, 1332662450423635969, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375184, 1332662689314414594, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200013, 1332662689314414594, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403031, 1332662689314414594, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375185, 1332662758860169217, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200014, 1332662758860169217, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403032, 1332662758860169217, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375186, 1332662809711910913, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200015, 1332662809711910913, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403033, 1332662809711910913, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375187, 1332662858294534146, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200016, 1332662858294534146, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403034, 1332662858294534146, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075804, 1335439751687208961, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075806, 1335439904372457474, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075805, 1335440004809261058, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075807, 1335440081128816642, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075808, 1335440153140822017, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342603, 1337719928086458369, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910942457858, 1337719928086458369, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375201, 1337719928086458369, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200030, 1337719928086458369, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511885, 1337719928086458369, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342604, 1337720128930705409, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910950846466, 1337720128930705409, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375202, 1337720128930705409, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200031, 1337720128930705409, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511886, 1337720128930705409, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342596, 1337796232345407489, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960394, 1337796232345407489, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375194, 1337796232345407489, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200023, 1337796232345407489, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511878, 1337796232345407489, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342597, 1337796311940714498, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910866960395, 1337796311940714498, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375195, 1337796311940714498, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200024, 1337796311940714498, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511879, 1337796311940714498, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342605, 1340626549594677250, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910950846467, 1340626549594677250, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375203, 1340626549594677250, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200032, 1340626549594677250, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511887, 1340626549594677250, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342606, 1340626612895113217, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910950846468, 1340626612895113217, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375204, 1340626612895113217, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200033, 1340626612895113217, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511888, 1340626612895113217, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342607, 1340626666078887937, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910950846469, 1340626666078887937, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375205, 1340626666078887937, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200034, 1340626666078887937, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511889, 1340626666078887937, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342608, 1340626895356321793, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910950846470, 1340626895356321793, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375206, 1340626895356321793, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200035, 1340626895356321793, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511890, 1340626895356321793, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342609, 1340626939119689729, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910950846471, 1340626939119689729, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375207, 1340626939119689729, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200036, 1340626939119689729, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511891, 1340626939119689729, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342610, 1340626988251766786, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910950846472, 1340626988251766786, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375208, 1340626988251766786, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200037, 1340626988251766786, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511892, 1340626988251766786, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1448926974943342611, 1340627032942075906, 1448924556381843458);
INSERT INTO `sys_role_menu_ref` VALUES (1448926910950846473, 1340627032942075906, 1448924616192618497);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375209, 1340627032942075906, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200038, 1340627032942075906, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511893, 1340627032942075906, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075817, 1351012936860155906, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075818, 1351013587816136705, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075809, 1360233188433977345, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075810, 1360233383397810177, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375181, 1370051609388388353, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200010, 1370051609388388353, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403021, 1370051609388388353, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375182, 1370404146704654337, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200011, 1370404146704654337, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403022, 1370404146704654337, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075784, 1380173787882696705, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075799, 1387633500164599809, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075800, 1387633799226863618, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075801, 1387633960401383426, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075802, 1387634089447534594, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075803, 1387634157474951169, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990346, 1389555212654223361, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990337, 1397807288445526017, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990347, 1397807399338729473, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990348, 1397807399363895298, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990349, 1397807399389061121, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990350, 1397807399414226945, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990351, 1397807399439392770, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990352, 1397807399456169985, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990353, 1397807399481335810, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836894990354, 1399667141430452225, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403029, 1448537070279237634, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075785, 1460629524738764802, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836962099212, 1460639200696160257, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375210, 1460639200696160257, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200039, 1460639200696160257, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403011, 1460639200696160257, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1465888361674375180, 1465587677695479810, 1448924680386441217);
INSERT INTO `sys_role_menu_ref` VALUES (1465888325427200009, 1465587677695479810, 1448924738452385794);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316143403020, 1465587677695479810, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075786, 1465621050623209474, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075787, 1465621206781341698, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075788, 1465621319830417409, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075789, 1465621468124229634, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075790, 1465621601087860738, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075791, 1465621733564952578, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075792, 1465621843787067394, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075811, 1504776412970254338, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511894, 1504776412970254338, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075812, 1504779965155655682, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511895, 1504779965155655682, 1463431580473810945);
INSERT INTO `sys_role_menu_ref` VALUES (1505730836832075813, 1504780214448308226, 2);
INSERT INTO `sys_role_menu_ref` VALUES (1504780316210511896, 1504780214448308226, 1463431580473810945);
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
  `id` bigint(19) NOT NULL COMMENT '唯一主键',
  `tenant_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户名称',
  `enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否启用  0否  1是',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除标记:0未删除，1删除',
  `version` int(11) NOT NULL COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `role_code` (`tenant_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色信息表';

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant` VALUES (1, '演示租户', '1', '演示租户', '0', 18, 1, '2017-03-08 15:00:42', 1465171199435362305, '2022-07-26 10:43:31', '2022-07-26 10:43:31');
INSERT INTO `sys_tenant` VALUES (1315214795665907713, '测试租户', '0', '不启用租户', '0', 0, 1, '2020-10-11 16:56:57', 1, '2020-10-11 16:56:57', '2021-04-08 23:51:26');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(19) NOT NULL COMMENT '用户主键',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账户',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
  `password_level` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录密码强度',
  `enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否启用',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
  `mobile` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机',
  `no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户头像',
  `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后登陆IP',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `sign` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '签名',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `iz_exist_org` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否存在组织',
  `iz_tenant_admin` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否租户管理员',
  `tenant_id` bigint(19) DEFAULT NULL COMMENT '多租户ID',
  `enable_switch_tenant` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '允许切换租户（0 不允许 1 允许）',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除状态',
  `version` int(11) NOT NULL COMMENT '版本（乐观锁）',
  `create_by` bigint(19) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(19) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_user` (`username`,`real_name`) USING BTREE,
  KEY `tenant` (`tenant_id`) USING BTREE COMMENT '租户'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user`(`id`, `username`, `password`, `password_level`, `enable`, `real_name`, `mobile`, `no`, `avatar`, `login_ip`, `email`, `sign`, `remark`, `iz_exist_org`, `iz_tenant_admin`, `tenant_id`, `enable_switch_tenant`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`, `ts`) VALUES (1, 'system', 'TS{MTY1OTM2NzA0MjMzOA==}$2a$10$FtVMZrf/LeM4ikU2ZXO8XuhmRDpJmIxLVm6Fa3ZEM8jVgZ2CFhuH6', '2', '1', '超级管理员', '15321010110', '112', 'http://upload.bedebug.com/20220802/1660928576913664278HC1G.jpg', '', 'meet.parker@foxmail.com', '没有自学能力的人没有未来', '', '0', '0', 0, '1', '0', 95, 1, '2020-09-25 15:03:22', 1, '2021-05-04 01:59:11', '2022-08-02 15:29:33');
INSERT INTO `sys_user`(`id`, `username`, `password`, `password_level`, `enable`, `real_name`, `mobile`, `no`, `avatar`, `login_ip`, `email`, `sign`, `remark`, `iz_exist_org`, `iz_tenant_admin`, `tenant_id`, `enable_switch_tenant`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`, `ts`) VALUES (1313694379541635074, 'demo', 'TS{MTY1ODI5ODUzMzMwOA==}$2a$10$TS0LnHPhQT87rEpFi1A60.BeTQ80vCRGGL.5CPTqb61eCzP8HzueK', '2', '1', '租户内部管理员', '13301225424', 'test_001', 'http://upload.bedebug.com/20220809/1660188426197843638MXWG.jpg', '', 'meet.parker1@foxmail.com', NULL, NULL, '1', '0', 1, '0', '0', 6, 1, '2020-10-06 23:15:22', 1465879900211294210, '2021-12-01 16:42:13', '2022-08-09 13:55:05');
INSERT INTO `sys_user`(`id`, `username`, `password`, `password_level`, `enable`, `real_name`, `mobile`, `no`, `avatar`, `login_ip`, `email`, `sign`, `remark`, `iz_exist_org`, `iz_tenant_admin`, `tenant_id`, `enable_switch_tenant`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`, `ts`) VALUES (1315218541317750785, 'zhangsan', 'TS{MTY1ODI5ODUzMzMwOA==}$2a$10$TS0LnHPhQT87rEpFi1A60.BeTQ80vCRGGL.5CPTqb61eCzP8HzueK', '2', '1', '张三', '55555555555', '123123', NULL, '', 'meet.parker3@foxmail.com', NULL, NULL, '1', '0', 1, '0', '0', 3, 1, '2020-10-11 17:11:50', 1, '2021-06-11 17:44:11', '2022-08-09 14:01:18');
INSERT INTO `sys_user`(`id`, `username`, `password`, `password_level`, `enable`, `real_name`, `mobile`, `no`, `avatar`, `login_ip`, `email`, `sign`, `remark`, `iz_exist_org`, `iz_tenant_admin`, `tenant_id`, `enable_switch_tenant`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`, `ts`) VALUES (1315224823500120066, 'lyf', 'TS{MTY1ODI5ODUzMzMwOA==}$2a$10$TS0LnHPhQT87rEpFi1A60.BeTQ80vCRGGL.5CPTqb61eCzP8HzueK', '2', '1', '刘亦菲', '44444444444', '0101001', NULL, '', 'meet.parker4@foxmail.com', NULL, NULL, '1', '0', 1, '0', '0', 0, 1313694379541635074, '2020-10-11 17:36:48', 1313694379541635074, '2020-10-11 17:36:48', '2022-08-09 14:01:22');
INSERT INTO `sys_user`(`id`, `username`, `password`, `password_level`, `enable`, `real_name`, `mobile`, `no`, `avatar`, `login_ip`, `email`, `sign`, `remark`, `iz_exist_org`, `iz_tenant_admin`, `tenant_id`, `enable_switch_tenant`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`, `ts`) VALUES (1465171199435362305, 'admin', 'TS{MTY1OTM2NDMzMzM5Nw==}$2a$10$2GTqJeztWRLOYDoB2EStm.FoncwpHG4GGtieD7qZbND1.cwjXx34u', '2', '1', '系统管理员', '33333333333', '01001', 'http://upload.bedebug.com/20220809/16605552760947393781AWR.jpg', '', 'meet.parker5@foxmail.com', NULL, NULL, '1', '0', 0, '1', '0', 6, 1, '2021-11-29 12:10:10', 1, '2022-07-26 20:16:02', '2022-08-09 14:01:26');
INSERT INTO `sys_user`(`id`, `username`, `password`, `password_level`, `enable`, `real_name`, `mobile`, `no`, `avatar`, `login_ip`, `email`, `sign`, `remark`, `iz_exist_org`, `iz_tenant_admin`, `tenant_id`, `enable_switch_tenant`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`, `ts`) VALUES (1465879900211294210, 'tenant', 'TS{MTY1ODI5ODUzMzMwOA==}$2a$10$TS0LnHPhQT87rEpFi1A60.BeTQ80vCRGGL.5CPTqb61eCzP8HzueK', '2', '1', '租户管理员', '17310558930', '123123', 'http://upload.bedebug.com/20220809/166121046267039280RQ81U.jpg', '', 'meet.parker2@foxmail.com', NULL, NULL, '0', '1', 1, '0', '0', 1, 1, '2021-12-01 11:06:17', 1, '2021-12-01 11:06:53', '2022-08-09 13:55:40');
INSERT INTO `sys_user`(`id`, `username`, `password`, `password_level`, `enable`, `real_name`, `mobile`, `no`, `avatar`, `login_ip`, `email`, `sign`, `remark`, `iz_exist_org`, `iz_tenant_admin`, `tenant_id`, `enable_switch_tenant`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`, `ts`) VALUES (1465886867659096066, 'dept', 'TS{MTY1ODI5ODUzMzMwOA==}$2a$10$TS0LnHPhQT87rEpFi1A60.BeTQ80vCRGGL.5CPTqb61eCzP8HzueK', '2', '1', '部门管理员', '22222222222', '123213213', 'http://upload.bedebug.com/20220809/16610489376695984679HUA.jpg', '', 'meet.parker6@foxmail.com', NULL, NULL, '1', '0', 1, '0', '0', 0, 1313694379541635074, '2021-12-01 11:33:59', 1313694379541635074, '2021-12-01 11:33:59', '2022-08-09 14:01:30');
INSERT INTO `sys_user`(`id`, `username`, `password`, `password_level`, `enable`, `real_name`, `mobile`, `no`, `avatar`, `login_ip`, `email`, `sign`, `remark`, `iz_exist_org`, `iz_tenant_admin`, `tenant_id`, `enable_switch_tenant`, `deleted`, `version`, `create_by`, `create_time`, `update_by`, `update_time`, `ts`) VALUES (1465991640378986498, 'songyi', 'TS{MTY1ODI5ODUzMzMwOA==}$2a$10$TS0LnHPhQT87rEpFi1A60.BeTQ80vCRGGL.5CPTqb61eCzP8HzueK', '2', '1', '宋轶', '11111111111', '432431', 'http://upload.bedebug.com/20220809/1661003971159749330PV6N.jpg', '', 'meet.parker7@foxmail.com', NULL, NULL, '1', '0', 1, '0', '0', 0, 1465886867659096066, '2021-12-01 18:30:19', 1465886867659096066, '2021-12-01 18:30:19', '2022-08-09 14:01:33');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_org_ref
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_org_ref`;
CREATE TABLE `sys_user_org_ref` (
  `id` bigint(19) NOT NULL COMMENT '用户角色关联',
  `user_id` bigint(19) NOT NULL COMMENT '用户主键',
  `org_id` bigint(19) NOT NULL COMMENT '当前组织机构',
  `org_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组织机构组',
  `iz_def` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否默认',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_org_user` (`user_id`,`org_ids`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='组织机构用户关联表';

-- ----------------------------
-- Records of sys_user_org_ref
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_org_ref` VALUES (1403403014114373634, 1315218541317750785, 1332879851278856193, '0,1332710973848449026,1332879851278856193', '1');
INSERT INTO `sys_user_org_ref` VALUES (1448922152303259650, 1315224823500120066, 1448921834744115202, '0,1401861234604605441,1401861575953842177,1401873907685687297,1448921834744115202', '1');
INSERT INTO `sys_user_org_ref` VALUES (1448925001863065602, 1448923198635307009, 1401861575953842177, '0,1401861234604605441,1401861575953842177', '1');
INSERT INTO `sys_user_org_ref` VALUES (1465587924685459458, 1465171199435362305, 1465586952575815681, '0,1465586952575815681', '1');
INSERT INTO `sys_user_org_ref` VALUES (1465886975591120898, 1465886867659096066, 1401861575953842177, '0,1401861234604605441,1401861575953842177', '1');
INSERT INTO `sys_user_org_ref` VALUES (1465964516926427137, 1313694379541635074, 1401861234604605441, '0,1401861234604605441', '1');
INSERT INTO `sys_user_org_ref` VALUES (1465964516926427138, 1313694379541635074, 1401873858511667201, '0,1401873858511667201', '0');
INSERT INTO `sys_user_org_ref` VALUES (1465964516926427139, 1313694379541635074, 1332710973848449026, '0,1332710973848449026', '0');
INSERT INTO `sys_user_org_ref` VALUES (1465988921358503938, 1465988919403958274, 1448921889865658369, '0,1401861234604605441,1401861575953842177,1401873907685687297,1448921889865658369', '1');
INSERT INTO `sys_user_org_ref` VALUES (1465988921421418498, 1465988919403958274, 1448921889865658369, '0,1401861234604605441,1401861575953842177,1401873907685687297,1448921889865658369', '0');
INSERT INTO `sys_user_org_ref` VALUES (1465991642006376449, 1465991640378986498, 1448921889865658369, '0,1401861234604605441,1401861575953842177,1401873907685687297,1448921889865658369', '1');
INSERT INTO `sys_user_org_ref` VALUES (1465991642006376450, 1465991640378986498, 1448921889865658369, '0,1401861234604605441,1401861575953842177,1401873907685687297,1448921889865658369', '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role_ref
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role_ref`;
CREATE TABLE `sys_user_role_ref` (
  `id` bigint(19) NOT NULL COMMENT '用户角色关联',
  `user_id` bigint(19) NOT NULL COMMENT '用户主键',
  `role_id` bigint(19) NOT NULL COMMENT '角色主键',
  `iz_def` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否默认',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_user_role` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联表';

-- ----------------------------
-- Records of sys_user_role_ref
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role_ref` VALUES (1448927772578328577, 1315224823500120066, 1448924556381843458, '1');
INSERT INTO `sys_user_role_ref` VALUES (1448943254769410051, 1448923198635307009, 1448924556381843458, '0');
INSERT INTO `sys_user_role_ref` VALUES (1448943254769410052, 1448923198635307009, 1448924616192618497, '0');
INSERT INTO `sys_user_role_ref` VALUES (1448943254769410053, 1448923198635307009, 1448924680386441217, '1');
INSERT INTO `sys_user_role_ref` VALUES (1448943254769410054, 1448923198635307009, 1448924738452385794, '0');
INSERT INTO `sys_user_role_ref` VALUES (1460635992343007233, 1460635865448534017, 2, '1');
INSERT INTO `sys_user_role_ref` VALUES (1465589100898349058, 1465171199435362305, 2, '1');
INSERT INTO `sys_user_role_ref` VALUES (1465882374703853570, 1465879900211294210, 1463431580473810945, '1');
INSERT INTO `sys_user_role_ref` VALUES (1465886929952899073, 1465886867659096066, 1448924680386441217, '1');
INSERT INTO `sys_user_role_ref` VALUES (1465964680936296449, 1313694379541635074, 1448924680386441217, '1');
INSERT INTO `sys_user_role_ref` VALUES (1465997319281979394, 1465991640378986498, 1448924556381843458, '1');
COMMIT;

-- ----------------------------
-- Table structure for test_car
-- ----------------------------
DROP TABLE IF EXISTS `test_car`;
CREATE TABLE `test_car` (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `car_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '汽车名称',
  `car_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '汽车类型',
  `car_brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '汽车品牌',
  `produce_data` date NOT NULL COMMENT '生产日期',
  `iz_usable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否启用',
  `tenant_id` bigint(19) DEFAULT NULL COMMENT '多租户ID',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除标记:0未删除，1删除',
  `version` int(10) NOT NULL COMMENT '版本号(乐观锁)',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  `org_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组织机构组',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='测试汽车';

-- ----------------------------
-- Records of test_car
-- ----------------------------
BEGIN;
INSERT INTO `test_car` VALUES (1340634224709677058, '奔驰GXX', 'Gxx', '奔驰', '2020-12-20', '1', 1, '0', 0, 1313694379541635074, '2020-12-20 20:24:42', 1313694379541635074, '2020-12-20 20:24:42', '2021-04-08 23:51:39', '');
INSERT INTO `test_car` VALUES (1340634337075081217, '北汽绅宝', '电动', '绅宝', '2020-12-13', '1', 1, '0', 0, 1313694379541635074, '2020-12-20 20:25:08', 1313694379541635074, '2020-12-20 20:25:08', '2021-04-08 23:51:39', '');
INSERT INTO `test_car` VALUES (1340634432696823810, '宝马7系', '烧油', '宝马', '2020-12-20', '0', 1, '0', 0, 1313694379541635074, '2020-12-20 20:25:31', 1313694379541635074, '2020-12-20 20:25:31', '2021-04-08 23:51:39', '');
INSERT INTO `test_car` VALUES (1340634485616357378, '测试', '123', '123', '2020-12-07', '0', 1, '1', 0, 1313694379541635074, '2020-12-20 20:25:44', 1313694379541635074, '2020-12-20 20:25:44', '2021-04-08 23:51:39', '');
INSERT INTO `test_car` VALUES (1340635288087375874, '宝马7系', '烧油', '宝马', '2020-12-20', '0', 1, '0', 2, 1313694379541635074, '2020-12-20 20:28:55', 1313694379541635074, '2021-01-18 17:08:23', '2021-04-08 23:51:39', '');
INSERT INTO `test_car` VALUES (1340635288095764482, '北汽绅宝', '电动', '绅宝', '2020-12-13', '1', 1, '0', 0, 1313694379541635074, '2020-12-20 20:28:55', 1313694379541635074, '2020-12-20 20:28:55', '2021-04-08 23:51:39', '');
INSERT INTO `test_car` VALUES (1340635288099958786, '奔驰GXX', 'Gxx', '奔驰', '2020-12-20', '1', 1, '0', 0, 1313694379541635074, '2020-12-20 20:28:55', 1313694379541635074, '2020-12-20 20:28:55', '2021-04-08 23:51:39', '');
INSERT INTO `test_car` VALUES (1448923884416593921, '演示汽车', '1111', '111', '2021-10-05', '1', 1, '1', 1, 1313694379541635074, '2021-10-15 16:09:08', 1, '2021-10-15 16:34:49', '2021-10-15 17:12:38', '0,1401861234604605441,1401861575953842177,1401873907685687297,1448921834744115202');
INSERT INTO `test_car` VALUES (1448927202366894081, '自己_范冰冰', '11111', '11111', '2021-10-04', '1', 1, '0', 1, 1448923198635307009, '2021-10-15 16:22:19', 1, '2021-10-15 16:34:37', '2021-10-15 16:32:54', '0,1401861234604605441,1401861575953842177');
INSERT INTO `test_car` VALUES (1448927860478357506, '自己_刘亦菲', '111', '2222', '2021-10-04', '1', 1, '0', 1, 1315224823500120066, '2021-10-15 16:24:56', 1, '2021-10-15 16:34:28', '2021-10-15 16:32:45', '0,1401861234604605441,1401861575953842177,1401873907685687297,1448921834744115202');
INSERT INTO `test_car` VALUES (1448940392903516161, '自己_演示', '123123', '123123', '2021-10-04', '1', 1, '0', 1, 1313694379541635074, '2021-10-15 17:14:44', 1313694379541635074, '2021-11-30 18:05:11', '2021-11-30 18:02:56', '0,1401861234604605441,1401861575953842177');
INSERT INTO `test_car` VALUES (1465997520956698625, '宋轶自己的车', '油车', '宝马', '2021-12-01', '1', 1, '0', 0, 1465991640378986498, '2021-12-01 18:53:41', 1465991640378986498, '2021-12-01 18:53:41', '2021-12-01 18:51:27', '0,1401861234604605441,1401861575953842177,1401873907685687297,1448921889865658369');
INSERT INTO `test_car` VALUES (1465999901249384450, '测试汽车', '测试汽车', '测试汽车', '2021-12-01', '1', 1, '0', 0, 1465879900211294210, '2021-12-01 19:03:08', 1465879900211294210, '2021-12-01 19:03:08', '2021-12-01 19:00:54', NULL);
INSERT INTO `test_car` VALUES (1555921049517580290, '111', 'ddd', 'aaa', '2022-08-01', '1', 0, '0', 0, 1, '2022-08-06 22:17:40', 1, '2022-08-06 22:17:40', '2022-08-06 22:17:40', NULL);
INSERT INTO `test_car` VALUES (1555921343362129922, '自己_超管', '11', '111', '2022-08-02', '1', 0, '0', 0, 1, '2022-08-06 22:18:51', 1, '2022-08-06 22:18:51', '2022-08-06 22:18:50', NULL);
COMMIT;

-- ----------------------------
-- Table structure for test_car_copy1
-- ----------------------------
DROP TABLE IF EXISTS `test_car_copy1`;
CREATE TABLE `test_car_copy1` (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `car_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '汽车名称',
  `car_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '汽车类型',
  `car_brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '汽车品牌',
  `produce_data` date NOT NULL COMMENT '生产日期',
  `iz_usable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否启用',
  `tenant_id` bigint(19) DEFAULT NULL COMMENT '多租户ID',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除标记:0未删除，1删除',
  `version` int(10) NOT NULL COMMENT '版本号(乐观锁)',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='测试';

-- ----------------------------
-- Table structure for test_car_copy2
-- ----------------------------
DROP TABLE IF EXISTS `test_car_copy2`;
CREATE TABLE `test_car_copy2` (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `car_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '汽车名称',
  `car_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '汽车类型',
  `car_brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '汽车品牌',
  `produce_data` date NOT NULL COMMENT '生产日期',
  `iz_usable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否启用',
  `tenant_id` bigint(19) DEFAULT NULL COMMENT '多租户ID',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除标记:0未删除，1删除',
  `version` int(10) NOT NULL COMMENT '版本号(乐观锁)',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='测试';

-- ----------------------------
-- Table structure for test_car_copy3
-- ----------------------------
DROP TABLE IF EXISTS `test_car_copy3`;
CREATE TABLE `test_car_copy3` (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `car_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '汽车名称',
  `car_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '汽车类型',
  `car_brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '汽车品牌',
  `produce_data` date NOT NULL COMMENT '生产日期',
  `iz_usable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否启用',
  `tenant_id` bigint(19) DEFAULT NULL COMMENT '多租户ID',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除标记:0未删除，1删除',
  `version` int(10) NOT NULL COMMENT '版本号(乐观锁)',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='测试';

-- ----------------------------
-- Table structure for test_car_copy4
-- ----------------------------
DROP TABLE IF EXISTS `test_car_copy4`;
CREATE TABLE `test_car_copy4` (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `car_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '汽车名称',
  `car_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '汽车类型',
  `car_brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '汽车品牌',
  `produce_data` date NOT NULL COMMENT '生产日期',
  `iz_usable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否启用',
  `tenant_id` bigint(19) DEFAULT NULL COMMENT '多租户ID',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除标记:0未删除，1删除',
  `version` int(10) NOT NULL COMMENT '版本号(乐观锁)',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='测试';

-- ----------------------------
-- Table structure for test_car_copy5
-- ----------------------------
DROP TABLE IF EXISTS `test_car_copy5`;
CREATE TABLE `test_car_copy5` (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `car_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '汽车名称',
  `car_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '汽车类型',
  `car_brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '汽车品牌',
  `produce_data` date NOT NULL COMMENT '生产日期',
  `iz_usable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否启用',
  `tenant_id` bigint(19) DEFAULT NULL COMMENT '多租户ID',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除标记:0未删除，1删除',
  `version` int(10) NOT NULL COMMENT '版本号(乐观锁)',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='测试';

-- ----------------------------
-- Table structure for test_entity
-- ----------------------------
DROP TABLE IF EXISTS `test_entity`;
CREATE TABLE `test_entity` (
  `id` bigint(19) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tenant_id` bigint(19) DEFAULT NULL COMMENT '多租户ID',
  `create_by` bigint(19) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_by` bigint(19) NOT NULL,
  `update_time` datetime NOT NULL,
  `version` int(11) NOT NULL,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for test_user
-- ----------------------------
DROP TABLE IF EXISTS `test_user`;
CREATE TABLE `test_user` (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `money` double(8,2) NOT NULL COMMENT '金钱',
  `age` smallint(5) NOT NULL COMMENT '年龄',
  `birth` date NOT NULL COMMENT '生日',
  `iz_usable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否启用',
  `tenant_id` bigint(19) DEFAULT NULL COMMENT '多租户ID',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除标记:0未删除，1删除',
  `version` int(10) NOT NULL COMMENT '版本号(乐观锁)',
  `create_by` bigint(19) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` bigint(19) NOT NULL COMMENT '修改用户',
  `update_time` datetime NOT NULL COMMENT '修改日期',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='某系统用户';

SET FOREIGN_KEY_CHECKS = 1;
