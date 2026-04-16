package com.ruoyi.test.system;

import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.test.BaseControllerTest;
import com.ruoyi.web.controller.system.SysPostController;
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
 * 岗位管理Controller测试
 * 实际接口路径: /system/post
 */
public class SysPostControllerTest extends BaseControllerTest {

    @Mock
    private ISysPostService sysPostService;

    @InjectMocks
    private SysPostController sysPostController;

    @Override
    protected Object getController() {
        return sysPostController;
    }

    private SysPost createTestPost() {
        SysPost post = new SysPost();
        post.setPostId(1L);
        post.setPostCode("test");
        post.setPostName("测试岗位");
        post.setPostSort(String.valueOf(1));
        post.setStatus("0");
        return post;
    }

    @Test
    @DisplayName("查询岗位列表")
    void testList() throws Exception {
        List<SysPost> list = new ArrayList<>();
        list.add(createTestPost());

        when(sysPostService.selectPostList(any(SysPost.class))).thenReturn(list);

        performGet("/system/post/list")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("根据ID查询岗位")
    void testGetInfo() throws Exception {
        SysPost post = createTestPost();
        when(sysPostService.selectPostById(anyLong())).thenReturn(post);

        performGet("/system/post/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("新增岗位")
    void testAdd() throws Exception {
        SysPost post = createTestPost();
        when(sysPostService.insertPost(any(SysPost.class))).thenReturn(1);

        performPost("/system/post", post)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("修改岗位")
    void testEdit() throws Exception {
        SysPost post = createTestPost();
        when(sysPostService.updatePost(any(SysPost.class))).thenReturn(1);

        performPut("/system/post", post)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("删除岗位")
    void testRemove() throws Exception {
        when(sysPostService.deletePostByIds(any(Long[].class))).thenReturn(1);

        performDelete("/system/post/1")
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}