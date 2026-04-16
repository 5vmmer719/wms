package com.ruoyi.test.system;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.system.SysDeptController;
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
 * 部门管理Controller测试
 * 实际接口路径: /system/dept
 */
public class SysDeptControllerTest extends BaseControllerTest {

    @Mock
    private ISysDeptService sysDeptService;

    @InjectMocks
    private SysDeptController sysDeptController;

    @Override
    protected Object getController() {
        return sysDeptController;
    }

    private SysDept createTestDept() {
        SysDept dept = new SysDept();
        dept.setDeptId(100L);
        dept.setDeptName("测试部门");
        dept.setParentId(0L);
        dept.setOrderNum(1);
        dept.setStatus("0");
        return dept;
    }

    @Test
    @DisplayName("查询部门列表")
    void testList() throws Exception {
        List<SysDept> list = new ArrayList<>();
        list.add(createTestDept());

        when(sysDeptService.selectDeptList(any(SysDept.class))).thenReturn(list);

        performGet("/system/dept/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询部门")
    void testGetInfo() throws Exception {
        SysDept dept = createTestDept();
        when(sysDeptService.selectDeptById(anyLong())).thenReturn(dept);

        performGet("/system/dept/100")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增部门")
    void testAdd() throws Exception {
        SysDept dept = createTestDept();
        when(sysDeptService.insertDept(any(SysDept.class))).thenReturn(1);

        performPost("/system/dept", dept)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改部门")
    void testEdit() throws Exception {
        SysDept dept = createTestDept();
        when(sysDeptService.updateDept(any(SysDept.class))).thenReturn(1);

        performPut("/system/dept", dept)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除部门")
    void testRemove() throws Exception {
        when(sysDeptService.deleteDeptById(anyLong())).thenReturn(1);

        performDelete("/system/dept/100")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}