create table base_customer
(
    customer_id     bigint auto_increment comment '客户ID'
        primary key,
    customer_code   varchar(30)              not null comment '客户编码',
    customer_name   varchar(100)             not null comment '客户名称',
    contact_person  varchar(50)              null comment '联系人',
    contact_phone   varchar(20)              null comment '联系电话',
    email           varchar(100)             null comment '邮箱',
    address         varchar(200)             null comment '地址',
    customer_level  varchar(10)  default 'B' null comment '客户等级（A/B/C）',
    customer_status char         default '0' null comment '状态（0正常 1停用）',
    del_flag        char         default '0' null comment '删除标识（0存在 1删除）',
    create_by       varchar(64)  default ''  null comment '创建者',
    create_time     datetime                 null comment '创建时间',
    update_by       varchar(64)  default ''  null comment '更新者',
    update_time     datetime                 null comment '更新时间',
    remark          varchar(500) default ''  null comment '备注'
)
    comment '客户表';

INSERT INTO wms.base_customer (customer_id, customer_code, customer_name, contact_person, contact_phone, email, address, customer_level, customer_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, 'CUS-001', '华南建材集团', '张经理', '13800138001', 'zhang@hnbuilding.com', '广东省广州市天河区建材路88号', 'A', '0', '0', 'admin', '2026-04-12 00:34:38', '', null, 'VIP客户，长期合作');
INSERT INTO wms.base_customer (customer_id, customer_code, customer_name, contact_person, contact_phone, email, address, customer_level, customer_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, 'CUS-002', '东方幕墙工程有限公司', '李总', '13900139002', 'li@dfmq.com', '上海市浦东新区金桥路66号', 'A', '0', '0', 'admin', '2026-04-12 00:34:38', '', null, '幕墙工程大客户');
INSERT INTO wms.base_customer (customer_id, customer_code, customer_name, contact_person, contact_phone, email, address, customer_level, customer_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (3, 'CUS-003', '中原装饰材料批发', '王老板', '13700137003', null, '河南省郑州市二七区建材市场A区', 'B', '0', '0', 'admin', '2026-04-12 00:34:38', 'admin', '2026-04-12 00:47:33', '批发渠道客户');
INSERT INTO wms.base_customer (customer_id, customer_code, customer_name, contact_person, contact_phone, email, address, customer_level, customer_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (4, 'CUS-004', '西南家居连锁', '赵经理', '13600136004', 'zhao@xnjj.com', '四川省成都市武侯区家居大道12号', 'B', '0', '0', 'admin', '2026-04-12 00:34:38', '', null, '家居零售渠道');
INSERT INTO wms.base_customer (customer_id, customer_code, customer_name, contact_person, contact_phone, email, address, customer_level, customer_status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (5, 'CUS-005', '北方建筑设计院', '刘工', '13500135005', 'liu@bfjz.com', '北京市朝阳区建国路99号', 'C', '0', '0', 'admin', '2026-04-12 00:34:38', '', null, '设计院样品客户');
