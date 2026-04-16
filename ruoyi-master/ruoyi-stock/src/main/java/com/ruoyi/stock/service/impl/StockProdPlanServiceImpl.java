package com.ruoyi.stock.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruoyi.base.domain.BaseMatBom;
import com.ruoyi.base.service.IBaseMatBomService;
import com.ruoyi.base.service.IBaseWorkshopService;
import com.ruoyi.common.bean.typeEnum.ProdOrderStatusEnum;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.OrderNoUtil;
import com.ruoyi.stock.domain.StockInfo;
import com.ruoyi.stock.domain.StockProdOrder;
import com.ruoyi.stock.domain.StockProdPlan;
import com.ruoyi.stock.mapper.StockInfoMapper;
import com.ruoyi.stock.mapper.StockProdOrderMapper;
import com.ruoyi.stock.mapper.StockProdPlanMapper;
import com.ruoyi.stock.service.IStockProdOrderService;
import com.ruoyi.stock.service.IStockProdPlanService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 生产计划Service业务层处理
 *
 * @author wms
 */
@Service
public class StockProdPlanServiceImpl implements IStockProdPlanService {

    @Autowired
    private StockProdPlanMapper stockProdPlanMapper;
    @Autowired
    private StockProdOrderMapper stockProdOrderMapper;
    @Autowired
    private IStockProdOrderService stockProdOrderService;
    @Autowired
    private IBaseWorkshopService baseWorkshopService;
    @Autowired
    private IBaseMatBomService baseMatBomService;
    @Autowired
    private StockInfoMapper stockInfoMapper;

    private static final String STATUS_DRAFT = "draft";
    private static final String STATUS_CONFIRMED = "confirmed";
    private static final String STATUS_EXECUTING = "executing";
    private static final String STATUS_COMPLETED = "completed";
    private static final String STATUS_CANCELLED = "cancelled";

    @Override
    public StockProdPlan selectStockProdPlanByPlanId(Long planId) {
        return stockProdPlanMapper.selectStockProdPlanByPlanId(planId);
    }

    @Override
    public List<StockProdPlan> selectStockProdPlanList(StockProdPlan stockProdPlan) {
        List<StockProdPlan> list = stockProdPlanMapper.selectStockProdPlanList(stockProdPlan);
        if (CollectionUtils.isNotEmpty(list)) {
            for (StockProdPlan plan : list) {
                fillPlanInfo(plan);
            }
        }
        return list;
    }

    @Override
    public int insertStockProdPlan(String username, StockProdPlan stockProdPlan) {
        stockProdPlan.setPlanNo(OrderNoUtil.generate(OrderNoUtil.OrderPrefix.PROD_PLAN));
        stockProdPlan.setPlanStatus(STATUS_DRAFT);
        stockProdPlan.setActualQuantity(BigDecimal.ZERO);
        stockProdPlan.setCompletionRate(BigDecimal.ZERO);
        stockProdPlan.setCreateBy(username);
        stockProdPlan.setCreateTime(DateUtils.getNowDate());
        return stockProdPlanMapper.insertStockProdPlan(stockProdPlan);
    }

    @Override
    public int updateStockProdPlan(StockProdPlan stockProdPlan) {
        stockProdPlan.setUpdateTime(DateUtils.getNowDate());
        return stockProdPlanMapper.updateStockProdPlan(stockProdPlan);
    }

    @Override
    public int deleteStockProdPlanByPlanIds(Long[] planIds) {
        return stockProdPlanMapper.deleteStockProdPlanByPlanIds(planIds);
    }

    /**
     * 确认计划
     */
    @Override
    public AjaxResult confirm(String username, Long planId) {
        StockProdPlan plan = stockProdPlanMapper.selectStockProdPlanByPlanId(planId);
        if (plan == null) {
            return AjaxResult.error("计划不存在");
        }
        if (!STATUS_DRAFT.equals(plan.getPlanStatus())) {
            return AjaxResult.error("只有草稿状态的计划才能确认");
        }
        plan.setPlanStatus(STATUS_CONFIRMED);
        plan.setUpdateBy(username);
        plan.setUpdateTime(DateUtils.getNowDate());
        stockProdPlanMapper.updateStockProdPlan(plan);
        return AjaxResult.success("计划已确认");
    }

