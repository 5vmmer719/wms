package com.ruoyi.common.bean.typeEnum;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

//生产订单状态
@Getter
public enum ProdOrderStatusEnum {

    PLANNED("planned", "待排产"),
    ONGOING("ongoing", "生产中"),
    COMPLETED("completed", "已完工"),
    CLOSED( "closed","已关闭");

    private String value;
    private String label;

    ProdOrderStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String getLabel(String value) {
        ProdOrderStatusEnum[] enums = values();
        if(value != null){
            for (ProdOrderStatusEnum e : enums) {
                if (e.getValue().equals(value)) {
                    return e.getLabel();
                }
            }
        }
        return null;
    }

    public static Map<String, String> toMap(){
        Map<String, String> m = new HashMap<>();
        ProdOrderStatusEnum[] enums = values();
        for (ProdOrderStatusEnum e : enums) {
            m.put(e.getValue(), e.getLabel());
        }
        return m;
    }

}
