package com.ruoyi.stock.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.bean.request.StockOutRequestBody;
import com.ruoyi.common.bean.typeEnum.InOrderTypeEnum;
import com.ruoyi.common.bean.typeEnum.OrderStatusEnum;
import com.ruoyi.common.bean.typeEnum.OutOrderTypeEnum;
import com.ruoyi.common.bean.typeEnum.StockRecordTypeEnum;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.OrderNoUtil;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.stock.domain.*;
import com.ruoyi.stock.domain.stats.StockOutStats;
import com.ruoyi.stock.mapper.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.stock.service.IStockOutOrderService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 出库单Service业务层处理
 *
 * @author summer
 * @date 2026-02-15
 */
@Service
public class StockOutOrderServiceImpl implements IStockOutOrderService {
    @Autowired
    private StockOutOrderMapper stockOutOrderMapper;
    @Autowired
    private StockOutDetailMapper stockOutDetailMapper;
    @Autowired
    private StockMatLabelMapper stockMatLabelMapper;
    @Autowired
    private StockInfoMapper stockInfoMapper;
    @Autowired
    private StockRecordMapper stockRecordMapper;
    @Autowired
    private StockInOrderMapper stockInOrderMapper;
    @Autowired
    private StockInDetailMapper stockInDetailMapper;

    /**
     * 查询出库单数量
     */
    @Override
    public Map<String, Long> selectStockOutOrderTotal(Date selectDate){
        return stockOutOrderMapper.selectStockOutOrderTotal(selectDate);
    }

    /**
     * 查询出库单
     *
     * @param orderId 出库单主键
     * @return 出库单
     */
    @Override
    public StockOutOrder selectStockOutOrderByOrderId(Long orderId) {
        return stockOutOrderMapper.selectStockOutOrderByOrderId(orderId);
    }

    /**
     * 查询出库单
     *
     * @param orderNo
     * @return 出库单
     */
    @Override
    public StockOutOrder selectStockOutOrderByOrderNo(String orderNo){
        return stockOutOrderMapper.selectStockOutOrderByOrderNo(orderNo);
    }

    /**
     * 查询出库单列表
     *
     * @param stockOutOrder 出库单
     * @return 出库单
     */
    @Override
    public List<StockOutOrder> selectStockOutOrderList(StockOutOrder stockOutOrder) {
        return stockOutOrderMapper.selectStockOutOrderList(stockOutOrder);
    }

    /**
     * 新增出库单
     *
     * @param stockOutOrder 出库单
     * @return 结果
     */
    @Override
    public int insertStockOutOrder(String username, StockOutOrder stockOutOrder) {
        //出库单
        String orderNo = OrderNoUtil.getOutOrderNo(stockOutOrder.getOrderType());
        Date nowDate = DateUtils.getNowDate();
        stockOutOrder.setOrderNo(orderNo);
        stockOutOrder.setOrderStatus(OrderStatusEnum.CREATED.getValue());
        stockOutOrder.setCreateBy(username);
        stockOutOrder.setCreateTime(nowDate);
        //出库单详情
        List<StockOutDetail> detailList = stockOutOrder.getDetailList();
        if(CollectionUtils.isNotEmpty(detailList)){
            int i = 1;
            for(StockOutDetail detail : detailList){
                detail.setLineNo(i);
                detail.setWarehouseCode(stockOutOrder.getWarehouseCode());
                detail.setWorkshopCode(stockOutOrder.getWorkshopCode());
                detail.setProdOrderNo(stockOutOrder.getProdOrderNo());
                detail.setOrderNo(orderNo);
                detail.setCreateBy(username);
                detail.setCreateTime(nowDate);
                i++;
            }
        }
        stockOutDetailMapper.insertStockOutDetailList(detailList);
        return stockOutOrderMapper.insertStockOutOrder(stockOutOrder);
    }

