package com.ruoyi.base.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 不合格品处理对象 quality_defect_handle
 *
 * @author summer
 * @date 2026-04-11
 */
@Data
public class QualityDefectHandle extends BaseEntity {

    /** 主键 */
    private Long handleId;

    /** 关联检验任务编号 */
    @Excel(name = "检验任务编号")
    private String taskNo;

    /** 处理方式（rework返工/scrap报废/concession让步接收） */
    @Excel(name = "处理方式")
    private String handleType;

    /** 处理数量 */
    @Excel(name = "处理数量")
    private BigDecimal handleQty;

    /** 处理说明 */
    @Excel(name = "处理说明")
    private String handleDesc;

    /** 处理人 */
    @Excel(name = "处理人")
    private String handleBy;

    /** 处理日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "处理日期", dateFormat = "yyyy-MM-dd")
    private Date handleDate;

    /** 状态（pending待处理/processing处理中/completed已完成） */
    @Excel(name = "状态")
    private String handleStatus;

    /** 状态标签（非持久化） */
    private String handleStatusLabel;

    /** 删除标识 */
    private String delFlag;

    /** 检验任务信息（非持久化，关联查询时加载） */
    private String matCode;
    private String matName;
    private String checkType;
    private BigDecimal quantity;
    private BigDecimal unqualifiedQty;
}

