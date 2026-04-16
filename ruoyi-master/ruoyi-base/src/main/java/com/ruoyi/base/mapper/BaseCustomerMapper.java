package com.ruoyi.base.mapper;

import java.util.List;
import com.ruoyi.base.domain.BaseCustomer;

/**
 * 客户Mapper接口
 *
 * @author wms
 */
public interface BaseCustomerMapper {

    /**
     * 查询客户
     */
    public BaseCustomer selectBaseCustomerByCustomerId(Long customerId);

    /**
     * 根据客户编码查询
     */
    public BaseCustomer selectBaseCustomerByCustomerCode(String customerCode);

    /**
     * 查询客户列表
     */
    public List<BaseCustomer> selectBaseCustomerList(BaseCustomer baseCustomer);

    /**
     * 新增客户
     */
    public int insertBaseCustomer(BaseCustomer baseCustomer);

    /**
     * 修改客户
     */
    public int updateBaseCustomer(BaseCustomer baseCustomer);

    /**
     * 删除客户
     */
    public int deleteBaseCustomerByCustomerId(Long customerId);

    /**
     * 批量删除客户
     */
    public int deleteBaseCustomerByCustomerIds(Long[] customerIds);
}

