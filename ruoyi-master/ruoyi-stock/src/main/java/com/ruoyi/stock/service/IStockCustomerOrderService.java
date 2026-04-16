package com.ruoyi.stock.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.stock.domain.StockCustomerOrder;

/**
 * 客户订单Service接口
 *
 * @author wms
 */
public interface IStockCustomerOrderService {

    public StockCustomerOrder selectStockCustomerOrderByOrderId(Long orderId);

    public StockCustomerOrder selectStockCustomerOrderByOrderNo(String orderNo);

    public List<StockCustomerOrder> selectStockCustomerOrderList(StockCustomerOrder stockCustomerOrder);

    public int insertStockCustomerOrder(String username, StockCustomerOrder stockCustomerOrder);

    public int updateStockCustomerOrder(StockCustomerOrder stockCustomerOrder);

    public int deleteStockCustomerOrderByOrderId(Long orderId);

    public int deleteStockCustomerOrderByOrderIds(Long[] orderIds);

    /**
     * 查询订单详情（含明细列表）
     */
    public Map<String, Object> getDetail(Long orderId);

    /**
     * 确认订单
     */
    public AjaxResult confirmOrder(String username, Long orderId);

    /**
     * 从订单明细生成生产工单
     */
    public AjaxResult generateProdOrder(String username, Long orderId);

    /**
     * 关闭订单
     */
    public AjaxResult closeOrder(String username, Long orderId);
}

