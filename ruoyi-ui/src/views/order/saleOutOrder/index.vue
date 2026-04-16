<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="单据号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入单据号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="客户订单" prop="customerOrderNo">
        <el-input v-model="queryParams.customerOrderNo" placeholder="请输入客户订单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="单据状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择" clearable>
          <el-option v-for="item in orderStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="仓库" prop="warehouseCode">
        <el-select v-model="queryParams.warehouseCode" placeholder="请选择仓库" clearable>
          <el-option v-for="item in warehouseList" :key="item.warehouseCode" :label="item.warehouseName" :value="item.warehouseCode" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['stock:outOrder:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['stock:outOrder:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['stock:outOrder:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="outOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="单据号" align="center" prop="orderNo" width="180" />
      <el-table-column label="客户订单" align="center" prop="customerOrderNo" width="180">
        <template slot-scope="scope">
          <span v-if="scope.row.customerOrderNo" style="color: #409EFF;">{{ scope.row.customerOrderNo }}</span>
          <span v-else style="color: #909399;">-</span>
        </template>
      </el-table-column>
      <el-table-column label="仓库" align="center" prop="warehouseName" />
      <el-table-column label="车间" align="center" prop="workshopName" />
      <el-table-column label="单据状态" align="center" prop="orderStatusLabel" width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ $moment(scope.row.createTime).format('YYYY-MM-DD HH:mm') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)" v-hasPermi="['stock:outOrder:edit']">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['stock:outOrder:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加出库单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="客户订单" prop="customerOrderNo">
              <el-select v-model="form.customerOrderNo" placeholder="选择客户订单（可选）" style="width: 100%" filterable clearable @change="onCustomerOrderChange">
                <el-option v-for="item in customerOrderOptions" :key="item.orderNo" :label="item.orderNo + ' - ' + item.customerName" :value="item.orderNo" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="仓库" prop="warehouseCode">
              <el-select v-model="form.warehouseCode" placeholder="请选择仓库">
                <el-option v-for="item in warehouseList" :key="item.warehouseCode" :label="item.warehouseName" :value="item.warehouseCode" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="用途/原因" prop="orderReason">
            <el-input placeholder="请输入用途/原因" v-model="form.orderReason" />
          </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="选择物料">
              <el-button size="small" type="success" icon="el-icon-search" @click="openSelectMatDialog">物料清单</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-table :data="outOrderDetailList">
        <el-table-column label="行号" align="center" type="index" />
        <el-table-column label="物料编码" align="center" prop="matCode" />
        <el-table-column label="物料名称" align="center" prop="matName" />
        <el-table-column label="图号" align="center" prop="figNum" />
        <el-table-column label="数量" align="center" prop="quantity">
          <template slot-scope="scope">
            <el-input-number style="width: 100px" size="small" v-model="scope.row.quantity" controls-position="right" :min="1" />
          </template>
        </el-table-column>
        <el-table-column label="单位" align="center" prop="unitCode">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode" />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="80">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-scissors" @click="handleRemove(scope.$index)">去除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">创 建</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 出库单详情对话框 -->
    <el-dialog class="detail-dialog" title="出库单详情" :visible.sync="outOrderDetailOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="120px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="出库单号："><span>{{ form.orderNo }}</span></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="仓库："><span>{{ form.warehouseName }}</span></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="领料人："><span>{{ form.createBy }}</span></el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="领料时间："><span>{{ $moment(form.createTime).format('YYYY-MM-DD HH:mm') }}</span></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="车间："><span>{{ form.workshopName }}</span></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户订单：">
              <span v-if="form.customerOrderNo" style="color: #409EFF;">{{ form.customerOrderNo }}</span>
              <span v-else>-</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="用途/原因："><span>{{ form.orderReason }}</span></el-form-item>
        </el-row>
      </el-form>
      <el-table class="detail-table" :data="outOrderDetailList">
        <el-table-column label="行号" align="center" prop="lineNo" />
        <el-table-column label="物料编码" align="center" prop="matCode" />
        <el-table-column label="物料名称" align="center" prop="matName" />
        <el-table-column label="图号" align="center" prop="figNum" />
        <el-table-column label="数量" align="center" prop="quantity" />
        <el-table-column label="已领" align="center" prop="receivedQuantity" />
        <el-table-column label="单位" align="center" prop="unitCode">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode" />
          </template>
        </el-table-column>
        <el-table-column label="推荐货位" align="center" prop="locationCode" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmPrintOutOrder">打 印</el-button>
        <el-button @click="cancelOrderDetail">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="选择物料" :visible.sync="selectMatOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <selectMat :selectType="'multiple'" @confirmSelectArr="confirmSelectMatArr" @confirmSelect="confirmSelectMat"></selectMat>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelSelectMat">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOutOrder, getOutOrder, delOutOrder, addOutOrder, printOutOrder } from "@/api/stock/outOrder";
import { listAllWorkshop } from "@/api/base/workshop";
import { listAllWarehouse } from "@/api/base/warehouse";
import { listCustomerOrder, getCustomerOrderDetail } from "@/api/order/customerOrder";
import selectMat from "../../components/select-mat/index";

