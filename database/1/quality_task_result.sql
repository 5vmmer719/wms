create table quality_task_result
(
    result_id      bigint auto_increment comment '主键'
        primary key,
    task_no        varchar(30)              not null comment '所属任务编号',
    item_no        int                      not null comment '检验项序号',
    item_name      varchar(100)             null comment '检验项名称',
    standard_value varchar(50)              null comment '标准值',
    actual_value   varchar(50)              null comment '实测值',
    min_value      decimal(12, 4)           null comment '下限',
    max_value      decimal(12, 4)           null comment '上限',
    judge_result   char                     null comment '判定（0合格 1不合格）',
    defect_type    varchar(50)              null comment '缺陷类型（气泡/划痕/尺寸偏差）',
    defect_level   varchar(20)              null comment '缺陷等级（minor轻微/major严重/critical致命）',
    del_flag       char         default '0' null comment '删除标识（0存在 1删除）',
    create_by      varchar(64)  default ''  null comment '创建者',
    create_time    datetime                 null comment '创建时间',
    update_by      varchar(64)  default ''  null comment '更新者',
    update_time    datetime                 null comment '更新时间',
    remark         varchar(500) default ''  null comment '备注'
)
    comment '检验结果明细表' collate = utf8mb4_general_ci
                             row_format = DYNAMIC;

INSERT INTO wms.quality_task_result (result_id, task_no, item_no, item_name, standard_value, actual_value, min_value, max_value, judge_result, defect_type, defect_level, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'QT20260412002', 1, '厚度偏差', '±0.2', '1', -0.2000, 0.2000, '1', null, null, '1', 'admin', '2026-04-12 03:19:56', '', null, '');
INSERT INTO wms.quality_task_result (result_id, task_no, item_no, item_name, standard_value, actual_value, min_value, max_value, judge_result, defect_type, defect_level, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, 'QT20260412002', 2, '可见光透射比', '≥89', null, 89.0000, null, null, null, null, '1', 'admin', '2026-04-12 03:19:56', '', null, '');
INSERT INTO wms.quality_task_result (result_id, task_no, item_no, item_name, standard_value, actual_value, min_value, max_value, judge_result, defect_type, defect_level, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (3, 'QT20260412002', 3, '弯曲度', '≤0.2', null, null, 0.2000, null, null, null, '1', 'admin', '2026-04-12 03:19:56', '', null, '');
INSERT INTO wms.quality_task_result (result_id, task_no, item_no, item_name, standard_value, actual_value, min_value, max_value, judge_result, defect_type, defect_level, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (4, 'QT20260412002', 4, '气泡', '≤3', null, null, 3.0000, null, null, null, '1', 'admin', '2026-04-12 03:19:56', '', null, '');
INSERT INTO wms.quality_task_result (result_id, task_no, item_no, item_name, standard_value, actual_value, min_value, max_value, judge_result, defect_type, defect_level, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (5, 'QT20260412002', 5, '划伤', '≤2', null, null, 2.0000, null, null, null, '1', 'admin', '2026-04-12 03:19:56', '', null, '');
INSERT INTO wms.quality_task_result (result_id, task_no, item_no, item_name, standard_value, actual_value, min_value, max_value, judge_result, defect_type, defect_level, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (6, 'QT20260412002', 6, '尺寸偏差', '±1.0', null, -1.0000, 1.0000, null, null, null, '1', 'admin', '2026-04-12 03:19:56', '', null, '');
INSERT INTO wms.quality_task_result (result_id, task_no, item_no, item_name, standard_value, actual_value, min_value, max_value, judge_result, defect_type, defect_level, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (7, 'QT20260413001', 1, 'SiO₂含量', '≥99.5', '100', 99.5000, null, '0', null, null, '0', 'admin', '2026-04-13 12:52:39', '', null, '');
INSERT INTO wms.quality_task_result (result_id, task_no, item_no, item_name, standard_value, actual_value, min_value, max_value, judge_result, defect_type, defect_level, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (8, 'QT20260413001', 2, 'Fe₂O₃含量', '≤100', '20', null, 100.0000, '0', null, null, '0', 'admin', '2026-04-13 12:52:39', '', null, '');
INSERT INTO wms.quality_task_result (result_id, task_no, item_no, item_name, standard_value, actual_value, min_value, max_value, judge_result, defect_type, defect_level, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (9, 'QT20260413001', 3, '粒度', '40-140', '100', 40.0000, 140.0000, '0', null, null, '0', 'admin', '2026-04-13 12:52:39', '', null, '');
INSERT INTO wms.quality_task_result (result_id, task_no, item_no, item_name, standard_value, actual_value, min_value, max_value, judge_result, defect_type, defect_level, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (10, 'QT20260413001', 4, '水分', '≤5', '6', null, 5.0000, '1', null, null, '0', 'admin', '2026-04-13 12:52:39', '', null, '');
