package com.ruoyi.stock.mapper;

import java.util.Date;
import java.util.List;

import com.ruoyi.stock.domain.StockMatLabel;
import org.apache.ibatis.annotations.Param;

/**
 * 物料标签Mapper接口
 *
 * @author summer
 * @date 2022-07-25
 */
public interface StockMatLabelMapper {
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
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 结果
     */
    public int updateStatus(@Param("labelId") Long labelId, @Param("status") String status,
                            @Param("updateBy") String updateBy, @Param("updateTime") Date updateTime);

    /**
     * 删除物料标签
     *
     * @param labelId 物料标签主键
     * @return 结果
     */
    public int deleteStockMatLabelByLabelId(Long labelId);

    /**
     * 批量删除物料标签
     *
     * @param labelIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockMatLabelByLabelIds(Long[] labelIds);
}