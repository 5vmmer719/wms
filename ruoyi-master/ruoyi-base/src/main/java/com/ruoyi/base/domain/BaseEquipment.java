package com.ruoyi.base.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备台账对象 base_equipment
 *
 * @author ruoyi
 * @date 2026-04-11
 */
@Data
public class BaseEquipment extends BaseEntity {

    /** 主键 */
    private Long equipmentId;

    /** 设备编码 */
    @Excel(name = "设备编码")
    private String equipmentCode;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String equipmentName;

    /** 设备类型（furnace/forming/annealing/cutting/other） */
    @Excel(name = "设备类型")
    private String equipmentType;

    /** 所属车间编码 */
    @Excel(name = "车间编码")
    private String workshopCode;

    /** 车间名称（非持久化） */
    private String workshopName;

    /** 日产能 */
    @Excel(name = "日产能")
    private BigDecimal capacity;

    /** 产能单位 */
    @Excel(name = "产能单位")
    private String capacityUnit;

    /** 状态（0正常 1维护中 2故障 3停用） */
    @Excel(name = "状态")
    private String equipmentStatus;

    /** 购置日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "购置日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date purchaseDate;

    /** 上次维护日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上次维护日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastMaintainDate;

    /** 下次计划维护日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "下次维护日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date nextMaintainDate;

    /** 维护周期（天） */
    @Excel(name = "维护周期(天)")
    private Integer maintainCycle;

    /** 删除标识 */
    private String delFlag;

}

