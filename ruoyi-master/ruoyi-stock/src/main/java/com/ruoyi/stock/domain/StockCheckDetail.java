package com.ruoyi.stock.domain;

import java.math.BigDecimal;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 盘点明细对象 stock_check_detail
 *
 * @author wms
 */
@Data
public class StockCheckDetail extends BaseEntity {

    /** 明细ID */
    private Long detailId;

    /** 盘点单号 */
    private String checkNo;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String matCode;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String matName;

    /** 仓库编码 */
    private String warehouseCode;

    /** 货位编码 */
    @Excel(name = "货位")
    private String locationCode;

    /** 批次 */
    @Excel(name = "批次")
    private String batch;

    /** 单位 */
    private String unitCode;

    /** 系统数量（快照） */
    @Excel(name = "系统数量")
    private BigDecimal systemQty;

    /** 实盘数量 */
    @Excel(name = "实盘数量")
    private BigDecimal actualQty;

    /** 差异数量（实盘-系统） */
    @Excel(name = "差异数量")
    private BigDecimal diffQty;

    /** 差异原因 */
    @Excel(name = "差异原因")
    private String diffReason;

    /** 是否已调整（0否 1是） */
    private String adjustFlag;

    /** 删除标志 */
    private String delFlag;
}

