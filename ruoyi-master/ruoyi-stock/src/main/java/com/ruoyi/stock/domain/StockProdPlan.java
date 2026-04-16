package com.ruoyi.stock.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 生产计划对象 stock_prod_plan
 *
 * @author wms
 */
@Data
public class StockProdPlan extends BaseEntity {

    /** 计划ID */
    private Long planId;

    /** 计划编号 */
    @Excel(name = "计划编号")
    private String planNo;

    /** 计划名称 */
    @Excel(name = "计划名称")
    private String planName;

    /** 计划类型（monthly月度/weekly周度） */
    @Excel(name = "计划类型")
    private String planType;

    /** 计划开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planStartDate;

    /** 计划结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planEndDate;

    /** 产品物料编码 */
    @Excel(name = "物料编码")
    private String matCode;

    /** 产品名称 */
    @Excel(name = "物料名称")
    private String matName;

    /** 计划生产数量 */
    @Excel(name = "计划数量")
    private BigDecimal planQuantity;

    /** 实际完成数量 */
    @Excel(name = "实际完成数量")
    private BigDecimal actualQuantity;

    /** 关联客户订单号 */
    @Excel(name = "客户订单号")
    private String customerOrderNo;

    /** 生产车间编码 */
    @Excel(name = "车间编码")
    private String workshopCode;

    /** 车间名称（非持久化） */
    private String workshopName;

    /** 计划状态（draft/confirmed/executing/completed/cancelled） */
    @Excel(name = "状态")
    private String planStatus;

    /** 状态中文标签（非持久化） */
    private String planStatusLabel;

    /** 完成率（%） */
    @Excel(name = "完成率")
    private BigDecimal completionRate;

    /** 删除标识 */
    private String delFlag;

    /** ===== 非持久化统计字段 ===== */

    /** 关联工单总数 */
    private Integer totalOrderCount;

    /** 已完工工单数 */
    private Integer completedOrderCount;

    /** 生产中工单数 */
    private Integer ongoingOrderCount;

    /** 待排产工单数 */
    private Integer plannedOrderCount;

}

