create table stock_prod_plan
(
    plan_id           bigint auto_increment comment '计划ID'
        primary key,
    plan_no           varchar(30)                    not null comment '计划编号',
    plan_name         varchar(100)                   null comment '计划名称',
    plan_type         varchar(20)                    null comment '计划类型（monthly月度/weekly周度）',
    plan_start_date   date                           null comment '计划开始日期',
    plan_end_date     date                           null comment '计划结束日期',
    mat_code          varchar(30)                    null comment '产品物料编码',
    mat_name          varchar(100)                   null comment '产品名称',
    plan_quantity     decimal(12, 4)                 null comment '计划生产数量',
    actual_quantity   decimal(12, 4) default 0.0000  null comment '实际完成数量',
    customer_order_no varchar(30)                    null comment '关联客户订单号',
    workshop_code     varchar(30)                    null comment '生产车间编码',
    plan_status       varchar(20)    default 'draft' null comment '状态（draft草稿/confirmed已确认/executing执行中/completed已完成/cancelled已取消）',
    completion_rate   decimal(5, 2)  default 0.00    null comment '完成率（%）',
    del_flag          char           default '0'     null comment '删除标识（0正常 1删除）',
    create_by         varchar(64)    default ''      null comment '创建者',
    create_time       datetime                       null comment '创建时间',
    update_by         varchar(64)    default ''      null comment '更新者',
    update_time       datetime                       null comment '更新时间',
    remark            varchar(500)   default ''      null comment '备注'
)
    comment '生产计划表' collate = utf8mb4_general_ci;

INSERT INTO wms.stock_prod_plan (plan_id, plan_no, plan_name, plan_type, plan_start_date, plan_end_date, mat_code, mat_name, plan_quantity, actual_quantity, customer_order_no, workshop_code, plan_status, completion_rate, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'PP20260412034334', '1', 'monthly', '2026-04-02', '2026-04-06', 'T-CP001', '浮法玻璃4mm', 0.0000, 0.0000, null, 'WS-A01', 'completed', 0.00, '0', 'admin', '2026-04-12 03:43:04', 'admin', '2026-04-12 03:43:26', '');
INSERT INTO wms.stock_prod_plan (plan_id, plan_no, plan_name, plan_type, plan_start_date, plan_end_date, mat_code, mat_name, plan_quantity, actual_quantity, customer_order_no, workshop_code, plan_status, completion_rate, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, 'PP20260413124578', 'test', 'weekly', '2026-04-13', '2027-04-03', 'T-CP005', '镀膜玻璃6mm', 10.0000, 0.0000, 'CO20260413124266', 'WS-A01', 'executing', 0.00, '0', 'admin', '2026-04-13 12:45:03', 'admin', '2026-04-13 12:45:21', 'test');
