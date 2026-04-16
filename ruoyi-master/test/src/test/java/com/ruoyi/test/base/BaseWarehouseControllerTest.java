package com.ruoyi.test.base;

import com.ruoyi.base.domain.BaseWarehouse;
import com.ruoyi.base.service.IBaseWarehouseService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.base.BaseWarehouseController;
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
 * 仓库管理Controller测试
 */
public class BaseWarehouseControllerTest extends BaseControllerTest {

    @Mock
    private IBaseWarehouseService baseWarehouseService;

    @InjectMocks
    private BaseWarehouseController baseWarehouseController;

    @Override
    protected Object getController() {
        return baseWarehouseController;
    }

    private BaseWarehouse createTestWarehouse() {
        BaseWarehouse warehouse = new BaseWarehouse();
        warehouse.setWarehouseId(1L);
        warehouse.setWarehouseCode("WH001");
        warehouse.setWarehouseName("测试仓库");
        warehouse.setWarehouseType("01");
        return warehouse;
    }

    @Test
    @DisplayName("查询仓库列表")
    void testList() throws Exception {
        List<BaseWarehouse> list = new ArrayList<>();
        list.add(createTestWarehouse());

        when(baseWarehouseService.selectBaseWarehouseList(any(BaseWarehouse.class))).thenReturn(list);

        performGet("/base/warehouse/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询仓库")
    void testGetInfo() throws Exception {
        BaseWarehouse warehouse = createTestWarehouse();
        when(baseWarehouseService.selectBaseWarehouseByWarehouseId(anyLong())).thenReturn(warehouse);

        performGet("/base/warehouse/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增仓库")
    void testAdd() throws Exception {
        BaseWarehouse warehouse = createTestWarehouse();
        when(baseWarehouseService.insertBaseWarehouse(any(BaseWarehouse.class))).thenReturn(1);

        performPost("/base/warehouse", warehouse)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改仓库")
    void testEdit() throws Exception {
        BaseWarehouse warehouse = createTestWarehouse();
        when(baseWarehouseService.updateBaseWarehouse(any(BaseWarehouse.class))).thenReturn(1);

        performPut("/base/warehouse", warehouse)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除仓库")
    void testRemove() throws Exception {
        when(baseWarehouseService.deleteBaseWarehouseByWarehouseIds(any(Long[].class))).thenReturn(1);

        performDelete("/base/warehouse/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("查询仓库列表-带筛选条件")
    void testListWithCondition() throws Exception {
        List<BaseWarehouse> list = new ArrayList<>();
        list.add(createTestWarehouse());

        when(baseWarehouseService.selectBaseWarehouseList(any(BaseWarehouse.class))).thenReturn(list);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/base/warehouse/list")
                .param("warehouseCode", "WH001")
                .param("warehouseName", "测试")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}