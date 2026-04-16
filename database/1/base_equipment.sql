create table base_equipment
(
    equipment_id       bigint auto_increment comment '主键'
        primary key,
    equipment_code     varchar(30)              not null comment '设备编码',
    equipment_name     varchar(100)             not null comment '设备名称（1号窑炉/A线成型机）',
    equipment_type     varchar(20)              null comment '类型（furnace窑炉/forming成型机/annealing退火窑/cutting切裁机/other其他）',
    workshop_code      varchar(30)              null comment '所属车间编码',
    capacity           decimal(10, 2)           null comment '日产能',
    capacity_unit      varchar(20)              null comment '产能单位（吨/天、片/天）',
    equipment_status   char         default '0' null comment '状态（0正常 1维护中 2故障 3停用）',
    purchase_date      date                     null comment '购置日期',
    last_maintain_date date                     null comment '上次维护日期',
    next_maintain_date date                     null comment '下次计划维护日期',
    maintain_cycle     int                      null comment '维护周期（天）',
    del_flag           char         default '0' null comment '删除标识（0存在 1删除）',
    create_by          varchar(64)  default ''  null comment '创建者',
    create_time        datetime                 null comment '创建时间',
    update_by          varchar(64)  default ''  null comment '更新者',
    update_time        datetime                 null comment '更新时间',
    remark             varchar(500) default ''  null comment '备注'
)
    comment '设备台账表' collate = utf8mb4_general_ci
                         row_format = DYNAMIC;

INSERT INTO wms.base_equipment (equipment_id, equipment_code, equipment_name, equipment_type, workshop_code, capacity, capacity_unit, equipment_status, purchase_date, last_maintain_date, next_maintain_date, maintain_cycle, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'EQ-001', '1号浮法窑炉', 'furnace', null, 500.00, '吨/天', '0', '2020-06-15', null, null, null, '0', 'admin', '2026-04-11 23:57:17', '', null, '日产能500吨，900吨级浮法玻璃生产线');
INSERT INTO wms.base_equipment (equipment_id, equipment_code, equipment_name, equipment_type, workshop_code, capacity, capacity_unit, equipment_status, purchase_date, last_maintain_date, next_maintain_date, maintain_cycle, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, 'EQ-002', 'A线锡槽成型机', 'forming', null, 480.00, '吨/天', '0', '2020-06-15', null, null, null, '0', 'admin', '2026-04-11 23:57:17', '', null, '浮法成型段，配合1号窑炉');
INSERT INTO wms.base_equipment (equipment_id, equipment_code, equipment_name, equipment_type, workshop_code, capacity, capacity_unit, equipment_status, purchase_date, last_maintain_date, next_maintain_date, maintain_cycle, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (3, 'EQ-003', 'A线退火窑', 'annealing', null, 500.00, '吨/天', '0', '2020-06-15', null, null, null, '0', 'admin', '2026-04-11 23:57:17', '', null, '退火段，长度120米');
INSERT INTO wms.base_equipment (equipment_id, equipment_code, equipment_name, equipment_type, workshop_code, capacity, capacity_unit, equipment_status, purchase_date, last_maintain_date, next_maintain_date, maintain_cycle, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (4, 'EQ-004', '1号自动切裁机', 'cutting', null, 2000.00, '片/天', '0', '2021-03-20', null, null, null, '0', 'admin', '2026-04-11 23:57:17', '', null, '自动化切裁线，支持多规格切割');
INSERT INTO wms.base_equipment (equipment_id, equipment_code, equipment_name, equipment_type, workshop_code, capacity, capacity_unit, equipment_status, purchase_date, last_maintain_date, next_maintain_date, maintain_cycle, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (5, 'EQ-005', '2号自动切裁机', 'cutting', null, 2000.00, '片/天', '0', '2021-03-20', null, null, null, '0', 'admin', '2026-04-11 23:57:17', '', null, '备用切裁线');
INSERT INTO wms.base_equipment (equipment_id, equipment_code, equipment_name, equipment_type, workshop_code, capacity, capacity_unit, equipment_status, purchase_date, last_maintain_date, next_maintain_date, maintain_cycle, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (6, 'EQ-006', '磨边机组', 'other', 'WS-A01', 1500.00, '片/天', '0', '2022-01-10', '2026-04-11', '2026-04-18', 7, '0', 'admin', '2026-04-11 23:57:17', '', '2026-04-12 00:24:38', '玻璃边缘磨削加工');
