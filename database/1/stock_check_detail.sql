create table stock_check_detail
(
    detail_id      bigint auto_increment comment '明细ID'
        primary key,
    check_no       varchar(30)              not null comment '盘点单号',
    mat_code       varchar(30)              not null comment '物料编码',
    mat_name       varchar(100)             null comment '物料名称',
    warehouse_code varchar(30)              null comment '仓库编码',
    location_code  varchar(30)              null comment '货位编码',
    batch          varchar(30)              null comment '批次',
    unit_code      varchar(20)              null comment '单位',
    system_qty     decimal(12, 4)           null comment '系统数量（快照）',
    actual_qty     decimal(12, 4)           null comment '实盘数量',
    diff_qty       decimal(12, 4)           null comment '差异数量（实盘-系统）',
    diff_reason    varchar(200)             null comment '差异原因',
    adjust_flag    char         default '0' null comment '是否已调整（0否 1是）',
    del_flag       char         default '0' null comment '删除标志',
    create_by      varchar(64)  default ''  null comment '创建者',
    create_time    datetime                 null comment '创建时间',
    update_by      varchar(64)  default ''  null comment '更新者',
    update_time    datetime                 null comment '更新时间',
    remark         varchar(500) default ''  null comment '备注'
)
    comment '盘点明细表' collate = utf8mb4_general_ci;

INSERT INTO wms.stock_check_detail (detail_id, check_no, mat_code, mat_name, warehouse_code, location_code, batch, unit_code, system_qty, actual_qty, diff_qty, diff_reason, adjust_flag, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'SC20260412031634', 'T-CP001', '浮法玻璃4mm', 'WH-A02', 'CA-01', 'TP20260403-001', 'SQUARE_METER', 500.0000, 0.0000, -500.0000, null, '0', '0', 'admin', '2026-04-12 03:16:12', 'admin', '2026-04-12 03:16:27', '');
INSERT INTO wms.stock_check_detail (detail_id, check_no, mat_code, mat_name, warehouse_code, location_code, batch, unit_code, system_qty, actual_qty, diff_qty, diff_reason, adjust_flag, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, 'SC20260412031634', 'T-CP002', '浮法玻璃6mm', 'WH-A02', 'CA-02', 'TP20260403-002', 'SQUARE_METER', 2500.0000, 0.0000, -2500.0000, null, '0', '0', 'admin', '2026-04-12 03:16:12', 'admin', '2026-04-12 03:16:27', '');
INSERT INTO wms.stock_check_detail (detail_id, check_no, mat_code, mat_name, warehouse_code, location_code, batch, unit_code, system_qty, actual_qty, diff_qty, diff_reason, adjust_flag, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (3, 'SC20260412031634', 'T-CP003', '钢化玻璃4mm', 'WH-A02', 'CA-01', 'TP20260404-001', 'SQUARE_METER', 3000.0000, 0.0000, -3000.0000, null, '0', '0', 'admin', '2026-04-12 03:16:12', 'admin', '2026-04-12 03:16:27', '');
INSERT INTO wms.stock_check_detail (detail_id, check_no, mat_code, mat_name, warehouse_code, location_code, batch, unit_code, system_qty, actual_qty, diff_qty, diff_reason, adjust_flag, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (4, 'SC20260412031634', 'T-CP005', '镀膜玻璃6mm', 'WH-A02', 'CA-02', 'TP20260405-001', 'SQUARE_METER', 1000.0000, 0.0000, -1000.0000, null, '0', '0', 'admin', '2026-04-12 03:16:12', 'admin', '2026-04-12 03:16:27', '');
INSERT INTO wms.stock_check_detail (detail_id, check_no, mat_code, mat_name, warehouse_code, location_code, batch, unit_code, system_qty, actual_qty, diff_qty, diff_reason, adjust_flag, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (5, 'SC20260412031634', 'T-YL001', '工业纯碱A级', 'WH-A02', '2', 'TB20260401-001', 'KG', 400.0000, 0.0000, -400.0000, null, '0', '0', 'admin', '2026-04-12 03:16:12', 'admin', '2026-04-12 03:16:27', '');
INSERT INTO wms.stock_check_detail (detail_id, check_no, mat_code, mat_name, warehouse_code, location_code, batch, unit_code, system_qty, actual_qty, diff_qty, diff_reason, adjust_flag, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (6, 'SC20260412031634', 'T-CP002', '浮法玻璃6mm', 'WH-A02', '2', null, null, 10.0000, 0.0000, -10.0000, null, '0', '0', 'admin', '2026-04-12 03:16:12', 'admin', '2026-04-12 03:16:27', '');
INSERT INTO wms.stock_check_detail (detail_id, check_no, mat_code, mat_name, warehouse_code, location_code, batch, unit_code, system_qty, actual_qty, diff_qty, diff_reason, adjust_flag, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (7, 'SC20260412031634', 'T-CP001', '浮法玻璃4mm', 'WH-A02', '2', null, null, 20000.0000, 1.0000, -19999.0000, null, '0', '0', 'admin', '2026-04-12 03:16:12', 'admin', '2026-04-12 03:16:27', '');
INSERT INTO wms.stock_check_detail (detail_id, check_no, mat_code, mat_name, warehouse_code, location_code, batch, unit_code, system_qty, actual_qty, diff_qty, diff_reason, adjust_flag, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (8, 'SC20260412031634', 'T-CP001', '浮法玻璃4mm', 'WH-A02', 'CA-01,2', null, 'PCS', 2000.0000, 0.0000, -2000.0000, null, '0', '0', 'admin', '2026-04-12 03:16:12', 'admin', '2026-04-12 03:16:27', '');
