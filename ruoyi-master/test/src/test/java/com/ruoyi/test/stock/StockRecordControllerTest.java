package com.ruoyi.test.stock;

import com.ruoyi.stock.domain.StockRecord;
import com.ruoyi.stock.service.IStockRecordService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.stock.StockRecordController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 库存流水Controller测试
 * 实际接口路径: /stock/record
 */
public class StockRecordControllerTest extends BaseControllerTest {

    @Mock
    private IStockRecordService stockRecordService;

    @InjectMocks
    private StockRecordController stockRecordController;

    @Override
    protected Object getController() {
        return stockRecordController;
    }

    private StockRecord createTestRecord() {
        StockRecord record = new StockRecord();
        record.setRecordId(1L);
        record.setWarehouseCode("WH001");
        record.setMatCode("MAT001");
        record.setMatName("测试物料");
        record.setRecordType("1");
        record.setQuantity(new BigDecimal("100"));
        record.setOrderNo("PI202604020001");
        return record;
    }

    @Test
    @DisplayName("查询库存流水列表")
    void testList() throws Exception {
        List<StockRecord> list = new ArrayList<>();
        list.add(createTestRecord());

        when(stockRecordService.selectStockRecordList(any(StockRecord.class))).thenReturn(list);

        performGet("/stock/record/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("按仓库查询流水")
    void testListByWarehouse() throws Exception {
        List<StockRecord> list = new ArrayList<>();
        list.add(createTestRecord());

        when(stockRecordService.selectStockRecordList(any(StockRecord.class))).thenReturn(list);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/stock/record/list")
                .param("warehouseCode", "WH001")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("按物料编码查询流水")
    void testListByMatCode() throws Exception {
        List<StockRecord> list = new ArrayList<>();
        list.add(createTestRecord());

        when(stockRecordService.selectStockRecordList(any(StockRecord.class))).thenReturn(list);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/stock/record/list")
                .param("matCode", "MAT001")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("按单号查询流水")
    void testListByOrderNo() throws Exception {
        List<StockRecord> list = new ArrayList<>();
        list.add(createTestRecord());

        when(stockRecordService.selectStockRecordList(any(StockRecord.class))).thenReturn(list);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/stock/record/list")
                .param("orderNo", "PI202604020001")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}