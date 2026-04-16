package com.ruoyi.common.bean.typeEnum;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备状态枚举
 */
@Getter
public enum EquipmentStatusEnum {

    NORMAL("0", "正常"),
    MAINTAINING("1", "维护中"),
    FAULT("2", "故障"),
    DISABLED("3", "停用");

    private String value;
    private String label;

    EquipmentStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String getLabel(String value) {
        EquipmentStatusEnum[] enums = values();
        if (value != null) {
            for (EquipmentStatusEnum e : enums) {
                if (e.getValue().equals(value)) {
                    return e.getLabel();
                }
            }
        }
        return "";
    }

    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        for (EquipmentStatusEnum e : values()) {
            map.put(e.getValue(), e.getLabel());
        }
        return map;
    }
}

