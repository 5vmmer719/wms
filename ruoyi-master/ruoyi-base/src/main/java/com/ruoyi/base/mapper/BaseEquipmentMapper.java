package com.ruoyi.base.mapper;

import java.util.List;

import com.ruoyi.base.domain.BaseEquipment;

/**
 * 设备台账Mapper接口
 *
 * @author ruoyi
 * @date 2026-04-11
 */
public interface BaseEquipmentMapper {

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
     * 查询指定前缀下的最大设备编码
     */
    public String selectMaxEquipmentCodeByPrefix(String prefix);

    /**
     * 新增设备
     */
    public int insertBaseEquipment(BaseEquipment baseEquipment);

    /**
     * 修改设备
     */
    public int updateBaseEquipment(BaseEquipment baseEquipment);

    /**
     * 删除设备
     */
    public int deleteBaseEquipmentByEquipmentId(Long equipmentId);

    /**
     * 批量删除设备
     */
    public int deleteBaseEquipmentByEquipmentIds(Long[] equipmentIds);

    /**
     * 查询需要维护的设备列表（next_maintain_date <= 当前日期，且状态为正常，且设置了维护周期）
     */
    public List<BaseEquipment> selectEquipmentNeedMaintain();

}

