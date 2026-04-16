package com.ruoyi.stock.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.bean.typeEnum.AllotProgressEnum;
import com.ruoyi.common.bean.typeEnum.InOrderTypeEnum;
import com.ruoyi.common.bean.typeEnum.OrderStatusEnum;
import com.ruoyi.common.bean.typeEnum.OutOrderTypeEnum;
import com.ruoyi.common.bean.typeEnum.StockRecordTypeEnum;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.OrderNoUtil;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.stock.domain.*;
import com.ruoyi.stock.mapper.*;
import com.ruoyi.stock.service.IStockInOrderService;
import com.ruoyi.stock.service.IStockOutOrderService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.stock.service.IStockAllotOrderService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 调拨单Service业务层处理
 *
 * @author summer
 * @date 2022-08-05
 */
@Service
public class StockAllotOrderServiceImpl implements IStockAllotOrderService {
    @Autowired
    private StockAllotOrderMapper stockAllotOrderMapper;
    @Autowired
    private StockAllotDetailMapper stockAllotDetailMapper;
    @Autowired
    private StockInfoMapper stockInfoMapper;
    @Autowired
    private StockRecordMapper stockRecordMapper;
    @Autowired
    private StockOutOrderMapper stockOutOrderMapper;
    @Autowired
    private StockOutDetailMapper stockOutDetailMapper;
    @Autowired
    private IStockOutOrderService stockOutOrderService;
    @Autowired
    private IStockInOrderService stockInOrderService;

    /**
     * 查询调拨单
     *
     * @param allotId 调拨单主键
     * @return 调拨单
     */
    @Override
    public StockAllotOrder selectStockAllotOrderByAllotId(Long allotId) {
        return stockAllotOrderMapper.selectStockAllotOrderByAllotId(allotId);
    }

    /**
     * 查询调拨单
     */
    @Override
    public StockAllotOrder selectStockAllotOrderByAllotNo(String allotNo){
        return stockAllotOrderMapper.selectStockAllotOrderByAllotNo(allotNo);
    }

    /**
     * 查询调拨单列表
     *
     * @param stockAllotOrder 调拨单
     * @return 调拨单
     */
    @Override
    public List<StockAllotOrder> selectStockAllotOrderList(StockAllotOrder stockAllotOrder) {
        return stockAllotOrderMapper.selectStockAllotOrderList(stockAllotOrder);
    }

    /**
     * 新增调拨单
     *
     * @param stockAllotOrder 调拨单
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStockAllotOrder(String username, StockAllotOrder stockAllotOrder) {
        String allotNo = OrderNoUtil.generateUniqueKey(OrderNoUtil.ALLOT_PREFIX);
        Date nowDate = DateUtils.getNowDate();
        stockAllotOrder.setAllotNo(allotNo);
        stockAllotOrder.setAllotStatus(OrderStatusEnum.CREATED.getValue());
        stockAllotOrder.setAllotProgress(AllotProgressEnum.CREATED.getValue());
        stockAllotOrder.setCreateBy(username);
        stockAllotOrder.setCreateTime(nowDate);
        //调拨单详情
        List<StockAllotDetail> detailList = stockAllotOrder.getDetailList();
        if(CollectionUtils.isNotEmpty(detailList)){
            int i = 1;
            for(StockAllotDetail detail : detailList){
                detail.setAllotNo(allotNo);
                detail.setSrcWarehouseCode(stockAllotOrder.getSrcWarehouseCode());
                detail.setDestWarehouseCode(stockAllotOrder.getDestWarehouseCode());
                detail.setLineNo(i);
                detail.setCreateBy(username);
                detail.setCreateTime(nowDate);
                i++;
            }
            //批量新增调拨单详情
            stockAllotDetailMapper.insertStockAllotDetailList(detailList);
        }
        return stockAllotOrderMapper.insertStockAllotOrder(stockAllotOrder);
    }

    /**
     * 修改调拨单
     *
     * @param stockAllotOrder 调拨单
     * @return 结果
     */
    @Override
    public int updateStockAllotOrder(StockAllotOrder stockAllotOrder) {
        stockAllotOrder.setUpdateTime(DateUtils.getNowDate());
        return stockAllotOrderMapper.updateStockAllotOrder(stockAllotOrder);
    }

    /**
     * 批量删除调拨单
     *
     * @param allotIds 需要删除的调拨单主键
     * @return 结果
     */
    @Override
    public int deleteStockAllotOrderByAllotIds(Long[] allotIds) {
        return stockAllotOrderMapper.deleteStockAllotOrderByAllotIds(allotIds);
    }

    /**
     * 删除调拨单信息
     *
     * @param allotId 调拨单主键
     * @return 结果
     */
    @Override
    public int deleteStockAllotOrderByAllotId(Long allotId) {
        return stockAllotOrderMapper.deleteStockAllotOrderByAllotId(allotId);
    }

