create table stock_mat_label
(
    label_id      bigint auto_increment comment '主键'
        primary key,
    label_code    varchar(64)                   not null comment '标签编码',
    label_type    varchar(32)                   null comment '标签类型',
    mat_code      varchar(64)                   null comment '物料编码',
    batch         varchar(128)                  null comment '批次',
    supplier_code varchar(64)                   null comment '供应商编码',
    supplier_name varchar(128)                  null comment '供应商名称',
    prod_time     datetime                      null comment '生产时间',
    status        varchar(32) default 'created' null comment '状态(created/in_stored/in_transit)',
    remark        varchar(500)                  null comment '备注',
    del_flag      char        default '0'       null comment '删除标识',
    create_by     varchar(64)                   null comment '创建人',
    create_time   datetime                      null comment '创建时间',
    update_by     varchar(64)                   null comment '修改人',
    update_time   datetime                      null comment '修改时间',
    constraint uk_label_code
        unique (label_code)
)
    comment '物料标签' collate = utf8mb4_general_ci
                       row_format = DYNAMIC;

create index idx_batch
    on stock_mat_label (batch);

create index idx_mat_code
    on stock_mat_label (mat_code);

create index idx_status
    on stock_mat_label (status);

create index idx_supplier_code
    on stock_mat_label (supplier_code);

INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (101, 'LB20260401001', 'purchase', 'T-YL001', 'TB20260401-001', 'S-001', '青岛纯碱工业有限公司', null, '0', '工业纯碱A级-第1批', '0', 'admin', '2026-04-01 09:30:00', null, '2026-04-08 12:38:36');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (102, 'LB20260401002', 'purchase', 'T-YL001', 'TB20260401-002', 'S-001', '青岛纯碱工业有限公司', null, '0', '工业纯碱A级-第2批', '0', 'admin', '2026-04-01 10:00:00', null, '2026-04-08 12:38:37');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (103, 'LB20260401003', 'purchase', 'T-YL002', 'TB20260401-003', 'S-002', '凤阳石英砂矿业公司', null, '0', '优质石英砂A级-第1批', '0', 'admin', '2026-04-01 10:30:00', null, '2026-04-08 12:38:39');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (104, 'LB20260401004', 'purchase', 'T-YL002', 'TB20260401-004', 'S-002', '凤阳石英砂矿业公司', null, '0', '优质石英砂A级-第2批', '0', 'admin', '2026-04-01 11:00:00', null, '2026-04-08 12:38:40');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (105, 'LB20260401005', 'purchase', 'T-YL003', 'TB20260401-005', 'S-003', '邯郸石灰石矿业集团', null, '0', '石灰石粉A级', '0', 'admin', '2026-04-01 11:30:00', null, '2026-04-08 12:38:42');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (106, 'LB20260401006', 'purchase', 'T-YL004', 'TB20260401-006', 'S-003', '邯郸石灰石矿业集团', null, '0', '白云石A级', '0', 'admin', '2026-04-01 12:00:00', null, '2026-04-08 12:38:47');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (107, 'LB20260402001', 'purchase', 'T-FL001', 'TB20260402-001', 'S-004', '南京化工材料有限公司', null, '0', '澄清剂A型', '0', 'admin', '2026-04-02 09:00:00', null, '2026-04-08 12:38:48');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (108, 'LB20260402002', 'purchase', 'T-FL002', 'TB20260402-002', 'S-004', '南京化工材料有限公司', null, '0', '脱色剂A型', '0', 'admin', '2026-04-02 09:30:00', null, '2026-04-08 12:38:50');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (109, 'LB20260402003', 'purchase', 'T-FL003', 'TB20260402-003', 'S-004', '南京化工材料有限公司', null, '0', '着色剂A型', '0', 'admin', '2026-04-02 10:00:00', null, '2026-04-08 12:38:52');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (110, 'LB20260402004', 'purchase', 'T-FL004', 'TB20260402-004', 'S-005', '佛山精密模具配件厂', null, '0', '脱模剂A型', '0', 'admin', '2026-04-02 10:30:00', null, '2026-04-08 12:38:53');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (111, 'LB20260402005', 'purchase', 'T-FL005', 'TB20260402-005', 'S-005', '佛山精密模具配件厂', null, '0', '抛光粉A型', '0', 'admin', '2026-04-02 11:00:00', null, '2026-04-08 12:39:00');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (112, 'LB20260402006', 'purchase', 'T-BC001', 'TB20260402-006', 'S-006', '天津纸箱包装有限公司', null, '0', '标准纸箱B型', '0', 'admin', '2026-04-02 14:00:00', null, '2026-04-08 12:38:59');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (113, 'LB20260402007', 'purchase', 'T-BC002', 'TB20260402-007', 'S-006', '天津纸箱包装有限公司', null, '0', '大号纸箱B型', '0', 'admin', '2026-04-02 14:30:00', null, '2026-04-08 12:39:02');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (114, 'LB20260402008', 'purchase', 'T-BC003', 'TB20260402-008', 'S-007', '济南塑料包装有限公司', null, '0', '气泡膜B型', '0', 'admin', '2026-04-02 15:00:00', null, '2026-04-08 12:39:04');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (115, 'LB20260402009', 'purchase', 'T-BC004', 'TB20260402-009', 'S-007', '济南塑料包装有限公司', null, '0', '珍珠棉B型', '0', 'admin', '2026-04-02 15:30:00', null, '2026-04-08 12:39:05');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (116, 'LB20260403001', 'production', 'T-BJ001', 'TP20260401-001', null, null, '2026-04-01 16:00:00', '0', '玻璃原板A-第1批', '0', 'admin', '2026-04-03 08:00:00', null, '2026-04-08 12:39:17');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (117, 'LB20260403002', 'production', 'T-BJ001', 'TP20260401-002', null, null, '2026-04-01 18:00:00', '0', '玻璃原板A-第2批', '0', 'admin', '2026-04-03 08:30:00', null, '2026-04-08 12:39:18');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (118, 'LB20260403003', 'production', 'T-BJ002', 'TP20260402-001', null, null, '2026-04-02 16:00:00', '0', '打磨半成品A', '0', 'admin', '2026-04-03 09:00:00', null, '2026-04-08 12:39:19');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (119, 'LB20260403004', 'production', 'T-BJ003', 'TP20260402-002', null, null, '2026-04-02 18:00:00', '0', '切割半成品A', '0', 'admin', '2026-04-03 09:30:00', null, '2026-04-08 12:39:21');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (120, 'LB20260404001', 'production', 'T-CP001', 'TP20260403-001', null, null, '2026-04-03 16:00:00', '0', '浮法玻璃4mm', '0', 'admin', '2026-04-04 08:00:00', null, '2026-04-08 12:39:22');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (121, 'LB20260404002', 'production', 'T-CP002', 'TP20260403-002', null, null, '2026-04-03 18:00:00', '0', '浮法玻璃6mm', '0', 'admin', '2026-04-04 08:30:00', null, '2026-04-08 12:39:14');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (122, 'LB20260405001', 'production', 'T-CP003', 'TP20260404-001', null, null, '2026-04-04 16:00:00', '0', '钢化玻璃4mm', '0', 'admin', '2026-04-05 08:00:00', null, '2026-04-08 12:39:12');
INSERT INTO wms.stock_mat_label (label_id, label_code, label_type, mat_code, batch, supplier_code, supplier_name, prod_time, status, remark, del_flag, create_by, create_time, update_by, update_time) VALUES (123, 'LB20260405002', 'production', 'T-CP005', 'TP20260405-001', null, null, '2026-04-05 16:00:00', '0', '镀膜玻璃6mm', '0', 'admin', '2026-04-05 14:00:00', null, '2026-04-08 12:39:09');
