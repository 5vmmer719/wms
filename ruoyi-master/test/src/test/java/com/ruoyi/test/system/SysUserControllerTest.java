package com.ruoyi.test.system;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.system.SysUserController;
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
 * 用户管理Controller测试
 * 实际接口路径: /system/user
 */
public class SysUserControllerTest extends BaseControllerTest {

    @Mock
    private ISysUserService sysUserService;

    @InjectMocks
    private SysUserController sysUserController;

    @Override
    protected Object getController() {
        return sysUserController;
    }

    private SysUser createTestUser() {
        SysUser user = new SysUser();
        user.setUserId(1L);
        user.setUserName("testuser");
        user.setNickName("测试用户");
        user.setEmail("test@example.com");
        user.setPhonenumber("13800138000");
        user.setStatus("0");
        return user;
    }

    @Test
    @DisplayName("查询用户列表")
    void testList() throws Exception {
        List<SysUser> list = new ArrayList<>();
        list.add(createTestUser());

        when(sysUserService.selectUserList(any(SysUser.class))).thenReturn(list);

        performGet("/system/user/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询用户")
    void testGetInfo() throws Exception {
        SysUser user = createTestUser();
        when(sysUserService.selectUserById(anyLong())).thenReturn(user);

        performGet("/system/user/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增用户")
    void testAdd() throws Exception {
        SysUser user = createTestUser();
        when(sysUserService.insertUser(any(SysUser.class))).thenReturn(1);

        performPost("/system/user", user)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改用户")
    void testEdit() throws Exception {
        SysUser user = createTestUser();
        when(sysUserService.updateUser(any(SysUser.class))).thenReturn(1);

        performPut("/system/user", user)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除用户")
    void testRemove() throws Exception {
        when(sysUserService.deleteUserByIds(any(Long[].class))).thenReturn(1);

        performDelete("/system/user/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("重置密码")
    void testResetPwd() throws Exception {
        SysUser user = createTestUser();
        user.setPassword("123456");
        when(sysUserService.resetPwd(any(SysUser.class))).thenReturn(1);

        performPut("/system/user/resetPwd", user)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改用户状态")
    void testChangeStatus() throws Exception {
        SysUser user = createTestUser();
        user.setStatus("1");
        when(sysUserService.updateUserStatus(any(SysUser.class))).thenReturn(1);

        performPut("/system/user/changeStatus", user)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}