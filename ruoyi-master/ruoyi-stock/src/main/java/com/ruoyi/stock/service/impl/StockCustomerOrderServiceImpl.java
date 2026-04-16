package com.ruoyi.stock.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.base.domain.BaseCustomer;
import com.ruoyi.base.mapper.BaseCustomerMapper;
import com.ruoyi.common.bean.typeEnum.CustomerOrderStatusEnum;
import com.ruoyi.common.bean.typeEnum.ProdOrderStatusEnum;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.OrderNoUtil;
import com.ruoyi.stock.domain.StockCustomerOrder;
import com.ruoyi.stock.domain.StockCustomerOrderDetail;
import com.ruoyi.stock.domain.StockProdOrder;
import com.ruoyi.stock.mapper.StockCustomerOrderDetailMapper;
import com.ruoyi.stock.mapper.StockCustomerOrderMapper;
import com.ruoyi.stock.mapper.StockProdOrderMapper;
import com.ruoyi.stock.service.IStockCustomerOrderService;
import com.ruoyi.stock.service.IStockProdOrderService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客户订单Service业务层处理
 *
 * @author wms
 */
@Service
public class StockCustomerOrderServiceImpl implements IStockCustomerOrderService {

    @Autowired
    private StockCustomerOrderMapper stockCustomerOrderMapper;

    @Autowired
    private StockCustomerOrderDetailMapper stockCustomerOrderDetailMapper;

    @Autowired
    private BaseCustomerMapper baseCustomerMapper;

    @Autowired
    private StockProdOrderMapper stockProdOrderMapper;

    @Autowired
    private IStockProdOrderService stockProdOrderService;

    @Override
    public StockCustomerOrder selectStockCustomerOrderByOrderId(Long orderId) {
        return stockCustomerOrderMapper.selectStockCustomerOrderByOrderId(orderId);
    }

    @Override
    public StockCustomerOrder selectStockCustomerOrderByOrderNo(String orderNo) {
        return stockCustomerOrderMapper.selectStockCustomerOrderByOrderNo(orderNo);
    }

    @Override
    public List<StockCustomerOrder> selectStockCustomerOrderList(StockCustomerOrder stockCustomerOrder) {
        return stockCustomerOrderMapper.selectStockCustomerOrderList(stockCustomerOrder);
    }

    /**
     * 新增客户订单（含明细）
     */
    @Override
    @Transactional
    public int insertStockCustomerOrder(String username, StockCustomerOrder stockCustomerOrder) {
        // 自动生成订单号
        stockCustomerOrder.setOrderNo(OrderNoUtil.generate(OrderNoUtil.OrderPrefix.CUSTOMER_ORDER));
        stockCustomerOrder.setOrderStatus(CustomerOrderStatusEnum.CREATED.getValue());
        stockCustomerOrder.setCreateBy(username);
        stockCustomerOrder.setCreateTime(DateUtils.getNowDate());

        // 自动填充客户名称
        if (StringUtils.isNotEmpty(stockCustomerOrder.getCustomerCode()) && StringUtils.isEmpty(stockCustomerOrder.getCustomerName())) {
            BaseCustomer customer = baseCustomerMapper.selectBaseCustomerByCustomerCode(stockCustomerOrder.getCustomerCode());
            if (customer != null) {
                stockCustomerOrder.setCustomerName(customer.getCustomerName());
            }
        }

        // 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<StockCustomerOrderDetail> detailList = stockCustomerOrder.getDetailList();
        if (CollectionUtils.isNotEmpty(detailList)) {
            int lineNo = 1;
            for (StockCustomerOrderDetail detail : detailList) {
                detail.setOrderNo(stockCustomerOrder.getOrderNo());
                detail.setLineNo(lineNo++);
                detail.setDeliveredQty(BigDecimal.ZERO);
                detail.setCreateBy(username);
                detail.setCreateTime(DateUtils.getNowDate());
                // 计算行金额
                if (detail.getUnitPrice() != null && detail.getQuantity() != null) {
                    detail.setAmount(detail.getUnitPrice().multiply(detail.getQuantity()));
                    totalAmount = totalAmount.add(detail.getAmount());
                }
            }
            stockCustomerOrder.setTotalAmount(totalAmount);
            stockCustomerOrderDetailMapper.insertStockCustomerOrderDetailList(detailList);
        }

        return stockCustomerOrderMapper.insertStockCustomerOrder(stockCustomerOrder);
    }

