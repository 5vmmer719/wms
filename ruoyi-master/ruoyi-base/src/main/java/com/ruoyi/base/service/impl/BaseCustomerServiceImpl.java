package com.ruoyi.base.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.base.domain.BaseCustomer;
import com.ruoyi.base.mapper.BaseCustomerMapper;
import com.ruoyi.base.service.IBaseCustomerService;
import com.ruoyi.common.utils.DateUtils;

/**
 * 客户Service业务层处理
 *
 * @author wms
 */
@Service
public class BaseCustomerServiceImpl implements IBaseCustomerService {

    @Autowired
    private BaseCustomerMapper baseCustomerMapper;

    @Override
    public BaseCustomer selectBaseCustomerByCustomerId(Long customerId) {
        return baseCustomerMapper.selectBaseCustomerByCustomerId(customerId);
    }

    @Override
    public BaseCustomer selectBaseCustomerByCustomerCode(String customerCode) {
        return baseCustomerMapper.selectBaseCustomerByCustomerCode(customerCode);
    }

    @Override
    public List<BaseCustomer> selectBaseCustomerList(BaseCustomer baseCustomer) {
        return baseCustomerMapper.selectBaseCustomerList(baseCustomer);
    }

    @Override
    public int insertBaseCustomer(BaseCustomer baseCustomer) {
        baseCustomer.setCreateTime(DateUtils.getNowDate());
        return baseCustomerMapper.insertBaseCustomer(baseCustomer);
    }

    @Override
    public int updateBaseCustomer(BaseCustomer baseCustomer) {
        baseCustomer.setUpdateTime(DateUtils.getNowDate());
        return baseCustomerMapper.updateBaseCustomer(baseCustomer);
    }

    @Override
    public int deleteBaseCustomerByCustomerId(Long customerId) {
        return baseCustomerMapper.deleteBaseCustomerByCustomerId(customerId);
    }

    @Override
    public int deleteBaseCustomerByCustomerIds(Long[] customerIds) {
        return baseCustomerMapper.deleteBaseCustomerByCustomerIds(customerIds);
    }
}

