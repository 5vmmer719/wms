package com.ruoyi.base.mapper;

import java.util.List;

import com.ruoyi.base.domain.QualityTaskResult;

/**
 * 检验结果明细Mapper接口
 *
 * @author summer
 * @date 2026-04-11
 */
public interface QualityTaskResultMapper {

    /**
     * 查询检验结果明细
     */
    public QualityTaskResult selectQualityTaskResultByResultId(Long resultId);

    /**
     * 根据任务编号查询检验结果明细列表
     */
    public List<QualityTaskResult> selectQualityTaskResultByTaskNo(String taskNo);

    /**
     * 查询检验结果明细列表
     */
    public List<QualityTaskResult> selectQualityTaskResultList(QualityTaskResult qualityTaskResult);

    /**
     * 新增检验结果明细
     */
    public int insertQualityTaskResult(QualityTaskResult qualityTaskResult);

    /**
     * 批量新增检验结果明细
     */
    public int insertQualityTaskResultList(List<QualityTaskResult> list);

    /**
     * 修改检验结果明细
     */
    public int updateQualityTaskResult(QualityTaskResult qualityTaskResult);

    /**
     * 根据任务编号删除检验结果明细
     */
    public int deleteQualityTaskResultByTaskNo(String taskNo);
}

