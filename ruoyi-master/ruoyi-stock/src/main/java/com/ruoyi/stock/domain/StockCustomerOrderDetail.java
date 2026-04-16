package com.ruoyi.stock.domain;

import java.math.BigDecimal;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户订单明细对象 stock_customer_order_detail
 *
 * @author wms
 */
@Data
public class StockCustomerOrderDetail extends BaseEntity {

    /** 明细ID */
    private Long detailId;

    /** 客户订单号 */
    @Excel(name = "订单号")
    private String orderNo;

    /** 行号 */
    @Excel(name = "行号")
    private Integer lineNo;

    /** 产品物料编码 */
    @Excel(name = "物料编码")
    private String matCode;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String matName;

    /** 规格 */
    @Excel(name = "规格")
    private String spec;

    /** 订单数量 */
    @Excel(name = "订单数量")
    private BigDecimal quantity;

    /** 已交付数量 */
    @Excel(name = "已交付数量")
    private BigDecimal deliveredQty;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal unitPrice;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /** 关联生产工单号 */
    @Excel(name = "生产工单号")
    private String prodOrderNo;

    /** 单位（关联物料表） */
    private String unitCode;

    /** 图号（关联物料表） */
    private String figNum;

    /** 删除标识 */
    private String delFlag;

}

