create table quality_defect_handle
(
    handle_id     bigint auto_increment comment '主键'
        primary key,
    task_no       varchar(30)                    not null comment '关联检验任务编号',
    handle_type   varchar(20)                    not null comment '处理方式（rework返工/scrap报废/concession让步接收）',
    handle_qty    decimal(12, 4)                 null comment '处理数量',
    handle_desc   varchar(500)                   null comment '处理说明',
    handle_by     varchar(64)                    null comment '处理人',
    handle_date   date                           null comment '处理日期',
    handle_status varchar(20)  default 'pending' null comment '状态（pending待处理/processing处理中/completed已完成）',
    del_flag      char         default '0'       null comment '删除标识（0存在 1删除）',
    create_by     varchar(64)  default ''        null comment '创建者',
    create_time   datetime                       null comment '创建时间',
    update_by     varchar(64)  default ''        null comment '更新者',
    update_time   datetime                       null comment '更新时间',
    remark        varchar(500) default ''        null comment '备注'
)
    comment '不合格品处理表' collate = utf8mb4_general_ci
                             row_format = DYNAMIC;

INSERT INTO wms.quality_defect_handle (handle_id, task_no, handle_type, handle_qty, handle_desc, handle_by, handle_date, handle_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'QT20260413001', 'concession', 2.0000, 'test', null, null, 'pending', '1', 'admin', '2026-04-13 12:52:59', '', null, '');
