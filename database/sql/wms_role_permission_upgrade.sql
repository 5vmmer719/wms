-- =============================================
-- WMS 权限体系优化升级脚本
-- 定义四种角色：管理员/库管员/质检员/计划员
-- 执行前提：基于 wms.sql 初始化后的数据库
-- 执行方式：可在已有数据库上直接执行（幂等设计）
-- 注意：如果是全新部署，直接使用 wms.sql 即可，无需执行此脚本
--       此脚本仅用于已有数据库的增量升级
-- =============================================

-- =============================================
-- 第一部分：停用不需要的菜单（status='1' 表示停用）
-- =============================================
-- 系统工具（整个模块）
UPDATE `sys_menu` SET `status` = '1' WHERE `menu_id` IN (3, 115, 116, 117, 1054, 1055, 1056, 1057, 1058, 1059);
-- 系统监控下的：数据监控、服务监控、缓存监控、缓存列表、定时任务
UPDATE `sys_menu` SET `status` = '1' WHERE `menu_id` IN (110, 111, 112, 113, 114, 1048, 1049, 1050, 1051, 1052, 1053);
-- 系统管理下的：角色管理、菜单管理、部门管理、岗位管理、参数设置
UPDATE `sys_menu` SET `status` = '1' WHERE `menu_id` IN (101, 102, 103, 104, 106, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1030, 1031, 1032, 1033, 1034);

