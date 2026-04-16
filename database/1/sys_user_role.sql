create table sys_user_role
(
    user_id bigint not null comment '用户ID',
    role_id bigint not null comment '角色ID',
    primary key (user_id, role_id)
)
    comment '用户和角色关联表' collate = utf8mb4_general_ci
                               row_format = DYNAMIC;

INSERT INTO wms.sys_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO wms.sys_user_role (user_id, role_id) VALUES (101, 3);
INSERT INTO wms.sys_user_role (user_id, role_id) VALUES (102, 4);
INSERT INTO wms.sys_user_role (user_id, role_id) VALUES (103, 5);
