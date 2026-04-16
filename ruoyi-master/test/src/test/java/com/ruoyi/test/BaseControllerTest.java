package com.ruoyi.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Controller测试基类
 * 提供通用的测试方法和配置
 * 使用MockMvc进行独立测试，不启动完整的Spring上下文
 */
@ExtendWith(MockitoExtension.class)
public abstract class BaseControllerTest {

    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    /**
     * 获取需要测试的Controller实例
     * 子类需要实现此方法返回被测试的Controller
     */
    protected abstract Object getController();

    @BeforeEach
    public void setUp() {
        // 初始化MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(getController())
                .build();

        // 初始化ObjectMapper
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * 将对象转换为JSON字符串
     */
    protected String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * 执行GET请求
     */
    protected ResultActions performGet(String url) throws Exception {
        return mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * 执行带参数的GET请求
     */
    protected ResultActions performGet(String url, String paramName, String paramValue) throws Exception {
        return mockMvc.perform(get(url)
                .param(paramName, paramValue)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * 执行POST请求
     */
    protected ResultActions performPost(String url, Object body) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body)))
                .andDo(print());
    }

    /**
     * 执行POST请求（表单参数）
     */
    protected ResultActions performPostForm(String url, String paramName, String paramValue) throws Exception {
        return mockMvc.perform(post(url)
                .param(paramName, paramValue)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print());
    }

    /**
     * 执行PUT请求
     */
    protected ResultActions performPut(String url, Object body) throws Exception {
        return mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body)))
                .andDo(print());
    }

    /**
     * 执行DELETE请求
     */
    protected ResultActions performDelete(String url) throws Exception {
        return mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}