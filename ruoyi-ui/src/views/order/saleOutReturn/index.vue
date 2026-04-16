<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="退货单号" prop="returnNo">
        <el-input v-model="queryParams.returnNo" placeholder="请输入退货单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="退货原因" prop="returnReason">
        <el-input v-model="queryParams.returnReason" placeholder="请输入退货原因" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="仓库" prop="warehouseCode">
        <el-select v-model="queryParams.warehouseCode" placeholder="请选择仓库" clearable>
          <el-option v-for="item in warehouseList" :key="item.warehouseCode" :label="item.warehouseName" :value="item.warehouseCode" />
        </el-select>
      </el-form-item>
      <el-form-item label="出库单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入出库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['stock:outReturn:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['stock:outReturn:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['stock:outReturn:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="outReturnList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="退货单号" align="center" prop="returnNo" />
      <el-table-column label="仓库" align="center" prop="warehouseName" />
      <el-table-column label="车间" align="center" prop="workshopName" />
      <el-table-column label="退货类型" align="center" prop="returnTypeLabel" />
      <el-table-column label="退货状态" align="center" prop="returnStatusLabel" />
      <el-table-column label="出库单号" align="center" prop="orderNo" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)" v-hasPermi="['stock:outReturn:query']">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['stock:outReturn:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加退货对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="出库单号" prop="orderNo">
              <el-input v-model="form.orderNo" readonly placeholder="请选择出库单">
                <el-button slot="append" icon="el-icon-search" @click="openSelectOutOrderDialog" />
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="车间" prop="workshopCode">
              <el-select v-model="form.workshopCode" disabled placeholder="请选择车间">
                <el-option v-for="item in workshopList" :key="item.workshopCode" :label="item.workshopName" :value="item.workshopCode" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="退货原因" prop="returnReason">
            <el-input v-model="form.returnReason" placeholder="请输入退货原因" />
          </el-form-item>
        </el-row>
      </el-form>
      <el-table v-loading="loading" :data="outReturnDetailList">
        <el-table-column label="物料编码" align="center" prop="matCode" width="120" />
        <el-table-column label="物料名称" align="center" prop="matName" width="180" />
        <el-table-column label="仓库" align="center" prop="warehouseName" width="80" />
        <el-table-column label="货位" align="center" prop="locationCode" width="80" />
        <el-table-column label="可退数量" align="center" prop="receivedQuantity" width="80" />
        <el-table-column label="需退货" align="center" prop="quantity" width="100">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.quantity" style="width: 90px" size="small" controls-position="right" :min="0" :max="scope.row.receivedQuantity" />
          </template>
        </el-table-column>
        <el-table-column label="单位" align="center" prop="unitCode" width="80">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode" />
          </template>
        </el-table-column>
        <el-table-column label="批次" align="center" prop="batch" width="150" />
        <el-table-column label="供应商" align="center" prop="supplierName" width="150" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="选择出库单" :visible.sync="selectOutOrderOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <selectOutOrder :outOrderType="outOrderType" :onlyReceived="true" @confirmSelect="confirmSelectOutOrder"></selectOutOrder>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelSelectOutOrder">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 退货详情 -->
    <el-dialog class="detail-dialog" title="销售退货单详情" :visible.sync="outOrderReturnDetailOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="120px">
        <el-row>
          <el-col :span="8"><el-form-item label="退货单号："><span>{{ form.returnNo }}</span></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="退货类型："><span>{{ form.returnTypeLabel }}</span></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="出库单号："><span>{{ form.orderNo }}</span></el-form-item></el-col>
        </el-row>
        <el-row>
          <el-col :span="8"><el-form-item label="车间："><span>{{ form.workshopName }}</span></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="创建人："><span>{{ form.createBy }}</span></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="创建时间："><span>{{ $moment(form.createTime).format('YYYY-MM-DD HH:mm') }}</span></el-form-item></el-col>
        </el-row>
        <el-row>
          <el-form-item label="退货原因："><span>{{ form.returnReason }}</span></el-form-item>
        </el-row>
      </el-form>
      <el-table class="detail-table" :data="outReturnDetailList">
        <el-table-column label="物料编码" align="center" prop="matCode" width="120" />
        <el-table-column label="物料名称" align="center" prop="matName" width="180" />
        <el-table-column label="仓库" align="center" prop="warehouseName" width="120" />
        <el-table-column label="需退货数" align="center" prop="quantity" width="80" />
        <el-table-column label="已退货数" align="center" prop="returnQuantity" width="80" />
        <el-table-column label="单位" align="center" prop="unitCode" width="80">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode" />
          </template>
        </el-table-column>
        <el-table-column label="批次" align="center" prop="batch" width="150" />
        <el-table-column label="所在货位" align="center" prop="locationCode" width="150" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" icon="el-icon-printer" @click="confirmPrintOutOrderReturn">打 印</el-button>
        <el-button @click="cancelOrderReturnDetail">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOutReturn, getOutReturn, delOutReturn, addOutReturn, printOutOrderReturn } from "@/api/stock/outReturn";
import { returnListRecord } from "@/api/stock/record";
import { listAllWorkshop } from "@/api/base/workshop";
import { listAllWarehouse } from "@/api/base/warehouse";
import selectOutOrder from "../../components/select-out-order/index";

