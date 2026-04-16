package com.ruoyi.test.base;

import com.ruoyi.base.domain.BaseMat;
import com.ruoyi.base.domain.BaseMatBom;
import com.ruoyi.base.service.IBaseMatBomService;
import com.ruoyi.base.service.IBaseMatService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.base.BaseMatBomController;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 物料BOM管理Controller测试
 * 实际接口路径: /base/bom
 */
public class BaseMatBomControllerTest extends BaseControllerTest {

    @Mock
    private IBaseMatBomService baseMatBomService;

    @Mock
    private IBaseMatService baseMatService;

    @InjectMocks
    private BaseMatBomController baseMatBomController;

    @Override
    protected Object getController() {
        return baseMatBomController;
    }

    private BaseMatBom createTestBom() {
        BaseMatBom bom = new BaseMatBom();
        bom.setBomId(1L);
        bom.setFatherMatCode("MAT001");
        bom.setChildMatCode("MAT002");
        bom.setChildMatNum(new BigDecimal("10"));
        return bom;
    }

    private BaseMat createTestMat() {
        BaseMat mat = new BaseMat();
        mat.setMatCode("MAT001");
        mat.setMatName("测试物料");
        return mat;
    }

    @Test
    @DisplayName("查询BOM列表")
    void testList() throws Exception {
        List<BaseMat> list = new ArrayList<>();
        list.add(createTestMat());

        when(baseMatService.selectBomList(anyString(), anyString())).thenReturn(list);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/base/bom/list")
                .param("matCode", "MAT001")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("查询BOM详情列表")
    void testDetailList() throws Exception {
        List<BaseMatBom> list = new ArrayList<>();
        list.add(createTestBom());

        when(baseMatBomService.selectBaseMatBomList(any(BaseMatBom.class))).thenReturn(list);
        when(baseMatService.selectBaseMatByMatCode(anyString())).thenReturn(createTestMat());

        performGet("/base/bom/detailList")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询BOM")
    void testGetInfo() throws Exception {
        BaseMatBom bom = createTestBom();
        when(baseMatBomService.selectBaseMatBomByBomId(anyLong())).thenReturn(bom);

        performGet("/base/bom/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增BOM")
    void testAdd() throws Exception {
        BaseMatBom bom = createTestBom();
        when(baseMatBomService.insertBaseMatBom(any(BaseMatBom.class))).thenReturn(1);

        performPost("/base/bom", bom)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改BOM")
    void testEdit() throws Exception {
        BaseMatBom bom = createTestBom();
        when(baseMatBomService.updateBaseMatBom(any(BaseMatBom.class))).thenReturn(1);

        performPut("/base/bom", bom)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除BOM")
    void testRemove() throws Exception {
        when(baseMatBomService.deleteBaseMatBomByMatCodes(any(String[].class))).thenReturn(1);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/base/bom/MAT001")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}