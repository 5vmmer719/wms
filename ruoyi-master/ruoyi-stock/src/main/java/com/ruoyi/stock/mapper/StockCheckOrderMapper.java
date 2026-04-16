package com.ruoyi.stock.mapper;

import java.util.List;

import com.ruoyi.stock.domain.StockCheckOrder;

/**
 * 盘点单Mapper接口
 *
 * @author wms
 */
public interface StockCheckOrderMapper {

    public StockCheckOrder selectStockCheckOrderByCheckId(Long checkId);

    public StockCheckOrder selectStockCheckOrderByCheckNo(String checkNo);

    public List<StockCheckOrder> selectStockCheckOrderList(StockCheckOrder stockCheckOrder);

    public int insertStockCheckOrder(StockCheckOrder stockCheckOrder);

    public int updateStockCheckOrder(StockCheckOrder stockCheckOrder);

    public int deleteStockCheckOrderByCheckId(Long checkId);

    public int deleteStockCheckOrderByCheckIds(Long[] checkIds);
}

