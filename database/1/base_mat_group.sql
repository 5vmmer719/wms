create table base_mat_group
(
    group_id               bigint auto_increment comment '主键'
        primary key,
    group_code             varchar(64)      null comment '物料组编码',
    group_name             varchar(128)     null comment '物料组名称',
    default_warehouse_type varchar(32)      null comment '默认仓库类型',
    del_flag               char default '0' null comment '删除标识',
    create_by              varchar(64)      null comment '创建人',
    create_time            datetime         null comment '创建时间',
    update_by              varchar(64)      null comment '修改人',
    update_time            datetime         null comment '修改时间'
)
    comment '物料组' collate = utf8mb4_general_ci
                     row_format = DYNAMIC;

INSERT INTO wms.base_mat_group (group_id, group_code, group_name, default_warehouse_type, del_flag, create_by, create_time, update_by, update_time) VALUES (101, 'CP', '成品类', 'finished_product', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_mat_group (group_id, group_code, group_name, default_warehouse_type, del_flag, create_by, create_time, update_by, update_time) VALUES (102, 'BJ', '半成品类', 'semi_finished', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_mat_group (group_id, group_code, group_name, default_warehouse_type, del_flag, create_by, create_time, update_by, update_time) VALUES (103, 'YL', '原材料', 'raw_material', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_mat_group (group_id, group_code, group_name, default_warehouse_type, del_flag, create_by, create_time, update_by, update_time) VALUES (104, 'FL', '辅料', 'auxiliary', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_mat_group (group_id, group_code, group_name, default_warehouse_type, del_flag, create_by, create_time, update_by, update_time) VALUES (105, 'BC', '包材', 'packaging', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_mat_group (group_id, group_code, group_name, default_warehouse_type, del_flag, create_by, create_time, update_by, update_time) VALUES (106, 'HG', '化工原料', 'dangerous', '0', 'admin', '2026-04-01 08:00:00', null, null);
