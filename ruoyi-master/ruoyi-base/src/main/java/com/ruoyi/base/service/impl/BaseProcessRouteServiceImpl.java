package com.ruoyi.base.service.impl;

import java.util.List;

import com.ruoyi.base.domain.BaseMat;
import com.ruoyi.base.domain.BaseProcessParam;
import com.ruoyi.base.domain.BaseProcessStep;
import com.ruoyi.base.mapper.BaseProcessParamMapper;
import com.ruoyi.base.mapper.BaseProcessStepMapper;
import com.ruoyi.base.service.IBaseMatService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.base.mapper.BaseProcessRouteMapper;
import com.ruoyi.base.domain.BaseProcessRoute;
import com.ruoyi.base.service.IBaseProcessRouteService;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 工艺路线Service业务层处理
 *
 * @author summer
 * @date 2026-04-11
 */
@Service
public class BaseProcessRouteServiceImpl implements IBaseProcessRouteService {

    @Autowired
    private BaseProcessRouteMapper baseProcessRouteMapper;

    @Autowired
    private BaseProcessStepMapper baseProcessStepMapper;

    @Autowired
    private BaseProcessParamMapper baseProcessParamMapper;

    @Autowired
    private IBaseMatService baseMatService;

    /**
     * 查询工艺路线
     *
     * @param routeId 工艺路线主键
     * @return 工艺路线
     */
    @Override
    public BaseProcessRoute selectBaseProcessRouteByRouteId(Long routeId) {
        return baseProcessRouteMapper.selectBaseProcessRouteByRouteId(routeId);
    }

    /**
     * 根据编码查询工艺路线
     *
     * @param routeCode 工艺路线编码
     * @return 工艺路线
     */
    @Override
    public BaseProcessRoute selectBaseProcessRouteByRouteCode(String routeCode) {
        return baseProcessRouteMapper.selectBaseProcessRouteByRouteCode(routeCode);
    }

    /**
     * 查询工艺路线详情（含工序和参数）
     * 级联加载：工艺路线 → 工序列表 → 每个工序的参数列表
     *
     * @param routeId 工艺路线主键
     * @return 工艺路线（含工序列表，每个工序含参数列表）
     */
    @Override
    public BaseProcessRoute selectBaseProcessRouteDetail(Long routeId) {
        BaseProcessRoute route = baseProcessRouteMapper.selectBaseProcessRouteByRouteId(routeId);
        if (route == null) {
            return null;
        }
        // 关联物料名称
        if (StringUtils.isNotEmpty(route.getMatCode())) {
            BaseMat mat = baseMatService.selectBaseMatByMatCode(route.getMatCode());
            if (mat != null) {
                route.setMatName(mat.getMatName());
            }
        }
        // 级联加载工序
        List<BaseProcessStep> steps = baseProcessStepMapper.selectBaseProcessStepByRouteCode(route.getRouteCode());
        if (steps != null) {
            for (BaseProcessStep step : steps) {
                // 级联加载每个工序的参数
                List<BaseProcessParam> params = baseProcessParamMapper.selectBaseProcessParamByStepId(step.getStepId());
                step.setParamsList(params);
            }
        }
        route.setSteps(steps);
        return route;
    }

    /**
     * 查询工艺路线列表
     *
     * @param baseProcessRoute 工艺路线
     * @return 工艺路线
     */
    @Override
    public List<BaseProcessRoute> selectBaseProcessRouteList(BaseProcessRoute baseProcessRoute) {
        List<BaseProcessRoute> list = baseProcessRouteMapper.selectBaseProcessRouteList(baseProcessRoute);
        // 关联物料名称
        if (list != null) {
            for (BaseProcessRoute route : list) {
                if (StringUtils.isNotEmpty(route.getMatCode())) {
                    BaseMat mat = baseMatService.selectBaseMatByMatCode(route.getMatCode());
                    if (mat != null) {
                        route.setMatName(mat.getMatName());
                    }
                }
            }
        }
        return list;
    }

    /**
     * 新增工艺路线（自动生成编码）
     * 编码规则：GY + yyyyMMdd + 3位流水号，如 GY20260411001
     *
     * @param baseProcessRoute 工艺路线
     * @return 结果
     */
    @Override
    public int insertBaseProcessRoute(BaseProcessRoute baseProcessRoute) {
        // 自动生成编码
        if (StringUtils.isEmpty(baseProcessRoute.getRouteCode())) {
            baseProcessRoute.setRouteCode(generateRouteCode());
        } else {
            // 手动输入编码时校验唯一性
            BaseProcessRoute exist = baseProcessRouteMapper.selectBaseProcessRouteByRouteCode(baseProcessRoute.getRouteCode());
            if (exist != null) {
                throw new ServiceException("工艺路线编码'" + baseProcessRoute.getRouteCode() + "'已存在");
            }
        }
        baseProcessRoute.setCreateTime(DateUtils.getNowDate());
        return baseProcessRouteMapper.insertBaseProcessRoute(baseProcessRoute);
    }

    /**
     * 自动生成工艺路线编码
     * 规则：GY + yyyyMMdd + 3位流水号（001-999）
     *
     * @return 新编码
     */
    private String generateRouteCode() {
        String prefix = "GY" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        String maxCode = baseProcessRouteMapper.selectMaxRouteCodeByPrefix(prefix);
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

    /**
     * 修改工艺路线
     *
     * @param baseProcessRoute 工艺路线
     * @return 结果
     */
    @Override
    public int updateBaseProcessRoute(BaseProcessRoute baseProcessRoute) {
        baseProcessRoute.setUpdateTime(DateUtils.getNowDate());
        return baseProcessRouteMapper.updateBaseProcessRoute(baseProcessRoute);
    }

    /**
     * 批量删除工艺路线（级联删除工序和参数）
     *
     * @param routeIds 需要删除的工艺路线主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteBaseProcessRouteByRouteIds(Long[] routeIds) {
        // 级联删除：先删参数，再删工序，最后删路线
        for (Long routeId : routeIds) {
            BaseProcessRoute route = baseProcessRouteMapper.selectBaseProcessRouteByRouteId(routeId);
            if (route != null) {
                List<BaseProcessStep> steps = baseProcessStepMapper.selectBaseProcessStepByRouteCode(route.getRouteCode());
                if (steps != null) {
                    for (BaseProcessStep step : steps) {
                        baseProcessParamMapper.deleteBaseProcessParamByStepId(step.getStepId());
                    }
                }
                baseProcessStepMapper.deleteBaseProcessStepByRouteCode(route.getRouteCode());
            }
        }
        return baseProcessRouteMapper.deleteBaseProcessRouteByRouteIds(routeIds);
    }

    /**
     * 删除工艺路线信息
     *
     * @param routeId 工艺路线主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteBaseProcessRouteByRouteId(Long routeId) {
        return deleteBaseProcessRouteByRouteIds(new Long[]{routeId});
    }
}

