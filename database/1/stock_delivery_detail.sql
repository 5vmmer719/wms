create table stock_delivery_detail
(
    detail_id   bigint auto_increment comment '明细ID'
        primary key,
    delivery_no varchar(30)             not null comment '交付单号',
    line_no     int                     not null comment '行号',
    mat_code    varchar(30)             not null comment '物料编码',
    mat_name    varchar(100)            null comment '物料名称',
    spec        varchar(100)            null comment '规格',
    quantity    decimal(12, 4)          not null comment '交付数量',
    unit_code   varchar(20)             null comment '单位',
    del_flag    char        default '0' null comment '删除标志',
    create_by   varchar(64) default ''  null comment '创建者',
    create_time datetime                null comment '创建时间',
    update_by   varchar(64) default ''  null comment '更新者',
    update_time datetime                null comment '更新时间',
    remark      varchar(500)            null comment '备注'
)
    comment '交付明细表';

INSERT INTO wms.stock_delivery_detail (detail_id, delivery_no, line_no, mat_code, mat_name, spec, quantity, unit_code, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'DL20260412012617', 1, 'T-CP001', '浮法玻璃4mm', 'FG-4MM', 21.0000, 'PCS', '0', 'admin', '2026-04-12 01:26:09', '', null, null);
INSERT INTO wms.stock_delivery_detail (detail_id, delivery_no, line_no, mat_code, mat_name, spec, quantity, unit_code, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, 'DL20260412013213', 1, 'T-CP001', '浮法玻璃4mm', 'FG-4MM', 20000.0000, 'PCS', '0', 'admin', '2026-04-12 01:32:26', '', null, null);
INSERT INTO wms.stock_delivery_detail (detail_id, delivery_no, line_no, mat_code, mat_name, spec, quantity, unit_code, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (3, 'DL20260412021377', 1, 'T-CP006', '中空玻璃标准型', 'IG-STD2', 2.0000, 'SQUARE_METER', '0', 'admin', '2026-04-12 02:13:21', '', null, null);
