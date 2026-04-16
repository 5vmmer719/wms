package com.ruoyi.base.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备维护记录对象 base_equipment_maintain
 *
 * @author ruoyi
 * @date 2026-04-11
 */
@Data
public class BaseEquipmentMaintain extends BaseEntity {

    /** 主键 */
    private Long maintainId;

    /** 维护单号 */
    @Excel(name = "维护单号")
    private String maintainNo;

    /** 设备编码 */
    @Excel(name = "设备编码")
    private String equipmentCode;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String equipmentName;

    /** 维护类型（routine/repair/overhaul） */
    @Excel(name = "维护类型")
    private String maintainType;

    /** 维护日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "维护日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date maintainDate;

    /** 维护结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date maintainEndDate;

    /** 维护内容 */
    @Excel(name = "维护内容")
    private String maintainDesc;

    /** 维护人员 */
    @Excel(name = "维护人员")
    private String maintainBy;

    /** 维护耗时（小时） */
    @Excel(name = "耗时(小时)")
    private BigDecimal maintainHours;

    /** 维护费用 */
    @Excel(name = "维护费用")
    private BigDecimal maintainCost;

    /** 状态（0进行中 1已完成） */
    @Excel(name = "状态")
    private String maintainStatus;

    /** 来源（auto自动 manual手动） */
    @Excel(name = "来源")
    private String source;

    /** 删除标识 */
    private String delFlag;

}

