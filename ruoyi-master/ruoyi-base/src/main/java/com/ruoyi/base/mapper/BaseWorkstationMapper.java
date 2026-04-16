package com.ruoyi.base.mapper;

import java.util.List;

import com.ruoyi.base.domain.BaseWorkstation;

/**
 * 工位Mapper接口
 *
 * @author ruoyi
 * @date 2026-04-11
 */
public interface BaseWorkstationMapper {

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
     * 查询指定前缀下的最大工位编码
     */
    public String selectMaxStationCodeByPrefix(String prefix);

    /**
     * 新增工位
     */
    public int insertBaseWorkstation(BaseWorkstation baseWorkstation);

    /**
     * 修改工位
     */
    public int updateBaseWorkstation(BaseWorkstation baseWorkstation);

    /**
     * 删除工位
     */
    public int deleteBaseWorkstationByStationId(Long stationId);

    /**
     * 批量删除工位
     */
    public int deleteBaseWorkstationByStationIds(Long[] stationIds);

}

