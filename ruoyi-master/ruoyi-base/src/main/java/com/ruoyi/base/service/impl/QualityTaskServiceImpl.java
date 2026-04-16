package com.ruoyi.base.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruoyi.base.domain.QualityStandardItem;
import com.ruoyi.base.domain.QualityTaskResult;
import com.ruoyi.base.mapper.QualityStandardItemMapper;
import com.ruoyi.base.mapper.QualityTaskResultMapper;
import com.ruoyi.common.bean.typeEnum.QualityTaskStatusEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.base.mapper.QualityTaskMapper;
import com.ruoyi.base.domain.QualityTask;
import com.ruoyi.base.service.IQualityTaskService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 检验任务Service业务层处理
 *
 * @author summer
 * @date 2026-04-11
 */
@Service
public class QualityTaskServiceImpl implements IQualityTaskService {

    @Autowired
    private QualityTaskMapper qualityTaskMapper;

    @Autowired
    private QualityTaskResultMapper qualityTaskResultMapper;

    @Autowired
    private QualityStandardItemMapper qualityStandardItemMapper;

    /**
     * 查询检验任务
     */
    @Override
    public QualityTask selectQualityTaskByTaskId(Long taskId) {
        QualityTask task = qualityTaskMapper.selectQualityTaskByTaskId(taskId);
        if (task != null) {
            task.setTaskStatusLabel(QualityTaskStatusEnum.getLabel(task.getTaskStatus()));
        }
        return task;
    }

    /**
     * 根据任务编号查询
     */
    @Override
    public QualityTask selectQualityTaskByTaskNo(String taskNo) {
        return qualityTaskMapper.selectQualityTaskByTaskNo(taskNo);
    }

    /**
     * 查询检验任务详情（含检验结果明细）
     */
    @Override
    public QualityTask selectQualityTaskDetail(Long taskId) {
        QualityTask task = qualityTaskMapper.selectQualityTaskByTaskId(taskId);
        if (task != null) {
            task.setTaskStatusLabel(QualityTaskStatusEnum.getLabel(task.getTaskStatus()));
            // 加载已有的检验结果明细
            List<QualityTaskResult> resultList = qualityTaskResultMapper.selectQualityTaskResultByTaskNo(task.getTaskNo());
            if (CollectionUtils.isNotEmpty(resultList)) {
                task.setResultList(resultList);
            } else {
                // 如果还没有检验结果，根据检验标准初始化空结果列表
                if (StringUtils.isNotEmpty(task.getStandardCode())) {
                    List<QualityStandardItem> items = qualityStandardItemMapper.selectQualityStandardItemByStandardCode(task.getStandardCode());
                    if (CollectionUtils.isNotEmpty(items)) {
                        java.util.ArrayList<QualityTaskResult> initResults = new java.util.ArrayList<>();
                        for (QualityStandardItem item : items) {
                            QualityTaskResult result = new QualityTaskResult();
                            result.setTaskNo(task.getTaskNo());
                            result.setItemNo(item.getItemNo());
                            result.setItemName(item.getItemName());
                            result.setStandardValue(item.getStandardValue());
                            result.setMinValue(item.getMinValue());
                            result.setMaxValue(item.getMaxValue());
                            initResults.add(result);
                        }
                        task.setResultList(initResults);
                    }
                }
            }
        }
        return task;
    }

    /**
     * 查询检验任务列表
     */
    @Override
    public List<QualityTask> selectQualityTaskList(QualityTask qualityTask) {
        List<QualityTask> list = qualityTaskMapper.selectQualityTaskList(qualityTask);
        if (CollectionUtils.isNotEmpty(list)) {
            for (QualityTask t : list) {
                t.setTaskStatusLabel(QualityTaskStatusEnum.getLabel(t.getTaskStatus()));
            }
        }
        return list;
    }

    /**
     * 新增检验任务
     * 编码规则：QT + yyyyMMdd + 3位流水号
     */
    @Override
    public int insertQualityTask(QualityTask qualityTask) {
        // 自动生成任务编号
        if (StringUtils.isEmpty(qualityTask.getTaskNo())) {
            qualityTask.setTaskNo(generateTaskNo());
        }
        if (StringUtils.isEmpty(qualityTask.getTaskStatus())) {
            qualityTask.setTaskStatus(QualityTaskStatusEnum.PENDING.getValue());
        }
        qualityTask.setCreateTime(DateUtils.getNowDate());
        return qualityTaskMapper.insertQualityTask(qualityTask);
    }

    /**
     * 修改检验任务
     */
    @Override
    public int updateQualityTask(QualityTask qualityTask) {
        qualityTask.setUpdateTime(DateUtils.getNowDate());
        return qualityTaskMapper.updateQualityTask(qualityTask);
    }

