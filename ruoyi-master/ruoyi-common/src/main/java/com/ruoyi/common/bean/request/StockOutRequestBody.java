package com.ruoyi.common.bean.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 提交出库单请求体
 */
@Data
public class StockOutRequestBody {

    /**
     * 出库单号
     */
    private String orderNo;

    /**
     * 领取数量映射
     * key: 物料编码 (matCode)
     * value: 领取数量
     */
    private Map<String, BigDecimal> receivedMap;

}