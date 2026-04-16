package com.ruoyi.base.domain;

import java.math.BigDecimal;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工艺参数标准对象 base_process_param
 *
 * @author summer
 * @date 2026-04-11
 */
@Data
public class BaseProcessParam extends BaseEntity {

    /** 主键 */
    private Long paramId;

    /** 所属工序ID */
    @Excel(name = "所属工序ID")
    private Long stepId;

    /** 参数名称 */
    @Excel(name = "参数名称")
    private String paramName;

    /** 单位 */
    @Excel(name = "单位")
    private String paramUnit;

    /** 标准值 */
    @Excel(name = "标准值")
    private String standardValue;

    /** 下限值 */
    @Excel(name = "下限值")
    private BigDecimal minValue;

    /** 上限值 */
    @Excel(name = "上限值")
    private BigDecimal maxValue;

    /** 是否关键参数（0否 1是） */
    @Excel(name = "是否关键参数", readConverterExp = "0=否,1=是")
    private String isKey;

    /** 删除标识 */
    private String delFlag;

}

