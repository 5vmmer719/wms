package com.ruoyi.stock.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.bean.typeEnum.OrderStatusEnum;
import com.ruoyi.common.bean.typeEnum.OutOrderReturnTypeEnum;
import com.ruoyi.common.bean.typeEnum.StockRecordTypeEnum;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.OrderNoUtil;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.stock.domain.StockInReturnDetail;
import com.ruoyi.stock.domain.StockOutReturnDetail;
import com.ruoyi.stock.domain.StockRecord;
import com.ruoyi.stock.mapper.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.stock.domain.StockOutReturn;
import com.ruoyi.stock.service.IStockOutReturnService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 出库单退货Service业务层处理
 *
 * @author summer
 * @date 2022-07-25
 */
@Service
public class StockOutReturnServiceImpl implements IStockOutReturnService {
    @Autowired
    private StockOutReturnMapper stockOutReturnMapper;
    @Autowired
    private StockOutReturnDetailMapper stockOutReturnDetailMapper;
    @Autowired
    private StockMatLabelMapper stockMatLabelMapper;
    @Autowired
    private StockInfoMapper stockInfoMapper;
    @Autowired
    private StockRecordMapper stockRecordMapper;

    /**
     * 查询出库单退货数量
     */
    @Override
    public Map<String, Long> selectStockOutReturnTotal(Date selectDate){
        return stockOutReturnMapper.selectStockOutReturnTotal(selectDate);
    }

    /**
     * 查询出库单退货
     *
     * @param returnId 出库单退货主键
     * @return 出库单退货
     */
    @Override
    public StockOutReturn selectStockOutReturnByReturnId(Long returnId) {
        return stockOutReturnMapper.selectStockOutReturnByReturnId(returnId);
    }

    /**
     * 查询出库单退货
     */
    @Override
    public StockOutReturn selectStockOutReturnByReturnNo(String returnNo){
        return stockOutReturnMapper.selectStockOutReturnByReturnNo(returnNo);
    }

    /**
     * 查询出库单退货列表
     *
     * @param stockOutReturn 出库单退货
     * @return 出库单退货
     */
    @Override
    public List<StockOutReturn> selectStockOutReturnList(StockOutReturn stockOutReturn) {
        return stockOutReturnMapper.selectStockOutReturnList(stockOutReturn);
    }

    /**
     * 新增出库单退货
     *
     * @param outReturn 出库单退货
     * @return 结果
     */
    @Override
    public int insertStockOutReturn(String username, StockOutReturn outReturn) {
        Date nowDate = DateUtils.getNowDate();
        String returnNo = OrderNoUtil.getOutOrderReturnNo(outReturn.getReturnType());
        //出库退货单
        outReturn.setReturnNo(returnNo);
        outReturn.setReturnStatus(OrderStatusEnum.CREATED.getValue());
        outReturn.setCreateBy(username);
        outReturn.setCreateTime(nowDate);
        //出库退货详情
        List<StockOutReturnDetail> detailList = outReturn.getDetailList();
        int i = 1;
        Iterator<StockOutReturnDetail> iterator = detailList.iterator();
        StockOutReturnDetail detail = null;
        while (iterator.hasNext()) {
            detail = iterator.next();
            if (detail.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                iterator.remove();
                continue;
            }
            detail.setReturnNo(returnNo);
            detail.setLineNo(i);
            detail.setCreateBy(username);
            detail.setCreateTime(nowDate);
            i++;
        }
        stockOutReturnDetailMapper.insertStockOutReturnDetailList(detailList);
        return stockOutReturnMapper.insertStockOutReturn(outReturn);
    }

    /**
     * 修改出库单退货
     *
     * @param stockOutReturn 出库单退货
     * @return 结果
     */
    @Override
    public int updateStockOutReturn(StockOutReturn stockOutReturn) {
        stockOutReturn.setUpdateTime(DateUtils.getNowDate());
        return stockOutReturnMapper.updateStockOutReturn(stockOutReturn);
    }

    /**
     * 批量删除出库单退货
     *
     * @param returnIds 需要删除的出库单退货主键
     * @return 结果
     */
    @Override
    public int deleteStockOutReturnByReturnIds(Long[] returnIds) {
        if(ArrayUtils.isNotEmpty(returnIds)){
            for(Long returnId : returnIds){
                stockOutReturnDetailMapper.deleteStockOutReturnDetailByReturnId(returnId);
            }
        }
        return stockOutReturnMapper.deleteStockOutReturnByReturnIds(returnIds);
    }

