# WMS仓库管理系统 - 项目结构与代码规范

> 本文档记录了项目所有模块的目录结构、内容和代码格式规范。每次操作前请先阅读本文档，遵循第一性原理，确保不引入新的bug。
> 每次操作前请阅读本文档，遵循第一性原理，确保代码质量和系统稳定性。 每次操作前先对涉及的文件进行细致的查看，并制定详细的计划后再执行。计划需要写为文档格式，每个操作结束之后对照文档查看是否有遗漏
---

## 一、项目概述

### 1.1 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 2.5.14 + MyBatis |
| 前端框架 | Vue 2.6.12 + Element UI 2.15.8 |
| 数据库 | MySQL 5.6+ |
| 缓存 | Redis |
| 安全 | Spring Security + JWT |
| 构建工具 | Maven (后端) / npm (前端) |
| JDK版本 | Java 8 |

### 1.2 项目版本

- 系统版本：3.8.3
- 项目名称：wms-platform (若依管理系统)

---

## 二、目录结构总览

```
wms-platform/
├── database/                 # 数据库脚本
├── md/                       # 项目文档
├── ruoyi-master/             # 后端代码 (Maven多模块)
│   ├── ruoyi-admin/          # 主入口模块 (Controller层)
│   ├── ruoyi-base/           # 基础数据模块
│   ├── ruoyi-stock/          # 库存管理模块
│   ├── ruoyi-common/         # 通用工具模块
│   ├── ruoyi-framework/      # 框架核心模块
│   ├── ruoyi-system/         # 系统管理模块
│   └── ruoyi-quartz/         # 定时任务模块
├── ruoyi-ui/                 # 前端代码 (Vue)
├── wms/                      # WMS相关文件
│   ├── temp/                 # 临时文件
│   └── wms_file/             # 业务文件
├── work/                     # 工作文档目录
└── logs/                     # 日志目录
```

---

## 三、后端模块详解

### 3.1 ruoyi-admin (主入口模块)

**职责**：应用入口，存放Controller层代码

**目录结构**：
```
ruoyi-admin/
├── src/main/java/com/ruoyi/web/
│   ├── controller/           # 控制器
│   │   ├── base/             # 基础数据控制器
│   │   ├── stock/            # 库存管理控制器
│   │   ├── system/           # 系统管理控制器
│   │   ├── monitor/          # 监控控制器
│   │   └── common/           # 公共控制器
│   ├── component/            # 组件配置
│   └── utils/                # 工具类
└── src/main/resources/
    └── application.yml       # 应用配置
```

**Controller代码格式**：
```java
package com.ruoyi.web.controller.{模块名};

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.{模块名}.domain.{实体类};
import com.ruoyi.{模块名}.service.I{实体类}Service;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * {功能名称}Controller
 *
 * @author ruoyi
 * @date {日期}
 */
@RestController
@RequestMapping("/{模块}/{子路径}")
public class {实体类}Controller extends BaseController {

    @Autowired
    private I{实体类}Service {实体类首字母小写}Service;

    /**
     * 查询{功能名称}列表
     */
    @PreAuthorize("@ss.hasPermi('{模块}:{子路径}:list')")
    @GetMapping("/list")
    public TableDataInfo list({实体类} {实体类首字母小写}) {
        startPage();
        List<{实体类}> list = {实体类首字母小写}Service.select{实体类}List({实体类首字母小写});
        return getDataTable(list);
    }

    /**
     * 获取{功能名称}详细信息
     */
    @PreAuthorize("@ss.hasPermi('{模块}:{子路径}:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("{主键名}") Long {主键名}) {
        return AjaxResult.success({实体类首字母小写}Service.select{实体类}By{主键名}({主键名}));
    }

    /**
     * 新增{功能名称}
     */
    @PreAuthorize("@ss.hasPermi('{模块}:{子路径}:add')")
    @Log(title = "{功能名称}", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody {实体类} {实体类首字母小写}) {
        {实体类首字母小写}.setCreateBy(getUsername());
        return toAjax({实体类首字母小写}Service.insert{实体类}({实体类首字母小写}));
    }

    /**
     * 修改{功能名称}
     */
    @PreAuthorize("@ss.hasPermi('{模块}:{子路径}:edit')")
    @Log(title = "{功能名称}", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody {实体类} {实体类首字母小写}) {
        {实体类首字母小写}.setUpdateBy(getUsername());
        return toAjax({实体类首字母小写}Service.update{实体类}({实体类首字母小写}));
    }

    /**
     * 删除{功能名称}
     */
    @PreAuthorize("@ss.hasPermi('{模块}:{子路径}:remove')")
    @Log(title = "{功能名称}", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax({实体类首字母小写}Service.delete{实体类}By{主键名}s(ids));
    }
}
```

