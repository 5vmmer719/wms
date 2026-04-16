package com.ruoyi.test.system;

import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.system.SysMenuController;
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
 * 菜单管理Controller测试
 * 实际接口路径: /system/menu
 */
public class SysMenuControllerTest extends BaseControllerTest {

    @Mock
    private ISysMenuService sysMenuService;

    @InjectMocks
    private SysMenuController sysMenuController;

    @Override
    protected Object getController() {
        return sysMenuController;
    }

    private SysMenu createTestMenu() {
        SysMenu menu = new SysMenu();
        menu.setMenuId(1L);
        menu.setMenuName("测试菜单");
        menu.setParentId(0L);
        menu.setOrderNum(1);
        menu.setMenuType("M");
        menu.setVisible("0");
        menu.setStatus("0");
        return menu;
    }

    @Test
    @DisplayName("查询菜单列表")
    void testList() throws Exception {
        List<SysMenu> list = new ArrayList<>();
        list.add(createTestMenu());

        when(sysMenuService.selectMenuList(any(SysMenu.class), any(Long.class))).thenReturn(list);

        performGet("/system/menu/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询菜单")
    void testGetInfo() throws Exception {
        SysMenu menu = createTestMenu();
        when(sysMenuService.selectMenuById(anyLong())).thenReturn(menu);

        performGet("/system/menu/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增菜单")
    void testAdd() throws Exception {
        SysMenu menu = createTestMenu();
        when(sysMenuService.insertMenu(any(SysMenu.class))).thenReturn(1);

        performPost("/system/menu", menu)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改菜单")
    void testEdit() throws Exception {
        SysMenu menu = createTestMenu();
        when(sysMenuService.updateMenu(any(SysMenu.class))).thenReturn(1);

        performPut("/system/menu", menu)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除菜单")
    void testRemove() throws Exception {
        when(sysMenuService.deleteMenuById(anyLong())).thenReturn(1);

        performDelete("/system/menu/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}