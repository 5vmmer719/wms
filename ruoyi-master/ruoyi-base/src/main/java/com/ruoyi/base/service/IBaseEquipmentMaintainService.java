package com.ruoyi.base.service;

import java.util.List;

import com.ruoyi.base.domain.BaseEquipmentMaintain;

/**
 * 设备维护记录Service接口
 *
 * @author ruoyi
 * @date 2026-04-11
 */
public interface IBaseEquipmentMaintainService {

    /**
     * 查询维护记录
     */
    public BaseEquipmentMaintain selectBaseEquipmentMaintainByMaintainId(Long maintainId);

    /**
     * 查询维护记录列表
     */
    public List<BaseEquipmentMaintain> selectBaseEquipmentMaintainList(BaseEquipmentMaintain baseEquipmentMaintain);

    /**
     * 新增维护记录
     */
    public int insertBaseEquipmentMaintain(BaseEquipmentMaintain baseEquipmentMaintain);

    /**
     * 修改维护记录
     */
    public int updateBaseEquipmentMaintain(BaseEquipmentMaintain baseEquipmentMaintain);

    /**
     * 完成维护（更新设备状态+维护日期）
     */
    public int completeMaintain(Long maintainId);

    /**
     * 批量删除维护记录
     */
    public int deleteBaseEquipmentMaintainByMaintainIds(Long[] maintainIds);

    /**
     * 删除维护记录
     */
    public int deleteBaseEquipmentMaintainByMaintainId(Long maintainId);

}

