package com.ruoyi.common.bean.typeEnum;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 检验任务状态枚举
 */
@Getter
public enum QualityTaskStatusEnum {

    PENDING("pending", "待检验"),
    CHECKING("checking", "检验中"),
    PASSED("passed", "合格"),
    FAILED("failed", "不合格");

    private String value;
    private String label;

    QualityTaskStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String getLabel(String value) {
        QualityTaskStatusEnum[] enums = values();
        if (value != null) {
            for (QualityTaskStatusEnum e : enums) {
                if (e.getValue().equals(value)) {
                    return e.getLabel();
                }
            }
        }
        return null;
    }

    public static Map<String, String> toMap() {
        Map<String, String> m = new HashMap<>();
        QualityTaskStatusEnum[] enums = values();
        for (QualityTaskStatusEnum e : enums) {
            m.put(e.getValue(), e.getLabel());
        }
        return m;
    }
}

