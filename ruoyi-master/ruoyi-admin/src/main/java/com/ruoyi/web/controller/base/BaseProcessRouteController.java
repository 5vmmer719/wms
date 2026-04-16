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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.base.domain.BaseProcessRoute;
import com.ruoyi.base.domain.BaseProcessStep;
import com.ruoyi.base.domain.BaseProcessParam;
import com.ruoyi.base.service.IBaseProcessRouteService;
import com.ruoyi.base.service.IBaseProcessStepService;
import com.ruoyi.base.service.IBaseProcessParamService;

/**
 * 工艺路线Controller
 *
 * @author summer
 * @date 2026-04-11
 */
@RestController
@RequestMapping("/base/processRoute")
public class BaseProcessRouteController extends BaseController {

    @Autowired
    private IBaseProcessRouteService baseProcessRouteService;

    @Autowired
    private IBaseProcessStepService baseProcessStepService;

    @Autowired
    private IBaseProcessParamService baseProcessParamService;

    // ==================== 工艺路线 CRUD ====================

    /**
     * 查询工艺路线列表
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseProcessRoute baseProcessRoute) {
        startPage();
        List<BaseProcessRoute> list = baseProcessRouteService.selectBaseProcessRouteList(baseProcessRoute);
        return getDataTable(list);
    }

    /**
     * 查询工艺路线列表（所有） - 下拉选择框数据
     */
    @GetMapping("/listAll")
    public List<BaseProcessRoute> listAll(BaseProcessRoute baseProcessRoute) {
        return baseProcessRouteService.selectBaseProcessRouteList(baseProcessRoute);
    }

    /**
     * 导出工艺路线列表
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:export')")
    @Log(title = "工艺路线", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseProcessRoute baseProcessRoute) {
        List<BaseProcessRoute> list = baseProcessRouteService.selectBaseProcessRouteList(baseProcessRoute);
        ExcelUtil<BaseProcessRoute> util = new ExcelUtil<BaseProcessRoute>(BaseProcessRoute.class);
        util.exportExcel(response, list, "工艺路线数据");
    }

    /**
     * 获取工艺路线详细信息（含工序和参数）
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:query')")
    @GetMapping(value = "/{routeId}")
    public AjaxResult getInfo(@PathVariable("routeId") Long routeId) {
        return AjaxResult.success(baseProcessRouteService.selectBaseProcessRouteDetail(routeId));
    }

    /**
     * 新增工艺路线
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:add')")
    @Log(title = "工艺路线", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseProcessRoute baseProcessRoute) {
        baseProcessRoute.setCreateBy(getUsername());
        return toAjax(baseProcessRouteService.insertBaseProcessRoute(baseProcessRoute));
    }

    /**
     * 修改工艺路线
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:edit')")
    @Log(title = "工艺路线", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseProcessRoute baseProcessRoute) {
        baseProcessRoute.setUpdateBy(getUsername());
        return toAjax(baseProcessRouteService.updateBaseProcessRoute(baseProcessRoute));
    }

    /**
     * 删除工艺路线（级联删除工序和参数）
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:remove')")
    @Log(title = "工艺路线", businessType = BusinessType.DELETE)
    @DeleteMapping("/{routeIds}")
    public AjaxResult remove(@PathVariable Long[] routeIds) {
        return toAjax(baseProcessRouteService.deleteBaseProcessRouteByRouteIds(routeIds));
    }

    // ==================== 工序 CRUD ====================

    /**
     * 根据工艺路线编码查询工序列表
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:query')")
    @GetMapping("/step/list")
    public TableDataInfo stepList(BaseProcessStep baseProcessStep) {
        startPage();
        List<BaseProcessStep> list = baseProcessStepService.selectBaseProcessStepList(baseProcessStep);
        return getDataTable(list);
    }

    /**
     * 获取工序详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:query')")
    @GetMapping(value = "/step/{stepId}")
    public AjaxResult getStepInfo(@PathVariable("stepId") Long stepId) {
        return AjaxResult.success(baseProcessStepService.selectBaseProcessStepByStepId(stepId));
    }

    /**
     * 新增工序
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:add')")
    @Log(title = "工序", businessType = BusinessType.INSERT)
    @PostMapping("/step")
    public AjaxResult addStep(@RequestBody BaseProcessStep baseProcessStep) {
        baseProcessStep.setCreateBy(getUsername());
        return toAjax(baseProcessStepService.insertBaseProcessStep(baseProcessStep));
    }

    /**
     * 修改工序
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:edit')")
    @Log(title = "工序", businessType = BusinessType.UPDATE)
    @PutMapping("/step")
    public AjaxResult editStep(@RequestBody BaseProcessStep baseProcessStep) {
        baseProcessStep.setUpdateBy(getUsername());
        return toAjax(baseProcessStepService.updateBaseProcessStep(baseProcessStep));
    }

    /**
     * 删除工序（级联删除参数）
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:remove')")
    @Log(title = "工序", businessType = BusinessType.DELETE)
    @DeleteMapping("/step/{stepIds}")
    public AjaxResult removeStep(@PathVariable Long[] stepIds) {
        return toAjax(baseProcessStepService.deleteBaseProcessStepByStepIds(stepIds));
    }

    // ==================== 工艺参数 CRUD ====================

    /**
     * 根据工序ID查询工艺参数列表
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:query')")
    @GetMapping("/param/list")
    public TableDataInfo paramList(BaseProcessParam baseProcessParam) {
        startPage();
        List<BaseProcessParam> list = baseProcessParamService.selectBaseProcessParamList(baseProcessParam);
        return getDataTable(list);
    }

    /**
     * 获取工艺参数详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:query')")
    @GetMapping(value = "/param/{paramId}")
    public AjaxResult getParamInfo(@PathVariable("paramId") Long paramId) {
        return AjaxResult.success(baseProcessParamService.selectBaseProcessParamByParamId(paramId));
    }

    /**
     * 新增工艺参数
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:add')")
    @Log(title = "工艺参数", businessType = BusinessType.INSERT)
    @PostMapping("/param")
    public AjaxResult addParam(@RequestBody BaseProcessParam baseProcessParam) {
        baseProcessParam.setCreateBy(getUsername());
        return toAjax(baseProcessParamService.insertBaseProcessParam(baseProcessParam));
    }

    /**
     * 修改工艺参数
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:edit')")
    @Log(title = "工艺参数", businessType = BusinessType.UPDATE)
    @PutMapping("/param")
    public AjaxResult editParam(@RequestBody BaseProcessParam baseProcessParam) {
        baseProcessParam.setUpdateBy(getUsername());
        return toAjax(baseProcessParamService.updateBaseProcessParam(baseProcessParam));
    }

    /**
     * 删除工艺参数
     */
    @PreAuthorize("@ss.hasPermi('base:processRoute:remove')")
    @Log(title = "工艺参数", businessType = BusinessType.DELETE)
    @DeleteMapping("/param/{paramIds}")
    public AjaxResult removeParam(@PathVariable Long[] paramIds) {
        return toAjax(baseProcessParamService.deleteBaseProcessParamByParamIds(paramIds));
    }
}

