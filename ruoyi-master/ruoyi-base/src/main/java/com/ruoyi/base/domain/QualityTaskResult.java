package com.ruoyi.base.domain;

import java.math.BigDecimal;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 检验结果明细对象 quality_task_result
 *
 * @author summer
 * @date 2026-04-11
 */
@Data
public class QualityTaskResult extends BaseEntity {

    /** 主键 */
    private Long resultId;

    /** 所属任务编号 */
    @Excel(name = "任务编号")
    private String taskNo;

    /** 检验项序号 */
    @Excel(name = "序号")
    private Integer itemNo;

    /** 检验项名称 */
    @Excel(name = "检验项名称")
    private String itemName;

    /** 标准值 */
    @Excel(name = "标准值")
    private String standardValue;

    /** 实测值 */
    @Excel(name = "实测值")
    private String actualValue;

    /** 下限 */
    @Excel(name = "下限")
    private BigDecimal minValue;

    /** 上限 */
    @Excel(name = "上限")
    private BigDecimal maxValue;

    /** 判定（0合格 1不合格） */
    @Excel(name = "判定", readConverterExp = "0=合格,1=不合格")
    private String judgeResult;

    /** 缺陷类型 */
    @Excel(name = "缺陷类型")
    private String defectType;

    /** 缺陷等级（minor轻微/major严重/critical致命） */
    @Excel(name = "缺陷等级")
    private String defectLevel;

    /** 删除标识 */
    private String delFlag;
}

