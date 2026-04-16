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
import com.ruoyi.base.domain.QualityStandard;
import com.ruoyi.base.service.IQualityStandardService;

/**
 * 检验标准Controller
 *
 * @author summer
 * @date 2026-04-11
 */
@RestController
@RequestMapping("/quality/standard")
public class QualityStandardController extends BaseController {

    @Autowired
    private IQualityStandardService qualityStandardService;

    /**
     * 查询检验标准列表
     */
    @PreAuthorize("@ss.hasPermi('quality:standard:list')")
    @GetMapping("/list")
    public TableDataInfo list(QualityStandard qualityStandard) {
        startPage();
        List<QualityStandard> list = qualityStandardService.selectQualityStandardList(qualityStandard);
        return getDataTable(list);
    }

    /**
     * 查询所有检验标准（用于下拉框选择）
     */
    @GetMapping("/listAll")
    public List<QualityStandard> listAll(QualityStandard qualityStandard) {
        return qualityStandardService.selectQualityStandardList(qualityStandard);
    }

    /**
     * 导出检验标准列表
     */
    @PreAuthorize("@ss.hasPermi('quality:standard:export')")
    @Log(title = "检验标准", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QualityStandard qualityStandard) {
        List<QualityStandard> list = qualityStandardService.selectQualityStandardList(qualityStandard);
        ExcelUtil<QualityStandard> util = new ExcelUtil<QualityStandard>(QualityStandard.class);
        util.exportExcel(response, list, "检验标准数据");
    }

    /**
     * 获取检验标准详细信息（含检验项目）
     */
    @PreAuthorize("@ss.hasPermi('quality:standard:query')")
    @GetMapping(value = "/{standardId}")
    public AjaxResult getInfo(@PathVariable("standardId") Long standardId) {
        return AjaxResult.success(qualityStandardService.selectQualityStandardDetail(standardId));
    }

    /**
     * 新增检验标准
     */
    @PreAuthorize("@ss.hasPermi('quality:standard:add')")
    @Log(title = "检验标准", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QualityStandard qualityStandard) {
        qualityStandard.setCreateBy(getUsername());
        return toAjax(qualityStandardService.insertQualityStandard(qualityStandard));
    }

    /**
     * 修改检验标准
     */
    @PreAuthorize("@ss.hasPermi('quality:standard:edit')")
    @Log(title = "检验标准", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QualityStandard qualityStandard) {
        qualityStandard.setUpdateBy(getUsername());
        return toAjax(qualityStandardService.updateQualityStandard(qualityStandard));
    }

    /**
     * 删除检验标准
     */
    @PreAuthorize("@ss.hasPermi('quality:standard:remove')")
    @Log(title = "检验标准", businessType = BusinessType.DELETE)
    @DeleteMapping("/{standardIds}")
    public AjaxResult remove(@PathVariable Long[] standardIds) {
        return toAjax(qualityStandardService.deleteQualityStandardByStandardIds(standardIds));
    }
}

