create table stock_in_return_detail
(
    detail_id       bigint auto_increment comment '主键'
        primary key,
    warehouse_code  varchar(64)                     null comment '仓库',
    return_no       varchar(64)                     null comment '入库退货单号',
    line_no         int                             null comment '行号',
    label_id        bigint                          null comment '物料标签id',
    mat_code        varchar(64)                     null comment '物料编码',
    mat_name        varchar(128)                    null comment '物料名称',
    fd_code         varchar(64)                     null comment '财务编码',
    fig_num         varchar(64)                     null comment '图号',
    mat_group       varchar(64)                     null comment '物料组',
    mat_class       varchar(64)                     null comment '物料分类',
    unit_code       varchar(32)                     null comment '单位',
    batch           varchar(128)                    null comment '批次',
    quantity        decimal(24, 6) default 0.000000 null comment '数量',
    return_quantity decimal(24, 6) default 0.000000 null comment '退还数量',
    location_code   varchar(64)                     null comment '货位',
    supplier_code   varchar(64)                     null comment '供应商编码',
    supplier_name   varchar(128)                    null comment '供应商名称',
    del_flag        char           default '0'      null comment '删除标识',
    create_by       varchar(64)                     null comment '创建人',
    create_time     datetime                        null comment '创建时间',
    update_by       varchar(64)                     null comment '修改人',
    update_time     datetime                        null comment '修改时间'
)
    comment '入库单退货详情' collate = utf8mb4_general_ci
                             row_format = DYNAMIC;

INSERT INTO wms.stock_in_return_detail (detail_id, warehouse_code, return_no, line_no, label_id, mat_code, mat_name, fd_code, fig_num, mat_group, mat_class, unit_code, batch, quantity, return_quantity, location_code, supplier_code, supplier_name, del_flag, create_by, create_time, update_by, update_time) VALUES (101, 'WH-A04', 'TRT20260407001', 1, 109, 'T-FL003', '着色剂A型', 'TL003', 'HL-ZS-A', 'FL', 'HL', 'KG', 'TB20260402-003', 700.000000, 100.000000, 'FA-01', 'S-004', '南京化工材料有限公司', '0', 'admin', '2026-04-07 09:00:00', null, null);
INSERT INTO wms.stock_in_return_detail (detail_id, warehouse_code, return_no, line_no, label_id, mat_code, mat_name, fd_code, fig_num, mat_group, mat_class, unit_code, batch, quantity, return_quantity, location_code, supplier_code, supplier_name, del_flag, create_by, create_time, update_by, update_time) VALUES (102, 'WH-A01', 'TRT20260407002', 1, 105, 'T-YL003', '石灰石粉A级', 'TY003', 'LZ-FN-A', 'YL', 'LZ', 'KG', 'TB20260401-005', 12000.000000, 500.000000, 'RA-03', 'S-003', '邯郸石灰石矿业集团', '0', 'admin', '2026-04-07 14:00:00', null, null);
