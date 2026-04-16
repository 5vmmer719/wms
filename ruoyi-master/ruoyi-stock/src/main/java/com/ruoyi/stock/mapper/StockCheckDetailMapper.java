package com.ruoyi.stock.mapper;

import java.util.List;

import com.ruoyi.stock.domain.StockCheckDetail;

/**
 * 盘点明细Mapper接口
 *
 * @author wms
 */
public interface StockCheckDetailMapper {

    public StockCheckDetail selectStockCheckDetailByDetailId(Long detailId);

    public List<StockCheckDetail> selectStockCheckDetailByCheckNo(String checkNo);

    public int insertStockCheckDetail(StockCheckDetail stockCheckDetail);

    public int insertBatchStockCheckDetail(List<StockCheckDetail> list);

    public int updateStockCheckDetail(StockCheckDetail stockCheckDetail);

    public int deleteStockCheckDetailByCheckNo(String checkNo);

    public int deleteStockCheckDetailByDetailId(Long detailId);
}

