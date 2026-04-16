package com.ruoyi.test.base;

import com.ruoyi.base.domain.BaseMatGroup;
import com.ruoyi.base.service.IBaseMatGroupService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.base.BaseMatGroupController;
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
 * 物料组管理Controller测试
 */
public class BaseMatGroupControllerTest extends BaseControllerTest {

    @Mock
    private IBaseMatGroupService baseMatGroupService;

    @InjectMocks
    private BaseMatGroupController baseMatGroupController;

    @Override
    protected Object getController() {
        return baseMatGroupController;
    }

    private BaseMatGroup createTestMatGroup() {
        BaseMatGroup group = new BaseMatGroup();
        group.setGroupId(1L);
        group.setGroupCode("01");
        group.setGroupName("测试物料组");
        group.setDefaultWarehouseType("01");
        return group;
    }

    @Test
    @DisplayName("查询物料组列表")
    void testList() throws Exception {
        List<BaseMatGroup> list = new ArrayList<>();
        list.add(createTestMatGroup());

        when(baseMatGroupService.selectBaseMatGroupList(any(BaseMatGroup.class))).thenReturn(list);

        performGet("/base/matGroup/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询物料组")
    void testGetInfo() throws Exception {
        BaseMatGroup group = createTestMatGroup();
        when(baseMatGroupService.selectBaseMatGroupByGroupId(anyLong())).thenReturn(group);

        performGet("/base/matGroup/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增物料组")
    void testAdd() throws Exception {
        BaseMatGroup group = createTestMatGroup();
        when(baseMatGroupService.insertBaseMatGroup(any(BaseMatGroup.class))).thenReturn(1);

        performPost("/base/matGroup", group)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改物料组")
    void testEdit() throws Exception {
        BaseMatGroup group = createTestMatGroup();
        when(baseMatGroupService.updateBaseMatGroup(any(BaseMatGroup.class))).thenReturn(1);

        performPut("/base/matGroup", group)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除物料组")
    void testRemove() throws Exception {
        when(baseMatGroupService.deleteBaseMatGroupByGroupIds(any(Long[].class))).thenReturn(1);

        performDelete("/base/matGroup/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}