---

### 3.2 ruoyi-base (基础数据模块)

**职责**：物料、仓库、车间、供应商等基础数据管理

**目录结构**：
```
ruoyi-base/
├── src/main/java/com/ruoyi/base/
│   ├── domain/               # 实体类
│   │   ├── BaseMat.java          # 物料主数据
│   │   ├── BaseMatClass.java     # 物料分类
│   │   ├── BaseMatGroup.java     # 物料组
│   │   ├── BaseMatBom.java       # 物料BOM
│   │   ├── BaseWarehouse.java    # 仓库
│   │   ├── BaseLocation.java     # 库位
│   │   ├── BaseWorkshop.java     # 车间
│   │   └── BaseSupplier.java     # 供应商
│   ├── mapper/               # MyBatis Mapper接口
│   └── service/              # 业务层
│       ├── I{实体类}Service.java      # 接口
│       └── impl/{实体类}ServiceImpl.java # 实现
└── src/main/resources/mapper/base/
    └── {实体类}Mapper.xml     # MyBatis映射文件
```

**Domain代码格式**：
```java
package com.ruoyi.base.domain;

import java.math.BigDecimal;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * {实体名称}对象 {表名}
 *
 * @author ruoyi
 * @date {日期}
 */
@Data
public class {实体类} extends BaseEntity {

    /** 主键 */
    private Long {主键名};

    /** 字段说明 */
    @Excel(name = "导出列名")
    private String {字段名};

    /** 关联字段(显示用) */
    private String {字段名}Name;
}
```

**Service接口格式**：
```java
package com.ruoyi.base.service;

import java.util.List;
import com.ruoyi.base.domain.{实体类};

/**
 * {功能名称}Service接口
 *
 * @author ruoyi
 * @date {日期}
 */
public interface I{实体类}Service {

    /**
     * 查询{功能名称}
     */
    public {实体类} select{实体类}By{主键名}(Long {主键名});

    /**
     * 查询{功能名称}列表
     */
    public List<{实体类}> select{实体类}List({实体类} {实体类首字母小写});

    /**
     * 新增{功能名称}
     */
    public int insert{实体类}({实体类} {实体类首字母小写});

    /**
     * 修改{功能名称}
     */
    public int update{实体类}({实体类} {实体类首字母小写});

    /**
     * 批量删除{功能名称}
     */
    public int delete{实体类}By{主键名}s(Long[] {主键名}s);

    /**
     * 删除{功能名称}信息
     */
    public int delete{实体类}By{主键名}(Long {主键名});
}
```

**Service实现类格式**：
```java
package com.ruoyi.base.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.base.mapper.{实体类}Mapper;
import com.ruoyi.base.domain.{实体类};
import com.ruoyi.base.service.I{实体类}Service;

/**
 * {功能名称}Service业务层处理
 *
 * @author ruoyi
 * @date {日期}
 */
@Service
public class {实体类}ServiceImpl implements I{实体类}Service {

    @Autowired
    private {实体类}Mapper {实体类首字母小写}Mapper;

    @Override
    public {实体类} select{实体类}By{主键名}(Long {主键名}) {
        return {实体类首字母小写}Mapper.select{实体类}By{主键名}({主键名});
    }

    @Override
    public List<{实体类}> select{实体类}List({实体类} {实体类首字母小写}) {
        return {实体类首字母小写}Mapper.select{实体类}List({实体类首字母小写});
    }

    @Override
    public int insert{实体类}({实体类} {实体类首字母小写}) {
        {实体类首字母小写}.setCreateTime(DateUtils.getNowDate());
        return {实体类首字母小写}Mapper.insert{实体类}({实体类首字母小写});
    }

    @Override
    public int update{实体类}({实体类} {实体类首字母小写}) {
        {实体类首字母小写}.setUpdateTime(DateUtils.getNowDate());
        return {实体类首字母小写}Mapper.update{实体类}({实体类首字母小写});
    }

    @Override
    public int delete{实体类}By{主键名}s(Long[] {主键名}s) {
        return {实体类首字母小写}Mapper.delete{实体类}By{主键名}s({主键名}s);
    }

    @Override
    public int delete{实体类}By{主键名}(Long {主键名}) {
        return {实体类首字母小写}Mapper.delete{实体类}By{主键名}({主键名});
    }
}
```

