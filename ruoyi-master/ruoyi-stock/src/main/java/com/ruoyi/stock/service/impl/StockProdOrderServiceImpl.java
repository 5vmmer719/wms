package com.ruoyi.stock.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.base.domain.BaseMat;
import com.ruoyi.base.domain.BaseMatBom;
import com.ruoyi.base.domain.BaseProcessRoute;
import com.ruoyi.base.domain.BaseWarehouse;
import com.ruoyi.base.service.IBaseMatBomService;
import com.ruoyi.base.service.IBaseMatService;
import com.ruoyi.base.service.IBaseWarehouseService;
import com.ruoyi.base.service.IBaseWorkshopService;
import com.ruoyi.base.service.IBaseProcessRouteService;
import com.ruoyi.common.bean.typeEnum.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.OrderNoUtil;
import com.ruoyi.stock.domain.*;
import com.ruoyi.stock.mapper.*;
import com.ruoyi.stock.service.IStockInfoService;
import com.ruoyi.stock.service.IStockOutOrderService;
import com.ruoyi.system.service.ISysDictDataService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.stock.service.IStockProdOrderService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 生产订单Service业务层处理
 *
 * @author summer
 * @date 2022-07-25
 */
@Service
public class StockProdOrderServiceImpl implements IStockProdOrderService {
    @Autowired
    private StockProdOrderMapper stockProdOrderMapper;
    @Autowired
    private StockInOrderMapper stockInOrderMapper;
    @Autowired
    private StockInDetailMapper stockInDetailMapper;
    @Autowired
    private StockOutOrderMapper stockOutOrderMapper;
    @Autowired
    private StockOutDetailMapper stockOutDetailMapper;
    @Autowired
    private IStockOutOrderService stockOutOrderService;
    @Autowired
    private IBaseMatBomService baseMatBomService;
    @Autowired
    private IBaseMatService baseMatService;
    @Autowired
    private IBaseWarehouseService baseWarehouseService;
    @Autowired
    private IBaseWorkshopService baseWorkshopService;
    @Autowired
    private IBaseProcessRouteService baseProcessRouteService;
    @Autowired
    private IStockInfoService stockInfoService;
    @Autowired
    private StockInfoMapper stockInfoMapper;
    @Autowired
    private StockRecordMapper stockRecordMapper;
    @Autowired
    private ISysDictDataService sysDictDataService;

    private static final String dictType = "base_mat_unit";

    /**
     * 查询生产订单
     */
    @Override
    public StockProdOrder selectStockProdOrderByOrderId(Long orderId) {
        return stockProdOrderMapper.selectStockProdOrderByOrderId(orderId);
    }

    /**
     * 查询生产订单
     */
    @Override
    public StockProdOrder selectStockProdOrderByOrderNo(String orderNo){
        return stockProdOrderMapper.selectStockProdOrderByOrderNo(orderNo);
    }

    /**
     * 查询生产订单列表
     */
    @Override
    public List<StockProdOrder> selectStockProdOrderList(StockProdOrder stockProdOrder) {
        return stockProdOrderMapper.selectStockProdOrderList(stockProdOrder);
    }

    /**
     * 新增生产订单
     */
    @Override
    public int insertStockProdOrder(StockProdOrder stockProdOrder) {
        stockProdOrder.setCreateTime(DateUtils.getNowDate());
        return stockProdOrderMapper.insertStockProdOrder(stockProdOrder);
    }

    /**
     * 修改生产订单
     */
    @Override
    public int updateStockProdOrder(StockProdOrder stockProdOrder) {
        stockProdOrder.setUpdateTime(DateUtils.getNowDate());
        return stockProdOrderMapper.updateStockProdOrder(stockProdOrder);
    }

    /**
     * 批量删除生产订单
     */
    @Override
    public int deleteStockProdOrderByOrderIds(Long[] orderIds) {
        return stockProdOrderMapper.deleteStockProdOrderByOrderIds(orderIds);
    }

    /**
     * 删除生产订单信息
     */
    @Override
    public int deleteStockProdOrderByOrderId(Long orderId) {
        return stockProdOrderMapper.deleteStockProdOrderByOrderId(orderId);
    }

