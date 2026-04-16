package com.ruoyi.common.bean.typeEnum;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 维护类型枚举
 */
@Getter
public enum MaintainTypeEnum {

    ROUTINE("routine", "例行保养"),
    REPAIR("repair", "维修"),
    OVERHAUL("overhaul", "大修");

    private String value;
    private String label;

    MaintainTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String getLabel(String value) {
        MaintainTypeEnum[] enums = values();
        if (value != null) {
            for (MaintainTypeEnum e : enums) {
                if (e.getValue().equals(value)) {
                    return e.getLabel();
                }
            }
        }
        return "";
    }

    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        for (MaintainTypeEnum e : values()) {
            map.put(e.getValue(), e.getLabel());
        }
        return map;
    }
}

