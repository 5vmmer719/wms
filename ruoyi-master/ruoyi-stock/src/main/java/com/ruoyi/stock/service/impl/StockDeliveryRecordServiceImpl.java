package com.ruoyi.stock.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.bean.typeEnum.CustomerOrderStatusEnum;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.OrderNoUtil;
import com.ruoyi.stock.domain.StockCustomerOrder;
import com.ruoyi.stock.domain.StockCustomerOrderDetail;
import com.ruoyi.stock.domain.StockDeliveryDetail;
import com.ruoyi.stock.domain.StockDeliveryRecord;
import com.ruoyi.stock.mapper.StockCustomerOrderDetailMapper;
import com.ruoyi.stock.mapper.StockCustomerOrderMapper;
import com.ruoyi.stock.mapper.StockDeliveryDetailMapper;
import com.ruoyi.stock.mapper.StockDeliveryRecordMapper;
import com.ruoyi.stock.service.IStockDeliveryRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交付记录Service业务层处理
 *
 * @author wms
 */
@Service
public class StockDeliveryRecordServiceImpl implements IStockDeliveryRecordService {

    @Autowired
    private StockDeliveryRecordMapper deliveryRecordMapper;

    @Autowired
    private StockDeliveryDetailMapper deliveryDetailMapper;

    @Autowired
    private StockCustomerOrderMapper customerOrderMapper;

    @Autowired
    private StockCustomerOrderDetailMapper customerOrderDetailMapper;

    @Override
    public StockDeliveryRecord selectStockDeliveryRecordByDeliveryId(Long deliveryId) {
        return deliveryRecordMapper.selectStockDeliveryRecordByDeliveryId(deliveryId);
    }

    @Override
    public List<StockDeliveryRecord> selectStockDeliveryRecordList(StockDeliveryRecord stockDeliveryRecord) {
        return deliveryRecordMapper.selectStockDeliveryRecordList(stockDeliveryRecord);
    }

    /**
     * 新增交付记录（含明细）
     */
    @Override
    @Transactional
    public int insertStockDeliveryRecord(String username, StockDeliveryRecord record) {
        record.setDeliveryNo(OrderNoUtil.generate(OrderNoUtil.OrderPrefix.DELIVERY));
        record.setDeliveryStatus("pending");
        record.setCreateBy(username);
        record.setCreateTime(DateUtils.getNowDate());

        // 处理明细
        List<StockDeliveryDetail> detailList = record.getDetailList();
        BigDecimal totalQty = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(detailList)) {
            int lineNo = 1;
            for (StockDeliveryDetail detail : detailList) {
                detail.setDeliveryNo(record.getDeliveryNo());
                detail.setLineNo(lineNo++);
                detail.setCreateBy(username);
                detail.setCreateTime(DateUtils.getNowDate());
                if (detail.getQuantity() != null) {
                    totalQty = totalQty.add(detail.getQuantity());
                }
            }
            deliveryDetailMapper.insertStockDeliveryDetailList(detailList);
        }
        record.setTotalQuantity(totalQty);

