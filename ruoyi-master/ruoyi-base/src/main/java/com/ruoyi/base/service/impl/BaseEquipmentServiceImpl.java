package com.ruoyi.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.base.mapper.BaseEquipmentMapper;
import com.ruoyi.base.domain.BaseEquipment;
import com.ruoyi.base.service.IBaseEquipmentService;

/**
 * 设备台账Service业务层处理
 *
 * @author ruoyi
 * @date 2026-04-11
 */
@Service
public class BaseEquipmentServiceImpl implements IBaseEquipmentService {

    @Autowired
    private BaseEquipmentMapper baseEquipmentMapper;

    /**
     * 查询设备
     */
    @Override
    public BaseEquipment selectBaseEquipmentByEquipmentId(Long equipmentId) {
        return baseEquipmentMapper.selectBaseEquipmentByEquipmentId(equipmentId);
    }

    /**
     * 根据编码查询设备
     */
    @Override
    public BaseEquipment selectBaseEquipmentByEquipmentCode(String equipmentCode) {
        return baseEquipmentMapper.selectBaseEquipmentByEquipmentCode(equipmentCode);
    }

    /**
     * 查询设备列表
     */
    @Override
    public List<BaseEquipment> selectBaseEquipmentList(BaseEquipment baseEquipment) {
        return baseEquipmentMapper.selectBaseEquipmentList(baseEquipment);
    }

    /**
     * 新增设备（自动生成编码）
     * 编码规则：EQ + yyyyMMdd + 3位流水号，如 EQ20260411001
     */
    @Override
    public int insertBaseEquipment(BaseEquipment baseEquipment) {
        if (StringUtils.isEmpty(baseEquipment.getEquipmentCode())) {
            baseEquipment.setEquipmentCode(generateEquipmentCode());
        } else {
            BaseEquipment exist = baseEquipmentMapper.selectBaseEquipmentByEquipmentCode(baseEquipment.getEquipmentCode());
            if (exist != null) {
                throw new ServiceException("设备编码'" + baseEquipment.getEquipmentCode() + "'已存在");
            }
        }
        baseEquipment.setCreateTime(DateUtils.getNowDate());
        return baseEquipmentMapper.insertBaseEquipment(baseEquipment);
    }

    /**
     * 修改设备
     */
    @Override
    public int updateBaseEquipment(BaseEquipment baseEquipment) {
        baseEquipment.setUpdateTime(DateUtils.getNowDate());
        return baseEquipmentMapper.updateBaseEquipment(baseEquipment);
    }

    /**
     * 批量删除设备
     */
    @Override
    public int deleteBaseEquipmentByEquipmentIds(Long[] equipmentIds) {
        return baseEquipmentMapper.deleteBaseEquipmentByEquipmentIds(equipmentIds);
    }

    /**
     * 删除设备
     */
    @Override
    public int deleteBaseEquipmentByEquipmentId(Long equipmentId) {
        return baseEquipmentMapper.deleteBaseEquipmentByEquipmentId(equipmentId);
    }

    /**
     * 自动生成设备编码
     */
    private String generateEquipmentCode() {
        String prefix = "EQ" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        String maxCode = baseEquipmentMapper.selectMaxEquipmentCodeByPrefix(prefix);
        int nextSeq = 1;
        if (StringUtils.isNotEmpty(maxCode) && maxCode.length() > prefix.length()) {
            try {
                nextSeq = Integer.parseInt(maxCode.substring(prefix.length())) + 1;
            } catch (NumberFormatException ignored) {
            }
        }
        return prefix + String.format("%03d", nextSeq);
    }
}

