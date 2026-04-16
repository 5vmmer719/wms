<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="盘点单号" prop="checkNo">
        <el-input v-model="queryParams.checkNo" placeholder="请输入盘点单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="盘点类型" prop="checkType">
        <el-select v-model="queryParams.checkType" placeholder="请选择" clearable>
          <el-option label="全盘" value="full" />
          <el-option label="循环盘" value="cycle" />
          <el-option label="抽盘" value="spot" />
        </el-select>
      </el-form-item>
      <el-form-item label="仓库" prop="warehouseCode">
        <el-select v-model="queryParams.warehouseCode" placeholder="请选择仓库" clearable>
          <el-option v-for="item in warehouseList" :key="item.warehouseCode" :label="item.warehouseName" :value="item.warehouseCode" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="checkStatus">
        <el-select v-model="queryParams.checkStatus" placeholder="请选择" clearable>
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['stock:check:add']">创建盘点单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['stock:check:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="checkList">
      <el-table-column label="盘点单号" align="center" prop="checkNo" width="180" />
      <el-table-column label="盘点类型" align="center" prop="checkType" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.checkType === 'full'">全盘</span>
          <span v-else-if="scope.row.checkType === 'cycle'">循环盘</span>
          <span v-else-if="scope.row.checkType === 'spot'">抽盘</span>
          <span v-else>{{ scope.row.checkType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="仓库" align="center" prop="warehouseName" />
      <el-table-column label="状态" align="center" prop="checkStatus" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.checkStatus)">{{ getStatusLabel(scope.row.checkStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="物料总数" align="center" prop="totalItems" width="90" />
      <el-table-column label="差异数" align="center" prop="diffItems" width="80">
        <template slot-scope="scope">
          <span :style="{ color: scope.row.diffItems > 0 ? '#e6a23c' : '' }">{{ scope.row.diffItems }}</span>
        </template>
      </el-table-column>
      <el-table-column label="盘点人" align="center" prop="checkerName" width="100" />
      <el-table-column label="计划日期" align="center" prop="planDate" width="110">
        <template slot-scope="scope">
          <span v-if="scope.row.planDate">{{ $moment(scope.row.planDate).format('YYYY-MM-DD') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ $moment(scope.row.createTime).format('YYYY-MM-DD HH:mm') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="240">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button v-if="scope.row.checkStatus === 'created' || scope.row.checkStatus === 'counting'"
            size="mini" type="text" icon="el-icon-edit" @click="handleCount(scope.row)">录入</el-button>
          <el-button v-if="scope.row.checkStatus === 'completed'"
            size="mini" type="text" icon="el-icon-sort" @click="handleAdjust(scope.row)" style="color:#e6a23c">调整</el-button>
          <el-button v-if="scope.row.checkStatus === 'created'"
            size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['stock:check:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 创建盘点单对话框 -->
    <el-dialog title="创建盘点单" :visible.sync="addOpen" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="100px">
        <el-form-item label="盘点仓库" prop="warehouseCode">
          <el-select v-model="addForm.warehouseCode" placeholder="请选择仓库" style="width:100%"
            @change="onWarehouseChange">
            <el-option v-for="item in warehouseList" :key="item.warehouseCode" :label="item.warehouseName" :value="item.warehouseCode" />
          </el-select>
        </el-form-item>
        <el-form-item label="盘点类型" prop="checkType">
          <el-select v-model="addForm.checkType" placeholder="请选择" style="width:100%">
            <el-option label="全盘" value="full" />
            <el-option label="循环盘" value="cycle" />
            <el-option label="抽盘" value="spot" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划盘点日期" prop="planDate">
          <el-date-picker v-model="addForm.planDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="addForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitAdd">创 建</el-button>
        <el-button @click="addOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 盘点详情/录入对话框 -->
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <el-descriptions :column="4" border size="small" style="margin-bottom:16px">
        <el-descriptions-item label="盘点单号">{{ detailOrder.checkNo }}</el-descriptions-item>
        <el-descriptions-item label="仓库">{{ detailOrder.warehouseName }}</el-descriptions-item>
        <el-descriptions-item label="盘点类型">
          <span v-if="detailOrder.checkType === 'full'">全盘</span>
          <span v-else-if="detailOrder.checkType === 'cycle'">循环盘</span>
          <span v-else-if="detailOrder.checkType === 'spot'">抽盘</span>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(detailOrder.checkStatus)">{{ getStatusLabel(detailOrder.checkStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="物料总数">{{ detailOrder.totalItems }}</el-descriptions-item>
        <el-descriptions-item label="差异数">{{ detailOrder.diffItems }}</el-descriptions-item>
        <el-descriptions-item label="盘点人">{{ detailOrder.checkerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailOrder.createTime ? $moment(detailOrder.createTime).format('YYYY-MM-DD HH:mm') : '' }}</el-descriptions-item>
      </el-descriptions>

      <el-table :data="detailList" border size="small" max-height="450">
        <el-table-column label="序号" type="index" width="50" align="center" />
        <el-table-column label="物料编码" prop="matCode" width="120" align="center" />
        <el-table-column label="物料名称" prop="matName" width="140" align="center" />
        <el-table-column label="货位" prop="locationCode" width="100" align="center" />
        <el-table-column label="批次" prop="batch" width="160" align="center" />
        <el-table-column label="系统数量" prop="systemQty" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.systemQty }}</span>
          </template>
        </el-table-column>
        <el-table-column label="实盘数量" prop="actualQty" width="120" align="center">
          <template slot-scope="scope">
            <el-input-number v-if="isEditing" v-model="scope.row.actualQty" :min="0" :precision="4"
              size="small" controls-position="right" style="width:110px" />
            <span v-else>{{ scope.row.actualQty != null ? scope.row.actualQty : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="差异" prop="diffQty" width="90" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.diffQty != null" :style="{ color: scope.row.diffQty > 0 ? '#67c23a' : scope.row.diffQty < 0 ? '#f56c6c' : '' }">
              {{ scope.row.diffQty > 0 ? '+' : '' }}{{ scope.row.diffQty }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="差异原因" prop="diffReason" min-width="140">
          <template slot-scope="scope">
            <el-input v-if="isEditing" v-model="scope.row.diffReason" size="small" placeholder="差异原因" />
            <span v-else>{{ scope.row.diffReason || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已调整" prop="adjustFlag" width="70" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.adjustFlag === '1'" type="success" size="mini">是</el-tag>
            <el-tag v-else type="info" size="mini">否</el-tag>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button v-if="isEditing" type="primary" :loading="submitLoading" @click="submitCount">提交盘点结果</el-button>
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCheck, getCheck, addCheck, submitCheck, adjustCheck, delCheck } from "@/api/stock/check";
import { listAllWarehouse } from "@/api/base/warehouse";

export default {
  name: "StockCheck",
  dicts: ['base_mat_unit'],
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      checkList: [],
      warehouseList: [],
      queryParams: {
        pageNum: 1, pageSize: 10,
        checkNo: null, checkType: null, warehouseCode: null, checkStatus: null,
      },
      statusOptions: [
        { value: 'created', label: '已创建' },
        { value: 'counting', label: '盘点中' },
        { value: 'completed', label: '已完成' },
        { value: 'adjusted', label: '已调整' },
      ],
      // 创建对话框
      addOpen: false,
      addForm: {},
      addRules: {
        warehouseCode: [{ required: true, message: '请选择仓库', trigger: 'change' }],
        checkType: [{ required: true, message: '请选择盘点类型', trigger: 'change' }],
      },
      // 详情/录入对话框
      detailOpen: false,
      detailTitle: '',
      detailOrder: {},
      detailList: [],
      isEditing: false,
      submitLoading: false,
    };
  },
  created() {
    this.getList();
    this.loadWarehouseList();
  },
  methods: {
    getList() {
      this.loading = true;
      listCheck(this.queryParams).then(response => {
        this.checkList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    loadWarehouseList() {
      listAllWarehouse().then(response => {
        this.warehouseList = response;
      });
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList(); },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery(); },
    getStatusLabel(status) {
      const map = { created: '已创建', counting: '盘点中', completed: '已完成', adjusted: '已调整' };
      return map[status] || status;
    },
    getStatusType(status) {
      const map = { created: 'info', counting: 'warning', completed: 'success', adjusted: '' };
      return map[status] || 'info';
    },
    onWarehouseChange(code) {
      const wh = this.warehouseList.find(w => w.warehouseCode === code);
      this.addForm.warehouseName = wh ? wh.warehouseName : '';
    },
    /** 创建盘点单 */
    handleAdd() {
      this.addForm = { warehouseCode: null, warehouseName: '', checkType: 'full', planDate: null, remark: '' };
      this.addOpen = true;
      this.$nextTick(() => { if (this.$refs.addForm) this.$refs.addForm.clearValidate(); });
    },
    submitAdd() {
      this.$refs.addForm.validate(valid => {
        if (!valid) return;
        this.submitLoading = true;
        addCheck(this.addForm).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess(response.msg);
            this.addOpen = false;
            this.getList();
          } else {
            this.$modal.msgError(response.msg);
          }
        }).finally(() => { this.submitLoading = false; });
      });
    },
    /** 查看详情 */
    handleDetail(row) {
      this.isEditing = false;
      this.detailTitle = '盘点单详情 - ' + row.checkNo;
      getCheck(row.checkId).then(response => {
        this.detailOrder = response.data;
        this.detailList = response.data.detailList || [];
        this.detailOpen = true;
      });
    },
    /** 录入实盘数量 */
    handleCount(row) {
      this.isEditing = true;
      this.detailTitle = '录入盘点结果 - ' + row.checkNo;
      getCheck(row.checkId).then(response => {
        this.detailOrder = response.data;
        this.detailList = response.data.detailList || [];
        this.detailOpen = true;
      });
    },
    /** 提交盘点结果 */
    submitCount() {
      const unfilledCount = this.detailList.filter(d => d.actualQty == null).length;
      if (unfilledCount > 0) {
        this.$modal.confirm('还有 ' + unfilledCount + ' 项未录入实盘数量，未录入项将被跳过，是否继续？').then(() => {
          this.doSubmitCount();
        }).catch(() => {});
      } else {
        this.doSubmitCount();
      }
    },
    doSubmitCount() {
      this.submitLoading = true;
      const data = {
        checkId: this.detailOrder.checkId,
        detailList: this.detailList,
      };
      submitCheck(data).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess(response.msg);
          this.detailOpen = false;
          this.getList();
        } else {
          this.$modal.msgError(response.msg);
        }
      }).finally(() => { this.submitLoading = false; });
    },
    /** 执行盘点调整 */
    handleAdjust(row) {
      this.$modal.confirm('确认执行盘点调整？盘盈将自动入库，盘亏将自动出库。').then(() => {
        adjustCheck(row.checkId).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess(response.msg);
            this.getList();
          } else {
            this.$modal.msgError(response.msg);
          }
        });
      }).catch(() => {});
    },
    /** 删除 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除盘点单"' + row.checkNo + '"？').then(() => {
        return delCheck(row.checkId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('stock/check/export', { ...this.queryParams }, `check_${new Date().getTime()}.xlsx`);
    },
  }
};
</script>

