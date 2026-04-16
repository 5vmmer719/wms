<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="任务编号" prop="taskNo">
        <el-input v-model="queryParams.taskNo" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="处理方式" prop="handleType">
        <el-select v-model="queryParams.handleType" placeholder="请选择" clearable>
          <el-option v-for="item in handleTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="处理状态" prop="handleStatus">
        <el-select v-model="queryParams.handleStatus" placeholder="请选择" clearable>
          <el-option v-for="item in handleStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['quality:defectHandle:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['quality:defectHandle:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['quality:defectHandle:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="handleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="检验任务编号" align="center" prop="taskNo" width="150" />
      <el-table-column label="物料编码" align="center" prop="matCode" width="120" />
      <el-table-column label="物料名称" align="center" prop="matName" min-width="120" :show-overflow-tooltip="true" />
      <el-table-column label="不合格数量" align="center" prop="unqualifiedQty" width="100" />
      <el-table-column label="处理方式" align="center" width="100">
        <template slot-scope="scope">{{ getHandleTypeLabel(scope.row.handleType) }}</template>
      </el-table-column>
      <el-table-column label="处理数量" align="center" prop="handleQty" width="100" />
      <el-table-column label="处理人" align="center" prop="handleBy" width="100" />
      <el-table-column label="处理日期" align="center" prop="handleDate" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.handleDate ? $moment(scope.row.handleDate).format('YYYY-MM-DD') : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="90">
        <template slot-scope="scope">
          <el-tag :type="getHandleStatusType(scope.row.handleStatus)" size="small">{{ scope.row.handleStatusLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)" v-hasPermi="['quality:defectHandle:query']">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['quality:defectHandle:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['quality:defectHandle:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="检验任务" prop="taskNo">
          <el-select v-model="form.taskNo" placeholder="请选择不合格的检验任务" style="width: 100%"
            :disabled="form.handleId != null" filterable @change="onTaskChange">
            <el-option v-for="item in failedTaskOptions" :key="item.taskNo"
              :label="item.taskNo + ' - ' + (item.matName || item.matCode || '')"
              :value="item.taskNo" />
          </el-select>
        </el-form-item>
        <!-- 选中任务后显示关联信息 -->
        <el-row v-if="form.taskNo" :gutter="10" style="margin-bottom: 10px;">
          <el-col :span="24">
            <el-alert type="info" :closable="false" show-icon>
              <template slot="title">
                物料：{{ selectedTask.matCode }} {{ selectedTask.matName }}
                &nbsp;|&nbsp; 送检数量：{{ selectedTask.quantity }}
                &nbsp;|&nbsp; 不合格数量：<span style="color: #F56C6C; font-weight: bold;">{{ selectedTask.unqualifiedQty }}</span>
              </template>
            </el-alert>
          </el-col>
        </el-row>
        <el-form-item label="处理方式" prop="handleType">
          <el-radio-group v-model="form.handleType">
            <el-radio-button v-for="item in handleTypeOptions" :key="item.value" :label="item.value">
              {{ item.label }}
            </el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="处理数量" prop="handleQty">
              <el-input-number v-model="form.handleQty" :min="0" :precision="4" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处理状态" prop="handleStatus">
              <el-select v-model="form.handleStatus" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in handleStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="处理人" prop="handleBy">
              <el-input v-model="form.handleBy" placeholder="请输入处理人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处理日期" prop="handleDate">
              <el-date-picker v-model="form.handleDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="处理说明" prop="handleDesc">
          <el-input v-model="form.handleDesc" type="textarea" :rows="3" placeholder="请输入处理说明（如：返工原因、报废数量确认等）" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="不合格品处理详情" :visible.sync="detailOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="检验任务编号">{{ detailData.taskNo }}</el-descriptions-item>
        <el-descriptions-item label="物料">{{ detailData.matCode }} {{ detailData.matName }}</el-descriptions-item>
        <el-descriptions-item label="不合格数量">{{ detailData.unqualifiedQty }}</el-descriptions-item>
        <el-descriptions-item label="处理方式">{{ getHandleTypeLabel(detailData.handleType) }}</el-descriptions-item>
        <el-descriptions-item label="处理数量">{{ detailData.handleQty }}</el-descriptions-item>
        <el-descriptions-item label="处理人">{{ detailData.handleBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理日期">{{ detailData.handleDate ? $moment(detailData.handleDate).format('YYYY-MM-DD') : '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ detailData.handleStatusLabel }}</el-descriptions-item>
        <el-descriptions-item label="处理说明" :span="2">{{ detailData.handleDesc || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDefectHandle, getDefectHandle, addDefectHandle, updateDefectHandle, delDefectHandle, listAllQualityTask } from "@/api/base/quality";

export default {
  name: "QualityDefectHandle",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      handleList: [],
      title: "",
      open: false,
      detailOpen: false,
      detailData: {},
      failedTaskOptions: [],
      selectedTask: {},
      submitLoading: false,
      handleTypeOptions: [
        { value: 'rework', label: '返工' },
        { value: 'scrap', label: '报废' },
        { value: 'concession', label: '让步接收' }
      ],
      handleStatusOptions: [
        { value: 'pending', label: '待处理' },
        { value: 'processing', label: '处理中' },
        { value: 'completed', label: '已完成' }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskNo: null,
        handleType: null,
        handleStatus: null,
      },
      form: {},
      rules: {
        taskNo: [{ required: true, message: "检验任务编号不能为空", trigger: "blur" }],
        handleType: [{ required: true, message: "处理方式不能为空", trigger: "change" }],
      },
    };
  },
  created() {
    this.getList();
    this.loadFailedTasks();
  },
  methods: {
    getList() {
      this.loading = true;
      listDefectHandle(this.queryParams).then(response => {
        this.handleList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    loadFailedTasks() {
      // 加载状态为"不合格"的检验任务，作为下拉选项
      listAllQualityTask({ taskStatus: 'failed' }).then(response => {
        this.failedTaskOptions = response;
      });
    },
    onTaskChange(taskNo) {
      const task = this.failedTaskOptions.find(t => t.taskNo === taskNo);
      if (task) {
        this.selectedTask = task;
        // 自动填充处理数量为不合格数量
        this.form.handleQty = task.unqualifiedQty;
      } else {
        this.selectedTask = {};
      }
    },
    getHandleTypeLabel(value) {
      const item = this.handleTypeOptions.find(i => i.value === value);
      return item ? item.label : value || '-';
    },
    getHandleStatusType(status) {
      const map = { pending: 'info', processing: 'warning', completed: 'success' };
      return map[status] || 'info';
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        handleId: null,
        taskNo: null,
        handleType: null,
        handleQty: null,
        handleDesc: null,
        handleBy: null,
        handleDate: null,
        handleStatus: 'pending',
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
      this.ids = selection.map(item => item.handleId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.selectedTask = {};
      this.loadFailedTasks();
      this.title = "新增不合格品处理";
      this.open = true;
    },
    handleUpdate(row) {
      this.reset();
      getDefectHandle(row.handleId).then(response => {
        this.form = response.data;
        this.title = "修改不合格品处理";
        this.open = true;
      });
    },
    handleDetail(row) {
      getDefectHandle(row.handleId).then(response => {
        this.detailData = response.data;
        this.detailOpen = true;
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.submitLoading) return;
          this.submitLoading = true;
          if (this.form.handleId != null) {
            updateDefectHandle(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => { this.submitLoading = false; });
          } else {
            addDefectHandle(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => { this.submitLoading = false; });
          }
        }
      });
    },
    handleDelete(row) {
      const handleIds = row.handleId ? [row.handleId] : this.ids;
      this.$modal.confirm('是否确认删除所选不合格品处理记录？').then(() => {
        return delDefectHandle(handleIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('quality/defectHandle/export', { ...this.queryParams }, `不合格品处理_${new Date().getTime()}.xlsx`);
    },
  }
};
</script>

