<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="客户名称" prop="customerName">
        <el-input v-model="queryParams.customerName" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="全部" clearable>
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['order:customerOrder:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['order:customerOrder:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['order:customerOrder:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单号" align="center" prop="orderNo" width="180" />
      <el-table-column label="客户名称" align="center" prop="customerName" min-width="150" :show-overflow-tooltip="true" />
      <el-table-column label="下单日期" align="center" prop="orderDate" width="110" />
      <el-table-column label="交付日期" align="center" prop="deliveryDate" width="110">
        <template slot-scope="scope">
          <span :style="isOverdue(scope.row) ? 'color: #F56C6C; font-weight: bold;' : ''">
            {{ scope.row.deliveryDate }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="订单金额" align="center" prop="totalAmount" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.totalAmount">¥{{ scope.row.totalAmount.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="orderStatus" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.orderStatus)" size="small">
            {{ scope.row.orderStatusLabel }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button v-if="scope.row.orderStatus === 'created'" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['order:customerOrder:edit']">编辑</el-button>
          <el-dropdown v-if="showMoreActions(scope.row)" size="mini" @command="(cmd) => handleCommand(cmd, scope.row)" style="margin-left: 10px;">
            <el-button size="mini" type="text">更多<i class="el-icon-arrow-down el-icon--right"></i></el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-if="scope.row.orderStatus === 'created'" command="confirm" icon="el-icon-check">确认订单</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.orderStatus === 'confirmed' || scope.row.orderStatus === 'producing'" command="generate" icon="el-icon-s-order">生成工单</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.orderStatus !== 'closed'" command="close" icon="el-icon-circle-close">关闭订单</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.orderStatus === 'created'" command="delete" icon="el-icon-delete">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/修改对话框（含订单明细） -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="客户" prop="customerCode">
              <el-select v-model="form.customerCode" placeholder="请选择客户" style="width: 100%" filterable @change="onCustomerChange">
                <el-option v-for="item in customerOptions" :key="item.customerCode" :label="item.customerName" :value="item.customerCode" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="下单日期" prop="orderDate">
              <el-date-picker v-model="form.orderDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="交付日期" prop="deliveryDate">
              <el-date-picker v-model="form.deliveryDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>

        <!-- 订单明细 -->
        <el-divider content-position="left">订单明细</el-divider>
        <el-button type="primary" size="mini" icon="el-icon-plus" @click="addDetailRow" style="margin-bottom: 10px;">添加产品</el-button>
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
          <el-table-column label="产品名称" min-width="150">
            <template slot-scope="scope">
              <span>{{ scope.row.matName || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="规格" min-width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.spec" size="small" placeholder="规格" />
            </template>
          </el-table-column>
          <el-table-column label="数量" width="120">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.quantity" size="small" :min="0" :precision="2" :controls="false" style="width: 100%" @change="calcRowAmount(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column label="单价" width="120">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.unitPrice" size="small" :min="0" :precision="2" :controls="false" style="width: 100%" @change="calcRowAmount(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column label="金额" width="120" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.amount">¥{{ scope.row.amount.toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="60" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-delete" @click="removeDetailRow(scope.$index)" />
            </template>
          </el-table-column>
        </el-table>
        <div style="text-align: right; margin-top: 10px; font-weight: bold; font-size: 14px;">
          合计金额：¥{{ totalAmount.toFixed(2) }}
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 订单详情对话框 -->
    <el-dialog title="订单详情" :visible.sync="detailOpen" width="1100px" append-to-body>
      <template v-if="detailData.order">
        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="订单号">{{ detailData.order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="客户名称">{{ detailData.order.customerName }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(detailData.order.orderStatus)" size="small">{{ detailData.order.orderStatusLabel }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="下单日期">{{ detailData.order.orderDate }}</el-descriptions-item>
          <el-descriptions-item label="要求交付日期">{{ detailData.order.deliveryDate }}</el-descriptions-item>
          <el-descriptions-item label="订单金额">
            <span v-if="detailData.order.totalAmount">¥{{ detailData.order.totalAmount.toFixed(2) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="备注" :span="3">{{ detailData.order.remark }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">订单明细</el-divider>
        <el-table :data="detailData.detailList" border size="small">
          <el-table-column label="行号" prop="lineNo" width="60" align="center" />
          <el-table-column label="物料编码" prop="matCode" width="120" />
          <el-table-column label="产品名称" prop="matName" min-width="150" />
          <el-table-column label="规格" prop="spec" width="120" />
          <el-table-column label="数量" prop="quantity" width="100" align="center" />
          <el-table-column label="单价" width="100" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.unitPrice">¥{{ scope.row.unitPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column label="金额" width="110" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.amount">¥{{ scope.row.amount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="已交付" prop="deliveredQty" width="80" align="center" />
          <el-table-column label="关联工单" prop="prodOrderNo" width="180">
            <template slot-scope="scope">
              <span v-if="scope.row.prodOrderNo" style="color: #409EFF;">{{ scope.row.prodOrderNo }}</span>
              <span v-else style="color: #909399;">未关联</span>
            </template>
          </el-table-column>
        </el-table>

        <template v-if="detailData.prodOrders && detailData.prodOrders.length > 0">
          <el-divider content-position="left">关联生产工单</el-divider>
          <el-table :data="detailData.prodOrders" border size="small">
            <el-table-column label="工单号" prop="orderNo" width="180" />
            <el-table-column label="工令号" prop="workNo" width="120" />
            <el-table-column label="物料名称" prop="matName" min-width="150" />
            <el-table-column label="计划数量" prop="quantity" width="100" align="center" />
            <el-table-column label="完成数量" prop="actualQuantity" width="100" align="center" />
            <el-table-column label="状态" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="getProdStatusType(scope.row.orderStatus)" size="small">{{ scope.row.orderStatusLabel }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </template>
    </el-dialog>

    <!-- 选择成品物料弹窗 -->
    <el-dialog title="选择成品物料" :visible.sync="selectMatOpen" width="1000px" append-to-body>
      <selectMat :matGroup="'CP'" @confirmSelect="confirmSelectMat" />
    </el-dialog>
  </div>
</template>

<script>
import { listCustomerOrder, getCustomerOrder, getCustomerOrderDetail, addCustomerOrder, updateCustomerOrder, delCustomerOrder, confirmCustomerOrder, generateProdOrder, closeCustomerOrder } from "@/api/order/customerOrder";
import { listAllCustomer } from "@/api/base/customer";
import selectMat from "../../components/select-mat/index";

export default {
  name: "CustomerOrder",
  components: { selectMat },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      orderList: [],
      customerOptions: [],
      title: "",
      open: false,
      detailOpen: false,
      detailData: {},
      selectMatOpen: false,
      currentDetailIndex: -1,
      statusOptions: [
        { value: "created", label: "已创建" },
        { value: "confirmed", label: "已确认" },
        { value: "producing", label: "生产中" },
        { value: "completed", label: "已完成" },
        { value: "delivered", label: "已交付" },
        { value: "closed", label: "已关闭" },
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        customerName: null,
        orderStatus: null,
      },
      form: {},
      rules: {
        customerCode: [{ required: true, message: "请选择客户", trigger: "change" }],
        orderDate: [{ required: true, message: "请选择下单日期", trigger: "change" }],
        deliveryDate: [{ required: true, message: "请选择交付日期", trigger: "change" }],
      }
    };
  },
  computed: {
    totalAmount() {
      if (!this.form.detailList) return 0;
      return this.form.detailList.reduce((sum, row) => sum + (row.amount || 0), 0);
    }
  },
  created() {
    this.getList();
    this.loadCustomerOptions();
  },
  methods: {
    getList() {
      this.loading = true;
      listCustomerOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    loadCustomerOptions() {
      listAllCustomer({ customerStatus: '0' }).then(response => {
        this.customerOptions = response.data || [];
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        orderId: null,
        orderNo: null,
        customerCode: null,
        customerName: null,
        orderDate: null,
        deliveryDate: null,
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
      this.ids = selection.map(item => item.orderId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    onCustomerChange(val) {
      const customer = this.customerOptions.find(c => c.customerCode === val);
      if (customer) {
        this.form.customerName = customer.customerName;
      }
    },
    addDetailRow() {
      this.form.detailList.push({
        matCode: null,
        matName: null,
        spec: null,
        quantity: null,
        unitPrice: null,
        amount: null,
      });
    },
    /** 打开成品物料选择弹窗 */
    openSelectMat(index) {
      this.currentDetailIndex = index;
      this.selectMatOpen = true;
    },
    /** 确认选择物料后回填到明细行 */
    confirmSelectMat(row) {
      if (this.currentDetailIndex >= 0 && this.currentDetailIndex < this.form.detailList.length) {
        const detail = this.form.detailList[this.currentDetailIndex];
        detail.matCode = row.matCode;
        detail.matName = row.matName;
        detail.spec = row.figNum || null;
        if (row.standardPrice) {
          detail.unitPrice = row.standardPrice;
          this.calcRowAmount(detail);
        }
      }
      this.selectMatOpen = false;
    },
    removeDetailRow(index) {
      this.form.detailList.splice(index, 1);
    },
    calcRowAmount(row) {
      if (row.quantity && row.unitPrice) {
        row.amount = row.quantity * row.unitPrice;
      } else {
        row.amount = null;
      }
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增客户订单";
    },
    handleUpdate(row) {
      this.reset();
      const orderId = row.orderId || this.ids[0];
      getCustomerOrderDetail(orderId).then(response => {
        const data = response.data;
        this.form = {
          orderId: data.order.orderId,
          orderNo: data.order.orderNo,
          customerCode: data.order.customerCode,
          customerName: data.order.customerName,
          orderDate: data.order.orderDate,
          deliveryDate: data.order.deliveryDate,
          remark: data.order.remark,
          detailList: data.detailList || [],
        };
        this.open = true;
        this.title = "修改客户订单";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.detailList.length === 0) {
            this.$modal.msgWarning("请至少添加一条订单明细");
            return;
          }
          if (this.form.orderId != null) {
            updateCustomerOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCustomerOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDetail(row) {
      getCustomerOrderDetail(row.orderId).then(response => {
        this.detailData = response.data;
        this.detailOpen = true;
      });
    },
    handleDelete(row) {
      const orderIds = row.orderId ? [row.orderId] : this.ids;
      this.$modal.confirm('是否确认删除选中的订单？').then(() => {
        return delCustomerOrder(orderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleCommand(command, row) {
      switch (command) {
        case 'confirm':
          this.$modal.confirm('确认该订单？确认后将不可编辑。').then(() => {
            return confirmCustomerOrder(row.orderId);
          }).then(res => {
            this.$modal.msgSuccess(res.msg);
            this.getList();
          }).catch(() => {});
          break;
        case 'generate':
          this.$modal.confirm('确认为该订单生成生产工单？').then(() => {
            return generateProdOrder(row.orderId);
          }).then(res => {
            this.$modal.msgSuccess(res.msg);
            this.getList();
          }).catch(() => {});
          break;
        case 'close':
          this.$modal.confirm('确认关闭该订单？').then(() => {
            return closeCustomerOrder(row.orderId);
          }).then(res => {
            this.$modal.msgSuccess(res.msg);
            this.getList();
          }).catch(() => {});
          break;
        case 'delete':
          this.handleDelete(row);
          break;
      }
    },
    showMoreActions(row) {
      return row.orderStatus !== 'closed';
    },
    isOverdue(row) {
      if (!row.deliveryDate || row.orderStatus === 'delivered' || row.orderStatus === 'closed') return false;
      return new Date(row.deliveryDate) < new Date();
    },
    getStatusType(status) {
      const map = { created: 'info', confirmed: '', producing: 'warning', completed: 'success', delivered: 'success', closed: 'info' };
      return map[status] || 'info';
    },
    getProdStatusType(status) {
      const map = { planned: 'info', ongoing: 'warning', completed: 'success', closed: 'info' };
      return map[status] || 'info';
    },
    handleExport() {
      this.download('order/customerOrder/export', { ...this.queryParams }, `客户订单_${new Date().getTime()}.xlsx`);
    }
  }
};
</script>

