package com.ruoyi.web.controller.stats;

import com.ruoyi.base.domain.QualityTask;
import com.ruoyi.base.service.IBaseMatService;
import com.ruoyi.base.service.IQualityTaskService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.stock.domain.StockCustomerOrder;
import com.ruoyi.stock.domain.StockDeliveryRecord;
import com.ruoyi.stock.domain.StockInOrder;
import com.ruoyi.stock.domain.StockInfo;
import com.ruoyi.stock.domain.StockProdOrder;
import com.ruoyi.stock.domain.stats.StockInStats;
import com.ruoyi.stock.domain.stats.StockOutStats;
import com.ruoyi.stock.domain.stats.StockRecordStats;
import com.ruoyi.stock.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计Controller
 *
 * @author summer
 * @date 2022-07-24
 */
@RestController
@RequestMapping("/stats")
public class StatsController extends BaseController {
    @Autowired
    private IBaseMatService baseMatService;
    @Autowired
    private IStockInOrderService stockInOrderService;
    @Autowired
    private IStockInReturnService stockInReturnService;
    @Autowired
    private IStockOutOrderService stockOutOrderService;
    @Autowired
    private IStockOutReturnService stockOutReturnService;
    @Autowired
    private IStockRecordService stockRecordService;
    @Autowired
    private IStockCustomerOrderService stockCustomerOrderService;
    @Autowired
    private IStockProdOrderService stockProdOrderService;
    @Autowired
    private IQualityTaskService qualityTaskService;
    @Autowired
    private IStockDeliveryRecordService stockDeliveryRecordService;
    @Autowired
    private IStockInfoService stockInfoService;

    /**
     * 首页头部统计
     */
    @GetMapping("/indexUpper")
    public AjaxResult indexUpper() {
        Map<String, Long> statsMap = new HashMap<>();
        //物料数
        int matTotal = baseMatService.selectBaseMatTotal();
        statsMap.put("matTotal", Long.parseLong(String.valueOf(matTotal)));
        Date nowDate = DateUtils.getNowDate();
        //入库单数
        Map<String, Long> inOrderStats = stockInOrderService.selectStockInOrderTotal(nowDate);
        statsMap.putAll(inOrderStats);
        //入库退货单数
        Map<String, Long> inReturnStats = stockInReturnService.selectStockInReturnTotal(nowDate);
        statsMap.putAll(inReturnStats);
        //出库单数
        Map<String, Long> outOrderStats = stockOutOrderService.selectStockOutOrderTotal(nowDate);
        statsMap.putAll(outOrderStats);
        //出库退货单数
        Map<String, Long> outReturnStats = stockOutReturnService.selectStockOutReturnTotal(nowDate);
        statsMap.putAll(outReturnStats);
        return AjaxResult.success(statsMap);
    }

    /**
     * 首页中部统计
     */
    @GetMapping("/indexMiddle")
    public AjaxResult indexMiddle() {
        Map<String, Long[]> statsMap = new HashMap<>();
        Date[] weekDay = DateUtils.getThisWeekDay();
        Long[] purchaseArr = new Long[7];
        Long[] productionArr = new Long[7];
        Long[] commonArr = new Long[7];
        Map<String, Long> inOrderStats = null;
        Map<String, Long> outOrderStats = null;
        int i = 0;
        for(Date d : weekDay){
            inOrderStats = stockInOrderService.selectStockInOrderTotal(d);
            purchaseArr[i] = inOrderStats.get("purchase");
            outOrderStats = stockOutOrderService.selectStockOutOrderTotal(d);
            productionArr[i] = outOrderStats.get("production");
            commonArr[i] = outOrderStats.get("common");
            i++;
        }
        statsMap.put("purchaseArr", purchaseArr);
        statsMap.put("productionArr", productionArr);
        statsMap.put("commonArr", commonArr);
        return AjaxResult.success(statsMap);
    }

