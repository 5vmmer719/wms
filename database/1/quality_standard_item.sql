create table quality_standard_item
(
    item_id        bigint auto_increment comment '主键'
        primary key,
    standard_code  varchar(30)              not null comment '所属检验标准编码',
    item_no        int                      not null comment '序号',
    item_name      varchar(100)             not null comment '检验项名称（透光率/平整度/厚度偏差）',
    item_unit      varchar(20)              null comment '单位',
    standard_value varchar(50)              null comment '标准值',
    min_value      decimal(12, 4)           null comment '下限',
    max_value      decimal(12, 4)           null comment '上限',
    check_method   varchar(200)             null comment '检验方法',
    is_key         char         default '0' null comment '是否关键项（0否 1是）',
    del_flag       char         default '0' null comment '删除标识（0存在 1删除）',
    create_by      varchar(64)  default ''  null comment '创建者',
    create_time    datetime                 null comment '创建时间',
    update_by      varchar(64)  default ''  null comment '更新者',
    update_time    datetime                 null comment '更新时间',
    remark         varchar(500) default ''  null comment '备注'
)
    comment '检验项目表' collate = utf8mb4_general_ci
                         row_format = DYNAMIC;

INSERT INTO wms.quality_standard_item (item_id, standard_code, item_no, item_name, item_unit, standard_value, min_value, max_value, check_method, is_key, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'QS-001', 1, '厚度偏差', 'mm', '±0.2', -0.2000, 0.2000, '千分尺多点测量取平均值', '1', '0', 'admin', '2026-04-12 02:50:14', '', null, '5mm厚度玻璃的允许偏差');
INSERT INTO wms.quality_standard_item (item_id, standard_code, item_no, item_name, item_unit, standard_value, min_value, max_value, check_method, is_key, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, 'QS-001', 2, '可见光透射比', '%', '≥89', 89.0000, null, '分光光度计测量', '1', '0', 'admin', '2026-04-12 02:50:14', '', null, '透明浮法玻璃标准');
INSERT INTO wms.quality_standard_item (item_id, standard_code, item_no, item_name, item_unit, standard_value, min_value, max_value, check_method, is_key, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (3, 'QS-001', 3, '弯曲度', '%', '≤0.2', null, 0.2000, '弧高法测量', '1', '0', 'admin', '2026-04-12 02:50:14', '', null, '弓形弯曲度');
INSERT INTO wms.quality_standard_item (item_id, standard_code, item_no, item_name, item_unit, standard_value, min_value, max_value, check_method, is_key, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (4, 'QS-001', 4, '气泡', '个/m²', '≤3', null, 3.0000, '目视检查，灯箱透射', '0', '0', 'admin', '2026-04-12 02:50:14', '', null, '直径>0.3mm的气泡数');
INSERT INTO wms.quality_standard_item (item_id, standard_code, item_no, item_name, item_unit, standard_value, min_value, max_value, check_method, is_key, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (5, 'QS-001', 5, '划伤', '条/m²', '≤2', null, 2.0000, '目视检查', '0', '0', 'admin', '2026-04-12 02:50:14', '', null, '长度>30mm的划伤数');
INSERT INTO wms.quality_standard_item (item_id, standard_code, item_no, item_name, item_unit, standard_value, min_value, max_value, check_method, is_key, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (6, 'QS-001', 6, '尺寸偏差', 'mm', '±1.0', -1.0000, 1.0000, '钢卷尺测量', '0', '0', 'admin', '2026-04-12 02:50:14', '', null, '长宽尺寸允许偏差');
INSERT INTO wms.quality_standard_item (item_id, standard_code, item_no, item_name, item_unit, standard_value, min_value, max_value, check_method, is_key, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (7, 'QS-002', 1, 'SiO₂含量', '%', '≥99.5', 99.5000, null, '化学分析法', '1', '0', 'admin', '2026-04-12 02:50:14', '', null, '石英砂纯度要求');
INSERT INTO wms.quality_standard_item (item_id, standard_code, item_no, item_name, item_unit, standard_value, min_value, max_value, check_method, is_key, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (8, 'QS-002', 2, 'Fe₂O₃含量', 'ppm', '≤100', null, 100.0000, '光谱分析法', '1', '0', 'admin', '2026-04-12 02:50:14', '', null, '铁含量影响玻璃颜色');
INSERT INTO wms.quality_standard_item (item_id, standard_code, item_no, item_name, item_unit, standard_value, min_value, max_value, check_method, is_key, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (9, 'QS-002', 3, '粒度', 'mesh', '40-140', 40.0000, 140.0000, '筛分法', '0', '0', 'admin', '2026-04-12 02:50:14', '', null, '颗粒度范围');
INSERT INTO wms.quality_standard_item (item_id, standard_code, item_no, item_name, item_unit, standard_value, min_value, max_value, check_method, is_key, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (10, 'QS-002', 4, '水分', '%', '≤5', null, 5.0000, '烘干称重法', '0', '0', 'admin', '2026-04-12 02:50:14', '', null, '含水率上限');
