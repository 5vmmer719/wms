package com.ruoyi.test.stock;

import com.ruoyi.common.bean.typeEnum.AllotProgressEnum;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.stock.domain.StockAllotDetail;
import com.ruoyi.stock.domain.StockAllotOrder;
import com.ruoyi.stock.service.IStockAllotDetailService;
import com.ruoyi.stock.service.IStockAllotOrderService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.stock.StockAllotOrderController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 调拨单Controller测试
 * 实际接口路径: /stock/allotOrder
 */
public class StockAllotOrderControllerTest extends BaseControllerTest {

    @Mock
    private IStockAllotOrderService stockAllotOrderService;

    @Mock
    private IStockAllotDetailService stockAllotDetailService;

    @InjectMocks
    private StockAllotOrderController stockAllotOrderController;

    @Override
    protected Object getController() {
        return stockAllotOrderController;
    }

    private StockAllotOrder createTestAllotOrder() {
        StockAllotOrder order = new StockAllotOrder();
        order.setAllotId(1L);
        order.setAllotNo("A202604020001");  // 正确字段名
        order.setSrcWarehouseCode("WH001");  // 正确字段名
        order.setDestWarehouseCode("WH002"); // 正确字段名
        order.setAllotProgress(AllotProgressEnum.CREATED.getValue()); // 正确字段名
        return order;
    }

    private StockAllotDetail createTestAllotDetail() {
        StockAllotDetail detail = new StockAllotDetail();
        detail.setDetailId(1L);
        detail.setAllotNo("A202604020001");  // 正确字段名
        detail.setLineNo(1);
        detail.setMatCode("MAT001");
        detail.setMatName("测试物料");
        detail.setQuantity(new BigDecimal("100"));
        return detail;
    }

    @Test
    @DisplayName("查询调拨单列表")
    void testList() throws Exception {
        List<StockAllotOrder> list = new ArrayList<>();
        list.add(createTestAllotOrder());

        when(stockAllotOrderService.selectStockAllotOrderList(any(StockAllotOrder.class))).thenReturn(list);

        performGet("/stock/allotOrder/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询调拨单")
    void testGetInfo() throws Exception {
        StockAllotOrder order = createTestAllotOrder();
        List<StockAllotDetail> details = new ArrayList<>();
        details.add(createTestAllotDetail());

        when(stockAllotOrderService.selectStockAllotOrderByAllotId(anyLong())).thenReturn(order);
        when(stockAllotDetailService.selectStockAllotDetailListByAllotNo(anyString())).thenReturn(details);

        performGet("/stock/allotOrder/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增调拨单")
    void testAdd() throws Exception {
        StockAllotOrder order = createTestAllotOrder();
        List<StockAllotDetail> details = new ArrayList<>();
        details.add(createTestAllotDetail());
        order.setDetailList(details);

        when(stockAllotOrderService.insertStockAllotOrder(anyString(), any(StockAllotOrder.class))).thenReturn(1);

        performPost("/stock/allotOrder", order)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改调拨单")
    void testEdit() throws Exception {
        StockAllotOrder order = createTestAllotOrder();
        when(stockAllotOrderService.updateStockAllotOrder(any(StockAllotOrder.class))).thenReturn(1);

        performPut("/stock/allotOrder", order)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除调拨单")
    void testRemove() throws Exception {
        when(stockAllotOrderService.deleteStockAllotOrderByAllotIds(any(Long[].class))).thenReturn(1);

        performDelete("/stock/allotOrder/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("确认调拨单-生成出库单")
    void testConfirmAllot() throws Exception {
        StockAllotOrder order = createTestAllotOrder();

        when(stockAllotOrderService.confirmAllotAndCreateOutOrder(anyString(), anyString()))
                .thenReturn(AjaxResult.success("确认成功", order));

        performPostForm("/stock/allotOrder/confirmAllot", "allotNo", "A202604020001")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("确认调拨单-调拨单不存在")
    void testConfirmAllotNotFound() throws Exception {
        when(stockAllotOrderService.confirmAllotAndCreateOutOrder(anyString(), anyString()))
                .thenReturn(AjaxResult.error("调拨单不存在"));

        performPostForm("/stock/allotOrder/confirmAllot", "allotNo", "A999999999999")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("查询调拨单-扫码端")
    void testGetInfoByAllotNo() throws Exception {
        StockAllotOrder order = createTestAllotOrder();
        List<StockAllotDetail> details = new ArrayList<>();
        details.add(createTestAllotDetail());

        when(stockAllotOrderService.selectStockAllotOrderByAllotNo(anyString())).thenReturn(order);
        when(stockAllotDetailService.selectStockAllotDetailListByAllotNo(anyString())).thenReturn(details);

        performGet("/stock/allotOrder/m/A202604020001")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}