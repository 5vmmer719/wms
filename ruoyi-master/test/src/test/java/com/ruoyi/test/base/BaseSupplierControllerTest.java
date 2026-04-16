package com.ruoyi.test.base;

import com.ruoyi.base.domain.BaseSupplier;
import com.ruoyi.base.service.IBaseSupplierService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.base.BaseSupplierController;
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
 * 供应商管理Controller测试
 */
public class BaseSupplierControllerTest extends BaseControllerTest {

    @Mock
    private IBaseSupplierService baseSupplierService;

    @InjectMocks
    private BaseSupplierController baseSupplierController;

    @Override
    protected Object getController() {
        return baseSupplierController;
    }

    private BaseSupplier createTestSupplier() {
        BaseSupplier supplier = new BaseSupplier();
        supplier.setSupplierId(1L);
        supplier.setSupplierCode("SUP001");
        supplier.setSupplierName("测试供应商");
        supplier.setContact("张三");
        supplier.setAddress("测试地址");
        return supplier;
    }

    @Test
    @DisplayName("查询供应商列表")
    void testList() throws Exception {
        List<BaseSupplier> list = new ArrayList<>();
        list.add(createTestSupplier());

        when(baseSupplierService.selectBaseSupplierList(any(BaseSupplier.class))).thenReturn(list);

        performGet("/base/supplier/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询供应商")
    void testGetInfo() throws Exception {
        BaseSupplier supplier = createTestSupplier();
        when(baseSupplierService.selectBaseSupplierBySupplierId(anyLong())).thenReturn(supplier);

        performGet("/base/supplier/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增供应商")
    void testAdd() throws Exception {
        BaseSupplier supplier = createTestSupplier();
        when(baseSupplierService.insertBaseSupplier(any(BaseSupplier.class))).thenReturn(1);

        performPost("/base/supplier", supplier)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改供应商")
    void testEdit() throws Exception {
        BaseSupplier supplier = createTestSupplier();
        when(baseSupplierService.updateBaseSupplier(any(BaseSupplier.class))).thenReturn(1);

        performPut("/base/supplier", supplier)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除供应商")
    void testRemove() throws Exception {
        when(baseSupplierService.deleteBaseSupplierBySupplierIds(any(Long[].class))).thenReturn(1);

        performDelete("/base/supplier/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}