    /**
     * 扫码提交调拨单-拣货
     */
    @Override
    @Transactional
    public AjaxResult submitAllotPicking(String username, StockAllotOrder stockAllotOrder){
        List<StockAllotDetail> detailList = stockAllotOrder.getDetailList();
        if(CollectionUtils.isEmpty(detailList)){
            return AjaxResult.error("系统繁忙，请稍后再试！");
        }
        Date nowDate = DateUtils.getNowDate();
        String allotNo = stockAllotOrder.getAllotNo();
        String srcWarehouseCode = stockAllotOrder.getSrcWarehouseCode();

        // 库存不足拦截检查：在实际扣减库存前，先检查所有物料的源货位库存是否充足
        for(StockAllotDetail detail : detailList){
            String srcLocationCode = detail.getSrcLocationCode();
            BigDecimal allotQuantity = detail.getQuantity();
            StockInfo checkInfo = new StockInfo();
            checkInfo.setWarehouseCode(srcWarehouseCode);
            checkInfo.setLocationCode(srcLocationCode);
            checkInfo.setMatCode(detail.getMatCode());
            checkInfo.setBatch(detail.getBatch());
            checkInfo.setSupplierCode(detail.getSupplierCode());
            List<StockInfo> checkList = stockInfoMapper.selectStockInfoList(checkInfo);
            BigDecimal currentStock = BigDecimal.ZERO;
            if(CollectionUtils.isNotEmpty(checkList)){
                currentStock = checkList.get(0).getQuantity() != null ? checkList.get(0).getQuantity() : BigDecimal.ZERO;
            }
            if(currentStock.compareTo(allotQuantity) < 0){
                return AjaxResult.error("源货位库存不足，当前库存" + currentStock.stripTrailingZeros().toPlainString()
                    + "，调拨数量" + allotQuantity.stripTrailingZeros().toPlainString() + "，请调整调拨数量");
            }
        }

        StockRecord record = null;
        int i = 1;
        for(StockAllotDetail detail : detailList){
            String srcLocationCode = detail.getSrcLocationCode();
            //新增调拨详情信息
            detail.setAllotNo(allotNo);
            detail.setSrcWarehouseCode(srcWarehouseCode);
            detail.setDestWarehouseCode(stockAllotOrder.getDestWarehouseCode());
            detail.setLineNo(i);
            detail.setCreateBy(username);
            detail.setCreateTime(nowDate);
            stockAllotDetailMapper.insertStockAllotDetail(detail);
            //修改库存信息
            stockInfoMapper.updateQuantity(srcWarehouseCode, srcLocationCode, detail.getBatch(), detail.getSupplierCode(),
                detail.getMatCode(), detail.getQuantity());
            //新增库存操作信息
            record = new StockRecord();
            BeanUtils.copyBeanProp(record, detail);
            record.setWarehouseCode(srcWarehouseCode);
            record.setLocationCode(srcLocationCode);
            record.setRecordType(StockRecordTypeEnum.ALLOT_OUT.getValue());
            record.setOrderNo(allotNo);
            record.setCreateBy(username);
            record.setCreateTime(nowDate);
            stockRecordMapper.insertStockRecord(record);
            i++;
        }
        return AjaxResult.success("提交成功");
    }

    /**
     * 扫码提交调拨单-接收
     */
    @Override
    @Transactional
    public AjaxResult submitAllotReceive(String username, StockAllotOrder stockAllotOrder){
        List<StockAllotDetail> detailList = stockAllotOrder.getDetailList();
        if(CollectionUtils.isEmpty(detailList)){
            return AjaxResult.error("系统繁忙，请稍后再试！");
        }
        Date nowDate = DateUtils.getNowDate();
        StockInfo info = null;
        StockRecord record = null;
        for(StockAllotDetail detail : detailList){
            String destWarehouseCode = detail.getDestWarehouseCode();
            String destLocationCode = detail.getDestLocationCode();
            BigDecimal signQuantity = detail.getSignQuantity();
            //修改调拨单详情
            stockAllotDetailMapper.updateStockAllotReceive(detail.getDetailId(), destLocationCode, signQuantity, username, nowDate);
            //修改库存信息
            info = new StockInfo();
            info.setWarehouseCode(destWarehouseCode);
            info.setLocationCode(destLocationCode);
            info.setMatCode(detail.getMatCode());
            info.setBatch(detail.getBatch());
            info.setSupplierCode(detail.getSupplierCode());
            List<StockInfo> infoList = stockInfoMapper.selectStockInfoList(info);
            if(CollectionUtils.isNotEmpty(infoList)){
                info = infoList.get(0);
                info.setQuantity(signQuantity.add(info.getQuantity()));
                info.setUpdateBy(username);
                info.setUpdateTime(nowDate);
                stockInfoMapper.updateStockInfo(info);
            }else{
                BeanUtils.copyBeanProp(info, detail);
                info.setQuantity(signQuantity);
                info.setCreateBy(username);
                info.setCreateTime(nowDate);
                stockInfoMapper.insertStockInfo(info);
            }
            //新增存库操作信息
            record = new StockRecord();
            BeanUtils.copyBeanProp(record, detail);
            record.setWarehouseCode(destWarehouseCode);
            record.setLocationCode(destLocationCode);
            record.setRecordType(StockRecordTypeEnum.ALLOT_IN.getValue());
            record.setQuantity(signQuantity);
            record.setOrderNo(stockAllotOrder.getAllotNo());
            record.setCreateBy(username);
            record.setCreateTime(nowDate);
            stockRecordMapper.insertStockRecord(record);
        }
        return AjaxResult.success("提交成功");
    }

