-- ============================================================
-- 工艺路线管理模块 SQL
-- 包含：base_process_route / base_process_step / base_process_param
-- 以及：stock_prod_order 新增 route_code 字段
-- 以及：sys_menu 菜单权限配置
-- ============================================================

-- ----------------------------
-- 1. 工艺路线表
-- ----------------------------
DROP TABLE IF EXISTS `base_process_route`;
CREATE TABLE `base_process_route` (
    `route_id`       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `route_code`     VARCHAR(30)  NOT NULL COMMENT '工艺路线编码',
    `route_name`     VARCHAR(100) NOT NULL COMMENT '工艺路线名称',
    `mat_code`       VARCHAR(30)  DEFAULT NULL COMMENT '关联产品物料编码',
    `route_version`  VARCHAR(10)  DEFAULT 'V1.0' COMMENT '版本号',
    `route_status`   CHAR(1)      DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `del_flag`       CHAR(1)      DEFAULT '0' COMMENT '删除标识（0存在 1删除）',
    `create_by`      VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    `create_time`    DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_by`      VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    `update_time`    DATETIME     DEFAULT NULL COMMENT '更新时间',
    `remark`         VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`route_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='工艺路线表';

-- ----------------------------
-- 2. 工序表
-- ----------------------------
DROP TABLE IF EXISTS `base_process_step`;
CREATE TABLE `base_process_step` (
    `step_id`        BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `route_code`     VARCHAR(30)  NOT NULL COMMENT '所属工艺路线编码',
    `step_no`        INT          NOT NULL COMMENT '工序序号',
    `step_code`      VARCHAR(30)  NOT NULL COMMENT '工序编码',
    `step_name`      VARCHAR(100) NOT NULL COMMENT '工序名称（熔制/成型/退火/切割）',
    `step_type`      VARCHAR(20)  DEFAULT NULL COMMENT '工序类型（melting熔制/forming成型/annealing退火/cutting切割/other其他）',
    `standard_hours` DECIMAL(10,2) DEFAULT NULL COMMENT '标准工时（小时）',
    `del_flag`       CHAR(1)      DEFAULT '0' COMMENT '删除标识（0存在 1删除）',
    `create_by`      VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    `create_time`    DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_by`      VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    `update_time`    DATETIME     DEFAULT NULL COMMENT '更新时间',
    `remark`         VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`step_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='工序表';

-- ----------------------------
-- 3. 工艺参数标准表
-- ----------------------------
DROP TABLE IF EXISTS `base_process_param`;
CREATE TABLE `base_process_param` (
    `param_id`       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `step_id`        BIGINT       NOT NULL COMMENT '所属工序ID',
    `param_name`     VARCHAR(100) NOT NULL COMMENT '参数名称（熔制温度/成型速度/退火曲线温度）',
    `param_unit`     VARCHAR(20)  DEFAULT NULL COMMENT '单位（℃/m·min⁻¹/mm）',
    `standard_value` VARCHAR(50)  DEFAULT NULL COMMENT '标准值',
    `min_value`      DECIMAL(12,4) DEFAULT NULL COMMENT '下限值',
    `max_value`      DECIMAL(12,4) DEFAULT NULL COMMENT '上限值',
    `is_key`         CHAR(1)      DEFAULT '0' COMMENT '是否关键参数（0否 1是）',
    `del_flag`       CHAR(1)      DEFAULT '0' COMMENT '删除标识（0存在 1删除）',
    `create_by`      VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    `create_time`    DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_by`      VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    `update_time`    DATETIME     DEFAULT NULL COMMENT '更新时间',
    `remark`         VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`param_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='工艺参数标准表';

-- ----------------------------
-- 4. 生产工单新增工艺路线编码字段
-- ----------------------------
ALTER TABLE `stock_prod_order` ADD COLUMN `route_code` VARCHAR(30) DEFAULT NULL COMMENT '工艺路线编码' AFTER `workshop_code`;

-- ----------------------------
-- 5. 菜单权限配置
-- ----------------------------
-- 工艺路线管理菜单（挂在"数据管理"目录下，parent_id=2028）
INSERT INTO `sys_menu` VALUES (2200, '工艺路线管理', 2028, 9, 'processRoute', 'base/processRoute/index', NULL, 1, 0, 'C', '0', '0', 'base:processRoute:list', 'guide', 'admin', sysdate(), '', NULL, '工艺路线管理菜单');
-- 按钮权限
INSERT INTO `sys_menu` VALUES (2201, '工艺路线查询', 2200, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:processRoute:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2202, '工艺路线新增', 2200, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:processRoute:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2203, '工艺路线修改', 2200, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:processRoute:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2204, '工艺路线删除', 2200, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:processRoute:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2205, '工艺路线导出', 2200, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'base:processRoute:export', '#', 'admin', sysdate(), '', NULL, '');

-- 给计划员(role_id=5)分配工艺路线权限
INSERT INTO `sys_role_menu` VALUES (5, 2200);
INSERT INTO `sys_role_menu` VALUES (5, 2201);
INSERT INTO `sys_role_menu` VALUES (5, 2202);
INSERT INTO `sys_role_menu` VALUES (5, 2203);
INSERT INTO `sys_role_menu` VALUES (5, 2204);
INSERT INTO `sys_role_menu` VALUES (5, 2205);

-- ----------------------------
-- 6. 初始化测试数据（玻璃生产典型工艺路线）
-- ----------------------------
-- 工艺路线：浮法玻璃标准工艺
INSERT INTO `base_process_route` (`route_id`, `route_code`, `route_name`, `mat_code`, `route_version`, `route_status`, `create_by`, `create_time`, `remark`)
VALUES (1, 'PR-001', '浮法玻璃标准工艺', NULL, 'V1.0', '0', 'admin', sysdate(), '浮法玻璃生产标准工艺路线，包含熔制、成型、退火、切割四道工序');

-- 工序：熔制 → 成型 → 退火 → 切割
INSERT INTO `base_process_step` (`step_id`, `route_code`, `step_no`, `step_code`, `step_name`, `step_type`, `standard_hours`, `create_by`, `create_time`, `remark`)
VALUES
(1, 'PR-001', 1, 'ST-001', '配料', 'other', 1.00, 'admin', sysdate(), '按配方比例称量石英砂、纯碱、石灰石等原料'),
(2, 'PR-001', 2, 'ST-002', '熔制', 'melting', 24.00, 'admin', sysdate(), '在窑炉中将配合料加热至1500-1600℃熔化成玻璃液'),
(3, 'PR-001', 3, 'ST-003', '成型', 'forming', 4.00, 'admin', sysdate(), '玻璃液流入锡槽，在锡液面上浮法成型为平板玻璃'),
(4, 'PR-001', 4, 'ST-004', '退火', 'annealing', 8.00, 'admin', sysdate(), '玻璃带通过退火窑缓慢冷却，消除内应力'),
(5, 'PR-001', 5, 'ST-005', '切割检验', 'cutting', 2.00, 'admin', sysdate(), '按规格尺寸切割成片，进行外观和尺寸检验');

-- 工艺参数
INSERT INTO `base_process_param` (`param_id`, `step_id`, `param_name`, `param_unit`, `standard_value`, `min_value`, `max_value`, `is_key`, `create_by`, `create_time`, `remark`)
VALUES
-- 熔制工序参数
(1, 2, '熔制温度', '℃', '1550', 1500.0000, 1600.0000, '1', 'admin', sysdate(), '窑炉熔化区温度'),
(2, 2, '熔制时间', 'h', '24', 20.0000, 28.0000, '0', 'admin', sysdate(), '从投料到出料的时间'),
(3, 2, '玻璃液均化温度', '℃', '1200', 1150.0000, 1250.0000, '1', 'admin', sysdate(), '均化区温度'),
-- 成型工序参数
(4, 3, '锡槽温度', '℃', '1050', 1000.0000, 1100.0000, '1', 'admin', sysdate(), '锡槽入口温度'),
(5, 3, '拉引速度', 'm/min', '15', 10.0000, 25.0000, '1', 'admin', sysdate(), '玻璃带拉引速度，影响厚度'),
(6, 3, '玻璃带宽度', 'mm', '3300', 3000.0000, 3600.0000, '0', 'admin', sysdate(), '成型后的玻璃带宽度'),
-- 退火工序参数
(7, 4, '退火起始温度', '℃', '600', 580.0000, 620.0000, '1', 'admin', sysdate(), '退火窑入口温度'),
(8, 4, '退火终止温度', '℃', '60', 40.0000, 80.0000, '0', 'admin', sysdate(), '退火窑出口温度'),
(9, 4, '退火时间', 'h', '8', 6.0000, 10.0000, '0', 'admin', sysdate(), '退火窑通过时间'),
-- 切割工序参数
(10, 5, '切割精度', 'mm', '±1.0', -1.0000, 1.0000, '1', 'admin', sysdate(), '切割尺寸允许偏差');

-- 工艺路线：钢化玻璃工艺
INSERT INTO `base_process_route` (`route_id`, `route_code`, `route_name`, `mat_code`, `route_version`, `route_status`, `create_by`, `create_time`, `remark`)
VALUES (2, 'PR-002', '钢化玻璃加工工艺', NULL, 'V1.0', '0', 'admin', sysdate(), '在浮法玻璃基础上增加钢化处理');

INSERT INTO `base_process_step` (`step_id`, `route_code`, `step_no`, `step_code`, `step_name`, `step_type`, `standard_hours`, `create_by`, `create_time`, `remark`)
VALUES
(6, 'PR-002', 1, 'ST-006', '切割', 'cutting', 1.50, 'admin', sysdate(), '将原片玻璃按需求尺寸切割'),
(7, 'PR-002', 2, 'ST-007', '磨边', 'other', 1.00, 'admin', sysdate(), '对切割后的玻璃边缘进行磨削处理'),
(8, 'PR-002', 3, 'ST-008', '清洗', 'other', 0.50, 'admin', sysdate(), '清洗玻璃表面杂质和油污'),
(9, 'PR-002', 4, 'ST-009', '钢化加热', 'melting', 2.00, 'admin', sysdate(), '将玻璃加热至接近软化点'),
(10, 'PR-002', 5, 'ST-010', '急冷', 'annealing', 0.50, 'admin', sysdate(), '用冷风快速冷却，形成表面压应力');

INSERT INTO `base_process_param` (`param_id`, `step_id`, `param_name`, `param_unit`, `standard_value`, `min_value`, `max_value`, `is_key`, `create_by`, `create_time`, `remark`)
VALUES
(11, 9, '钢化加热温度', '℃', '680', 650.0000, 700.0000, '1', 'admin', sysdate(), '钢化炉加热温度'),
(12, 9, '加热时间', 's/mm', '40', 35.0000, 50.0000, '1', 'admin', sysdate(), '每毫米厚度的加热时间'),
(13, 10, '急冷风压', 'kPa', '15', 10.0000, 20.0000, '1', 'admin', sysdate(), '冷却段风压'),
(14, 10, '碎片数', '片/50×50mm', '45', 40.0000, NULL, '1', 'admin', sysdate(), '钢化后碎片数量，≥40片为合格');

