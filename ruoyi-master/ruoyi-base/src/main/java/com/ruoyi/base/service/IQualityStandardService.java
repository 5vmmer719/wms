package com.ruoyi.base.service;

import java.util.List;

import com.ruoyi.base.domain.QualityStandard;

/**
 * 检验标准Service接口
 *
 * @author summer
 * @date 2026-04-11
 */
public interface IQualityStandardService {

    /**
     * 查询检验标准
     */
    public QualityStandard selectQualityStandardByStandardId(Long standardId);

    /**
     * 根据编码查询检验标准
     */
    public QualityStandard selectQualityStandardByStandardCode(String standardCode);

    /**
     * 查询检验标准详情（含检验项目列表）
     */
    public QualityStandard selectQualityStandardDetail(Long standardId);

    /**
     * 查询检验标准列表
     */
    public List<QualityStandard> selectQualityStandardList(QualityStandard qualityStandard);

    /**
     * 新增检验标准（含检验项目，级联保存）
     */
    public int insertQualityStandard(QualityStandard qualityStandard);

    /**
     * 修改检验标准（含检验项目，先删后增）
     */
    public int updateQualityStandard(QualityStandard qualityStandard);

    /**
     * 批量删除检验标准
     */
    public int deleteQualityStandardByStandardIds(Long[] standardIds);

    /**
     * 删除检验标准
     */
    public int deleteQualityStandardByStandardId(Long standardId);
}

