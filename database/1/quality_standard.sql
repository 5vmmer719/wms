create table quality_standard
(
    standard_id     bigint auto_increment comment '主键'
        primary key,
    standard_code   varchar(30)              not null comment '检验标准编码',
    standard_name   varchar(100)             not null comment '检验标准名称',
    check_type      varchar(20)              not null comment '检验类型（incoming原料检验/process过程检验/final成品检验）',
    mat_code        varchar(30)              null comment '关联物料编码',
    standard_status char         default '0' null comment '状态（0正常 1停用）',
    del_flag        char         default '0' null comment '删除标识（0存在 1删除）',
    create_by       varchar(64)  default ''  null comment '创建者',
    create_time     datetime                 null comment '创建时间',
    update_by       varchar(64)  default ''  null comment '更新者',
    update_time     datetime                 null comment '更新时间',
    remark          varchar(500) default ''  null comment '备注'
)
    comment '检验标准表' collate = utf8mb4_general_ci
                         row_format = DYNAMIC;

INSERT INTO wms.quality_standard (standard_id, standard_code, standard_name, check_type, mat_code, standard_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'QS-001', '浮法玻璃成品检验标准', 'final', null, '0', '0', 'admin', '2026-04-12 02:50:14', '', null, '依据GB 11614-2009《浮法玻璃》标准制定');
INSERT INTO wms.quality_standard (standard_id, standard_code, standard_name, check_type, mat_code, standard_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, 'QS-002', '石英砂原料检验标准', 'incoming', null, '0', '0', 'admin', '2026-04-12 02:50:14', '', null, '石英砂入库质量检验');
