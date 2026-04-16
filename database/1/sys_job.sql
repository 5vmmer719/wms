create table sys_job
(
    job_id          bigint auto_increment comment '任务ID',
    job_name        varchar(64)  default ''        not null comment '任务名称',
    job_group       varchar(64)  default 'DEFAULT' not null comment '任务组名',
    invoke_target   varchar(500)                   not null comment '调用目标字符串',
    cron_expression varchar(255) default ''        null comment 'cron执行表达式',
    misfire_policy  varchar(20)  default '3'       null comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
    concurrent      char         default '1'       null comment '是否并发执行（0允许 1禁止）',
    status          char         default '0'       null comment '状态（0正常 1暂停）',
    create_by       varchar(64)  default ''        null comment '创建者',
    create_time     datetime                       null comment '创建时间',
    update_by       varchar(64)  default ''        null comment '更新者',
    update_time     datetime                       null comment '更新时间',
    remark          varchar(500) default ''        null comment '备注信息',
    primary key (job_id, job_name, job_group)
)
    comment '定时任务调度表' collate = utf8mb4_general_ci
                             row_format = DYNAMIC;

INSERT INTO wms.sys_job (job_id, job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time, update_by, update_time, remark) VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2022-07-10 01:24:56', '', null, '');
INSERT INTO wms.sys_job (job_id, job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time, update_by, update_time, remark) VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2022-07-10 01:24:56', '', null, '');
INSERT INTO wms.sys_job (job_id, job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time, update_by, update_time, remark) VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2022-07-10 01:24:56', '', null, '');
INSERT INTO wms.sys_job (job_id, job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time, update_by, update_time, remark) VALUES (4, '库存预警检查', 'SYSTEM', 'stockWarningTask.checkStockWarning', '0 0/30 * * * ?', '3', '1', '0', 'admin', '2022-07-10 01:24:56', '', null, '每30分钟检查库存预警，库存低于安全库存时自动发送通知公告');
INSERT INTO wms.sys_job (job_id, job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time, update_by, update_time, remark) VALUES (5, '设备定期维护检查', 'DEFAULT', 'equipmentMaintainTask.autoCreateMaintain()', '0 0 1 * * ?', '3', '1', '0', 'admin', '2026-04-12 00:23:24', '', null, '每天凌晨1点扫描到期设备，自动创建维护单据');
INSERT INTO wms.sys_job (job_id, job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time, update_by, update_time, remark) VALUES (6, '订单异常预警检查', 'DEFAULT', 'orderWarningTask.checkOrderWarning()', '0 0 * * * ?', '3', '1', '0', 'admin', '2026-04-12 02:46:13', '', null, '每小时检查订单交付预警、生产延期预警、质检不合格预警');
