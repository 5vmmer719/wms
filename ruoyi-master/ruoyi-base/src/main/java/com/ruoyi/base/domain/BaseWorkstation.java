package com.ruoyi.base.domain;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工位对象 base_workstation
 *
 * @author ruoyi
 * @date 2026-04-11
 */
@Data
public class BaseWorkstation extends BaseEntity {

    /** 主键 */
    private Long stationId;

    /** 工位编码 */
    @Excel(name = "工位编码")
    private String stationCode;

    /** 工位名称 */
    @Excel(name = "工位名称")
    private String stationName;

    /** 所属设备编码 */
    @Excel(name = "设备编码")
    private String equipmentCode;

    /** 设备名称（非持久化） */
    private String equipmentName;

    /** 所属车间编码 */
    @Excel(name = "车间编码")
    private String workshopCode;

    /** 车间名称（非持久化） */
    private String workshopName;

    /** 默认操作员ID */
    private Long operatorId;

    /** 默认操作员姓名 */
    @Excel(name = "操作员")
    private String operatorName;

    /** 状态（0空闲 1生产中 2维护中） */
    @Excel(name = "状态")
    private String stationStatus;

    /** 删除标识 */
    private String delFlag;

}

