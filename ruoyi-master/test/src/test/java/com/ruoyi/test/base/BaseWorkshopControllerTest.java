package com.ruoyi.test.base;

import com.ruoyi.base.domain.BaseWorkshop;
import com.ruoyi.base.service.IBaseWorkshopService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.base.BaseWorkshopController;
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
 * 车间管理Controller测试
 */
public class BaseWorkshopControllerTest extends BaseControllerTest {

    @Mock
    private IBaseWorkshopService baseWorkshopService;

    @InjectMocks
    private BaseWorkshopController baseWorkshopController;

    @Override
    protected Object getController() {
        return baseWorkshopController;
    }

    private BaseWorkshop createTestWorkshop() {
        BaseWorkshop workshop = new BaseWorkshop();
        workshop.setWorkshopId(1L);
        workshop.setWorkshopCode("WS001");
        workshop.setWorkshopName("测试车间");
        return workshop;
    }

    @Test
    @DisplayName("查询车间列表")
    void testList() throws Exception {
        List<BaseWorkshop> list = new ArrayList<>();
        list.add(createTestWorkshop());

        when(baseWorkshopService.selectBaseWorkshopList(any(BaseWorkshop.class))).thenReturn(list);

        performGet("/base/workshop/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询车间")
    void testGetInfo() throws Exception {
        BaseWorkshop workshop = createTestWorkshop();
        when(baseWorkshopService.selectBaseWorkshopByWorkshopId(anyLong())).thenReturn(workshop);

        performGet("/base/workshop/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增车间")
    void testAdd() throws Exception {
        BaseWorkshop workshop = createTestWorkshop();
        when(baseWorkshopService.insertBaseWorkshop(any(BaseWorkshop.class))).thenReturn(1);

        performPost("/base/workshop", workshop)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改车间")
    void testEdit() throws Exception {
        BaseWorkshop workshop = createTestWorkshop();
        when(baseWorkshopService.updateBaseWorkshop(any(BaseWorkshop.class))).thenReturn(1);

        performPut("/base/workshop", workshop)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除车间")
    void testRemove() throws Exception {
        when(baseWorkshopService.deleteBaseWorkshopByWorkshopIds(any(Long[].class))).thenReturn(1);

        performDelete("/base/workshop/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}