    /**
     * 首页下部统计
     */
    @GetMapping("/indexLower")
    public AjaxResult indexLower() {
        Map<String, Long[]> statsMap = new HashMap<>();
        Date[] weekDay = DateUtils.getThisWeekDay();
        Long[] purchaseReturnArr = new Long[7];
        Long[] productionReturnArr = new Long[7];
        Long[] commonReturnArr = new Long[7];
        Map<String, Long> inReturnStats = null;
        Map<String, Long> outReturnStats = null;
        int i = 0;
        for(Date d : weekDay){
            inReturnStats = stockInReturnService.selectStockInReturnTotal(d);
            purchaseReturnArr[i] = inReturnStats.get("purchaseReturn");
            outReturnStats = stockOutReturnService.selectStockOutReturnTotal(d);
            productionReturnArr[i] = outReturnStats.get("productionReturn");
            commonReturnArr[i] = outReturnStats.get("commonReturn");
            i++;
        }
        statsMap.put("purchaseReturnArr", purchaseReturnArr);
        statsMap.put("productionReturnArr", productionReturnArr);
        statsMap.put("commonReturnArr", commonReturnArr);
        return AjaxResult.success(statsMap);
    }

    /**
     * 查询入库统计列表
     */
    @PreAuthorize("@ss.hasPermi('stats:stockIn:list')")
    @GetMapping("/stockIn")
    public TableDataInfo stockIn(String matCode, String matName) {
        startPage();
        List<StockInStats> list = stockInOrderService.selectStockInStatsList(matCode, matName);
        return getDataTable(list);
    }

    /**
     * 查询出库统计列表
     */
    @PreAuthorize("@ss.hasPermi('stats:stockOut:list')")
    @GetMapping("/stockOut")
    public TableDataInfo stockOut(String matCode, String matName) {
        startPage();
        List<StockOutStats> list = stockOutOrderService.selectStockOutStatsList(matCode, matName);
        return getDataTable(list);
    }

    /**
     * 查询库存操作统计列表
     */
    @PreAuthorize("@ss.hasPermi('stats:stockRecord:list')")
    @GetMapping("/stockRecord")
    public TableDataInfo stockRecord(StockRecordStats recordStats) {
        startPage();
        List<StockRecordStats> list = stockRecordService.statsStockRecord(recordStats);
        return getDataTable(list);
    }

    /**
     * 导出入库单统计
     */
    @PreAuthorize("@ss.hasPermi('stock:inOrder:export')")
    @Log(title = "入库单统计", businessType = BusinessType.EXPORT)
    @PostMapping("/statsInOrderExport")
    public void statsInOrderExport(HttpServletResponse response, String matCode, String matName) {
        List<StockInStats> list = stockInOrderService.selectStockInStatsList(matCode, matName);
        ExcelUtil<StockInStats> util = new ExcelUtil<StockInStats>(StockInStats.class);
        util.exportExcel(response, list, "入库单统计数据");
    }

    /**
     * 导出出库单统计
     */
    @PreAuthorize("@ss.hasPermi('stock:outOrder:export')")
    @Log(title = "出库单统计", businessType = BusinessType.EXPORT)
    @PostMapping("/statsOutOrderExport")
    public void statsOutOrderExport(HttpServletResponse response, String matCode, String matName) {
        List<StockOutStats> list = stockOutOrderService.selectStockOutStatsList(matCode, matName);
        ExcelUtil<StockOutStats> util = new ExcelUtil<StockOutStats>(StockOutStats.class);
        util.exportExcel(response, list, "出库单统计数据");
    }

    /**
     * 导出库存操作统计
     */
    @PreAuthorize("@ss.hasPermi('stock:record:export')")
    @Log(title = "库存操作统计", businessType = BusinessType.EXPORT)
    @PostMapping("/statsRecordExport")
    public void statsRecordExport(HttpServletResponse response, StockRecordStats recordStats) {
        List<StockRecordStats> list = stockRecordService.statsStockRecord(recordStats);
        ExcelUtil<StockRecordStats> util = new ExcelUtil<StockRecordStats>(StockRecordStats.class);
        util.exportExcel(response, list, "出库单统计数据");
    }

