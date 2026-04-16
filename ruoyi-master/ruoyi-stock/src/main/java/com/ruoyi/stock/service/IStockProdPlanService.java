package com.ruoyi.stock.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.stock.domain.StockProdPlan;

/**
 * 生产计划Service接口
 *
 * @author wms
 */
public interface IStockProdPlanService {

    /**
     * 查询生产计划
     */
    public StockProdPlan selectStockProdPlanByPlanId(Long planId);

    /**
     * 查询生产计划列表
     */
    public List<StockProdPlan> selectStockProdPlanList(StockProdPlan stockProdPlan);

    /**
     * 新增生产计划
     */
    public int insertStockProdPlan(String username, StockProdPlan stockProdPlan);

    /**
     * 修改生产计划
     */
    public int updateStockProdPlan(StockProdPlan stockProdPlan);

    /**
     * 批量删除生产计划
     */
    public int deleteStockProdPlanByPlanIds(Long[] planIds);

    /**
     * 确认计划 - draft -> confirmed
     */
    public AjaxResult confirm(String username, Long planId);

    /**
     * 生成工单 - 根据计划自动创建生产工单，状态变为executing
     */
    public AjaxResult generateOrders(String username, Long planId);

    /**
     * 完成计划 - executing -> completed
     */
    public AjaxResult completePlan(String username, Long planId);

    /**
     * 取消计划 - draft/confirmed -> cancelled
     */
    public AjaxResult cancelPlan(String username, Long planId);
}

