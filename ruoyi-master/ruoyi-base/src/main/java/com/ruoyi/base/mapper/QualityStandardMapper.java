package com.ruoyi.base.mapper;

import java.util.List;

import com.ruoyi.base.domain.QualityStandard;

/**
 * 检验标准Mapper接口
 *
 * @author summer
 * @date 2026-04-11
 */
public interface QualityStandardMapper {

    /**
     * 查询检验标准
     */
    public QualityStandard selectQualityStandardByStandardId(Long standardId);

    /**
     * 根据编码查询检验标准
     */
    public QualityStandard selectQualityStandardByStandardCode(String standardCode);

    /**
     * 查询检验标准列表
     */
    public List<QualityStandard> selectQualityStandardList(QualityStandard qualityStandard);

    /**
     * 查询指定前缀下的最大标准编码
     */
    public String selectMaxStandardCodeByPrefix(String prefix);

    /**
     * 新增检验标准
     */
    public int insertQualityStandard(QualityStandard qualityStandard);

    /**
     * 修改检验标准
     */
    public int updateQualityStandard(QualityStandard qualityStandard);

    /**
     * 删除检验标准
     */
    public int deleteQualityStandardByStandardId(Long standardId);

    /**
     * 批量删除检验标准
     */
    public int deleteQualityStandardByStandardIds(Long[] standardIds);
}

