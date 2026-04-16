package com.ruoyi.stock.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.bean.request.InReturnRequestBody;
import com.ruoyi.common.bean.typeEnum.OrderStatusEnum;
import com.ruoyi.common.bean.typeEnum.StockRecordTypeEnum;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.OrderNoUtil;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.stock.domain.StockInReturnDetail;
import com.ruoyi.stock.domain.StockMatLabel;
import com.ruoyi.stock.domain.StockRecord;
import com.ruoyi.stock.mapper.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.stock.domain.StockInReturn;
import com.ruoyi.stock.service.IStockInReturnService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入库单退货Service业务层处理
 *
 * @author summer
 * @date 2022-07-25
 */
@Service
public class StockInReturnServiceImpl implements IStockInReturnService {
    @Autowired
    private StockInReturnMapper stockInReturnMapper;
    @Autowired
    private StockInReturnDetailMapper stockInReturnDetailMapper;
    @Autowired
    private StockMatLabelMapper stockMatLabelMapper;
    @Autowired
    private StockInfoMapper stockInfoMapper;
    @Autowired
    private StockRecordMapper stockRecordMapper;

    /**
     * 查询入库单退货数量
     */
    @Override
    public Map<String, Long> selectStockInReturnTotal(Date selectDate){
        return stockInReturnMapper.selectStockInReturnTotal(selectDate);
    }

    /**
     * 查询入库单退货
     *
     * @param returnId 入库单退货主键
     * @return 入库单退货
     */
    @Override
    public StockInReturn selectStockInReturnByReturnId(Long returnId) {
        return stockInReturnMapper.selectStockInReturnByReturnId(returnId);
    }

    /**
     * 查询入库单退货
     */
    @Override
    public StockInReturn selectStockInReturnByReturnNo(String returnNo){
        return stockInReturnMapper.selectStockInReturnByReturnNo(returnNo);
    }

    /**
     * 查询入库单退货列表
     *
     * @param stockInReturn 入库单退货
     * @return 入库单退货
     */
    @Override
    public List<StockInReturn> selectStockInReturnList(StockInReturn stockInReturn) {
        return stockInReturnMapper.selectStockInReturnList(stockInReturn);
    }

    /**
     * 新增入库单退货
     *
     * @param inReturn 入库单退货
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStockInReturn(String username, StockInReturn inReturn) {
        //入库退货单
        Date nowDate = DateUtils.getNowDate();
        String returnType = inReturn.getReturnType();
        String warehouseCode = inReturn.getWarehouseCode();
        String returnNo = OrderNoUtil.getInOrderReturnNo(returnType);
        inReturn.setReturnNo(returnNo);
        inReturn.setReturnStatus(OrderStatusEnum.CREATED.getValue());
        inReturn.setCreateBy(username);
        inReturn.setCreateTime(nowDate);
        //入库退货详情
        List<StockInReturnDetail> inReturnDetailList = inReturn.getDetailList();
        int i = 1;
        Iterator<StockInReturnDetail> iterator = inReturnDetailList.iterator();
        StockInReturnDetail detail = null;
        while (iterator.hasNext()) {
            detail = iterator.next();
            if (detail.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                iterator.remove();
                continue;
            }
            detail.setWarehouseCode(warehouseCode);
            detail.setReturnNo(returnNo);
            detail.setLineNo(i);
            detail.setCreateBy(username);
            detail.setCreateTime(nowDate);
            i++;
        }
        stockInReturnDetailMapper.insertStockInReturnDetailList(inReturnDetailList);
        return stockInReturnMapper.insertStockInReturn(inReturn);
    }

    /**
     * 修改入库单退货
     *
     * @param stockInReturn 入库单退货
     * @return 结果
     */
    @Override
    public int updateStockInReturn(StockInReturn stockInReturn) {
        stockInReturn.setUpdateTime(DateUtils.getNowDate());
        return stockInReturnMapper.updateStockInReturn(stockInReturn);
    }

    /**
     * 批量删除入库单退货
     *
     * @param returnIds 需要删除的入库单退货主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStockInReturnByReturnIds(Long[] returnIds) {
        if(ArrayUtils.isNotEmpty(returnIds)){
            for(Long returnId : returnIds){
                stockInReturnDetailMapper.deleteStockInReturnDetailByReturnId(returnId);
            }
        }
        return stockInReturnMapper.deleteStockInReturnByReturnIds(returnIds);
    }

    /**
     * 删除入库单退货信息
     *
     * @param returnId 入库单退货主键
     * @return 结果
     */
    @Override
    public int deleteStockInReturnByReturnId(Long returnId) {
        return stockInReturnMapper.deleteStockInReturnByReturnId(returnId);
    }

