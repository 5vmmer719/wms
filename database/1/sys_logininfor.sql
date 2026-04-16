create table sys_logininfor
(
    info_id        bigint auto_increment comment '访问ID'
        primary key,
    user_name      varchar(50)  default ''  null comment '用户账号',
    ipaddr         varchar(128) default ''  null comment '登录IP地址',
    login_location varchar(255) default ''  null comment '登录地点',
    browser        varchar(50)  default ''  null comment '浏览器类型',
    os             varchar(50)  default ''  null comment '操作系统',
    status         char         default '0' null comment '登录状态（0成功 1失败）',
    msg            varchar(255) default ''  null comment '提示消息',
    login_time     datetime                 null comment '访问时间'
)
    comment '系统访问记录' collate = utf8mb4_general_ci
                           row_format = DYNAMIC;

INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (853, 'planner', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '退出成功', '2026-03-31 15:17:16');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (854, 'planner', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-03-31 15:17:19');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (855, 'planner', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '退出成功', '2026-03-31 15:17:31');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (856, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-03-31 15:17:38');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (857, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '退出成功', '2026-03-31 15:17:59');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (858, 'inspector', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-03-31 15:18:04');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (859, 'inspector', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '退出成功', '2026-03-31 15:18:46');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (860, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-03-31 15:18:52');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (861, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '退出成功', '2026-03-31 15:19:01');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (862, 'stockkeeper', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-03-31 15:19:07');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (863, 'stockkeeper', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '退出成功', '2026-03-31 15:21:07');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (864, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-03-31 15:21:16');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (865, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '退出成功', '2026-03-31 15:21:35');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (866, 'inspector', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-03-31 15:21:40');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (867, 'inspector', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '退出成功', '2026-03-31 15:22:30');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (868, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-03-31 15:22:35');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (869, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '退出成功', '2026-03-31 15:23:58');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (870, 'inspector', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-03-31 15:24:04');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (871, 'inspector', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '退出成功', '2026-03-31 16:04:29');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (872, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '1', '验证码错误', '2026-03-31 16:04:38');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (873, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-03-31 16:04:41');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (874, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-04-01 18:08:58');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (875, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-04-07 15:48:28');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (876, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '1', '验证码已失效', '2026-04-08 16:47:47');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (877, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-04-08 16:47:50');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (878, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-04-09 17:19:27');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (879, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-04-11 19:01:04');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (880, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '退出成功', '2026-04-11 21:58:30');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (881, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-04-11 22:01:02');
INSERT INTO wms.sys_logininfor (info_id, user_name, ipaddr, login_location, browser, os, status, msg, login_time) VALUES (882, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Mac OS X', '0', '登录成功', '2026-04-13 12:40:24');