**Mapper XML格式**：
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.ruoyi.base.mapper.{实体类}Mapper">

    <resultMap type="{实体类}" id="{实体类}Result">
        <result property="{属性名}" column="{字段名}" />
        <!-- 其他字段映射 -->
    </resultMap>

    <sql id="select{实体类}Vo">
        select {字段列表} from {表名}
    </sql>

    <select id="select{实体类}List" parameterType="{实体类}" resultMap="{实体类}Result">
        <include refid="select{实体类}Vo"/>
        WHERE del_flag = 0
        <if test="{属性名} != null and {属性名} != ''">
            and {字段名} = #{属性名}
        </if>
    </select>

    <select id="select{实体类}By{主键名}" parameterType="Long" resultMap="{实体类}Result">
        <include refid="select{实体类}Vo"/>
        where del_flag = 0 AND {主键字段} = #{主键名}
    </select>

    <insert id="insert{实体类}" parameterType="{实体类}" useGeneratedKeys="true" keyProperty="{主键名}">
        insert into {表名}
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="{属性名} != null">{字段名},</if>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <if test="{属性名} != null">#{属性名},</if>
        </trim>
    </insert>

    <update id="update{实体类}" parameterType="{实体类}">
        update {表名}
        <trim prefix="SET" suffixOverrides=",">
            <if test="{属性名} != null">{字段名} = #{属性名},</if>
        </trim>
        where {主键字段} = #{主键名}
    </update>

    <update id="delete{实体类}By{主键名}" parameterType="Long">
        update {表名} set del_flag = 1 where {主键字段} = #{主键名}
    </update>

    <update id="delete{实体类}By{主键名}s" parameterType="String">
        update {表名} set del_flag = 1 where {主键字段} in
        <foreach item="{主键名}" collection="array" open="(" separator="," close=")">
            #{主键名}
        </foreach>
    </update>
</mapper>
```

---

### 3.3 ruoyi-stock (库存管理模块)

**职责**：入库、出库、调拨、库存记录等业务逻辑

**目录结构**：
```
ruoyi-stock/
├── src/main/java/com/ruoyi/stock/
│   ├── domain/               # 实体类
│   │   ├── StockInfo.java        # 库存信息
│   │   ├── StockInOrder.java     # 入库单
│   │   ├── StockInDetail.java    # 入库明细
│   │   ├── StockOutOrder.java    # 出库单
│   │   ├── StockOutDetail.java   # 出库明细
│   │   ├── StockAllotOrder.java  # 调拨单
│   │   ├── StockAllotDetail.java # 调拨明细
│   │   ├── StockRecord.java      # 库存记录
│   │   ├── StockMatLabel.java    # 物料标签
│   │   ├── StockInReturn.java    # 入库退货
│   │   ├── StockOutReturn.java   # 出库退货
│   │   └── StockProdOrder.java   # 生产领料单
│   ├── mapper/               # Mapper接口
│   └── service/              # 业务层
└── src/main/resources/mapper/stock/
    └── *Mapper.xml           # MyBatis映射文件
