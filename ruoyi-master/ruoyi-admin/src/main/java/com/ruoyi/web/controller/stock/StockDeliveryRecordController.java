package com.ruoyi.web.controller.stock;

import java.util.List;
import java.util.Map;
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
import com.ruoyi.stock.domain.StockDeliveryRecord;
import com.ruoyi.stock.service.IStockDeliveryRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 交付管理Controller
 *
 * @author wms
 */
@RestController
@RequestMapping("/order/delivery")
public class StockDeliveryRecordController extends BaseController {

    @Autowired
    private IStockDeliveryRecordService stockDeliveryRecordService;

    /**
     * 查询交付记录列表
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:list')")
    @GetMapping("/list")
    public TableDataInfo list(StockDeliveryRecord stockDeliveryRecord) {
        startPage();
        List<StockDeliveryRecord> list = stockDeliveryRecordService.selectStockDeliveryRecordList(stockDeliveryRecord);
        for (StockDeliveryRecord record : list) {
            setStatusLabel(record);
        }
        return getDataTable(list);
    }

    /**
     * 导出交付记录
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:export')")
    @Log(title = "交付记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockDeliveryRecord stockDeliveryRecord) {
        List<StockDeliveryRecord> list = stockDeliveryRecordService.selectStockDeliveryRecordList(stockDeliveryRecord);
        ExcelUtil<StockDeliveryRecord> util = new ExcelUtil<StockDeliveryRecord>(StockDeliveryRecord.class);
        util.exportExcel(response, list, "交付记录数据");
    }

    /**
     * 获取交付详情
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:query')")
    @GetMapping("/detail/{deliveryId}")
    public AjaxResult getDetail(@PathVariable Long deliveryId) {
        Map<String, Object> detail = stockDeliveryRecordService.getDetail(deliveryId);
        return AjaxResult.success(detail);
    }

    /**
     * 新增交付记录
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:add')")
    @Log(title = "交付记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockDeliveryRecord stockDeliveryRecord) {
        return toAjax(stockDeliveryRecordService.insertStockDeliveryRecord(getUsername(), stockDeliveryRecord));
    }

    /**
     * 修改交付记录
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:edit')")
    @Log(title = "交付记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockDeliveryRecord stockDeliveryRecord) {
        return toAjax(stockDeliveryRecordService.updateStockDeliveryRecord(stockDeliveryRecord));
    }

    /**
     * 删除交付记录
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:remove')")
    @Log(title = "交付记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deliveryIds}")
    public AjaxResult remove(@PathVariable Long[] deliveryIds) {
        return toAjax(stockDeliveryRecordService.deleteStockDeliveryRecordByDeliveryIds(deliveryIds));
    }

    /**
     * 发货操作
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:edit')")
    @Log(title = "交付记录-发货", businessType = BusinessType.UPDATE)
    @PutMapping("/ship/{deliveryId}")
    public AjaxResult ship(@PathVariable Long deliveryId) {
        return stockDeliveryRecordService.ship(getUsername(), deliveryId);
    }

    /**
     * 签收操作
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:edit')")
    @Log(title = "交付记录-签收", businessType = BusinessType.UPDATE)
    @PutMapping("/receive/{deliveryId}")
    public AjaxResult receive(@PathVariable Long deliveryId) {
        return stockDeliveryRecordService.receive(getUsername(), deliveryId);
    }

    private void setStatusLabel(StockDeliveryRecord record) {
        if ("pending".equals(record.getDeliveryStatus())) {
            record.setDeliveryStatusLabel("待发货");
        } else if ("shipped".equals(record.getDeliveryStatus())) {
            record.setDeliveryStatusLabel("已发货");
        } else if ("received".equals(record.getDeliveryStatus())) {
            record.setDeliveryStatusLabel("已签收");
        }
    }
}

