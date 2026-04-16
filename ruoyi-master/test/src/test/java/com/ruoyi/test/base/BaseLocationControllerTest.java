package com.ruoyi.test.base;

import com.ruoyi.base.domain.BaseLocation;
import com.ruoyi.base.service.IBaseLocationService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.base.BaseLocationController;
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
 * 库位管理Controller测试
 */
public class BaseLocationControllerTest extends BaseControllerTest {

    @Mock
    private IBaseLocationService baseLocationService;

    @InjectMocks
    private BaseLocationController baseLocationController;

    @Override
    protected Object getController() {
        return baseLocationController;
    }

    private BaseLocation createTestLocation() {
        BaseLocation location = new BaseLocation();
        location.setLocationId(1L);
        location.setLocationCode("LOC001");
        location.setLocationName("测试库位");
        location.setWarehouseCode("WH001");
        location.setLocationType("01");
        return location;
    }

    @Test
    @DisplayName("查询库位列表")
    void testList() throws Exception {
        List<BaseLocation> list = new ArrayList<>();
        list.add(createTestLocation());

        when(baseLocationService.selectBaseLocationList(any(BaseLocation.class))).thenReturn(list);

        performGet("/base/location/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询库位")
    void testGetInfo() throws Exception {
        BaseLocation location = createTestLocation();
        when(baseLocationService.selectBaseLocationByLocationId(anyLong())).thenReturn(location);

        performGet("/base/location/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增库位")
    void testAdd() throws Exception {
        BaseLocation location = createTestLocation();
        when(baseLocationService.insertBaseLocation(any(BaseLocation.class))).thenReturn(1);

        performPost("/base/location", location)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改库位")
    void testEdit() throws Exception {
        BaseLocation location = createTestLocation();
        when(baseLocationService.updateBaseLocation(any(BaseLocation.class))).thenReturn(1);

        performPut("/base/location", location)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除库位")
    void testRemove() throws Exception {
        when(baseLocationService.deleteBaseLocationByLocationIds(any(Long[].class))).thenReturn(1);

        performDelete("/base/location/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}