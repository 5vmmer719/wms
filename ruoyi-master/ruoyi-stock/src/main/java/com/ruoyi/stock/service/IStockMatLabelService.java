package com.ruoyi.stock.service;

import java.util.List;

import com.ruoyi.stock.domain.StockMatLabel;

/**
 * 物料标签Service接口
 *
 * @author summer
 * @date 2022-07-25
 */
public interface IStockMatLabelService {
    /**
     * 查询物料标签
     *
     * @param labelId 物料标签主键
     * @return 物料标签
     */
    public StockMatLabel selectStockMatLabelByLabelId(Long labelId);

    /**
     * 根据标签编码查询物料标签
     *
     * @param labelCode 标签编码
     * @return 物料标签
     */
    public StockMatLabel selectStockMatLabelByLabelCode(String labelCode);

    /**
     * 查询物料标签列表
     *
     * @param stockMatLabel 物料标签
     * @return 物料标签集合
     */
    public List<StockMatLabel> selectStockMatLabelList(StockMatLabel stockMatLabel);

    /**
     * 查询物料标签列表（弹窗）
     *
     * @param stockMatLabel 物料标签
     * @return 物料标签集合
     */
    public List<StockMatLabel> selectStockMatLabelListDialog(StockMatLabel stockMatLabel);

    /**
     * 新增物料标签
     *
     * @param stockMatLabel 物料标签
     * @return 结果
     */
    public int insertStockMatLabel(StockMatLabel stockMatLabel);

    /**
     * 修改物料标签
     *
     * @param stockMatLabel 物料标签
     * @return 结果
     */
    public int updateStockMatLabel(StockMatLabel stockMatLabel);

    /**
     * 修改物料标签状态
     *
     * @param labelId 标签ID
     * @param status 状态
     * @return 结果
     */
    public int updateStatus(Long labelId, String status);

    /**
     * 批量删除物料标签
     *
     * @param labelIds 需要删除的物料标签主键集合
     * @return 结果
     */
    public int deleteStockMatLabelByLabelIds(Long[] labelIds);

    /**
     * 删除物料标签信息
     *
     * @param labelId 物料标签主键
     * @return 结果
     */
    public int deleteStockMatLabelByLabelId(Long labelId);
}