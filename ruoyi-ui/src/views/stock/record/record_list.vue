<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="仓库" prop="warehouseCode">
        <el-input
          v-model="queryParams.warehouseCode"
          placeholder="请输入仓库"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="货位" prop="locationCode">
        <el-input
          v-model="queryParams.locationCode"
          placeholder="请输入货位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="批次" prop="batch">
        <el-input
          v-model="queryParams.batch"
          placeholder="请输入批次"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单据号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入单据号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="供应商" prop="supplierName">
        <el-input
          v-model="queryParams.supplierName"
          placeholder="请输入供应商名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流水类型" prop="recordType">
        <el-select v-model="queryParams.recordType" placeholder="请选择流水类型" clearable>
          <el-option
            v-for="item in recordTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['stock:record:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" border>
      <el-table-column label="流水类型" fixed align="center" prop="recordTypeLabel" width="120" :show-overflow-tooltip="true" />
      <el-table-column label="仓库" align="center" prop="warehouseName" width="110" :show-overflow-tooltip="true" />
      <el-table-column label="货位" align="center" prop="locationCode" width="80" :show-overflow-tooltip="true" />
      <el-table-column label="物料编码" align="center" prop="matCode" width="120" :show-overflow-tooltip="true" />
      <el-table-column label="物料名称" align="center" prop="matName" min-width="140" :show-overflow-tooltip="true" />
      <el-table-column label="图号" align="center" prop="figNum" width="120" :show-overflow-tooltip="true" />
      <el-table-column label="数量" align="center" prop="quantity" width="80" />
      <el-table-column label="单位" align="center" prop="unitCode" width="70">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode"/>
        </template>
      </el-table-column>
      <el-table-column label="供应商" align="center" prop="supplierName" width="140" :show-overflow-tooltip="true" />
      <el-table-column label="批次" align="center" prop="batch" width="160" :show-overflow-tooltip="true" />
      <el-table-column label="单据号" align="center" prop="orderNo" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="车间" align="center" prop="workshopName" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="操作人" align="center" prop="createBy" width="80" />
      <el-table-column label="创建时间" fixed="right" align="center" prop="createTime" width="150">
        <template slot-scope="scope">
          <span>{{ $moment(scope.row.createTime).format('YYYY-MM-DD HH:mm') }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>
import { listRecord } from "@/api/stock/record";

export default {
  name: "Record",
  dicts: ['base_mat_unit'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 库存流水表格数据
      recordList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        warehouseCode: null,
        locationCode: null,
        labelId: null,
        recordType: null,
        matCode: null,
        matName: null,
        fdCode: null,
        figNum: null,
        matGroup: null,
        matClass: null,
        unitCode: null,
        batch: null,
        quantity: null,
        orderNo: null,
        lineNo: null,
        supplierCode: null,
        supplierName: null,
      },

      // 日期范围
      dateRange: [],

      // 流水类型选项
      recordTypeOptions: [
        { value: 'in_purchase', label: '原料入库' },
        { value: 'in_outsourcing', label: '外协入库' },
        { value: 'in_production', label: '车间入库' },
        { value: 'out_production', label: '生产领料' },
        { value: 'out_repair', label: '补领出库' },
        { value: 'out_common', label: '销售出库' },
        { value: 'in_purchase_return', label: '原料入库退货' },
        { value: 'out_production_return', label: '生产领料退货' },
        { value: 'out_repair_return', label: '补领出库退货' },
        { value: 'out_common_return', label: '销售出库退货' },
        { value: 'upper', label: '上架' },
        { value: 'lower', label: '下架' },
        { value: 'allot_in', label: '调拨入库' },
        { value: 'allot_out', label: '调拨出库' },
      ],
    };
  },
  created() {
    const matCode = this.$route.params && this.$route.params.matCode;
    this.setDefaultMatCode(matCode);
    this.getList();
  },
  methods: {
    setDefaultMatCode(matCode){
      this.queryParams.matCode = matCode;
    },
    /** 查询库存流水列表 */
    getList() {
      this.loading = true;
      listRecord(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.recordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('stock/record/export', {
        ...this.queryParams
      }, `record_${new Date().getTime()}.xlsx`)
    },
  }
};
</script>
