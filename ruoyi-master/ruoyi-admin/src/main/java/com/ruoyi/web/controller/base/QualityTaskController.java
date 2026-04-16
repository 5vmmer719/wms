package com.ruoyi.web.controller.base;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.base.domain.QualityTask;
import com.ruoyi.base.domain.QualityTaskResult;
import com.ruoyi.base.service.IQualityTaskService;

/**
 * 检验任务Controller
 *
 * @author summer
 * @date 2026-04-11
 */
@RestController
@RequestMapping("/quality/task")
public class QualityTaskController extends BaseController {

    @Autowired
    private IQualityTaskService qualityTaskService;

    /**
     * 查询检验任务列表
     */
    @PreAuthorize("@ss.hasPermi('quality:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(QualityTask qualityTask) {
        startPage();
        List<QualityTask> list = qualityTaskService.selectQualityTaskList(qualityTask);
        return getDataTable(list);
    }

    /**
     * 查询检验任务列表（不分页，用于下拉选择）
     */
    @GetMapping("/listAll")
    public List<QualityTask> listAll(QualityTask qualityTask) {
        return qualityTaskService.selectQualityTaskList(qualityTask);
    }

    /**
     * 导出检验任务列表
     */
    @PreAuthorize("@ss.hasPermi('quality:task:export')")
    @Log(title = "检验任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QualityTask qualityTask) {
        List<QualityTask> list = qualityTaskService.selectQualityTaskList(qualityTask);
        ExcelUtil<QualityTask> util = new ExcelUtil<QualityTask>(QualityTask.class);
        util.exportExcel(response, list, "检验任务数据");
    }

    /**
     * 获取检验任务详细信息（含检验结果明细）
     */
    @PreAuthorize("@ss.hasPermi('quality:task:query')")
    @GetMapping(value = "/{taskId}")
    public AjaxResult getInfo(@PathVariable("taskId") Long taskId) {
        return AjaxResult.success(qualityTaskService.selectQualityTaskDetail(taskId));
    }

    /**
     * 新增检验任务
     */
    @PreAuthorize("@ss.hasPermi('quality:task:add')")
    @Log(title = "检验任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QualityTask qualityTask) {
        qualityTask.setCreateBy(getUsername());
        return toAjax(qualityTaskService.insertQualityTask(qualityTask));
    }

    /**
     * 修改检验任务
     */
    @PreAuthorize("@ss.hasPermi('quality:task:edit')")
    @Log(title = "检验任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QualityTask qualityTask) {
        qualityTask.setUpdateBy(getUsername());
        return toAjax(qualityTaskService.updateQualityTask(qualityTask));
    }

    /**
     * 提交检验结果
     */
    @PreAuthorize("@ss.hasPermi('quality:task:check')")
    @Log(title = "检验任务", businessType = BusinessType.UPDATE)
    @PutMapping("/submitResult")
    @SuppressWarnings("unchecked")
    public AjaxResult submitResult(@RequestBody Map<String, Object> params) {
        Long taskId = Long.valueOf(params.get("taskId").toString());
        BigDecimal qualifiedQty = params.get("qualifiedQty") != null ? new BigDecimal(params.get("qualifiedQty").toString()) : BigDecimal.ZERO;
        BigDecimal unqualifiedQty = params.get("unqualifiedQty") != null ? new BigDecimal(params.get("unqualifiedQty").toString()) : BigDecimal.ZERO;

        // 解析结果列表
        List<Map<String, Object>> resultMaps = (List<Map<String, Object>>) params.get("resultList");
        java.util.ArrayList<QualityTaskResult> resultList = new java.util.ArrayList<>();
        if (resultMaps != null) {
            for (Map<String, Object> rm : resultMaps) {
                QualityTaskResult result = new QualityTaskResult();
                result.setItemNo(rm.get("itemNo") != null ? Integer.valueOf(rm.get("itemNo").toString()) : null);
                result.setItemName(rm.get("itemName") != null ? rm.get("itemName").toString() : null);
                result.setStandardValue(rm.get("standardValue") != null ? rm.get("standardValue").toString() : null);
                result.setActualValue(rm.get("actualValue") != null ? rm.get("actualValue").toString() : null);
                result.setMinValue(rm.get("minValue") != null ? new BigDecimal(rm.get("minValue").toString()) : null);
                result.setMaxValue(rm.get("maxValue") != null ? new BigDecimal(rm.get("maxValue").toString()) : null);
                result.setJudgeResult(rm.get("judgeResult") != null ? rm.get("judgeResult").toString() : null);
                result.setDefectType(rm.get("defectType") != null ? rm.get("defectType").toString() : null);
                result.setDefectLevel(rm.get("defectLevel") != null ? rm.get("defectLevel").toString() : null);
                resultList.add(result);
            }
        }

        return toAjax(qualityTaskService.submitCheckResult(taskId, resultList, qualifiedQty, unqualifiedQty, getUsername()));
    }

    /**
     * 删除检验任务
     */
    @PreAuthorize("@ss.hasPermi('quality:task:remove')")
    @Log(title = "检验任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{taskIds}")
    public AjaxResult remove(@PathVariable Long[] taskIds) {
        return toAjax(qualityTaskService.deleteQualityTaskByTaskIds(taskIds));
    }
}

