create table base_workstation
(
    station_id     bigint auto_increment comment '主键'
        primary key,
    station_code   varchar(30)              not null comment '工位编码',
    station_name   varchar(100)             not null comment '工位名称',
    equipment_code varchar(30)              null comment '所属设备编码',
    workshop_code  varchar(30)              null comment '所属车间编码',
    operator_id    bigint                   null comment '默认操作员ID',
    operator_name  varchar(64)              null comment '默认操作员姓名',
    station_status char         default '0' null comment '状态（0空闲 1生产中 2维护中）',
    del_flag       char         default '0' null comment '删除标识（0存在 1删除）',
    create_by      varchar(64)  default ''  null comment '创建者',
    create_time    datetime                 null comment '创建时间',
    update_by      varchar(64)  default ''  null comment '更新者',
    update_time    datetime                 null comment '更新时间',
    remark         varchar(500) default ''  null comment '备注'
)
    comment '工位表' collate = utf8mb4_general_ci
                     row_format = DYNAMIC;

INSERT INTO wms.base_workstation (station_id, station_code, station_name, equipment_code, workshop_code, operator_id, operator_name, station_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'WS-001', '窑炉操控台', 'EQ-001', null, null, null, '0', '0', 'admin', '2026-04-11 23:57:17', '', null, '窑炉温度监控与操控');
INSERT INTO wms.base_workstation (station_id, station_code, station_name, equipment_code, workshop_code, operator_id, operator_name, station_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, 'WS-002', '成型操控台', 'EQ-002', null, null, null, '0', '0', 'admin', '2026-04-11 23:57:17', '', null, '锡槽成型段操控');
INSERT INTO wms.base_workstation (station_id, station_code, station_name, equipment_code, workshop_code, operator_id, operator_name, station_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (3, 'WS-003', '退火监控台', 'EQ-003', null, null, null, '0', '0', 'admin', '2026-04-11 23:57:17', '', null, '退火温度曲线监控');
INSERT INTO wms.base_workstation (station_id, station_code, station_name, equipment_code, workshop_code, operator_id, operator_name, station_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (4, 'WS-004', '切裁1号工位', 'EQ-004', null, null, null, '0', '0', 'admin', '2026-04-11 23:57:17', '', null, '1号切裁机操作工位');
INSERT INTO wms.base_workstation (station_id, station_code, station_name, equipment_code, workshop_code, operator_id, operator_name, station_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (5, 'WS-005', '切裁2号工位', 'EQ-005', null, null, null, '0', '0', 'admin', '2026-04-11 23:57:17', '', null, '2号切裁机操作工位');
INSERT INTO wms.base_workstation (station_id, station_code, station_name, equipment_code, workshop_code, operator_id, operator_name, station_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (6, 'WS-006', '磨边工位', 'EQ-005', 'WS-A01', null, '1', '0', '0', 'admin', '2026-04-11 23:57:17', '', '2026-04-11 23:58:37', '磨边加工工位');
