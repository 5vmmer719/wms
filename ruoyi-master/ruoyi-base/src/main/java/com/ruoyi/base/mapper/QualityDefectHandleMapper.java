package com.ruoyi.base.mapper;

import java.util.List;

import com.ruoyi.base.domain.QualityDefectHandle;

/**
 * 不合格品处理Mapper接口
 *
 * @author summer
 * @date 2026-04-11
 */
public interface QualityDefectHandleMapper {

    /**
     * 查询不合格品处理
     */
    public QualityDefectHandle selectQualityDefectHandleByHandleId(Long handleId);

    /**
     * 查询不合格品处理列表
     */
    public List<QualityDefectHandle> selectQualityDefectHandleList(QualityDefectHandle qualityDefectHandle);

    /**
     * 新增不合格品处理
     */
    public int insertQualityDefectHandle(QualityDefectHandle qualityDefectHandle);

    /**
     * 修改不合格品处理
     */
    public int updateQualityDefectHandle(QualityDefectHandle qualityDefectHandle);

    /**
     * 删除不合格品处理
     */
    public int deleteQualityDefectHandleByHandleId(Long handleId);

    /**
     * 批量删除不合格品处理
     */
    public int deleteQualityDefectHandleByHandleIds(Long[] handleIds);

    /**
     * 根据任务编号查询不合格品处理
     */
    public List<QualityDefectHandle> selectQualityDefectHandleByTaskNo(String taskNo);
}

