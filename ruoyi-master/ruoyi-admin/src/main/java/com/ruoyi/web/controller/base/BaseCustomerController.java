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
import com.ruoyi.base.domain.BaseCustomer;
import com.ruoyi.base.service.IBaseCustomerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客户管理Controller
 *
 * @author wms
 */
@RestController
@RequestMapping("/base/customer")
public class BaseCustomerController extends BaseController {

    @Autowired
    private IBaseCustomerService baseCustomerService;

    /**
     * 查询客户列表
     */
    @PreAuthorize("@ss.hasPermi('base:customer:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseCustomer baseCustomer) {
        startPage();
        List<BaseCustomer> list = baseCustomerService.selectBaseCustomerList(baseCustomer);
        return getDataTable(list);
    }

    /**
     * 查询全部客户（不分页，供下拉选择）
     */
    @GetMapping("/listAll")
    public AjaxResult listAll(BaseCustomer baseCustomer) {
        List<BaseCustomer> list = baseCustomerService.selectBaseCustomerList(baseCustomer);
        return AjaxResult.success(list);
    }

    /**
     * 导出客户列表
     */
    @PreAuthorize("@ss.hasPermi('base:customer:export')")
    @Log(title = "客户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseCustomer baseCustomer) {
        List<BaseCustomer> list = baseCustomerService.selectBaseCustomerList(baseCustomer);
        ExcelUtil<BaseCustomer> util = new ExcelUtil<BaseCustomer>(BaseCustomer.class);
        util.exportExcel(response, list, "客户数据");
    }

    /**
     * 获取客户详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:customer:query')")
    @GetMapping(value = "/{customerId}")
    public AjaxResult getInfo(@PathVariable("customerId") Long customerId) {
        return AjaxResult.success(baseCustomerService.selectBaseCustomerByCustomerId(customerId));
    }

    /**
     * 新增客户
     */
    @PreAuthorize("@ss.hasPermi('base:customer:add')")
    @Log(title = "客户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseCustomer baseCustomer) {
        baseCustomer.setCreateBy(getUsername());
        return toAjax(baseCustomerService.insertBaseCustomer(baseCustomer));
    }

    /**
     * 修改客户
     */
    @PreAuthorize("@ss.hasPermi('base:customer:edit')")
    @Log(title = "客户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseCustomer baseCustomer) {
        baseCustomer.setUpdateBy(getUsername());
        return toAjax(baseCustomerService.updateBaseCustomer(baseCustomer));
    }

    /**
     * 删除客户
     */
    @PreAuthorize("@ss.hasPermi('base:customer:remove')")
    @Log(title = "客户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{customerIds}")
    public AjaxResult remove(@PathVariable Long[] customerIds) {
        return toAjax(baseCustomerService.deleteBaseCustomerByCustomerIds(customerIds));
    }
}

