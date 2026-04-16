package com.ruoyi.stock.mapper;

import java.util.List;
import com.ruoyi.stock.domain.StockCustomerOrder;

/**
 * 客户订单Mapper接口
 *
 * @author wms
 */
public interface StockCustomerOrderMapper {

    public StockCustomerOrder selectStockCustomerOrderByOrderId(Long orderId);

    public StockCustomerOrder selectStockCustomerOrderByOrderNo(String orderNo);

    public List<StockCustomerOrder> selectStockCustomerOrderList(StockCustomerOrder stockCustomerOrder);

    public int insertStockCustomerOrder(StockCustomerOrder stockCustomerOrder);

    public int updateStockCustomerOrder(StockCustomerOrder stockCustomerOrder);

    public int deleteStockCustomerOrderByOrderId(Long orderId);

    public int deleteStockCustomerOrderByOrderIds(Long[] orderIds);
}

