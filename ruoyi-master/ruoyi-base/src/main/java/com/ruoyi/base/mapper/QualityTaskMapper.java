package com.ruoyi.base.mapper;

import java.util.List;

import com.ruoyi.base.domain.QualityTask;

/**
 * 检验任务Mapper接口
 *
 * @author summer
 * @date 2026-04-11
 */
public interface QualityTaskMapper {

    /**
     * 查询检验任务
     */
    public QualityTask selectQualityTaskByTaskId(Long taskId);

    /**
     * 根据任务编号查询检验任务
     */
    public QualityTask selectQualityTaskByTaskNo(String taskNo);

    /**
     * 查询检验任务列表
     */
    public List<QualityTask> selectQualityTaskList(QualityTask qualityTask);

    /**
     * 查询指定前缀下的最大任务编号
     */
    public String selectMaxTaskNoByPrefix(String prefix);

    /**
     * 新增检验任务
     */
    public int insertQualityTask(QualityTask qualityTask);

    /**
     * 修改检验任务
     */
    public int updateQualityTask(QualityTask qualityTask);

    /**
     * 根据任务编号修改检验任务
     */
    public int updateQualityTaskByTaskNo(QualityTask qualityTask);

    /**
     * 删除检验任务
     */
    public int deleteQualityTaskByTaskId(Long taskId);

    /**
     * 批量删除检验任务
     */
    public int deleteQualityTaskByTaskIds(Long[] taskIds);
}

