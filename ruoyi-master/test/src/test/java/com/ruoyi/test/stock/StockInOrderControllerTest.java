package com.ruoyi.test.stock;

import com.ruoyi.common.bean.typeEnum.InOrderTypeEnum;
import com.ruoyi.common.bean.typeEnum.OrderStatusEnum;
import com.ruoyi.stock.domain.StockInDetail;
import com.ruoyi.stock.domain.StockInOrder;
import com.ruoyi.stock.service.IStockInDetailService;
import com.ruoyi.stock.service.IStockInOrderService;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.stock.StockInOrderController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 入库单Controller测试
 * 实际接口路径: /stock/inOrder
 */
public class StockInOrderControllerTest extends BaseControllerTest {

    @Mock
    private IStockInOrderService stockInOrderService;

    @Mock
    private IStockInDetailService stockInDetailService;

    @Mock
    private ISysDictDataService sysDictDataService;

    @InjectMocks
    private StockInOrderController stockInOrderController;

    @Override
    protected Object getController() {
        return stockInOrderController;
    }

    private StockInOrder createTestInOrder() {
        StockInOrder order = new StockInOrder();
        order.setOrderId(1L);  // 正确字段名
        order.setOrderNo("PI202604020001");
        order.setOrderType(InOrderTypeEnum.PURCHASE.getValue());
        order.setOrderStatus(OrderStatusEnum.CREATED.getValue());
        order.setWarehouseCode("WH001");
        order.setCheckStatus("1");
        return order;
    }

    private StockInDetail createTestInDetail() {
        StockInDetail detail = new StockInDetail();
        detail.setDetailId(1L);
        detail.setOrderNo("PI202604020001");
        detail.setLineNo(1);
        detail.setMatCode("MAT001");
        detail.setMatName("测试物料");
        detail.setQuantity(new BigDecimal("100"));
        detail.setQualifiedQuantity(new BigDecimal("100"));
        detail.setStockInQuantity(BigDecimal.ZERO);
        detail.setUnitCode("PCS");
        return detail;
    }

    @Test
    @DisplayName("查询入库单列表")
    void testList() throws Exception {
        List<StockInOrder> list = new ArrayList<>();
        list.add(createTestInOrder());

        when(stockInOrderService.selectStockInOrderList(any(StockInOrder.class))).thenReturn(list);

        performGet("/stock/inOrder/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询入库单")
    void testGetInfo() throws Exception {
        StockInOrder order = createTestInOrder();
        List<StockInDetail> details = new ArrayList<>();
        details.add(createTestInDetail());

        when(stockInOrderService.selectStockInOrderByOrderId(anyLong())).thenReturn(order);
        when(stockInDetailService.selectStockInDetailListByOrderNo(anyString())).thenReturn(details);

        performGet("/stock/inOrder/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增入库单")
    void testAdd() throws Exception {
        StockInOrder order = createTestInOrder();
        List<StockInDetail> details = new ArrayList<>();
        details.add(createTestInDetail());
        order.setDetailList(details);

        when(stockInOrderService.insertStockInOrder(anyString(), any(StockInOrder.class))).thenReturn(1);

        performPost("/stock/inOrder", order)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改入库单")
    void testEdit() throws Exception {
        StockInOrder order = createTestInOrder();
        when(stockInOrderService.updateStockInOrder(any(StockInOrder.class))).thenReturn(1);

        performPut("/stock/inOrder", order)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除入库单")
    void testRemove() throws Exception {
        when(stockInOrderService.deleteStockInOrderByOrderIds(any(Long[].class))).thenReturn(1);

        performDelete("/stock/inOrder/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("查询入库单-扫码端")
    void testGetInfoByOrderNo() throws Exception {
        StockInOrder order = createTestInOrder();
        order.setCheckStatus("2"); // 已质检
        List<StockInDetail> details = new ArrayList<>();
        StockInDetail detail = createTestInDetail();
        detail.setQualifiedQuantity(new BigDecimal("100"));
        detail.setStockInQuantity(new BigDecimal("50"));
        details.add(detail);

        when(stockInOrderService.selectStockInOrderByOrderNo(anyString())).thenReturn(order);
        when(stockInDetailService.selectStockInDetailListByOrderNo(anyString())).thenReturn(details);

        performGet("/stock/inOrder/m/PI202604020001")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("入库单质检")
    void testCheck() throws Exception {
        List<StockInDetail> details = new ArrayList<>();
        details.add(createTestInDetail());

        when(stockInOrderService.inOrderCheck(anyString(), anyList())).thenReturn(1);

        performPut("/stock/inOrder/check", details)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("扫码提交入库单")
    void testSubmitStockIn() throws Exception {
        StockInOrder order = createTestInOrder();
        List<StockInDetail> details = new ArrayList<>();
        details.add(createTestInDetail());
        order.setDetailList(details);

        when(stockInOrderService.submitStockIn(anyString(), any(StockInOrder.class)))
                .thenReturn(com.ruoyi.common.core.domain.AjaxResult.success("提交成功"));

        performPost("/stock/inOrder/submitStockIn", order)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}