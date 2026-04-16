package com.ruoyi.stock.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 生产订单对象 stock_prod_order
 *
 * @author summer
 * @date 2022-07-25
 */
@Data
public class StockProdOrder extends BaseEntity {

    /**
     * 主键
     */
    private Long orderId;

    /**
     * 单据号
     */
    @Excel(name = "单据号")
    private String orderNo;

    /**
     * 工令号
     */
    @Excel(name = "工令号")
    private String workNo;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码")
    private String matCode;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称")
    private String matName;

    /**
     * 车间
     */
    @Excel(name = "车间")
    private String workshopCode;

    private String workshopName;

    /**
     * 工艺路线编码
     */
    @Excel(name = "工艺路线")
    private String routeCode;

    /** 工艺路线名称（非持久化） */
    private String routeName;

    /**
     * 生产设备编码
     */
    @Excel(name = "设备编码")
    private String equipmentCode;

    /** 设备名称（非持久化） */
    private String equipmentName;

    /**
     * 工位编码
     */
    @Excel(name = "工位编码")
    private String stationCode;

    /** 工位名称（非持久化） */
    private String stationName;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private BigDecimal quantity;

    /**
     * 实际完成数量
     */
    @Excel(name = "实际完成数量")
    private BigDecimal actualQuantity;

    /**
     * 优先级（0普通 1紧急 2特急）
     */
    @Excel(name = "优先级")
    private Integer priority;

    /**
     * 计划开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planStartDate;

    /**
     * 计划完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planEndDate;

    /**
     * 实际开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date actualStartDate;

    /**
     * 实际完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date actualEndDate;

    /**
     * 关联客户订单号
     */
    @Excel(name = "客户订单号")
    private String customerOrderNo;

    /**
     * 关联生产计划编号
     */
    @Excel(name = "生产计划编号")
    private String planNo;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String orderStatus;

    private String orderStatusLabel;

    /**
     * 删除标识
     */
    private String delFlag;

}
