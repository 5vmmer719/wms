package com.ruoyi.test.base;

import com.ruoyi.base.domain.BaseMat;
import com.ruoyi.base.service.IBaseMatService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.base.BaseMatController;
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
 * 物料管理Controller测试
 */
public class BaseMatControllerTest extends BaseControllerTest {

    @Mock
    private IBaseMatService baseMatService;

    @InjectMocks
    private BaseMatController baseMatController;

    @Override
    protected Object getController() {
        return baseMatController;
    }

    private BaseMat createTestMat() {
        BaseMat mat = new BaseMat();
        mat.setMatId(1L);
        mat.setMatCode("MAT001");
        mat.setMatName("测试物料");
        mat.setMatGroup("01");
        mat.setMatClass("01");
        mat.setUnitCode("PCS");
        mat.setFdCode("FD001");
        mat.setFigNum("FIG001");
        return mat;
    }

    @Test
    @DisplayName("查询物料列表")
    void testList() throws Exception {
        List<BaseMat> list = new ArrayList<>();
        list.add(createTestMat());

        when(baseMatService.selectBaseMatList(any(BaseMat.class))).thenReturn(list);

        performGet("/base/mat/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询物料")
    void testGetInfo() throws Exception {
        BaseMat mat = createTestMat();
        when(baseMatService.selectBaseMatByMatId(anyLong())).thenReturn(mat);

        performGet("/base/mat/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增物料")
    void testAdd() throws Exception {
        BaseMat mat = createTestMat();
        when(baseMatService.insertBaseMat(any(BaseMat.class))).thenReturn(1);

        performPost("/base/mat", mat)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改物料")
    void testEdit() throws Exception {
        BaseMat mat = createTestMat();
        when(baseMatService.updateBaseMat(any(BaseMat.class))).thenReturn(1);

        performPut("/base/mat", mat)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除物料")
    void testRemove() throws Exception {
        when(baseMatService.deleteBaseMatByMatIds(any(Long[].class))).thenReturn(1);

        performDelete("/base/mat/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("查询物料列表-空列表")
    void testListEmpty() throws Exception {
        when(baseMatService.selectBaseMatList(any(BaseMat.class))).thenReturn(new ArrayList<>());

        performGet("/base/mat/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询物料-不存在")
    void testGetInfoNotFound() throws Exception {
        when(baseMatService.selectBaseMatByMatId(anyLong())).thenReturn(null);

        performGet("/base/mat/999")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}