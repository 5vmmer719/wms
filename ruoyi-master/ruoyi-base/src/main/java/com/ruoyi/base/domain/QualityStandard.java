package com.ruoyi.base.domain;

import java.util.List;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 检验标准对象 quality_standard
 *
 * @author summer
 * @date 2026-04-11
 */
@Data
public class QualityStandard extends BaseEntity {

    /** 主键 */
    private Long standardId;

    /** 检验标准编码 */
    @Excel(name = "检验标准编码")
    private String standardCode;

    /** 检验标准名称 */
    @Excel(name = "检验标准名称")
    private String standardName;

    /** 检验类型（incoming原料检验/process过程检验/final成品检验） */
    @Excel(name = "检验类型")
    private String checkType;

    /** 关联物料编码 */
    @Excel(name = "关联物料编码")
    private String matCode;

    /** 关联物料名称（非持久化） */
    private String matName;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String standardStatus;

    /** 删除标识 */
    private String delFlag;

    /** 检验项目列表（非持久化，级联查询时加载） */
    private List<QualityStandardItem> items;
}

