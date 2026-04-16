create table stock_in_return
(
    return_id      bigint auto_increment comment '主键'
        primary key,
    return_no      varchar(64)      null comment '入库退货单号',
    return_type    varchar(32)      null comment '入库退货类型',
    return_reason  varchar(255)     null comment '入库退货原因',
    return_status  varchar(32)      null comment '退货单据状态',
    order_no       varchar(64)      null comment '入库单号',
    warehouse_code varchar(64)      null comment '仓库',
    del_flag       char default '0' null comment '删除标识',
    create_by      varchar(64)      null comment '创建人',
    create_time    datetime         null comment '创建时间',
    update_by      varchar(64)      null comment '修改人',
    update_time    datetime         null comment '修改时间'
)
    comment '入库单退货' collate = utf8mb4_general_ci
                         row_format = DYNAMIC;

INSERT INTO wms.stock_in_return (return_id, return_no, return_type, return_reason, return_status, order_no, warehouse_code, del_flag, create_by, create_time, update_by, update_time) VALUES (101, 'TRT20260407001', 'purchase_return', '着色剂质量不合格-色差超标', 'returned', 'TRK20260402001', 'WH-A04', '0', 'admin', '2026-04-07 09:00:00', null, null);
INSERT INTO wms.stock_in_return (return_id, return_no, return_type, return_reason, return_status, order_no, warehouse_code, del_flag, create_by, create_time, update_by, update_time) VALUES (102, 'TRT20260407002', 'purchase_return', '石灰石粉含水量超标', 'created', 'TRK20260401001', 'WH-A01', '0', 'admin', '2026-04-07 14:00:00', null, null);
