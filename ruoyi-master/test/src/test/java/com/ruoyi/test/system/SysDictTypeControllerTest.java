package com.ruoyi.test.system;

import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.system.service.ISysDictTypeService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.system.SysDictTypeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * 字典类型管理Controller测试
 * 实际接口路径: /system/dict/type
 */
public class SysDictTypeControllerTest extends BaseControllerTest {

    @Mock
    private ISysDictTypeService sysDictTypeService;

    @InjectMocks
    private SysDictTypeController sysDictTypeController;

    @Override
    protected Object getController() {
        return sysDictTypeController;
    }

    private SysDictType createTestDictType() {
        SysDictType dictType = new SysDictType();
        dictType.setDictId(1L);
        dictType.setDictName("测试字典");
        dictType.setDictType("test_dict");
        dictType.setStatus("0");
        return dictType;
    }

    @Test
    @DisplayName("查询字典类型列表")
    void testList() throws Exception {
        List<SysDictType> list = new ArrayList<>();
        list.add(createTestDictType());

        when(sysDictTypeService.selectDictTypeList(any(SysDictType.class))).thenReturn(list);

        performGet("/system/dict/type/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询字典类型")
    void testGetInfo() throws Exception {
        SysDictType dictType = createTestDictType();
        when(sysDictTypeService.selectDictTypeById(anyLong())).thenReturn(dictType);

        performGet("/system/dict/type/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增字典类型")
    void testAdd() throws Exception {
        SysDictType dictType = createTestDictType();
        when(sysDictTypeService.insertDictType(any(SysDictType.class))).thenReturn(1);

        performPost("/system/dict/type", dictType)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改字典类型")
    void testEdit() throws Exception {
        SysDictType dictType = createTestDictType();
        when(sysDictTypeService.updateDictType(any(SysDictType.class))).thenReturn(1);

        performPut("/system/dict/type", dictType)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除字典类型")
    void testRemove() throws Exception {
        doNothing().when(sysDictTypeService).deleteDictTypeByIds(any(Long[].class));

        performDelete("/system/dict/type/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}