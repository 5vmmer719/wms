create table sys_user
(
    user_id     bigint auto_increment comment '用户ID'
        primary key,
    dept_id     bigint                    null comment '部门ID',
    user_name   varchar(30)               not null comment '用户账号',
    nick_name   varchar(30)               not null comment '用户昵称',
    user_type   varchar(2)   default '00' null comment '用户类型（00系统用户）',
    email       varchar(50)  default ''   null comment '用户邮箱',
    phonenumber varchar(11)  default ''   null comment '手机号码',
    sex         char         default '0'  null comment '用户性别（0男 1女 2未知）',
    avatar      varchar(100) default ''   null comment '头像地址',
    password    varchar(100) default ''   null comment '密码',
    status      char         default '0'  null comment '帐号状态（0正常 1停用）',
    del_flag    char         default '0'  null comment '删除标志（0代表存在 2代表删除）',
    login_ip    varchar(128) default ''   null comment '最后登录IP',
    login_date  datetime                  null comment '最后登录时间',
    create_by   varchar(64)  default ''   null comment '创建者',
    create_time datetime                  null comment '创建时间',
    update_by   varchar(64)  default ''   null comment '更新者',
    update_time datetime                  null comment '更新时间',
    remark      varchar(500)              null comment '备注'
)
    comment '用户信息表' collate = utf8mb4_general_ci
                         row_format = DYNAMIC;

INSERT INTO wms.sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark) VALUES (1, 201, 'admin', '管理员', '00', 'zt18519100654@163.com', '18519100654', '0', '/profile/avatar/2022/09/12/blob_20220912082155A001.jpeg', '$2a$10$76b1MOVniaux2ACn9XYM4u0C2dLgvJqjNWWR2TGQzLKBaPdak6m/K', '0', '0', '127.0.0.1', '2026-04-13 12:40:24', 'admin', '2022-07-10 01:24:55', '', '2026-04-13 12:40:24', '管理员');
INSERT INTO wms.sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark) VALUES (100, 100, 'jtom', 'jtom', '00', '397343331@qq.com', '18519100653', '0', '', '$2a$10$VvEBI93MJ1XQyf3z9fGVj./Kxp1gLk4WJWCfqJtc8TzwGK3AcoKu.', '1', '2', '127.0.0.1', '2022-09-10 09:00:17', 'admin', '2022-08-11 01:20:51', 'admin', '2026-03-31 17:24:19', null);
INSERT INTO wms.sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark) VALUES (101, 100, 'stockkeeper', '库管员', '00', '', '', '0', '', '$2a$10$76b1MOVniaux2ACn9XYM4u0C2dLgvJqjNWWR2TGQzLKBaPdak6m/K', '0', '0', '127.0.0.1', '2026-03-31 15:19:07', 'admin', '2022-07-10 01:24:55', '', '2026-03-31 15:19:07', '库管员账号，初始密码admin123');
INSERT INTO wms.sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark) VALUES (102, 100, 'inspector', '质检员', '00', '', '', '0', '', '$2a$10$76b1MOVniaux2ACn9XYM4u0C2dLgvJqjNWWR2TGQzLKBaPdak6m/K', '0', '0', '127.0.0.1', '2026-03-31 15:24:05', 'admin', '2022-07-10 01:24:55', '', '2026-03-31 15:24:04', '质检员账号，初始密码admin123');
INSERT INTO wms.sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark) VALUES (103, 100, 'planner', '计划员', '00', '', '', '0', '', '$2a$10$76b1MOVniaux2ACn9XYM4u0C2dLgvJqjNWWR2TGQzLKBaPdak6m/K', '0', '0', '127.0.0.1', '2026-03-31 15:17:19', 'admin', '2022-07-10 01:24:55', '', '2026-03-31 15:17:19', '计划员账号，初始密码admin123');
