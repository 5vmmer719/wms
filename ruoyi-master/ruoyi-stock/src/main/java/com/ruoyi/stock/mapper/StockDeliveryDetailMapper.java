package com.ruoyi.stock.mapper;

import java.util.List;
import com.ruoyi.stock.domain.StockDeliveryDetail;

/**
 * 交付明细Mapper接口
 *
 * @author wms
 */
public interface StockDeliveryDetailMapper {

    public List<StockDeliveryDetail> selectDetailListByDeliveryNo(String deliveryNo);

    public int insertStockDeliveryDetailList(List<StockDeliveryDetail> list);

    public int deleteDetailByDeliveryNo(String deliveryNo);
}