    /**
     * 生成工单 - 根据计划自动创建一个生产工单
     * 增强：BOM展开后校验物料库存是否充足
     */
    @Override
    @Transactional
    public AjaxResult generateOrders(String username, Long planId) {
        StockProdPlan plan = stockProdPlanMapper.selectStockProdPlanByPlanId(planId);
        if (plan == null) {
            return AjaxResult.error("计划不存在");
        }
        if (!STATUS_CONFIRMED.equals(plan.getPlanStatus())) {
            return AjaxResult.error("只有已确认状态的计划才能生成工单");
        }
        if (StringUtils.isEmpty(plan.getMatCode())) {
            return AjaxResult.error("计划未指定产品物料，无法生成工单");
        }

        // ===== 排产增强：BOM展开后校验物料库存 =====
        List<BaseMatBom> bomList = baseMatBomService.selectBaseMatBomByFatherMatCode(plan.getMatCode());
        if (CollectionUtils.isNotEmpty(bomList)) {
            List<String> shortageWarnings = new ArrayList<>();
            checkBomStock(bomList, plan.getPlanQuantity(), shortageWarnings);
            // 如果有库存不足的物料，返回警告信息但仍然允许生成工单
            if (CollectionUtils.isNotEmpty(shortageWarnings)) {
                // 仍然创建工单，但在返回消息中附带库存不足警告
                String orderNo = doCreateProdOrder(username, plan);
                StringBuilder msg = new StringBuilder("已生成生产工单：" + orderNo + "。\n⚠ 以下物料库存不足，请注意备料：\n");
                for (String warn : shortageWarnings) {
                    msg.append("  - ").append(warn).append("\n");
                }
                return AjaxResult.success(msg.toString());
            }
        }

        // 正常创建工单
        String orderNo = doCreateProdOrder(username, plan);
        return AjaxResult.success("已生成生产工单：" + orderNo);
    }

    /**
     * 实际创建生产工单的逻辑
     */
    private String doCreateProdOrder(String username, StockProdPlan plan) {
        StockProdOrder prodOrder = new StockProdOrder();
        prodOrder.setOrderNo(OrderNoUtil.generateUniqueKey(OrderNoUtil.PROD_PREFIX));
        prodOrder.setWorkNo(stockProdOrderService.generateWorkNo());
        prodOrder.setMatCode(plan.getMatCode());
        prodOrder.setMatName(plan.getMatName());
        prodOrder.setQuantity(plan.getPlanQuantity());
        prodOrder.setWorkshopCode(plan.getWorkshopCode());
        prodOrder.setPlanStartDate(plan.getPlanStartDate());
        prodOrder.setPlanEndDate(plan.getPlanEndDate());
        prodOrder.setCustomerOrderNo(plan.getCustomerOrderNo());
        prodOrder.setPlanNo(plan.getPlanNo());
        prodOrder.setOrderStatus(ProdOrderStatusEnum.PLANNED.getValue());
        prodOrder.setCreateBy(username);
        prodOrder.setCreateTime(DateUtils.getNowDate());
        stockProdOrderMapper.insertStockProdOrder(prodOrder);

        // 更新计划状态为执行中
        plan.setPlanStatus(STATUS_EXECUTING);
        plan.setUpdateBy(username);
        plan.setUpdateTime(DateUtils.getNowDate());
        stockProdPlanMapper.updateStockProdPlan(plan);

        return prodOrder.getOrderNo();
    }

    /**
     * BOM展开后校验物料库存
     * @param bomList BOM子项列表
     * @param parentQty 父项生产数量
     * @param warnings 库存不足警告信息收集器
     */
    private void checkBomStock(List<BaseMatBom> bomList, BigDecimal parentQty, List<String> warnings) {
        for (BaseMatBom bom : bomList) {
            BigDecimal childQty = bom.getChildMatNum();
            BigDecimal fatherQty = bom.getFatherMatNum();
            BigDecimal needQty = childQty.multiply(parentQty);
            if (fatherQty != null && fatherQty.compareTo(BigDecimal.ZERO) > 0 && fatherQty.compareTo(BigDecimal.ONE) != 0) {
                needQty = needQty.divide(fatherQty, 6, RoundingMode.HALF_UP);
            }

            if ("Y".equals(bom.getIsFictitious())) {
                // 虚拟件：递归展开
                List<BaseMatBom> childBomList = baseMatBomService.selectBaseMatBomByFatherMatCode(bom.getChildMatCode());
                if (CollectionUtils.isNotEmpty(childBomList)) {
                    checkBomStock(childBomList, needQty, warnings);
                }
            } else {
                // 叶子物料：查询库存
                StockInfo query = new StockInfo();
                query.setMatCode(bom.getChildMatCode());
                List<StockInfo> stockList = stockInfoMapper.selectStockInfoList(query);
                BigDecimal totalStock = BigDecimal.ZERO;
                if (CollectionUtils.isNotEmpty(stockList)) {
                    for (StockInfo info : stockList) {
                        if (info.getQuantity() != null) {
                            totalStock = totalStock.add(info.getQuantity());
                        }
                    }
                }
                if (totalStock.compareTo(needQty) < 0) {
                    String childName = StringUtils.isNotEmpty(bom.getChildMatName()) ? bom.getChildMatName() : bom.getChildMatCode();
                    warnings.add(childName + "（" + bom.getChildMatCode() + "）需要 " + needQty.stripTrailingZeros().toPlainString()
                            + "，当前库存 " + totalStock.stripTrailingZeros().toPlainString());
                }
            }
        }
    }

