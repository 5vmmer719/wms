package com.ruoyi.base.service.impl;

import java.util.List;

import com.ruoyi.common.bean.typeEnum.DefectHandleStatusEnum;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.base.mapper.QualityDefectHandleMapper;
import com.ruoyi.base.domain.QualityDefectHandle;
import com.ruoyi.base.service.IQualityDefectHandleService;

/**
 * 不合格品处理Service业务层处理
 *
 * @author summer
 * @date 2026-04-11
 */
@Service
public class QualityDefectHandleServiceImpl implements IQualityDefectHandleService {

    @Autowired
    private QualityDefectHandleMapper qualityDefectHandleMapper;

    /**
     * 查询不合格品处理
     */
    @Override
    public QualityDefectHandle selectQualityDefectHandleByHandleId(Long handleId) {
        QualityDefectHandle handle = qualityDefectHandleMapper.selectQualityDefectHandleByHandleId(handleId);
        if (handle != null) {
            handle.setHandleStatusLabel(DefectHandleStatusEnum.getLabel(handle.getHandleStatus()));
        }
        return handle;
    }

    /**
     * 查询不合格品处理列表
     */
    @Override
    public List<QualityDefectHandle> selectQualityDefectHandleList(QualityDefectHandle qualityDefectHandle) {
        List<QualityDefectHandle> list = qualityDefectHandleMapper.selectQualityDefectHandleList(qualityDefectHandle);
        for (QualityDefectHandle h : list) {
            h.setHandleStatusLabel(DefectHandleStatusEnum.getLabel(h.getHandleStatus()));
        }
        return list;
    }

    /**
     * 新增不合格品处理
     */
    @Override
    public int insertQualityDefectHandle(QualityDefectHandle qualityDefectHandle) {
        qualityDefectHandle.setCreateTime(DateUtils.getNowDate());
        return qualityDefectHandleMapper.insertQualityDefectHandle(qualityDefectHandle);
    }

    /**
     * 修改不合格品处理
     */
    @Override
    public int updateQualityDefectHandle(QualityDefectHandle qualityDefectHandle) {
        qualityDefectHandle.setUpdateTime(DateUtils.getNowDate());
        return qualityDefectHandleMapper.updateQualityDefectHandle(qualityDefectHandle);
    }

    /**
     * 批量删除不合格品处理
     */
    @Override
    public int deleteQualityDefectHandleByHandleIds(Long[] handleIds) {
        return qualityDefectHandleMapper.deleteQualityDefectHandleByHandleIds(handleIds);
    }

    /**
     * 删除不合格品处理
     */
    @Override
    public int deleteQualityDefectHandleByHandleId(Long handleId) {
        return qualityDefectHandleMapper.deleteQualityDefectHandleByHandleId(handleId);
    }
}

