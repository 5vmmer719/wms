package com.ruoyi.stock.domain;

import java.math.BigDecimal;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 交付明细对象 stock_delivery_detail
 *
 * @author wms
 */
@Data
public class StockDeliveryDetail extends BaseEntity {

    /** 明细ID */
    private Long detailId;

    /** 交付单号 */
    private String deliveryNo;

    /** 行号 */
    private Integer lineNo;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String matCode;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String matName;

    /** 规格 */
    @Excel(name = "规格")
    private String spec;

    /** 交付数量 */
    @Excel(name = "交付数量")
    private BigDecimal quantity;

    /** 单位 */
    @Excel(name = "单位")
    private String unitCode;

    /** 删除标志 */
    private String delFlag;
}

