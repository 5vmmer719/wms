package com.ruoyi.web.controller.stock;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.stock.domain.StockProdPlan;
import com.ruoyi.stock.service.IStockProdPlanService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 生产计划Controller
 *
 * @author wms
 */
@RestController
@RequestMapping("/stock/prodPlan")
public class StockProdPlanController extends BaseController {

    @Autowired
    private IStockProdPlanService stockProdPlanService;

    /**
     * 查询生产计划列表
     */
    @PreAuthorize("@ss.hasPermi('stock:prodPlan:list')")
    @GetMapping("/list")
    public TableDataInfo list(StockProdPlan stockProdPlan) {
        startPage();
        List<StockProdPlan> list = stockProdPlanService.selectStockProdPlanList(stockProdPlan);
        return getDataTable(list);
    }

    /**
     * 导出生产计划列表
     */
    @PreAuthorize("@ss.hasPermi('stock:prodPlan:export')")
    @Log(title = "生产计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockProdPlan stockProdPlan) {
        List<StockProdPlan> list = stockProdPlanService.selectStockProdPlanList(stockProdPlan);
        ExcelUtil<StockProdPlan> util = new ExcelUtil<StockProdPlan>(StockProdPlan.class);
        util.exportExcel(response, list, "生产计划数据");
    }

    /**
     * 获取生产计划详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:prodPlan:query')")
    @GetMapping(value = "/{planId}")
    public AjaxResult getInfo(@PathVariable("planId") Long planId) {
        return AjaxResult.success(stockProdPlanService.selectStockProdPlanByPlanId(planId));
    }

    /**
     * 新增生产计划
     */
    @PreAuthorize("@ss.hasPermi('stock:prodPlan:add')")
    @Log(title = "生产计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockProdPlan stockProdPlan) {
        return toAjax(stockProdPlanService.insertStockProdPlan(getUsername(), stockProdPlan));
    }

    /**
     * 修改生产计划
     */
    @PreAuthorize("@ss.hasPermi('stock:prodPlan:edit')")
    @Log(title = "生产计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockProdPlan stockProdPlan) {
        return toAjax(stockProdPlanService.updateStockProdPlan(stockProdPlan));
    }

    /**
     * 删除生产计划
     */
    @PreAuthorize("@ss.hasPermi('stock:prodPlan:remove')")
    @Log(title = "生产计划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{planIds}")
    public AjaxResult remove(@PathVariable Long[] planIds) {
        return toAjax(stockProdPlanService.deleteStockProdPlanByPlanIds(planIds));
    }

    /**
     * 确认计划
     */
    @PreAuthorize("@ss.hasPermi('stock:prodPlan:edit')")
    @Log(title = "确认生产计划", businessType = BusinessType.UPDATE)
    @PutMapping("/confirm/{planId}")
    public AjaxResult confirm(@PathVariable Long planId) {
        return stockProdPlanService.confirm(getUsername(), planId);
    }

    /**
     * 生成工单
     */
    @PreAuthorize("@ss.hasPermi('stock:prodPlan:edit')")
    @Log(title = "生产计划生成工单", businessType = BusinessType.UPDATE)
    @PutMapping("/generateOrders/{planId}")
    public AjaxResult generateOrders(@PathVariable Long planId) {
        return stockProdPlanService.generateOrders(getUsername(), planId);
    }

    /**
     * 完成计划
     */
    @PreAuthorize("@ss.hasPermi('stock:prodPlan:edit')")
    @Log(title = "完成生产计划", businessType = BusinessType.UPDATE)
    @PutMapping("/complete/{planId}")
    public AjaxResult completePlan(@PathVariable Long planId) {
        return stockProdPlanService.completePlan(getUsername(), planId);
    }

    /**
     * 取消计划
     */
    @PreAuthorize("@ss.hasPermi('stock:prodPlan:edit')")
    @Log(title = "取消生产计划", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel/{planId}")
    public AjaxResult cancelPlan(@PathVariable Long planId) {
        return stockProdPlanService.cancelPlan(getUsername(), planId);
    }
}

