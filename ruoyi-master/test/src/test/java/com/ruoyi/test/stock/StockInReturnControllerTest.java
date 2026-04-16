package com.ruoyi.test.stock;

import com.ruoyi.stock.domain.StockInReturn;
import com.ruoyi.stock.domain.StockInReturnDetail;
import com.ruoyi.stock.service.IStockInReturnService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.stock.StockInReturnController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 入库退货Controller测试
 * 实际接口路径: /stock/inReturn
 */
public class StockInReturnControllerTest extends BaseControllerTest {

    @Mock
    private IStockInReturnService stockInReturnService;

    @InjectMocks
    private StockInReturnController stockInReturnController;

    @Override
    protected Object getController() {
        return stockInReturnController;
    }

    private StockInReturn createTestInReturn() {
        StockInReturn ret = new StockInReturn();
        ret.setReturnId(1L);
        ret.setReturnNo("IR202604020001");  // 正确字段名
        ret.setOrderNo("PI202604020001");   // 正确字段名
        ret.setWarehouseCode("WH001");
        ret.setReturnType("1");
        ret.setReturnStatus("0");
        return ret;
    }

    @Test
    @DisplayName("查询入库退货列表")
    void testList() throws Exception {
        List<StockInReturn> list = new ArrayList<>();
        list.add(createTestInReturn());

        when(stockInReturnService.selectStockInReturnList(any(StockInReturn.class))).thenReturn(list);

        performGet("/stock/inReturn/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询入库退货")
    void testGetInfo() throws Exception {
        StockInReturn ret = createTestInReturn();
        when(stockInReturnService.selectStockInReturnByReturnId(anyLong())).thenReturn(ret);

        performGet("/stock/inReturn/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增入库退货")
    void testAdd() throws Exception {
        StockInReturn ret = createTestInReturn();
        when(stockInReturnService.insertStockInReturn(anyString(), any(StockInReturn.class)))
                .thenReturn(1);

        performPost("/stock/inReturn", ret)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除入库退货")
    void testRemove() throws Exception {
        when(stockInReturnService.deleteStockInReturnByReturnIds(any(Long[].class))).thenReturn(1);

        performDelete("/stock/inReturn/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}