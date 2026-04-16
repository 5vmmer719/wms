create table stock_check_order
(
    check_id       bigint auto_increment comment '盘点ID'
        primary key,
    check_no       varchar(30)                    not null comment '盘点单号',
    check_type     varchar(20)                    null comment '盘点类型（full全盘/cycle循环盘/spot抽盘）',
    warehouse_code varchar(30)                    null comment '盘点仓库编码',
    warehouse_name varchar(100)                   null comment '盘点仓库名称',
    check_status   varchar(20)  default 'created' null comment '状态（created已创建/counting盘点中/completed已完成/adjusted已调整）',
    plan_date      date                           null comment '计划盘点日期',
    actual_date    date                           null comment '实际盘点日期',
    checker_id     bigint                         null comment '盘点人ID',
    checker_name   varchar(64)                    null comment '盘点人',
    total_items    int          default 0         null comment '盘点物料总数',
    diff_items     int          default 0         null comment '差异物料数',
    del_flag       char         default '0'       null comment '删除标志',
    create_by      varchar(64)  default ''        null comment '创建者',
    create_time    datetime                       null comment '创建时间',
    update_by      varchar(64)  default ''        null comment '更新者',
    update_time    datetime                       null comment '更新时间',
    remark         varchar(500) default ''        null comment '备注'
)
    comment '盘点单表' collate = utf8mb4_general_ci;

INSERT INTO wms.stock_check_order (check_id, check_no, check_type, warehouse_code, warehouse_name, check_status, plan_date, actual_date, checker_id, checker_name, total_items, diff_items, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'SC20260412031634', 'full', 'WH-A02', '成品仓库A', 'completed', null, '2026-04-12', null, null, 8, 8, '0', 'admin', '2026-04-12 03:16:12', 'admin', '2026-04-12 03:16:27', '');