export default {
  name: "SaleOutOrder",
  dicts: ['base_mat_unit'],
  components: { selectMat },
  data() {
    return {
      loading: true,
      ids: [],
      orderNos: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      outOrderList: [],
      customerOrderOptions: [],
      title: "",
      open: false,
      submitLoading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        orderType: 'common',
        customerOrderNo: null,
        warehouseCode: null,
        orderStatus: null,
      },
      form: {},
      rules: {
        warehouseCode: [{ required: true, message: "请选择仓库", trigger: "blur" }],
      },
      dateRange: [],
      orderStatusOptions: [
        { value: 'created', label: '已创建' },
        { value: 'printed', label: '已打印' },
        { value: 'received', label: '已领料' },
      ],
      warehouseList: [],
      workshopList: [],
      selectMatOpen: false,
      outOrderDetailOpen: false,
      outOrderDetailList: [],
    };
  },
  created() {
    this.getList();
    this.getWarehouseList();
    this.getWorkshopList();
    this.loadCustomerOrderOptions();
  },
  methods: {
    getList() {
      this.loading = true;
      listOutOrder(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.outOrderList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).finally(() => { this.loading = false; });
    },
    loadCustomerOrderOptions() {
      listCustomerOrder({ pageNum: 1, pageSize: 500 }).then(response => {
        this.customerOrderOptions = (response.rows || []).filter(o =>
          ['confirmed', 'producing', 'completed'].includes(o.orderStatus)
        );
      });
    },
    cancel() {
      this.open = false;
      this.reset();
      this.outOrderDetailList = [];
    },
    reset() {
      this.form = {
        orderId: null, orderNo: null, orderType: null, customerOrderNo: null,
        warehouseCode: null, workshopCode: null, orderReason: null,
        matCode: null, matName: null, quantity: null, orderStatus: "0",
        warehouseKeeper: null, delFlag: null, createBy: null, createTime: null,
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId);
      this.orderNos = selection.map(item => item.orderNo);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    onCustomerOrderChange(val) {
      if (!val) {
        this.outOrderDetailList = [];
        return;
      }
      const order = this.customerOrderOptions.find(o => o.orderNo === val);
      if (order) {
        // 根据客户订单详情自动填充出库物料明细
        getCustomerOrderDetail(order.orderId).then(response => {
          const detailList = response.data.detailList || [];
          this.outOrderDetailList = detailList.map(d => ({
            matCode: d.matCode,
            matName: d.matName,
            fdCode: d.fdCode || null,
            figNum: d.figNum || null,
            quantity: d.quantity ? Number(d.quantity) - Number(d.deliveredQty || 0) : 0,
            unitCode: d.unitCode || null,
          })).filter(d => d.quantity > 0);
        });
      }
    },
    handleAdd() {
      this.reset();
      this.outOrderDetailList = [];
      this.open = true;
      this.title = "新增销售出库单";
    },
    handleDetail(row) {
      this.reset();
      getOutOrder(row.orderId).then(response => {
        this.form = response.data;
        this.outOrderDetailList = response.data.detailList;
        this.outOrderDetailOpen = true;
      });
    },
    confirmPrintOutOrder() {
      printOutOrder(this.form.orderId).then(response => {
        const binaryData = [];
        binaryData.push(response);
        let pdfUrl = window.URL.createObjectURL(new Blob(binaryData, { type: "application/pdf" }));
        window.open(pdfUrl);
      });
    },
    cancelOrderDetail() {
      this.outOrderDetailOpen = false;
    },
    submitForm() {
      let that = this;
      if (!that.outOrderDetailList || that.outOrderDetailList.length === 0) {
        that.$modal.msgError("请选择物料");
        return;
      }
      let checkFlag = false;
      that.outOrderDetailList.forEach(item => { if (item.quantity > 0) checkFlag = true; });
      if (!checkFlag) {
        that.$modal.msgError("请至少选择一项领取");
        return;
      }
      that.$refs["form"].validate(valid => {
        if (valid) {
          that.$modal.confirm('是否确认创建销售出库单？').then(function () {
            that.form.detailList = that.outOrderDetailList;
            that.form.orderType = 'common';
            that.submitLoading = true;
            addOutOrder(that.form).then(response => {
              if (that.form.customerOrderNo) {
                that.$modal.msgSuccess("新增成功，已自动创建关联交付单");
              } else {
                that.$modal.msgSuccess("新增成功");
              }
              that.open = false;
              that.getList();
              that.reset();
              that.outOrderDetailList = [];
            }).finally(() => { that.submitLoading = false; });
          });
        }
      });
    },
    handleDelete(row) {
      const orderIds = row.orderId || this.ids;
      const delOrderNos = row.orderNo || this.orderNos;
      this.$modal.confirm('是否确认删除出库单号为 "' + delOrderNos + '" 的数据项？').then(function () {
        return delOutOrder(orderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('stock/outOrder/export', { ...this.queryParams }, `saleOutOrder_${new Date().getTime()}.xlsx`);
    },
    getWarehouseList() {
      listAllWarehouse().then(response => { this.warehouseList = response; });
    },
    getWorkshopList() {
      listAllWorkshop().then(response => { this.workshopList = response; });
    },
    openSelectMatDialog() {
      this.selectMatOpen = true;
    },
    confirmSelectMat(item) {
      let detail = { matCode: item.matCode, matName: item.matName, fdCode: item.fdCode, figNum: item.figNum, quantity: 0, unitCode: item.unitCode };
      this.outOrderDetailList.unshift(detail);
      this.selectMatOpen = false;
    },
    confirmSelectMatArr(arr) {
      arr && arr.length > 0 && arr.forEach(item => {
        let detail = { matCode: item.matCode, matName: item.matName, fdCode: item.fdCode, figNum: item.figNum, matGroup: item.matGroup, matClass: item.matClass, quantity: 0, unitCode: item.unitCode };
        this.outOrderDetailList.unshift(detail);
      });
      this.selectMatOpen = false;
    },
    cancelSelectMat() {
      this.selectMatOpen = false;
    },
    handleRemove(index) {
      this.outOrderDetailList.splice(index, 1);
    },
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.detail-dialog {
  .el-form-item { margin-bottom: 0px; }
  .detail-table { margin-top: 20px; }
}
</style>