    /**
     * 排产 - 设置计划时间和优先级
     */
    @Override
    public AjaxResult schedule(String username, StockProdOrder stockProdOrder) {
        StockProdOrder existing = stockProdOrderMapper.selectStockProdOrderByOrderId(stockProdOrder.getOrderId());
        if (existing == null) {
            return AjaxResult.error("工单不存在");
        }
        if (!ProdOrderStatusEnum.PLANNED.getValue().equals(existing.getOrderStatus())) {
            return AjaxResult.error("只有待排产状态的工单才能进行排产");
        }
        existing.setPlanStartDate(stockProdOrder.getPlanStartDate());
        existing.setPlanEndDate(stockProdOrder.getPlanEndDate());
        existing.setPriority(stockProdOrder.getPriority());
        existing.setUpdateBy(username);
        existing.setUpdateTime(DateUtils.getNowDate());
        stockProdOrderMapper.updateStockProdOrder(existing);
        return AjaxResult.success("排产成功");
    }

    /**
     * 开工 - 状态从planned变为ongoing，记录实际开始时间
     * 自动根据BOM展开生成领料出库单
     */
    @Override
    @Transactional
    public AjaxResult start(String username, Long orderId) {
        StockProdOrder existing = stockProdOrderMapper.selectStockProdOrderByOrderId(orderId);
        if (existing == null) {
            return AjaxResult.error("工单不存在");
        }
        if (!ProdOrderStatusEnum.PLANNED.getValue().equals(existing.getOrderStatus())) {
            return AjaxResult.error("只有待排产状态的工单才能开工");
        }

        // 1. BOM展开：根据成品物料编码查询子物料
        String matCode = existing.getMatCode();
        List<BaseMatBom> bomList = baseMatBomService.selectBaseMatBomByFatherMatCode(matCode);
        if (CollectionUtils.isEmpty(bomList)) {
            return AjaxResult.error("该物料未维护BOM，无法自动生成领料出库单");
        }

        // 递归展开BOM（处理虚拟件），得到扁平化的叶子物料列表
        List<BomFlatItem> flatItems = flattenBom(bomList, existing.getQuantity());
        if (CollectionUtils.isEmpty(flatItems)) {
            return AjaxResult.error("BOM展开后无有效物料，请检查BOM配置");
        }

        // 2. 构建出库单明细，为每个物料匹配推荐仓库和货位
        List<StockOutDetail> detailList = new ArrayList<>();
        for (BomFlatItem item : flatItems) {
            StockOutDetail detail = new StockOutDetail();
            detail.setMatCode(item.matCode);
            BaseMat baseMat = baseMatService.selectBaseMatByMatCode(item.matCode);
            if (baseMat != null) {
                detail.setMatName(baseMat.getMatName());
                detail.setFdCode(baseMat.getFdCode());
                detail.setFigNum(baseMat.getFigNum());
                detail.setMatGroup(baseMat.getMatGroup());
                detail.setMatClass(baseMat.getMatClass());
                detail.setUnitCode(baseMat.getUnitCode());
                detail.setUnitName(sysDictDataService.selectDictLabel(dictType, baseMat.getUnitCode()));
            } else {
                detail.setMatName(item.matCode);
            }
            detail.setQuantity(item.quantity);
            detail.setReceivedQuantity(BigDecimal.ZERO);

            // 根据物料组获取推荐仓库
            List<BaseWarehouse> recommendWarehouses = baseWarehouseService.getRecommendWarehouseList(detail.getMatGroup());
            if (CollectionUtils.isNotEmpty(recommendWarehouses)) {
                for (BaseWarehouse wh : recommendWarehouses) {
                    String locationCode = stockInfoService.selectRecommendLocation(detail.getMatCode(), wh.getWarehouseCode());
                    if (StringUtils.isNotEmpty(locationCode)) {
                        detail.setWarehouseCode(wh.getWarehouseCode());
                        detail.setLocationCode(locationCode);
                        break;
                    }
                }
                // 无库存时默认选第一个推荐仓库
                if (StringUtils.isEmpty(detail.getWarehouseCode())) {
                    detail.setWarehouseCode(recommendWarehouses.get(0).getWarehouseCode());
                }
            }

            detailList.add(detail);
        }

        // 3. 创建出库单
        StockOutOrder outOrder = new StockOutOrder();
        outOrder.setOrderType(OutOrderTypeEnum.PRODUCTION.getValue());
        outOrder.setProdOrderNo(existing.getOrderNo());
        outOrder.setWorkshopCode(existing.getWorkshopCode());
        outOrder.setMatCode(existing.getMatCode());
        outOrder.setMatName(existing.getMatName());
        outOrder.setQuantity(existing.getQuantity());
        // 从明细行中取第一个有仓库的值设置到出库单主表（用于详情展示）
        for (StockOutDetail d : detailList) {
            if (StringUtils.isNotEmpty(d.getWarehouseCode())) {
                outOrder.setWarehouseCode(d.getWarehouseCode());
                break;
            }
        }
        outOrder.setDetailList(detailList);
        stockOutOrderService.insertStockOutOrder(username, outOrder);

        // 4. 更新工单状态
        Date now = DateUtils.getNowDate();
        existing.setOrderStatus(ProdOrderStatusEnum.ONGOING.getValue());
        existing.setActualStartDate(now);
        existing.setUpdateBy(username);
        existing.setUpdateTime(now);
        stockProdOrderMapper.updateStockProdOrder(existing);

        return AjaxResult.success("开工成功，已自动生成领料出库单：" + outOrder.getOrderNo());
    }