    /**
     * 修改客户订单（含明细：先删后插）
     */
    @Override
    @Transactional
    public int updateStockCustomerOrder(StockCustomerOrder stockCustomerOrder) {
        stockCustomerOrder.setUpdateTime(DateUtils.getNowDate());

        // 重新计算总金额并更新明细
        List<StockCustomerOrderDetail> detailList = stockCustomerOrder.getDetailList();
        if (detailList != null) {
            // 先删后插
            stockCustomerOrderDetailMapper.deleteDetailByOrderNo(stockCustomerOrder.getOrderNo());
            if (CollectionUtils.isNotEmpty(detailList)) {
                BigDecimal totalAmount = BigDecimal.ZERO;
                int lineNo = 1;
                for (StockCustomerOrderDetail detail : detailList) {
                    detail.setOrderNo(stockCustomerOrder.getOrderNo());
                    detail.setLineNo(lineNo++);
                    if (detail.getDeliveredQty() == null) {
                        detail.setDeliveredQty(BigDecimal.ZERO);
                    }
                    detail.setCreateBy(stockCustomerOrder.getUpdateBy());
                    detail.setCreateTime(DateUtils.getNowDate());
                    if (detail.getUnitPrice() != null && detail.getQuantity() != null) {
                        detail.setAmount(detail.getUnitPrice().multiply(detail.getQuantity()));
                        totalAmount = totalAmount.add(detail.getAmount());
                    }
                }
                stockCustomerOrder.setTotalAmount(totalAmount);
                stockCustomerOrderDetailMapper.insertStockCustomerOrderDetailList(detailList);
            }
        }

        return stockCustomerOrderMapper.updateStockCustomerOrder(stockCustomerOrder);
    }

    @Override
    public int deleteStockCustomerOrderByOrderId(Long orderId) {
        return stockCustomerOrderMapper.deleteStockCustomerOrderByOrderId(orderId);
    }

    @Override
    @Transactional
    public int deleteStockCustomerOrderByOrderIds(Long[] orderIds) {
        return stockCustomerOrderMapper.deleteStockCustomerOrderByOrderIds(orderIds);
    }

    /**
     * 查询订单详情（含明细列表和关联生产工单）
     */
    @Override
    public Map<String, Object> getDetail(Long orderId) {
        Map<String, Object> result = new HashMap<>();
        StockCustomerOrder order = stockCustomerOrderMapper.selectStockCustomerOrderByOrderId(orderId);
        if (order == null) {
            return result;
        }
        order.setOrderStatusLabel(CustomerOrderStatusEnum.getLabel(order.getOrderStatus()));
        result.put("order", order);

        // 查询明细
        List<StockCustomerOrderDetail> detailList = stockCustomerOrderDetailMapper.selectDetailListByOrderNo(order.getOrderNo());
        result.put("detailList", detailList);

        // 查询关联的生产工单
        if (CollectionUtils.isNotEmpty(detailList)) {
            StockProdOrder query = new StockProdOrder();
            query.setCustomerOrderNo(order.getOrderNo());
            List<StockProdOrder> prodOrders = stockProdOrderMapper.selectStockProdOrderList(query);
            if (CollectionUtils.isNotEmpty(prodOrders)) {
                for (StockProdOrder po : prodOrders) {
                    po.setOrderStatusLabel(ProdOrderStatusEnum.getLabel(po.getOrderStatus()));
                }
            }
            result.put("prodOrders", prodOrders);
        }

        return result;
    }