```

**关键实体说明**：

| 实体 | 说明 | 主要字段 |
|------|------|----------|
| StockInfo | 库存信息 | matCode, warehouseCode, locationCode, quantity |
| StockInOrder | 入库单 | inOrderId, inOrderCode, inType, status |
| StockOutOrder | 出库单 | outOrderId, outOrderCode, outType, status |
| StockAllotOrder | 调拨单 | allotId, allotCode, warehouseCode, targetWarehouse |
| StockRecord | 库存记录 | recordId, recordType, matCode, quantity |

---

### 3.4 ruoyi-common (通用工具模块)

**职责**：公共组件、工具类、基础类

**目录结构**：
```
ruoyi-common/
├── src/main/java/com/ruoyi/common/
│   ├── annotation/           # 自定义注解
│   ├── bean/                 # 业务Bean
│   │   ├── PdfPrintData.java        # PDF打印数据
│   │   ├── MatLabelPdfData.java     # 标签打印数据
│   │   └── typeEnum/                # 枚举类型
│   ├── config/               # 配置类
│   ├── constant/             # 常量定义
│   ├── core/                 # 核心类
│   │   ├── controller/BaseController.java  # 控制器基类
│   │   ├── domain/            # 基础实体
│   │   │   ├── BaseEntity.java
│   │   │   ├── AjaxResult.java
│   │   │   └── entity/       # 系统实体
│   │   ├── page/             # 分页相关
│   │   └── redis/            # Redis工具
│   ├── enums/                # 枚举类
│   ├── exception/            # 异常类
│   ├── utils/                # 工具类
│   └── xss/                  # XSS防护
```

**BaseController 常用方法**：
```java
// 设置请求分页数据
protected void startPage()

// 响应请求分页数据
protected TableDataInfo getDataTable(List<?> list)

// 响应返回结果
protected AjaxResult toAjax(int rows)

// 获取当前用户名
protected String getUsername()

// 获取当前用户ID
protected Long getUserId()
```

**AjaxResult 常用方法**：
```java
AjaxResult.success()           // 成功
AjaxResult.success(data)       // 成功+数据
AjaxResult.success(msg, data)  // 成功+消息+数据
AjaxResult.error()             // 失败
AjaxResult.error(msg)          // 失败+消息
AjaxResult.error(code, msg)    // 失败+错误码+消息
```

---

### 3.5 ruoyi-framework (框架核心模块)

**职责**：安全配置、数据源、拦截器等基础设施

**目录结构**：
```
ruoyi-framework/
├── src/main/java/com/ruoyi/framework/
│   ├── aspectj/              # AOP切面
│   ├── config/               # 框架配置
│   │   ├── SecurityConfig.java    # Spring Security配置
│   │   ├── DruidConfig.java       # Druid数据源配置
│   │   ├── RedisConfig.java       # Redis配置
│   │   ├── MyBatisConfig.java     # MyBatis配置
│   │   └── ThreadPoolConfig.java  # 线程池配置
│   ├── datasource/           # 动态数据源
│   ├── interceptor/          # 拦截器
│   ├── manager/              # 管理器
│   ├── security/             # 安全相关
│   │   ├── filter/JwtAuthenticationTokenFilter.java  # JWT过滤器
│   │   └── handle/           # 安全处理器
│   └── web/service/          # 登录服务等
```

---

### 3.6 ruoyi-system (系统管理模块)

**职责**：用户、角色、菜单、部门、字典等系统功能

**目录结构**：
```
ruoyi-system/
├── src/main/java/com/ruoyi/system/
│   ├── domain/               # 系统实体
│   │   ├── SysUser.java          # 用户
│   │   ├── SysRole.java          # 角色
│   │   ├── SysMenu.java          # 菜单
│   │   ├── SysDept.java          # 部门
│   │   ├── SysPost.java          # 岗位
│   │   ├── SysDictType.java      # 字典类型
│   │   ├── SysDictData.java      # 字典数据
│   │   ├── SysConfig.java        # 系统配置
│   │   ├── SysNotice.java        # 通知公告
│   │   ├── SysOperLog.java       # 操作日志
│   │   └── SysLogininfor.java    # 登录日志
│   ├── mapper/               # Mapper接口
│   └── service/              # 业务层
└── src/main/resources/mapper/system/
    └── *Mapper.xml
