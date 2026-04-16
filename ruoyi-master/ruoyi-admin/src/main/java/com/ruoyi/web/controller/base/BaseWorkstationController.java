package com.ruoyi.web.controller.base;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.base.service.IBaseEquipmentService;
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
import com.ruoyi.base.domain.BaseWorkstation;
import com.ruoyi.base.service.IBaseWorkstationService;

/**
 * 工位管理Controller
 *
 * @author ruoyi
 * @date 2026-04-11
 */
@RestController
@RequestMapping("/base/workstation")
public class BaseWorkstationController extends BaseController {

    @Autowired
    private IBaseWorkstationService baseWorkstationService;

    @Autowired
    private IBaseEquipmentService baseEquipmentService;

    @Autowired
    private IBaseWorkshopService baseWorkshopService;

    /**
     * 查询工位列表
     */
    @PreAuthorize("@ss.hasPermi('base:workstation:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseWorkstation baseWorkstation) {
        startPage();
        List<BaseWorkstation> list = baseWorkstationService.selectBaseWorkstationList(baseWorkstation);
        for (BaseWorkstation ws : list) {
            fillNames(ws);
        }
        return getDataTable(list);
    }

    /**
     * 查询所有工位（不分页，用于下拉选择）
     */
    @GetMapping("/listAll")
    public List<BaseWorkstation> listAll(BaseWorkstation baseWorkstation) {
        List<BaseWorkstation> list = baseWorkstationService.selectBaseWorkstationList(baseWorkstation);
        for (BaseWorkstation ws : list) {
            fillNames(ws);
        }
        return list;
    }

    /**
     * 导出工位列表
     */
    @PreAuthorize("@ss.hasPermi('base:workstation:export')")
    @Log(title = "工位管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseWorkstation baseWorkstation) {
        List<BaseWorkstation> list = baseWorkstationService.selectBaseWorkstationList(baseWorkstation);
        ExcelUtil<BaseWorkstation> util = new ExcelUtil<BaseWorkstation>(BaseWorkstation.class);
        util.exportExcel(response, list, "工位数据");
    }

    /**
     * 获取工位详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:workstation:query')")
    @GetMapping(value = "/{stationId}")
    public AjaxResult getInfo(@PathVariable("stationId") Long stationId) {
        BaseWorkstation ws = baseWorkstationService.selectBaseWorkstationByStationId(stationId);
        if (ws != null) {
            fillNames(ws);
        }
        return AjaxResult.success(ws);
    }

    /**
     * 新增工位
     */
    @PreAuthorize("@ss.hasPermi('base:workstation:add')")
    @Log(title = "工位管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseWorkstation baseWorkstation) {
        return toAjax(baseWorkstationService.insertBaseWorkstation(baseWorkstation));
    }

    /**
     * 修改工位
     */
    @PreAuthorize("@ss.hasPermi('base:workstation:edit')")
    @Log(title = "工位管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseWorkstation baseWorkstation) {
        return toAjax(baseWorkstationService.updateBaseWorkstation(baseWorkstation));
    }

    /**
     * 删除工位
     */
    @PreAuthorize("@ss.hasPermi('base:workstation:remove')")
    @Log(title = "工位管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{stationIds}")
    public AjaxResult remove(@PathVariable Long[] stationIds) {
        return toAjax(baseWorkstationService.deleteBaseWorkstationByStationIds(stationIds));
    }

    /**
     * 填充设备名称和车间名称
     */
    private void fillNames(BaseWorkstation ws) {
        if (ws.getWorkshopCode() != null && !ws.getWorkshopCode().isEmpty()) {
            String workshopName = baseWorkshopService.selectBaseWorkshopByWorkshopCode(ws.getWorkshopCode());
            ws.setWorkshopName(workshopName);
        }
        // equipmentName 已通过 Mapper XML 的 LEFT JOIN 填充
    }
}

