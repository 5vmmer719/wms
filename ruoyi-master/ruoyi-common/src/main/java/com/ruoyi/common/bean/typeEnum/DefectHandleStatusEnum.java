package com.ruoyi.common.bean.typeEnum;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 不合格品处理状态枚举
 */
@Getter
public enum DefectHandleStatusEnum {

    PENDING("pending", "待处理"),
    PROCESSING("processing", "处理中"),
    COMPLETED("completed", "已完成");

    private String value;
    private String label;

    DefectHandleStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String getLabel(String value) {
        DefectHandleStatusEnum[] enums = values();
        if (value != null) {
            for (DefectHandleStatusEnum e : enums) {
                if (e.getValue().equals(value)) {
                    return e.getLabel();
                }
            }
        }
        return null;
    }

    public static Map<String, String> toMap() {
        Map<String, String> m = new HashMap<>();
        DefectHandleStatusEnum[] enums = values();
        for (DefectHandleStatusEnum e : enums) {
            m.put(e.getValue(), e.getLabel());
        }
        return m;
    }
}

