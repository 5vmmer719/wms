create table base_location
(
    location_id    bigint auto_increment comment '主键'
        primary key,
    warehouse_code varchar(64)      null comment '仓库',
    location_code  varchar(64)      null comment '货位编码',
    location_name  varchar(128)     null comment '货位名称',
    location_type  varchar(32)      null comment '货位类型',
    del_flag       char default '0' null comment '删除标识',
    create_by      varchar(64)      null comment '创建人',
    create_time    datetime         null comment '创建时间',
    update_by      varchar(64)      null comment '修改人',
    update_time    datetime         null comment '修改时间'
)
    comment '货位' collate = utf8mb4_general_ci
                   row_format = DYNAMIC;

INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (101, 'WH-A01', 'RA-01', '纯碱存放区', 'normal', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (102, 'WH-A01', 'RA-02', '石英砂存放区', 'normal', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (103, 'WH-A01', 'RA-03', '石灰石存放区', 'normal', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (104, 'WH-A01', 'RA-04', '大宗原料区', 'bulk', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (105, 'WH-A02', 'CA-01', '玻璃成品A区', 'normal', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (106, 'WH-A02', 'CA-02', '玻璃成品B区', 'normal', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (107, 'WH-A02', 'CA-03', '待发货区', 'shipment', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (108, 'WH-A03', 'BA-01', '半成品A区', 'normal', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (109, 'WH-A03', 'BA-02', '半成品B区', 'normal', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (110, 'WH-A03', 'BA-03', '暂存区', 'temp', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (111, 'WH-A04', 'FA-01', '化工辅料区', 'normal', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (112, 'WH-A04', 'FA-02', '模具配件区', 'normal', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (113, 'WH-A05', 'PA-01', '纸箱存储区', 'normal', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (114, 'WH-A05', 'PA-02', '塑料包装区', 'normal', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_location (location_id, warehouse_code, location_code, location_name, location_type, del_flag, create_by, create_time, update_by, update_time) VALUES (115, 'WH-A06', 'DA-01', '危化品存放区', 'danger', '0', 'admin', '2026-04-01 08:00:00', null, null);
