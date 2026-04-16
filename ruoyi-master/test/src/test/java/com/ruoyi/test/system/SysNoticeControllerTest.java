package com.ruoyi.test.system;

import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.system.SysNoticeController;
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
 * 通知公告Controller测试
 * 实际接口路径: /system/notice
 */
public class SysNoticeControllerTest extends BaseControllerTest {

    @Mock
    private ISysNoticeService sysNoticeService;

    @InjectMocks
    private SysNoticeController sysNoticeController;

    @Override
    protected Object getController() {
        return sysNoticeController;
    }

    private SysNotice createTestNotice() {
        SysNotice notice = new SysNotice();
        notice.setNoticeId(1L);
        notice.setNoticeTitle("测试公告");
        notice.setNoticeContent("这是测试公告内容");
        notice.setNoticeType("1");
        notice.setStatus("0");
        return notice;
    }

    @Test
    @DisplayName("查询通知公告列表")
    void testList() throws Exception {
        List<SysNotice> list = new ArrayList<>();
        list.add(createTestNotice());

        when(sysNoticeService.selectNoticeList(any(SysNotice.class))).thenReturn(list);

        performGet("/system/notice/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询通知公告")
    void testGetInfo() throws Exception {
        SysNotice notice = createTestNotice();
        when(sysNoticeService.selectNoticeById(anyLong())).thenReturn(notice);

        performGet("/system/notice/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增通知公告")
    void testAdd() throws Exception {
        SysNotice notice = createTestNotice();
        when(sysNoticeService.insertNotice(any(SysNotice.class))).thenReturn(1);

        performPost("/system/notice", notice)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改通知公告")
    void testEdit() throws Exception {
        SysNotice notice = createTestNotice();
        when(sysNoticeService.updateNotice(any(SysNotice.class))).thenReturn(1);

        performPut("/system/notice", notice)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除通知公告")
    void testRemove() throws Exception {
        when(sysNoticeService.deleteNoticeByIds(any(Long[].class))).thenReturn(1);

        performDelete("/system/notice/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}