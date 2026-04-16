package com.ruoyi.web.controller.stock;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.bean.typeEnum.CustomerOrderStatusEnum;
import org.apache.commons.collections4.CollectionUtils;
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
import com.ruoyi.stock.domain.StockCustomerOrder;
import com.ruoyi.stock.service.IStockCustomerOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客户订单Controller
 *
 * @author wms
 */
@RestController
@RequestMapping("/order/customerOrder")
public class StockCustomerOrderController extends BaseController {

    @Autowired
    private IStockCustomerOrderService stockCustomerOrderService;

    /**
     * 查询客户订单列表
     */
    @PreAuthorize("@ss.hasPermi('order:customerOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(StockCustomerOrder stockCustomerOrder) {
        startPage();
        List<StockCustomerOrder> list = stockCustomerOrderService.selectStockCustomerOrderList(stockCustomerOrder);
        if (CollectionUtils.isNotEmpty(list)) {
            for (StockCustomerOrder order : list) {
                order.setOrderStatusLabel(CustomerOrderStatusEnum.getLabel(order.getOrderStatus()));
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出客户订单列表
     */
    @PreAuthorize("@ss.hasPermi('order:customerOrder:export')")
    @Log(title = "客户订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockCustomerOrder stockCustomerOrder) {
        List<StockCustomerOrder> list = stockCustomerOrderService.selectStockCustomerOrderList(stockCustomerOrder);
        ExcelUtil<StockCustomerOrder> util = new ExcelUtil<StockCustomerOrder>(StockCustomerOrder.class);
        util.exportExcel(response, list, "客户订单数据");
    }

    /**
     * 查询订单详情（含明细和关联工单）
     */
    @PreAuthorize("@ss.hasPermi('order:customerOrder:query')")
    @GetMapping("/detail/{orderId}")
    public AjaxResult detail(@PathVariable Long orderId) {
        return AjaxResult.success(stockCustomerOrderService.getDetail(orderId));
    }

    /**
     * 获取客户订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:customerOrder:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId) {
        return AjaxResult.success(stockCustomerOrderService.selectStockCustomerOrderByOrderId(orderId));
    }

    /**
     * 新增客户订单
     */
    @PreAuthorize("@ss.hasPermi('order:customerOrder:add')")
    @Log(title = "客户订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockCustomerOrder stockCustomerOrder) {
        return toAjax(stockCustomerOrderService.insertStockCustomerOrder(getUsername(), stockCustomerOrder));
    }

    /**
     * 修改客户订单
     */
    @PreAuthorize("@ss.hasPermi('order:customerOrder:edit')")
    @Log(title = "客户订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockCustomerOrder stockCustomerOrder) {
        stockCustomerOrder.setUpdateBy(getUsername());
        return toAjax(stockCustomerOrderService.updateStockCustomerOrder(stockCustomerOrder));
    }

    /**
     * 删除客户订单
     */
    @PreAuthorize("@ss.hasPermi('order:customerOrder:remove')")
    @Log(title = "客户订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds) {
        return toAjax(stockCustomerOrderService.deleteStockCustomerOrderByOrderIds(orderIds));
    }

    /**
     * 确认订单
     */
    @PreAuthorize("@ss.hasPermi('order:customerOrder:edit')")
    @Log(title = "客户订单确认", businessType = BusinessType.UPDATE)
    @PutMapping("/confirm/{orderId}")
    public AjaxResult confirm(@PathVariable Long orderId) {
        return stockCustomerOrderService.confirmOrder(getUsername(), orderId);
    }

    /**
     * 生成生产工单
     */
    @PreAuthorize("@ss.hasPermi('order:customerOrder:edit')")
    @Log(title = "客户订单生成工单", businessType = BusinessType.UPDATE)
    @PutMapping("/generateProdOrder/{orderId}")
    public AjaxResult generateProdOrder(@PathVariable Long orderId) {
        return stockCustomerOrderService.generateProdOrder(getUsername(), orderId);
    }

    /**
     * 关闭订单
     */
    @PreAuthorize("@ss.hasPermi('order:customerOrder:edit')")
    @Log(title = "客户订单关闭", businessType = BusinessType.UPDATE)
    @PutMapping("/close/{orderId}")
    public AjaxResult close(@PathVariable Long orderId) {
        return stockCustomerOrderService.closeOrder(getUsername(), orderId);
    }
}

