create table quality_task
(
    task_id         bigint auto_increment comment '主键'
        primary key,
    task_no         varchar(30)                    not null comment '检验任务编号',
    check_type      varchar(20)                    not null comment '检验类型（incoming/process/final）',
    source_type     varchar(20)                    null comment '来源类型（in_order入库单/prod_order生产工单）',
    source_no       varchar(30)                    null comment '来源单号',
    mat_code        varchar(30)                    null comment '物料编码',
    mat_name        varchar(100)                   null comment '物料名称',
    batch           varchar(30)                    null comment '批次',
    quantity        decimal(12, 4)                 null comment '送检数量',
    standard_code   varchar(30)                    null comment '检验标准编码',
    standard_name   varchar(100)                   null comment '检验标准名称',
    inspector_id    bigint                         null comment '质检员ID',
    inspector_name  varchar(64)                    null comment '质检员姓名',
    task_status     varchar(20)  default 'pending' null comment '状态（pending待检验/checking检验中/passed合格/failed不合格）',
    qualified_qty   decimal(12, 4)                 null comment '合格数量',
    unqualified_qty decimal(12, 4)                 null comment '不合格数量',
    check_time      datetime                       null comment '检验完成时间',
    del_flag        char         default '0'       null comment '删除标识（0存在 1删除）',
    create_by       varchar(64)  default ''        null comment '创建者',
    create_time     datetime                       null comment '创建时间',
    update_by       varchar(64)  default ''        null comment '更新者',
    update_time     datetime                       null comment '更新时间',
    remark          varchar(500) default ''        null comment '备注'
)
    comment '检验任务表' collate = utf8mb4_general_ci
                         row_format = DYNAMIC;

INSERT INTO wms.quality_task (task_id, task_no, check_type, source_type, source_no, mat_code, mat_name, batch, quantity, standard_code, standard_name, inspector_id, inspector_name, task_status, qualified_qty, unqualified_qty, check_time, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'QT20260412001', 'process', 'in_order', null, null, null, null, 1.0000, null, null, null, null, 'pending', null, null, null, '1', 'admin', '2026-04-12 03:19:29', '', null, '');
INSERT INTO wms.quality_task (task_id, task_no, check_type, source_type, source_no, mat_code, mat_name, batch, quantity, standard_code, standard_name, inspector_id, inspector_name, task_status, qualified_qty, unqualified_qty, check_time, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, 'QT20260412002', 'incoming', 'prod_order', null, null, null, null, 0.0000, 'QS-001', '浮法玻璃成品检验标准', null, 'admin', 'failed', 0.0000, 0.0000, '2026-04-12 03:19:56', '1', 'admin', '2026-04-12 03:19:50', 'admin', '2026-04-12 03:19:56', '');
INSERT INTO wms.quality_task (task_id, task_no, check_type, source_type, source_no, mat_code, mat_name, batch, quantity, standard_code, standard_name, inspector_id, inspector_name, task_status, qualified_qty, unqualified_qty, check_time, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (3, 'QT20260413001', 'incoming', 'in_order', null, null, null, null, 2.0000, 'QS-002', '石英砂原料检验标准', null, 'admin', 'failed', 0.0000, 2.0000, '2026-04-13 12:52:39', '0', 'admin', '2026-04-13 12:51:43', 'admin', '2026-04-13 12:52:39', '');
