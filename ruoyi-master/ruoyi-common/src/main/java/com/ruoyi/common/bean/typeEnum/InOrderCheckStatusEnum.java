package com.ruoyi.common.bean.typeEnum;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

//入库单检验状态
@Getter
public enum InOrderCheckStatusEnum {

    UN_CHECKOUT("un_checkout", "未检验"),
    CHECKING("checking", "检验中"),
    PASSED("passed", "检验合格"),
    FAILED("failed", "检验不合格"),
    CHECKOUT("checkout","已检验");

    private String value;
    private String label;

    InOrderCheckStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String getLabel(String value) {
        InOrderCheckStatusEnum[] enums = values();
        if(value != null){
            for (InOrderCheckStatusEnum e : enums) {
                if (e.getValue().equals(value)) {
                    return e.getLabel();
                }
            }
        }
        return null;
    }

    public static Map<String, String> toMap(){
        Map<String, String> m = new HashMap<>();
        InOrderCheckStatusEnum[] enums = values();
        for (InOrderCheckStatusEnum e : enums) {
            m.put(e.getValue(), e.getLabel());
        }
        return m;
    }

}
