package com.ruoyi.stock.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.stock.domain.StockProdOrder;

/**
 * 生产订单Service接口
 *
 * @author summer
 * @date 2022-07-25
 */
public interface IStockProdOrderService {
    /**
     * 查询生产订单
     *
     * @param orderId 生产订单主键
     * @return 生产订单
     */
    public StockProdOrder selectStockProdOrderByOrderId(Long orderId);

    /**
     * 查询生产订单
     *
     * @param orderNo
     * @return 生产订单
     */
    public StockProdOrder selectStockProdOrderByOrderNo(String orderNo);

    /**
     * 查询生产订单列表
     *
     * @param stockProdOrder 生产订单
     * @return 生产订单集合
     */
    public List<StockProdOrder> selectStockProdOrderList(StockProdOrder stockProdOrder);

    /**
     * 新增生产订单
     *
     * @param stockProdOrder 生产订单
     * @return 结果
     */
    public int insertStockProdOrder(StockProdOrder stockProdOrder);

    /**
     * 修改生产订单
     *
     * @param stockProdOrder 生产订单
     * @return 结果
     */
    public int updateStockProdOrder(StockProdOrder stockProdOrder);

    /**
     * 批量删除生产订单
     *
     * @param orderIds 需要删除的生产订单主键集合
     * @return 结果
     */
    public int deleteStockProdOrderByOrderIds(Long[] orderIds);

    /**
     * 删除生产订单信息
     *
     * @param orderId 生产订单主键
     * @return 结果
     */
    public int deleteStockProdOrderByOrderId(Long orderId);

    /**
     * 排产 - 设置计划时间和优先级，状态从planned变为planned（保持）
     */
    public AjaxResult schedule(String username, StockProdOrder stockProdOrder);

    /**
     * 开工 - 状态从planned变为ongoing，记录实际开始时间
     */
    public AjaxResult start(String username, Long orderId);

    /**
     * 报工完工 - 状态从ongoing变为completed，记录实际完成数量和实际完成时间
     */
    public AjaxResult complete(String username, StockProdOrder stockProdOrder);

    /**
     * 关闭工单 - 状态从completed变为closed
     */
    public AjaxResult close(String username, Long orderId);

    /**
     * 查询工单详情（含关联出库单和入库单）
     */
    public Map<String, Object> getDetail(Long orderId);

    /**
     * 生成工令号（自动递增）
     */
    public String generateWorkNo();
}
