package com.ruoyi.stock.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 交付记录对象 stock_delivery_record
 *
 * @author wms
 */
@Data
public class StockDeliveryRecord extends BaseEntity {

    /** 交付ID */
    private Long deliveryId;

    /** 交付单号 */
    @Excel(name = "交付单号")
    private String deliveryNo;

    /** 客户订单号 */
    @Excel(name = "客户订单号")
    private String orderNo;

    /** 客户编码 */
    @Excel(name = "客户编码")
    private String customerCode;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 交付日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交付日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveryDate;

    /** 物流单号 */
    @Excel(name = "物流单号")
    private String logisticsNo;

    /** 物流公司 */
    @Excel(name = "物流公司")
    private String logisticsCompany;

    /** 交付地址 */
    @Excel(name = "交付地址")
    private String deliveryAddress;

    /** 状态 */
    @Excel(name = "状态")
    private String deliveryStatus;

    private String deliveryStatusLabel;

    /** 关联出库单号 */
    @Excel(name = "出库单号")
    private String outOrderNo;

    /** 交付总数量 */
    @Excel(name = "交付总数量")
    private BigDecimal totalQuantity;

    /** 删除标志 */
    private String delFlag;

    /** 交付明细列表（非持久化） */
    private List<StockDeliveryDetail> detailList;
}

