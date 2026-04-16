package com.ruoyi.test.stock;

import com.ruoyi.common.bean.request.StockOutRequestBody;
import com.ruoyi.common.bean.typeEnum.OrderStatusEnum;
import com.ruoyi.common.bean.typeEnum.OutOrderTypeEnum;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.stock.domain.StockOutDetail;
import com.ruoyi.stock.domain.StockOutOrder;
import com.ruoyi.stock.service.IStockOutDetailService;
import com.ruoyi.stock.service.IStockOutOrderService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.stock.StockOutOrderController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 出库单Controller测试
 * 实际接口路径: /stock/outOrder
 */
public class StockOutOrderControllerTest extends BaseControllerTest {

    @Mock
    private IStockOutOrderService stockOutOrderService;

    @Mock
    private IStockOutDetailService stockOutDetailService;

    @InjectMocks
    private StockOutOrderController stockOutOrderController;

    @Override
    protected Object getController() {
        return stockOutOrderController;
    }

    private StockOutOrder createTestOutOrder() {
        StockOutOrder order = new StockOutOrder();
        order.setOrderId(1L);  // 正确字段名
        order.setOrderNo("PO202604020001");
        order.setOrderType(OutOrderTypeEnum.PRODUCTION.getValue());
        order.setOrderStatus(OrderStatusEnum.CREATED.getValue());
        order.setWarehouseCode("WH001");
        order.setWorkshopCode("WS001");
        return order;
    }

    private StockOutDetail createTestOutDetail() {
        StockOutDetail detail = new StockOutDetail();
        detail.setDetailId(1L);
        detail.setOrderNo("PO202604020001");
        detail.setLineNo(1);
        detail.setMatCode("MAT001");
        detail.setMatName("测试物料");
        detail.setQuantity(new BigDecimal("100"));
        detail.setReceivedQuantity(BigDecimal.ZERO);
        detail.setUnitCode("PCS");
        return detail;
    }

    @Test
    @DisplayName("查询出库单列表")
    void testList() throws Exception {
        List<StockOutOrder> list = new ArrayList<>();
        list.add(createTestOutOrder());

        when(stockOutOrderService.selectStockOutOrderList(any(StockOutOrder.class))).thenReturn(list);

        performGet("/stock/outOrder/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询出库单")
    void testGetInfo() throws Exception {
        StockOutOrder order = createTestOutOrder();
        List<StockOutDetail> details = new ArrayList<>();
        details.add(createTestOutDetail());

        when(stockOutOrderService.selectStockOutOrderByOrderId(anyLong())).thenReturn(order);
        when(stockOutDetailService.selectStockOutDetailListByOrderNo(anyString())).thenReturn(details);

        performGet("/stock/outOrder/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增出库单")
    void testAdd() throws Exception {
        StockOutOrder order = createTestOutOrder();
        List<StockOutDetail> details = new ArrayList<>();
        details.add(createTestOutDetail());
        order.setDetailList(details);

        when(stockOutOrderService.insertStockOutOrder(anyString(), any(StockOutOrder.class))).thenReturn(1);

        performPost("/stock/outOrder", order)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改出库单")
    void testEdit() throws Exception {
        StockOutOrder order = createTestOutOrder();
        when(stockOutOrderService.updateStockOutOrder(any(StockOutOrder.class))).thenReturn(1);

        performPut("/stock/outOrder", order)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除出库单")
    void testRemove() throws Exception {
        when(stockOutOrderService.deleteStockOutOrderByOrderIds(any(Long[].class))).thenReturn(1);

        performDelete("/stock/outOrder/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("查询出库单-扫码端")
    void testGetInfoByOrderNo() throws Exception {
        StockOutOrder order = createTestOutOrder();
        List<StockOutDetail> details = new ArrayList<>();
        StockOutDetail detail = createTestOutDetail();
        detail.setQuantity(new BigDecimal("100"));
        detail.setReceivedQuantity(new BigDecimal("50"));
        details.add(detail);

        when(stockOutOrderService.selectStockOutOrderByOrderNo(anyString())).thenReturn(order);
        when(stockOutDetailService.selectStockOutDetailListByOrderNo(anyString())).thenReturn(details);

        performGet("/stock/outOrder/m/PO202604020001")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("扫码提交出库单")
    void testSubmitStockOut() throws Exception {
        StockOutRequestBody request = new StockOutRequestBody();
        request.setOrderNo("PO202604020001");
        Map<String, BigDecimal> receivedMap = new HashMap<>();
        receivedMap.put("MAT001", new BigDecimal("10"));
        request.setReceivedMap(receivedMap);

        when(stockOutOrderService.submitStockOut(anyString(), any(StockOutRequestBody.class)))
                .thenReturn(AjaxResult.success("提交成功"));

        performPost("/stock/outOrder/submitStockOut", request)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("扫码提交出库单-库存不足")
    void testSubmitStockOutInsufficientStock() throws Exception {
        StockOutRequestBody request = new StockOutRequestBody();
        request.setOrderNo("PO202604020001");
        Map<String, BigDecimal> receivedMap = new HashMap<>();
        receivedMap.put("MAT001", new BigDecimal("10000"));
        request.setReceivedMap(receivedMap);

        when(stockOutOrderService.submitStockOut(anyString(), any(StockOutRequestBody.class)))
                .thenReturn(AjaxResult.error("物料[MAT001]库存不足！"));

        performPost("/stock/outOrder/submitStockOut", request)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}