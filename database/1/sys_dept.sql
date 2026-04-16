create table sys_dept
(
    dept_id     bigint auto_increment comment '部门id'
        primary key,
    parent_id   bigint      default 0   null comment '父部门id',
    ancestors   varchar(50) default ''  null comment '祖级列表',
    dept_name   varchar(30) default ''  null comment '部门名称',
    order_num   int         default 0   null comment '显示顺序',
    leader      varchar(20)             null comment '负责人',
    phone       varchar(11)             null comment '联系电话',
    email       varchar(50)             null comment '邮箱',
    status      char        default '0' null comment '部门状态（0正常 1停用）',
    del_flag    char        default '0' null comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default ''  null comment '创建者',
    create_time datetime                null comment '创建时间',
    update_by   varchar(64) default ''  null comment '更新者',
    update_time datetime                null comment '更新时间'
)
    comment '部门表' collate = utf8mb4_general_ci
                     row_format = DYNAMIC;

INSERT INTO wms.sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time, update_by, update_time) VALUES (100, 0, '0', '总公司', 0, '老板', '15888888888', '123@qq.com', '0', '0', 'admin', '2022-07-10 01:24:55', '', null);
INSERT INTO wms.sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time, update_by, update_time) VALUES (200, 100, '0,100', '武汉总公司', 1, null, null, null, '0', '0', 'admin', '2022-07-23 05:23:15', '', null);
INSERT INTO wms.sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time, update_by, update_time) VALUES (201, 200, '0,100,200', '研发部', 1, null, null, null, '0', '0', 'admin', '2022-07-23 05:23:30', '', null);
