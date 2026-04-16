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
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.base.domain.BaseEquipmentMaintain;
import com.ruoyi.base.service.IBaseEquipmentMaintainService;

/**
 * 设备维护记录Controller
 *
 * @author ruoyi
 * @date 2026-04-11
 */
@RestController
@RequestMapping("/base/maintain")
public class BaseEquipmentMaintainController extends BaseController {

    @Autowired
    private IBaseEquipmentMaintainService baseEquipmentMaintainService;

    /**
     * 查询维护记录列表
     */
    @PreAuthorize("@ss.hasPermi('base:maintain:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseEquipmentMaintain baseEquipmentMaintain) {
        startPage();
        List<BaseEquipmentMaintain> list = baseEquipmentMaintainService.selectBaseEquipmentMaintainList(baseEquipmentMaintain);
        return getDataTable(list);
    }

    /**
     * 导出维护记录列表
     */
    @PreAuthorize("@ss.hasPermi('base:maintain:export')")
    @Log(title = "设备维护记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseEquipmentMaintain baseEquipmentMaintain) {
        List<BaseEquipmentMaintain> list = baseEquipmentMaintainService.selectBaseEquipmentMaintainList(baseEquipmentMaintain);
        ExcelUtil<BaseEquipmentMaintain> util = new ExcelUtil<BaseEquipmentMaintain>(BaseEquipmentMaintain.class);
        util.exportExcel(response, list, "设备维护记录");
    }

    /**
     * 获取维护记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:maintain:query')")
    @GetMapping(value = "/{maintainId}")
    public AjaxResult getInfo(@PathVariable("maintainId") Long maintainId) {
        return AjaxResult.success(baseEquipmentMaintainService.selectBaseEquipmentMaintainByMaintainId(maintainId));
    }

    /**
     * 新增维护记录
     */
    @PreAuthorize("@ss.hasPermi('base:maintain:add')")
    @Log(title = "设备维护记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseEquipmentMaintain baseEquipmentMaintain) {
        return toAjax(baseEquipmentMaintainService.insertBaseEquipmentMaintain(baseEquipmentMaintain));
    }

    /**
     * 修改维护记录
     */
    @PreAuthorize("@ss.hasPermi('base:maintain:edit')")
    @Log(title = "设备维护记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseEquipmentMaintain baseEquipmentMaintain) {
        return toAjax(baseEquipmentMaintainService.updateBaseEquipmentMaintain(baseEquipmentMaintain));
    }

    /**
     * 完成维护
     */
    @PreAuthorize("@ss.hasPermi('base:maintain:edit')")
    @Log(title = "设备维护记录", businessType = BusinessType.UPDATE)
    @PutMapping("/complete/{maintainId}")
    public AjaxResult complete(@PathVariable("maintainId") Long maintainId) {
        return toAjax(baseEquipmentMaintainService.completeMaintain(maintainId));
    }

    /**
     * 删除维护记录
     */
    @PreAuthorize("@ss.hasPermi('base:maintain:remove')")
    @Log(title = "设备维护记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{maintainIds}")
    public AjaxResult remove(@PathVariable Long[] maintainIds) {
        return toAjax(baseEquipmentMaintainService.deleteBaseEquipmentMaintainByMaintainIds(maintainIds));
    }
}

