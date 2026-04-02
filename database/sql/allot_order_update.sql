-- 调拨流程改造数据库更新脚本
-- 执行此脚本前请确认备份数据库
ALTER TABLE stock_in_order ADD COLUMN warehouse_code varchar(64) DEFAULT NULL COMMENT '仓库编码';
ALTER TABLE stock_in_order ADD COLUMN allot_no varchar(64) DEFAULT NULL COMMENT '调拨单号';

-- 为 stock_out_order 表添加调拨相关字段
ALTER TABLE stock_out_order ADD COLUMN allot_no VARCHAR(32) COMMENT '关联调拨单号';
ALTER TABLE stock_out_order ADD COLUMN dest_warehouse_code VARCHAR(32) COMMENT '目标仓库编码(调拨出库时使用)';

-- 为 stock_in_order 表添加调拨相关字段
ALTER TABLE stock_in_order ADD COLUMN allot_no VARCHAR(32) COMMENT '关联调拨单号';

-- 为 stock_out_detail 表添加供应商字段（如果不存在）
ALTER TABLE stock_out_detail ADD COLUMN supplier_code VARCHAR(32) COMMENT '供应商编码';
ALTER TABLE stock_out_detail ADD COLUMN supplier_name VARCHAR(64) COMMENT '供应商名称';


-- 1. 为stock_in_order表添加warehouse_code字段
ALTER TABLE stock_in_order ADD COLUMN warehouse_code varchar(64) DEFAULT NULL COMMENT '仓库编码';
ALTER TABLE stock_in_order ADD COLUMN warehouse_name varchar(64) DEFAULT NULL COMMENT '仓库名称';

-- 添加索引（可选，提高查询性能）
CREATE INDEX idx_out_order_allot_no ON stock_out_order(allot_no);
CREATE INDEX idx_in_order_allot_no ON stock_in_order(allot_no);