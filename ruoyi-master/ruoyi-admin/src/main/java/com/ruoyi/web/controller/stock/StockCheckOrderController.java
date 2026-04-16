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
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.stock.domain.StockCheckOrder;
import com.ruoyi.stock.service.IStockCheckOrderService;

/**
 * 库存盘点Controller
 *
 * @author wms
 */
@RestController
@RequestMapping("/stock/check")
public class StockCheckOrderController extends BaseController {

    @Autowired
    private IStockCheckOrderService stockCheckOrderService;

    /**
     * 查询盘点单列表
     */
    @PreAuthorize("@ss.hasPermi('stock:check:list')")
    @GetMapping("/list")
    public TableDataInfo list(StockCheckOrder stockCheckOrder) {
        startPage();
        List<StockCheckOrder> list = stockCheckOrderService.selectStockCheckOrderList(stockCheckOrder);
        return getDataTable(list);
    }

    /**
     * 导出盘点单列表
     */
    @PreAuthorize("@ss.hasPermi('stock:check:export')")
    @Log(title = "库存盘点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockCheckOrder stockCheckOrder) {
        List<StockCheckOrder> list = stockCheckOrderService.selectStockCheckOrderList(stockCheckOrder);
        ExcelUtil<StockCheckOrder> util = new ExcelUtil<StockCheckOrder>(StockCheckOrder.class);
        util.exportExcel(response, list, "盘点单数据");
    }

    /**
     * 获取盘点单详细信息（含明细）
     */
    @PreAuthorize("@ss.hasPermi('stock:check:query')")
    @GetMapping(value = "/{checkId}")
    public AjaxResult getInfo(@PathVariable("checkId") Long checkId) {
        return AjaxResult.success(stockCheckOrderService.selectStockCheckOrderByCheckId(checkId));
    }

    /**
     * 新增盘点单（自动快照库存）
     */
    @PreAuthorize("@ss.hasPermi('stock:check:add')")
    @Log(title = "库存盘点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockCheckOrder stockCheckOrder) {
        return stockCheckOrderService.insertStockCheckOrder(getUsername(), stockCheckOrder);
    }

    /**
     * 修改盘点单
     */
    @PreAuthorize("@ss.hasPermi('stock:check:edit')")
    @Log(title = "库存盘点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockCheckOrder stockCheckOrder) {
        return toAjax(stockCheckOrderService.updateStockCheckOrder(stockCheckOrder));
    }

    /**
     * 提交盘点结果
     */
    @PreAuthorize("@ss.hasPermi('stock:check:edit')")
    @Log(title = "提交盘点结果", businessType = BusinessType.UPDATE)
    @PutMapping("/submit")
    public AjaxResult submit(@RequestBody StockCheckOrder stockCheckOrder) {
        return stockCheckOrderService.submitCheckResult(getUsername(), stockCheckOrder);
    }

    /**
     * 执行盘点调整（盘盈入库/盘亏出库）
     */
    @PreAuthorize("@ss.hasPermi('stock:check:edit')")
    @Log(title = "盘点调整", businessType = BusinessType.UPDATE)
    @PutMapping("/adjust/{checkId}")
    public AjaxResult adjust(@PathVariable("checkId") Long checkId) {
        return stockCheckOrderService.adjustStock(getUsername(), checkId);
    }

    /**
     * 删除盘点单
     */
    @PreAuthorize("@ss.hasPermi('stock:check:remove')")
    @Log(title = "库存盘点", businessType = BusinessType.DELETE)
    @DeleteMapping("/{checkIds}")
    public AjaxResult remove(@PathVariable Long[] checkIds) {
        return toAjax(stockCheckOrderService.deleteStockCheckOrderByCheckIds(checkIds));
    }
}

