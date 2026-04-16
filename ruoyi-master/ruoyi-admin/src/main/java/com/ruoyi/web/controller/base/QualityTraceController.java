package com.ruoyi.web.controller.base;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.base.domain.QualityDefectHandle;
import com.ruoyi.base.domain.QualityTask;
import com.ruoyi.base.domain.QualityTaskResult;
import com.ruoyi.base.service.IQualityDefectHandleService;
import com.ruoyi.base.service.IQualityTaskService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.stock.domain.*;
import com.ruoyi.stock.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 质量追溯Controller
 *
 * @author wms
 */
@RestController
@RequestMapping("/quality/trace")
public class QualityTraceController extends BaseController {

    @Autowired
    private IStockProdOrderService stockProdOrderService;
    @Autowired
    private IStockInOrderService stockInOrderService;
    @Autowired
    private IStockOutOrderService stockOutOrderService;
    @Autowired
    private IQualityTaskService qualityTaskService;
    @Autowired
    private IQualityDefectHandleService qualityDefectHandleService;
    @Autowired
    private IStockMatLabelService stockMatLabelService;

    /**
     * 质量追溯查询
     * 输入：生产工单号 / 入库单号 / 出库单号 / 批次号
     * 返回：完整追溯链数据
     */
    @GetMapping("/query")
    public AjaxResult traceQuery(@RequestParam("keyword") String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return AjaxResult.error("请输入追溯关键字");
        }
        keyword = keyword.trim();

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> traceChain = new ArrayList<>();

        // 尝试匹配生产工单号
        StockProdOrder prodOrderQuery = new StockProdOrder();
        prodOrderQuery.setOrderNo(keyword);
        List<StockProdOrder> prodOrders = stockProdOrderService.selectStockProdOrderList(prodOrderQuery);

        if (!prodOrders.isEmpty()) {
            // 以生产工单为起点进行追溯
            StockProdOrder prodOrder = prodOrders.get(0);
            buildTraceFromProdOrder(prodOrder, traceChain, result);
            result.put("traceType", "prodOrder");
            result.put("traceChain", traceChain);
            return AjaxResult.success(result);
        }

        // 尝试匹配入库单号
        StockInOrder inOrderQuery = new StockInOrder();
        inOrderQuery.setOrderNo(keyword);
        List<StockInOrder> inOrders = stockInOrderService.selectStockInOrderList(inOrderQuery);

        if (!inOrders.isEmpty()) {
            StockInOrder inOrder = inOrders.get(0);
            buildTraceFromInOrder(inOrder, traceChain, result);
            result.put("traceType", "inOrder");
            result.put("traceChain", traceChain);
            return AjaxResult.success(result);
        }

        // 尝试匹配出库单号
        StockOutOrder outOrderQuery = new StockOutOrder();
        outOrderQuery.setOrderNo(keyword);
        List<StockOutOrder> outOrders = stockOutOrderService.selectStockOutOrderList(outOrderQuery);

        if (!outOrders.isEmpty()) {
            StockOutOrder outOrder = outOrders.get(0);
            buildTraceFromOutOrder(outOrder, traceChain, result);
            result.put("traceType", "outOrder");
            result.put("traceChain", traceChain);
            return AjaxResult.success(result);
        }

        // 尝试匹配批次号（通过物料标签查询）
        StockMatLabel labelQuery = new StockMatLabel();
        labelQuery.setBatch(keyword);
        List<StockMatLabel> labels = stockMatLabelService.selectStockMatLabelList(labelQuery);

        if (!labels.isEmpty()) {
            buildTraceFromBatch(labels, traceChain, result);
            result.put("traceType", "batch");
            result.put("traceChain", traceChain);
            return AjaxResult.success(result);
        }

