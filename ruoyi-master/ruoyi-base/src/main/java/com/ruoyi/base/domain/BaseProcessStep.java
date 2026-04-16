package com.ruoyi.base.domain;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工序对象 base_process_step
 *
 * @author summer
 * @date 2026-04-11
 */
@Data
public class BaseProcessStep extends BaseEntity {

    /** 主键 */
    private Long stepId;

    /** 所属工艺路线编码 */
    @Excel(name = "工艺路线编码")
    private String routeCode;

    /** 工序序号 */
    @Excel(name = "工序序号")
    private Integer stepNo;

    /** 工序编码 */
    @Excel(name = "工序编码")
    private String stepCode;

    /** 工序名称 */
    @Excel(name = "工序名称")
    private String stepName;

    /** 工序类型（melting熔制/forming成型/annealing退火/cutting切割/other其他） */
    @Excel(name = "工序类型")
    private String stepType;

    /** 标准工时（小时） */
    @Excel(name = "标准工时(h)")
    private BigDecimal standardHours;

    /** 删除标识 */
    private String delFlag;

    /** 工艺参数列表（非持久化，查询时级联加载） */
    private List<BaseProcessParam> paramsList;

}

