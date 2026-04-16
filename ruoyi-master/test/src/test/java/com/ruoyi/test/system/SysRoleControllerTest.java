package com.ruoyi.test.system;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.system.SysRoleController;
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
 * 角色管理Controller测试
 * 实际接口路径: /system/role
 */
public class SysRoleControllerTest extends BaseControllerTest {

    @Mock
    private ISysRoleService sysRoleService;

    @InjectMocks
    private SysRoleController sysRoleController;

    @Override
    protected Object getController() {
        return sysRoleController;
    }

    private SysRole createTestRole() {
        SysRole role = new SysRole();
        role.setRoleId(1L);
        role.setRoleName("测试角色");
        role.setRoleKey("test");
        role.setRoleSort("1");
        role.setStatus("0");
        return role;
    }

    @Test
    @DisplayName("查询角色列表")
    void testList() throws Exception {
        List<SysRole> list = new ArrayList<>();
        list.add(createTestRole());

        when(sysRoleService.selectRoleList(any(SysRole.class))).thenReturn(list);

        performGet("/system/role/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询角色")
    void testGetInfo() throws Exception {
        SysRole role = createTestRole();
        when(sysRoleService.selectRoleById(anyLong())).thenReturn(role);

        performGet("/system/role/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增角色")
    void testAdd() throws Exception {
        SysRole role = createTestRole();
        when(sysRoleService.insertRole(any(SysRole.class))).thenReturn(1);

        performPost("/system/role", role)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改角色")
    void testEdit() throws Exception {
        SysRole role = createTestRole();
        when(sysRoleService.updateRole(any(SysRole.class))).thenReturn(1);

        performPut("/system/role", role)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除角色")
    void testRemove() throws Exception {
        when(sysRoleService.deleteRoleByIds(any(Long[].class))).thenReturn(1);

        performDelete("/system/role/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}