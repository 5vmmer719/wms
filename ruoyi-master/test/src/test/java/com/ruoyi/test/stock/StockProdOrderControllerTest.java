package com.ruoyi.test.stock;

import com.ruoyi.stock.domain.StockProdOrder;
import com.ruoyi.stock.service.IStockProdOrderService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.stock.StockProdOrderController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * 生产订单Controller测试
 * 实际接口路径: /stock/prodOrder
 */
public class StockProdOrderControllerTest extends BaseControllerTest {

    @Mock
    private IStockProdOrderService stockProdOrderService;

    @InjectMocks
    private StockProdOrderController stockProdOrderController;

    @Override
    protected Object getController() {
        return stockProdOrderController;
    }

    private StockProdOrder createTestProdOrder() {
        StockProdOrder order = new StockProdOrder();
        order.setOrderId(1L);
        order.setOrderNo("PRD202604020001");
        order.setMatCode("PRODUCT001");
        order.setMatName("成品物料");
        order.setWorkshopCode("WS001");
        order.setOrderStatus("0");
        return order;
    }

    @Test
    @DisplayName("查询生产订单列表")
    void testList() throws Exception {
        List<StockProdOrder> list = new ArrayList<>();
        list.add(createTestProdOrder());

        when(stockProdOrderService.selectStockProdOrderList(any(StockProdOrder.class))).thenReturn(list);

        performGet("/stock/prodOrder/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
//    @DisplayName("根据ID查询生产订单")
//    void testGetInfo() throws Exception {
//        StockProdOrder order = createTestProdOrder();
//        when(stockProdOrderService.selectStockProdOrderByProdOrderId(anyLong())).thenReturn(order);
//
//        performGet("/stock/prodOrder/1")
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }

    @Test
    @DisplayName("新增生产订单")
    void testAdd() throws Exception {
        StockProdOrder order = createTestProdOrder();
        when(stockProdOrderService.insertStockProdOrder(any(StockProdOrder.class))).thenReturn(1);

        performPost("/stock/prodOrder", order)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改生产订单")
    void testEdit() throws Exception {
        StockProdOrder order = createTestProdOrder();
        when(stockProdOrderService.updateStockProdOrder(any(StockProdOrder.class))).thenReturn(1);

        performPut("/stock/prodOrder", order)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除生产订单")
    void testRemove() throws Exception {
        when(stockProdOrderService.deleteStockProdOrderByOrderIds(any(Long[].class))).thenReturn(1);

        performDelete("/stock/prodOrder/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}