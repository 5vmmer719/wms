package com.ruoyi.base.mapper;

import java.util.List;

import com.ruoyi.base.domain.BaseEquipmentMaintain;

/**
 * 设备维护记录Mapper接口
 *
 * @author ruoyi
 * @date 2026-04-11
 */
public interface BaseEquipmentMaintainMapper {

    /**
     * 查询维护记录
     */
    public BaseEquipmentMaintain selectBaseEquipmentMaintainByMaintainId(Long maintainId);

    /**
     * 查询维护记录列表
     */
    public List<BaseEquipmentMaintain> selectBaseEquipmentMaintainList(BaseEquipmentMaintain baseEquipmentMaintain);

    /**
     * 查询指定前缀下的最大维护单号
     */
    public String selectMaxMaintainNoByPrefix(String prefix);

    /**
     * 新增维护记录
     */
    public int insertBaseEquipmentMaintain(BaseEquipmentMaintain baseEquipmentMaintain);

    /**
     * 修改维护记录
     */
    public int updateBaseEquipmentMaintain(BaseEquipmentMaintain baseEquipmentMaintain);

    /**
     * 删除维护记录
     */
    public int deleteBaseEquipmentMaintainByMaintainId(Long maintainId);

    /**
     * 批量删除维护记录
     */
    public int deleteBaseEquipmentMaintainByMaintainIds(Long[] maintainIds);

}

