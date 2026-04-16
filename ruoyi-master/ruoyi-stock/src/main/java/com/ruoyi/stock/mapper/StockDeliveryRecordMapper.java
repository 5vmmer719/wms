package com.ruoyi.stock.mapper;

import java.util.List;
import com.ruoyi.stock.domain.StockDeliveryRecord;

/**
 * 交付记录Mapper接口
 *
 * @author wms
 */
public interface StockDeliveryRecordMapper {

    public StockDeliveryRecord selectStockDeliveryRecordByDeliveryId(Long deliveryId);

    public StockDeliveryRecord selectStockDeliveryRecordByDeliveryNo(String deliveryNo);

    public List<StockDeliveryRecord> selectStockDeliveryRecordList(StockDeliveryRecord stockDeliveryRecord);

    public List<StockDeliveryRecord> selectByOrderNo(String orderNo);

    public int insertStockDeliveryRecord(StockDeliveryRecord stockDeliveryRecord);

    public int updateStockDeliveryRecord(StockDeliveryRecord stockDeliveryRecord);

    public int deleteStockDeliveryRecordByDeliveryId(Long deliveryId);

    public int deleteStockDeliveryRecordByDeliveryIds(Long[] deliveryIds);
}

