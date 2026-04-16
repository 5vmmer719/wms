package com.ruoyi.web.controller.base;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.base.service.IBaseWorkshopService;
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
import com.ruoyi.base.domain.BaseEquipment;
import com.ruoyi.base.service.IBaseEquipmentService;

/**
 * 设备管理Controller
 *
 * @author ruoyi
 * @date 2026-04-11
 */
@RestController
@RequestMapping("/base/equipment")
public class BaseEquipmentController extends BaseController {

    @Autowired
    private IBaseEquipmentService baseEquipmentService;

    @Autowired
    private IBaseWorkshopService baseWorkshopService;

    /**
     * 查询设备列表
     */
    @PreAuthorize("@ss.hasPermi('base:equipment:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseEquipment baseEquipment) {
        startPage();
        List<BaseEquipment> list = baseEquipmentService.selectBaseEquipmentList(baseEquipment);
        // 填充车间名称
        for (BaseEquipment eq : list) {
            fillWorkshopName(eq);
        }
        return getDataTable(list);
    }

    /**
     * 查询所有设备（不分页，用于下拉选择）
     */
    @GetMapping("/listAll")
    public List<BaseEquipment> listAll(BaseEquipment baseEquipment) {
        List<BaseEquipment> list = baseEquipmentService.selectBaseEquipmentList(baseEquipment);
        for (BaseEquipment eq : list) {
            fillWorkshopName(eq);
        }
        return list;
    }

    /**
     * 导出设备列表
     */
    @PreAuthorize("@ss.hasPermi('base:equipment:export')")
    @Log(title = "设备管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseEquipment baseEquipment) {
        List<BaseEquipment> list = baseEquipmentService.selectBaseEquipmentList(baseEquipment);
        ExcelUtil<BaseEquipment> util = new ExcelUtil<BaseEquipment>(BaseEquipment.class);
        util.exportExcel(response, list, "设备数据");
    }

    /**
     * 获取设备详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:equipment:query')")
    @GetMapping(value = "/{equipmentId}")
    public AjaxResult getInfo(@PathVariable("equipmentId") Long equipmentId) {
        BaseEquipment eq = baseEquipmentService.selectBaseEquipmentByEquipmentId(equipmentId);
        if (eq != null) {
            fillWorkshopName(eq);
        }
        return AjaxResult.success(eq);
    }

    /**
     * 新增设备
     */
    @PreAuthorize("@ss.hasPermi('base:equipment:add')")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseEquipment baseEquipment) {
        return toAjax(baseEquipmentService.insertBaseEquipment(baseEquipment));
    }

    /**
     * 修改设备
     */
    @PreAuthorize("@ss.hasPermi('base:equipment:edit')")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseEquipment baseEquipment) {
        return toAjax(baseEquipmentService.updateBaseEquipment(baseEquipment));
    }

    /**
     * 删除设备
     */
    @PreAuthorize("@ss.hasPermi('base:equipment:remove')")
    @Log(title = "设备管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{equipmentIds}")
    public AjaxResult remove(@PathVariable Long[] equipmentIds) {
        return toAjax(baseEquipmentService.deleteBaseEquipmentByEquipmentIds(equipmentIds));
    }

    /**
     * 填充车间名称
     */
    private void fillWorkshopName(BaseEquipment eq) {
        if (eq.getWorkshopCode() != null && !eq.getWorkshopCode().isEmpty()) {
            String workshopName = baseWorkshopService.selectBaseWorkshopByWorkshopCode(eq.getWorkshopCode());
            eq.setWorkshopName(workshopName);
        }
    }
}

