create table stock_customer_order_detail
(
    detail_id     bigint auto_increment comment '明细ID'
        primary key,
    order_no      varchar(30)                   not null comment '客户订单号',
    line_no       int                           not null comment '行号',
    mat_code      varchar(30)                   not null comment '产品物料编码',
    mat_name      varchar(100)                  null comment '产品名称',
    spec          varchar(100)                  null comment '规格',
    quantity      decimal(12, 4)                not null comment '订单数量',
    delivered_qty decimal(12, 4) default 0.0000 null comment '已交付数量',
    unit_price    decimal(12, 4)                null comment '单价',
    amount        decimal(12, 2)                null comment '金额',
    prod_order_no varchar(30)                   null comment '关联生产工单号',
    del_flag      char           default '0'    null comment '删除标识（0存在 1删除）',
    create_by     varchar(64)    default ''     null comment '创建者',
    create_time   datetime                      null comment '创建时间',
    update_by     varchar(64)    default ''     null comment '更新者',
    update_time   datetime                      null comment '更新时间',
    remark        varchar(500)   default ''     null comment '备注'
)
    comment '客户订单明细表' collate = utf8mb4_general_ci;

INSERT INTO wms.stock_customer_order_detail (detail_id, order_no, line_no, mat_code, mat_name, spec, quantity, delivered_qty, unit_price, amount, prod_order_no, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'CO20260412005610', 1, 'T-CP001', '浮法玻璃4mm', 'FG-4MM', 21.0000, 21.0000, 42.0000, 882.00, 'P20260412005699', '0', 'admin', '2026-04-12 00:56:10', 'admin', '2026-04-12 01:26:12', '');
INSERT INTO wms.stock_customer_order_detail (detail_id, order_no, line_no, mat_code, mat_name, spec, quantity, delivered_qty, unit_price, amount, prod_order_no, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, 'CO20260412012734', 1, 'T-CP001', '浮法玻璃4mm', 'FG-4MM', 2000.0000, 20000.0000, 42.0000, 84000.00, 'P20260412012849', '0', 'admin', '2026-04-12 01:27:53', 'admin', '2026-04-12 01:32:29', '');
INSERT INTO wms.stock_customer_order_detail (detail_id, order_no, line_no, mat_code, mat_name, spec, quantity, delivered_qty, unit_price, amount, prod_order_no, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (3, 'CO20260412020174', 1, 'T-CP006', '中空玻璃标准型', 'IG-STD2', 2.0000, 2.0000, 160.0000, 320.00, null, '0', 'admin', '2026-04-12 02:01:41', 'admin', '2026-04-12 02:13:32', '');
INSERT INTO wms.stock_customer_order_detail (detail_id, order_no, line_no, mat_code, mat_name, spec, quantity, delivered_qty, unit_price, amount, prod_order_no, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (4, 'CO20260413124266', 1, 'T-CP001', '浮法玻璃4mm', 'FG-4MM', 20.0000, 0.0000, 42.0000, 840.00, 'P20260413124394', '0', 'admin', '2026-04-13 12:42:20', 'admin', '2026-04-13 12:43:25', '');
