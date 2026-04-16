package com.ruoyi.base.service;

import java.util.List;

import com.ruoyi.base.domain.QualityTask;
import com.ruoyi.base.domain.QualityTaskResult;

/**
 * 检验任务Service接口
 *
 * @author summer
 * @date 2026-04-11
 */
public interface IQualityTaskService {

    /**
     * 查询检验任务
     */
    public QualityTask selectQualityTaskByTaskId(Long taskId);

    /**
     * 根据任务编号查询检验任务
     */
    public QualityTask selectQualityTaskByTaskNo(String taskNo);

    /**
     * 查询检验任务详情（含检验结果明细）
     */
    public QualityTask selectQualityTaskDetail(Long taskId);

    /**
     * 查询检验任务列表
     */
    public List<QualityTask> selectQualityTaskList(QualityTask qualityTask);

    /**
     * 新增检验任务
     */
    public int insertQualityTask(QualityTask qualityTask);

    /**
     * 修改检验任务
     */
    public int updateQualityTask(QualityTask qualityTask);

    /**
     * 提交检验结果（录入实测值，系统自动判定合格/不合格）
     *
     * @param taskId     检验任务ID
     * @param resultList 检验结果明细列表（含实测值）
     * @param qualifiedQty 合格数量
     * @param unqualifiedQty 不合格数量
     * @param username   操作人
     * @return 结果
     */
    public int submitCheckResult(Long taskId, List<QualityTaskResult> resultList,
                                  java.math.BigDecimal qualifiedQty, java.math.BigDecimal unqualifiedQty,
                                  String username);

    /**
     * 批量删除检验任务
     */
    public int deleteQualityTaskByTaskIds(Long[] taskIds);

    /**
     * 删除检验任务
     */
    public int deleteQualityTaskByTaskId(Long taskId);
}