    // ===================== 订单进度看板接口 =====================

    /**
     * 订单进度看板 - 综合统计数据
     * 返回：客户订单状态分布、生产完成率、交付准时率、预警列表等
     */
    @GetMapping("/orderProgress")
    public AjaxResult orderProgress() {
        Map<String, Object> result = new HashMap<>();

        // 1. 客户订单状态分布
        List<StockCustomerOrder> allOrders = stockCustomerOrderService.selectStockCustomerOrderList(new StockCustomerOrder());
        Map<String, Long> orderStatusDist = new HashMap<>();
        orderStatusDist.put("created", 0L);
        orderStatusDist.put("confirmed", 0L);
        orderStatusDist.put("producing", 0L);
        orderStatusDist.put("completed", 0L);
        orderStatusDist.put("delivered", 0L);
        orderStatusDist.put("closed", 0L);
        for (StockCustomerOrder o : allOrders) {
            String status = o.getOrderStatus();
            if (status != null) {
                orderStatusDist.put(status, orderStatusDist.getOrDefault(status, 0L) + 1);
            }
        }
        result.put("orderStatusDist", orderStatusDist);
        result.put("orderTotal", allOrders.size());

        // 2. 生产工单状态分布
        List<StockProdOrder> allProdOrders = stockProdOrderService.selectStockProdOrderList(new StockProdOrder());
        Map<String, Long> prodStatusDist = new HashMap<>();
        prodStatusDist.put("planned", 0L);
        prodStatusDist.put("ongoing", 0L);
        prodStatusDist.put("completed", 0L);
        prodStatusDist.put("closed", 0L);
        BigDecimal totalPlanQty = BigDecimal.ZERO;
        BigDecimal totalActualQty = BigDecimal.ZERO;
        for (StockProdOrder po : allProdOrders) {
            String status = po.getOrderStatus();
            if (status != null) {
                prodStatusDist.put(status, prodStatusDist.getOrDefault(status, 0L) + 1);
            }
            if (po.getQuantity() != null) {
                totalPlanQty = totalPlanQty.add(po.getQuantity());
            }
            if (po.getActualQuantity() != null) {
                totalActualQty = totalActualQty.add(po.getActualQuantity());
            }
        }
        result.put("prodStatusDist", prodStatusDist);
        result.put("prodTotal", allProdOrders.size());
        // 总体生产完成率
        BigDecimal prodCompletionRate = BigDecimal.ZERO;
        if (totalPlanQty.compareTo(BigDecimal.ZERO) > 0) {
            prodCompletionRate = totalActualQty.multiply(new BigDecimal("100"))
                    .divide(totalPlanQty, 1, RoundingMode.HALF_UP);
        }
        result.put("prodCompletionRate", prodCompletionRate);

        // 3. 质检任务状态分布
        List<QualityTask> allTasks = qualityTaskService.selectQualityTaskList(new QualityTask());
        Map<String, Long> qualityStatusDist = new HashMap<>();
        qualityStatusDist.put("pending", 0L);
        qualityStatusDist.put("checking", 0L);
        qualityStatusDist.put("passed", 0L);
        qualityStatusDist.put("failed", 0L);
        for (QualityTask t : allTasks) {
            String status = t.getTaskStatus();
            if (status != null) {
                qualityStatusDist.put(status, qualityStatusDist.getOrDefault(status, 0L) + 1);
            }
        }
        result.put("qualityStatusDist", qualityStatusDist);
        // 质检合格率
        long passedCount = qualityStatusDist.getOrDefault("passed", 0L);
        long failedCount = qualityStatusDist.getOrDefault("failed", 0L);
        long totalChecked = passedCount + failedCount;
        BigDecimal qualityPassRate = BigDecimal.ZERO;
        if (totalChecked > 0) {
            qualityPassRate = new BigDecimal(passedCount * 100).divide(new BigDecimal(totalChecked), 1, RoundingMode.HALF_UP);
        }
        result.put("qualityPassRate", qualityPassRate);

        // 4. 交付准时率
        List<StockDeliveryRecord> allDeliveries = stockDeliveryRecordService.selectStockDeliveryRecordList(new StockDeliveryRecord());
        result.put("deliveryTotal", allDeliveries.size());

        // 5. 本周生产完成趋势（按天统计完工工单数）
        Date[] weekDays = DateUtils.getThisWeekDay();
        Long[] prodCompletedArr = new Long[7];
        Long[] orderCreatedArr = new Long[7];
        for (int i = 0; i < 7; i++) {
            final Date day = weekDays[i];
            final String dayStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(day);
            // 统计当天完工的生产工单数
            long completedCount = allProdOrders.stream()
                    .filter(po -> po.getActualEndDate() != null
                            && new java.text.SimpleDateFormat("yyyy-MM-dd").format(po.getActualEndDate()).equals(dayStr))
                    .count();
            prodCompletedArr[i] = completedCount;
            // 统计当天创建的客户订单数
            long createdCount = allOrders.stream()
                    .filter(o -> o.getCreateTime() != null
                            && new java.text.SimpleDateFormat("yyyy-MM-dd").format(o.getCreateTime()).equals(dayStr))
                    .count();
            orderCreatedArr[i] = createdCount;
        }
        result.put("prodCompletedArr", prodCompletedArr);
        result.put("orderCreatedArr", orderCreatedArr);

        return AjaxResult.success(result);
    }

