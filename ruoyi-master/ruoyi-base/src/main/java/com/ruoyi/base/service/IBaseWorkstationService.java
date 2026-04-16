package com.ruoyi.base.service;

import java.util.List;

import com.ruoyi.base.domain.BaseWorkstation;

/**
 * 工位Service接口
 *
 * @author ruoyi
 * @date 2026-04-11
 */
public interface IBaseWorkstationService {

    /**
     * 查询工位
     */
    public BaseWorkstation selectBaseWorkstationByStationId(Long stationId);

    /**
     * 根据编码查询工位
     */
    public BaseWorkstation selectBaseWorkstationByStationCode(String stationCode);

    /**
     * 查询工位列表
     */
    public List<BaseWorkstation> selectBaseWorkstationList(BaseWorkstation baseWorkstation);

    /**
     * 新增工位
     */
    public int insertBaseWorkstation(BaseWorkstation baseWorkstation);

    /**
     * 修改工位
     */
    public int updateBaseWorkstation(BaseWorkstation baseWorkstation);

    /**
     * 批量删除工位
     */
    public int deleteBaseWorkstationByStationIds(Long[] stationIds);

    /**
     * 删除工位
     */
    public int deleteBaseWorkstationByStationId(Long stationId);

}

