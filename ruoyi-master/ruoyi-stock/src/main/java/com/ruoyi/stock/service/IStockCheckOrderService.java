package com.ruoyi.stock.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.stock.domain.StockCheckOrder;

/**
 * 库存盘点Service接口
 *
 * @author wms
 */
public interface IStockCheckOrderService {

    /**
     * 查询盘点单
     */
    public StockCheckOrder selectStockCheckOrderByCheckId(Long checkId);

    /**
     * 查询盘点单列表
     */
    public List<StockCheckOrder> selectStockCheckOrderList(StockCheckOrder stockCheckOrder);

    /**
     * 新增盘点单（自动快照库存）
     */
    public AjaxResult insertStockCheckOrder(String username, StockCheckOrder stockCheckOrder);

    /**
     * 修改盘点单
     */
    public int updateStockCheckOrder(StockCheckOrder stockCheckOrder);

    /**
     * 提交盘点结果（录入实盘数量）
     */
    public AjaxResult submitCheckResult(String username, StockCheckOrder stockCheckOrder);

    /**
     * 执行盘点调整（盘盈入库/盘亏出库）
     */
    public AjaxResult adjustStock(String username, Long checkId);

    /**
     * 批量删除盘点单
     */
    public int deleteStockCheckOrderByCheckIds(Long[] checkIds);

    /**
     * 删除盘点单
     */
    public int deleteStockCheckOrderByCheckId(Long checkId);
}

