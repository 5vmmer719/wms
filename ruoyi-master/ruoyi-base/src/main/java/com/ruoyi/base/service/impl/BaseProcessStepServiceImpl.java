package com.ruoyi.base.service.impl;

import java.util.List;

import com.ruoyi.base.mapper.BaseProcessParamMapper;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.base.mapper.BaseProcessStepMapper;
import com.ruoyi.base.domain.BaseProcessStep;
import com.ruoyi.base.service.IBaseProcessStepService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工序Service业务层处理
 *
 * @author summer
 * @date 2026-04-11
 */
@Service
public class BaseProcessStepServiceImpl implements IBaseProcessStepService {

    @Autowired
    private BaseProcessStepMapper baseProcessStepMapper;

    @Autowired
    private BaseProcessParamMapper baseProcessParamMapper;

    /**
     * 查询工序
     *
     * @param stepId 工序主键
     * @return 工序
     */
    @Override
    public BaseProcessStep selectBaseProcessStepByStepId(Long stepId) {
        return baseProcessStepMapper.selectBaseProcessStepByStepId(stepId);
    }

    /**
     * 根据工艺路线编码查询工序列表
     *
     * @param routeCode 工艺路线编码
     * @return 工序集合
     */
    @Override
    public List<BaseProcessStep> selectBaseProcessStepByRouteCode(String routeCode) {
        return baseProcessStepMapper.selectBaseProcessStepByRouteCode(routeCode);
    }

    /**
     * 查询工序列表
     *
     * @param baseProcessStep 工序
     * @return 工序
     */
    @Override
    public List<BaseProcessStep> selectBaseProcessStepList(BaseProcessStep baseProcessStep) {
        return baseProcessStepMapper.selectBaseProcessStepList(baseProcessStep);
    }

    /**
     * 新增工序
     *
     * @param baseProcessStep 工序
     * @return 结果
     */
    @Override
    public int insertBaseProcessStep(BaseProcessStep baseProcessStep) {
        baseProcessStep.setCreateTime(DateUtils.getNowDate());
        return baseProcessStepMapper.insertBaseProcessStep(baseProcessStep);
    }

    /**
     * 修改工序
     *
     * @param baseProcessStep 工序
     * @return 结果
     */
    @Override
    public int updateBaseProcessStep(BaseProcessStep baseProcessStep) {
        baseProcessStep.setUpdateTime(DateUtils.getNowDate());
        return baseProcessStepMapper.updateBaseProcessStep(baseProcessStep);
    }

    /**
     * 批量删除工序（级联删除参数）
     *
     * @param stepIds 需要删除的工序主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteBaseProcessStepByStepIds(Long[] stepIds) {
        for (Long stepId : stepIds) {
            baseProcessParamMapper.deleteBaseProcessParamByStepId(stepId);
        }
        return baseProcessStepMapper.deleteBaseProcessStepByStepIds(stepIds);
    }

    /**
     * 删除工序信息（级联删除参数）
     *
     * @param stepId 工序主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteBaseProcessStepByStepId(Long stepId) {
        baseProcessParamMapper.deleteBaseProcessParamByStepId(stepId);
        return baseProcessStepMapper.deleteBaseProcessStepByStepId(stepId);
    }
}

