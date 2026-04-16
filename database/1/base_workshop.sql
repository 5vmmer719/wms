create table base_workshop
(
    workshop_id   bigint auto_increment comment '主键'
        primary key,
    workshop_code varchar(64)      null comment '车间编码',
    workshop_name varchar(128)     null comment '车间名称',
    del_flag      char default '0' null comment '删除标识',
    create_by     varchar(64)      null comment '创建人',
    create_time   datetime         null comment '创建时间',
    update_by     varchar(64)      null comment '修改人',
    update_time   datetime         null comment '修改时间'
)
    comment '车间' collate = utf8mb4_general_ci
                   row_format = DYNAMIC;

INSERT INTO wms.base_workshop (workshop_id, workshop_code, workshop_name, del_flag, create_by, create_time, update_by, update_time) VALUES (101, 'WS-A01', '玻璃熔制车间', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_workshop (workshop_id, workshop_code, workshop_name, del_flag, create_by, create_time, update_by, update_time) VALUES (102, 'WS-A02', '玻璃成型车间', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_workshop (workshop_id, workshop_code, workshop_name, del_flag, create_by, create_time, update_by, update_time) VALUES (103, 'WS-A03', '深加工车间', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_workshop (workshop_id, workshop_code, workshop_name, del_flag, create_by, create_time, update_by, update_time) VALUES (104, 'WS-A04', '包装车间', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_workshop (workshop_id, workshop_code, workshop_name, del_flag, create_by, create_time, update_by, update_time) VALUES (105, 'WS-A05', '质检车间', '0', 'admin', '2026-04-01 08:00:00', null, null);
