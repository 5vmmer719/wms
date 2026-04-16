package com.ruoyi.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruoyi.base.domain.BaseMat;
import com.ruoyi.base.domain.QualityStandardItem;
import com.ruoyi.base.mapper.QualityStandardItemMapper;
import com.ruoyi.base.service.IBaseMatService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.base.mapper.QualityStandardMapper;
import com.ruoyi.base.domain.QualityStandard;
import com.ruoyi.base.service.IQualityStandardService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 检验标准Service业务层处理
 *
 * @author summer
 * @date 2026-04-11
 */
@Service
public class QualityStandardServiceImpl implements IQualityStandardService {

    @Autowired
    private QualityStandardMapper qualityStandardMapper;

    @Autowired
    private QualityStandardItemMapper qualityStandardItemMapper;

    @Autowired
    private IBaseMatService baseMatService;

    /**
     * 查询检验标准
     */
    @Override
    public QualityStandard selectQualityStandardByStandardId(Long standardId) {
        return qualityStandardMapper.selectQualityStandardByStandardId(standardId);
    }

    /**
     * 根据编码查询检验标准
     */
    @Override
    public QualityStandard selectQualityStandardByStandardCode(String standardCode) {
        return qualityStandardMapper.selectQualityStandardByStandardCode(standardCode);
    }

    /**
     * 查询检验标准详情（含检验项目列表）
     */
    @Override
    public QualityStandard selectQualityStandardDetail(Long standardId) {
        QualityStandard standard = qualityStandardMapper.selectQualityStandardByStandardId(standardId);
        if (standard != null) {
            // 关联物料名称
            if (StringUtils.isNotEmpty(standard.getMatCode())) {
                BaseMat mat = baseMatService.selectBaseMatByMatCode(standard.getMatCode());
                if (mat != null) {
                    standard.setMatName(mat.getMatName());
                }
            }
            // 级联加载检验项目
            standard.setItems(qualityStandardItemMapper.selectQualityStandardItemByStandardCode(standard.getStandardCode()));
        }
        return standard;
    }

    /**
     * 查询检验标准列表
     */
    @Override
    public List<QualityStandard> selectQualityStandardList(QualityStandard qualityStandard) {
        List<QualityStandard> list = qualityStandardMapper.selectQualityStandardList(qualityStandard);
        if (CollectionUtils.isNotEmpty(list)) {
            for (QualityStandard s : list) {
                if (StringUtils.isNotEmpty(s.getMatCode())) {
                    BaseMat mat = baseMatService.selectBaseMatByMatCode(s.getMatCode());
                    if (mat != null) {
                        s.setMatName(mat.getMatName());
                    }
                }
            }
        }
        return list;
    }

    /**
     * 新增检验标准（含检验项目，级联保存）
     * 编码规则：QS + yyyyMMdd + 3位流水号
     */
    @Override
    @Transactional
    public int insertQualityStandard(QualityStandard qualityStandard) {
        // 自动生成编码
        if (StringUtils.isEmpty(qualityStandard.getStandardCode())) {
            qualityStandard.setStandardCode(generateStandardCode());
        } else {
            // 手动输入编码时校验唯一性
            QualityStandard exist = qualityStandardMapper.selectQualityStandardByStandardCode(qualityStandard.getStandardCode());
            if (exist != null) {
                throw new ServiceException("检验标准编码'" + qualityStandard.getStandardCode() + "'已存在");
            }
        }
        qualityStandard.setCreateTime(DateUtils.getNowDate());
        int rows = qualityStandardMapper.insertQualityStandard(qualityStandard);

        // 级联保存检验项目
        List<QualityStandardItem> items = qualityStandard.getItems();
        if (CollectionUtils.isNotEmpty(items)) {
            Date nowDate = DateUtils.getNowDate();
            for (int i = 0; i < items.size(); i++) {
                QualityStandardItem item = items.get(i);
                item.setStandardCode(qualityStandard.getStandardCode());
                item.setItemNo(i + 1);
                item.setCreateBy(qualityStandard.getCreateBy());
                item.setCreateTime(nowDate);
            }
            qualityStandardItemMapper.insertQualityStandardItemList(items);
        }
        return rows;
    }

    /**
     * 修改检验标准（含检验项目，先删后增）
     */
    @Override
    @Transactional
    public int updateQualityStandard(QualityStandard qualityStandard) {
        qualityStandard.setUpdateTime(DateUtils.getNowDate());
        int rows = qualityStandardMapper.updateQualityStandard(qualityStandard);

        // 先删后增检验项目
        qualityStandardItemMapper.deleteQualityStandardItemByStandardCode(qualityStandard.getStandardCode());
        List<QualityStandardItem> items = qualityStandard.getItems();
        if (CollectionUtils.isNotEmpty(items)) {
            Date nowDate = DateUtils.getNowDate();
            for (int i = 0; i < items.size(); i++) {
                QualityStandardItem item = items.get(i);
                item.setStandardCode(qualityStandard.getStandardCode());
                item.setItemNo(i + 1);
                item.setCreateBy(qualityStandard.getUpdateBy());
                item.setCreateTime(nowDate);
            }
            qualityStandardItemMapper.insertQualityStandardItemList(items);
        }
        return rows;
    }

    /**
     * 批量删除检验标准
     */
    @Override
    @Transactional
    public int deleteQualityStandardByStandardIds(Long[] standardIds) {
        // 级联删除检验项目
        for (Long standardId : standardIds) {
            QualityStandard standard = qualityStandardMapper.selectQualityStandardByStandardId(standardId);
            if (standard != null) {
                qualityStandardItemMapper.deleteQualityStandardItemByStandardCode(standard.getStandardCode());
            }
        }
        return qualityStandardMapper.deleteQualityStandardByStandardIds(standardIds);
    }

    /**
     * 删除检验标准
     */
    @Override
    @Transactional
    public int deleteQualityStandardByStandardId(Long standardId) {
        QualityStandard standard = qualityStandardMapper.selectQualityStandardByStandardId(standardId);
        if (standard != null) {
            qualityStandardItemMapper.deleteQualityStandardItemByStandardCode(standard.getStandardCode());
        }
        return qualityStandardMapper.deleteQualityStandardByStandardId(standardId);
    }

    /**
     * 自动生成检验标准编码
     * 规则：QS + yyyyMMdd + 3位流水号（001-999）
     */
    private String generateStandardCode() {
        String prefix = "QS" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        String maxCode = qualityStandardMapper.selectMaxStandardCodeByPrefix(prefix);
        int seq = 1;
        if (StringUtils.isNotEmpty(maxCode) && maxCode.length() > prefix.length()) {
            try {
                seq = Integer.parseInt(maxCode.substring(prefix.length())) + 1;
            } catch (NumberFormatException e) {
                seq = 1;
            }
        }
        return prefix + String.format("%03d", seq);
    }
}