    /**
     * 扫码提交入库单退货-退货
     * 注意：物料标签已重构，不再存储数量信息，数量信息从入库退货详情获取
     */
    @Override
    @Transactional
    public AjaxResult submitInReturn(String username, InReturnRequestBody inReturnRequestBody){
        String returnNo = inReturnRequestBody.getReturnNo();
        Map<Long, BigDecimal> returnMap = inReturnRequestBody.getReturnMap();
        StockInReturn stockInReturn = stockInReturnMapper.selectStockInReturnByReturnNo(returnNo);
        if(stockInReturn == null || MapUtils.isEmpty(returnMap)){
            return AjaxResult.error("系统繁忙，请稍后再试！");
        }
        Date nowDate = DateUtils.getNowDate();
        StockMatLabel matLabel = null;
        StockRecord record = null;
        for(Map.Entry<Long, BigDecimal> entry : returnMap.entrySet()){
            Long labelId = entry.getKey();
            BigDecimal labelQuantity = entry.getValue();
            matLabel = stockMatLabelMapper.selectStockMatLabelByLabelId(labelId);
            if(matLabel == null){
                return AjaxResult.error("物料标签不存在！");
            }
            // 查询入库退货详情获取仓库、货位等信息
            StockInReturnDetail queryDetail = new StockInReturnDetail();
            queryDetail.setReturnNo(returnNo);
            queryDetail.setLabelId(labelId);
            List<StockInReturnDetail> detailList = stockInReturnDetailMapper.selectStockInReturnDetailList(queryDetail);
            if(CollectionUtils.isEmpty(detailList)){
                return AjaxResult.error("退货详情不存在！");
            }
            StockInReturnDetail detail = detailList.get(0);
            //修改退货详情
            stockInReturnDetailMapper.updateReturnQuantity(returnNo, labelId, username, nowDate, labelQuantity);
            //修改库存
            stockInfoMapper.updateQuantity(detail.getWarehouseCode(), detail.getLocationCode(), detail.getBatch(),
                    detail.getSupplierCode(), detail.getMatCode(), labelQuantity);
            //新增库存操作信息
            record = new StockRecord();
            record.setWarehouseCode(detail.getWarehouseCode());
            record.setLocationCode(detail.getLocationCode());
            record.setMatCode(detail.getMatCode());
            record.setMatName(detail.getMatName());
            record.setFdCode(detail.getFdCode());
            record.setFigNum(detail.getFigNum());
            record.setMatGroup(detail.getMatGroup());
            record.setMatClass(detail.getMatClass());
            record.setUnitCode(detail.getUnitCode());
            record.setBatch(detail.getBatch());
            record.setSupplierCode(detail.getSupplierCode());
            record.setSupplierName(detail.getSupplierName());
            record.setRecordType(StockRecordTypeEnum.getStockInReturnRecordType(stockInReturn.getReturnType()));
            record.setQuantity(labelQuantity);
            record.setOrderNo(stockInReturn.getReturnNo());
            record.setCreateBy(username);
            record.setCreateTime(nowDate);
            stockRecordMapper.insertStockRecord(record);
        }
        //判断退货单是否全部退完
        List<StockInReturnDetail> allDetails = stockInReturnDetailMapper.selectStockInReturnDetailListByReturnNo(returnNo);
        boolean allReturned = true;
        if(CollectionUtils.isNotEmpty(allDetails)){
            for(StockInReturnDetail d : allDetails){
                BigDecimal qty = d.getQuantity() != null ? d.getQuantity() : BigDecimal.ZERO;
                BigDecimal rtnQty = d.getReturnQuantity() != null ? d.getReturnQuantity() : BigDecimal.ZERO;
                if(rtnQty.compareTo(qty) < 0){
                    allReturned = false;
                    break;
                }
            }
        }
        //修改退货单状态
        stockInReturn.setReturnStatus(allReturned ? OrderStatusEnum.RETURNED.getValue() : OrderStatusEnum.PARTIAL_RETURNED.getValue());
        stockInReturn.setUpdateBy(username);
        stockInReturn.setUpdateTime(nowDate);
        stockInReturnMapper.updateStockInReturn(stockInReturn);
        return AjaxResult.success("提交成功");
    }

}