    /**
     * 订单预警列表 - 即将到期和已逾期的订单
     */
    @GetMapping("/orderWarnings")
    public AjaxResult orderWarnings() {
        List<Map<String, Object>> warnings = new ArrayList<>();
        Date now = new Date();

        // 1. 交付预警：距交付日期 ≤ 3天且订单状态未完成/未交付
        List<StockCustomerOrder> allOrders = stockCustomerOrderService.selectStockCustomerOrderList(new StockCustomerOrder());
        for (StockCustomerOrder order : allOrders) {
            if (order.getDeliveryDate() == null) continue;
            String status = order.getOrderStatus();
            if ("delivered".equals(status) || "closed".equals(status)) continue;

            long diffDays = (order.getDeliveryDate().getTime() - now.getTime()) / (1000 * 60 * 60 * 24);
            if (diffDays < 0) {
                // 已逾期
                Map<String, Object> w = new HashMap<>();
                w.put("type", "overdue");
                w.put("level", "danger");
                w.put("title", "订单已逾期");
                w.put("content", "客户订单 " + order.getOrderNo() + "（" + order.getCustomerName() + "）已逾期 " + Math.abs(diffDays) + " 天");
                w.put("orderNo", order.getOrderNo());
                w.put("daysRemaining", diffDays);
                warnings.add(w);
            } else if (diffDays <= 3) {
                // 即将到期
                Map<String, Object> w = new HashMap<>();
                w.put("type", "urgent");
                w.put("level", "warning");
                w.put("title", "订单即将到期");
                w.put("content", "客户订单 " + order.getOrderNo() + "（" + order.getCustomerName() + "）还有 " + diffDays + " 天到期");
                w.put("orderNo", order.getOrderNo());
                w.put("daysRemaining", diffDays);
                warnings.add(w);
            }
        }

        // 2. 质检预警：不合格且未处理的检验任务
        QualityTask taskQuery = new QualityTask();
        taskQuery.setTaskStatus("failed");
        List<QualityTask> failedTasks = qualityTaskService.selectQualityTaskList(taskQuery);
        for (QualityTask task : failedTasks) {
            Map<String, Object> w = new HashMap<>();
            w.put("type", "quality");
            w.put("level", "danger");
            w.put("title", "质检不合格");
            w.put("content", "检验任务 " + task.getTaskNo() + "（" + task.getMatName() + "）判定不合格，不合格数量：" + (task.getUnqualifiedQty() != null ? task.getUnqualifiedQty() : "N/A"));
            w.put("taskNo", task.getTaskNo());
            warnings.add(w);
        }

        // 3. 生产延期预警：超过计划完成日期但仍未完工的工单
        List<StockProdOrder> allProdOrders = stockProdOrderService.selectStockProdOrderList(new StockProdOrder());
        for (StockProdOrder po : allProdOrders) {
            if (po.getPlanEndDate() == null) continue;
            if ("completed".equals(po.getOrderStatus()) || "closed".equals(po.getOrderStatus())) continue;
            long diffDays = (po.getPlanEndDate().getTime() - now.getTime()) / (1000 * 60 * 60 * 24);
            if (diffDays < 0) {
                Map<String, Object> w = new HashMap<>();
                w.put("type", "prodDelay");
                w.put("level", "warning");
                w.put("title", "生产延期");
                w.put("content", "生产工单 " + po.getOrderNo() + "（" + po.getMatName() + "）已超期 " + Math.abs(diffDays) + " 天未完工");
                w.put("orderNo", po.getOrderNo());
                w.put("daysRemaining", diffDays);
                warnings.add(w);
            }
        }

        // 按严重程度排序：danger > warning
        warnings.sort((a, b) -> {
            int levelA = "danger".equals(a.get("level")) ? 0 : 1;
            int levelB = "danger".equals(b.get("level")) ? 0 : 1;
            return levelA - levelB;
        });

        return AjaxResult.success(warnings);
    }

