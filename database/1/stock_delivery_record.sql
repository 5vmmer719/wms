create table stock_delivery_record
(
    delivery_id       bigint auto_increment comment '交付ID'
        primary key,
    delivery_no       varchar(30)                   not null comment '交付单号',
    order_no          varchar(30)                   not null comment '客户订单号',
    customer_code     varchar(30)                   null comment '客户编码',
    customer_name     varchar(100)                  null comment '客户名称',
    delivery_date     date                          null comment '交付日期',
    logistics_no      varchar(50)                   null comment '物流单号',
    logistics_company varchar(50)                   null comment '物流公司',
    delivery_address  varchar(200)                  null comment '交付地址',
    delivery_status   varchar(20) default 'pending' null comment '状态（pending待发货/shipped已发货/received已签收）',
    out_order_no      varchar(30)                   null comment '关联出库单号',
    total_quantity    decimal(12, 4)                null comment '交付总数量',
    del_flag          char        default '0'       null comment '删除标志（0正常 1删除）',
    create_by         varchar(64) default ''        null comment '创建者',
    create_time       datetime                      null comment '创建时间',
    update_by         varchar(64) default ''        null comment '更新者',
    update_time       datetime                      null comment '更新时间',
    remark            varchar(500)                  null comment '备注'
)
    comment '交付记录表';

INSERT INTO wms.stock_delivery_record (delivery_id, delivery_no, order_no, customer_code, customer_name, delivery_date, logistics_no, logistics_company, delivery_address, delivery_status, out_order_no, total_quantity, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'DL20260412012617', 'CO20260412005610', 'CUS-002', '东方幕墙工程有限公司', '2026-04-12', null, null, null, 'received', null, 21.0000, '0', 'admin', '2026-04-12 01:26:09', 'admin', '2026-04-12 01:26:16', null);
INSERT INTO wms.stock_delivery_record (delivery_id, delivery_no, order_no, customer_code, customer_name, delivery_date, logistics_no, logistics_company, delivery_address, delivery_status, out_order_no, total_quantity, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, 'DL20260412013213', 'CO20260412012734', 'CUS-001', '华南建材集团', '2026-04-12', null, null, null, 'received', null, 20000.0000, '0', 'admin', '2026-04-12 01:32:26', 'admin', '2026-04-12 01:32:35', null);
INSERT INTO wms.stock_delivery_record (delivery_id, delivery_no, order_no, customer_code, customer_name, delivery_date, logistics_no, logistics_company, delivery_address, delivery_status, out_order_no, total_quantity, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (3, 'DL20260412021377', 'CO20260412020174', 'CUS-001', '华南建材集团', '2026-04-12', null, null, null, 'received', 'OC20260412021367', 2.0000, '0', 'admin', '2026-04-12 02:13:21', 'admin', '2026-04-12 02:13:35', null);
