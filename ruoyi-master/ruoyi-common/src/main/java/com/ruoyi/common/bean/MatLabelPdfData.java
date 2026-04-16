package com.ruoyi.common.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 物料标签打印数据对象
 * 注意：物料标签已重构，不再存储数量、单价等业务数据
 * 数量和单价信息应在入库单明细中维护
 *
 * @author summer
 * @date 2022-07-25
 */
@Data
public class MatLabelPdfData implements Serializable {

    /**
     * 主键
     */
    private Long labelId;

    /**
     * 标签编码
     */
    private String labelCode;

    /**
     * 标签类型
     */
    private String labelType;

    /**
     * 物料编码
     */
    private String matCode;

    /**
     * 批次
     */
    private String batch;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 生产时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date prodTime;

    /**
     * 状态(created/in_stored/in_transit)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}