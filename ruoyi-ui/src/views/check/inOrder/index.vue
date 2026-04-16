<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单据号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入单据号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="检验状态" prop="checkStatus">
        <el-select v-model="queryParams.checkStatus" placeholder="请选择">
          <el-option
            v-for="item in checkStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="检验员" prop="checkBy">
        <el-input
          v-model="queryParams.checkBy"
          placeholder="请输入检验员"
          clearable
          @keyup.enter.native="handleQuery"
        />
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

    <el-table v-loading="loading" :data="inOrderList">
      <el-table-column label="单据号" align="center" prop="orderNo" />
      <el-table-column label="单据类型" align="center" prop="orderTypeLabel" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{$moment(scope.row.createTime).format('YYYY-MM-DD HH:mm')}}</span>
        </template>
      </el-table-column>
      <el-table-column label="检验状态" align="center" prop="checkStatusLabel" />
      <el-table-column label="检验员" align="center" prop="checkBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.checkStatus === 'un_checkout'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleCheck(scope.row)"
            v-hasPermi="['check:inOrder:check']"
          >检验</el-button>
          <el-button
            v-if="scope.row.checkStatus === 'checkout'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleCheck(scope.row)"
            v-hasPermi="['check:inOrder:query']"
          >详情</el-button>
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

    <!-- 入库单质检对话框 -->
    <el-dialog :title="'入库单质检'" :visible.sync="inOrderDetailOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="入库单号：">
              <span>{{form.orderNo}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库单类型：">
              <span>{{form.orderTypeLabel}}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="创建时间：">
              <span>{{$moment(form.createTime).format('YYYY-MM-DD HH:mm')}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人：">
              <span>{{form.createBy}}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-table :data="inOrderDetailList" style="width: 100%">
        <el-table-column label="行号" align="center" type="index" />
        <el-table-column label="物料编码" align="center" prop="matCode" width="100" />
        <el-table-column label="物料名称" align="center" prop="matName" width="120" />
        <el-table-column label="财务编码" align="center" prop="fdCode" width="100" />
        <el-table-column label="图号" align="center" prop="figNum" width="120" />
        <el-table-column label="数量" align="center" prop="quantity" width="80" />
        <el-table-column label="合格数量" align="center" prop="qualifiedQuantity" width="100">
          <template slot-scope="scope">
            <el-input-number v-if="form.checkStatus === 'un_checkout'" v-model="scope.row.qualifiedQuantity"
              style="width: 90px" size="small" controls-position="right" :min="0" />
            <span v-if="form.checkStatus === 'checkout'">{{scope.row.qualifiedQuantity}}</span>
          </template>
        </el-table-column>
        <el-table-column label="单价" align="center" prop="unitPrice" width="80" />
        <el-table-column label="单位" align="center" prop="unitCode" width="50">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode"/>
          </template>
        </el-table-column>
        <el-table-column label="批次" align="center" prop="batch" width="180" />
        <el-table-column label="供应商" align="center" prop="supplierName" width="180" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-if="form.checkStatus === 'un_checkout'" @click="confirmCheck">确 认</el-button>
        <el-button @click="cancelOrderDetail">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listInOrder, getInOrder, checkInOrder } from "@/api/stock/inOrder";

export default {
  name: "InOrder",
  dicts: ['base_mat_unit'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 入库单表格数据
      inOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        orderType: null,
        orderStatus: null,
        checkStatus: null,
        checkBy: null,
      },
      // 表单参数
      form: {},

      // 日期范围
      dateRange: [],

      //检验状态
      checkStatusOptions:[
        {value: 'un_checkout', label: '未检验'},
        {value: 'checking', label: '检验中'},
        {value: 'passed', label: '检验合格'},
        {value: 'failed', label: '检验不合格'},
        {value: 'checkout', label: '已检验'}
      ],

      //入库单详情
      inOrderDetailOpen: false,
      inOrderDetailList: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询入库单列表 */
    getList() {
      this.loading = true;
      listInOrder(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.inOrderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        orderId: null,
        orderNo: null,
        orderType: null,
        orderStatus: "0",
        checkStatus: "0",
        checkBy: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
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
    /** 详情按钮操作 */
    handleCheck(row) {
      this.reset();
      getInOrder(row.orderId).then(response => {
        this.form = response.data;
        this.inOrderDetailList = response.data.detailList;
        this.inOrderDetailOpen = true;
      });
    },
    cancelOrderDetail(){
      this.reset();
      this.inOrderDetailList = [];
      this.inOrderDetailOpen = false;
    },
    /** 确认质检结果 */
    confirmCheck(){
      let that = this;
      that.$modal.confirm('是否确认质检结果？').then(function() {
        checkInOrder(that.inOrderDetailList).then(response => {
          that.$modal.msgSuccess("质检成功");
          that.cancelOrderDetail();
          that.getList();
        });
      });
    },
  }
};
</script>