```

---

### 3.7 ruoyi-quartz (定时任务模块)

**职责**：定时任务管理与执行

**目录结构**：
```
ruoyi-quartz/
├── src/main/java/com/ruoyi/quartz/
│   ├── config/               # Quartz配置
│   ├── controller/           # 任务管理控制器
│   ├── domain/               # 任务实体
│   ├── mapper/               # Mapper接口
│   ├── service/              # 业务层
│   ├── task/                 # 任务示例
│   └── util/                 # 工具类
```

---

## 四、前端模块详解

### 4.1 目录结构

```
ruoyi-ui/
├── public/                   # 静态资源
├── src/
│   ├── api/                  # API接口
│   │   ├── base/             # 基础数据API
│   │   ├── stock/            # 库存管理API
│   │   ├── system/           # 系统管理API
│   │   └── login.js          # 登录API
│   ├── assets/               # 资源文件
│   ├── components/           # 公共组件
│   ├── directive/            # 自定义指令
│   ├── layout/               # 布局组件
│   ├── plugins/              # 插件
│   ├── router/               # 路由配置
│   ├── store/                # Vuex状态管理
│   ├── utils/                # 工具类
│   ├── views/                # 页面视图
│   │   ├── base/             # 基础数据页面
│   │   ├── stock/            # 库存管理页面
│   │   ├── system/           # 系统管理页面
│   │   ├── monitor/          # 系统监控页面
│   │   ├── dashboard/        # 首页仪表盘
│   │   └── login.vue         # 登录页
│   ├── App.vue               # 根组件
│   ├── main.js               # 入口文件
│   ├── permission.js         # 权限控制
│   └── settings.js           # 全局设置
├── package.json              # 依赖配置
└── vue.config.js             # Vue配置
```

### 4.2 API接口格式

```javascript
// src/api/{模块}/{功能}.js
import request from '@/utils/request'

