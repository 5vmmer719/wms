<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="计划编号" prop="planNo">
        <el-input v-model="queryParams.planNo" placeholder="请输入计划编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="计划名称" prop="planName">
        <el-input v-model="queryParams.planName" placeholder="请输入计划名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="计划类型" prop="planType">
        <el-select v-model="queryParams.planType" placeholder="请选择" clearable>
          <el-option value="monthly" label="月度计划" />
          <el-option value="weekly" label="周度计划" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="planStatus">
        <el-select v-model="queryParams.planStatus" placeholder="请选择" clearable>
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="物料名称" prop="matName">
        <el-input v-model="queryParams.matName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="车间" prop="workshopCode">
        <el-select v-model="queryParams.workshopCode" placeholder="请选择车间" clearable>
          <el-option v-for="item in workshopList" :key="item.workshopCode" :label="item.workshopName" :value="item.workshopCode" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['stock:prodPlan:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['stock:prodPlan:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['stock:prodPlan:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="planList" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="计划编号" fixed align="center" prop="planNo" width="180" />
      <el-table-column label="计划名称" align="center" prop="planName" min-width="140" :show-overflow-tooltip="true" />
      <el-table-column label="计划类型" align="center" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.planType === 'monthly'" size="small">月度</el-tag>
          <el-tag v-else-if="scope.row.planType === 'weekly'" type="success" size="small">周度</el-tag>
          <span v-else>{{ scope.row.planType || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="物料编码" align="center" prop="matCode" width="120" :show-overflow-tooltip="true" />
      <el-table-column label="物料名称" align="center" prop="matName" width="160" :show-overflow-tooltip="true" />
      <el-table-column label="车间" align="center" prop="workshopName" width="120" />
      <el-table-column label="计划数量" align="center" prop="planQuantity" width="100" />
      <el-table-column label="完成数量" align="center" prop="actualQuantity" width="100" />
      <el-table-column label="完成率" align="center" width="120">
        <template slot-scope="scope">
          <el-progress :percentage="Number(scope.row.completionRate || 0)" :color="getProgressColor(scope.row.completionRate)" :stroke-width="14" :text-inside="true" style="width: 100px;" />
        </template>
      </el-table-column>
      <el-table-column label="计划时间" align="center" width="210">
        <template slot-scope="scope">
          <span v-if="scope.row.planStartDate">{{ parseTime(scope.row.planStartDate, '{y}-{m}-{d}') }} ~ {{ parseTime(scope.row.planEndDate, '{y}-{m}-{d}') }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="客户订单" align="center" prop="customerOrderNo" width="180">
        <template slot-scope="scope">
          <span v-if="scope.row.customerOrderNo" style="color: #409EFF;">{{ scope.row.customerOrderNo }}</span>
          <span v-else style="color: #909399;">-</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.planStatus === 'draft'" type="info">草稿</el-tag>
          <el-tag v-else-if="scope.row.planStatus === 'confirmed'" type="warning">已确认</el-tag>
          <el-tag v-else-if="scope.row.planStatus === 'executing'" type="primary">执行中</el-tag>
          <el-tag v-else-if="scope.row.planStatus === 'completed'" type="success">已完成</el-tag>
          <el-tag v-else-if="scope.row.planStatus === 'cancelled'" type="danger">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="工单统计" align="center" width="200">
        <template slot-scope="scope">
          <span v-if="scope.row.totalOrderCount > 0">
            共{{ scope.row.totalOrderCount }}单
            <el-tag size="mini" type="info" v-if="scope.row.plannedOrderCount">待排{{ scope.row.plannedOrderCount }}</el-tag>
            <el-tag size="mini" type="primary" v-if="scope.row.ongoingOrderCount">生产{{ scope.row.ongoingOrderCount }}</el-tag>
            <el-tag size="mini" type="success" v-if="scope.row.completedOrderCount">完工{{ scope.row.completedOrderCount }}</el-tag>
          </span>
          <span v-else style="color: #909399;">暂无工单</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">详情</el-button>
          <el-dropdown trigger="click" @command="(cmd) => handleCommand(cmd, scope.row)" style="margin-left: 10px;">
            <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-if="scope.row.planStatus === 'draft'" command="edit" icon="el-icon-edit">编辑</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.planStatus === 'draft'" command="confirm" icon="el-icon-check">确认</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.planStatus === 'confirmed'" command="generate" icon="el-icon-document-add">生成工单</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.planStatus === 'executing'" command="complete" icon="el-icon-circle-check">完成</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.planStatus === 'draft' || scope.row.planStatus === 'confirmed'" command="cancel" icon="el-icon-close">取消</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.planStatus === 'draft'" command="delete" icon="el-icon-delete" divided>删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="计划名称" prop="planName">
              <el-input v-model="form.planName" placeholder="请输入计划名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划类型" prop="planType">
              <el-select v-model="form.planType" placeholder="请选择计划类型" style="width: 100%">
                <el-option value="monthly" label="月度计划" />
                <el-option value="weekly" label="周度计划" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="产品物料" prop="matCode">
              <el-input readonly placeholder="请选择物料" v-model="form.matName">
                <el-button slot="append" icon="el-icon-search" @click="selectMatOpen = true"></el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划数量" prop="planQuantity">
              <el-input-number v-model="form.planQuantity" controls-position="right" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="车间" prop="workshopCode">
              <el-select v-model="form.workshopCode" placeholder="请选择车间" style="width: 100%">
                <el-option v-for="item in workshopList" :key="item.workshopCode" :label="item.workshopName" :value="item.workshopCode" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户订单">
              <el-select v-model="form.customerOrderNo" placeholder="关联客户订单（可选）" style="width: 100%" filterable clearable>
                <el-option v-for="item in customerOrderOptions" :key="item.orderNo" :label="item.orderNo + ' - ' + item.customerName" :value="item.orderNo" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="计划开始" prop="planStartDate">
              <el-date-picker v-model="form.planStartDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划结束" prop="planEndDate">
              <el-date-picker v-model="form.planEndDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 认</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="生产计划详情" :visible.sync="detailOpen" width="900px" append-to-body :close-on-click-modal="false">
      <el-descriptions :column="3" border size="medium" v-if="detailData">
        <el-descriptions-item label="计划编号">{{ detailData.planNo }}</el-descriptions-item>
        <el-descriptions-item label="计划名称">{{ detailData.planName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="detailData.planStatus === 'draft'" type="info">草稿</el-tag>
          <el-tag v-else-if="detailData.planStatus === 'confirmed'" type="warning">已确认</el-tag>
          <el-tag v-else-if="detailData.planStatus === 'executing'" type="primary">执行中</el-tag>
          <el-tag v-else-if="detailData.planStatus === 'completed'" type="success">已完成</el-tag>
          <el-tag v-else-if="detailData.planStatus === 'cancelled'" type="danger">已取消</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="计划类型">
          <span v-if="detailData.planType === 'monthly'">月度计划</span>
          <span v-else-if="detailData.planType === 'weekly'">周度计划</span>
          <span v-else>{{ detailData.planType || '-' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="物料编码">{{ detailData.matCode }}</el-descriptions-item>
        <el-descriptions-item label="物料名称">{{ detailData.matName }}</el-descriptions-item>
        <el-descriptions-item label="车间">{{ detailData.workshopName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="计划数量">{{ detailData.planQuantity }}</el-descriptions-item>
        <el-descriptions-item label="完成数量">{{ detailData.actualQuantity || 0 }}</el-descriptions-item>
        <el-descriptions-item label="完成率">
          <el-progress :percentage="Number(detailData.completionRate || 0)" :color="getProgressColor(detailData.completionRate)" :stroke-width="14" :text-inside="true" style="width: 120px;" />
        </el-descriptions-item>
        <el-descriptions-item label="计划开始">{{ detailData.planStartDate ? parseTime(detailData.planStartDate, '{y}-{m}-{d}') : '-' }}</el-descriptions-item>
        <el-descriptions-item label="计划结束">{{ detailData.planEndDate ? parseTime(detailData.planEndDate, '{y}-{m}-{d}') : '-' }}</el-descriptions-item>
        <el-descriptions-item label="客户订单">
          <span v-if="detailData.customerOrderNo" style="color: #409EFF;">{{ detailData.customerOrderNo }}</span>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime ? parseTime(detailData.createTime, '{y}-{m}-{d} {h}:{i}') : '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detailData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>

      <!-- 关联工单列表 -->
      <div style="margin-top: 20px;">
        <h4 style="margin-bottom: 10px;">关联生产工单</h4>
        <el-table :data="relatedOrders" size="small" border v-loading="detailLoading">
          <el-table-column label="工单号" align="center" prop="orderNo" width="180" />
          <el-table-column label="工令号" align="center" prop="workNo" width="120" />
          <el-table-column label="物料" align="center" prop="matName" width="160" :show-overflow-tooltip="true" />
          <el-table-column label="计划数量" align="center" prop="quantity" width="90" />
          <el-table-column label="完成数量" align="center" prop="actualQuantity" width="90" />
          <el-table-column label="状态" align="center" width="90">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.orderStatus === 'planned'" type="info" size="small">待排产</el-tag>
              <el-tag v-else-if="scope.row.orderStatus === 'ongoing'" type="primary" size="small">生产中</el-tag>
              <el-tag v-else-if="scope.row.orderStatus === 'completed'" type="success" size="small">已完工</el-tag>
              <el-tag v-else-if="scope.row.orderStatus === 'closed'" size="small">已关闭</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="!relatedOrders || relatedOrders.length === 0" description="暂无关联工单" :image-size="60" />
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 选择物料 -->
    <el-dialog title="选择物料" :visible.sync="selectMatOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <selectMatBom @confirmSelect="confirmSelectMat"></selectMatBom>
      <div slot="footer" class="dialog-footer">
        <el-button @click="selectMatOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProdPlan, getProdPlan, addProdPlan, updateProdPlan, delProdPlan, confirmProdPlan, generateOrders, completeProdPlan, cancelProdPlan } from "@/api/stock/prodPlan";
import { listProdOrder } from "@/api/stock/prodOrder";
import { listAllWorkshop } from "@/api/base/workshop";
import { listCustomerOrder } from "@/api/order/customerOrder";
import selectMatBom from "../../components/select-mat-bom/index";

export default {
  name: "ProdPlan",
  components: { selectMatBom },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      planList: [],
      title: "",
      open: false,
      submitLoading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        planNo: null,
        planName: null,
        planType: null,
        planStatus: null,
        matName: null,
        workshopCode: null,
      },
      form: {},
      rules: {
        planName: [{ required: true, message: "请输入计划名称", trigger: "blur" }],
        planType: [{ required: true, message: "请选择计划类型", trigger: "change" }],
        matCode: [{ required: true, message: "请选择产品物料", trigger: "blur" }],
        planQuantity: [{ required: true, message: "请输入计划数量", trigger: "blur" }],
        workshopCode: [{ required: true, message: "请选择车间", trigger: "change" }],
        planStartDate: [{ required: true, message: "请选择计划开始日期", trigger: "change" }],
        planEndDate: [{ required: true, message: "请选择计划结束日期", trigger: "change" }],
      },
      workshopList: [],
      customerOrderOptions: [],
      selectMatOpen: false,
      statusOptions: [
        { value: 'draft', label: '草稿' },
        { value: 'confirmed', label: '已确认' },
        { value: 'executing', label: '执行中' },
        { value: 'completed', label: '已完成' },
        { value: 'cancelled', label: '已取消' },
      ],
      // 详情
      detailOpen: false,
      detailLoading: false,
      detailData: null,
      relatedOrders: [],
    };
  },
  created() {
    this.getList();
    this.getWorkshopList();
    this.loadCustomerOrderOptions();
  },
  methods: {
    getList() {
      this.loading = true;
      listProdPlan(this.queryParams).then(response => {
        this.planList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).finally(() => {
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        planId: null,
        planNo: null,
        planName: null,
        planType: null,
        planStartDate: null,
        planEndDate: null,
        matCode: null,
        matName: null,
        planQuantity: null,
        customerOrderNo: null,
        workshopCode: null,
        remark: null,
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
      this.ids = selection.map(item => item.planId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增生产计划";
    },
    handleEdit(row) {
      this.reset();
      getProdPlan(row.planId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改生产计划";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let that = this;
          if (that.form.planId != null) {
            that.$modal.confirm('是否确认修改该生产计划？').then(function() {
              that.submitLoading = true;
              updateProdPlan(that.form).then(response => {
                that.$modal.msgSuccess("修改成功");
                that.open = false;
                that.getList();
              }).finally(() => { that.submitLoading = false; });
            });
          } else {
            that.$modal.confirm('是否确认创建该生产计划？').then(function() {
              that.submitLoading = true;
              addProdPlan(that.form).then(response => {
                that.$modal.msgSuccess("新增成功");
                that.open = false;
                that.getList();
              }).finally(() => { that.submitLoading = false; });
            });
          }
        }
      });
    },
    /** 操作命令分发 */
    handleCommand(command, row) {
      switch (command) {
        case 'edit': this.handleEdit(row); break;
        case 'confirm': this.handleConfirm(row); break;
        case 'generate': this.handleGenerate(row); break;
        case 'complete': this.handleComplete(row); break;
        case 'cancel': this.handleCancel(row); break;
        case 'delete': this.handleDelete(row); break;
      }
    },
    /** 确认计划 */
    handleConfirm(row) {
      this.$modal.confirm('是否确认该生产计划？确认后将不可编辑。').then(() => {
        return confirmProdPlan(row.planId);
      }).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess(response.msg);
          this.getList();
        } else {
          this.$modal.msgError(response.msg);
        }
      }).catch(() => {});
    },
    /** 生成工单 */
    handleGenerate(row) {
      this.$modal.confirm('是否根据该计划生成生产工单？').then(() => {
        return generateOrders(row.planId);
      }).then(response => {
        if (response.code === 200) {
          this.getList();
          this.$alert(response.msg, '生成结果', { confirmButtonText: '知道了', type: 'success', dangerouslyUseHTMLString: false });
        } else {
          this.$modal.msgError(response.msg);
        }
      }).catch(() => {});
    },
    /** 完成计划 */
    handleComplete(row) {
      this.$modal.confirm('是否确认完成该生产计划？').then(() => {
        return completeProdPlan(row.planId);
      }).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess(response.msg);
          this.getList();
        } else {
          this.$modal.msgError(response.msg);
        }
      }).catch(() => {});
    },
    /** 取消计划 */
    handleCancel(row) {
      this.$modal.confirm('是否确认取消该生产计划？').then(() => {
        return cancelProdPlan(row.planId);
      }).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess(response.msg);
          this.getList();
        } else {
          this.$modal.msgError(response.msg);
        }
      }).catch(() => {});
    },
    /** 详情 */
    handleView(row) {
      this.detailOpen = true;
      this.detailLoading = true;
      this.detailData = row;
      this.relatedOrders = [];
      // 查询关联工单
      if (row.planNo) {
        listProdOrder({ planNo: row.planNo, pageNum: 1, pageSize: 100 }).then(response => {
          this.relatedOrders = response.rows || [];
        }).finally(() => { this.detailLoading = false; });
      } else {
        this.detailLoading = false;
      }
    },
    handleDelete(row) {
      const planIds = row.planId || this.ids;
      this.$modal.confirm('是否确认删除该生产计划？').then(function() {
        return delProdPlan(planIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('stock/prodPlan/export', { ...this.queryParams }, `prodPlan_${new Date().getTime()}.xlsx`);
    },
    confirmSelectMat(item) {
      this.form.matCode = item.matCode;
      this.form.matName = item.matName;
      this.selectMatOpen = false;
    },
    getWorkshopList() {
      listAllWorkshop().then(response => {
        this.workshopList = response;
      });
    },
    loadCustomerOrderOptions() {
      listCustomerOrder({ pageNum: 1, pageSize: 500 }).then(response => {
        this.customerOrderOptions = (response.rows || []).filter(o =>
          ['confirmed', 'producing', 'completed'].includes(o.orderStatus)
        );
      });
    },
    getProgressColor(rate) {
      const r = Number(rate || 0);
      if (r >= 100) return '#67C23A';
      if (r >= 60) return '#409EFF';
      if (r >= 30) return '#E6A23C';
      return '#F56C6C';
    },
  }
};
</script>

