package com.ruoyi.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruoyi.base.domain.BaseEquipment;
import com.ruoyi.base.mapper.BaseEquipmentMapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.base.mapper.BaseEquipmentMaintainMapper;
import com.ruoyi.base.domain.BaseEquipmentMaintain;
import com.ruoyi.base.service.IBaseEquipmentMaintainService;

/**
 * 设备维护记录Service业务层处理
 *
 * @author ruoyi
 * @date 2026-04-11
 */
@Service
public class BaseEquipmentMaintainServiceImpl implements IBaseEquipmentMaintainService {

    @Autowired
    private BaseEquipmentMaintainMapper baseEquipmentMaintainMapper;

    @Autowired
    private BaseEquipmentMapper baseEquipmentMapper;

    /**
     * 查询维护记录
     */
    @Override
    public BaseEquipmentMaintain selectBaseEquipmentMaintainByMaintainId(Long maintainId) {
        return baseEquipmentMaintainMapper.selectBaseEquipmentMaintainByMaintainId(maintainId);
    }

    /**
     * 查询维护记录列表
     */
    @Override
    public List<BaseEquipmentMaintain> selectBaseEquipmentMaintainList(BaseEquipmentMaintain baseEquipmentMaintain) {
        return baseEquipmentMaintainMapper.selectBaseEquipmentMaintainList(baseEquipmentMaintain);
    }

    /**
     * 新增维护记录（自动生成维护单号，同时将设备状态改为"维护中"）
     * 编码规则：WH + yyyyMMdd + 3位流水号，如 WH20260411001
     */
    @Override
    @Transactional
    public int insertBaseEquipmentMaintain(BaseEquipmentMaintain baseEquipmentMaintain) {
        // 自动生成维护单号
        if (StringUtils.isEmpty(baseEquipmentMaintain.getMaintainNo())) {
            baseEquipmentMaintain.setMaintainNo(generateMaintainNo());
        }
        // 填充设备名称
        if (StringUtils.isNotEmpty(baseEquipmentMaintain.getEquipmentCode()) && StringUtils.isEmpty(baseEquipmentMaintain.getEquipmentName())) {
            BaseEquipment equipment = baseEquipmentMapper.selectBaseEquipmentByEquipmentCode(baseEquipmentMaintain.getEquipmentCode());
            if (equipment != null) {
                baseEquipmentMaintain.setEquipmentName(equipment.getEquipmentName());
            }
        }
        baseEquipmentMaintain.setCreateTime(DateUtils.getNowDate());
        baseEquipmentMaintain.setMaintainStatus("0"); // 进行中
        // 默认来源为手动
        if (StringUtils.isEmpty(baseEquipmentMaintain.getSource())) {
            baseEquipmentMaintain.setSource("manual");
        }

        // 将设备状态改为"维护中"
        if (StringUtils.isNotEmpty(baseEquipmentMaintain.getEquipmentCode())) {
            BaseEquipment equipment = baseEquipmentMapper.selectBaseEquipmentByEquipmentCode(baseEquipmentMaintain.getEquipmentCode());
            if (equipment != null) {
                equipment.setEquipmentStatus("1"); // 维护中
                equipment.setUpdateTime(DateUtils.getNowDate());
                baseEquipmentMapper.updateBaseEquipment(equipment);
            }
        }

        return baseEquipmentMaintainMapper.insertBaseEquipmentMaintain(baseEquipmentMaintain);
    }

    /**
     * 修改维护记录
     */
    @Override
    public int updateBaseEquipmentMaintain(BaseEquipmentMaintain baseEquipmentMaintain) {
        baseEquipmentMaintain.setUpdateTime(DateUtils.getNowDate());
        return baseEquipmentMaintainMapper.updateBaseEquipmentMaintain(baseEquipmentMaintain);
    }

    /**
     * 完成维护：更新维护记录状态为已完成，恢复设备状态为正常，
     * 更新设备维护日期，并根据维护周期自动计算下次维护日期
     */
    @Override
    @Transactional
    public int completeMaintain(Long maintainId) {
        BaseEquipmentMaintain maintain = baseEquipmentMaintainMapper.selectBaseEquipmentMaintainByMaintainId(maintainId);
        if (maintain == null) {
            return 0;
        }
        // 更新维护记录
        maintain.setMaintainStatus("1"); // 已完成
        maintain.setMaintainEndDate(new Date());
        maintain.setUpdateTime(DateUtils.getNowDate());
        baseEquipmentMaintainMapper.updateBaseEquipmentMaintain(maintain);

        // 恢复设备状态为正常，更新维护日期，自动计算下次维护日期
        if (StringUtils.isNotEmpty(maintain.getEquipmentCode())) {
            BaseEquipment equipment = baseEquipmentMapper.selectBaseEquipmentByEquipmentCode(maintain.getEquipmentCode());
            if (equipment != null) {
                equipment.setEquipmentStatus("0"); // 正常
                equipment.setLastMaintainDate(new Date()); // 本次完成日期作为上次维护日期
                // 根据维护周期自动计算下次维护日期
                if (equipment.getMaintainCycle() != null && equipment.getMaintainCycle() > 0) {
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(java.util.Calendar.DAY_OF_MONTH, equipment.getMaintainCycle());
                    equipment.setNextMaintainDate(cal.getTime());
                }
                equipment.setUpdateTime(DateUtils.getNowDate());
                baseEquipmentMapper.updateBaseEquipment(equipment);
            }
        }
        return 1;
    }

    /**
     * 批量删除维护记录
     */
    @Override
    public int deleteBaseEquipmentMaintainByMaintainIds(Long[] maintainIds) {
        return baseEquipmentMaintainMapper.deleteBaseEquipmentMaintainByMaintainIds(maintainIds);
    }

    /**
     * 删除维护记录
     */
    @Override
    public int deleteBaseEquipmentMaintainByMaintainId(Long maintainId) {
        return baseEquipmentMaintainMapper.deleteBaseEquipmentMaintainByMaintainId(maintainId);
    }

    /**
     * 自动生成维护单号
     */
    private String generateMaintainNo() {
        String prefix = "WH" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        String maxNo = baseEquipmentMaintainMapper.selectMaxMaintainNoByPrefix(prefix);
        int nextSeq = 1;
        if (StringUtils.isNotEmpty(maxNo) && maxNo.length() > prefix.length()) {
            try {
                nextSeq = Integer.parseInt(maxNo.substring(prefix.length())) + 1;
            } catch (NumberFormatException ignored) {
            }
        }
        return prefix + String.format("%03d", nextSeq);
    }
}

