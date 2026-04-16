package com.ruoyi.base.service;

import java.util.List;
import com.ruoyi.base.domain.BaseCustomer;

/**
 * 客户Service接口
 *
 * @author wms
 */
public interface IBaseCustomerService {

    public BaseCustomer selectBaseCustomerByCustomerId(Long customerId);

    public BaseCustomer selectBaseCustomerByCustomerCode(String customerCode);

    public List<BaseCustomer> selectBaseCustomerList(BaseCustomer baseCustomer);

    public int insertBaseCustomer(BaseCustomer baseCustomer);

    public int updateBaseCustomer(BaseCustomer baseCustomer);

    public int deleteBaseCustomerByCustomerId(Long customerId);

    public int deleteBaseCustomerByCustomerIds(Long[] customerIds);
}