    /**
     * BOM扁平化展开（递归处理虚拟件）
     */
    private List<BomFlatItem> flattenBom(List<BaseMatBom> bomList, BigDecimal parentQuantity) {
        List<BomFlatItem> result = new ArrayList<>();
        for (BaseMatBom bom : bomList) {
            // 子项数量 = BOM中子项数量 * 父项生产数量 / 父项BOM数量
            BigDecimal childQty = bom.getChildMatNum();
            BigDecimal fatherQty = bom.getFatherMatNum();
            BigDecimal actualQty = childQty.multiply(parentQuantity);
            if (fatherQty != null && fatherQty.compareTo(BigDecimal.ZERO) > 0 && fatherQty.compareTo(BigDecimal.ONE) != 0) {
                actualQty = actualQty.divide(fatherQty, 6, BigDecimal.ROUND_HALF_UP);
            }

            if ("Y".equals(bom.getIsFictitious())) {
                // 虚拟件：递归展开
                List<BaseMatBom> childBomList = baseMatBomService.selectBaseMatBomByFatherMatCode(bom.getChildMatCode());
                if (CollectionUtils.isNotEmpty(childBomList)) {
                    result.addAll(flattenBom(childBomList, actualQty));
                }
            } else {
                // 叶子物料：合并同物料
                mergeItem(result, bom.getChildMatCode(), actualQty);
            }
        }
        return result;
    }

    /**
     * 合并同物料的数量
     */
    private void mergeItem(List<BomFlatItem> list, String matCode, BigDecimal quantity) {
        for (BomFlatItem item : list) {
            if (matCode.equals(item.matCode)) {
                item.quantity = item.quantity.add(quantity);
                return;
            }
        }
        list.add(new BomFlatItem(matCode, quantity));
    }

    /**
     * BOM展开后的扁平化物料项
     */
    private static class BomFlatItem {
        String matCode;
        BigDecimal quantity;
        BomFlatItem(String matCode, BigDecimal quantity) {
            this.matCode = matCode;
            this.quantity = quantity;
        }
    }

