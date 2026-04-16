package com.ruoyi.test.stock;

import com.ruoyi.stock.domain.StockOutReturn;
import com.ruoyi.stock.service.IStockOutReturnService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.stock.StockOutReturnController;
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
 * 出库退库Controller测试
 * 实际接口路径: /stock/outReturn
 */
public class StockOutReturnControllerTest extends BaseControllerTest {

    @Mock
    private IStockOutReturnService stockOutReturnService;

    @InjectMocks
    private StockOutReturnController stockOutReturnController;

    @Override
    protected Object getController() {
        return stockOutReturnController;
    }

    private StockOutReturn createTestOutReturn() {
        StockOutReturn ret = new StockOutReturn();
        ret.setReturnId(1L);
        ret.setReturnNo("OR202604020001");  // 正确字段名
        ret.setOrderNo("PO202604020001");   // 正确字段名
        ret.setWarehouseCode("WH001");
        ret.setWorkshopCode("WS001");
        ret.setReturnType("1");
        ret.setReturnStatus("0");
        return ret;
    }

    @Test
    @DisplayName("查询出库退库列表")
    void testList() throws Exception {
        List<StockOutReturn> list = new ArrayList<>();
        list.add(createTestOutReturn());

        when(stockOutReturnService.selectStockOutReturnList(any(StockOutReturn.class))).thenReturn(list);

        performGet("/stock/outReturn/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询出库退库")
    void testGetInfo() throws Exception {
        StockOutReturn ret = createTestOutReturn();
        when(stockOutReturnService.selectStockOutReturnByReturnId(anyLong())).thenReturn(ret);

        performGet("/stock/outReturn/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增出库退库")
    void testAdd() throws Exception {
        StockOutReturn ret = createTestOutReturn();
        when(stockOutReturnService.insertStockOutReturn(anyString(), any(StockOutReturn.class)))
                .thenReturn(1);

        performPost("/stock/outReturn", ret)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除出库退库")
    void testRemove() throws Exception {
        when(stockOutReturnService.deleteStockOutReturnByReturnIds(any(Long[].class))).thenReturn(1);

        performDelete("/stock/outReturn/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}