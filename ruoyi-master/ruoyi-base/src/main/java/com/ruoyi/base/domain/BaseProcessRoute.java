package com.ruoyi.base.domain;

import java.util.List;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工艺路线对象 base_process_route
 *
 * @author summer
 * @date 2026-04-11
 */
@Data
public class BaseProcessRoute extends BaseEntity {

    /** 主键 */
    private Long routeId;

    /** 工艺路线编码 */
    @Excel(name = "工艺路线编码")
    private String routeCode;

    /** 工艺路线名称 */
    @Excel(name = "工艺路线名称")
    private String routeName;

    /** 关联产品物料编码 */
    @Excel(name = "关联物料编码")
    private String matCode;

    /** 关联产品物料名称（非持久化，查询时关联） */
    private String matName;

    /** 版本号 */
    @Excel(name = "版本号")
    private String routeVersion;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String routeStatus;

    /** 删除标识 */
    private String delFlag;

    /** 工序列表（非持久化，查询详情时级联加载） */
    private List<BaseProcessStep> steps;

}

