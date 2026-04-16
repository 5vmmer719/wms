package com.ruoyi.base.mapper;

import java.util.List;

import com.ruoyi.base.domain.BaseProcessStep;

/**
 * 工序Mapper接口
 *
 * @author summer
 * @date 2026-04-11
 */
public interface BaseProcessStepMapper {

    /**
     * 查询工序
     *
     * @param stepId 工序主键
     * @return 工序
     */
    public BaseProcessStep selectBaseProcessStepByStepId(Long stepId);

    /**
     * 根据工艺路线编码查询工序列表
     *
     * @param routeCode 工艺路线编码
     * @return 工序集合
     */
    public List<BaseProcessStep> selectBaseProcessStepByRouteCode(String routeCode);

    /**
     * 查询工序列表
     *
     * @param baseProcessStep 工序
     * @return 工序集合
     */
    public List<BaseProcessStep> selectBaseProcessStepList(BaseProcessStep baseProcessStep);

    /**
     * 新增工序
     *
     * @param baseProcessStep 工序
     * @return 结果
     */
    public int insertBaseProcessStep(BaseProcessStep baseProcessStep);

    /**
     * 修改工序
     *
     * @param baseProcessStep 工序
     * @return 结果
     */
    public int updateBaseProcessStep(BaseProcessStep baseProcessStep);

    /**
     * 删除工序
     *
     * @param stepId 工序主键
     * @return 结果
     */
    public int deleteBaseProcessStepByStepId(Long stepId);

    /**
     * 批量删除工序
     *
     * @param stepIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseProcessStepByStepIds(Long[] stepIds);

    /**
     * 根据工艺路线编码删除工序
     *
     * @param routeCode 工艺路线编码
     * @return 结果
     */
    public int deleteBaseProcessStepByRouteCode(String routeCode);
}