// 查询列表
export function list{实体类}(query) {
  return request({
    url: '/{模块}/{功能}/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function get{实体类}(id) {
  return request({
    url: '/{模块}/{功能}/' + id,
    method: 'get'
  })
}

// 新增
export function add{实体类}(data) {
  return request({
    url: '/{模块}/{功能}',
    method: 'post',
    data: data
  })
}

// 修改
export function update{实体类}(data) {
  return request({
    url: '/{模块}/{功能}',
    method: 'put',
    data: data
  })
}

// 删除
export function del{实体类}(id) {
  return request({
    url: '/{模块}/{功能}/' + id,
    method: 'delete'
  })
}
```

### 4.3 Vue组件格式

```vue
<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="{字段名}" prop="{字段名}">
        <el-input v-model="queryParams.{字段名}" placeholder="请输入{字段名}" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10">
      <el-col :span="1.5">
        <el-button type="primary" @click="handleAdd" v-hasPermi="['{模块}:{功能}:add']">新增</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="dataList">
      <el-table-column type="selection" width="55" />
      <el-table-column label="{列名}" prop="{字段名}" />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button @click="handleUpdate(scope.row)" v-hasPermi="['{模块}:{功能}:edit']">修改</el-button>
          <el-button @click="handleDelete(scope.row)" v-hasPermi="['{模块}:{功能}:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open">
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item label="{字段名}" prop="{字段名}">
          <el-input v-model="form.{字段名}" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list{实体类}, get{实体类}, add{实体类}, update{实体类}, del{实体类} } from "@/api/{模块}/{功能}";

export default {
  name: "{实体类}",
  data() {
    return {
      loading: true,
      total: 0,
      dataList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      form: {},
      rules: {
        {字段名}: [{ required: true, message: "{字段名}不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      list{实体类}(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加{功能名称}";
    },
    handleUpdate(row) {
      this.reset();
      get{实体类}(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改{功能名称}";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            update{实体类}(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            add{实体类}(this.form).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      this.$modal.confirm('是否确认删除?').then(() => {
        return del{实体类}(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      });
    },
    reset() {
      this.form = {};
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    }
  }
};
</script>
```

---

## 五、数据库设计规范

### 5.1 表命名规范

| 前缀 | 说明 | 示例 |
|------|------|------|
| base_ | 基础数据表 | base_mat, base_warehouse |
| stock_ | 库存管理表 | stock_in_order, stock_out_order |
| sys_ | 系统管理表 | sys_user, sys_role |

### 5.2 通用字段

每个表都包含以下通用字段：

```sql
create_by      varchar(64)   创建者
create_time    datetime      创建时间
update_by      varchar(64)   更新者
update_time    datetime      更新时间
remark         varchar(500)  备注
```

### 5.3 主要数据表

| 表名 | 说明 |
|------|------|
| base_mat | 物料主数据 |
| base_mat_class | 物料分类 |
| base_mat_group | 物料组 |
| base_mat_bom | 物料BOM |
| base_warehouse | 仓库 |
| base_location | 库位 |
| base_workshop | 车间 |
| base_supplier | 供应商 |
| stock_info | 库存信息 |
| stock_in_order | 入库单 |
| stock_in_detail | 入库明细 |
| stock_out_order | 出库单 |
| stock_out_detail | 出库明细 |
| stock_allot_order | 调拨单 |
| stock_allot_detail | 调拨明细 |
| stock_record | 库存流水记录 |
| stock_mat_label | 物料标签 |

---

## 六、权限标识规范

### 6.1 格式

```
{模块}:{功能}:{操作}
```

### 6.2 操作类型

| 标识 | 说明 |
|------|------|
| list | 列表查询 |
| query | 详情查询 |
| add | 新增 |
| edit | 修改 |
| remove | 删除 |
| export | 导出 |
| import | 导入 |

### 6.3 示例

```
base:mat:list        物料列表
base:mat:query       物料详情
base:mat:add         新增物料
base:mat:edit        修改物料
base:mat:remove      删除物料
base:mat:export      导出物料
stock:in:list        入库单列表
stock:out:add        新增出库单
```

---

## 七、接口路径规范

### 7.1 RESTful风格

| 操作 | HTTP方法 | 路径 | 说明 |
|------|----------|------|------|
| 列表 | GET | /{模块}/{功能}/list | 分页查询 |
| 详情 | GET | /{模块}/{功能}/{id} | 单条查询 |
| 新增 | POST | /{模块}/{功能} | 新增数据 |
| 修改 | PUT | /{模块}/{功能} | 修改数据 |
| 删除 | DELETE | /{模块}/{功能}/{ids} | 批量删除 |
| 导出 | POST | /{模块}/{功能}/export | 导出Excel |
| 导入 | POST | /{模块}/{功能}/importData | 导入Excel |

### 7.2 示例

```
GET  /base/mat/list          物料列表
GET  /base/mat/1             物料详情
POST /base/mat               新增物料
PUT  /base/mat               修改物料
DELETE /base/mat/1,2,3       删除物料
POST /base/mat/export        导出物料
```

---

## 八、开发注意事项

### 8.1 后端开发规范

1. **Controller层**：只负责接收请求和返回响应，不写业务逻辑
2. **Service层**：业务逻辑处理，事务控制
3. **Mapper层**：数据访问，SQL编写
4. **删除操作**：使用逻辑删除（update del_flag = 1），不使用物理删除
5. **权限控制**：使用 @PreAuthorize 注解控制访问权限
6. **日志记录**：使用 @Log 注解记录操作日志
7. **异常处理**：使用 ServiceException 抛出业务异常

### 8.2 前端开发规范

1. **组件命名**：使用 PascalCase 命名
2. **API统一管理**：所有接口放在 api 目录下
3. **字典数据**：使用 dicts 属性声明，页面使用 dict-tag 组件
4. **权限控制**：使用 v-hasPermi 指令控制按钮显示
5. **表单验证**：使用 rules 定义验证规则
6. **消息提示**：使用 this.$modal.msgSuccess/msgError

### 8.3 数据库规范

1. **表名**：小写字母，下划线分隔
2. **主键**：使用自增ID
3. **字段名**：小写字母，下划线分隔
4. **逻辑删除**：使用 del_flag 字段（0正常，1删除）
5. **时间字段**：使用 datetime 类型

---

## 九、常用配置

### 9.1 application.yml 主要配置

```yaml
server:
  port: 9991                    # 服务端口
  servlet:
    context-path: /wms-api      # 上下文路径

spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/wms
    username: root
    password: 123456
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456

mybatis:
  typeAliasesPackage: com.ruoyi.**.domain
  mapperLocations: classpath*:mapper/**/*Mapper.xml

token:
  expireTime: 1440              # Token有效期(分钟)

filedir: ./wms/wms_file/        # 文件存储路径
```

### 9.2 文件上传下载

```java
// 文件上传路径配置
filedir: ./wms/wms_file/
filetemp: ./wms/temp/

// PDF字体配置
pdf:
  pdf_font_library: ./simsun.ttc,1
```

---

## 十、模块依赖关系

```
ruoyi-admin (主入口)
├── ruoyi-base (基础数据)
├── ruoyi-stock (库存管理)
├── ruoyi-system (系统管理)
├── ruoyi-framework (框架核心)
│   └── ruoyi-common (通用工具)
└── ruoyi-quartz (定时任务)
```

---

---

## 十一、前端业务模块详情

### 11.1 业务页面模块

| 模块路径 | 说明 | 包含页面 |
|----------|------|----------|
| views/purchase/ | 采购业务 | inOrder(采购入库), inReturn(采购退货), matLabel(标签打印) |
| views/prod/ | 生产业务 | prodOrder(生产订单), outOrder(生产出库), outReturn(生产退库) |
| views/common/ | 普通业务 | outOrder(普通出库), outReturn(普通退库) |
| views/allocation/ | 调拨业务 | allot(调拨管理) |
| views/check/ | 盘点业务 | inOrder(盘点入库) |
| views/stats/ | 统计报表 | stockIn(入库统计), stockOut(出库统计), stockRecord(流水统计) |

### 11.2 公共选择组件

```
views/components/
├── select-mat/              # 物料选择弹窗
├── select-mat-label/        # 物料标签选择
├── select-mat-bom/          # BOM选择
├── select-supplier/         # 供应商选择
├── select-warehouse/        # 仓库选择
├── select-warehouse-keeper/ # 保管员选择
├── select-workshop/         # 车间选择
├── select-in-order/         # 入库单选择
├── select-out-order/        # 出库单选择
├── select-prod-order/       # 生产订单选择
├── select-user-class/       # 用户分类选择
└── bom-detail-list/         # BOM明细列表
```

---

## 十二、PDF模板配置

### 12.1 PDF模板目录

```
ruoyi-admin/src/main/resources/pdf/
├── label.pdf                # 物料标签模板
├── warehouse.pdf            # 仓库标签模板
├── location.pdf             # 货位标签模板
├── purchase_in_order.pdf    # 采购入库单模板
├── purchase_in_return.pdf   # 采购退换单模板
├── production_out_order.pdf # 生产出库单模板
├── production_out_return.pdf# 生产退换单模板
├── common_out_order.pdf     # 普通出库单模板
├── common_out_return.pdf    # 普通退换单模板
└── allot_order.pdf          # 调拨单模板
```

### 12.2 PDF数据Bean

| Bean类 | 用途 |
|--------|------|
| PdfPrintData | PDF打印数据基类 |
| MatLabelPdfData | 物料标签打印数据 |
| InOrderPdfData | 入库单打印数据 |
| OutDetailPdfData | 出库明细打印数据 |
| AllotDetailPdfData | 调拨明细打印数据 |

---

## 十三、快速参考

### 14.1 新增功能开发流程

1. **数据库**：创建表（包含通用字段）
2. **Domain**：创建实体类（继承BaseEntity）
3. **Mapper**：创建Mapper接口 + XML映射文件
4. **Service**：创建Service接口 + 实现类
5. **Controller**：创建Controller（在ruoyi-admin模块）
6. **前端API**：创建api/*.js
7. **前端页面**：创建views/*.vue
8. **菜单配置**：通过系统管理添加菜单和权限

### 14.2 常用工具类

| 类名 | 用途 |
|------|------|
| StringUtils | 字符串处理 |
| DateUtils | 日期处理 |
| SecurityUtils | 安全工具（获取当前用户等） |
| ExcelUtil | Excel导入导出 |
| RedisCache | Redis缓存操作 |
| ServletUtils | Servlet工具 |

### 14.3 注解速查

| 注解 | 用途 |
|------|------|
| @PreAuthorize | 权限控制 |
| @Log | 操作日志记录 |
| @Excel | Excel导出字段 |
| @DataScope | 数据权限 |
| @DataSource | 动态数据源 |
| @RepeatSubmit | 防重复提交 |
| @RateLimiter | 限流 |

---

> 文档更新时间：2026-03-31
>
> 每次操作前请阅读本文档，遵循第一性原理，确保代码质量和系统稳定性。 每次操作前先对涉及的文件进行细致的查看，并制定详细的计划后再执行。计划需要写为文档格式，每个操作结束之后对照文档查看是否有遗漏