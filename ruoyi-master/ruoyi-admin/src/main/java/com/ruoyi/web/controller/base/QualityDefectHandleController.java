package com.ruoyi.web.controller.base;

import java.util.List;
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
import com.ruoyi.base.domain.QualityDefectHandle;
import com.ruoyi.base.service.IQualityDefectHandleService;

/**
 * 不合格品处理Controller
 *
 * @author summer
 * @date 2026-04-11
 */
@RestController
@RequestMapping("/quality/defectHandle")
public class QualityDefectHandleController extends BaseController {

    @Autowired
    private IQualityDefectHandleService qualityDefectHandleService;

    /**
     * 查询不合格品处理列表
     */
    @PreAuthorize("@ss.hasPermi('quality:defectHandle:list')")
    @GetMapping("/list")
    public TableDataInfo list(QualityDefectHandle qualityDefectHandle) {
        startPage();
        List<QualityDefectHandle> list = qualityDefectHandleService.selectQualityDefectHandleList(qualityDefectHandle);
        return getDataTable(list);
    }

    /**
     * 导出不合格品处理列表
     */
    @PreAuthorize("@ss.hasPermi('quality:defectHandle:export')")
    @Log(title = "不合格品处理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QualityDefectHandle qualityDefectHandle) {
        List<QualityDefectHandle> list = qualityDefectHandleService.selectQualityDefectHandleList(qualityDefectHandle);
        ExcelUtil<QualityDefectHandle> util = new ExcelUtil<QualityDefectHandle>(QualityDefectHandle.class);
        util.exportExcel(response, list, "不合格品处理数据");
    }

    /**
     * 获取不合格品处理详细信息
     */
    @PreAuthorize("@ss.hasPermi('quality:defectHandle:query')")
    @GetMapping(value = "/{handleId}")
    public AjaxResult getInfo(@PathVariable("handleId") Long handleId) {
        return AjaxResult.success(qualityDefectHandleService.selectQualityDefectHandleByHandleId(handleId));
    }

    /**
     * 新增不合格品处理
     */
    @PreAuthorize("@ss.hasPermi('quality:defectHandle:add')")
    @Log(title = "不合格品处理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QualityDefectHandle qualityDefectHandle) {
        qualityDefectHandle.setCreateBy(getUsername());
        return toAjax(qualityDefectHandleService.insertQualityDefectHandle(qualityDefectHandle));
    }

    /**
     * 修改不合格品处理
     */
    @PreAuthorize("@ss.hasPermi('quality:defectHandle:edit')")
    @Log(title = "不合格品处理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QualityDefectHandle qualityDefectHandle) {
        qualityDefectHandle.setUpdateBy(getUsername());
        return toAjax(qualityDefectHandleService.updateQualityDefectHandle(qualityDefectHandle));
    }

    /**
     * 删除不合格品处理
     */
    @PreAuthorize("@ss.hasPermi('quality:defectHandle:remove')")
    @Log(title = "不合格品处理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{handleIds}")
    public AjaxResult remove(@PathVariable Long[] handleIds) {
        return toAjax(qualityDefectHandleService.deleteQualityDefectHandleByHandleIds(handleIds));
    }
}