        return deliveryRecordMapper.insertStockDeliveryRecord(record);
    }

    @Override
    public int updateStockDeliveryRecord(StockDeliveryRecord record) {
        record.setUpdateTime(DateUtils.getNowDate());
        return deliveryRecordMapper.updateStockDeliveryRecord(record);
    }

    @Override
    @Transactional
    public int deleteStockDeliveryRecordByDeliveryIds(Long[] deliveryIds) {
        return deliveryRecordMapper.deleteStockDeliveryRecordByDeliveryIds(deliveryIds);
    }

    /**
     * 发货操作：pending -> shipped
     * 同时回写客户订单明细的 delivered_qty
     */
    @Override
    @Transactional
    public AjaxResult ship(String username, Long deliveryId) {
        StockDeliveryRecord record = deliveryRecordMapper.selectStockDeliveryRecordByDeliveryId(deliveryId);
        if (record == null) {
            return AjaxResult.error("交付记录不存在");
        }
        if (!"pending".equals(record.getDeliveryStatus())) {
            return AjaxResult.error("只有待发货状态的交付单才能发货");
        }

        Date now = DateUtils.getNowDate();

        // 更新交付状态
        record.setDeliveryStatus("shipped");
        record.setDeliveryDate(now);
        record.setUpdateBy(username);
        record.setUpdateTime(now);
        deliveryRecordMapper.updateStockDeliveryRecord(record);

        // 回写客户订单明细的 delivered_qty
        List<StockDeliveryDetail> detailList = deliveryDetailMapper.selectDetailListByDeliveryNo(record.getDeliveryNo());
        if (CollectionUtils.isNotEmpty(detailList)) {
            List<StockCustomerOrderDetail> orderDetails = customerOrderDetailMapper.selectDetailListByOrderNo(record.getOrderNo());
            if (CollectionUtils.isNotEmpty(orderDetails)) {
                for (StockDeliveryDetail dd : detailList) {
                    for (StockCustomerOrderDetail od : orderDetails) {
                        if (dd.getMatCode() != null && dd.getMatCode().equals(od.getMatCode())) {
                            BigDecimal delivered = od.getDeliveredQty() != null ? od.getDeliveredQty() : BigDecimal.ZERO;
                            BigDecimal newDelivered = delivered.add(dd.getQuantity() != null ? dd.getQuantity() : BigDecimal.ZERO);
                            od.setDeliveredQty(newDelivered);
                            od.setUpdateBy(username);
                            od.setUpdateTime(now);
                            customerOrderDetailMapper.updateStockCustomerOrderDetail(od);
                            break;
                        }
                    }
                }
            }

            // 检查是否全部交付完成，如果是则更新订单状态为 delivered
            checkAndUpdateOrderStatus(record.getOrderNo(), username, now);
        }

        return AjaxResult.success("发货成功");
    }

    /**
     * 签收：shipped -> received
     */
    @Override
    public AjaxResult receive(String username, Long deliveryId) {
        StockDeliveryRecord record = deliveryRecordMapper.selectStockDeliveryRecordByDeliveryId(deliveryId);
        if (record == null) {
            return AjaxResult.error("交付记录不存在");
        }
        if (!"shipped".equals(record.getDeliveryStatus())) {
            return AjaxResult.error("只有已发货状态的交付单才能签收");
        }

        record.setDeliveryStatus("received");
        record.setUpdateBy(username);
        record.setUpdateTime(DateUtils.getNowDate());
        deliveryRecordMapper.updateStockDeliveryRecord(record);

        return AjaxResult.success("签收成功");
    }

    /**
     * 查询交付详情（含明细）
     */
    @Override
    public Map<String, Object> getDetail(Long deliveryId) {
        Map<String, Object> result = new HashMap<>();
        StockDeliveryRecord record = deliveryRecordMapper.selectStockDeliveryRecordByDeliveryId(deliveryId);
        if (record == null) {
            return result;
        }
        setStatusLabel(record);
        result.put("record", record);

        List<StockDeliveryDetail> detailList = deliveryDetailMapper.selectDetailListByDeliveryNo(record.getDeliveryNo());
        result.put("detailList", detailList);

        return result;
    }

    /**
     * 检查客户订单是否全部交付完成
     */
    private void checkAndUpdateOrderStatus(String orderNo, String username, Date now) {
        List<StockCustomerOrderDetail> orderDetails = customerOrderDetailMapper.selectDetailListByOrderNo(orderNo);
        if (CollectionUtils.isEmpty(orderDetails)) {
            return;
        }

        boolean allDelivered = true;
        for (StockCustomerOrderDetail od : orderDetails) {
            BigDecimal qty = od.getQuantity() != null ? od.getQuantity() : BigDecimal.ZERO;
            BigDecimal delivered = od.getDeliveredQty() != null ? od.getDeliveredQty() : BigDecimal.ZERO;
            if (delivered.compareTo(qty) < 0) {
                allDelivered = false;
                break;
            }
        }

        if (allDelivered) {
            StockCustomerOrder order = customerOrderMapper.selectStockCustomerOrderByOrderNo(orderNo);
            if (order != null && !CustomerOrderStatusEnum.DELIVERED.getValue().equals(order.getOrderStatus())
                    && !CustomerOrderStatusEnum.CLOSED.getValue().equals(order.getOrderStatus())) {
                order.setOrderStatus(CustomerOrderStatusEnum.DELIVERED.getValue());
                order.setActualDeliveryDate(now);
                order.setUpdateBy(username);
                order.setUpdateTime(now);
                customerOrderMapper.updateStockCustomerOrder(order);
            }
        }
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

