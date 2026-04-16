package com.ruoyi.base.mapper;

import java.util.List;

import com.ruoyi.base.domain.BaseProcessParam;

/**
 * 工艺参数Mapper接口
 *
 * @author summer
 * @date 2026-04-11
 */
public interface BaseProcessParamMapper {

    /**
     * 查询工艺参数
     *
     * @param paramId 工艺参数主键
     * @return 工艺参数
     */
    public BaseProcessParam selectBaseProcessParamByParamId(Long paramId);

    /**
     * 根据工序ID查询工艺参数列表
     *
     * @param stepId 工序ID
     * @return 工艺参数集合
     */
    public List<BaseProcessParam> selectBaseProcessParamByStepId(Long stepId);

    /**
     * 查询工艺参数列表
     *
     * @param baseProcessParam 工艺参数
     * @return 工艺参数集合
     */
    public List<BaseProcessParam> selectBaseProcessParamList(BaseProcessParam baseProcessParam);

    /**
     * 新增工艺参数
     *
     * @param baseProcessParam 工艺参数
     * @return 结果
     */
    public int insertBaseProcessParam(BaseProcessParam baseProcessParam);

    /**
     * 修改工艺参数
     *
     * @param baseProcessParam 工艺参数
     * @return 结果
     */
    public int updateBaseProcessParam(BaseProcessParam baseProcessParam);

    /**
     * 删除工艺参数
     *
     * @param paramId 工艺参数主键
     * @return 结果
     */
    public int deleteBaseProcessParamByParamId(Long paramId);

    /**
     * 批量删除工艺参数
     *
     * @param paramIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseProcessParamByParamIds(Long[] paramIds);

    /**
     * 根据工序ID删除工艺参数
     *
     * @param stepId 工序ID
     * @return 结果
     */
    public int deleteBaseProcessParamByStepId(Long stepId);
}