    /**
     * 确认调拨单并生成调拨出库单
     */
    @Override
    @Transactional
    public AjaxResult confirmAllotAndCreateOutOrder(String username, String allotNo) {
        // 查询调拨单
        StockAllotOrder allotOrder = stockAllotOrderMapper.selectStockAllotOrderByAllotNo(allotNo);
        if (allotOrder == null) {
            return AjaxResult.error("调拨单不存在");
        }

        // 检查状态，只有已创建状态才能确认
        if (!AllotProgressEnum.CREATED.getValue().equals(allotOrder.getAllotProgress())) {
            return AjaxResult.error("调拨单状态不正确，无法确认");
        }

        Date nowDate = DateUtils.getNowDate();

        // 创建调拨出库单
        StockOutOrder outOrder = new StockOutOrder();
        outOrder.setOrderType(OutOrderTypeEnum.ALLOT.getValue());
        outOrder.setWarehouseCode(allotOrder.getSrcWarehouseCode());
        outOrder.setAllotNo(allotNo);
        outOrder.setDestWarehouseCode(allotOrder.getDestWarehouseCode());
        outOrder.setOrderStatus(OrderStatusEnum.CREATED.getValue());
        outOrder.setCreateBy(username);
        outOrder.setCreateTime(nowDate);

        // 生成出库单号
        String outOrderNo = OrderNoUtil.generate(OrderNoUtil.OrderPrefix.OUT_ALLOT);
        outOrder.setOrderNo(outOrderNo);

        // 查询调拨单详情
        List<StockAllotDetail> allotDetails = stockAllotDetailMapper.selectStockAllotDetailListByAllotNo(allotNo);
        if (CollectionUtils.isEmpty(allotDetails)) {
            return AjaxResult.error("调拨单没有物料明细，无法确认");
        }

        // 校验数量
        for (StockAllotDetail allotDetail : allotDetails) {
            if (allotDetail.getQuantity() == null || allotDetail.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                return AjaxResult.error("物料[" + allotDetail.getMatName() + "]的调拨数量不能为空或为0");
            }
        }

        // 创建出库单详情
        List<StockOutDetail> outDetails = new ArrayList<>();
        int lineNo = 1;
        for (StockAllotDetail allotDetail : allotDetails) {
            StockOutDetail outDetail = new StockOutDetail();
            outDetail.setOrderNo(outOrderNo);
            outDetail.setLineNo(lineNo++);
            outDetail.setWarehouseCode(allotOrder.getSrcWarehouseCode());
            outDetail.setMatCode(allotDetail.getMatCode());
            outDetail.setMatName(allotDetail.getMatName());
            outDetail.setFdCode(allotDetail.getFdCode());
            outDetail.setFigNum(allotDetail.getFigNum());
            outDetail.setMatGroup(allotDetail.getMatGroup());
            outDetail.setMatClass(allotDetail.getMatClass());
            outDetail.setUnitCode(allotDetail.getUnitCode());
            outDetail.setBatch(allotDetail.getBatch());
            outDetail.setSupplierCode(allotDetail.getSupplierCode());
            outDetail.setSupplierName(allotDetail.getSupplierName());
            outDetail.setQuantity(allotDetail.getQuantity());
            outDetail.setReceivedQuantity(BigDecimal.ZERO);
            outDetail.setCreateBy(username);
            outDetail.setCreateTime(nowDate);
            outDetails.add(outDetail);
        }
        outOrder.setDetailList(outDetails);

        // 保存出库单
        stockOutOrderMapper.insertStockOutOrder(outOrder);
        stockOutDetailMapper.insertStockOutDetailList(outDetails);

        // 更新调拨单状态为已确认，并记录出库单号
        allotOrder.setAllotStatus(OrderStatusEnum.CONFIRMED.getValue());
        allotOrder.setAllotProgress(AllotProgressEnum.CONFIRMED.getValue());
        allotOrder.setUpdateBy(username);
        allotOrder.setUpdateTime(nowDate);
        allotOrder.setRemark("已生成调拨出库单：" + outOrderNo);
        stockAllotOrderMapper.updateStockAllotOrder(allotOrder);

        return AjaxResult.success("确认成功，已生成调拨出库单：" + outOrderNo, outOrder);
    }

}
