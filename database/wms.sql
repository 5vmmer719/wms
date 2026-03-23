/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.176.20-33307
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 192.168.176.20:3307
 Source Schema         : wms1

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 30/04/2023 16:21:50
*/



SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_location
-- ----------------------------
DROP TABLE IF EXISTS `base_location`;
CREATE TABLE `base_location`  (
  `location_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库',
  `location_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货位编码',
  `location_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货位名称',
  `location_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货位类型',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`location_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '货位' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_location
-- ----------------------------
INSERT INTO `base_location` VALUES (1, '1001', 'A01', '纯碱', NULL, '0', NULL, '2022-07-24 10:33:46', NULL, '2022-07-24 21:33:17');
INSERT INTO `base_location` VALUES (2, '1001', 'A02', '石英砂', NULL, '0', NULL, '2022-07-24 10:34:01', NULL, '2022-07-24 12:01:41');

-- ----------------------------
-- Table structure for base_mat
-- ----------------------------
DROP TABLE IF EXISTS `base_mat`;
CREATE TABLE `base_mat`  (
  `mat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `mat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料编码',
  `mat_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料描述',
  `fd_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '财务编码',
  `fig_num` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图号',
  `mat_group` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料组',
  `mat_class` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类',
  `unit_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基本单位',
  `gross_weight` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '毛重',
  `safety_stock` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '安全库存',
  `standard_price` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '标准价',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`mat_id`) USING BTREE,
  INDEX `MAT_CODE`(`mat_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '物料主数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_mat
-- ----------------------------
INSERT INTO `base_mat` VALUES (1, '10000001', '平板玻璃', '10000001', 'BL-Glass', 'ZC', 'ZC', 'UNIT', 0.000000, 0.000000, 0.000000, '0', NULL, '2022-07-24 14:10:36', 'admin', '2022-08-08 17:18:41');
INSERT INTO `base_mat` VALUES (2, '11000001', '纯碱', '11000001', 'BL-CJ', 'ZZ', 'LS', 'PCS', 0.000000, 0.000000, 0.000000, '0', NULL, '2022-07-24 14:13:56', 'admin', '2022-07-24 18:25:34');
INSERT INTO `base_mat` VALUES (3, '11000002', '石英砂', '11000002', 'BL-SYS', 'WG', 'TH', 'PCS', 0.000000, 0.000000, 0.000000, '0', NULL, '2022-07-24 14:15:10', 'admin', '2022-07-24 18:25:34');

-- ----------------------------
-- Table structure for base_mat_bom
-- ----------------------------
DROP TABLE IF EXISTS `base_mat_bom`;
CREATE TABLE `base_mat_bom`  (
  `bom_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `father_mat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父物料编码',
  `father_mat_num` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '父项数量',
  `child_no` int(11) NULL DEFAULT NULL COMMENT '子项行项目号',
  `child_mat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子项物料编码',
  `child_mat_num` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '子项数量',
  `is_fictitious` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否虚拟键',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`bom_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '物料BOM' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_mat_bom
-- ----------------------------
INSERT INTO `base_mat_bom` VALUES (7, '10000001', 1.000000, 1, '11000001', 2.000000, 'N', 'admin', '2022-07-24 20:39:29', NULL, NULL);
INSERT INTO `base_mat_bom` VALUES (8, '10000001', 1.000000, 2, '11000002', 3.000000, 'N', 'admin', '2022-07-24 20:39:30', NULL, NULL);

-- ----------------------------
-- Table structure for base_mat_class
-- ----------------------------
DROP TABLE IF EXISTS `base_mat_class`;
CREATE TABLE `base_mat_class`  (
  `class_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `class_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类编码',
  `class_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`class_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '物料分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_mat_class
-- ----------------------------
INSERT INTO `base_mat_class` VALUES (1, 'LS', '纯碱', '0', NULL, '2022-07-23 20:27:00', NULL, NULL);
INSERT INTO `base_mat_class` VALUES (2, 'TH', '石英砂', '0', NULL, '2022-07-23 20:27:12', NULL, '2022-07-23 20:27:26');
INSERT INTO `base_mat_class` VALUES (3, 'ZC', '成品玻璃', '0', NULL, '2022-07-24 14:10:56', NULL, NULL);

-- ----------------------------
-- Table structure for base_mat_group
-- ----------------------------
DROP TABLE IF EXISTS `base_mat_group`;
CREATE TABLE `base_mat_group`  (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料组编码',
  `group_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料组名称',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '物料组' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_mat_group
-- ----------------------------
INSERT INTO `base_mat_group` VALUES (1, 'ZC', '成品玻璃', '0', NULL, '2022-07-23 20:24:57', NULL, NULL);
INSERT INTO `base_mat_group` VALUES (2, 'ZZ', '原材料', '0', NULL, '2022-07-23 20:25:52', NULL, NULL);
INSERT INTO `base_mat_group` VALUES (3, 'WG', '辅料', '0', NULL, '2022-07-23 20:26:02', NULL, NULL);

-- ----------------------------
-- Table structure for base_supplier
-- ----------------------------
DROP TABLE IF EXISTS `base_supplier`;
CREATE TABLE `base_supplier`  (
  `supplier_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `supplier_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `supply_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供货名称',
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `contact` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `tax_number` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '税号',
  `deposit_bank` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `bank_account` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `is_qualified` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否合格供应商',
  `city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市',
  `postal_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`supplier_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '供应商' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_supplier
-- ----------------------------
INSERT INTO `base_supplier` VALUES (1, 'WHCJGYS', '沙河玻璃原料供应商', '纯碱', '河北省邢台市沙河市', '18519100654', '123', '123', '123', 'Y', '沙河', '054100', '0', NULL, '2022-07-24 10:01:35', NULL, '2022-07-24 10:04:27');
INSERT INTO `base_supplier` VALUES (4, 'WHCJGYS2', '秦皇岛石英砂供应商', '石英砂', '河北省秦皇岛市', '18519100654', '234', '234', '234', 'Y', '秦皇岛', '066000', '0', NULL, '2022-07-24 10:05:01', NULL, '2022-07-24 10:05:34');
INSERT INTO `base_supplier` VALUES (5, 'test', 'test', 'test', 'test', 'test', 'test', 'test', 'test', 'Y', 'test', 'test', '0', NULL, '2023-04-30 15:11:30', NULL, NULL);

-- ----------------------------
-- Table structure for base_warehouse
-- ----------------------------
DROP TABLE IF EXISTS `base_warehouse`;
CREATE TABLE `base_warehouse`  (
  `warehouse_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`warehouse_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仓库' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_warehouse
-- ----------------------------
INSERT INTO `base_warehouse` VALUES (1, '1001', '原料仓库', '0', NULL, '2022-07-24 09:37:16', NULL, NULL);
INSERT INTO `base_warehouse` VALUES (2, '1002', '成品仓库', '0', NULL, '2022-07-24 11:57:35', NULL, NULL);

-- ----------------------------
-- Table structure for base_workshop
-- ----------------------------
DROP TABLE IF EXISTS `base_workshop`;
CREATE TABLE `base_workshop`  (
  `workshop_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `workshop_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车间编码',
  `workshop_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车间名称',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`workshop_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车间' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_workshop
-- ----------------------------
INSERT INTO `base_workshop` VALUES (1, 'NO1', '玻璃熔制车间', '0', NULL, '2022-07-28 12:31:55', NULL, '2022-07-28 12:32:03');

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (77, 'sys_user_class', '用户管理的物料分类', NULL, NULL, 'SysUserClass', 'crud', 'com.ruoyi.system', 'system', 'class', '用户管理的物料分类', 'ruoyi', '0', '/', '{\"parentMenuId\":1}', 'admin', '2022-08-11 01:17:08', '', '2022-08-11 01:17:21', NULL);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1050 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (1048, '77', 'user_name', '用户账号', 'varchar(64)', 'String', 'userName', '0', '0', NULL, '1', '1', '1', '1', 'LIKE', 'input', '', 1, 'admin', '2022-08-11 01:17:08', '', '2022-08-11 01:17:21');
INSERT INTO `gen_table_column` VALUES (1049, '77', 'class_code', '物料分类编码', 'varchar(64)', 'String', 'classCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-08-11 01:17:08', '', '2022-08-11 01:17:21');

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob NULL COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Blob类型的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日历信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Cron类型的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint(20) NOT NULL COMMENT '触发的时间',
  `sched_time` bigint(20) NOT NULL COMMENT '定时器制定的时间',
  `priority` int(11) NOT NULL COMMENT '优先级',
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '已触发的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务详细信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储的悲观锁信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '暂停的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint(20) NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint(20) NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '调度器状态表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint(20) NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint(20) NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint(20) NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '简单触发器的信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int(11) NULL DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int(11) NULL DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint(20) NULL DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint(20) NULL DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '同步机制的行锁表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint(20) NULL DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint(20) NULL DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int(11) NULL DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器的类型',
  `start_time` bigint(20) NOT NULL COMMENT '开始时间',
  `end_time` bigint(20) NULL DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint(6) NULL DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `sched_name`(`sched_name`, `job_name`, `job_group`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '触发器详细信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for stock_allot_detail
-- ----------------------------
DROP TABLE IF EXISTS `stock_allot_detail`;
CREATE TABLE `stock_allot_detail`  (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `allot_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '调拨单号',
  `src_warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发起仓库',
  `dest_warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标仓库',
  `src_location_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发起货位',
  `dest_location_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标货位',
  `line_no` int(11) NULL DEFAULT NULL COMMENT '行号',
  `label_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料标签id',
  `mat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料编码',
  `mat_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `fd_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '财务编码',
  `fig_num` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图号',
  `mat_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料组',
  `mat_class` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料分类',
  `batch` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次',
  `quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '数量',
  `sign_quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '签收数量',
  `unit_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `supplier_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '调拨单详情;' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_allot_detail
-- ----------------------------
INSERT INTO `stock_allot_detail` VALUES (3, 'A20220805212137', '1001', '1002', 'A01', 'A01', 1, '1', '11000001', '纯碱', '11000001', 'BL-CJ', 'ZZ', 'LS', 'CG20220726121220', 1.000000, 1.000000, 'PCS', 'WHCJGYS', '沙河玻璃原料供应商', '0', 'admin', '2022-08-06 17:43:38', 'admin', '2022-08-07 11:44:10');

-- ----------------------------
-- Table structure for stock_allot_order
-- ----------------------------
DROP TABLE IF EXISTS `stock_allot_order`;
CREATE TABLE `stock_allot_order`  (
  `allot_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `allot_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '调拨单号',
  `allot_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '调拨原因',
  `src_warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发起仓库',
  `dest_warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标仓库',
  `allot_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '调拨单状态',
  `allot_progress` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '调拨进度',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`allot_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '调拨单;' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_allot_order
-- ----------------------------
INSERT INTO `stock_allot_order` VALUES (1, 'A20220805212137', '放错位置', '1001', '1002', 'printed', 'receive', '0', 'admin', '2022-08-05 21:21:30', 'admin', '2022-08-07 11:44:10');

-- ----------------------------
-- Table structure for stock_in_detail
-- ----------------------------
DROP TABLE IF EXISTS `stock_in_detail`;
CREATE TABLE `stock_in_detail`  (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库单号',
  `line_no` int(11) NULL DEFAULT NULL COMMENT '行号',
  `mat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料编码',
  `mat_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `fd_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '财务编码',
  `fig_num` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图号',
  `mat_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料组',
  `mat_class` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料分类',
  `unit_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `label_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料标签id',
  `batch` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次',
  `quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '数量',
  `qualified_quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '合格数量',
  `stock_in_quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '入库数量',
  `unit_price` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '单价',
  `supplier_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `prod_time` datetime NULL DEFAULT NULL COMMENT '生产时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入库单详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_in_detail
-- ----------------------------
INSERT INTO `stock_in_detail` VALUES (9, '1001', 'IP20220727192324', 2, '11000002', '石英砂', '11000002', 'BL-SYS', 'WG', 'TH', 'PCS', '2', 'CG20220726122639', 1.000000, 1.000000, 1.000000, 88.000000, 'WHCJGYS2', '秦皇岛石英砂供应商', '2022-07-26 00:00:00', '0', 'admin', '2022-07-27 19:23:31', 'admin', '2022-08-03 17:19:49');
INSERT INTO `stock_in_detail` VALUES (10, '1001', 'IP20220727192324', 1, '11000001', '纯碱', '11000001', 'BL-CJ', 'ZZ', 'LS', 'PCS', '1', 'CG20220726121220', 1.000000, 1.000000, 1.000000, 20.000000, 'WHCJGYS', '沙河玻璃原料供应商', '2022-07-26 00:00:00', '0', 'admin', '2022-07-27 19:23:31', 'admin', '2022-08-03 17:19:49');
INSERT INTO `stock_in_detail` VALUES (11, '1001', 'IP20220729170164', 1, '11000001', '纯碱', '11000001', 'BL-CJ', 'ZZ', 'LS', 'PCS', '3', 'CG20220729170041', 1.000000, 1.000000, 1.000000, 127.200000, 'WHCJGYS2', '秦皇岛石英砂供应商', '2022-07-29 00:00:00', '0', 'admin', '2022-07-29 17:01:23', 'admin', '2022-08-03 20:31:04');
INSERT INTO `stock_in_detail` VALUES (12, NULL, 'IP20220809115240', 1, '11000002', '石英砂', '11000002', 'BL-SYS', 'WG', 'TH', 'PCS', '4', 'CG20220803171636', 20.000000, 20.000000, 0.000000, 123.000000, 'WHCJGYS', '沙河玻璃原料供应商', '2022-08-03 00:00:00', '0', 'admin', '2022-08-09 11:52:04', 'admin', '2022-08-09 11:52:37');

-- ----------------------------
-- Table structure for stock_in_order
-- ----------------------------
DROP TABLE IF EXISTS `stock_in_order`;
CREATE TABLE `stock_in_order`  (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据号',
  `order_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据类型',
  `order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据状态',
  `check_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检验状态',
  `check_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检验员',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入库单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_in_order
-- ----------------------------
INSERT INTO `stock_in_order` VALUES (6, 'IP20220727192324', 'purchase', 'printed', 'checkout', 'admin', '0', 'admin', '2022-07-27 19:23:31', 'admin', '2022-08-09 11:51:38');
INSERT INTO `stock_in_order` VALUES (7, 'IP20220729170164', 'purchase', 'printed', 'checkout', 'admin', '0', 'admin', '2022-07-29 17:01:23', 'admin', '2022-08-09 11:51:48');
INSERT INTO `stock_in_order` VALUES (8, 'IP20220809115240', 'purchase', 'printed', 'checkout', 'admin', '0', 'admin', '2022-08-09 11:52:04', 'admin', '2022-08-09 11:52:47');

-- ----------------------------
-- Table structure for stock_in_return
-- ----------------------------
DROP TABLE IF EXISTS `stock_in_return`;
CREATE TABLE `stock_in_return`  (
  `return_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `return_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库退货单号',
  `return_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库退货类型',
  `return_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库退货原因',
  `return_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退货单据状态',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库单号',
  `warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`return_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入库单退货' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_in_return
-- ----------------------------
INSERT INTO `stock_in_return` VALUES (7, 'IPR20220804210147', 'purchase_return', '不好用', 'returned', 'IP20220729170164', '1001', '0', 'admin', '2022-08-04 21:01:20', 'admin', '2022-08-04 21:23:47');

-- ----------------------------
-- Table structure for stock_in_return_detail
-- ----------------------------
DROP TABLE IF EXISTS `stock_in_return_detail`;
CREATE TABLE `stock_in_return_detail`  (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库',
  `return_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库退货单号',
  `line_no` int(11) NULL DEFAULT NULL COMMENT '行号',
  `label_id` bigint(20) NULL DEFAULT NULL COMMENT '物料标签id',
  `mat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料编码',
  `mat_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `fd_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '财务编码',
  `fig_num` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图号',
  `mat_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料组',
  `mat_class` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料分类',
  `unit_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `batch` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次',
  `quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '数量',
  `return_quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '退还数量',
  `location_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货位',
  `supplier_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入库单退货详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_in_return_detail
-- ----------------------------
INSERT INTO `stock_in_return_detail` VALUES (10, '1001', 'IPR20220804210147', 1, 3, '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220729170041', 1.000000, 1.000000, 'A01', 'WHCJGYS2', '武汉超级供应商2', '0', 'admin', '2022-08-04 21:01:20', 'admin', '2022-08-04 21:23:47');

-- ----------------------------
-- Table structure for stock_info
-- ----------------------------
DROP TABLE IF EXISTS `stock_info`;
CREATE TABLE `stock_info`  (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库',
  `location_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货位',
  `mat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料编码',
  `mat_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `fd_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '财务编码',
  `fig_num` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图号',
  `mat_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料组',
  `mat_class` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料分类',
  `unit_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `batch` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次',
  `quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '数量',
  `supplier_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '库存信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_info
-- ----------------------------
INSERT INTO `stock_info` VALUES (23, '1001', 'A01', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220726121220', 0.000000, 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-03 17:19:49', 'admin', '2022-08-03 17:19:49');
INSERT INTO `stock_info` VALUES (24, '1001', 'A02', '11000002', '石英砂', '11000002', '鄂energy-TH', 'WG', 'TH', 'PCS', 'CG20220726122639', 1.000000, 'WHCJGYS2', '武汉超级供应商2', '0', 'admin', '2022-08-03 17:19:49', 'admin', '2022-08-03 17:19:49');
INSERT INTO `stock_info` VALUES (25, '1001', 'A01', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220729170041', 0.000000, 'WHCJGYS2', '武汉超级供应商2', '0', 'admin', '2022-08-03 20:31:04', 'admin', '2022-08-03 20:31:04');
INSERT INTO `stock_info` VALUES (27, '1002', 'A01', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220726121220', 1.000000, 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-07 11:44:10', 'admin', '2022-08-07 11:30:25');

-- ----------------------------
-- Table structure for stock_mat_label
-- ----------------------------
DROP TABLE IF EXISTS `stock_mat_label`;
CREATE TABLE `stock_mat_label`  (
  `label_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库',
  `location_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货位',
  `label_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签类型',
  `mat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料编码',
  `mat_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料描述',
  `fd_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '财务编码',
  `fig_num` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图号',
  `mat_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料组',
  `mat_class` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类',
  `unit_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基本单位',
  `batch` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次',
  `supplier_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `prod_time` datetime NULL DEFAULT NULL COMMENT '生产时间',
  `quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '数量',
  `usable_quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '可用数量',
  `received_quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '已领取数量',
  `unit_price` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '单价',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库单号',
  `order_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库单类型',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`label_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '物料标签' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_mat_label
-- ----------------------------
INSERT INTO `stock_mat_label` VALUES (1, '1002', 'A01', 'purchase', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220726121220', 'WHCJGYS', '武汉超级供应商', '2022-07-26 00:00:00', 1.000000, 1.000000, 0.000000, 20.000000, 'IP20220727192324', 'purchase', '0', 'admin', '2022-07-26 12:13:31', 'admin', '2022-08-07 11:44:10');
INSERT INTO `stock_mat_label` VALUES (2, '1001', 'A02', 'purchase', '11000002', '石英砂', '11000002', '鄂energy-TH', 'WG', 'TH', 'PCS', 'CG20220726122639', 'WHCJGYS2', '武汉超级供应商2', '2022-07-26 00:00:00', 1.000000, 1.000000, 0.000000, 88.000000, 'IP20220727192324', 'purchase', '0', 'admin', '2022-07-26 12:26:53', 'admin', '2022-08-04 18:42:31');
INSERT INTO `stock_mat_label` VALUES (3, '1001', 'A01', 'purchase', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220729170041', 'WHCJGYS2', '武汉超级供应商2', '2022-07-29 00:00:00', 1.000000, 1.000000, 1.000000, 127.200000, 'IP20220729170164', 'purchase', '0', 'admin', '2022-07-29 17:01:09', 'admin', '2022-08-04 21:23:47');
INSERT INTO `stock_mat_label` VALUES (4, NULL, NULL, 'purchase', '11000002', '石英砂', '11000002', '鄂energy-TH', 'WG', 'TH', 'PCS', 'CG20220803171636', 'WHCJGYS', '武汉超级供应商', '2022-08-03 00:00:00', 20.000000, 0.000000, 0.000000, 123.000000, 'IP20220809115240', 'purchase', '0', 'admin', '2022-08-03 17:16:55', NULL, NULL);
INSERT INTO `stock_mat_label` VALUES (5, NULL, NULL, 'purchase', '11000002', '石英砂', '11000002', '鄂energy-TH', 'WG', 'TH', 'PCS', 'CG20220822202912', 'WHCJGYS', '武汉超级供应商', '2022-08-22 00:00:00', 1.000000, 0.000000, 0.000000, 0.000000, NULL, NULL, '0', 'admin', '2022-08-22 20:29:20', NULL, NULL);

-- ----------------------------
-- Table structure for stock_out_detail
-- ----------------------------
DROP TABLE IF EXISTS `stock_out_detail`;
CREATE TABLE `stock_out_detail`  (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库',
  `workshop_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车间',
  `location_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货位',
  `prod_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生产订单号',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出库单号',
  `line_no` int(11) NULL DEFAULT NULL COMMENT '行号',
  `mat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料编码',
  `mat_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `fd_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '财务编码',
  `fig_num` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图号',
  `mat_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料组',
  `mat_class` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料分类',
  `batch` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次',
  `quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '数量',
  `received_quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '已领数量',
  `unit_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '出库单详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_out_detail
-- ----------------------------
INSERT INTO `stock_out_detail` VALUES (9, '1001', 'NO1', '暂无库存', 'P20220728140756', 'OP20220804183758', 1, '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', NULL, 2.000000, 1.000000, 'PCS', '0', 'admin', '2022-08-04 18:37:53', 'admin', '2022-08-09 17:36:19');
INSERT INTO `stock_out_detail` VALUES (10, '1001', 'NO1', 'A02', 'P20220728140756', 'OP20220804183758', 2, '11000002', '石英砂', '11000002', '鄂energy-TH', 'WG', 'TH', NULL, 3.000000, 1.000000, 'PCS', '0', 'admin', '2022-08-04 18:37:53', 'admin', '2022-08-09 17:36:19');
INSERT INTO `stock_out_detail` VALUES (11, '1001', 'NO1', '暂无库存', NULL, 'OC20220805085419', 1, '11000001', '纯碱', '11000001', '鄂energy-LS', NULL, NULL, NULL, 1.000000, 1.000000, 'PCS', '0', 'admin', '2022-08-05 08:54:33', 'admin', '2022-08-08 15:09:36');
INSERT INTO `stock_out_detail` VALUES (12, '1001', 'NO1', 'A02', NULL, 'OC20220809115354', 1, '11000002', '石英砂', '11000002', '鄂energy-TH', NULL, NULL, NULL, 1.000000, 0.000000, 'PCS', '0', 'admin', '2022-08-09 11:53:28', NULL, '2022-08-09 11:53:31');
INSERT INTO `stock_out_detail` VALUES (14, '1001', 'NO1', '暂无库存', 'P20220728140756', 'OP20220818120393', 1, '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', NULL, 2.000000, 0.000000, 'PCS', '0', 'admin', '2022-08-18 12:03:05', NULL, '2022-08-18 12:04:19');

-- ----------------------------
-- Table structure for stock_out_order
-- ----------------------------
DROP TABLE IF EXISTS `stock_out_order`;
CREATE TABLE `stock_out_order`  (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据号',
  `order_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据类型',
  `prod_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生产订单号',
  `warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库',
  `workshop_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车间',
  `order_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原因',
  `mat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料编码',
  `mat_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '数量',
  `order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据状态',
  `warehouse_keeper` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库管员',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '出库单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_out_order
-- ----------------------------
INSERT INTO `stock_out_order` VALUES (8, 'OP20220804183758', 'production', 'P20220728140756', '1001', 'NO1', NULL, '10000001', '平板玻璃', 1.000000, 'printed', 'admin', '0', 'admin', '2022-08-04 18:37:53', 'admin', '2022-08-09 17:36:20');
INSERT INTO `stock_out_order` VALUES (9, 'OC20220805085419', 'common', NULL, '1001', 'NO1', '生产', NULL, NULL, 0.000000, 'printed', NULL, '0', 'admin', '2022-08-05 08:54:33', 'admin', '2022-08-08 15:08:28');
INSERT INTO `stock_out_order` VALUES (10, 'OC20220809115354', 'common', NULL, '1001', 'NO1', '生产使用', NULL, NULL, 0.000000, 'printed', NULL, '0', 'admin', '2022-08-09 11:53:28', 'admin', '2022-08-09 11:53:32');
INSERT INTO `stock_out_order` VALUES (12, 'OP20220818120393', 'production', 'P20220728140756', '1001', 'NO1', NULL, '10000001', '平板玻璃', 1.000000, 'printed', 'admin', '0', 'admin', '2022-08-18 12:03:05', 'admin', '2022-08-18 12:04:22');

-- ----------------------------
-- Table structure for stock_out_return
-- ----------------------------
DROP TABLE IF EXISTS `stock_out_return`;
CREATE TABLE `stock_out_return`  (
  `return_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `return_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出库退货单号',
  `warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库',
  `workshop_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车间',
  `return_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退货类型',
  `return_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退货原因',
  `return_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退货状态',
  `prod_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生产订单号',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出库单号',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`return_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '出库单退货' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_out_return
-- ----------------------------
INSERT INTO `stock_out_return` VALUES (6, 'OPR20220804184139', '1001', 'NO1', 'production_return', '有故障', 'printed', 'P20220728140756', 'OP20220804183758', '0', 'admin', '2022-08-04 18:41:48', 'admin', '2022-08-04 21:20:00');
INSERT INTO `stock_out_return` VALUES (7, 'OCR20220805091230', '1001', 'NO1', 'common_return', '不好使用', 'returned', NULL, 'OC20220805085419', '0', 'admin', '2022-08-05 09:12:25', 'admin', '2022-08-05 09:58:48');
INSERT INTO `stock_out_return` VALUES (8, 'OCR20220809200684', '1001', 'NO1', 'common_return', '不好', 'created', NULL, 'OC20220805085419', '0', 'admin', '2022-08-09 20:06:59', NULL, NULL);

-- ----------------------------
-- Table structure for stock_out_return_detail
-- ----------------------------
DROP TABLE IF EXISTS `stock_out_return_detail`;
CREATE TABLE `stock_out_return_detail`  (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库',
  `workshop_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车间',
  `location_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货位',
  `return_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出库退货单号',
  `line_no` int(11) NULL DEFAULT NULL COMMENT '行号',
  `label_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料标签id',
  `mat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料编码',
  `mat_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `fd_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '财务编码',
  `fig_num` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图号',
  `mat_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料组',
  `mat_class` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料分类',
  `batch` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次',
  `quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '数量',
  `return_quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '退还数量',
  `unit_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `supplier_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '出库退货详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_out_return_detail
-- ----------------------------
INSERT INTO `stock_out_return_detail` VALUES (11, '1001', 'NO1', 'A01', 'OPR20220804184139', 1, '1', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'CG20220726121220', 1.000000, 1.000000, 'PCS', 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-04 18:41:48', 'admin', '2022-08-04 18:42:31');
INSERT INTO `stock_out_return_detail` VALUES (12, '1001', 'NO1', 'A02', 'OPR20220804184139', 2, '2', '11000002', '石英砂', '11000002', '鄂energy-TH', 'WG', 'TH', 'CG20220726122639', 1.000000, 1.000000, 'PCS', 'WHCJGYS2', '武汉超级供应商2', '0', 'admin', '2022-08-04 18:41:48', 'admin', '2022-08-04 18:42:31');
INSERT INTO `stock_out_return_detail` VALUES (13, '1001', 'NO1', 'A01', 'OCR20220805091230', 1, '1', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'CG20220726121220', 1.000000, 1.000000, 'PCS', 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-05 09:12:25', 'admin', '2022-08-05 09:58:48');
INSERT INTO `stock_out_return_detail` VALUES (14, '1001', 'NO1', 'A01', 'OCR20220809200684', 1, '1', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'CG20220726121220', 1.000000, 0.000000, 'PCS', 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-09 20:06:59', NULL, NULL);

-- ----------------------------
-- Table structure for stock_prod_order
-- ----------------------------
DROP TABLE IF EXISTS `stock_prod_order`;
CREATE TABLE `stock_prod_order`  (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据号',
  `work_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工令号',
  `mat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料编码',
  `mat_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `workshop_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车间',
  `quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '数量',
  `order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '生产订单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_prod_order
-- ----------------------------
INSERT INTO `stock_prod_order` VALUES (2, 'P20220728140756', 'GL20226666', '10000001', '平板玻璃', 'NO1', 1.000000, 'ongoing', '0', 'admin', '2022-07-28 14:07:41', NULL, NULL);

-- ----------------------------
-- Table structure for stock_record
-- ----------------------------
DROP TABLE IF EXISTS `stock_record`;
CREATE TABLE `stock_record`  (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `warehouse_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库',
  `location_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货位',
  `label_id` bigint(20) NULL DEFAULT NULL COMMENT '物料标签id',
  `record_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流水类型',
  `mat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料编码',
  `mat_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `fd_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '财务编码',
  `fig_num` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图号',
  `mat_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料组',
  `mat_class` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料分类',
  `unit_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `batch` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次',
  `quantity` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '数量',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据号',
  `workshop_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车间',
  `supplier_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '库存流水' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stock_record
-- ----------------------------
INSERT INTO `stock_record` VALUES (25, '1001', 'A01', 1, 'in_purchase', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220726121220', 1.000000, 'IP20220727192324', NULL, 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-03 17:19:49', 'admin', '2022-08-03 17:19:49');
INSERT INTO `stock_record` VALUES (26, '1001', 'A02', 2, 'in_purchase', '11000002', '石英砂', '11000002', '鄂energy-TH', 'WG', 'TH', 'PCS', 'CG20220726122639', 1.000000, 'IP20220727192324', NULL, 'WHCJGYS2', '武汉超级供应商2', '0', 'admin', '2022-08-03 17:19:49', 'admin', '2022-08-03 17:19:49');
INSERT INTO `stock_record` VALUES (29, '1001', 'A01', 3, 'in_purchase', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220729170041', 1.000000, 'IP20220729170164', NULL, 'WHCJGYS2', '武汉超级供应商2', '0', 'admin', '2022-08-03 20:31:04', 'admin', '2022-08-03 20:31:04');
INSERT INTO `stock_record` VALUES (33, '1001', 'A01', 1, 'out_production', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220726121220', 1.000000, 'OP20220804183758', 'NO1', 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-04 18:39:41', 'admin', '2022-08-04 18:18:42');
INSERT INTO `stock_record` VALUES (34, '1001', 'A02', 2, 'out_production', '11000002', '石英砂', '11000002', '鄂energy-TH', 'WG', 'TH', 'PCS', 'CG20220726122639', 1.000000, 'OP20220804183758', 'NO1', 'WHCJGYS2', '武汉超级供应商2', '0', 'admin', '2022-08-04 18:39:41', 'admin', '2022-08-04 18:18:42');
INSERT INTO `stock_record` VALUES (35, '1001', 'A01', 1, 'out_production_return', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220726121220', 1.000000, 'OP20220804183758', 'NO1', 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-04 18:42:31', 'admin', '2022-08-04 18:42:31');
INSERT INTO `stock_record` VALUES (36, '1001', 'A02', 2, 'out_production_return', '11000002', '石英砂', '11000002', '鄂energy-TH', 'WG', 'TH', 'PCS', 'CG20220726122639', 1.000000, 'OP20220804183758', 'NO1', 'WHCJGYS2', '武汉超级供应商2', '0', 'admin', '2022-08-04 18:42:31', 'admin', '2022-08-04 18:42:31');
INSERT INTO `stock_record` VALUES (37, '1001', 'A01', 3, 'in_purchase_return', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220729170041', 1.000000, 'IPR20220804210147', NULL, 'WHCJGYS2', '武汉超级供应商2', '0', 'admin', '2022-08-04 21:23:47', 'admin', '2022-08-04 18:07:15');
INSERT INTO `stock_record` VALUES (39, '1001', 'A01', 1, 'lower', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220726121220', 1.000000, 'IP20220727192324', NULL, 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-04 22:11:53', NULL, NULL);
INSERT INTO `stock_record` VALUES (41, '1001', 'A01', 1, 'upper', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220726121220', 1.000000, 'IP20220727192324', NULL, 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-04 22:29:02', NULL, NULL);
INSERT INTO `stock_record` VALUES (42, '1001', 'A01', 1, 'out_common', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220726121220', 1.000000, 'OC20220805085419', 'NO1', 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-05 08:55:19', 'admin', '2022-08-04 22:29:02');
INSERT INTO `stock_record` VALUES (43, '1001', 'A01', 1, 'out_common_return', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220726121220', 1.000000, 'OC20220805085419', 'NO1', 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-05 09:58:48', 'admin', '2022-08-05 09:58:48');
INSERT INTO `stock_record` VALUES (46, '1001', 'A01', 1, 'allot_out', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220726121220', 1.000000, 'A20220805212137', NULL, 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-06 17:43:38', 'admin', '2022-08-06 17:41:14');
INSERT INTO `stock_record` VALUES (48, '1002', 'A01', 1, 'allot_in', '11000001', '纯碱', '11000001', '鄂energy-LS', 'ZZ', 'LS', 'PCS', 'CG20220726121220', 1.000000, 'A20220805212137', NULL, 'WHCJGYS', '武汉超级供应商', '0', 'admin', '2022-08-07 11:44:10', 'admin', '2022-08-07 11:30:25');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2022-07-10 01:24:56', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2022-07-10 01:24:56', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2022-07-10 01:24:56', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaOnOff', 'true', 'Y', 'admin', '2022-07-10 01:24:56', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2022-07-10 01:24:56', '', NULL, '是否开启注册用户功能（true开启，false关闭）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 202 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '总公司', 0, '老板', '15888888888', '123@qq.com', '0', '0', 'admin', '2022-07-10 01:24:55', '', NULL);
INSERT INTO `sys_dept` VALUES (200, 100, '0,100', '武汉总公司', 1, NULL, NULL, NULL, '0', '0', 'admin', '2022-07-23 05:23:15', '', NULL);
INSERT INTO `sys_dept` VALUES (201, 200, '0,100,200', '研发部', 1, NULL, NULL, NULL, '0', '0', 'admin', '2022-07-23 05:23:30', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(11) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 113 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (19, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (20, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (21, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (22, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (23, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (24, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (25, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (26, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (27, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (28, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (110, 1, '箱', 'PCS', 'base_mat_unit', NULL, 'default', 'N', '0', 'admin', '2022-07-24 06:02:32', 'admin', '2022-07-24 06:05:19', NULL);
INSERT INTO `sys_dict_data` VALUES (111, 2, '吨', 'T', 'base_mat_unit', NULL, 'default', 'N', '0', 'admin', '2022-07-24 06:02:50', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (112, 3, '件', 'UNIT', 'base_mat_unit', NULL, 'default', 'N', '0', 'admin', '2022-07-24 06:12:53', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 104 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2022-07-10 01:24:56', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type` VALUES (103, '物料单位', 'base_mat_unit', '0', 'admin', '2022-07-24 06:01:45', '', NULL, '物料单位');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2022-07-10 01:24:56', '', NULL, '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2022-07-10 01:24:56', '', NULL, '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2022-07-10 01:24:56', '', NULL, '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 853 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(11) NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int(11) NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2166 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 22, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2022-07-10 01:24:55', 'admin', '2022-07-14 08:05:50', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 33, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2022-07-10 01:24:55', 'admin', '2022-07-14 08:06:00', '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 44, 'tool', NULL, '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2022-07-10 01:24:55', 'admin', '2022-07-14 08:06:06', '系统工具目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2022-07-10 01:24:55', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2022-07-10 01:24:55', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2022-07-10 01:24:55', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2022-07-10 01:24:55', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2022-07-10 01:24:55', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2022-07-10 01:24:55', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2022-07-10 01:24:55', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2022-07-10 01:24:55', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2022-07-10 01:24:55', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2022-07-10 01:24:55', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2022-07-10 01:24:55', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', '2022-07-10 01:24:55', '', NULL, '数据监控菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2022-07-10 01:24:55', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2022-07-10 01:24:55', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', '2022-07-10 01:24:55', '', NULL, '缓存列表菜单');
INSERT INTO `sys_menu` VALUES (115, '表单构建', 3, 1, 'build', 'tool/build/index', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2022-07-10 01:24:55', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2022-07-10 01:24:55', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2022-07-10 01:24:55', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2022-07-10 01:24:55', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2022-07-10 01:24:55', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '在线查询', 109, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '批量强退', 109, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '单条强退', 109, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '任务查询', 110, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务新增', 110, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务修改', 110, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务删除', 110, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '状态修改', 110, 5, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '任务导出', 110, 7, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '生成查询', 115, 1, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成修改', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成删除', 115, 3, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '导入代码', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '预览代码', 115, 4, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '生成代码', 115, 5, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2028, '数据管理', 0, 1, 'base', NULL, NULL, 1, 0, 'M', '0', '0', '', 'table', 'admin', '2022-07-23 05:32:57', 'admin', '2022-07-23 07:39:45', '');
INSERT INTO `sys_menu` VALUES (2029, '库存管理', 0, 6, 'stock', NULL, NULL, 1, 0, 'M', '0', '0', '', 'redis', 'admin', '2022-07-23 05:34:17', 'admin', '2022-07-25 12:41:29', '');
INSERT INTO `sys_menu` VALUES (2030, '采购管理', 0, 2, 'purchase', NULL, NULL, 1, 0, 'M', '0', '0', '', 'shopping', 'admin', '2022-07-23 05:35:16', 'admin', '2022-07-25 12:41:33', '');
INSERT INTO `sys_menu` VALUES (2031, '质检管理', 0, 3, 'check', NULL, NULL, 1, 0, 'M', '0', '0', '', 'search', 'admin', '2022-07-23 05:37:57', 'admin', '2022-07-25 12:41:38', '');
INSERT INTO `sys_menu` VALUES (2032, '生产管理', 0, 4, 'prod', NULL, NULL, 1, 0, 'M', '0', '0', '', 'swagger', 'admin', '2022-07-23 05:39:02', 'admin', '2022-07-25 12:41:43', '');
INSERT INTO `sys_menu` VALUES (2033, '通用管理', 0, 5, 'common', NULL, NULL, 1, 0, 'M', '0', '0', '', 'nested', 'admin', '2022-07-23 05:40:15', 'admin', '2022-07-25 12:41:49', '');
INSERT INTO `sys_menu` VALUES (2034, '统计管理', 0, 8, 'stats', NULL, NULL, 1, 0, 'M', '0', '0', '', 'chart', 'admin', '2022-07-23 05:41:29', 'admin', '2022-08-05 09:07:57', '');
INSERT INTO `sys_menu` VALUES (2036, '物料Bom管理', 2028, 2, 'bom', 'base/bom/index', NULL, 1, 0, 'C', '0', '0', 'base:bom:list', 'cascader', 'admin', '2022-07-23 07:20:01', 'admin', '2022-07-23 07:48:11', '物料BOM菜单');
INSERT INTO `sys_menu` VALUES (2037, '物料BOM查询', 2036, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:bom:query', '#', 'admin', '2022-07-23 07:20:01', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2038, '物料BOM新增', 2036, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:bom:add', '#', 'admin', '2022-07-23 07:20:01', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2039, '物料BOM修改', 2036, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:bom:edit', '#', 'admin', '2022-07-23 07:20:01', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2040, '物料BOM删除', 2036, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:bom:remove', '#', 'admin', '2022-07-23 07:20:01', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2041, '物料BOM导出', 2036, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:bom:export', '#', 'admin', '2022-07-23 07:20:01', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2042, '物料分类管理', 2028, 4, 'class', 'base/class/index', NULL, 1, 0, 'C', '0', '0', 'base:class:list', 'input', 'admin', '2022-07-23 07:20:06', 'admin', '2022-07-23 07:43:25', '物料分类菜单');
INSERT INTO `sys_menu` VALUES (2043, '物料分类查询', 2042, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:class:query', '#', 'admin', '2022-07-23 07:20:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2044, '物料分类新增', 2042, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:class:add', '#', 'admin', '2022-07-23 07:20:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2045, '物料分类修改', 2042, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:class:edit', '#', 'admin', '2022-07-23 07:20:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2046, '物料分类删除', 2042, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:class:remove', '#', 'admin', '2022-07-23 07:20:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2047, '物料分类导出', 2042, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:class:export', '#', 'admin', '2022-07-23 07:20:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2048, '物料组管理', 2028, 3, 'group', 'base/group/index', NULL, 1, 0, 'C', '0', '0', 'base:group:list', 'row', 'admin', '2022-07-23 07:20:12', 'admin', '2022-07-23 07:41:58', '物料组菜单');
INSERT INTO `sys_menu` VALUES (2049, '物料组查询', 2048, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:group:query', '#', 'admin', '2022-07-23 07:20:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2050, '物料组新增', 2048, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:group:add', '#', 'admin', '2022-07-23 07:20:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2051, '物料组修改', 2048, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:group:edit', '#', 'admin', '2022-07-23 07:20:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2052, '物料组删除', 2048, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:group:remove', '#', 'admin', '2022-07-23 07:20:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2053, '物料组导出', 2048, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:group:export', '#', 'admin', '2022-07-23 07:20:12', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2054, '货位管理', 2028, 7, 'location', 'base/location/index', NULL, 1, 0, 'C', '0', '0', 'base:location:list', 'tree-table', 'admin', '2022-07-23 07:20:17', 'admin', '2022-07-24 06:34:56', '货位菜单');
INSERT INTO `sys_menu` VALUES (2055, '货位查询', 2054, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:location:query', '#', 'admin', '2022-07-23 07:20:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2056, '货位新增', 2054, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:location:add', '#', 'admin', '2022-07-23 07:20:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2057, '货位修改', 2054, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:location:edit', '#', 'admin', '2022-07-23 07:20:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2058, '货位删除', 2054, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:location:remove', '#', 'admin', '2022-07-23 07:20:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2059, '货位导出', 2054, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:location:export', '#', 'admin', '2022-07-23 07:20:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2060, '物料数据管理', 2028, 1, 'mat', 'base/mat/index', NULL, 1, 0, 'C', '0', '0', 'base:mat:list', 'documentation', 'admin', '2022-07-23 07:20:23', 'admin', '2022-07-23 07:48:02', '物料主数据菜单');
INSERT INTO `sys_menu` VALUES (2061, '物料主数据查询', 2060, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:mat:query', '#', 'admin', '2022-07-23 07:20:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2062, '物料主数据新增', 2060, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:mat:add', '#', 'admin', '2022-07-23 07:20:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2063, '物料主数据修改', 2060, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:mat:edit', '#', 'admin', '2022-07-23 07:20:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2064, '物料主数据删除', 2060, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:mat:remove', '#', 'admin', '2022-07-23 07:20:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2065, '物料主数据导出', 2060, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:mat:export', '#', 'admin', '2022-07-23 07:20:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2066, '供应商管理', 2028, 5, 'supplier', 'base/supplier/index', NULL, 1, 0, 'C', '0', '0', 'base:supplier:list', 'peoples', 'admin', '2022-07-23 07:20:28', 'admin', '2022-07-24 02:07:21', '供应商菜单');
INSERT INTO `sys_menu` VALUES (2067, '供应商查询', 2066, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:supplier:query', '#', 'admin', '2022-07-23 07:20:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2068, '供应商新增', 2066, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:supplier:add', '#', 'admin', '2022-07-23 07:20:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2069, '供应商修改', 2066, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:supplier:edit', '#', 'admin', '2022-07-23 07:20:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2070, '供应商删除', 2066, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:supplier:remove', '#', 'admin', '2022-07-23 07:20:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2071, '供应商导出', 2066, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:supplier:export', '#', 'admin', '2022-07-23 07:20:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2077, '质检单管理', 2031, 1, 'checkOrder', 'check/inOrder/index', NULL, 1, 0, 'C', '0', '0', 'check:inOrder:list', 'tab', 'admin', '2022-07-23 09:33:44', 'admin', '2022-07-28 00:41:45', '');
INSERT INTO `sys_menu` VALUES (2082, '出库单管理', 2033, 1, 'commonOutOrder', 'common/outOrder/index', NULL, 1, 0, 'C', '0', '0', 'stock:common:outOrder:list', 'tab', 'admin', '2022-07-23 12:08:13', 'admin', '2022-08-07 14:37:29', '');
INSERT INTO `sys_menu` VALUES (2083, '出库退货管理', 2033, 2, 'commonOutReturn', 'common/outReturn/index', NULL, 1, 0, 'C', '0', '0', 'stock:common:outReturn:list', 'component', 'admin', '2022-07-23 12:08:35', 'admin', '2022-08-07 14:37:38', '');
INSERT INTO `sys_menu` VALUES (2084, '入库统计', 2034, 1, 'stockInStats', 'stats/stockIn/index', NULL, 1, 0, 'C', '0', '0', 'stats:stockIn:list', 'chart', 'admin', '2022-07-23 12:09:08', 'admin', '2022-08-05 03:39:36', '');
INSERT INTO `sys_menu` VALUES (2085, '出库统计', 2034, 2, 'stockOutStats', 'stats/stockOut/index', NULL, 1, 0, 'C', '0', '0', 'stats:stockOut:list', 'chart', 'admin', '2022-07-23 12:09:42', 'admin', '2022-08-05 03:40:36', '');
INSERT INTO `sys_menu` VALUES (2086, '出入库汇总', 2034, 3, 'stockRecordStats', 'stats/stockRecord/index', NULL, 1, 0, 'C', '0', '0', 'stats:stockRecord:list', 'list', 'admin', '2022-07-23 12:10:55', 'admin', '2022-08-07 08:22:03', '');
INSERT INTO `sys_menu` VALUES (2093, '仓库管理', 2028, 6, 'warehouse', 'base/warehouse/index', NULL, 1, 0, 'C', '0', '0', 'base:warehouse:list', 'tree', 'admin', '2022-07-24 00:54:51', 'admin', '2022-07-24 02:07:29', '仓库菜单');
INSERT INTO `sys_menu` VALUES (2094, '仓库查询', 2093, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:warehouse:query', '#', 'admin', '2022-07-24 00:54:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2095, '仓库新增', 2093, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:warehouse:add', '#', 'admin', '2022-07-24 00:54:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2096, '仓库修改', 2093, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:warehouse:edit', '#', 'admin', '2022-07-24 00:54:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2097, '仓库删除', 2093, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:warehouse:remove', '#', 'admin', '2022-07-24 00:54:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2098, '仓库导出', 2093, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:warehouse:export', '#', 'admin', '2022-07-24 00:54:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2099, '库存信息管理', 2029, 1, 'info', 'stock/info/index', NULL, 1, 0, 'C', '0', '0', 'stock:info:list', 'excel', 'admin', '2022-07-25 12:29:02', 'admin', '2022-07-25 12:37:18', '库存信息菜单');
INSERT INTO `sys_menu` VALUES (2100, '库存信息查询', 2099, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:info:query', '#', 'admin', '2022-07-25 12:29:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2101, '库存信息新增', 2099, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:info:add', '#', 'admin', '2022-07-25 12:29:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2102, '库存信息修改', 2099, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:info:edit', '#', 'admin', '2022-07-25 12:29:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2103, '库存信息删除', 2099, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:info:remove', '#', 'admin', '2022-07-25 12:29:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2104, '库存信息导出', 2099, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:info:export', '#', 'admin', '2022-07-25 12:29:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2105, '入库单管理', 2030, 2, 'inOrder', 'purchase/inOrder/index', NULL, 1, 0, 'C', '0', '0', 'stock:inOrder:list', 'tab', 'admin', '2022-07-25 12:29:13', 'admin', '2022-07-25 13:21:50', '入库单菜单');
INSERT INTO `sys_menu` VALUES (2106, '入库单查询', 2105, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:inOrder:query', '#', 'admin', '2022-07-25 12:29:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2107, '入库单新增', 2105, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:inOrder:add', '#', 'admin', '2022-07-25 12:29:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2108, '入库单修改', 2105, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:inOrder:edit', '#', 'admin', '2022-07-25 12:29:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2109, '入库单删除', 2105, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:inOrder:remove', '#', 'admin', '2022-07-25 12:29:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2110, '入库单导出', 2105, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:inOrder:export', '#', 'admin', '2022-07-25 12:29:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2111, '出库单管理', 2032, 2, 'outOrder', 'prod/outOrder/index', NULL, 1, 0, 'C', '0', '0', 'stock:outOrder:list', 'tab', 'admin', '2022-07-25 12:29:44', 'admin', '2022-07-25 13:22:13', '出库单菜单');
INSERT INTO `sys_menu` VALUES (2112, '出库单查询', 2111, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:outOrder:query', '#', 'admin', '2022-07-25 12:29:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2113, '出库单新增', 2111, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:outOrder:add', '#', 'admin', '2022-07-25 12:29:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2114, '出库单修改', 2111, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:outOrder:edit', '#', 'admin', '2022-07-25 12:29:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2115, '出库单删除', 2111, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:outOrder:remove', '#', 'admin', '2022-07-25 12:29:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2116, '出库单导出', 2111, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:outOrder:export', '#', 'admin', '2022-07-25 12:29:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2117, '生产订单管理', 2032, 1, 'prodOrder', 'prod/prodOrder/index', NULL, 1, 0, 'C', '0', '0', 'stock:prodOrder:list', 'edit', 'admin', '2022-07-25 12:30:00', 'admin', '2022-07-25 13:22:09', '生产订单菜单');
INSERT INTO `sys_menu` VALUES (2118, '生产订单查询', 2117, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:prodOrder:query', '#', 'admin', '2022-07-25 12:30:01', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2119, '生产订单新增', 2117, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:prodOrder:add', '#', 'admin', '2022-07-25 12:30:01', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2120, '生产订单修改', 2117, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:prodOrder:edit', '#', 'admin', '2022-07-25 12:30:01', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2121, '生产订单删除', 2117, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:prodOrder:remove', '#', 'admin', '2022-07-25 12:30:01', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2122, '生产订单导出', 2117, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:prodOrder:export', '#', 'admin', '2022-07-25 12:30:01', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2123, '库存汇总管理', 2029, 2, 'record', 'stock/record/index', NULL, 1, 0, 'C', '0', '0', 'stock:record:list', 'list', 'admin', '2022-07-25 12:30:10', 'admin', '2022-07-30 03:24:43', '库存流水菜单');
INSERT INTO `sys_menu` VALUES (2124, '库存流水查询', 2123, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:record:query', '#', 'admin', '2022-07-25 12:30:10', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2125, '库存流水新增', 2123, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:record:add', '#', 'admin', '2022-07-25 12:30:10', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2126, '库存流水修改', 2123, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:record:edit', '#', 'admin', '2022-07-25 12:30:10', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2127, '库存流水删除', 2123, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:record:remove', '#', 'admin', '2022-07-25 12:30:11', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2128, '库存流水导出', 2123, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:record:export', '#', 'admin', '2022-07-25 12:30:11', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2129, '入库退货管理', 2030, 3, 'inReturn', 'purchase/inReturn/index', NULL, 1, 0, 'C', '0', '0', 'stock:inReturn:list', 'component', 'admin', '2022-07-25 12:32:20', 'admin', '2022-07-25 13:21:55', '入库单退货菜单');
INSERT INTO `sys_menu` VALUES (2130, '入库单退货查询', 2129, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:inReturn:query', '#', 'admin', '2022-07-25 12:32:20', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2131, '入库单退货新增', 2129, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:inReturn:add', '#', 'admin', '2022-07-25 12:32:20', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2132, '入库单退货修改', 2129, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:inReturn:edit', '#', 'admin', '2022-07-25 12:32:20', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2133, '入库单退货删除', 2129, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:inReturn:remove', '#', 'admin', '2022-07-25 12:32:20', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2134, '入库单退货导出', 2129, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:inReturn:export', '#', 'admin', '2022-07-25 12:32:20', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2135, '出库退货管理', 2032, 3, 'outReturn', 'prod/outReturn/index', NULL, 1, 0, 'C', '0', '0', 'stock:outReturn:list', 'component', 'admin', '2022-07-25 12:34:19', 'admin', '2022-07-25 13:22:18', '出库单退货菜单');
INSERT INTO `sys_menu` VALUES (2136, '出库单退货查询', 2135, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:outReturn:query', '#', 'admin', '2022-07-25 12:34:19', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2137, '出库单退货新增', 2135, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:outReturn:add', '#', 'admin', '2022-07-25 12:34:19', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2138, '出库单退货修改', 2135, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:outReturn:edit', '#', 'admin', '2022-07-25 12:34:20', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2139, '出库单退货删除', 2135, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:outReturn:remove', '#', 'admin', '2022-07-25 12:34:20', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2140, '出库单退货导出', 2135, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:outReturn:export', '#', 'admin', '2022-07-25 12:34:20', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2141, '物料标签管理', 2030, 1, 'matLabel', 'purchase/matLabel/index', NULL, 1, 0, 'C', '0', '0', 'stock:matLabel:list', 'dict', 'admin', '2022-07-25 12:39:23', 'admin', '2022-07-25 13:21:44', '物料标签菜单');
INSERT INTO `sys_menu` VALUES (2142, '物料标签查询', 2141, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:matLabel:query', '#', 'admin', '2022-07-25 12:39:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2143, '物料标签新增', 2141, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:matLabel:add', '#', 'admin', '2022-07-25 12:39:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2144, '物料标签修改', 2141, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:matLabel:edit', '#', 'admin', '2022-07-25 12:39:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2145, '物料标签删除', 2141, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:matLabel:remove', '#', 'admin', '2022-07-25 12:39:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2146, '物料标签导出', 2141, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:matLabel:export', '#', 'admin', '2022-07-25 12:39:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2147, '车间管理', 2028, 8, 'workshop', 'base/workshop/index', NULL, 1, 0, 'C', '0', '0', 'base:workshop:list', 'radio', 'admin', '2022-07-28 04:23:07', 'admin', '2022-07-28 04:30:58', '车间菜单');
INSERT INTO `sys_menu` VALUES (2148, '车间查询', 2147, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:workshop:query', '#', 'admin', '2022-07-28 04:23:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2149, '车间新增', 2147, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:workshop:add', '#', 'admin', '2022-07-28 04:23:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2150, '车间修改', 2147, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:workshop:edit', '#', 'admin', '2022-07-28 04:23:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2151, '车间删除', 2147, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:workshop:remove', '#', 'admin', '2022-07-28 04:23:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2152, '车间导出', 2147, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:workshop:export', '#', 'admin', '2022-07-28 04:23:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2153, '出库单查询', 2082, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'stock:common:outOrder:query', '#', 'admin', '2022-07-29 07:31:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2154, '出库单新增', 2082, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'stock:common:outOrder:add', '#', 'admin', '2022-07-29 07:31:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2155, '出库单修改', 2082, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'stock:common:outOrder:edit', '#', 'admin', '2022-07-29 07:32:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2156, '出库单删除', 2082, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'stock:common:outOrder:remove', '#', 'admin', '2022-07-29 07:32:37', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2157, '出库单导出', 2082, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'stock:common:outOrder:export', '#', 'admin', '2022-07-29 07:32:58', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2158, '调拨管理', 0, 7, 'allocation', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'example', 'admin', '2022-08-05 09:08:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2160, '调拨单管理', 2158, 1, 'allotOrder', 'allocation/allot/index', NULL, 1, 0, 'C', '0', '0', 'stock:allotOrder:list', 'tab', 'admin', '2022-08-05 11:46:15', 'admin', '2022-08-05 11:48:02', '调拨单菜单');
INSERT INTO `sys_menu` VALUES (2161, '调拨单查询', 2160, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:allotOrder:query', '#', 'admin', '2022-08-05 11:46:16', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2162, '调拨单新增', 2160, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:allotOrder:add', '#', 'admin', '2022-08-05 11:46:16', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2163, '调拨单修改', 2160, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:allotOrder:edit', '#', 'admin', '2022-08-05 11:46:16', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2164, '调拨单删除', 2160, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:allotOrder:remove', '#', 'admin', '2022-08-05 11:46:16', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2165, '调拨单导出', 2160, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'stock:allotOrder:export', '#', 'admin', '2022-08-05 11:46:16', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(11) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(11) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int(11) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 677 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int(11) NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2022-07-10 01:24:55', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2022-07-10 01:24:55', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(11) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2022-07-10 01:24:55', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2022-07-10 01:24:55', 'admin', '2022-07-13 01:51:01', '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 117);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 201, 'admin', '管理员', '00', 'zt18519100654@163.com', '18519100654', '0', '/profile/avatar/2022/09/12/blob_20220912082155A001.jpeg', '$2a$10$76b1MOVniaux2ACn9XYM4u0C2dLgvJqjNWWR2TGQzLKBaPdak6m/K', '0', '0', '127.0.0.1', '2023-04-30 15:10:09', 'admin', '2022-07-10 01:24:55', '', '2023-04-30 15:10:09', '管理员');
INSERT INTO `sys_user` VALUES (100, 100, 'jtom', 'jtom', '00', '397343331@qq.com', '18519100653', '0', '', '$2a$10$VvEBI93MJ1XQyf3z9fGVj./Kxp1gLk4WJWCfqJtc8TzwGK3AcoKu.', '0', '0', '127.0.0.1', '2022-09-10 09:00:17', 'admin', '2022-08-11 01:20:51', 'admin', '2022-09-10 01:00:16', NULL);

-- ----------------------------
-- Table structure for sys_user_class
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_class`;
CREATE TABLE `sys_user_class`  (
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户账号',
  `class_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料分类编码'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户管理的物料分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_class
-- ----------------------------
INSERT INTO `sys_user_class` VALUES ('jtom', 'LS');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (100, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (100, 2);

SET FOREIGN_KEY_CHECKS = 1;
