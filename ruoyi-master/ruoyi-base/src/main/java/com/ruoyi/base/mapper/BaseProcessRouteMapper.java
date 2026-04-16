package com.ruoyi.base.mapper;

import java.util.List;

import com.ruoyi.base.domain.BaseProcessRoute;

/**
 * 工艺路线Mapper接口
 *
 * @author summer
 * @date 2026-04-11
 */
public interface BaseProcessRouteMapper {

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
     * 查询工艺路线列表
     *
     * @param baseProcessRoute 工艺路线
     * @return 工艺路线集合
     */
    public List<BaseProcessRoute> selectBaseProcessRouteList(BaseProcessRoute baseProcessRoute);

    /**
     * 查询指定前缀下的最大路线编码
     *
     * @param prefix 编码前缀（如 GY20260411）
     * @return 最大编码
     */
    public String selectMaxRouteCodeByPrefix(String prefix);

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
     * 删除工艺路线（逻辑删除）
     *
     * @param routeId 工艺路线主键
     * @return 结果
     */
    public int deleteBaseProcessRouteByRouteId(Long routeId);

    /**
     * 批量删除工艺路线（逻辑删除）
     *
     * @param routeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseProcessRouteByRouteIds(Long[] routeIds);
}

