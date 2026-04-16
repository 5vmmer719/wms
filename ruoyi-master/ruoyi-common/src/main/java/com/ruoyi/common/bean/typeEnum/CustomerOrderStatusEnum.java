package com.ruoyi.common.bean.typeEnum;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户订单状态枚举
 */
@Getter
public enum CustomerOrderStatusEnum {

    CREATED("created", "已创建"),
    CONFIRMED("confirmed", "已确认"),
    PRODUCING("producing", "生产中"),
    COMPLETED("completed", "已完成"),
    DELIVERED("delivered", "已交付"),
    CLOSED("closed", "已关闭");

    private String value;
    private String label;

    CustomerOrderStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String getLabel(String value) {
        CustomerOrderStatusEnum[] enums = values();
        if (value != null) {
            for (CustomerOrderStatusEnum e : enums) {
                if (e.getValue().equals(value)) {
                    return e.getLabel();
                }
            }
        }
        return null;
    }

    public static Map<String, String> toMap() {
        Map<String, String> m = new HashMap<>();
        CustomerOrderStatusEnum[] enums = values();
        for (CustomerOrderStatusEnum e : enums) {
            m.put(e.getValue(), e.getLabel());
        }
        return m;
    }

}