    // ===================== 库存预警看板接口 =====================

    /**
     * 库存预警看板数据
     * 返回：低库存物料列表、超库存物料列表、库存正常物料数、预警物料数
     */
    @GetMapping("/stockWarning")
    public AjaxResult stockWarning() {
        Map<String, Object> result = new HashMap<>();

        // 查询所有物料的库存汇总
        List<StockInfo> statsList = stockInfoService.selectStockInfoStatsList(new StockInfo());

        List<Map<String, Object>> lowStockList = new ArrayList<>();
        List<Map<String, Object>> highStockList = new ArrayList<>();
        int normalCount = 0;

        for (StockInfo info : statsList) {
            BigDecimal currentQty = info.getStatsQuantity() != null ? info.getStatsQuantity() : BigDecimal.ZERO;
            boolean isLow = info.getSafetyStock() != null
                    && info.getSafetyStock().compareTo(BigDecimal.ZERO) > 0
                    && currentQty.compareTo(info.getSafetyStock()) < 0;
            boolean isHigh = info.getMaxStock() != null
                    && info.getMaxStock().compareTo(BigDecimal.ZERO) > 0
                    && currentQty.compareTo(info.getMaxStock()) > 0;

            if (isLow) {
                Map<String, Object> item = new HashMap<>();
                item.put("matCode", info.getMatCode());
                item.put("matName", info.getMatName());
                item.put("currentQty", currentQty);
                item.put("safetyStock", info.getSafetyStock());
                item.put("shortage", info.getSafetyStock().subtract(currentQty));
                item.put("unitCode", info.getUnitCode());
                lowStockList.add(item);
            } else if (isHigh) {
                Map<String, Object> item = new HashMap<>();
                item.put("matCode", info.getMatCode());
                item.put("matName", info.getMatName());
                item.put("currentQty", currentQty);
                item.put("maxStock", info.getMaxStock());
                item.put("excess", currentQty.subtract(info.getMaxStock()));
                item.put("unitCode", info.getUnitCode());
                highStockList.add(item);
            } else {
                normalCount++;
            }
        }

        result.put("lowStockList", lowStockList);
        result.put("highStockList", highStockList);
        result.put("lowCount", lowStockList.size());
        result.put("highCount", highStockList.size());
        result.put("normalCount", normalCount);
        result.put("totalCount", statsList.size());

        return AjaxResult.success(result);
    }
}
