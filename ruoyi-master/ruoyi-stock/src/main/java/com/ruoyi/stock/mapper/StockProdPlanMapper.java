package com.ruoyi.stock.mapper;

import java.util.List;

import com.ruoyi.stock.domain.StockProdPlan;

/**
 * 生产计划Mapper接口
 *
 * @author wms
 */
public interface StockProdPlanMapper {

    /**
     * 查询生产计划
     */
    public StockProdPlan selectStockProdPlanByPlanId(Long planId);

    /**
     * 通过计划编号查询
     */
    public StockProdPlan selectStockProdPlanByPlanNo(String planNo);

    /**
     * 查询生产计划列表
     */
    public List<StockProdPlan> selectStockProdPlanList(StockProdPlan stockProdPlan);

    /**
     * 新增生产计划
     */
    public int insertStockProdPlan(StockProdPlan stockProdPlan);

    /**
     * 修改生产计划
     */
    public int updateStockProdPlan(StockProdPlan stockProdPlan);

    /**
     * 删除生产计划
     */
    public int deleteStockProdPlanByPlanId(Long planId);

    /**
     * 批量删除生产计划
     */
    public int deleteStockProdPlanByPlanIds(Long[] planIds);
}