        return AjaxResult.error("未找到与 \"" + keyword + "\" 相关的追溯数据");
    }

    /**
     * 从生产工单出发构建追溯链
     */
    private void buildTraceFromProdOrder(StockProdOrder prodOrder,
                                          List<Map<String, Object>> traceChain,
                                          Map<String, Object> result) {
        // 节点1：生产工单信息
        Map<String, Object> prodNode = new LinkedHashMap<>();
        prodNode.put("nodeType", "prodOrder");
        prodNode.put("nodeName", "生产工单");
        prodNode.put("orderNo", prodOrder.getOrderNo());
        prodNode.put("matCode", prodOrder.getMatCode());
        prodNode.put("matName", prodOrder.getMatName());
        prodNode.put("quantity", prodOrder.getQuantity());
        prodNode.put("actualQuantity", prodOrder.getActualQuantity());
        prodNode.put("orderStatus", prodOrder.getOrderStatus());
        prodNode.put("workshopCode", prodOrder.getWorkshopCode());
        prodNode.put("routeCode", prodOrder.getRouteCode());
        prodNode.put("equipmentCode", prodOrder.getEquipmentCode());
        prodNode.put("planStartDate", prodOrder.getPlanStartDate());
        prodNode.put("planEndDate", prodOrder.getPlanEndDate());
        prodNode.put("customerOrderNo", prodOrder.getCustomerOrderNo());
        traceChain.add(prodNode);

        // 节点2：领料出库单（上游）
        StockOutOrder outQuery = new StockOutOrder();
        outQuery.setProdOrderNo(prodOrder.getOrderNo());
        List<StockOutOrder> outOrders = stockOutOrderService.selectStockOutOrderList(outQuery);
        List<Map<String, Object>> outOrderNodes = new ArrayList<>();
        Set<String> supplierSet = new HashSet<>();
        Set<String> batchSet = new HashSet<>();

        for (StockOutOrder outOrder : outOrders) {
            Map<String, Object> outNode = new LinkedHashMap<>();
            outNode.put("nodeType", "outOrder");
            outNode.put("nodeName", "领料出库");
            outNode.put("orderNo", outOrder.getOrderNo());
            outNode.put("matCode", outOrder.getMatCode());
            outNode.put("matName", outOrder.getMatName());
            outNode.put("quantity", outOrder.getQuantity());
            outNode.put("orderStatus", outOrder.getOrderStatus());
            outNode.put("warehouseCode", outOrder.getWarehouseCode());

            // 获取出库明细，提取批次和供应商信息
            if (outOrder.getDetailList() != null) {
                List<Map<String, Object>> details = new ArrayList<>();
                for (StockOutDetail detail : outOrder.getDetailList()) {
                    Map<String, Object> d = new LinkedHashMap<>();
                    d.put("matCode", detail.getMatCode());
                    d.put("matName", detail.getMatName());
                    d.put("batch", detail.getBatch());
                    d.put("quantity", detail.getQuantity());
                    d.put("supplierCode", detail.getSupplierCode());
                    d.put("supplierName", detail.getSupplierName());
                    details.add(d);
                    if (StringUtils.isNotBlank(detail.getSupplierCode())) {
                        supplierSet.add(detail.getSupplierCode() + "|" + (detail.getSupplierName() != null ? detail.getSupplierName() : ""));
                    }
                    if (StringUtils.isNotBlank(detail.getBatch())) {
                        batchSet.add(detail.getBatch());
                    }
                }
                outNode.put("details", details);
            }
            outOrderNodes.add(outNode);
        }
        if (!outOrderNodes.isEmpty()) {
            result.put("outOrders", outOrderNodes);
        }

        // 节点3：完工入库单（下游）
        StockInOrder inQuery = new StockInOrder();
        inQuery.setProdOrderNo(prodOrder.getOrderNo());
        List<StockInOrder> inOrders = stockInOrderService.selectStockInOrderList(inQuery);
        List<Map<String, Object>> inOrderNodes = new ArrayList<>();

        for (StockInOrder inOrder : inOrders) {
            Map<String, Object> inNode = new LinkedHashMap<>();
            inNode.put("nodeType", "inOrder");
            inNode.put("nodeName", "完工入库");
            inNode.put("orderNo", inOrder.getOrderNo());
            inNode.put("orderStatus", inOrder.getOrderStatus());
            inNode.put("checkStatus", inOrder.getCheckStatus());
            inNode.put("warehouseCode", inOrder.getWarehouseCode());
            inNode.put("warehouseName", inOrder.getWarehouseName());
            inOrderNodes.add(inNode);
        }
        if (!inOrderNodes.isEmpty()) {
            result.put("inOrders", inOrderNodes);
        }

        // 节点4：质检任务
        QualityTask taskQuery = new QualityTask();
        taskQuery.setSourceNo(prodOrder.getOrderNo());
        List<QualityTask> tasks = qualityTaskService.selectQualityTaskList(taskQuery);

        // 也查询关联入库单的质检任务
        for (StockInOrder inOrder : inOrders) {
            QualityTask inTaskQuery = new QualityTask();
            inTaskQuery.setSourceNo(inOrder.getOrderNo());
            List<QualityTask> inTasks = qualityTaskService.selectQualityTaskList(inTaskQuery);
            tasks.addAll(inTasks);
        }

        List<Map<String, Object>> taskNodes = new ArrayList<>();
        for (QualityTask task : tasks) {
            Map<String, Object> taskNode = new LinkedHashMap<>();
            taskNode.put("nodeType", "qualityTask");
            taskNode.put("nodeName", "质检任务");
            taskNode.put("taskNo", task.getTaskNo());
            taskNode.put("checkType", task.getCheckType());
            taskNode.put("sourceType", task.getSourceType());
            taskNode.put("sourceNo", task.getSourceNo());
            taskNode.put("matCode", task.getMatCode());
            taskNode.put("matName", task.getMatName());
            taskNode.put("taskStatus", task.getTaskStatus());
            taskNode.put("qualifiedQty", task.getQualifiedQty());
            taskNode.put("unqualifiedQty", task.getUnqualifiedQty());
            taskNode.put("inspectorName", task.getInspectorName());
            taskNode.put("checkTime", task.getCheckTime());

            // 获取检验结果明细
            QualityTask detail = qualityTaskService.selectQualityTaskDetail(task.getTaskId());
            if (detail != null && detail.getResultList() != null) {
                List<Map<String, Object>> results = new ArrayList<>();
                for (QualityTaskResult r : detail.getResultList()) {
                    Map<String, Object> rMap = new LinkedHashMap<>();
                    rMap.put("itemName", r.getItemName());
                    rMap.put("standardValue", r.getStandardValue());
                    rMap.put("actualValue", r.getActualValue());
                    rMap.put("minValue", r.getMinValue());
                    rMap.put("maxValue", r.getMaxValue());
                    rMap.put("judgeResult", r.getJudgeResult());
                    rMap.put("defectType", r.getDefectType());
                    rMap.put("defectLevel", r.getDefectLevel());
                    results.add(rMap);
                }
                taskNode.put("resultList", results);
            }
            taskNodes.add(taskNode);
        }
        if (!taskNodes.isEmpty()) {
            result.put("qualityTasks", taskNodes);
        }

        // 节点5：供应商信息
        List<Map<String, Object>> supplierNodes = new ArrayList<>();
        for (String s : supplierSet) {
            String[] parts = s.split("\\|", -1);
            Map<String, Object> sNode = new LinkedHashMap<>();
            sNode.put("nodeType", "supplier");
            sNode.put("nodeName", "供应商");
            sNode.put("supplierCode", parts[0]);
            sNode.put("supplierName", parts.length > 1 ? parts[1] : "");
            supplierNodes.add(sNode);
        }
        if (!supplierNodes.isEmpty()) {
            result.put("suppliers", supplierNodes);
        }

        // 节点6：物料批次标签
        List<Map<String, Object>> labelNodes = new ArrayList<>();
        for (String batch : batchSet) {
            StockMatLabel labelQuery = new StockMatLabel();
            labelQuery.setBatch(batch);
            List<StockMatLabel> labels = stockMatLabelService.selectStockMatLabelList(labelQuery);
            for (StockMatLabel label : labels) {
                Map<String, Object> lNode = new LinkedHashMap<>();
                lNode.put("nodeType", "matLabel");
                lNode.put("nodeName", "物料标签");
                lNode.put("labelCode", label.getLabelCode());
                lNode.put("matCode", label.getMatCode());
                lNode.put("matName", label.getMatName());
                lNode.put("batch", label.getBatch());
                lNode.put("supplierCode", label.getSupplierCode());
                lNode.put("supplierName", label.getSupplierName());
                lNode.put("prodTime", label.getProdTime());
                labelNodes.add(lNode);
            }
        }
        if (!labelNodes.isEmpty()) {
            result.put("matLabels", labelNodes);
        }
    }

    /**
     * 从入库单出发构建追溯链
     */
    private void buildTraceFromInOrder(StockInOrder inOrder,
                                        List<Map<String, Object>> traceChain,
                                        Map<String, Object> result) {
        // 入库单节点
        Map<String, Object> inNode = new LinkedHashMap<>();
        inNode.put("nodeType", "inOrder");
        inNode.put("nodeName", "入库单");
        inNode.put("orderNo", inOrder.getOrderNo());
        inNode.put("orderType", inOrder.getOrderType());
        inNode.put("orderStatus", inOrder.getOrderStatus());
        inNode.put("checkStatus", inOrder.getCheckStatus());
        inNode.put("warehouseCode", inOrder.getWarehouseCode());
        inNode.put("warehouseName", inOrder.getWarehouseName());
        inNode.put("prodOrderNo", inOrder.getProdOrderNo());
        traceChain.add(inNode);

        // 如果关联了生产工单，继续追溯
        if (StringUtils.isNotBlank(inOrder.getProdOrderNo())) {
            StockProdOrder prodQuery = new StockProdOrder();
            prodQuery.setOrderNo(inOrder.getProdOrderNo());
            List<StockProdOrder> prodOrders = stockProdOrderService.selectStockProdOrderList(prodQuery);
            if (!prodOrders.isEmpty()) {
                buildTraceFromProdOrder(prodOrders.get(0), traceChain, result);
            }
        }

        // 查询关联的质检任务
        QualityTask taskQuery = new QualityTask();
        taskQuery.setSourceNo(inOrder.getOrderNo());
        List<QualityTask> tasks = qualityTaskService.selectQualityTaskList(taskQuery);
        if (!tasks.isEmpty()) {
            List<Map<String, Object>> taskNodes = new ArrayList<>();
            for (QualityTask task : tasks) {
                Map<String, Object> taskNode = new LinkedHashMap<>();
                taskNode.put("nodeType", "qualityTask");
                taskNode.put("nodeName", "质检任务");
                taskNode.put("taskNo", task.getTaskNo());
                taskNode.put("taskStatus", task.getTaskStatus());
                taskNode.put("matCode", task.getMatCode());
                taskNode.put("matName", task.getMatName());
                taskNode.put("qualifiedQty", task.getQualifiedQty());
                taskNode.put("unqualifiedQty", task.getUnqualifiedQty());
                taskNodes.add(taskNode);
            }
            result.put("qualityTasks", taskNodes);
        }
    }

    /**
     * 从出库单出发构建追溯链
     */
    private void buildTraceFromOutOrder(StockOutOrder outOrder,
                                         List<Map<String, Object>> traceChain,
                                         Map<String, Object> result) {
        // 出库单节点
        Map<String, Object> outNode = new LinkedHashMap<>();
        outNode.put("nodeType", "outOrder");
        outNode.put("nodeName", "出库单");
        outNode.put("orderNo", outOrder.getOrderNo());
        outNode.put("matCode", outOrder.getMatCode());
        outNode.put("matName", outOrder.getMatName());
        outNode.put("quantity", outOrder.getQuantity());
        outNode.put("orderStatus", outOrder.getOrderStatus());
        outNode.put("prodOrderNo", outOrder.getProdOrderNo());
        traceChain.add(outNode);

        // 如果关联了生产工单，继续追溯
        if (StringUtils.isNotBlank(outOrder.getProdOrderNo())) {
            StockProdOrder prodQuery = new StockProdOrder();
            prodQuery.setOrderNo(outOrder.getProdOrderNo());
            List<StockProdOrder> prodOrders = stockProdOrderService.selectStockProdOrderList(prodQuery);
            if (!prodOrders.isEmpty()) {
                buildTraceFromProdOrder(prodOrders.get(0), traceChain, result);
            }
        }
    }

    /**
     * 从批次号出发构建追溯链
     */
    private void buildTraceFromBatch(List<StockMatLabel> labels,
                                      List<Map<String, Object>> traceChain,
                                      Map<String, Object> result) {
        // 物料标签节点
        StockMatLabel firstLabel = labels.get(0);
        Map<String, Object> labelNode = new LinkedHashMap<>();
        labelNode.put("nodeType", "matLabel");
        labelNode.put("nodeName", "物料标签/批次");
        labelNode.put("batch", firstLabel.getBatch());
        labelNode.put("matCode", firstLabel.getMatCode());
        labelNode.put("matName", firstLabel.getMatName());
        labelNode.put("supplierCode", firstLabel.getSupplierCode());
        labelNode.put("supplierName", firstLabel.getSupplierName());
        labelNode.put("labelCount", labels.size());
        traceChain.add(labelNode);

        // 通过物料编码和批次查找关联的出库明细 → 生产工单
        // 由于出库明细中包含 batch 和 matCode，可以反向追溯
        result.put("matLabels", labels.stream().map(l -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("labelCode", l.getLabelCode());
            m.put("matCode", l.getMatCode());
            m.put("matName", l.getMatName());
            m.put("batch", l.getBatch());
            m.put("supplierCode", l.getSupplierCode());
            m.put("supplierName", l.getSupplierName());
            m.put("prodTime", l.getProdTime());
            return m;
        }).collect(Collectors.toList()));
    }

    /**
     * 质量统计数据
     * 返回：各检验类型的合格率、缺陷类型分布、缺陷等级分布、月度趋势
     */
    @GetMapping("/stats")
    public AjaxResult qualityStats() {
        Map<String, Object> result = new HashMap<>();

        // 查询所有检验任务
        List<QualityTask> allTasks = qualityTaskService.selectQualityTaskList(new QualityTask());

        // 1. 按检验类型统计合格率
        Map<String, Map<String, Object>> typeStats = new LinkedHashMap<>();
        String[] types = {"incoming", "process", "final"};
        String[] typeNames = {"来料检验", "过程检验", "终检"};
        for (int i = 0; i < types.length; i++) {
            final String type = types[i];
            long total = allTasks.stream().filter(t -> type.equals(t.getCheckType()) && ("passed".equals(t.getTaskStatus()) || "failed".equals(t.getTaskStatus()))).count();
            long passed = allTasks.stream().filter(t -> type.equals(t.getCheckType()) && "passed".equals(t.getTaskStatus())).count();
            Map<String, Object> stat = new LinkedHashMap<>();
            stat.put("typeName", typeNames[i]);
            stat.put("total", total);
            stat.put("passed", passed);
            stat.put("failed", total - passed);
            stat.put("passRate", total > 0 ? new BigDecimal(passed * 100).divide(new BigDecimal(total), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            typeStats.put(type, stat);
        }
        result.put("typeStats", typeStats);

        // 2. 缺陷类型分布（从检验结果明细中聚合）
        Map<String, Integer> defectTypeDist = new LinkedHashMap<>();
        Map<String, Integer> defectLevelDist = new LinkedHashMap<>();
        defectLevelDist.put("minor", 0);
        defectLevelDist.put("major", 0);
        defectLevelDist.put("critical", 0);

        for (QualityTask task : allTasks) {
            if (!"passed".equals(task.getTaskStatus()) && !"failed".equals(task.getTaskStatus())) continue;
            QualityTask detail = qualityTaskService.selectQualityTaskDetail(task.getTaskId());
            if (detail == null || detail.getResultList() == null) continue;
            for (QualityTaskResult r : detail.getResultList()) {
                if ("1".equals(r.getJudgeResult())) {
                    // 不合格项
                    String defectType = StringUtils.isNotBlank(r.getDefectType()) ? r.getDefectType() : "未分类";
                    defectTypeDist.put(defectType, defectTypeDist.getOrDefault(defectType, 0) + 1);
                    String defectLevel = StringUtils.isNotBlank(r.getDefectLevel()) ? r.getDefectLevel() : "minor";
                    defectLevelDist.put(defectLevel, defectLevelDist.getOrDefault(defectLevel, 0) + 1);
                }
            }
        }
        result.put("defectTypeDist", defectTypeDist);
        result.put("defectLevelDist", defectLevelDist);

        // 3. 总体统计
        long totalChecked = allTasks.stream().filter(t -> "passed".equals(t.getTaskStatus()) || "failed".equals(t.getTaskStatus())).count();
        long totalPassed = allTasks.stream().filter(t -> "passed".equals(t.getTaskStatus())).count();
        long totalFailed = allTasks.stream().filter(t -> "failed".equals(t.getTaskStatus())).count();
        long totalPending = allTasks.stream().filter(t -> "pending".equals(t.getTaskStatus()) || "checking".equals(t.getTaskStatus())).count();
        result.put("totalChecked", totalChecked);
        result.put("totalPassed", totalPassed);
        result.put("totalFailed", totalFailed);
        result.put("totalPending", totalPending);
        result.put("overallPassRate", totalChecked > 0
                ? new BigDecimal(totalPassed * 100).divide(new BigDecimal(totalChecked), 1, RoundingMode.HALF_UP)
                : BigDecimal.ZERO);

        // 4. 不合格品处理统计
        QualityDefectHandle handleQuery = new QualityDefectHandle();
        List<QualityDefectHandle> handles = qualityDefectHandleService.selectQualityDefectHandleList(handleQuery);
        Map<String, Long> handleStatusDist = handles.stream()
                .collect(Collectors.groupingBy(h -> h.getHandleStatus() != null ? h.getHandleStatus() : "unknown", Collectors.counting()));
        result.put("defectHandleCount", handles.size());
        result.put("handleStatusDist", handleStatusDist);

        return AjaxResult.success(result);
    }
}

