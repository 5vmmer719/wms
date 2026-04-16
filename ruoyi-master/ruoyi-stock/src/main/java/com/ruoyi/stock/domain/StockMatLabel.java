package com.ruoyi.stock.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物料标签对象 stock_mat_label
 *
 * @author summer
 * @date 2022-07-25
 */
@Data
public class StockMatLabel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long labelId;

    /**
     * 标签编码
     */
    @Excel(name = "标签编码")
    private String labelCode;

    /**
     * 标签类型
     */
    @Excel(name = "标签类型")
    private String labelType;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码")
    private String matCode;

    /**
     * 批次
     */
    @Excel(name = "批次")
    private String batch;

    /**
     * 供应商编码
     */
    @Excel(name = "供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @Excel(name = "供应商名称")
    private String supplierName;

    /**
     * 生产时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生产时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date prodTime;

    /**
     * 状态(0-启用 1-停用)
     */
    @Excel(name = "状态")
    private String status;

    /**
     * 删除标识(0正常 1删除)
     */
    private String delFlag;

    // ========== 以下为关联查询字段（来自base_mat/base_mat_group/base_mat_class表） ==========

    /** 物料描述 */
    @Excel(name = "物料描述")
    private String matName;

    /** 财务编码 */
    @Excel(name = "财务编码")
    private String fdCode;

    /** 图号 */
    @Excel(name = "图号")
    private String figNum;

    /** 物料组编码 */
    private String matGroup;

    /** 物料分类编码 */
    private String matClass;

    /** 基本单位 */
    @Excel(name = "基本单位")
    private String unitCode;

    /** 物料组名称 */
    @Excel(name = "物料组")
    private String matGroupName;

    /** 物料分类名称 */
    @Excel(name = "物料分类")
    private String matClassName;

    /** 仓库类型（查询参数，用于按仓库类型过滤物料标签） */
    private String warehouseType;

}