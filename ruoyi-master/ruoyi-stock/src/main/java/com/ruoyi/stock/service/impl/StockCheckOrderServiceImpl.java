package com.ruoyi.stock.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.bean.typeEnum.StockRecordTypeEnum;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.OrderNoUtil;
import com.ruoyi.stock.domain.StockCheckDetail;
import com.ruoyi.stock.domain.StockCheckOrder;
import com.ruoyi.stock.domain.StockInfo;
import com.ruoyi.stock.domain.StockRecord;
import com.ruoyi.stock.mapper.StockCheckDetailMapper;
import com.ruoyi.stock.mapper.StockCheckOrderMapper;
import com.ruoyi.stock.mapper.StockInfoMapper;
import com.ruoyi.stock.mapper.StockRecordMapper;
import com.ruoyi.stock.service.IStockCheckOrderService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存盘点Service实现
 *
 * @author wms
 */
@Service
public class StockCheckOrderServiceImpl implements IStockCheckOrderService {

    private static final String STATUS_CREATED = "created";
    private static final String STATUS_COUNTING = "counting";
    private static final String STATUS_COMPLETED = "completed";
    private static final String STATUS_ADJUSTED = "adjusted";

    @Autowired
    private StockCheckOrderMapper stockCheckOrderMapper;
    @Autowired
    private StockCheckDetailMapper stockCheckDetailMapper;
    @Autowired
    private StockInfoMapper stockInfoMapper;
    @Autowired
    private StockRecordMapper stockRecordMapper;

    @Override
    public StockCheckOrder selectStockCheckOrderByCheckId(Long checkId) {
        StockCheckOrder order = stockCheckOrderMapper.selectStockCheckOrderByCheckId(checkId);
        if (order != null) {
            List<StockCheckDetail> details = stockCheckDetailMapper.selectStockCheckDetailByCheckNo(order.getCheckNo());
            order.setDetailList(details);
        }
        return order;
    }

    @Override
    public List<StockCheckOrder> selectStockCheckOrderList(StockCheckOrder stockCheckOrder) {
        return stockCheckOrderMapper.selectStockCheckOrderList(stockCheckOrder);
    }

    /**
     * 新增盘点单：自动从stock_info快照当前库存数据生成盘点明细
     */
    @Override
    @Transactional
    public AjaxResult insertStockCheckOrder(String username, StockCheckOrder stockCheckOrder) {
        String warehouseCode = stockCheckOrder.getWarehouseCode();
        if (StringUtils.isEmpty(warehouseCode)) {
            return AjaxResult.error("请选择盘点仓库");
        }

        // 生成盘点单号
        String checkNo = OrderNoUtil.generate(OrderNoUtil.OrderPrefix.STOCK_CHECK);
        Date nowDate = DateUtils.getNowDate();

        stockCheckOrder.setCheckNo(checkNo);
        stockCheckOrder.setCheckStatus(STATUS_CREATED);
        stockCheckOrder.setCreateBy(username);
        stockCheckOrder.setCreateTime(nowDate);

        // 从stock_info快照当前仓库的库存数据
        StockInfo queryParam = new StockInfo();
        queryParam.setWarehouseCode(warehouseCode);
        List<StockInfo> stockInfoList = stockInfoMapper.selectStockInfoList(queryParam);

        if (CollectionUtils.isEmpty(stockInfoList)) {
            return AjaxResult.error("该仓库暂无库存数据，无法创建盘点单");
        }

        // 过滤掉数量为0或负数的记录
        List<StockCheckDetail> detailList = new ArrayList<>();
        for (StockInfo info : stockInfoList) {
            if (info.getQuantity() != null && info.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                StockCheckDetail detail = new StockCheckDetail();
                detail.setCheckNo(checkNo);
                detail.setMatCode(info.getMatCode());
                detail.setMatName(info.getMatName());
                detail.setWarehouseCode(info.getWarehouseCode());
                detail.setLocationCode(info.getLocationCode());
                detail.setBatch(info.getBatch());
                detail.setUnitCode(info.getUnitCode());
                detail.setSystemQty(info.getQuantity());
                detail.setActualQty(null); // 实盘数量待录入
                detail.setDiffQty(null);
                detail.setCreateBy(username);
                detail.setCreateTime(nowDate);
                detailList.add(detail);
            }
        }

        if (detailList.isEmpty()) {
            return AjaxResult.error("该仓库无有效库存记录，无法创建盘点单");
        }

        stockCheckOrder.setTotalItems(detailList.size());
        stockCheckOrder.setDiffItems(0);
        stockCheckOrderMapper.insertStockCheckOrder(stockCheckOrder);
        stockCheckDetailMapper.insertBatchStockCheckDetail(detailList);

        return AjaxResult.success("盘点单创建成功，共快照 " + detailList.size() + " 条库存记录");
    }

    @Override
    public int updateStockCheckOrder(StockCheckOrder stockCheckOrder) {
        stockCheckOrder.setUpdateTime(DateUtils.getNowDate());
        return stockCheckOrderMapper.updateStockCheckOrder(stockCheckOrder);
    }

