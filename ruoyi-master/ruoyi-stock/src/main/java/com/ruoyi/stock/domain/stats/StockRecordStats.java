package com.ruoyi.stock.domain.stats;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class StockRecordStats {

    @Excel(name = "物料编码")
    private String matCode;

    @Excel(name = "物料名称")
    private String matName;

    @Excel(name = "图号")
    private String figNum;

    @Excel(name = "原料入库")
    private BigDecimal inPurchase;//原料入库

    @Excel(name = "原料入库退货")
    private BigDecimal inPurchasePeturn;//原料入库退货

    @Excel(name = "生产领料")
    private BigDecimal outProduction;//生产领料

    @Excel(name = "生产领料退货")
    private BigDecimal outProductionReturn;//生产领料退货

    @Excel(name = "销售出库")
    private BigDecimal outCommon;//销售出库

    @Excel(name = "销售出库退货")
    private BigDecimal outCommonReturn;//销售出库退货

    @Excel(name = "上架")
    private BigDecimal upper;//上架

    @Excel(name = "下架")
    private BigDecimal lower;//下架

    @Excel(name = "调拨入库")
    private BigDecimal allotIn;//调拨入库

    @Excel(name = "调拨出库")
    private BigDecimal allotOut;//调拨出库

    /**
     * 请求参数
     */
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
