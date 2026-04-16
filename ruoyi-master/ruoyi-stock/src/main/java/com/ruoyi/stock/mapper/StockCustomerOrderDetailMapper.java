package com.ruoyi.stock.mapper;

import java.util.List;
import com.ruoyi.stock.domain.StockCustomerOrderDetail;

/**
 * 客户订单明细Mapper接口
 *
 * @author wms
 */
public interface StockCustomerOrderDetailMapper {

    /**
     * 查询订单明细
     */
    public StockCustomerOrderDetail selectStockCustomerOrderDetailByDetailId(Long detailId);

    /**
     * 根据订单号查询明细列表
     */
    public List<StockCustomerOrderDetail> selectDetailListByOrderNo(String orderNo);

    /**
     * 查询订单明细列表
     */
    public List<StockCustomerOrderDetail> selectStockCustomerOrderDetailList(StockCustomerOrderDetail detail);

    /**
     * 批量新增订单明细
     */
    public int insertStockCustomerOrderDetailList(List<StockCustomerOrderDetail> detailList);

    /**
     * 修改订单明细
     */
    public int updateStockCustomerOrderDetail(StockCustomerOrderDetail detail);

    /**
     * 根据订单号删除明细（软删除）
     */
    public int deleteDetailByOrderNo(String orderNo);

    /**
     * 删除订单明细
     */
    public int deleteStockCustomerOrderDetailByDetailId(Long detailId);

    /**
     * 批量删除订单明细
     */
    public int deleteStockCustomerOrderDetailByDetailIds(Long[] detailIds);
}

