create table base_equipment_maintain
(
    maintain_id       bigint auto_increment comment '主键'
        primary key,
    maintain_no       varchar(30)                   not null comment '维护单号',
    equipment_code    varchar(30)                   not null comment '设备编码',
    equipment_name    varchar(100)                  null comment '设备名称',
    maintain_type     varchar(20)                   null comment '维护类型（routine例行/repair维修/overhaul大修）',
    maintain_date     date                          not null comment '维护日期',
    maintain_end_date date                          null comment '维护结束日期',
    maintain_desc     varchar(500)                  null comment '维护内容',
    maintain_by       varchar(64)                   null comment '维护人员',
    maintain_hours    decimal(10, 2)                null comment '维护耗时（小时）',
    maintain_cost     decimal(12, 2)                null comment '维护费用',
    maintain_status   char         default '0'      null comment '状态（0进行中 1已完成）',
    source            varchar(10)  default 'manual' null comment '来源（auto自动 manual手动）',
    del_flag          char         default '0'      null comment '删除标识（0存在 1删除）',
    create_by         varchar(64)  default ''       null comment '创建者',
    create_time       datetime                      null comment '创建时间',
    update_by         varchar(64)  default ''       null comment '更新者',
    update_time       datetime                      null comment '更新时间',
    remark            varchar(500) default ''       null comment '备注'
)
    comment '设备维护记录表' collate = utf8mb4_general_ci
                             row_format = DYNAMIC;

INSERT INTO wms.base_equipment_maintain (maintain_id, maintain_no, equipment_code, equipment_name, maintain_type, maintain_date, maintain_end_date, maintain_desc, maintain_by, maintain_hours, maintain_cost, maintain_status, source, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'WH20260411001', 'EQ-006', '磨边机组', 'overhaul', '2026-04-11', '2026-04-11', null, null, 1.00, 0.00, '1', 'manual', '0', '', '2026-04-11 23:59:03', '', '2026-04-12 00:02:11', '');
