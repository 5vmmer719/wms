create table base_mat_class
(
    class_id    bigint auto_increment comment '主键'
        primary key,
    class_code  varchar(64)      null comment '分类编码',
    class_name  varchar(128)     null comment '分类名称',
    del_flag    char default '0' null comment '删除标识',
    create_by   varchar(64)      null comment '创建人',
    create_time datetime         null comment '创建时间',
    update_by   varchar(64)      null comment '修改人',
    update_time datetime         null comment '修改时间'
)
    comment '物料分类' collate = utf8mb4_general_ci
                       row_format = DYNAMIC;

INSERT INTO wms.base_mat_class (class_id, class_code, class_name, del_flag, create_by, create_time, update_by, update_time) VALUES (101, 'GP', '玻璃制品', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_mat_class (class_id, class_code, class_name, del_flag, create_by, create_time, update_by, update_time) VALUES (102, 'TB', '陶瓷制品', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_mat_class (class_id, class_code, class_name, del_flag, create_by, create_time, update_by, update_time) VALUES (103, 'CJ', '纯碱类', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_mat_class (class_id, class_code, class_name, del_flag, create_by, create_time, update_by, update_time) VALUES (104, 'SYS', '石英砂类', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_mat_class (class_id, class_code, class_name, del_flag, create_by, create_time, update_by, update_time) VALUES (105, 'LZ', '石灰石类', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_mat_class (class_id, class_code, class_name, del_flag, create_by, create_time, update_by, update_time) VALUES (106, 'HL', '化工辅料', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_mat_class (class_id, class_code, class_name, del_flag, create_by, create_time, update_by, update_time) VALUES (107, 'MJ', '模具配件', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_mat_class (class_id, class_code, class_name, del_flag, create_by, create_time, update_by, update_time) VALUES (108, 'ZB', '纸箱包装', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_mat_class (class_id, class_code, class_name, del_flag, create_by, create_time, update_by, update_time) VALUES (109, 'SL', '塑料包装', '0', 'admin', '2026-04-01 08:00:00', null, null);