    /**
     * 提交盘点结果：录入实盘数量，计算差异
     */
    @Override
    @Transactional
    public AjaxResult submitCheckResult(String username, StockCheckOrder stockCheckOrder) {
        StockCheckOrder dbOrder = stockCheckOrderMapper.selectStockCheckOrderByCheckId(stockCheckOrder.getCheckId());
        if (dbOrder == null) {
            return AjaxResult.error("盘点单不存在");
        }
        if (!STATUS_CREATED.equals(dbOrder.getCheckStatus()) && !STATUS_COUNTING.equals(dbOrder.getCheckStatus())) {
            return AjaxResult.error("只有已创建或盘点中状态的盘点单才能提交结果");
        }

        List<StockCheckDetail> detailList = stockCheckOrder.getDetailList();
        if (CollectionUtils.isEmpty(detailList)) {
            return AjaxResult.error("盘点明细不能为空");
        }

        Date nowDate = DateUtils.getNowDate();
        int diffCount = 0;

        for (StockCheckDetail detail : detailList) {
            if (detail.getActualQty() == null) {
                continue; // 跳过未录入的
            }
            BigDecimal systemQty = detail.getSystemQty() != null ? detail.getSystemQty() : BigDecimal.ZERO;
            BigDecimal diffQty = detail.getActualQty().subtract(systemQty);
            detail.setDiffQty(diffQty);
            detail.setUpdateBy(username);
            detail.setUpdateTime(nowDate);
            stockCheckDetailMapper.updateStockCheckDetail(detail);

            if (diffQty.compareTo(BigDecimal.ZERO) != 0) {
                diffCount++;
            }
        }

        // 更新盘点单状态
        StockCheckOrder updateOrder = new StockCheckOrder();
        updateOrder.setCheckId(dbOrder.getCheckId());
        updateOrder.setCheckStatus(STATUS_COMPLETED);
        updateOrder.setActualDate(nowDate);
        updateOrder.setDiffItems(diffCount);
        updateOrder.setUpdateBy(username);
        updateOrder.setUpdateTime(nowDate);
        stockCheckOrderMapper.updateStockCheckOrder(updateOrder);

        return AjaxResult.success("盘点结果提交成功，共 " + diffCount + " 项存在差异");
    }

    /**
     * 执行盘点调整：根据差异自动生成盘盈入库/盘亏出库的库存流水，并更新stock_info
     */
    @Override
    @Transactional
    public AjaxResult adjustStock(String username, Long checkId) {
        StockCheckOrder order = stockCheckOrderMapper.selectStockCheckOrderByCheckId(checkId);
        if (order == null) {
            return AjaxResult.error("盘点单不存在");
        }
        if (!STATUS_COMPLETED.equals(order.getCheckStatus())) {
            return AjaxResult.error("只有已完成状态的盘点单才能执行调整");
        }

        List<StockCheckDetail> detailList = stockCheckDetailMapper.selectStockCheckDetailByCheckNo(order.getCheckNo());
        if (CollectionUtils.isEmpty(detailList)) {
            return AjaxResult.error("盘点明细为空");
        }

        Date nowDate = DateUtils.getNowDate();
        int profitCount = 0;
        int lossCount = 0;

        for (StockCheckDetail detail : detailList) {
            if (detail.getDiffQty() == null || detail.getDiffQty().compareTo(BigDecimal.ZERO) == 0) {
                continue; // 无差异，跳过
            }
            if ("1".equals(detail.getAdjustFlag())) {
                continue; // 已调整，跳过
            }

            BigDecimal diffQty = detail.getDiffQty();
            String recordType;
            BigDecimal adjustQty;

            if (diffQty.compareTo(BigDecimal.ZERO) > 0) {
                // 盘盈：实盘 > 系统，需入库
                recordType = StockRecordTypeEnum.CHECK_PROFIT.getValue();
                adjustQty = diffQty;
                profitCount++;
            } else {
                // 盘亏：实盘 < 系统，需出库
                recordType = StockRecordTypeEnum.CHECK_LOSS.getValue();
                adjustQty = diffQty.abs();
                lossCount++;
            }

            // 更新库存：直接用实盘数量覆盖系统数量不合理，应该用差异量调整
            // 盘盈：quantity + diffQty（正数）；盘亏：quantity + diffQty（负数）
            stockInfoMapper.updateQuantityByMatCode(
                detail.getWarehouseCode(),
                detail.getMatCode(),
                diffQty.negate() // updateQuantityByMatCode 是 quantity - #{quantity}，所以盘盈传负数，盘亏传正数
            );

            // 生成库存流水记录
            StockRecord record = new StockRecord();
            record.setWarehouseCode(detail.getWarehouseCode());
            record.setLocationCode(detail.getLocationCode());
            record.setMatCode(detail.getMatCode());
            record.setMatName(detail.getMatName());
            record.setBatch(detail.getBatch());
            record.setRecordType(recordType);
            record.setQuantity(adjustQty);
            record.setOrderNo(order.getCheckNo());
            record.setCreateBy(username);
            record.setCreateTime(nowDate);
            stockRecordMapper.insertStockRecord(record);

            // 标记明细已调整
            StockCheckDetail updateDetail = new StockCheckDetail();
            updateDetail.setDetailId(detail.getDetailId());
            updateDetail.setAdjustFlag("1");
            updateDetail.setUpdateBy(username);
            updateDetail.setUpdateTime(nowDate);
            stockCheckDetailMapper.updateStockCheckDetail(updateDetail);
        }

        // 更新盘点单状态为已调整
        StockCheckOrder updateOrder = new StockCheckOrder();
        updateOrder.setCheckId(checkId);
        updateOrder.setCheckStatus(STATUS_ADJUSTED);
        updateOrder.setUpdateBy(username);
        updateOrder.setUpdateTime(nowDate);
        stockCheckOrderMapper.updateStockCheckOrder(updateOrder);

        return AjaxResult.success("盘点调整完成，盘盈 " + profitCount + " 项，盘亏 " + lossCount + " 项");
    }

    @Override
    public int deleteStockCheckOrderByCheckIds(Long[] checkIds) {
        return stockCheckOrderMapper.deleteStockCheckOrderByCheckIds(checkIds);
    }

    @Override
    public int deleteStockCheckOrderByCheckId(Long checkId) {
        return stockCheckOrderMapper.deleteStockCheckOrderByCheckId(checkId);
    }
}

