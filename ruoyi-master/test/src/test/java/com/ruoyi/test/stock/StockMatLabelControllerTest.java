package com.ruoyi.test.stock;

import com.ruoyi.stock.domain.StockMatLabel;
import com.ruoyi.stock.service.IStockMatLabelService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.stock.StockMatLabelController;
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
 * 物料标签Controller测试
 * 实际接口路径: /stock/matLabel
 */
public class StockMatLabelControllerTest extends BaseControllerTest {

    @Mock
    private IStockMatLabelService stockMatLabelService;

    @InjectMocks
    private StockMatLabelController stockMatLabelController;

    @Override
    protected Object getController() {
        return stockMatLabelController;
    }

    private StockMatLabel createTestLabel() {
        StockMatLabel label = new StockMatLabel();
        label.setLabelId(1L);
        label.setMatCode("MAT001");
        label.setMatName("测试物料");
        label.setQuantity(new BigDecimal("10"));
        label.setUsableQuantity(new BigDecimal("10"));
        label.setWarehouseCode("WH001");
        label.setBatch("B001");
        label.setUnitCode("PCS");
        return label;
    }

    @Test
    @DisplayName("查询物料标签列表")
    void testList() throws Exception {
        List<StockMatLabel> list = new ArrayList<>();
        list.add(createTestLabel());

        when(stockMatLabelService.selectStockMatLabelList(any(StockMatLabel.class))).thenReturn(list);

        performGet("/stock/matLabel/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询物料标签")
    void testGetInfo() throws Exception {
        StockMatLabel label = createTestLabel();
        when(stockMatLabelService.selectStockMatLabelByLabelId(anyLong())).thenReturn(label);

        performGet("/stock/matLabel/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增物料标签")
    void testAdd() throws Exception {
        StockMatLabel label = createTestLabel();
        when(stockMatLabelService.insertStockMatLabel(any(StockMatLabel.class))).thenReturn(1);

        performPost("/stock/matLabel", label)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改物料标签")
    void testEdit() throws Exception {
        StockMatLabel label = createTestLabel();
        when(stockMatLabelService.updateStockMatLabel(any(StockMatLabel.class))).thenReturn(1);

        performPut("/stock/matLabel", label)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除物料标签")
    void testRemove() throws Exception {
        when(stockMatLabelService.deleteStockMatLabelByLabelIds(any(Long[].class))).thenReturn(1);

        performDelete("/stock/matLabel/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}