    /**
     * 修改出库单
     *
     * @param stockOutOrder 出库单
     * @return 结果
     */
    @Override
    public int updateStockOutOrder(StockOutOrder stockOutOrder) {
        stockOutOrder.setUpdateTime(DateUtils.getNowDate());
        return stockOutOrderMapper.updateStockOutOrder(stockOutOrder);
    }

    /**
     * 批量删除出库单
     *
     * @param orderIds 需要删除的出库单主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStockOutOrderByOrderIds(Long[] orderIds) {
        if(ArrayUtils.isNotEmpty(orderIds)){
            for(Long orderId : orderIds){
                stockOutDetailMapper.deleteStockOutDetailByOrderId(orderId);
            }
        }
        return stockOutOrderMapper.deleteStockOutOrderByOrderIds(orderIds);
    }

    /**
     * 删除出库单信息
     *
     * @param orderId 出库单主键
     * @return 结果
     */
    @Override
    public int deleteStockOutOrderByOrderId(Long orderId) {
        return stockOutOrderMapper.deleteStockOutOrderByOrderId(orderId);
    }

    /**
     * 扫码提交出库单-出库
     * 按物料编码领取，自动从库存中扣减
     */
    @Override
    @Transactional
    public AjaxResult submitStockOut(String username, StockOutRequestBody stockOutRequestBody){
        String orderNo = stockOutRequestBody.getOrderNo();
        Map<String, BigDecimal> receivedMap = stockOutRequestBody.getReceivedMap();
        StockOutOrder stockOutOrder = stockOutOrderMapper.selectStockOutOrderByOrderNo(orderNo);
        if(stockOutOrder == null || MapUtils.isEmpty(receivedMap)){
            return AjaxResult.error("系统繁忙，请稍后再试！");
        }
        Date nowDate = DateUtils.getNowDate();
        String warehouseCode = stockOutOrder.getWarehouseCode();
        String workshopCode = stockOutOrder.getWorkshopCode();

        // 查询出库单详情，获取批次和供应商信息用于精确扣减库存
        List<StockOutDetail> outDetails = stockOutDetailMapper.selectStockOutDetailListByOrderNo(orderNo);
        Map<String, StockOutDetail> detailMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(outDetails)) {
            for (StockOutDetail detail : outDetails) {
                detailMap.put(detail.getMatCode(), detail);
            }
        }

        // 按物料编码处理领取
        for(Map.Entry<String, BigDecimal> entry : receivedMap.entrySet()){
            String matCode = entry.getKey();
            BigDecimal receivedQuantity = entry.getValue();

            if(receivedQuantity == null || receivedQuantity.compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }

            // 更新出库单详情的已领数量
            stockOutDetailMapper.updateReceivedQuantity(orderNo, matCode, username, nowDate, receivedQuantity);

            // 从详情中获取批次和供应商信息，精确扣减库存
            StockOutDetail outDetail = detailMap.get(matCode);
            if (outDetail != null) {
                // 按仓库+物料+批次+供应商精确扣减库存
                stockInfoMapper.updateQuantityByMatCodeAndBatch(warehouseCode, matCode,
                    outDetail.getBatch(), outDetail.getSupplierCode(), receivedQuantity);
            } else {
                // 兜底：只按仓库+物料扣减
                stockInfoMapper.updateQuantityByMatCode(warehouseCode, matCode, receivedQuantity);
            }

            // 新增库存操作流水信息
            StockRecord record = new StockRecord();
            record.setMatCode(matCode);
            record.setWarehouseCode(warehouseCode);
            record.setWorkshopCode(workshopCode);
            record.setRecordType(StockRecordTypeEnum.getStockOutRecordType(stockOutOrder.getOrderType()));
            record.setQuantity(receivedQuantity);
            record.setOrderNo(orderNo);
            record.setCreateBy(username);
            record.setCreateTime(nowDate);
            stockRecordMapper.insertStockRecord(record);
        }

        // 检查出库单是否全部领取完成
        List<StockOutDetail> detailList = stockOutDetailMapper.selectStockOutDetailListByOrderNo(orderNo);
        boolean allReceived = true;
        if(CollectionUtils.isNotEmpty(detailList)){
            for(StockOutDetail detail : detailList){
                if(detail.getQuantity().compareTo(detail.getReceivedQuantity()) > 0){
                    allReceived = false;
                    break;
                }
            }
        }

