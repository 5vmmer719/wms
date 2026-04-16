create table stock_customer_order
(
    order_id             bigint auto_increment comment '订单ID'
        primary key,
    order_no             varchar(30)                    not null comment '客户订单号',
    customer_code        varchar(30)                    not null comment '客户编码',
    customer_name        varchar(100)                   null comment '客户名称',
    order_date           date                           null comment '下单日期',
    delivery_date        date                           null comment '要求交付日期',
    actual_delivery_date date                           null comment '实际交付日期',
    order_status         varchar(20)  default 'created' null comment '状态（created已创建/confirmed已确认/producing生产中/completed已完成/delivered已交付/closed已关闭）',
    total_amount         decimal(12, 2)                 null comment '订单总金额',
    del_flag             char         default '0'       null comment '删除标识（0存在 1删除）',
    create_by            varchar(64)  default ''        null comment '创建者',
    create_time          datetime                       null comment '创建时间',
    update_by            varchar(64)  default ''        null comment '更新者',
    update_time          datetime                       null comment '更新时间',
    remark               varchar(500) default ''        null comment '备注'
)
    comment '客户订单表';

INSERT INTO wms.stock_customer_order (order_id, order_no, customer_code, customer_name, order_date, delivery_date, actual_delivery_date, order_status, total_amount, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'CO20260412005610', 'CUS-002', '东方幕墙工程有限公司', '2026-04-13', '2026-04-27', '2026-04-12', 'closed', 882.00, '0', 'admin', '2026-04-12 00:56:10', 'admin', '2026-04-12 01:26:51', '');
INSERT INTO wms.stock_customer_order (order_id, order_no, customer_code, customer_name, order_date, delivery_date, actual_delivery_date, order_status, total_amount, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, 'CO20260412012734', 'CUS-001', '华南建材集团', '2026-04-14', '2026-04-14', '2026-04-12', 'delivered', 84000.00, '0', 'admin', '2026-04-12 01:27:53', 'admin', '2026-04-12 01:32:29', '');
INSERT INTO wms.stock_customer_order (order_id, order_no, customer_code, customer_name, order_date, delivery_date, actual_delivery_date, order_status, total_amount, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (3, 'CO20260412020174', 'CUS-001', '华南建材集团', '2026-04-14', '2026-04-21', '2026-04-12', 'delivered', 320.00, '0', 'admin', '2026-04-12 02:01:41', 'admin', '2026-04-12 02:13:32', '');
INSERT INTO wms.stock_customer_order (order_id, order_no, customer_code, customer_name, order_date, delivery_date, actual_delivery_date, order_status, total_amount, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (4, 'CO20260413124266', 'CUS-001', '华南建材集团', '2026-04-13', '2026-04-20', null, 'producing', 840.00, '0', 'admin', '2026-04-13 12:42:20', 'admin', '2026-04-13 12:43:25', 'test');
