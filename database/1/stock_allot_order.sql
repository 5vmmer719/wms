create table stock_allot_order
(
    allot_id            bigint auto_increment comment '主键'
        primary key,
    allot_no            varchar(64)      null comment '调拨单号',
    allot_reason        varchar(255)     null comment '调拨原因',
    src_warehouse_code  varchar(64)      null comment '发起仓库',
    dest_warehouse_code varchar(64)      null comment '目标仓库',
    allot_status        varchar(32)      null comment '调拨单状态',
    allot_progress      varchar(64)      null comment '调拨进度',
    del_flag            char default '0' null comment '删除标识',
    create_by           varchar(64)      null comment '创建人',
    create_time         datetime         null comment '创建时间',
    update_by           varchar(64)      null comment '修改人',
    update_time         datetime         null comment '修改时间',
    remark              varchar(500)     null comment '备注'
)
    comment '调拨单;' collate = utf8mb4_general_ci
                      row_format = DYNAMIC;

INSERT INTO wms.stock_allot_order (allot_id, allot_no, allot_reason, src_warehouse_code, dest_warehouse_code, allot_status, allot_progress, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (101, 'TDB20260406001', '生产备料-纯碱调拨至半成品仓暂存', 'WH-A01', 'WH-A03', 'printed', 'receive', '1', 'admin', '2026-04-06 08:30:00', null, null, null);
INSERT INTO wms.stock_allot_order (allot_id, allot_no, allot_reason, src_warehouse_code, dest_warehouse_code, allot_status, allot_progress, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (102, 'TDB20260406002', '辅料调拨至原材料仓统一管理', 'WH-A04', 'WH-A01', 'printed', 'receive', '1', 'admin', '2026-04-06 09:30:00', null, null, null);
INSERT INTO wms.stock_allot_order (allot_id, allot_no, allot_reason, src_warehouse_code, dest_warehouse_code, allot_status, allot_progress, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (103, 'TDB20260407001', '石英砂备料调拨', 'WH-A01', 'WH-A03', 'created', null, '1', 'admin', '2026-04-07 08:00:00', null, null, null);
INSERT INTO wms.stock_allot_order (allot_id, allot_no, allot_reason, src_warehouse_code, dest_warehouse_code, allot_status, allot_progress, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (104, 'A20260408134088', '1', 'WH-A01', 'WH-A02', 'created', 'confirmed', '1', 'admin', '2026-04-08 13:40:49', 'admin', '2026-04-08 13:41:02', null);
INSERT INTO wms.stock_allot_order (allot_id, allot_no, allot_reason, src_warehouse_code, dest_warehouse_code, allot_status, allot_progress, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (105, 'A20260408135475', '2', 'WH-A01', 'WH-A02', 'created', 'confirmed', '1', 'admin', '2026-04-08 13:54:08', 'admin', '2026-04-08 13:54:20', '已生成调拨出库单：OA20260408135481');
INSERT INTO wms.stock_allot_order (allot_id, allot_no, allot_reason, src_warehouse_code, dest_warehouse_code, allot_status, allot_progress, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (106, 'A20260408135996', '1', 'WH-A01', 'WH-A02', 'created', 'confirmed', '1', 'admin', '2026-04-08 13:59:49', 'admin', '2026-04-08 13:59:58', '已生成调拨出库单：OA20260408135995');
INSERT INTO wms.stock_allot_order (allot_id, allot_no, allot_reason, src_warehouse_code, dest_warehouse_code, allot_status, allot_progress, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (107, 'A20260408140586', '1', 'WH-A01', 'WH-A01', 'created', 'confirmed', '1', 'admin', '2026-04-08 14:05:56', 'admin', '2026-04-08 14:06:02', '已生成调拨出库单：OA20260408140662');
INSERT INTO wms.stock_allot_order (allot_id, allot_no, allot_reason, src_warehouse_code, dest_warehouse_code, allot_status, allot_progress, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (108, 'A20260408142784', '1', 'WH-A01', 'WH-A02', 'confirmed', 'confirmed', '1', 'admin', '2026-04-08 14:27:25', 'admin', '2026-04-08 14:27:28', '已生成调拨出库单：OA20260408142744');
INSERT INTO wms.stock_allot_order (allot_id, allot_no, allot_reason, src_warehouse_code, dest_warehouse_code, allot_status, allot_progress, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (109, 'A20260408143597', '1', 'WH-A01', 'WH-A02', 'confirmed', 'confirmed', '1', 'admin', '2026-04-08 14:35:50', 'admin', '2026-04-08 14:35:53', '已生成调拨出库单：OA20260408143539');
INSERT INTO wms.stock_allot_order (allot_id, allot_no, allot_reason, src_warehouse_code, dest_warehouse_code, allot_status, allot_progress, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (110, 'A20260408144570', '1', 'WH-A01', 'WH-A06', 'confirmed', 'completed', '1', 'admin', '2026-04-08 14:45:21', 'admin', '2026-04-08 14:45:53', '已生成调拨出库单：OA20260408144511');
INSERT INTO wms.stock_allot_order (allot_id, allot_no, allot_reason, src_warehouse_code, dest_warehouse_code, allot_status, allot_progress, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (111, 'A20260409182071', 'test', 'WH-A01', 'WH-A04', 'confirmed', 'out_completed', '1', 'admin', '2026-04-09 18:20:13', 'admin', '2026-04-09 18:20:54', '已生成调拨出库单：OA20260409182082');
INSERT INTO wms.stock_allot_order (allot_id, allot_no, allot_reason, src_warehouse_code, dest_warehouse_code, allot_status, allot_progress, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (112, 'A20260409182618', 'test', 'WH-A01', 'WH-A04', 'confirmed', 'completed', '0', 'admin', '2026-04-09 18:26:21', 'admin', '2026-04-09 18:26:51', '已生成调拨出库单：OA20260409182670');
