package com.ruoyi.base.service;

import java.util.List;

import com.ruoyi.base.domain.BaseProcessRoute;
import com.ruoyi.base.domain.BaseProcessStep;

/**
 * 工艺路线Service接口
 *
 * @author summer
 * @date 2026-04-11
 */
public interface IBaseProcessRouteService {

    /**
     * 查询工艺路线
     *
     * @param routeId 工艺路线主键
     * @return 工艺路线
     */
    public BaseProcessRoute selectBaseProcessRouteByRouteId(Long routeId);

    /**
     * 根据编码查询工艺路线
     *
     * @param routeCode 工艺路线编码
     * @return 工艺路线
     */
    public BaseProcessRoute selectBaseProcessRouteByRouteCode(String routeCode);

    /**
     * 查询工艺路线详情（含工序和参数）
     *
     * @param routeId 工艺路线主键
     * @return 工艺路线（含工序列表，每个工序含参数列表）
     */
    public BaseProcessRoute selectBaseProcessRouteDetail(Long routeId);

    /**
     * 查询工艺路线列表
     *
     * @param baseProcessRoute 工艺路线
     * @return 工艺路线集合
     */
    public List<BaseProcessRoute> selectBaseProcessRouteList(BaseProcessRoute baseProcessRoute);

    /**
     * 新增工艺路线
     *
     * @param baseProcessRoute 工艺路线
     * @return 结果
     */
    public int insertBaseProcessRoute(BaseProcessRoute baseProcessRoute);

    /**
     * 修改工艺路线
     *
     * @param baseProcessRoute 工艺路线
     * @return 结果
     */
    public int updateBaseProcessRoute(BaseProcessRoute baseProcessRoute);

    /**
     * 批量删除工艺路线
     *
     * @param routeIds 需要删除的工艺路线主键集合
     * @return 结果
     */
    public int deleteBaseProcessRouteByRouteIds(Long[] routeIds);

    /**
     * 删除工艺路线信息
     *
     * @param routeId 工艺路线主键
     * @return 结果
     */
    public int deleteBaseProcessRouteByRouteId(Long routeId);
}