    /**
     * 确认订单：created -> confirmed
     */
    @Override
    public AjaxResult confirmOrder(String username, Long orderId) {
        StockCustomerOrder order = stockCustomerOrderMapper.selectStockCustomerOrderByOrderId(orderId);
        if (order == null) {
            return AjaxResult.error("订单不存在");
        }
        if (!CustomerOrderStatusEnum.CREATED.getValue().equals(order.getOrderStatus())) {
            return AjaxResult.error("只有已创建状态的订单才能确认");
        }
        order.setOrderStatus(CustomerOrderStatusEnum.CONFIRMED.getValue());
        order.setUpdateBy(username);
        order.setUpdateTime(DateUtils.getNowDate());
        stockCustomerOrderMapper.updateStockCustomerOrder(order);
        return AjaxResult.success("订单已确认");
    }

    /**
     * 从客户订单一键生成生产工单
     * 遍历订单明细，为每行未关联工单的明细创建一个生产工单
     */
    @Override
    @Transactional
    public AjaxResult generateProdOrder(String username, Long orderId) {
        StockCustomerOrder order = stockCustomerOrderMapper.selectStockCustomerOrderByOrderId(orderId);
        if (order == null) {
            return AjaxResult.error("订单不存在");
        }
        String status = order.getOrderStatus();
        if (!CustomerOrderStatusEnum.CONFIRMED.getValue().equals(status)
                && !CustomerOrderStatusEnum.PRODUCING.getValue().equals(status)) {
            return AjaxResult.error("只有已确认或生产中状态的订单才能生成工单");
        }

        List<StockCustomerOrderDetail> detailList = stockCustomerOrderDetailMapper.selectDetailListByOrderNo(order.getOrderNo());
        if (CollectionUtils.isEmpty(detailList)) {
            return AjaxResult.error("订单无明细数据");
        }

        int count = 0;
        Date now = DateUtils.getNowDate();
        StringBuilder prodOrderNos = new StringBuilder();

        for (StockCustomerOrderDetail detail : detailList) {
            // 跳过已关联工单的明细行
            if (StringUtils.isNotEmpty(detail.getProdOrderNo())) {
                continue;
            }

            // 创建生产工单
            StockProdOrder prodOrder = new StockProdOrder();
            prodOrder.setOrderNo(OrderNoUtil.generateUniqueKey(OrderNoUtil.PROD_PREFIX));
            prodOrder.setWorkNo(stockProdOrderService.generateWorkNo());
            prodOrder.setMatCode(detail.getMatCode());
            prodOrder.setMatName(detail.getMatName());
            prodOrder.setQuantity(detail.getQuantity());
            prodOrder.setOrderStatus(ProdOrderStatusEnum.PLANNED.getValue());
            prodOrder.setCustomerOrderNo(order.getOrderNo());
            prodOrder.setCreateBy(username);
            prodOrder.setCreateTime(now);
            stockProdOrderMapper.insertStockProdOrder(prodOrder);

            // 回写明细行的关联工单号
            detail.setProdOrderNo(prodOrder.getOrderNo());
            detail.setUpdateBy(username);
            detail.setUpdateTime(now);
            stockCustomerOrderDetailMapper.updateStockCustomerOrderDetail(detail);

            if (prodOrderNos.length() > 0) {
                prodOrderNos.append(", ");
            }
            prodOrderNos.append(prodOrder.getOrderNo());
            count++;
        }

        if (count == 0) {
            return AjaxResult.error("所有明细行已关联生产工单，无需重复生成");
        }

        // 更新订单状态为生产中
        order.setOrderStatus(CustomerOrderStatusEnum.PRODUCING.getValue());
        order.setUpdateBy(username);
        order.setUpdateTime(now);
        stockCustomerOrderMapper.updateStockCustomerOrder(order);

        return AjaxResult.success("已生成 " + count + " 个生产工单：" + prodOrderNos.toString());
    }

    /**
     * 关闭订单
     */
    @Override
    public AjaxResult closeOrder(String username, Long orderId) {
        StockCustomerOrder order = stockCustomerOrderMapper.selectStockCustomerOrderByOrderId(orderId);
        if (order == null) {
            return AjaxResult.error("订单不存在");
        }
        order.setOrderStatus(CustomerOrderStatusEnum.CLOSED.getValue());
        order.setUpdateBy(username);
        order.setUpdateTime(DateUtils.getNowDate());
        stockCustomerOrderMapper.updateStockCustomerOrder(order);
        return AjaxResult.success("订单已关闭");
    }
}

