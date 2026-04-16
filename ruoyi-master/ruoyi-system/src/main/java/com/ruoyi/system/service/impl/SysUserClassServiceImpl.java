package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysUserClassMapper;
import com.ruoyi.system.domain.SysUserClass;
import com.ruoyi.system.service.ISysUserClassService;

/**
 * 用户管理的物料分类Service业务层处理
 *
 * @author summer
 * @date 2022-08-11
 */
@Service
public class SysUserClassServiceImpl implements ISysUserClassService {
    @Autowired
    private SysUserClassMapper sysUserClassMapper;

    /**
     * 查询用户管理的物料分类列表
     *
     * @param sysUserClass 用户管理的物料分类
     * @return 用户管理的物料分类
     */
    @Override
    public List<SysUserClass> selectSysUserClassList(SysUserClass sysUserClass) {
        return sysUserClassMapper.selectSysUserClassList(sysUserClass);
    }

    /**
     * 查询用户管理的物料分类列表
     */
    @Override
    public List<SysUserClass> selectSysUserClassListByUserName(String userName){
        return sysUserClassMapper.selectSysUserClassListByUserName(userName);
    }

    /**
     * 修改用户管理的物料分类
     */
    @Override
    public int changeActive(SysUserClass sysUserClass){
        String userName = sysUserClass.getUserName();
        String classCode = sysUserClass.getClassCode();
        List<SysUserClass> userClassList = sysUserClassMapper.selectSysUserClassListByUserAndClass(userName, classCode);
        if(userClassList != null && !userClassList.isEmpty()){
            // 存在记录则删除（可能有重复，先删除所有）
            sysUserClassMapper.deleteSysUserClassByUserName(userName, classCode);
            return 1;
        }else{
            return sysUserClassMapper.insertSysUserClass(sysUserClass);
        }
    }

}
