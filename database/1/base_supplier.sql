create table base_supplier
(
    supplier_id   bigint auto_increment comment '主键'
        primary key,
    supplier_code varchar(64)      null comment '供应商编码',
    supplier_name varchar(128)     null comment '供应商名称',
    supply_type   varchar(128)     null comment '供货名称',
    address       varchar(128)     null comment '地址',
    contact       varchar(64)      null comment '联系方式',
    tax_number    varchar(128)     null comment '税号',
    deposit_bank  varchar(128)     null comment '开户行',
    bank_account  varchar(128)     null comment '账号',
    is_qualified  char default '0' null comment '是否合格供应商',
    city          varchar(32)      null comment '城市',
    postal_code   varchar(64)      null comment '邮政编码',
    del_flag      char default '0' null comment '删除标识',
    create_by     varchar(64)      null comment '创建人',
    create_time   datetime         null comment '创建时间',
    update_by     varchar(64)      null comment '修改人',
    update_time   datetime         null comment '修改时间'
)
    comment '供应商' collate = utf8mb4_general_ci
                     row_format = DYNAMIC;

INSERT INTO wms.base_supplier (supplier_id, supplier_code, supplier_name, supply_type, address, contact, tax_number, deposit_bank, bank_account, is_qualified, city, postal_code, del_flag, create_by, create_time, update_by, update_time) VALUES (101, 'S-001', '青岛纯碱工业有限公司', '纯碱', '山东省青岛市城阳区化工园区', '孙经理 13800001001', '37021400000101', '中国银行青岛分行', '620001001001', 'Y', '青岛', '266109', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_supplier (supplier_id, supplier_code, supplier_name, supply_type, address, contact, tax_number, deposit_bank, bank_account, is_qualified, city, postal_code, del_flag, create_by, create_time, update_by, update_time) VALUES (102, 'S-002', '凤阳石英砂矿业公司', '石英砂', '安徽省滁州市凤阳县矿业路88号', '周总 13800001002', '34112600000102', '工商银行凤阳支行', '620001001002', 'Y', '滁州', '233100', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_supplier (supplier_id, supplier_code, supplier_name, supply_type, address, contact, tax_number, deposit_bank, bank_account, is_qualified, city, postal_code, del_flag, create_by, create_time, update_by, update_time) VALUES (103, 'S-003', '邯郸石灰石矿业集团', '石灰石', '河北省邯郸市武安市石矿路', '马经理 13800001003', '13048100000103', '建设银行邯郸分行', '620001001003', 'Y', '邯郸', '056300', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_supplier (supplier_id, supplier_code, supplier_name, supply_type, address, contact, tax_number, deposit_bank, bank_account, is_qualified, city, postal_code, del_flag, create_by, create_time, update_by, update_time) VALUES (104, 'S-004', '南京化工材料有限公司', '化工辅料', '江苏省南京市江宁区化工园区', '钱经理 13800001004', '32011500000104', '招商银行南京分行', '620001001004', 'Y', '南京', '211100', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_supplier (supplier_id, supplier_code, supplier_name, supply_type, address, contact, tax_number, deposit_bank, bank_account, is_qualified, city, postal_code, del_flag, create_by, create_time, update_by, update_time) VALUES (105, 'S-005', '佛山精密模具配件厂', '模具配件', '广东省佛山市南海区工业园', '吴经理 13800001005', '44060500000105', '农业银行佛山分行', '620001001005', 'Y', '佛山', '528200', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_supplier (supplier_id, supplier_code, supplier_name, supply_type, address, contact, tax_number, deposit_bank, bank_account, is_qualified, city, postal_code, del_flag, create_by, create_time, update_by, update_time) VALUES (106, 'S-006', '天津纸箱包装有限公司', '纸箱包装', '天津市武清区包装工业园', '郑经理 13800001006', '12011400000106', '交通银行天津分行', '620001001006', 'Y', '天津', '301700', '0', 'admin', '2026-04-01 08:00:00', null, null);
INSERT INTO wms.base_supplier (supplier_id, supplier_code, supplier_name, supply_type, address, contact, tax_number, deposit_bank, bank_account, is_qualified, city, postal_code, del_flag, create_by, create_time, update_by, update_time) VALUES (107, 'S-007', '济南塑料包装有限公司', '塑料包装', '山东省济南市历城区塑料工业园', '冯总 13800001007', '37011200000107', '中信银行济南分行', '620001001007', 'Y', '济南', '250100', '0', 'admin', '2026-04-01 08:00:00', null, null);
