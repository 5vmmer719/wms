package com.ruoyi.web.controller.stock;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.base.service.IBaseMatClassService;
import com.ruoyi.base.service.IBaseMatGroupService;
import com.ruoyi.base.service.IBaseWarehouseService;
import com.ruoyi.base.service.IBaseWorkshopService;
import com.ruoyi.common.bean.typeEnum.StockRecordTypeEnum;
import com.ruoyi.stock.service.IStockInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.stock.domain.StockRecord;
import com.ruoyi.stock.service.IStockRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 库存流水Controller
 *
 * @author summer
 * @date 2022-07-25
 */
@RestController
@RequestMapping("/stock/record")
public class StockRecordController extends BaseController {
    @Autowired
    private IStockRecordService stockRecordService;
    @Autowired
    private IBaseMatGroupService baseMatGroupService;
    @Autowired
    private IBaseMatClassService baseMatClassService;
    @Autowired
    private IBaseWarehouseService baseWarehouseService;
    @Autowired
    private IBaseWorkshopService baseWorkshopService;

    /**
     * 查询库存流水列表
     */
    @PreAuthorize("@ss.hasPermi('stock:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(StockRecord stockRecord) {
        startPage();
        List<StockRecord> list = stockRecordService.selectStockRecordList(stockRecord);
        if(CollectionUtils.isNotEmpty(list)){
            for(StockRecord record : list){
                record.setRecordTypeLabel(StockRecordTypeEnum.getLabel(record.getRecordType()));
                record.setMatGroupName(baseMatGroupService.selectBaseMatGroupNameByGroupCode(record.getMatGroup()));
                record.setMatClassName(baseMatClassService.selectBaseMatClassNameByClassCode(record.getMatClass()));
                record.setWarehouseName(baseWarehouseService.selectBaseWarehouseNameByWarehouseCode(record.getWarehouseCode()));
                record.setWorkshopName(baseWorkshopService.selectBaseWorkshopByWorkshopCode(record.getWorkshopCode()));
            }
        }
        return getDataTable(list);
    }

    /**
     * 查询库存流水列表（退货）
     * 按物料编码+批次+货位合并汇总出库数量，并减去已退数量
     */
    @GetMapping("/returnList/{orderNo}")
    public AjaxResult returnList(@PathVariable("orderNo")String orderNo) {
        List<StockRecord> list = stockRecordService.selectStockRecordListByOrderNo(orderNo);
        if(CollectionUtils.isEmpty(list)){
            return AjaxResult.success(list);
        }

        // 分离出库记录和退货记录
        List<StockRecord> outRecords = new java.util.ArrayList<>();
        List<StockRecord> returnRecords = new java.util.ArrayList<>();
        for(StockRecord record : list){
            String recordType = record.getRecordType();
            if(recordType != null && recordType.contains("_return")){
                returnRecords.add(record);
            } else {
                outRecords.add(record);
            }
        }

        // 按 matCode + batch + locationCode 合并出库记录
        java.util.Map<String, StockRecord> mergedMap = new java.util.LinkedHashMap<>();
        for(StockRecord record : outRecords){
            String key = (record.getMatCode() == null ? "" : record.getMatCode())
                    + "|" + (record.getBatch() == null ? "" : record.getBatch())
                    + "|" + (record.getLocationCode() == null ? "" : record.getLocationCode());
            StockRecord existing = mergedMap.get(key);
            if(existing == null){
                // 首次出现，直接放入
                record.setRecordTypeLabel(StockRecordTypeEnum.getLabel(record.getRecordType()));
                record.setMatGroupName(baseMatGroupService.selectBaseMatGroupNameByGroupCode(record.getMatGroup()));
                record.setMatClassName(baseMatClassService.selectBaseMatClassNameByClassCode(record.getMatClass()));
                record.setWarehouseName(baseWarehouseService.selectBaseWarehouseNameByWarehouseCode(record.getWarehouseCode()));
                mergedMap.put(key, record);
            } else {
                // 已存在，累加数量
                existing.setQuantity(existing.getQuantity().add(record.getQuantity()));
            }
        }

        // 计算已退数量并从出库数量中减去
        for(StockRecord returnRecord : returnRecords){
            String key = (returnRecord.getMatCode() == null ? "" : returnRecord.getMatCode())
                    + "|" + (returnRecord.getBatch() == null ? "" : returnRecord.getBatch())
                    + "|" + (returnRecord.getLocationCode() == null ? "" : returnRecord.getLocationCode());
            StockRecord existing = mergedMap.get(key);
            if(existing != null){
                // 退货记录的quantity是退货数量，从出库数量中减去
                existing.setQuantity(existing.getQuantity().subtract(returnRecord.getQuantity()));
            }
        }

        // 过滤掉已全部退货的记录（quantity <= 0）
        List<StockRecord> result = new java.util.ArrayList<>();
        for(StockRecord record : mergedMap.values()){
            if(record.getQuantity().compareTo(java.math.BigDecimal.ZERO) > 0){
                result.add(record);
            }
        }

        return AjaxResult.success(result);
    }

    /**
     * 导出库存流水列表
     */
    @PreAuthorize("@ss.hasPermi('stock:record:export')")
    @Log(title = "库存流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockRecord stockRecord) {
        List<StockRecord> list = stockRecordService.selectStockRecordList(stockRecord);
        ExcelUtil<StockRecord> util = new ExcelUtil<StockRecord>(StockRecord.class);
        util.exportExcel(response, list, "库存流水数据");
    }

    /**
     * 获取库存流水详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:record:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId) {
        return AjaxResult.success(stockRecordService.selectStockRecordByRecordId(recordId));
    }

    /**
     * 新增库存流水
     */
    @PreAuthorize("@ss.hasPermi('stock:record:add')")
    @Log(title = "库存流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockRecord stockRecord) {
        return toAjax(stockRecordService.insertStockRecord(stockRecord));
    }

    /**
     * 修改库存流水
     */
    @PreAuthorize("@ss.hasPermi('stock:record:edit')")
    @Log(title = "库存流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockRecord stockRecord) {
        return toAjax(stockRecordService.updateStockRecord(stockRecord));
    }

    /**
     * 删除库存流水
     */
    @PreAuthorize("@ss.hasPermi('stock:record:remove')")
    @Log(title = "库存流水", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds) {
        return toAjax(stockRecordService.deleteStockRecordByRecordIds(recordIds));
    }
}