    /**
     * 提交检验结果
     * 核心逻辑：
     * 1. 遍历结果明细，根据min/max自动判定每项的合格/不合格
     * 2. 保存检验结果明细
     * 3. 根据所有检验项的判定结果，更新任务状态（全部合格→passed，有不合格→failed）
     */
    @Override
    @Transactional
    public int submitCheckResult(Long taskId, List<QualityTaskResult> resultList,
                                  BigDecimal qualifiedQty, BigDecimal unqualifiedQty,
                                  String username) {
        QualityTask task = qualityTaskMapper.selectQualityTaskByTaskId(taskId);
        if (task == null) {
            throw new com.ruoyi.common.exception.ServiceException("检验任务不存在");
        }

        Date nowDate = DateUtils.getNowDate();
        boolean hasFailedItem = false;

        // 先删除旧的检验结果
        qualityTaskResultMapper.deleteQualityTaskResultByTaskNo(task.getTaskNo());

        // 遍历结果明细，自动判定
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (QualityTaskResult result : resultList) {
                result.setTaskNo(task.getTaskNo());
                result.setCreateBy(username);
                result.setCreateTime(nowDate);

                // 自动判定逻辑：根据实测值与上下限比较
                if (StringUtils.isNotEmpty(result.getActualValue())) {
                    try {
                        BigDecimal actual = new BigDecimal(result.getActualValue());
                        boolean pass = true;
                        if (result.getMinValue() != null && actual.compareTo(result.getMinValue()) < 0) {
                            pass = false;
                        }
                        if (result.getMaxValue() != null && actual.compareTo(result.getMaxValue()) > 0) {
                            pass = false;
                        }
                        result.setJudgeResult(pass ? "0" : "1");
                    } catch (NumberFormatException e) {
                        // 非数值型实测值，需要手动判定（前端已传入judgeResult）
                    }
                }

                if ("1".equals(result.getJudgeResult())) {
                    hasFailedItem = true;
                }
            }
            qualityTaskResultMapper.insertQualityTaskResultList(resultList);
        }

        // 更新任务状态和数量
        // 根据检验项判定结果自动修正合格/不合格数量
        BigDecimal totalQty = task.getQuantity() != null ? task.getQuantity() : BigDecimal.ZERO;
        if (hasFailedItem) {
            // 有不合格项：如果前端传的不合格数量为0或null，则自动设为送检数量
            if (unqualifiedQty == null || unqualifiedQty.compareTo(BigDecimal.ZERO) <= 0) {
                unqualifiedQty = totalQty;
                qualifiedQty = BigDecimal.ZERO;
            }
        } else {
            // 全部合格：如果前端传的合格数量为0或null，则自动设为送检数量
            if (qualifiedQty == null || qualifiedQty.compareTo(BigDecimal.ZERO) <= 0) {
                qualifiedQty = totalQty;
                unqualifiedQty = BigDecimal.ZERO;
            }
        }
        task.setQualifiedQty(qualifiedQty);
        task.setUnqualifiedQty(unqualifiedQty);
        task.setCheckTime(nowDate);
        task.setInspectorName(username);
        task.setTaskStatus(hasFailedItem ? QualityTaskStatusEnum.FAILED.getValue() : QualityTaskStatusEnum.PASSED.getValue());
        task.setUpdateBy(username);
        task.setUpdateTime(nowDate);

        return qualityTaskMapper.updateQualityTask(task);
    }

    /**
     * 批量删除检验任务
     */
    @Override
    @Transactional
    public int deleteQualityTaskByTaskIds(Long[] taskIds) {
        for (Long taskId : taskIds) {
            QualityTask task = qualityTaskMapper.selectQualityTaskByTaskId(taskId);
            if (task != null) {
                qualityTaskResultMapper.deleteQualityTaskResultByTaskNo(task.getTaskNo());
            }
        }
        return qualityTaskMapper.deleteQualityTaskByTaskIds(taskIds);
    }

    /**
     * 删除检验任务
     */
    @Override
    @Transactional
    public int deleteQualityTaskByTaskId(Long taskId) {
        QualityTask task = qualityTaskMapper.selectQualityTaskByTaskId(taskId);
        if (task != null) {
            qualityTaskResultMapper.deleteQualityTaskResultByTaskNo(task.getTaskNo());
        }
        return qualityTaskMapper.deleteQualityTaskByTaskId(taskId);
    }

    /**
     * 自动生成检验任务编号
     * 规则：QT + yyyyMMdd + 3位流水号（001-999）
     */
    private String generateTaskNo() {
        String prefix = "QT" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        String maxNo = qualityTaskMapper.selectMaxTaskNoByPrefix(prefix);
        int seq = 1;
        if (StringUtils.isNotEmpty(maxNo) && maxNo.length() > prefix.length()) {
            try {
                seq = Integer.parseInt(maxNo.substring(prefix.length())) + 1;
            } catch (NumberFormatException e) {
                seq = 1;
            }
        }
        return prefix + String.format("%03d", seq);
    }
}

