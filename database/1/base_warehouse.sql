create table base_warehouse
(
    warehouse_id   bigint auto_increment comment '主键'
        primary key,
    warehouse_code varchar(64)      null comment '仓库编码',
    warehouse_name varchar(128)     null comment '仓库名称',
    warehouse_type varchar(32)      null comment '仓库类型',
    del_flag       char default '0' null comment '删除标识',
    create_by      varchar(64)      null comment '创建人',
    create_time    datetime         null comment '创建时间',
    update_by      varchar(64)      null comment '修改人',
    update_time    datetime         null comment '修改时间'
)
    comment '仓库' collate = utf8mb4_general_ci
                   row_format = DYNAMIC;

INSERT INTO wms.base_warehouse (warehouse_id, warehouse_code, warehouse_name, warehouse_type, del_flag, create_by, create_time, update_by, update_time) VALUES (101, 'WH-A01', '原材料仓库A', 'raw_material', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_warehouse (warehouse_id, warehouse_code, warehouse_name, warehouse_type, del_flag, create_by, create_time, update_by, update_time) VALUES (102, 'WH-A02', '成品仓库A', 'finished_product', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_warehouse (warehouse_id, warehouse_code, warehouse_name, warehouse_type, del_flag, create_by, create_time, update_by, update_time) VALUES (103, 'WH-A03', '半成品仓库A', 'semi_finished', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_warehouse (warehouse_id, warehouse_code, warehouse_name, warehouse_type, del_flag, create_by, create_time, update_by, update_time) VALUES (104, 'WH-A04', '辅料仓库A', 'auxiliary', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_warehouse (warehouse_id, warehouse_code, warehouse_name, warehouse_type, del_flag, create_by, create_time, update_by, update_time) VALUES (105, 'WH-A05', '包材仓库A', 'packaging', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_warehouse (warehouse_id, warehouse_code, warehouse_name, warehouse_type, del_flag, create_by, create_time, update_by, update_time) VALUES (106, 'WH-A06', '危化品仓库A', 'dangerous', '0', 'admin', '2026-04-01 08:00:00', null, null);
