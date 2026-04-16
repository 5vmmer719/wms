package com.ruoyi.base.service;

import java.util.List;

import com.ruoyi.base.domain.BaseEquipment;

/**
 * 设备台账Service接口
 *
 * @author ruoyi
 * @date 2026-04-11
 */
public interface IBaseEquipmentService {

    /**
     * 查询设备
     */
    public BaseEquipment selectBaseEquipmentByEquipmentId(Long equipmentId);

    /**
     * 根据编码查询设备
     */
    public BaseEquipment selectBaseEquipmentByEquipmentCode(String equipmentCode);

    /**
     * 查询设备列表
     */
    public List<BaseEquipment> selectBaseEquipmentList(BaseEquipment baseEquipment);

    /**
     * 新增设备
     */
    public int insertBaseEquipment(BaseEquipment baseEquipment);

    /**
     * 修改设备
     */
    public int updateBaseEquipment(BaseEquipment baseEquipment);

    /**
     * 批量删除设备
     */
    public int deleteBaseEquipmentByEquipmentIds(Long[] equipmentIds);

    /**
     * 删除设备
     */
    public int deleteBaseEquipmentByEquipmentId(Long equipmentId);

}

