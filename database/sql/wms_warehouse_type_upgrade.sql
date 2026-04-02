-- ============================================================
-- 仓库类型系统升级SQL
-- 功能：为仓库添加类型字段，物料组关联默认仓库类型
-- ============================================================

SET NAMES utf8mb4;

-- ============================================================
-- 1. 添加仓库类型字段
-- ============================================================

-- 为仓库表添加类型字段
ALTER TABLE `base_warehouse` ADD COLUMN `warehouse_type` varchar(32) DEFAULT NULL COMMENT '仓库类型' AFTER `warehouse_name`;

-- 为物料组表添加默认仓库类型字段
ALTER TABLE `base_mat_group` ADD COLUMN `default_warehouse_type` varchar(32) DEFAULT NULL COMMENT '默认仓库类型' AFTER `group_name`;

-- ============================================================
-- 2. 添加字典类型和字典数据
-- ============================================================

-- 添加仓库类型字典类型
INSERT INTO `sys_dict_type` (`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES ('仓库类型', 'warehouse_type', '0', 'admin', NOW(), '仓库类型列表');

-- 添加仓库类型字典数据
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`) VALUES
(1, '原材料仓库', 'raw_material', 'warehouse_type', '', 'primary', 'N', '0', 'admin', NOW(), '存放原材料的仓库'),
(2, '成品仓库', 'finished_product', 'warehouse_type', '', 'success', 'N', '0', 'admin', NOW(), '存放成品的仓库'),
(3, '半成品仓库', 'semi_finished', 'warehouse_type', '', 'info', 'N', '0', 'admin', NOW(), '存放半成品的仓库'),
(4, '辅料仓库', 'auxiliary', 'warehouse_type', '', 'warning', 'N', '0', 'admin', NOW(), '存放辅助材料的仓库'),
(5, '包材仓库', 'packaging', 'warehouse_type', '', 'warning', 'N', '0', 'admin', NOW(), '存放包装材料的仓库'),
(6, '危化品仓库', 'dangerous', 'warehouse_type', '', 'danger', 'N', '0', 'admin', NOW(), '存放危险化学品的仓库');

-- ============================================================
-- 3. 更新物料组的默认仓库类型
-- ============================================================

UPDATE `base_mat_group` SET `default_warehouse_type` = 'finished_product' WHERE `group_code` = 'CP';  -- 成品类
UPDATE `base_mat_group` SET `default_warehouse_type` = 'semi_finished' WHERE `group_code` = 'BJ';     -- 半成品类
UPDATE `base_mat_group` SET `default_warehouse_type` = 'raw_material' WHERE `group_code` = 'YL';      -- 原材料类
UPDATE `base_mat_group` SET `default_warehouse_type` = 'auxiliary' WHERE `group_code` = 'FL';         -- 辅料类
UPDATE `base_mat_group` SET `default_warehouse_type` = 'packaging' WHERE `group_code` = 'BC';         -- 包材类
UPDATE `base_mat_group` SET `default_warehouse_type` = 'dangerous' WHERE `group_code` = 'HG';         -- 化工类

-- ============================================================
-- 4. 更新测试数据中的仓库类型
-- ============================================================

UPDATE `base_warehouse` SET `warehouse_type` = 'raw_material' WHERE `warehouse_code` = 'WH001';
UPDATE `base_warehouse` SET `warehouse_type` = 'finished_product' WHERE `warehouse_code` = 'WH002';
UPDATE `base_warehouse` SET `warehouse_type` = 'semi_finished' WHERE `warehouse_code` = 'WH003';
UPDATE `base_warehouse` SET `warehouse_type` = 'auxiliary' WHERE `warehouse_code` = 'WH004';
UPDATE `base_warehouse` SET `warehouse_type` = 'packaging' WHERE `warehouse_code` = 'WH005';
UPDATE `base_warehouse` SET `warehouse_type` = 'dangerous' WHERE `warehouse_code` = 'WH007';

-- ============================================================
-- 升级完成
-- ============================================================