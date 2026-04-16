<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="任务编号" prop="taskNo">
        <el-input v-model="queryParams.taskNo" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="检验类型" prop="checkType">
        <el-select v-model="queryParams.checkType" placeholder="请选择" clearable>
          <el-option v-for="item in checkTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="任务状态" prop="taskStatus">
        <el-select v-model="queryParams.taskStatus" placeholder="请选择" clearable>
          <el-option v-for="item in taskStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="来源单号" prop="sourceNo">
        <el-input v-model="queryParams.sourceNo" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['quality:task:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['quality:task:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['quality:task:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="taskList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务编号" align="center" prop="taskNo" width="150" />
      <el-table-column label="检验类型" align="center" width="100">
        <template slot-scope="scope">{{ getCheckTypeLabel(scope.row.checkType) }}</template>
      </el-table-column>
      <el-table-column label="来源单号" align="center" prop="sourceNo" width="150" />
      <el-table-column label="物料编码" align="center" prop="matCode" width="120" />
      <el-table-column label="物料名称" align="center" prop="matName" min-width="120" :show-overflow-tooltip="true" />
      <el-table-column label="送检数量" align="center" prop="quantity" width="100" />
      <el-table-column label="合格数量" align="center" prop="qualifiedQty" width="100">
        <template slot-scope="scope">
          <span :style="{ color: scope.row.qualifiedQty > 0 ? '#67C23A' : '' }">{{ scope.row.qualifiedQty != null ? scope.row.qualifiedQty : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="不合格数量" align="center" prop="unqualifiedQty" width="100">
        <template slot-scope="scope">
          <span :style="{ color: scope.row.unqualifiedQty > 0 ? '#F56C6C' : '', fontWeight: scope.row.unqualifiedQty > 0 ? 'bold' : '' }">{{ scope.row.unqualifiedQty != null ? scope.row.unqualifiedQty : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="检验标准" align="center" prop="standardName" width="150" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.taskStatus)" size="small">{{ scope.row.taskStatusLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="质检员" align="center" prop="inspectorName" width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime ? $moment(scope.row.createTime).format('YYYY-MM-DD HH:mm') : '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button v-if="scope.row.taskStatus === 'pending' || scope.row.taskStatus === 'checking'"
            size="mini" type="text" icon="el-icon-edit-outline" @click="handleCheck(scope.row)" v-hasPermi="['quality:task:check']">检验</el-button>
          <el-button v-if="scope.row.taskStatus === 'passed' || scope.row.taskStatus === 'failed'"
            size="mini" type="text" icon="el-icon-view" @click="handleCheck(scope.row)" v-hasPermi="['quality:task:query']">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['quality:task:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增检验任务对话框 -->
    <el-dialog title="新增检验任务" :visible.sync="addOpen" width="700px" append-to-body :close-on-click-modal="false">
      <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="检验类型" prop="checkType">
              <el-select v-model="addForm.checkType" placeholder="请选择" style="width: 100%" @change="onCheckTypeChange">
                <el-option v-for="item in checkTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="来源类型" prop="sourceType">
              <el-select v-model="addForm.sourceType" placeholder="请选择" style="width: 100%">
                <el-option label="入库单" value="in_order" />
                <el-option label="生产工单" value="prod_order" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="来源单号" prop="sourceNo">
              <el-input v-model="addForm.sourceNo" placeholder="请输入来源单号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="送检数量" prop="quantity">
              <el-input-number v-model="addForm.quantity" :min="0" :precision="4" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="物料编码" prop="matCode">
              <el-input v-model="addForm.matCode" placeholder="请输入物料编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物料名称" prop="matName">
              <el-input v-model="addForm.matName" placeholder="请输入物料名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="批次" prop="batch">
              <el-input v-model="addForm.batch" placeholder="请输入批次" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检验标准" prop="standardCode">
              <el-select v-model="addForm.standardCode" placeholder="请选择检验标准" style="width: 100%" @change="onStandardChange" filterable>
                <el-option v-for="item in standardOptions" :key="item.standardCode" :label="item.standardName" :value="item.standardCode" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="addForm.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAddForm">确 定</el-button>
        <el-button @click="addOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 检验/详情对话框 -->
    <el-dialog :title="checkDialogTitle" :visible.sync="checkOpen" width="1100px" append-to-body :close-on-click-modal="false">
      <el-descriptions :column="3" border size="small" style="margin-bottom: 15px">
        <el-descriptions-item label="任务编号">{{ checkData.taskNo }}</el-descriptions-item>
        <el-descriptions-item label="检验类型">{{ getCheckTypeLabel(checkData.checkType) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(checkData.taskStatus)" size="small">{{ checkData.taskStatusLabel }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="来源单号">{{ checkData.sourceNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="物料">{{ checkData.matCode }} {{ checkData.matName }}</el-descriptions-item>
        <el-descriptions-item label="送检数量">{{ checkData.quantity }}</el-descriptions-item>
        <el-descriptions-item label="检验标准">{{ checkData.standardName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="质检员">{{ checkData.inspectorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="检验时间">{{ checkData.checkTime ? $moment(checkData.checkTime).format('YYYY-MM-DD HH:mm') : '-' }}</el-descriptions-item>
      </el-descriptions>

      <!-- 检验结果录入/查看 -->
      <el-divider content-position="left">检验结果明细</el-divider>
      <el-table :data="checkData.resultList || []" size="small" style="width: 100%">
        <el-table-column label="序号" align="center" prop="itemNo" width="60" />
        <el-table-column label="检验项" align="center" prop="itemName" min-width="120" />
        <el-table-column label="标准值" align="center" prop="standardValue" width="100" />
        <el-table-column label="下限" align="center" prop="minValue" width="80" />
        <el-table-column label="上限" align="center" prop="maxValue" width="80" />
        <el-table-column label="实测值" align="center" width="120">
          <template slot-scope="scope">
            <el-input v-if="isEditable" v-model="scope.row.actualValue" placeholder="请输入" size="small" />
            <span v-else>{{ scope.row.actualValue || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="判定" align="center" width="80">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.judgeResult === '0'" type="success" size="mini">合格</el-tag>
            <el-tag v-else-if="scope.row.judgeResult === '1'" type="danger" size="mini">不合格</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="缺陷类型" align="center" width="120">
          <template slot-scope="scope">
            <el-input v-if="isEditable && scope.row.judgeResult === '1'" v-model="scope.row.defectType" placeholder="缺陷类型" size="small" />
            <span v-else>{{ scope.row.defectType || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="缺陷等级" align="center" width="110">
          <template slot-scope="scope">
            <el-select v-if="isEditable && scope.row.judgeResult === '1'" v-model="scope.row.defectLevel" placeholder="等级" size="small">
              <el-option label="轻微" value="minor" />
              <el-option label="严重" value="major" />
              <el-option label="致命" value="critical" />
            </el-select>
            <span v-else>{{ getDefectLevelLabel(scope.row.defectLevel) }}</span>
          </template>
        </el-table-column>
      </el-table>

      <!-- 合格/不合格数量录入区域 -->
      <el-card shadow="never" style="margin-top: 15px" v-if="isEditable || checkData.taskStatus === 'passed' || checkData.taskStatus === 'failed'">
        <div slot="header" style="font-weight: bold; font-size: 14px;">
          <i class="el-icon-s-claim"></i> 数量判定
          <el-button v-if="isEditable" type="text" size="small" style="float: right; margin-top: -2px;" @click="autoCalcQty">
            <i class="el-icon-refresh"></i> 根据检验项自动计算
          </el-button>
        </div>
        <el-row :gutter="40">
          <el-col :span="8">
            <el-form-item label="送检数量" label-width="90px">
              <span style="font-size: 16px; font-weight: bold;">{{ checkData.quantity }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合格数量" label-width="90px">
              <el-input-number v-if="isEditable" v-model="checkQualifiedQty" :min="0" :max="checkData.quantity" :precision="4" size="small" style="width: 160px" @change="onQualifiedQtyChange" />
              <span v-else style="font-size: 16px; font-weight: bold; color: #67C23A;">{{ checkData.qualifiedQty }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="不合格数量" label-width="100px">
              <el-input-number v-if="isEditable" v-model="checkUnqualifiedQty" :min="0" :max="checkData.quantity" :precision="4" size="small" style="width: 160px" @change="onUnqualifiedQtyChange" />
              <span v-else style="font-size: 16px; font-weight: bold; color: #F56C6C;">{{ checkData.unqualifiedQty }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-if="isEditable" @click="submitCheckResult" v-hasPermi="['quality:task:check']">提交检验结果</el-button>
        <el-button @click="checkOpen = false">{{ isEditable ? '取 消' : '关 闭' }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listQualityTask, getQualityTask, addQualityTask, delQualityTask, submitCheckResult } from "@/api/base/quality";
import { listAllQualityStandard } from "@/api/base/quality";

export default {
  name: "QualityTask",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      taskList: [],
      checkTypeOptions: [
        { value: 'incoming', label: '原料检验' },
        { value: 'process', label: '过程检验' },
        { value: 'final', label: '成品检验' }
      ],
      taskStatusOptions: [
        { value: 'pending', label: '待检验' },
        { value: 'checking', label: '检验中' },
        { value: 'passed', label: '合格' },
        { value: 'failed', label: '不合格' }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskNo: null,
        checkType: null,
        taskStatus: null,
        sourceNo: null,
      },
      // 新增任务
      addOpen: false,
      addForm: {},
      addRules: {
        checkType: [{ required: true, message: "检验类型不能为空", trigger: "change" }],
      },
      standardOptions: [],
      // 检验/详情
      checkOpen: false,
      checkDialogTitle: "",
      checkData: {},
      checkQualifiedQty: 0,
      checkUnqualifiedQty: 0,
    };
  },
  computed: {
    isEditable() {
      return this.checkData.taskStatus === 'pending' || this.checkData.taskStatus === 'checking';
    }
  },
  created() {
    this.getList();
    this.loadStandardOptions();
  },
  methods: {
    getList() {
      this.loading = true;
      listQualityTask(this.queryParams).then(response => {
        this.taskList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    loadStandardOptions() {
      listAllQualityStandard({ standardStatus: '0' }).then(response => {
        this.standardOptions = response;
      });
    },
    getCheckTypeLabel(value) {
      const item = this.checkTypeOptions.find(i => i.value === value);
      return item ? item.label : value;
    },
    getStatusType(status) {
      const map = { pending: 'info', checking: 'warning', passed: 'success', failed: 'danger' };
      return map[status] || 'info';
    },
    getDefectLevelLabel(level) {
      const map = { minor: '轻微', major: '严重', critical: '致命' };
      return map[level] || level || '-';
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
      this.ids = selection.map(item => item.taskId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.addForm = {
        checkType: null,
        sourceType: null,
        sourceNo: null,
        matCode: null,
        matName: null,
        batch: null,
        quantity: null,
        standardCode: null,
        remark: null,
      };
      this.addOpen = true;
    },
    onCheckTypeChange(val) {
      // 可根据类型过滤检验标准
    },
    onStandardChange(val) {
      const std = this.standardOptions.find(s => s.standardCode === val);
      if (std) {
        this.addForm.standardName = std.standardName;
      }
    },
    submitAddForm() {
      this.$refs["addForm"].validate(valid => {
        if (valid) {
          addQualityTask(this.addForm).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.addOpen = false;
            this.getList();
          });
        }
      });
    },
    handleCheck(row) {
      getQualityTask(row.taskId).then(response => {
        this.checkData = response.data;
        if (this.checkData.taskStatus === 'pending' || this.checkData.taskStatus === 'checking') {
          // 待检验状态：默认都为0，需要用户手动填写或点击"自动计算"
          this.checkQualifiedQty = this.checkData.qualifiedQty || 0;
          this.checkUnqualifiedQty = this.checkData.unqualifiedQty || 0;
        } else {
          // 已完成检验：显示已有数据
          this.checkQualifiedQty = this.checkData.qualifiedQty || 0;
          this.checkUnqualifiedQty = this.checkData.unqualifiedQty || 0;
        }
        this.checkDialogTitle = (this.checkData.taskStatus === 'pending' || this.checkData.taskStatus === 'checking') ? '检验结果录入' : '检验任务详情';
        this.checkOpen = true;
      });
    },
    submitCheckResult() {
      let that = this;
      // 提交前自动根据检验项结果修正数量
      const resultList = that.checkData.resultList || [];
      const hasResult = resultList.some(r => r.actualValue);
      if (!hasResult) {
        that.$modal.msgWarning("请至少录入一项检验结果的实测值");
        return;
      }
      // 检查是否有不合格项
      const hasFailedItem = resultList.some(r => {
        if (r.actualValue) {
          try {
            const actual = parseFloat(r.actualValue);
            if (r.minValue != null && actual < parseFloat(r.minValue)) return true;
            if (r.maxValue != null && actual > parseFloat(r.maxValue)) return true;
          } catch (e) { /* 非数值不参与 */ }
        }
        return false;
      });
      // 如果有不合格项但不合格数量为0，自动修正
      if (hasFailedItem && that.checkUnqualifiedQty <= 0) {
        that.checkUnqualifiedQty = that.checkData.quantity || 0;
        that.checkQualifiedQty = 0;
      }
      // 如果全部合格但合格数量为0，自动修正
      if (!hasFailedItem && that.checkQualifiedQty <= 0) {
        that.checkQualifiedQty = that.checkData.quantity || 0;
        that.checkUnqualifiedQty = 0;
      }
      that.$modal.confirm('是否确认提交检验结果？提交后将根据实测值自动判定合格/不合格。').then(function() {
        submitCheckResult({
          taskId: that.checkData.taskId,
          resultList: that.checkData.resultList,
          qualifiedQty: that.checkQualifiedQty,
          unqualifiedQty: that.checkUnqualifiedQty,
        }).then(response => {
          that.$modal.msgSuccess("检验结果提交成功");
          that.checkOpen = false;
          that.getList();
        });
      });
    },
    handleDelete(row) {
      const taskIds = row.taskId ? [row.taskId] : this.ids;
      this.$modal.confirm('是否确认删除所选检验任务？').then(() => {
        return delQualityTask(taskIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('quality/task/export', { ...this.queryParams }, `检验任务_${new Date().getTime()}.xlsx`);
    },
    /** 根据检验项结果自动计算合格/不合格数量 */
    autoCalcQty() {
      const resultList = this.checkData.resultList || [];
      const hasResult = resultList.some(r => r.actualValue);
      if (!hasResult) {
        this.$modal.msgWarning("请先录入检验结果的实测值");
        return;
      }
      // 统计有不合格项则整批不合格
      const hasFailedItem = resultList.some(r => {
        if (r.actualValue) {
          try {
            const actual = parseFloat(r.actualValue);
            if (r.minValue != null && actual < parseFloat(r.minValue)) return true;
            if (r.maxValue != null && actual > parseFloat(r.maxValue)) return true;
          } catch (e) { /* 非数值不参与自动计算 */ }
        }
        return false;
      });
      if (hasFailedItem) {
        // 有不合格项：默认不合格数量=送检数量，合格数量=0（用户可自行调整）
        this.checkUnqualifiedQty = this.checkData.quantity || 0;
        this.checkQualifiedQty = 0;
        this.$modal.msgWarning("存在不合格检验项，不合格数量已设为送检数量，请根据实际情况调整");
      } else {
        // 全部合格
        this.checkQualifiedQty = this.checkData.quantity || 0;
        this.checkUnqualifiedQty = 0;
        this.$modal.msgSuccess("所有检验项合格，合格数量已设为送检数量");
      }
    },
    /** 合格数量变化时自动计算不合格数量 */
    onQualifiedQtyChange(val) {
      const total = this.checkData.quantity || 0;
      this.checkUnqualifiedQty = Math.max(0, total - (val || 0));
    },
    /** 不合格数量变化时自动计算合格数量 */
    onUnqualifiedQtyChange(val) {
      const total = this.checkData.quantity || 0;
      this.checkQualifiedQty = Math.max(0, total - (val || 0));
    },
  }
};
</script>

