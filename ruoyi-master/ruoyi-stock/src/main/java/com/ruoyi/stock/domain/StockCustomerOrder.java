package com.ruoyi.stock.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户订单对象 stock_customer_order
 *
 * @author wms
 */
@Data
public class StockCustomerOrder extends BaseEntity {

    /** 订单ID */
    private Long orderId;

    /** 客户订单号 */
    @Excel(name = "订单号")
    private String orderNo;

    /** 客户编码 */
    @Excel(name = "客户编码")
    private String customerCode;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 下单日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "下单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderDate;

    /** 要求交付日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "要求交付日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveryDate;

    /** 实际交付日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际交付日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualDeliveryDate;

    /** 状态（created/confirmed/producing/completed/delivered/closed） */
    @Excel(name = "状态")
    private String orderStatus;

    /** 状态中文标签（非持久化） */
    private String orderStatusLabel;

    /** 订单总金额 */
    @Excel(name = "订单总金额")
    private BigDecimal totalAmount;

    /** 删除标识 */
    private String delFlag;

    /** 订单明细列表（非持久化，用于主子表操作） */
    private List<StockCustomerOrderDetail> detailList;

}

