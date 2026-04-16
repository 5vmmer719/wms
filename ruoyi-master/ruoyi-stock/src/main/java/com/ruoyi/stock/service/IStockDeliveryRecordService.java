package com.ruoyi.stock.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.stock.domain.StockDeliveryRecord;

/**
 * 交付记录Service接口
 *
 * @author wms
 */
public interface IStockDeliveryRecordService {

    public StockDeliveryRecord selectStockDeliveryRecordByDeliveryId(Long deliveryId);

    public List<StockDeliveryRecord> selectStockDeliveryRecordList(StockDeliveryRecord stockDeliveryRecord);

    public int insertStockDeliveryRecord(String username, StockDeliveryRecord stockDeliveryRecord);

    public int updateStockDeliveryRecord(StockDeliveryRecord stockDeliveryRecord);

    public int deleteStockDeliveryRecordByDeliveryIds(Long[] deliveryIds);

    /** 发货：pending -> shipped，回写客户订单 delivered_qty */
    public AjaxResult ship(String username, Long deliveryId);

    /** 签收：shipped -> received */
    public AjaxResult receive(String username, Long deliveryId);

    /** 查询交付详情（含明细） */
    public Map<String, Object> getDetail(Long deliveryId);
}

