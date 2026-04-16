package com.ruoyi.base.domain;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户对象 base_customer
 *
 * @author wms
 */
@Data
public class BaseCustomer extends BaseEntity {

    /** 客户ID */
    private Long customerId;

    /** 客户编码 */
    @Excel(name = "客户编码")
    private String customerCode;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contactPerson;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String contactPhone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 客户等级（A/B/C） */
    @Excel(name = "客户等级")
    private String customerLevel;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态")
    private String customerStatus;

    /** 删除标识 */
    private String delFlag;

}

