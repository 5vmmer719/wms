package com.ruoyi.base.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.base.mapper.BaseProcessParamMapper;
import com.ruoyi.base.domain.BaseProcessParam;
import com.ruoyi.base.service.IBaseProcessParamService;

/**
 * 工艺参数Service业务层处理
 *
 * @author summer
 * @date 2026-04-11
 */
@Service
public class BaseProcessParamServiceImpl implements IBaseProcessParamService {

    @Autowired
    private BaseProcessParamMapper baseProcessParamMapper;

    /**
     * 查询工艺参数
     *
     * @param paramId 工艺参数主键
     * @return 工艺参数
     */
    @Override
    public BaseProcessParam selectBaseProcessParamByParamId(Long paramId) {
        return baseProcessParamMapper.selectBaseProcessParamByParamId(paramId);
    }

    /**
     * 根据工序ID查询工艺参数列表
     *
     * @param stepId 工序ID
     * @return 工艺参数集合
     */
    @Override
    public List<BaseProcessParam> selectBaseProcessParamByStepId(Long stepId) {
        return baseProcessParamMapper.selectBaseProcessParamByStepId(stepId);
    }

    /**
     * 查询工艺参数列表
     *
     * @param baseProcessParam 工艺参数
     * @return 工艺参数
     */
    @Override
    public List<BaseProcessParam> selectBaseProcessParamList(BaseProcessParam baseProcessParam) {
        return baseProcessParamMapper.selectBaseProcessParamList(baseProcessParam);
    }

    /**
     * 新增工艺参数
     *
     * @param baseProcessParam 工艺参数
     * @return 结果
     */
    @Override
    public int insertBaseProcessParam(BaseProcessParam baseProcessParam) {
        baseProcessParam.setCreateTime(DateUtils.getNowDate());
        return baseProcessParamMapper.insertBaseProcessParam(baseProcessParam);
    }

    /**
     * 修改工艺参数
     *
     * @param baseProcessParam 工艺参数
     * @return 结果
     */
    @Override
    public int updateBaseProcessParam(BaseProcessParam baseProcessParam) {
        baseProcessParam.setUpdateTime(DateUtils.getNowDate());
        return baseProcessParamMapper.updateBaseProcessParam(baseProcessParam);
    }

    /**
     * 批量删除工艺参数
     *
     * @param paramIds 需要删除的工艺参数主键
     * @return 结果
     */
    @Override
    public int deleteBaseProcessParamByParamIds(Long[] paramIds) {
        return baseProcessParamMapper.deleteBaseProcessParamByParamIds(paramIds);
    }

    /**
     * 删除工艺参数信息
     *
     * @param paramId 工艺参数主键
     * @return 结果
     */
    @Override
    public int deleteBaseProcessParamByParamId(Long paramId) {
        return baseProcessParamMapper.deleteBaseProcessParamByParamId(paramId);
    }
}

