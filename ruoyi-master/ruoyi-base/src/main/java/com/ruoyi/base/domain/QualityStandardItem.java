package com.ruoyi.base.domain;

import java.math.BigDecimal;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 检验项目对象 quality_standard_item
 *
 * @author summer
 * @date 2026-04-11
 */
@Data
public class QualityStandardItem extends BaseEntity {

    /** 主键 */
    private Long itemId;

    /** 所属检验标准编码 */
    @Excel(name = "检验标准编码")
    private String standardCode;

    /** 序号 */
    @Excel(name = "序号")
    private Integer itemNo;

    /** 检验项名称 */
    @Excel(name = "检验项名称")
    private String itemName;

    /** 单位 */
    @Excel(name = "单位")
    private String itemUnit;

    /** 标准值 */
    @Excel(name = "标准值")
    private String standardValue;

    /** 下限 */
    @Excel(name = "下限")
    private BigDecimal minValue;

    /** 上限 */
    @Excel(name = "上限")
    private BigDecimal maxValue;

    /** 检验方法 */
    @Excel(name = "检验方法")
    private String checkMethod;

    /** 是否关键项（0否 1是） */
    @Excel(name = "是否关键项", readConverterExp = "0=否,1=是")
    private String isKey;

    /** 删除标识 */
    private String delFlag;
}

