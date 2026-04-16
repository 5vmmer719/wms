create table stock_out_return
(
    return_id      bigint auto_increment comment '主键'
        primary key,
    return_no      varchar(64)      null comment '出库退货单号',
    warehouse_code varchar(64)      null comment '仓库',
    workshop_code  varchar(64)      null comment '车间',
    return_type    varchar(32)      null comment '退货类型',
    return_reason  varchar(255)     null comment '退货原因',
    return_status  varchar(32)      null comment '退货状态',
    prod_order_no  varchar(64)      null comment '生产订单号',
    order_no       varchar(64)      null comment '出库单号',
    del_flag       char default '0' null comment '删除标识',
    create_by      varchar(64)      null comment '创建人',
    create_time    datetime         null comment '创建时间',
    update_by      varchar(64)      null comment '修改人',
    update_time    datetime         null comment '修改时间'
)
    comment '出库单退货' collate = utf8mb4_general_ci
                         row_format = DYNAMIC;

INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (101, 'TCR20260407001', 'WH-A04', 'WS-A01', 'production_return', '辅料多领退回', 'returned', 'TP20260403001', 'TCK20260402001', '0', 'admin', '2026-04-07 10:00:00', null, null);
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (102, 'OPR20260408125959', 'WH-A03', 'WS-A02', 'production_return', '1', 'created', 'TP20260403002', 'TCK20260403002', '1', 'admin', '2026-04-08 12:59:32', null, null);
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (103, 'OPR20260408130030', 'WH-A03', 'WS-A02', 'production_return', '1', 'created', 'TP20260403002', 'TCK20260403002', '1', 'admin', '2026-04-08 13:00:27', null, null);
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (104, 'OPR20260408130443', 'WH-A03', 'WS-A02', 'production_return', '1', 'created', 'TP20260403002', 'TCK20260403002', '1', 'admin', '2026-04-08 13:04:52', null, null);
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (105, 'OPR20260408130777', 'WH-A03', 'WS-A02', 'production_return', '1', 'created', 'TP20260403002', 'TCK20260403002', '1', 'admin', '2026-04-08 13:07:48', null, null);
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (106, 'OPR20260408131841', 'WH-A03', 'WS-A02', 'production_return', '1', 'created', 'TP20260403001', 'TCK20260403001', '1', 'admin', '2026-04-08 13:18:42', null, null);
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (107, 'OPR20260408133140', 'WH-A04', 'WS-A01', 'production_return', '1', 'created', 'TP20260403001', 'TCK20260402001', '1', 'admin', '2026-04-08 13:31:38', null, null);
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (108, 'OPR20260408133610', 'WH-A04', 'WS-A01', 'production_return', '1', 'printed', 'TP20260403001', 'TCK20260402001', '0', 'admin', '2026-04-08 13:36:34', 'admin', '2026-04-08 13:36:55');
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (109, 'OPR20260408180142', 'WH-A04', 'WS-A01', 'production_return', '1', 'created', 'TP20260403002', 'OP20260408174946', '0', 'admin', '2026-04-08 18:01:39', null, null);
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (110, 'OPR20260408181738', 'WH-A04', 'WS-A01', 'production_return', '1', 'returned', 'TP20260403002', 'OP20260408174946', '0', 'admin', '2026-04-08 18:17:03', 'admin', '2026-04-08 18:17:12');
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (111, 'OPR20260408182299', 'WH-A04', 'WS-A01', 'production_return', '1', 'returned', 'TP20260403002', 'OP20260408174946', '0', 'admin', '2026-04-08 18:22:13', 'admin', '2026-04-08 18:22:46');
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (112, 'OPR20260408182817', 'WH-A04', 'WS-A01', 'production_return', '；', 'returned', 'TP20260403002', 'OP20260408174946', '0', 'admin', '2026-04-08 18:28:20', 'admin', '2026-04-08 18:28:51');
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (113, 'OPR20260408183336', 'WH-A02', 'WS-A03', 'production_return', '1', 'returned', 'TP20260404001', 'TCK20260405001', '0', 'admin', '2026-04-08 18:33:36', 'admin', '2026-04-08 18:33:47');
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (114, 'OPR20260411191375', 'WH-A01', 'WS-A04', 'production_return', 'meiyongwan', 'returned', 'P20260411190751', 'OP20260411191260', '0', 'admin', '2026-04-11 19:13:59', 'admin', '2026-04-11 19:14:15');
INSERT INTO wms.stock_out_return (return_id, return_no, warehouse_code, workshop_code, return_type, return_reason, return_status, prod_order_no, order_no, del_flag, create_by, create_time, update_by, update_time) VALUES (115, 'OPR20260411203375', 'WH-A01', 'WS-A01', 'production_return', '1', 'returned', 'P20260411201553', 'OP20260411201568', '0', 'admin', '2026-04-11 20:33:26', 'admin', '2026-04-11 20:33:46');
