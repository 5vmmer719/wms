package com.ruoyi.test.system;

import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.system.SysConfigController;
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
 * 参数配置管理Controller测试
 * 实际接口路径: /system/config
 */
public class SysConfigControllerTest extends BaseControllerTest {

    @Mock
    private ISysConfigService sysConfigService;

    @InjectMocks
    private SysConfigController sysConfigController;

    @Override
    protected Object getController() {
        return sysConfigController;
    }

    private SysConfig createTestConfig() {
        SysConfig config = new SysConfig();
        config.setConfigId(1L);
        config.setConfigName("测试参数");
        config.setConfigKey("test_key");
        config.setConfigValue("test_value");
        return config;
    }

    @Test
    @DisplayName("查询参数配置列表")
    void testList() throws Exception {
        List<SysConfig> list = new ArrayList<>();
        list.add(createTestConfig());

        when(sysConfigService.selectConfigList(any(SysConfig.class))).thenReturn(list);

        performGet("/system/config/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询参数配置")
    void testGetInfo() throws Exception {
        SysConfig config = createTestConfig();
        when(sysConfigService.selectConfigById(anyLong())).thenReturn(config);

        performGet("/system/config/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增参数配置")
    void testAdd() throws Exception {
        SysConfig config = createTestConfig();
        when(sysConfigService.insertConfig(any(SysConfig.class))).thenReturn(1);

        performPost("/system/config", config)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改参数配置")
    void testEdit() throws Exception {
        SysConfig config = createTestConfig();
        when(sysConfigService.updateConfig(any(SysConfig.class))).thenReturn(1);

        performPut("/system/config", config)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除参数配置")
    void testRemove() throws Exception {
        doNothing().when(sysConfigService).deleteConfigByIds(any(Long[].class));

        performDelete("/system/config/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}