    /**
     * 删除出库单退货信息
     *
     * @param returnId 出库单退货主键
     * @return 结果
     */
    @Override
    public int deleteStockOutReturnByReturnId(Long returnId) {
        return stockOutReturnMapper.deleteStockOutReturnByReturnId(returnId);
    }

    /**
     * 扫码提交出库单退货-退货
     * 注意：物料标签已重构，不再存储数量信息
     * returnQuantity 在原有已退数量基础上累加
     */
    @Override
    @Transactional
    public AjaxResult submitOutReturn(String username, StockOutReturn stockOutReturn){
        Date nowDate = DateUtils.getNowDate();
        List<StockOutReturnDetail> detailList = stockOutReturn.getDetailList();
        if(CollectionUtils.isEmpty(detailList)){
            return AjaxResult.error("系统繁忙，请稍后再试！");
        }
        String warehouseCode = stockOutReturn.getWarehouseCode();
        StockRecord record = null;
        for(StockOutReturnDetail detail : detailList){
            BigDecimal thisReturnQuantity = detail.getReturnQuantity();
            //查询当前已退数量，累加本次退货数量
            StockOutReturnDetail existingDetail = stockOutReturnDetailMapper.selectStockOutReturnDetailByDetailId(detail.getDetailId());
            if(existingDetail != null && existingDetail.getReturnQuantity() != null){
                detail.setReturnQuantity(existingDetail.getReturnQuantity().add(thisReturnQuantity));
            }
            //修改退货单详情
            detail.setUpdateBy(username);
            detail.setUpdateTime(nowDate);
            stockOutReturnDetailMapper.updateStockOutReturnDetail(detail);
            //修改库存信息（退货是加回库存，所以用负数）
            // 优先使用明细行的仓库编码（生产出库单每行可能属于不同仓库）
            String detailWarehouseCode = (detail.getWarehouseCode() != null && !detail.getWarehouseCode().isEmpty())
                    ? detail.getWarehouseCode() : warehouseCode;
            stockInfoMapper.updateQuantity(detailWarehouseCode, detail.getLocationCode(), detail.getBatch(),
                    detail.getSupplierCode(), detail.getMatCode(), thisReturnQuantity.multiply(new BigDecimal("-1")));
            //新增库存操作信息
            record = new StockRecord();
            BeanUtils.copyBeanProp(record, detail);
            record.setQuantity(thisReturnQuantity);
            record.setOrderNo(stockOutReturn.getOrderNo());
            record.setRecordType(StockRecordTypeEnum.getStockOutReturnRecordType(stockOutReturn.getReturnType()));
            record.setCreateBy(username);
            record.setCreateTime(nowDate);
            stockRecordMapper.insertStockRecord(record);
        }
        //判断退货单是否全部退完
        List<StockOutReturnDetail> allDetails = stockOutReturnDetailMapper.selectStockOutReturnDetailListByReturnNo(stockOutReturn.getReturnNo());
        boolean allReturned = true;
        if(CollectionUtils.isNotEmpty(allDetails)){
            for(StockOutReturnDetail d : allDetails){
                BigDecimal qty = d.getQuantity() != null ? d.getQuantity() : BigDecimal.ZERO;
                BigDecimal rtnQty = d.getReturnQuantity() != null ? d.getReturnQuantity() : BigDecimal.ZERO;
                if(rtnQty.compareTo(qty) < 0){
                    allReturned = false;
                    break;
                }
            }
        }
        //修改退货单状态
        // 扫码端只传 returnNo 不传 returnId，需要先查询出完整的退货单信息
        StockOutReturn dbOutReturn = stockOutReturnMapper.selectStockOutReturnByReturnNo(stockOutReturn.getReturnNo());
        if(dbOutReturn != null){
            dbOutReturn.setReturnStatus(allReturned ? OrderStatusEnum.RETURNED.getValue() : OrderStatusEnum.PARTIAL_RETURNED.getValue());
            dbOutReturn.setUpdateBy(username);
            dbOutReturn.setUpdateTime(nowDate);
            stockOutReturnMapper.updateStockOutReturn(dbOutReturn);
        }
        return AjaxResult.success("提交成功");
    }

}
