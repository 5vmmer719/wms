package com.ruoyi.stock.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 盘点单对象 stock_check_order
 *
 * @author wms
 */
@Data
public class StockCheckOrder extends BaseEntity {

    /** 盘点ID */
    private Long checkId;

    /** 盘点单号 */
    @Excel(name = "盘点单号")
    private String checkNo;

    /** 盘点类型（full全盘/cycle循环盘/spot抽盘） */
    @Excel(name = "盘点类型")
    private String checkType;

    /** 盘点仓库编码 */
    @Excel(name = "仓库编码")
    private String warehouseCode;

    /** 盘点仓库名称 */
    @Excel(name = "仓库名称")
    private String warehouseName;

    /** 状态（created已创建/counting盘点中/completed已完成/adjusted已调整） */
    @Excel(name = "状态")
    private String checkStatus;

    /** 计划盘点日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划盘点日期", dateFormat = "yyyy-MM-dd")
    private Date planDate;

    /** 实际盘点日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际盘点日期", dateFormat = "yyyy-MM-dd")
    private Date actualDate;

    /** 盘点人ID */
    private Long checkerId;

    /** 盘点人 */
    @Excel(name = "盘点人")
    private String checkerName;

    /** 盘点物料总数 */
    private Integer totalItems;

    /** 差异物料数 */
    private Integer diffItems;

    /** 删除标志 */
    private String delFlag;

    /** 盘点明细列表（非持久化） */
    private List<StockCheckDetail> detailList;
}