export default {
  name: "SaleOutReturn",
  dicts: ['base_mat_unit'],
  components: { selectOutOrder },
  data() {
    return {
      loading: true,
      ids: [],
      returnNos: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      outReturnList: [],
      title: "",
      open: false,
      submitLoading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        returnNo: null,
        warehouseCode: null,
        workshopCode: null,
        returnType: 'common_return',
        returnReason: null,
        returnStatus: null,
        orderNo: null,
      },
      form: {},
      rules: {
        orderNo: [{ required: true, message: "请选出库单", trigger: "blur" }],
        returnReason: [{ required: true, message: "退货原因不能为空", trigger: "blur" }],
      },
      warehouseList: [],
      workshopList: [],
      selectOutOrderOpen: false,
      outOrderType: '',
      outOrderReturnDetailOpen: false,
      outReturnDetailList: [],
    };
  },
  created() {
    this.getList();
    this.getWarehouseList();
    this.getWorkshopList();
  },
  methods: {
    getList() {
      this.loading = true;
      listOutReturn(this.queryParams).then(response => {
        this.outReturnList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).finally(() => { this.loading = false; });
    },
    cancel() {
      this.open = false;
      this.reset();
      this.outReturnDetailList = [];
    },
    reset() {
      this.form = {
        returnId: null, returnNo: null, warehouseCode: null, workshopCode: null,
        returnType: 'common_return', returnReason: null, returnStatus: "0",
        prodOrderNo: null, orderNo: null,
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.returnId);
      this.returnNos = selection.map(item => item.returnNo);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.outReturnDetailList = [];
      this.open = true;
      this.title = "新增销售退货单";
    },
    handleDetail(row) {
      this.reset();
      getOutReturn(row.returnId).then(response => {
        this.form = response.data;
        this.outReturnDetailList = response.data.detailList;
        this.outOrderReturnDetailOpen = true;
      });
    },
    confirmPrintOutOrderReturn() {
      printOutOrderReturn(this.form.returnId).then(response => {
        const binaryData = [];
        binaryData.push(response);
        let pdfUrl = window.URL.createObjectURL(new Blob(binaryData, { type: "application/pdf" }));
        window.open(pdfUrl);
      });
    },
    cancelOrderReturnDetail() {
      this.outOrderReturnDetailOpen = false;
    },
    submitForm() {
      let that = this;
      if (!that.outReturnDetailList || that.outReturnDetailList.length === 0) {
        that.$modal.msgError("请选择退货信息");
        return;
      }
      let checkFlag = false;
      let overFlag = false;
      that.outReturnDetailList.forEach(item => {
        if (item.quantity > 0) checkFlag = true;
        if (item.quantity > item.receivedQuantity) overFlag = true;
      });
      if (!checkFlag) { that.$modal.msgError("请至少选择一项退货"); return; }
      if (overFlag) { that.$modal.msgError("需退货数量不能超过可退数量"); return; }
      that.$refs["form"].validate(valid => {
        if (valid) {
          that.$modal.confirm('是否确认创建销售退货单？').then(function () {
            that.form.detailList = that.outReturnDetailList;
            that.form.returnType = 'common_return';
            that.submitLoading = true;
            addOutReturn(that.form).then(response => {
              that.$modal.msgSuccess("新增成功");
              that.open = false;
              that.getList();
              that.reset();
              that.outReturnDetailList = [];
            }).finally(() => { that.submitLoading = false; });
          });
        }
      });
    },
    handleDelete(row) {
      const returnIds = row.returnId || this.ids;
      const delReturnNos = row.returnNo || this.returnNos;
      this.$modal.confirm('是否确认删除退货单号为 "' + delReturnNos + '" 的数据项？').then(function () {
        return delOutReturn(returnIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('stock/outReturn/export', { ...this.queryParams }, `saleOutReturn_${new Date().getTime()}.xlsx`);
    },
    getWarehouseList() {
      listAllWarehouse().then(response => { this.warehouseList = response; });
    },
    getWorkshopList() {
      listAllWorkshop().then(response => { this.workshopList = response; });
    },
    openSelectOutOrderDialog() {
      this.outOrderType = 'common';
      this.selectOutOrderOpen = true;
      this.reset();
      this.outReturnDetailList = [];
    },
    confirmSelectOutOrder(item) {
      this.form.warehouseCode = item.warehouseCode;
      this.form.workshopCode = item.workshopCode;
      this.form.orderNo = item.orderNo;
      returnListRecord(item.orderNo).then(response => {
        let recordList = response.data;
        if (!recordList || recordList.length === 0) {
          this.$modal.msgWarning("该出库单已全部退货完成，无可退物料");
          this.form.orderNo = null;
          return;
        }
        if (!this.form.warehouseCode && recordList.length > 0) {
          this.form.warehouseCode = recordList[0].warehouseCode;
        }
        this.outReturnDetailList = [];
        recordList.forEach(record => {
          this.outReturnDetailList.push({
            warehouseCode: record.warehouseCode, warehouseName: record.warehouseName,
            workshopCode: record.workshopCode, locationCode: record.locationCode,
            labelId: record.labelId, matCode: record.matCode, matName: record.matName,
            fdCode: record.fdCode, figNum: record.figNum, matGroup: record.matGroup,
            matClass: record.matClass, receivedQuantity: record.quantity, quantity: 0,
            unitCode: record.unitCode, supplierCode: record.supplierCode,
            supplierName: record.supplierName, batch: record.batch,
            orderNo: record.orderNo, createBy: record.createBy, createTime: record.createTime,
          });
        });
        this.selectOutOrderOpen = false;
      });
    },
    cancelSelectOutOrder() {
      this.selectOutOrderOpen = false;
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.detail-dialog {
  .el-form-item { margin-bottom: 0px; }
  .detail-table { margin-top: 20px; }
}
</style>

