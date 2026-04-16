package com.ruoyi.test.stock;

import com.ruoyi.stock.domain.StockInfo;
import com.ruoyi.stock.service.IStockInfoService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.stock.StockInfoController;
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
import static org.mockito.Mockito.when;

/**
 * 库存信息Controller测试
 * 实际接口路径: /stock/info
 */
public class StockInfoControllerTest extends BaseControllerTest {

    @Mock
    private IStockInfoService stockInfoService;

    @InjectMocks
    private StockInfoController stockInfoController;

    @Override
    protected Object getController() {
        return stockInfoController;
    }

    private StockInfo createTestStockInfo() {
        StockInfo info = new StockInfo();
        info.setInfoId(1L);
        info.setWarehouseCode("WH001");
        info.setLocationCode("LOC001");
        info.setMatCode("MAT001");
        info.setMatName("测试物料");
        info.setQuantity(new BigDecimal("100"));
        info.setBatch("B001");
        info.setSupplierCode("SUP001");
        return info;
    }

    @Test
    @DisplayName("查询库存信息列表")
    void testList() throws Exception {
        List<StockInfo> list = new ArrayList<>();
        list.add(createTestStockInfo());

        when(stockInfoService.selectStockInfoList(any(StockInfo.class))).thenReturn(list);

        performGet("/stock/info/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询库存信息")
    void testGetInfo() throws Exception {
        StockInfo info = createTestStockInfo();
        when(stockInfoService.selectStockInfoByInfoId(anyLong())).thenReturn(info);

        performGet("/stock/info/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("查询库存汇总列表")
    void testStatsList() throws Exception {
        List<StockInfo> list = new ArrayList<>();
        StockInfo info = createTestStockInfo();
        info.setStatsQuantity(new BigDecimal("100"));
        list.add(info);

        when(stockInfoService.selectStockInfoStatsList(any(StockInfo.class))).thenReturn(list);

        performGet("/stock/info/statsList")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("按仓库查询库存")
    void testListByWarehouse() throws Exception {
        List<StockInfo> list = new ArrayList<>();
        list.add(createTestStockInfo());

        when(stockInfoService.selectStockInfoList(any(StockInfo.class))).thenReturn(list);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/stock/info/list")
                .param("warehouseCode", "WH001")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("按物料编码查询库存")
    void testListByMatCode() throws Exception {
        List<StockInfo> list = new ArrayList<>();
        list.add(createTestStockInfo());

        when(stockInfoService.selectStockInfoList(any(StockInfo.class))).thenReturn(list);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/stock/info/list")
                .param("matCode", "MAT001")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}