    /**
     * 完成计划
     */
    @Override
    public AjaxResult completePlan(String username, Long planId) {
        StockProdPlan plan = stockProdPlanMapper.selectStockProdPlanByPlanId(planId);
        if (plan == null) {
            return AjaxResult.error("计划不存在");
        }
        if (!STATUS_EXECUTING.equals(plan.getPlanStatus())) {
            return AjaxResult.error("只有执行中的计划才能完成");
        }
        // 统计关联工单的实际完成数量
        refreshPlanProgress(plan);
        plan.setPlanStatus(STATUS_COMPLETED);
        plan.setUpdateBy(username);
        plan.setUpdateTime(DateUtils.getNowDate());
        stockProdPlanMapper.updateStockProdPlan(plan);
        return AjaxResult.success("计划已完成");
    }

    /**
     * 取消计划
     */
    @Override
    public AjaxResult cancelPlan(String username, Long planId) {
        StockProdPlan plan = stockProdPlanMapper.selectStockProdPlanByPlanId(planId);
        if (plan == null) {
            return AjaxResult.error("计划不存在");
        }
        if (STATUS_COMPLETED.equals(plan.getPlanStatus()) || STATUS_CANCELLED.equals(plan.getPlanStatus())) {
            return AjaxResult.error("已完成或已取消的计划不能取消");
        }
        plan.setPlanStatus(STATUS_CANCELLED);
        plan.setUpdateBy(username);
        plan.setUpdateTime(DateUtils.getNowDate());
        stockProdPlanMapper.updateStockProdPlan(plan);
        return AjaxResult.success("计划已取消");
    }

    /**
     * 填充计划附加信息（车间名称、状态标签、关联工单统计）
     */
    private void fillPlanInfo(StockProdPlan plan) {
        // 车间名称
        if (StringUtils.isNotEmpty(plan.getWorkshopCode())) {
            plan.setWorkshopName(baseWorkshopService.selectBaseWorkshopByWorkshopCode(plan.getWorkshopCode()));
        }
        // 状态标签
        plan.setPlanStatusLabel(getPlanStatusLabel(plan.getPlanStatus()));
        // 关联工单统计
        if (StringUtils.isNotEmpty(plan.getPlanNo())) {
            StockProdOrder query = new StockProdOrder();
            query.setPlanNo(plan.getPlanNo());
            List<StockProdOrder> orders = stockProdOrderMapper.selectStockProdOrderList(query);
            int total = 0, completed = 0, ongoing = 0, planned = 0;
            BigDecimal actualQty = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(orders)) {
                total = orders.size();
                for (StockProdOrder order : orders) {
                    String status = order.getOrderStatus();
                    if (ProdOrderStatusEnum.COMPLETED.getValue().equals(status)
                            || ProdOrderStatusEnum.CLOSED.getValue().equals(status)) {
                        completed++;
                        if (order.getActualQuantity() != null) {
                            actualQty = actualQty.add(order.getActualQuantity());
                        }
                    } else if (ProdOrderStatusEnum.ONGOING.getValue().equals(status)) {
                        ongoing++;
                    } else if (ProdOrderStatusEnum.PLANNED.getValue().equals(status)) {
                        planned++;
                    }
                }
            }
            plan.setTotalOrderCount(total);
            plan.setCompletedOrderCount(completed);
            plan.setOngoingOrderCount(ongoing);
            plan.setPlannedOrderCount(planned);
            plan.setActualQuantity(actualQty);
            // 计算完成率
            if (plan.getPlanQuantity() != null && plan.getPlanQuantity().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal rate = actualQty.multiply(new BigDecimal("100"))
                        .divide(plan.getPlanQuantity(), 2, RoundingMode.HALF_UP);
                plan.setCompletionRate(rate);
            }
        }
    }

    /**
     * 刷新计划进度（更新实际数量和完成率到数据库）
     */
    private void refreshPlanProgress(StockProdPlan plan) {
        if (StringUtils.isEmpty(plan.getPlanNo())) return;
        StockProdOrder query = new StockProdOrder();
        query.setPlanNo(plan.getPlanNo());
        List<StockProdOrder> orders = stockProdOrderMapper.selectStockProdOrderList(query);
        BigDecimal actualQty = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(orders)) {
            for (StockProdOrder order : orders) {
                if (order.getActualQuantity() != null) {
                    actualQty = actualQty.add(order.getActualQuantity());
                }
            }
        }
        plan.setActualQuantity(actualQty);
        if (plan.getPlanQuantity() != null && plan.getPlanQuantity().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal rate = actualQty.multiply(new BigDecimal("100"))
                    .divide(plan.getPlanQuantity(), 2, RoundingMode.HALF_UP);
            plan.setCompletionRate(rate);
        }
    }

    /**
     * 获取计划状态中文标签
     */
    private String getPlanStatusLabel(String status) {
        if (status == null) return "";
        switch (status) {
            case STATUS_DRAFT: return "草稿";
            case STATUS_CONFIRMED: return "已确认";
            case STATUS_EXECUTING: return "执行中";
            case STATUS_COMPLETED: return "已完成";
            case STATUS_CANCELLED: return "已取消";
            default: return status;
        }
    }
}

