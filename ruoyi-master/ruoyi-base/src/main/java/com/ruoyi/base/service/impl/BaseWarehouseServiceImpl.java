package com.ruoyi.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.base.domain.BaseMatGroup;
import com.ruoyi.base.mapper.BaseMatGroupMapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.base.mapper.BaseWarehouseMapper;
import com.ruoyi.base.domain.BaseWarehouse;
import com.ruoyi.base.service.IBaseWarehouseService;

/**
 * 仓库Service业务层处理
 *
 * @author summer
 * @date 2022-07-24
 */
@Service
public class BaseWarehouseServiceImpl implements IBaseWarehouseService {
    @Autowired
    private BaseWarehouseMapper baseWarehouseMapper;

    @Autowired
    private BaseMatGroupMapper baseMatGroupMapper;

    /**
     * 查询仓库
     *
     * @param warehouseId 仓库主键
     * @return 仓库
     */
    @Override
    public BaseWarehouse selectBaseWarehouseByWarehouseId(Long warehouseId) {
        return baseWarehouseMapper.selectBaseWarehouseByWarehouseId(warehouseId);
    }

    /**
     * 查询仓库
     *
     * @param warehouseCode
     * @return 仓库
     */
    @Override
    public String selectBaseWarehouseNameByWarehouseCode(String warehouseCode) {
        return baseWarehouseMapper.selectBaseWarehouseNameByWarehouseCode(warehouseCode);
    }

    /**
     * 查询仓库列表
     *
     * @param baseWarehouse 仓库
     * @return 仓库
     */
    @Override
    public List<BaseWarehouse> selectBaseWarehouseList(BaseWarehouse baseWarehouse) {
        return baseWarehouseMapper.selectBaseWarehouseList(baseWarehouse);
    }

    /**
     * 新增仓库
     *
     * @param baseWarehouse 仓库
     * @return 结果
     */
    @Override
    public int insertBaseWarehouse(BaseWarehouse baseWarehouse) {
        baseWarehouse.setCreateTime(DateUtils.getNowDate());
        return baseWarehouseMapper.insertBaseWarehouse(baseWarehouse);
    }

    /**
     * 修改仓库
     *
     * @param baseWarehouse 仓库
     * @return 结果
     */
    @Override
    public int updateBaseWarehouse(BaseWarehouse baseWarehouse) {
        baseWarehouse.setUpdateTime(DateUtils.getNowDate());
        return baseWarehouseMapper.updateBaseWarehouse(baseWarehouse);
    }

    /**
     * 批量删除仓库
     *
     * @param warehouseIds 需要删除的仓库主键
     * @return 结果
     */
    @Override
    public int deleteBaseWarehouseByWarehouseIds(Long[] warehouseIds) {
        return baseWarehouseMapper.deleteBaseWarehouseByWarehouseIds(warehouseIds);
    }

    /**
     * 删除仓库信息
     *
     * @param warehouseId 仓库主键
     * @return 结果
     */
    @Override
    public int deleteBaseWarehouseByWarehouseId(Long warehouseId) {
        return baseWarehouseMapper.deleteBaseWarehouseByWarehouseId(warehouseId);
    }

    /**
     * 按仓库类型查询仓库列表
     *
     * @param warehouseType 仓库类型
     * @return 仓库集合
     */
    @Override
    public List<BaseWarehouse> selectWarehouseListByType(String warehouseType) {
        return baseWarehouseMapper.selectWarehouseListByType(warehouseType);
    }

    /**
     * 根据物料组编码获取推荐仓库类型
     *
     * @param matGroup 物料组编码
     * @return 仓库类型
     */
    @Override
    public String getWarehouseTypeByMatGroup(String matGroup) {
        if (StringUtils.isEmpty(matGroup)) {
            return "raw_material"; // 默认原材料仓库
        }
        BaseMatGroup group = baseMatGroupMapper.selectBaseMatGroupByGroupCode(matGroup);
        if (group != null && StringUtils.isNotEmpty(group.getDefaultWarehouseType())) {
            return group.getDefaultWarehouseType();
        }
        return "raw_material"; // 默认原材料仓库
    }

    /**
     * 根据物料组获取推荐仓库列表
     *
     * @param matGroup 物料组编码
     * @return 仓库集合
     */
    @Override
    public List<BaseWarehouse> getRecommendWarehouseList(String matGroup) {
        String warehouseType = getWarehouseTypeByMatGroup(matGroup);
        List<BaseWarehouse> warehouseList = selectWarehouseListByType(warehouseType);
        if (warehouseList == null) {
            warehouseList = new ArrayList<>();
        }
        // 如果没有匹配的仓库类型，返回所有仓库
        if (warehouseList.isEmpty()) {
            warehouseList = baseWarehouseMapper.selectBaseWarehouseList(new BaseWarehouse());
        }
        return warehouseList;
    }
}