    /**
     * 报工完工 - 状态从ongoing变为completed，记录实际完成数量和时间
     * 自动生成成品入库单并直接入库（增加库存+写入流水）
     */
    @Override
    @Transactional
    public AjaxResult complete(String username, StockProdOrder stockProdOrder) {
        StockProdOrder existing = stockProdOrderMapper.selectStockProdOrderByOrderId(stockProdOrder.getOrderId());
        if (existing == null) {
            return AjaxResult.error("工单不存在");
        }
        if (!ProdOrderStatusEnum.ONGOING.getValue().equals(existing.getOrderStatus())) {
            return AjaxResult.error("只有生产中状态的工单才能报工");
        }
        BigDecimal actualQuantity = stockProdOrder.getActualQuantity();
        if (actualQuantity == null || actualQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            return AjaxResult.error("实际完成数量必须大于0");
        }

        Date now = DateUtils.getNowDate();
        // 更新工单状态
        existing.setOrderStatus(ProdOrderStatusEnum.COMPLETED.getValue());
        existing.setActualQuantity(actualQuantity);
        existing.setActualEndDate(now);
        existing.setUpdateBy(username);
        existing.setUpdateTime(now);
        stockProdOrderMapper.updateStockProdOrder(existing);

        // 通过物料编码查询物料主数据，自动填充图号、分类、单位等信息
        BaseMat mat = baseMatService.selectBaseMatByMatCode(existing.getMatCode());

        // 自动生成生产批次号，格式：PB + yyyyMMdd + 4位随机数
        String prodBatch = OrderNoUtil.generateUniqueKey("PB");

        // 自动生成成品入库单（车间入库类型）
        StockInOrder inOrder = new StockInOrder();
        String inOrderNo = OrderNoUtil.getInOrderNo(InOrderTypeEnum.PRODUCTION.getValue());
        inOrder.setOrderNo(inOrderNo);
        inOrder.setOrderType(InOrderTypeEnum.PRODUCTION.getValue());
        inOrder.setCheckStatus(InOrderCheckStatusEnum.CHECKOUT.getValue()); // 生产入库跳过质检，直接设为已质检
        inOrder.setProdOrderNo(existing.getOrderNo()); // 关联生产工单号
        inOrder.setCreateBy(username);
        inOrder.setCreateTime(now);

        // 根据成品物料组匹配推荐仓库
        String warehouseCode = null;
        String locationCode = null;
        if (mat != null && StringUtils.isNotEmpty(mat.getMatGroup())) {
            List<BaseWarehouse> warehouses = baseWarehouseService.getRecommendWarehouseList(mat.getMatGroup());
            if (CollectionUtils.isNotEmpty(warehouses)) {
                // 尝试找到有货位的仓库
                for (BaseWarehouse wh : warehouses) {
                    String loc = stockInfoService.selectRecommendLocation(existing.getMatCode(), wh.getWarehouseCode());
                    if (StringUtils.isNotEmpty(loc)) {
                        warehouseCode = wh.getWarehouseCode();
                        locationCode = loc;
                        break;
                    }
                }
                // 无已有货位时用第一个推荐仓库
                if (warehouseCode == null) {
                    warehouseCode = warehouses.get(0).getWarehouseCode();
                }
            }
        }
        inOrder.setWarehouseCode(warehouseCode);

        // 创建入库明细 - 成品物料（通过物料编码自动填充完整信息）
        StockInDetail inDetail = new StockInDetail();
        inDetail.setOrderNo(inOrderNo);
        inDetail.setLineNo(1);
        inDetail.setMatCode(existing.getMatCode());
        inDetail.setMatName(existing.getMatName());
        inDetail.setQuantity(actualQuantity);
        inDetail.setQualifiedQuantity(actualQuantity);
        inDetail.setStockInQuantity(actualQuantity); // 直接入库，入库数量=实际完成数量
        inDetail.setWarehouseCode(warehouseCode);
        inDetail.setLocationCode(locationCode);
        inDetail.setBatch(prodBatch); // 自动生成的批次号
        if (mat != null) {
            inDetail.setUnitCode(mat.getUnitCode());
            inDetail.setFdCode(mat.getFdCode());
            inDetail.setFigNum(mat.getFigNum());
            inDetail.setMatGroup(mat.getMatGroup());
            inDetail.setMatClass(mat.getMatClass());
        }
        inDetail.setCreateBy(username);
        inDetail.setCreateTime(now);

        List<StockInDetail> detailList = new ArrayList<>();
        detailList.add(inDetail);
        inOrder.setDetailList(detailList);

        // 入库单状态直接设为已入库
        inOrder.setOrderStatus(OrderStatusEnum.ENTERED.getValue());
        stockInOrderMapper.insertStockInOrder(inOrder);
        stockInDetailMapper.insertStockInDetailList(detailList);

        // ===== 直接增加库存 =====
        // 查询是否已有相同物料+图号+仓库+货位的库存记录（精确匹配）
        StockInfo info = new StockInfo();
        info.setWarehouseCode(warehouseCode);
        info.setLocationCode(locationCode);
        info.setMatCode(existing.getMatCode());
        if (mat != null) {
            info.setFigNum(mat.getFigNum());
        }
        List<StockInfo> infoList = stockInfoMapper.selectStockInfoList(info);
        if (CollectionUtils.isNotEmpty(infoList)) {
            // 已有库存记录，累加数量，同时补全可能缺失的字段
            StockInfo existingInfo = infoList.get(0);
            existingInfo.setQuantity(actualQuantity.add(existingInfo.getQuantity()));
            // 补全之前可能缺失的字段
            if (mat != null) {
                if (StringUtils.isEmpty(existingInfo.getFdCode())) {
                    existingInfo.setFdCode(mat.getFdCode());
                }
                if (StringUtils.isEmpty(existingInfo.getFigNum())) {
                    existingInfo.setFigNum(mat.getFigNum());
                }
                if (StringUtils.isEmpty(existingInfo.getMatGroup())) {
                    existingInfo.setMatGroup(mat.getMatGroup());
                }
                if (StringUtils.isEmpty(existingInfo.getMatClass())) {
                    existingInfo.setMatClass(mat.getMatClass());
                }
                if (StringUtils.isEmpty(existingInfo.getUnitCode())) {
                    existingInfo.setUnitCode(mat.getUnitCode());
                }
            }
            existingInfo.setUpdateBy(username);
            existingInfo.setUpdateTime(now);
            stockInfoMapper.updateStockInfo(existingInfo);
        } else {
            // 新增库存记录（填充完整的物料信息）
            info.setMatName(existing.getMatName());
            info.setBatch(prodBatch);
            if (mat != null) {
                info.setFdCode(mat.getFdCode());
                info.setFigNum(mat.getFigNum());
                info.setMatGroup(mat.getMatGroup());
                info.setMatClass(mat.getMatClass());
                info.setUnitCode(mat.getUnitCode());
            }
            info.setQuantity(actualQuantity);
            info.setCreateBy(username);
            info.setCreateTime(now);
            stockInfoMapper.insertStockInfo(info);
        }

        // ===== 写入库存流水（填充完整信息） =====
        StockRecord record = new StockRecord();
        record.setRecordType(StockRecordTypeEnum.IN_PRODUCTION.getValue());
        record.setOrderNo(inOrderNo);
        record.setWarehouseCode(warehouseCode);
        record.setLocationCode(locationCode);
        record.setMatCode(existing.getMatCode());
        record.setMatName(existing.getMatName());
        record.setBatch(prodBatch);
        if (mat != null) {
            record.setFdCode(mat.getFdCode());
            record.setFigNum(mat.getFigNum());
            record.setMatGroup(mat.getMatGroup());
            record.setMatClass(mat.getMatClass());
            record.setUnitCode(mat.getUnitCode());
        }
        record.setQuantity(actualQuantity);
        record.setCreateBy(username);
        record.setCreateTime(now);
        stockRecordMapper.insertStockRecord(record);

        return AjaxResult.success("报工成功，成品已直接入库，入库单号：" + inOrderNo);
    }

