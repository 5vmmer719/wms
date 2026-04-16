package com.ruoyi.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.base.mapper.BaseWorkstationMapper;
import com.ruoyi.base.domain.BaseWorkstation;
import com.ruoyi.base.service.IBaseWorkstationService;

/**
 * 工位Service业务层处理
 *
 * @author ruoyi
 * @date 2026-04-11
 */
@Service
public class BaseWorkstationServiceImpl implements IBaseWorkstationService {

    @Autowired
    private BaseWorkstationMapper baseWorkstationMapper;

    /**
     * 查询工位
     */
    @Override
    public BaseWorkstation selectBaseWorkstationByStationId(Long stationId) {
        return baseWorkstationMapper.selectBaseWorkstationByStationId(stationId);
    }

    /**
     * 根据编码查询工位
     */
    @Override
    public BaseWorkstation selectBaseWorkstationByStationCode(String stationCode) {
        return baseWorkstationMapper.selectBaseWorkstationByStationCode(stationCode);
    }

    /**
     * 查询工位列表
     */
    @Override
    public List<BaseWorkstation> selectBaseWorkstationList(BaseWorkstation baseWorkstation) {
        return baseWorkstationMapper.selectBaseWorkstationList(baseWorkstation);
    }

    /**
     * 新增工位（自动生成编码）
     * 编码规则：WS + yyyyMMdd + 3位流水号，如 WS20260411001
     */
    @Override
    public int insertBaseWorkstation(BaseWorkstation baseWorkstation) {
        if (StringUtils.isEmpty(baseWorkstation.getStationCode())) {
            baseWorkstation.setStationCode(generateStationCode());
        } else {
            BaseWorkstation exist = baseWorkstationMapper.selectBaseWorkstationByStationCode(baseWorkstation.getStationCode());
            if (exist != null) {
                throw new ServiceException("工位编码'" + baseWorkstation.getStationCode() + "'已存在");
            }
        }
        baseWorkstation.setCreateTime(DateUtils.getNowDate());
        return baseWorkstationMapper.insertBaseWorkstation(baseWorkstation);
    }

    /**
     * 修改工位
     */
    @Override
    public int updateBaseWorkstation(BaseWorkstation baseWorkstation) {
        baseWorkstation.setUpdateTime(DateUtils.getNowDate());
        return baseWorkstationMapper.updateBaseWorkstation(baseWorkstation);
    }

    /**
     * 批量删除工位
     */
    @Override
    public int deleteBaseWorkstationByStationIds(Long[] stationIds) {
        return baseWorkstationMapper.deleteBaseWorkstationByStationIds(stationIds);
    }

    /**
     * 删除工位
     */
    @Override
    public int deleteBaseWorkstationByStationId(Long stationId) {
        return baseWorkstationMapper.deleteBaseWorkstationByStationId(stationId);
    }

    /**
     * 自动生成工位编码
     */
    private String generateStationCode() {
        String prefix = "WS" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        String maxCode = baseWorkstationMapper.selectMaxStationCodeByPrefix(prefix);
        int nextSeq = 1;
        if (StringUtils.isNotEmpty(maxCode) && maxCode.length() > prefix.length()) {
            try {
                nextSeq = Integer.parseInt(maxCode.substring(prefix.length())) + 1;
            } catch (NumberFormatException ignored) {
            }
        }
        return prefix + String.format("%03d", nextSeq);
    }
}