        // 更新出库单状态
        stockOutOrder.setOrderStatus(allReceived ? OrderStatusEnum.RECEIVED.getValue() : OrderStatusEnum.PICKING.getValue());
        stockOutOrder.setUpdateBy(username);
        stockOutOrder.setUpdateTime(nowDate);
        stockOutOrderMapper.updateStockOutOrder(stockOutOrder);

        // 如果是调拨出库单且全部出库完成，自动生成调拨入库单
        if (allReceived && OutOrderTypeEnum.ALLOT.getValue().equals(stockOutOrder.getOrderType())) {
            // 创建调拨入库单
            StockInOrder inOrder = new StockInOrder();
            inOrder.setOrderType(InOrderTypeEnum.ALLOT.getValue());
            inOrder.setOrderStatus(OrderStatusEnum.CREATED.getValue());
            inOrder.setAllotNo(stockOutOrder.getAllotNo());
            inOrder.setWarehouseCode(stockOutOrder.getDestWarehouseCode()); // 目标仓库
            inOrder.setCreateBy(username);
            inOrder.setCreateTime(nowDate);

            // 生成入库单号
            String inOrderNo = OrderNoUtil.generate(OrderNoUtil.OrderPrefix.IN_ALLOT);
            inOrder.setOrderNo(inOrderNo);

            // 查询出库单详情，创建对应的入库单详情
            List<StockOutDetail> outDetails = stockOutDetailMapper.selectStockOutDetailListByOrderNo(orderNo);
            List<StockInDetail> inDetails = new ArrayList<>();
            int lineNo = 1;
            for (StockOutDetail outDetail : outDetails) {
                StockInDetail inDetail = new StockInDetail();
                inDetail.setOrderNo(inOrderNo);
                inDetail.setLineNo(lineNo++);
                inDetail.setWarehouseCode(stockOutOrder.getDestWarehouseCode()); // 目标仓库
                inDetail.setMatCode(outDetail.getMatCode());
                inDetail.setMatName(outDetail.getMatName());
                inDetail.setFdCode(outDetail.getFdCode());
                inDetail.setFigNum(outDetail.getFigNum());
                inDetail.setMatGroup(outDetail.getMatGroup());
                inDetail.setMatClass(outDetail.getMatClass());
                inDetail.setUnitCode(outDetail.getUnitCode());
                inDetail.setBatch(outDetail.getBatch());
                inDetail.setSupplierCode(outDetail.getSupplierCode());
                inDetail.setSupplierName(outDetail.getSupplierName());
                inDetail.setQuantity(outDetail.getReceivedQuantity());
                inDetail.setQualifiedQuantity(outDetail.getReceivedQuantity());
                inDetail.setStockInQuantity(BigDecimal.ZERO);
                inDetail.setCreateBy(username);
                inDetail.setCreateTime(nowDate);
                inDetails.add(inDetail);
            }
            inOrder.setDetailList(inDetails);
            stockInOrderMapper.insertStockInOrder(inOrder);
            stockInDetailMapper.insertStockInDetailList(inDetails);

            // 入库单保持"已创建"状态，等待扫码确认入库
            // 后续在入库模块扫码确认后才会执行入库操作
        }

        return AjaxResult.success("提交成功");
    }

    /**
     * 出库统计
     */
    @Override
    public List<StockOutStats> selectStockOutStatsList(String matCode, String matName){
        return stockOutDetailMapper.selectStockOutStatsList(matCode, matName);
    }

    /**
     * 新增出库单并返回完整信息（用于调拨出库单生成）
     */
    @Override
    @Transactional
    public StockOutOrder insertStockOutOrderAndReturn(String username, StockOutOrder stockOutOrder) {
        insertStockOutOrder(username, stockOutOrder);
        return stockOutOrder;
    }

}
