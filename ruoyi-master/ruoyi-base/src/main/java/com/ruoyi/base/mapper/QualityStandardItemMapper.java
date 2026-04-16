package com.ruoyi.base.mapper;

import java.util.List;

import com.ruoyi.base.domain.QualityStandardItem;

/**
 * 检验项目Mapper接口
 *
 * @author summer
 * @date 2026-04-11
 */
public interface QualityStandardItemMapper {

    /**
     * 查询检验项目
     */
    public QualityStandardItem selectQualityStandardItemByItemId(Long itemId);

    /**
     * 根据检验标准编码查询检验项目列表
     */
    public List<QualityStandardItem> selectQualityStandardItemByStandardCode(String standardCode);

    /**
     * 查询检验项目列表
     */
    public List<QualityStandardItem> selectQualityStandardItemList(QualityStandardItem qualityStandardItem);

    /**
     * 新增检验项目
     */
    public int insertQualityStandardItem(QualityStandardItem qualityStandardItem);

    /**
     * 批量新增检验项目
     */
    public int insertQualityStandardItemList(List<QualityStandardItem> list);

    /**
     * 修改检验项目
     */
    public int updateQualityStandardItem(QualityStandardItem qualityStandardItem);

    /**
     * 删除检验项目
     */
    public int deleteQualityStandardItemByItemId(Long itemId);

    /**
     * 根据检验标准编码删除检验项目
     */
    public int deleteQualityStandardItemByStandardCode(String standardCode);
}

