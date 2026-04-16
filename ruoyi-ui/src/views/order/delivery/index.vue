<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="交付单号" prop="deliveryNo">
        <el-input v-model="queryParams.deliveryNo" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="客户订单" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入客户订单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="客户名称" prop="customerName">
        <el-input v-model="queryParams.customerName" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="deliveryStatus">
        <el-select v-model="queryParams.deliveryStatus" placeholder="全部" clearable>
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['order:delivery:add']">新增交付单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['order:delivery:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['order:delivery:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="deliveryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="交付单号" align="center" prop="deliveryNo" width="180" />
      <el-table-column label="客户订单号" align="center" prop="orderNo" width="180">
        <template slot-scope="scope">
          <span style="color: #409EFF;">{{ scope.row.orderNo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="客户名称" align="center" prop="customerName" min-width="120" :show-overflow-tooltip="true" />
      <el-table-column label="出库单号" align="center" prop="outOrderNo" width="180">
        <template slot-scope="scope">
          <span v-if="scope.row.outOrderNo" style="color: #67C23A;">{{ scope.row.outOrderNo }}</span>
          <span v-else style="color: #909399;">-</span>
        </template>
      </el-table-column>
      <el-table-column label="交付总数" align="center" prop="totalQuantity" width="100" />
      <el-table-column label="状态" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.deliveryStatus)" size="small">{{ scope.row.deliveryStatusLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="交付日期" align="center" prop="deliveryDate" width="110" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="260">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button v-if="scope.row.deliveryStatus === 'pending'" size="mini" type="text" icon="el-icon-s-promotion" @click="handleShip(scope.row)" v-hasPermi="['order:delivery:edit']">发货</el-button>
          <el-button v-if="scope.row.deliveryStatus === 'shipped'" size="mini" type="text" icon="el-icon-circle-check" @click="handleReceive(scope.row)" v-hasPermi="['order:delivery:edit']">签收</el-button>
          <el-button v-if="scope.row.deliveryStatus === 'pending'" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['order:delivery:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增交付单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户订单" prop="orderNo">
              <el-select v-model="form.orderNo" placeholder="请选择客户订单" style="width: 100%" filterable @change="onOrderChange">
                <el-option v-for="item in customerOrderOptions" :key="item.orderNo" :label="item.orderNo + ' - ' + item.customerName" :value="item.orderNo" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出库单号" prop="outOrderNo">
              <el-input v-model="form.outOrderNo" placeholder="关联出库单号（可选）" clearable />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="物流公司">
              <el-input v-model="form.logisticsCompany" placeholder="请输入物流公司" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物流单号">
              <el-input v-model="form.logisticsNo" placeholder="请输入物流单号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="交付地址">
            <el-input v-model="form.deliveryAddress" placeholder="请输入交付地址" />
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="备注">
            <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
          </el-form-item>
        </el-row>

        <!-- 交付明细 -->
        <el-divider content-position="left">交付明细</el-divider>
        <el-button type="primary" size="mini" icon="el-icon-plus" @click="addDetailRow" style="margin-bottom: 10px;">添加物料</el-button>
        <el-table :data="form.detailList" border size="small">
          <el-table-column label="序号" width="60" align="center">
            <template slot-scope="scope">{{ scope.$index + 1 }}</template>
          </el-table-column>
          <el-table-column label="物料编码" min-width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.matCode" size="small" placeholder="点击选择物料" readonly @focus="openSelectMat(scope.$index)">
                <el-button slot="append" icon="el-icon-search" @click="openSelectMat(scope.$index)" />
              </el-input>
            </template>
          </el-table-column>
          <el-table-column label="物料名称" min-width="150">
            <template slot-scope="scope">
              <span>{{ scope.row.matName || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="规格" min-width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.spec" size="small" placeholder="规格" />
            </template>
          </el-table-column>
          <el-table-column label="交付数量" width="130">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.quantity" size="small" :min="0" :precision="2" :controls="false" style="width: 100%" />
            </template>
          </el-table-column>
          <el-table-column label="单位" width="80" align="center">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="60" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-delete" @click="removeDetailRow(scope.$index)" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 交付详情对话框 -->
    <el-dialog title="交付单详情" :visible.sync="detailOpen" width="1000px" append-to-body>
      <template v-if="detailData.record">
        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="交付单号">{{ detailData.record.deliveryNo }}</el-descriptions-item>
          <el-descriptions-item label="客户订单号">
            <span style="color: #409EFF;">{{ detailData.record.orderNo }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="客户名称">{{ detailData.record.customerName }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(detailData.record.deliveryStatus)" size="small">{{ detailData.record.deliveryStatusLabel }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="交付日期">{{ detailData.record.deliveryDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="交付总数">{{ detailData.record.totalQuantity }}</el-descriptions-item>
          <el-descriptions-item label="出库单号">
            <span v-if="detailData.record.outOrderNo" style="color: #67C23A;">{{ detailData.record.outOrderNo }}</span>
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="物流公司">{{ detailData.record.logisticsCompany || '-' }}</el-descriptions-item>
          <el-descriptions-item label="物流单号">{{ detailData.record.logisticsNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="交付地址" :span="3">{{ detailData.record.deliveryAddress || '-' }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="3">{{ detailData.record.remark || '-' }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">交付明细</el-divider>
        <el-table :data="detailData.detailList" border size="small">
          <el-table-column label="行号" prop="lineNo" width="60" align="center" />
          <el-table-column label="物料编码" prop="matCode" width="140" />
          <el-table-column label="物料名称" prop="matName" min-width="150" />
          <el-table-column label="规格" prop="spec" width="120" />
          <el-table-column label="交付数量" prop="quantity" width="100" align="center" />
          <el-table-column label="单位" width="80" align="center">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode" />
            </template>
          </el-table-column>
        </el-table>
      </template>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="detailData.record && detailData.record.deliveryStatus === 'pending'" type="success" icon="el-icon-s-promotion" @click="handleShipFromDetail">发 货</el-button>
        <el-button v-if="detailData.record && detailData.record.deliveryStatus === 'shipped'" type="primary" icon="el-icon-circle-check" @click="handleReceiveFromDetail">签 收</el-button>
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 选择成品物料弹窗 -->
    <el-dialog title="选择成品物料" :visible.sync="selectMatOpen" width="1000px" append-to-body>
      <selectMat :matGroup="'CP'" @confirmSelect="confirmSelectMat" />
    </el-dialog>
  </div>
</template>

<script>
import { listDelivery, getDeliveryDetail, addDelivery, delDelivery, shipDelivery, receiveDelivery } from "@/api/order/delivery";
import { listCustomerOrder, getCustomerOrderDetail } from "@/api/order/customerOrder";
import selectMat from "../../components/select-mat/index";

export default {
  name: "Delivery",
  dicts: ['base_mat_unit'],
  components: { selectMat },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      deliveryList: [],
      customerOrderOptions: [],
      title: "",
      open: false,
      detailOpen: false,
      detailData: {},
      selectMatOpen: false,
      currentDetailIndex: -1,
      submitLoading: false,
      statusOptions: [
        { value: "pending", label: "待发货" },
        { value: "shipped", label: "已发货" },
        { value: "received", label: "已签收" },
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deliveryNo: null,
        orderNo: null,
        customerName: null,
        deliveryStatus: null,
      },
      form: {},
      rules: {
        orderNo: [{ required: true, message: "请选择客户订单", trigger: "change" }],
      },
    };
  },
  created() {
    this.getList();
    this.loadCustomerOrderOptions();
  },
  methods: {
    getList() {
      this.loading = true;
      listDelivery(this.queryParams).then(response => {
        this.deliveryList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).catch(() => { this.loading = false; });
    },
    loadCustomerOrderOptions() {
      // 加载已完成/生产中的客户订单供选择
      listCustomerOrder({ pageNum: 1, pageSize: 500 }).then(response => {
        this.customerOrderOptions = (response.rows || []).filter(o =>
          ['confirmed', 'producing', 'completed'].includes(o.orderStatus)
        );
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        deliveryId: null,
        deliveryNo: null,
        orderNo: null,
        customerCode: null,
        customerName: null,
        logisticsNo: null,
        logisticsCompany: null,
        deliveryAddress: null,
        outOrderNo: null,
        remark: null,
        detailList: [],
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
      this.ids = selection.map(item => item.deliveryId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    onOrderChange(val) {
      const order = this.customerOrderOptions.find(o => o.orderNo === val);
      if (order) {
        this.form.customerCode = order.customerCode;
        this.form.customerName = order.customerName;
        // 根据客户订单详情自动填充交付明细
        getCustomerOrderDetail(order.orderId).then(response => {
          const detailList = response.data.detailList || [];
          this.form.detailList = detailList.map(d => ({
            matCode: d.matCode,
            matName: d.matName,
            spec: d.spec || null,
            quantity: d.quantity ? Number(d.quantity) - Number(d.deliveredQty || 0) : null,
            unitCode: d.unitCode || null,
          })).filter(d => !d.quantity || d.quantity > 0);
        });
      }
    },
    addDetailRow() {
      this.form.detailList.push({
        matCode: null,
        matName: null,
        spec: null,
        quantity: null,
        unitCode: null,
      });
    },
    openSelectMat(index) {
      this.currentDetailIndex = index;
      this.selectMatOpen = true;
    },
    confirmSelectMat(row) {
      if (this.currentDetailIndex >= 0 && this.currentDetailIndex < this.form.detailList.length) {
        const detail = this.form.detailList[this.currentDetailIndex];
        detail.matCode = row.matCode;
        detail.matName = row.matName;
        detail.spec = row.figNum || null;
        detail.unitCode = row.unitCode || null;
      }
      this.selectMatOpen = false;
    },
    removeDetailRow(index) {
      this.form.detailList.splice(index, 1);
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增交付单";
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (!this.form.detailList || this.form.detailList.length === 0) {
            this.$modal.msgWarning("请至少添加一条交付明细");
            return;
          }
          // 校验明细数量
          let hasQty = false;
          for (const d of this.form.detailList) {
            if (d.quantity && d.quantity > 0) hasQty = true;
          }
          if (!hasQty) {
            this.$modal.msgWarning("请至少填写一项交付数量");
            return;
          }
          this.submitLoading = true;
          addDelivery(this.form).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.open = false;
            this.getList();
          }).finally(() => { this.submitLoading = false; });
        }
      });
    },
    handleDetail(row) {
      getDeliveryDetail(row.deliveryId).then(response => {
        this.detailData = response.data;
        this.detailOpen = true;
      });
    },
    handleShip(row) {
      this.$modal.confirm('确认对交付单 "' + row.deliveryNo + '" 执行发货操作？发货后将回写客户订单已交付数量。').then(() => {
        return shipDelivery(row.deliveryId);
      }).then(res => {
        this.$modal.msgSuccess(res.msg || "发货成功");
        this.getList();
      }).catch(() => {});
    },
    handleReceive(row) {
      this.$modal.confirm('确认对交付单 "' + row.deliveryNo + '" 执行签收操作？').then(() => {
        return receiveDelivery(row.deliveryId);
      }).then(res => {
        this.$modal.msgSuccess(res.msg || "签收成功");
        this.getList();
      }).catch(() => {});
    },
    handleShipFromDetail() {
      if (this.detailData.record) {
        this.handleShip(this.detailData.record);
        this.detailOpen = false;
      }
    },
    handleReceiveFromDetail() {
      if (this.detailData.record) {
        this.handleReceive(this.detailData.record);
        this.detailOpen = false;
      }
    },
    handleDelete(row) {
      const deliveryIds = row.deliveryId ? [row.deliveryId] : this.ids;
      this.$modal.confirm('是否确认删除选中的交付记录？').then(() => {
        return delDelivery(deliveryIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('order/delivery/export', { ...this.queryParams }, `交付记录_${new Date().getTime()}.xlsx`);
    },
    getStatusType(status) {
      const map = { pending: 'warning', shipped: '', received: 'success' };
      return map[status] || 'info';
    },
  }
};
</script>

