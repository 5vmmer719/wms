package com.ruoyi.base.service.impl;

import java.util.List;

import com.ruoyi.base.domain.BaseMat;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.base.mapper.BaseMatBomMapper;
import com.ruoyi.base.domain.BaseMatBom;
import com.ruoyi.base.service.IBaseMatBomService;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;

/**
 * 物料BOMService业务层处理
 *
 * @author summer
 * @date 2022-07-23
 */
@Service
public class BaseMatBomServiceImpl implements IBaseMatBomService {
    private static final Logger log = LoggerFactory.getLogger(BaseMatBomServiceImpl.class);

    @Autowired
    private BaseMatBomMapper baseMatBomMapper;
    @Autowired
    protected Validator validator;

    /**
     * 查询物料BOM
     *
     * @param bomId 物料BOM主键
     * @return 物料BOM
     */
    @Override
    public BaseMatBom selectBaseMatBomByBomId(Long bomId) {
        return baseMatBomMapper.selectBaseMatBomByBomId(bomId);
    }

    /**
     * 查询物料BOM列表
     */
    @Override
    public List<BaseMatBom> selectBaseMatBomByFatherMatCode(String fatherMatCode){
        return baseMatBomMapper.selectBaseMatBomByFatherMatCode(fatherMatCode);
    }

    /**
     * 查询物料BOM列表
     *
     * @param baseMatBom 物料BOM
     * @return 物料BOM
     */
    @Override
    public List<BaseMatBom> selectBaseMatBomList(BaseMatBom baseMatBom) {
        return baseMatBomMapper.selectBaseMatBomList(baseMatBom);
    }

    /**
     * 新增物料BOM
     *
     * @param baseMatBom 物料BOM
     * @return 结果
     */
    @Override
    public int insertBaseMatBom(BaseMatBom baseMatBom) {
        String fatherMatCode = baseMatBom.getFatherMatCode();
        String childMatCode = baseMatBom.getChildMatCode();

        // 重复关系检查：检查该父子物料BOM关系是否已存在
        List<BaseMatBom> existList = baseMatBomMapper.selectBaseMatBomByFatherMatCode(fatherMatCode);
        if (existList != null) {
            for (BaseMatBom exist : existList) {
                if (childMatCode.equals(exist.getChildMatCode())) {
                    throw new ServiceException("该BOM关系已存在，请勿重复添加");
                }
            }
        }

        // 循环依赖检查：子物料不能直接或间接引用父物料
        if (fatherMatCode.equals(childMatCode)) {
            throw new ServiceException("子物料不能与父物料相同，存在循环依赖");
        }
        if (checkCircularDependency(childMatCode, fatherMatCode)) {
            throw new ServiceException("存在循环依赖关系，无法添加该BOM");
        }

        baseMatBom.setCreateTime(DateUtils.getNowDate());
        return baseMatBomMapper.insertBaseMatBom(baseMatBom);
    }

    /**
     * 循环依赖检查：检查从startMatCode出发，是否能通过BOM关系链到达targetMatCode
     *
     * @param startMatCode  起始物料编码（子物料）
     * @param targetMatCode 目标物料编码（父物料）
     * @return true表示存在循环依赖
     */
    private boolean checkCircularDependency(String startMatCode, String targetMatCode) {
        List<BaseMatBom> childBomList = baseMatBomMapper.selectBaseMatBomByFatherMatCode(startMatCode);
        if (childBomList == null || childBomList.isEmpty()) {
            return false;
        }
        for (BaseMatBom bom : childBomList) {
            if (targetMatCode.equals(bom.getChildMatCode())) {
                return true;
            }
            if (checkCircularDependency(bom.getChildMatCode(), targetMatCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 修改物料BOM
     *
     * @param baseMatBom 物料BOM
     * @return 结果
     */
    @Override
    public int updateBaseMatBom(BaseMatBom baseMatBom) {
        baseMatBom.setUpdateTime(DateUtils.getNowDate());
        return baseMatBomMapper.updateBaseMatBom(baseMatBom);
    }

    /**
     * 批量删除物料BOM
     *
     * @param bomIds 需要删除的物料BOM主键
     * @return 结果
     */
    @Override
    public int deleteBaseMatBomByBomIds(Long[] bomIds) {
        return baseMatBomMapper.deleteBaseMatBomByBomIds(bomIds);
    }

    /**
     * 批量删除物料BOM
     *
     * @param matCodes
     * @return 结果
     */
    @Override
    public int deleteBaseMatBomByMatCodes(String[] matCodes){
        return baseMatBomMapper.deleteBaseMatBomByMatCodes(matCodes);
    }

    /**
     * 删除物料BOM信息
     *
     * @param bomId 物料BOM主键
     * @return 结果
     */
    @Override
    public int deleteBaseMatBomByBomId(Long bomId) {
        return baseMatBomMapper.deleteBaseMatBomByBomId(bomId);
    }

    /**
     * 按父物料删除物料BOM信息
     *
     * @param fatherMatCode
     * @return 结果
     */
    @Override
    public int deleteBaseMatBomByFatherMatCode(String fatherMatCode) {
        return baseMatBomMapper.deleteBaseMatBomByFMatCode(fatherMatCode);
    }

    /**
     * 导入物料BOM
     *
     * @param matBomList         物料Bom列表
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    @Transactional
    public String importMatBom(List<BaseMatBom> matBomList, String operName){
        if (StringUtils.isNull(matBomList) || matBomList.size() == 0) {
            throw new ServiceException("导入物料Bom不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        //删除原信息
        BaseMatBom indexBom = matBomList.get(0);
        BeanValidators.validateWithException(validator, indexBom);
        baseMatBomMapper.deleteBaseMatBomByFMatCode(indexBom.getFatherMatCode());
        //添加新信息
        for (BaseMatBom matBom : matBomList) {
            try {
                BeanValidators.validateWithException(validator, matBom);
                matBom.setCreateBy(operName);
                this.insertBaseMatBom(matBom);
                successNum++;
                successMsg.append("<br/>" + successNum + "、物料 " + matBom.getChildMatCode() + " 导入成功");
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、物料 " + matBom.getChildMatCode() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

}