    /**
     * 关闭工单 - 状态从completed变为closed
     */
    @Override
    public AjaxResult close(String username, Long orderId) {
        StockProdOrder existing = stockProdOrderMapper.selectStockProdOrderByOrderId(orderId);
        if (existing == null) {
            return AjaxResult.error("工单不存在");
        }
        if (!ProdOrderStatusEnum.COMPLETED.getValue().equals(existing.getOrderStatus())) {
            return AjaxResult.error("只有已完工状态的工单才能关闭");
        }
        existing.setOrderStatus(ProdOrderStatusEnum.CLOSED.getValue());
        existing.setUpdateBy(username);
        existing.setUpdateTime(DateUtils.getNowDate());
        stockProdOrderMapper.updateStockProdOrder(existing);
        return AjaxResult.success("工单已关闭");
    }

    /**
     * 查询工单详情（含关联出库单和入库单）
     */
    @Override
    public Map<String, Object> getDetail(Long orderId) {
        Map<String, Object> result = new HashMap<>();
        StockProdOrder prodOrder = stockProdOrderMapper.selectStockProdOrderByOrderId(orderId);
        if (prodOrder == null) {
            return result;
        }
        // 填充车间名称
        prodOrder.setWorkshopName(baseWorkshopService.selectBaseWorkshopByWorkshopCode(prodOrder.getWorkshopCode()));
        prodOrder.setOrderStatusLabel(ProdOrderStatusEnum.getLabel(prodOrder.getOrderStatus()));
        // 填充工艺路线名称
        if (StringUtils.isNotEmpty(prodOrder.getRouteCode())) {
            BaseProcessRoute route = baseProcessRouteService.selectBaseProcessRouteByRouteCode(prodOrder.getRouteCode());
            if (route != null) {
                prodOrder.setRouteName(route.getRouteName());
            }
        }
        result.put("prodOrder", prodOrder);

        // 查询关联出库单（通过prodOrderNo）
        StockOutOrder outQuery = new StockOutOrder();
        outQuery.setProdOrderNo(prodOrder.getOrderNo());
        List<StockOutOrder> outOrders = stockOutOrderMapper.selectStockOutOrderList(outQuery);
        if (CollectionUtils.isNotEmpty(outOrders)) {
            for (StockOutOrder outOrder : outOrders) {
                outOrder.setOrderStatusLabel(OrderStatusEnum.getLabel(outOrder.getOrderStatus()));
                // 如果出库单主表没有仓库（历史数据兼容），从明细行中获取
                if (StringUtils.isEmpty(outOrder.getWarehouseCode())) {
                    List<StockOutDetail> outDetails = stockOutDetailMapper.selectStockOutDetailListByOrderNo(outOrder.getOrderNo());
                    if (CollectionUtils.isNotEmpty(outDetails)) {
                        for (StockOutDetail d : outDetails) {
                            if (StringUtils.isNotEmpty(d.getWarehouseCode())) {
                                outOrder.setWarehouseCode(d.getWarehouseCode());
                                break;
                            }
                        }
                    }
                }
                outOrder.setWarehouseName(baseWarehouseService.selectBaseWarehouseNameByWarehouseCode(outOrder.getWarehouseCode()));
            }
        }
        result.put("outOrders", outOrders);

        // 查询关联入库单（通过prodOrderNo）
        StockInOrder inQuery = new StockInOrder();
        inQuery.setProdOrderNo(prodOrder.getOrderNo());
        List<StockInOrder> inOrders = stockInOrderMapper.selectStockInOrderList(inQuery);
        if (CollectionUtils.isNotEmpty(inOrders)) {
            for (StockInOrder inOrder : inOrders) {
                inOrder.setOrderStatusLabel(OrderStatusEnum.getLabel(inOrder.getOrderStatus()));
                inOrder.setCheckStatusLabel(InOrderCheckStatusEnum.getLabel(inOrder.getCheckStatus()));
            }
        }
        result.put("inOrders", inOrders);

        return result;
    }

    /**
     * 生成工令号（自动递增）
     * 格式：TGL-001, TGL-002, ...
     */
    @Override
    public synchronized String generateWorkNo() {
        String maxWorkNo = stockProdOrderMapper.selectMaxWorkNo();
        int nextSeq = 1;
        if (StringUtils.isNotEmpty(maxWorkNo)) {
            try {
                // 提取 TGL- 后面的数字部分
                String numPart = maxWorkNo.substring(maxWorkNo.lastIndexOf("-") + 1);
                nextSeq = Integer.parseInt(numPart) + 1;
            } catch (Exception e) {
                // 解析失败时从1开始
                nextSeq = 1;
            }
        }
        return String.format("TGL-%03d", nextSeq);
    }
}