-- =============================================
-- 第二部分：新增质检/通用退货按钮级菜单权限（补全缺失）
-- =============================================
INSERT IGNORE INTO `sys_menu` VALUES (2170, '质检单查询', 2077, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'check:inOrder:query', '#', 'admin', '2022-07-23 09:33:44', '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (2171, '质检单审核', 2077, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'check:inOrder:check', '#', 'admin', '2022-07-23 09:33:44', '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (2172, '质检单导出', 2077, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'check:inOrder:export', '#', 'admin', '2022-07-23 09:33:44', '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (2173, '出库退货查询', 2083, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'stock:common:outReturn:query', '#', 'admin', '2022-07-29 07:31:06', '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (2174, '出库退货新增', 2083, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'stock:common:outReturn:add', '#', 'admin', '2022-07-29 07:31:06', '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (2175, '出库退货修改', 2083, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'stock:common:outReturn:edit', '#', 'admin', '2022-07-29 07:31:06', '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (2176, '出库退货删除', 2083, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'stock:common:outReturn:remove', '#', 'admin', '2022-07-29 07:31:06', '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (2177, '出库退货导出', 2083, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'stock:common:outReturn:export', '#', 'admin', '2022-07-29 07:31:06', '', NULL, '');

-- =============================================
-- 第三部分：更新岗位表（替换为WMS业务岗位）
-- =============================================
UPDATE `sys_post` SET `post_code` = 'admin', `post_name` = '系统管理员', `post_sort` = 1 WHERE `post_id` = 1;
UPDATE `sys_post` SET `post_code` = 'stockkeeper', `post_name` = '库管员', `post_sort` = 2 WHERE `post_id` = 2;
UPDATE `sys_post` SET `post_code` = 'inspector', `post_name` = '质检员', `post_sort` = 3 WHERE `post_id` = 3;
UPDATE `sys_post` SET `post_code` = 'planner', `post_name` = '计划员', `post_sort` = 4 WHERE `post_id` = 4;

-- =============================================
-- 第四部分：新增三种业务角色
-- =============================================
INSERT IGNORE INTO `sys_role` VALUES (3, '库管员', 'stockkeeper', 3, '1', 1, 1, '0', '0', 'admin', '2022-07-10 01:24:55', '', NULL, '负责仓库收发货、库存管理、调拨管理');
INSERT IGNORE INTO `sys_role` VALUES (4, '质检员', 'inspector', 4, '1', 1, 1, '0', '0', 'admin', '2022-07-10 01:24:55', '', NULL, '负责入库物料质量检验');
INSERT IGNORE INTO `sys_role` VALUES (5, '计划员', 'planner', 5, '1', 1, 1, '0', '0', 'admin', '2022-07-10 01:24:55', '', NULL, '负责生产计划、物料数据维护');

-- =============================================
-- 第五部分：清理旧的 role_id=2 关联中停用菜单的部分
-- =============================================
DELETE FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` IN (2, 3, 101, 102, 103, 104, 106, 109, 110, 111, 112, 113, 114, 115, 116, 117, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1030, 1031, 1032, 1033, 1034, 1045, 1046, 1047, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059);

-- =============================================
-- 第六部分：写入角色-菜单关联（先清理再插入，保证幂等）
-- =============================================
DELETE FROM `sys_role_menu` WHERE `role_id` IN (3, 4, 5);

-- 库管员(role_id=3) 权限：数据管理(只读)+采购(全部)+库存(全部)+调拨(全部)+通用(全部)+统计(只读)
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(3,2028),(3,2060),(3,2061),(3,2036),(3,2037),(3,2048),(3,2049),(3,2042),(3,2043),(3,2066),(3,2067),
(3,2093),(3,2094),(3,2095),(3,2096),(3,2097),(3,2098),
(3,2054),(3,2055),(3,2056),(3,2057),(3,2058),(3,2059),
(3,2147),(3,2148),
(3,2030),(3,2141),(3,2142),(3,2143),(3,2144),(3,2145),(3,2146),
(3,2105),(3,2106),(3,2107),(3,2108),(3,2109),(3,2110),
(3,2129),(3,2130),(3,2131),(3,2132),(3,2133),(3,2134),
(3,2029),(3,2099),(3,2100),(3,2101),(3,2102),(3,2103),(3,2104),
(3,2123),(3,2124),(3,2125),(3,2126),(3,2127),(3,2128),
(3,2158),(3,2160),(3,2161),(3,2162),(3,2163),(3,2164),(3,2165),
(3,2033),(3,2082),(3,2153),(3,2154),(3,2155),(3,2156),(3,2157),
(3,2083),(3,2173),(3,2174),(3,2175),(3,2176),(3,2177),
(3,2034),(3,2084),(3,2085),(3,2086);

-- 质检员(role_id=4) 权限：数据管理(只读)+质检(全部)+采购-入库单(只读)+统计(只读)
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(4,2028),(4,2060),(4,2061),(4,2036),(4,2037),(4,2048),(4,2049),(4,2042),(4,2043),(4,2066),(4,2067),
(4,2093),(4,2094),(4,2054),(4,2055),(4,2147),(4,2148),
(4,2031),(4,2077),(4,2170),(4,2171),(4,2172),
(4,2030),(4,2141),(4,2142),(4,2105),(4,2106),
(4,2034),(4,2084),(4,2085),(4,2086);

-- 计划员(role_id=5) 权限：数据管理(全部CRUD)+生产(全部)+采购(只读)+库存(只读)+统计(全部)
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(5,2028),(5,2060),(5,2061),(5,2062),(5,2063),(5,2064),(5,2065),
(5,2036),(5,2037),(5,2038),(5,2039),(5,2040),(5,2041),
(5,2048),(5,2049),(5,2050),(5,2051),(5,2052),(5,2053),
(5,2042),(5,2043),(5,2044),(5,2045),(5,2046),(5,2047),
(5,2066),(5,2067),(5,2068),(5,2069),(5,2070),(5,2071),
(5,2093),(5,2094),(5,2054),(5,2055),
(5,2147),(5,2148),(5,2149),(5,2150),(5,2151),(5,2152),
(5,2032),(5,2117),(5,2118),(5,2119),(5,2120),(5,2121),(5,2122),
(5,2111),(5,2112),(5,2113),(5,2114),(5,2115),(5,2116),
(5,2135),(5,2136),(5,2137),(5,2138),(5,2139),(5,2140),
(5,2030),(5,2141),(5,2142),(5,2105),(5,2106),(5,2129),(5,2130),
(5,2029),(5,2099),(5,2100),(5,2123),(5,2124),
(5,2034),(5,2084),(5,2085),(5,2086);

-- =============================================
-- 第七部分：新增示例用户（密码均为 admin123 的BCrypt加密）
-- =============================================
INSERT IGNORE INTO `sys_user` VALUES (101, 100, 'stockkeeper', '库管员张三', '00', '', '', '0', '', '$2a$10$76b1MOVniaux2ACn9XYM4u0C2dLgvJqjNWWR2TGQzLKBaPdak6m/K', '0', '0', '', NULL, 'admin', '2022-07-10 01:24:55', '', NULL, '库管员示例账号');
INSERT IGNORE INTO `sys_user` VALUES (102, 100, 'inspector', '质检员李四', '00', '', '', '0', '', '$2a$10$76b1MOVniaux2ACn9XYM4u0C2dLgvJqjNWWR2TGQzLKBaPdak6m/K', '0', '0', '', NULL, 'admin', '2022-07-10 01:24:55', '', NULL, '质检员示例账号');
INSERT IGNORE INTO `sys_user` VALUES (103, 100, 'planner', '计划员王五', '00', '', '', '0', '', '$2a$10$76b1MOVniaux2ACn9XYM4u0C2dLgvJqjNWWR2TGQzLKBaPdak6m/K', '0', '0', '', NULL, 'admin', '2022-07-10 01:24:55', '', NULL, '计划员示例账号');

-- 用户-角色关联
INSERT IGNORE INTO `sys_user_role` VALUES (101, 3);
INSERT IGNORE INTO `sys_user_role` VALUES (102, 4);
INSERT IGNORE INTO `sys_user_role` VALUES (103, 5);

-- 用户-岗位关联
INSERT IGNORE INTO `sys_user_post` VALUES (101, 2);
INSERT IGNORE INTO `sys_user_post` VALUES (102, 3);
INSERT IGNORE INTO `sys_user_post` VALUES (103, 4);

-- 用户-物料分类关联
INSERT IGNORE INTO `sys_user_class` VALUES ('stockkeeper', 'LS');
INSERT IGNORE INTO `sys_user_class` VALUES ('stockkeeper', 'TH');
INSERT IGNORE INTO `sys_user_class` VALUES ('stockkeeper', 'ZC');
INSERT IGNORE INTO `sys_user_class` VALUES ('inspector', 'LS');
INSERT IGNORE INTO `sys_user_class` VALUES ('inspector', 'TH');
INSERT IGNORE INTO `sys_user_class` VALUES ('inspector', 'ZC');
INSERT IGNORE INTO `sys_user_class` VALUES ('planner', 'LS');
INSERT IGNORE INTO `sys_user_class` VALUES ('planner', 'TH');
INSERT IGNORE INTO `sys_user_class` VALUES ('planner', 'ZC');

-- 角色-部门数据权限关联
INSERT IGNORE INTO `sys_role_dept` VALUES (3, 100);
INSERT IGNORE INTO `sys_role_dept` VALUES (4, 100);
INSERT IGNORE INTO `sys_role_dept` VALUES (5, 100);

