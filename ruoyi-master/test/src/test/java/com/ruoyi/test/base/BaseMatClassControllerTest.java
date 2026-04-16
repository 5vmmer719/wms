package com.ruoyi.test.base;

import com.ruoyi.base.domain.BaseMatClass;
import com.ruoyi.base.service.IBaseMatClassService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.base.BaseMatClassController;
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
 * 物料分类管理Controller测试
 * 实际接口路径: /base/class
 */
public class BaseMatClassControllerTest extends BaseControllerTest {

    @Mock
    private IBaseMatClassService baseMatClassService;

    @InjectMocks
    private BaseMatClassController baseMatClassController;

    @Override
    protected Object getController() {
        return baseMatClassController;
    }

    private BaseMatClass createTestMatClass() {
        BaseMatClass matClass = new BaseMatClass();
        matClass.setClassId(1L);
        matClass.setClassCode("01");
        matClass.setClassName("测试分类");
        return matClass;
    }

    @Test
    @DisplayName("查询物料分类列表")
    void testList() throws Exception {
        List<BaseMatClass> list = new ArrayList<>();
        list.add(createTestMatClass());

        when(baseMatClassService.selectBaseMatClassList(any(BaseMatClass.class))).thenReturn(list);

        performGet("/base/class/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("查询物料分类列表-全部(下拉框)")
    void testListAll() throws Exception {
        List<BaseMatClass> list = new ArrayList<>();
        list.add(createTestMatClass());

        when(baseMatClassService.selectBaseMatClassList(any(BaseMatClass.class))).thenReturn(list);

        performGet("/base/class/listAll")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询物料分类")
    void testGetInfo() throws Exception {
        BaseMatClass matClass = createTestMatClass();
        when(baseMatClassService.selectBaseMatClassByClassId(anyLong())).thenReturn(matClass);

        performGet("/base/class/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增物料分类")
    void testAdd() throws Exception {
        BaseMatClass matClass = createTestMatClass();
        when(baseMatClassService.insertBaseMatClass(any(BaseMatClass.class))).thenReturn(1);

        performPost("/base/class", matClass)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改物料分类")
    void testEdit() throws Exception {
        BaseMatClass matClass = createTestMatClass();
        when(baseMatClassService.updateBaseMatClass(any(BaseMatClass.class))).thenReturn(1);

        performPut("/base/class", matClass)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除物料分类")
    void testRemove() throws Exception {
        when(baseMatClassService.deleteBaseMatClassByClassIds(any(Long[].class))).thenReturn(1);

        performDelete("/base/class/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}