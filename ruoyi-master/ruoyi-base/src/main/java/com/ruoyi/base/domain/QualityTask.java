package com.ruoyi.base.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 检验任务对象 quality_task
 *
 * @author summer
 * @date 2026-04-11
 */
@Data
public class QualityTask extends BaseEntity {

    /** 主键 */
    private Long taskId;

    /** 检验任务编号 */
    @Excel(name = "任务编号")
    private String taskNo;

    /** 检验类型（incoming/process/final） */
    @Excel(name = "检验类型")
    private String checkType;

    /** 来源类型（in_order/prod_order） */
    @Excel(name = "来源类型")
    private String sourceType;

    /** 来源单号 */
    @Excel(name = "来源单号")
    private String sourceNo;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String matCode;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String matName;

    /** 批次 */
    @Excel(name = "批次")
    private String batch;

    /** 送检数量 */
    @Excel(name = "送检数量")
    private BigDecimal quantity;

    /** 检验标准编码 */
    @Excel(name = "检验标准编码")
    private String standardCode;

    /** 检验标准名称 */
    @Excel(name = "检验标准名称")
    private String standardName;

    /** 质检员ID */
    private Long inspectorId;

    /** 质检员姓名 */
    @Excel(name = "质检员")
    private String inspectorName;

    /** 状态（pending待检验/checking检验中/passed合格/failed不合格） */
    @Excel(name = "状态")
    private String taskStatus;

    /** 状态标签（非持久化） */
    private String taskStatusLabel;

    /** 合格数量 */
    @Excel(name = "合格数量")
    private BigDecimal qualifiedQty;

    /** 不合格数量 */
    @Excel(name = "不合格数量")
    private BigDecimal unqualifiedQty;

    /** 检验完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "检验完成时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;

    /** 删除标识 */
    private String delFlag;

    /** 检验结果明细列表（非持久化，级联查询时加载） */
    private List<QualityTaskResult> resultList;
}

