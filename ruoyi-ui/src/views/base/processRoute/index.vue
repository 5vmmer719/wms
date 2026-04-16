<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="路线编码" prop="routeCode">
        <el-input v-model="queryParams.routeCode" placeholder="请输入工艺路线编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="路线名称" prop="routeName">
        <el-input v-model="queryParams.routeName" placeholder="请输入工艺路线名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="routeStatus">
        <el-select v-model="queryParams.routeStatus" placeholder="请选择" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['base:processRoute:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['base:processRoute:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['base:processRoute:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['base:processRoute:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 工艺路线表格 -->
    <el-table v-loading="loading" :data="routeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="路线编码" align="center" prop="routeCode" width="120" />
      <el-table-column label="路线名称" align="center" prop="routeName" min-width="160" />
      <el-table-column label="关联物料" align="center" prop="matCode" width="120" />
      <el-table-column label="物料名称" align="center" prop="matName" width="140" />
      <el-table-column label="版本号" align="center" prop="routeVersion" width="80" />
      <el-table-column label="状态" align="center" prop="routeStatus" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.routeStatus === '0' ? 'success' : 'danger'" size="small">
            {{ scope.row.routeStatus === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" min-width="160" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">工序</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['base:processRoute:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['base:processRoute:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/修改工艺路线对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="路线编码" prop="routeCode">
          <el-input v-model="form.routeCode" :placeholder="form.routeId != null ? '' : '留空则系统自动生成'" :disabled="form.routeId != null" />
        </el-form-item>
        <el-form-item label="路线名称" prop="routeName">
          <el-input v-model="form.routeName" placeholder="请输入工艺路线名称" />
        </el-form-item>
        <el-form-item label="关联物料" prop="matCode">
          <el-input v-model="form.matCode" placeholder="请输入关联产品物料编码（可选）" />
        </el-form-item>
        <el-form-item label="版本号" prop="routeVersion">
          <el-input v-model="form.routeVersion" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="状态" prop="routeStatus">
          <el-radio-group v-model="form.routeStatus">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 工序管理对话框 -->
    <el-dialog :title="'工序管理 - ' + currentRouteCode" :visible.sync="stepDialogOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddStep" v-hasPermi="['base:processRoute:add']">新增工序</el-button>
        </el-col>
      </el-row>

      <el-table v-loading="stepLoading" :data="stepList">
        <el-table-column label="序号" align="center" prop="stepNo" width="60" />
        <el-table-column label="工序编码" align="center" prop="stepCode" width="100" />
        <el-table-column label="工序名称" align="center" prop="stepName" width="120" />
        <el-table-column label="工序类型" align="center" prop="stepType" width="100">
          <template slot-scope="scope">
            <span>{{ stepTypeMap[scope.row.stepType] || scope.row.stepType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="标准工时(h)" align="center" prop="standardHours" width="100" />
        <el-table-column label="备注" align="center" prop="remark" min-width="160" :show-overflow-tooltip="true" />
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-setting" @click="handleParamDetail(scope.row)">参数</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateStep(scope.row)" v-hasPermi="['base:processRoute:edit']">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteStep(scope.row)" v-hasPermi="['base:processRoute:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="stepDialogOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 新增/修改工序对话框 -->
    <el-dialog :title="stepTitle" :visible.sync="stepFormOpen" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="stepForm" :model="stepForm" :rules="stepRules" label-width="100px">
        <el-form-item label="工序序号" prop="stepNo">
          <el-input-number v-model="stepForm.stepNo" :min="1" :step="1" />
        </el-form-item>
        <el-form-item label="工序编码" prop="stepCode">
          <el-input v-model="stepForm.stepCode" placeholder="请输入工序编码" />
        </el-form-item>
        <el-form-item label="工序名称" prop="stepName">
          <el-input v-model="stepForm.stepName" placeholder="请输入工序名称（如：熔制、成型、退火、切割）" />
        </el-form-item>
        <el-form-item label="工序类型" prop="stepType">
          <el-select v-model="stepForm.stepType" placeholder="请选择工序类型">
            <el-option label="熔制" value="melting" />
            <el-option label="成型" value="forming" />
            <el-option label="退火" value="annealing" />
            <el-option label="切割" value="cutting" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="标准工时(h)" prop="standardHours">
          <el-input-number v-model="stepForm.standardHours" :min="0" :precision="2" :step="0.5" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="stepForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="stepSubmitLoading" @click="submitStepForm">确 定</el-button>
        <el-button @click="stepFormOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 工艺参数管理对话框 -->
    <el-dialog :title="'工艺参数 - ' + currentStepName" :visible.sync="paramDialogOpen" width="1100px" append-to-body :close-on-click-modal="false">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddParam" v-hasPermi="['base:processRoute:add']">新增参数</el-button>
        </el-col>
      </el-row>

      <el-table v-loading="paramLoading" :data="paramList">
        <el-table-column label="参数名称" align="center" prop="paramName" width="140" />
        <el-table-column label="单位" align="center" prop="paramUnit" width="80" />
        <el-table-column label="标准值" align="center" prop="standardValue" width="100" />
        <el-table-column label="下限值" align="center" prop="minValue" width="100" />
        <el-table-column label="上限值" align="center" prop="maxValue" width="100" />
        <el-table-column label="关键参数" align="center" prop="isKey" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isKey === '1' ? 'danger' : 'info'" size="small">
              {{ scope.row.isKey === '1' ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" min-width="120" :show-overflow-tooltip="true" />
        <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateParam(scope.row)" v-hasPermi="['base:processRoute:edit']">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteParam(scope.row)" v-hasPermi="['base:processRoute:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="paramDialogOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 新增/修改工艺参数对话框 -->
    <el-dialog :title="paramTitle" :visible.sync="paramFormOpen" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="paramForm" :model="paramForm" :rules="paramRules" label-width="100px">
        <el-form-item label="参数名称" prop="paramName">
          <el-input v-model="paramForm.paramName" placeholder="请输入参数名称（如：熔制温度、拉引速度）" />
        </el-form-item>
        <el-form-item label="单位" prop="paramUnit">
          <el-input v-model="paramForm.paramUnit" placeholder="请输入单位（如：℃、m/min、mm）" />
        </el-form-item>
        <el-form-item label="标准值" prop="standardValue">
          <el-input v-model="paramForm.standardValue" placeholder="请输入标准值" />
        </el-form-item>
        <el-form-item label="下限值" prop="minValue">
          <el-input-number v-model="paramForm.minValue" :precision="4" :step="1" :controls="false" style="width: 100%" />
        </el-form-item>
        <el-form-item label="上限值" prop="maxValue">
          <el-input-number v-model="paramForm.maxValue" :precision="4" :step="1" :controls="false" style="width: 100%" />
        </el-form-item>
        <el-form-item label="关键参数" prop="isKey">
          <el-radio-group v-model="paramForm.isKey">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="paramForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="paramSubmitLoading" @click="submitParamForm">确 定</el-button>
        <el-button @click="paramFormOpen = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  listProcessRoute, getProcessRoute, addProcessRoute, updateProcessRoute, delProcessRoute,
  listProcessStep, addProcessStep, updateProcessStep, delProcessStep,
  listProcessParam, addProcessParam, updateProcessParam, delProcessParam
} from "@/api/base/processRoute";

export default {
  name: "ProcessRoute",
  data() {
    return {
      // ===== 工艺路线 =====
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      routeList: [],
      title: "",
      open: false,
      submitLoading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        routeCode: null,
        routeName: null,
        routeStatus: null,
      },
      form: {},
      rules: {
        routeName: [{ required: true, message: "工艺路线名称不能为空", trigger: "blur" }],
      },

      // ===== 工序 =====
      stepDialogOpen: false,
      stepLoading: false,
      stepList: [],
      currentRouteCode: '',
      stepTitle: '',
      stepFormOpen: false,
      stepSubmitLoading: false,
      stepForm: {},
      stepRules: {
        stepNo: [{ required: true, message: "工序序号不能为空", trigger: "blur" }],
        stepCode: [{ required: true, message: "工序编码不能为空", trigger: "blur" }],
        stepName: [{ required: true, message: "工序名称不能为空", trigger: "blur" }],
      },
      stepTypeMap: {
        'melting': '熔制',
        'forming': '成型',
        'annealing': '退火',
        'cutting': '切割',
        'other': '其他',
      },

      // ===== 工艺参数 =====
      paramDialogOpen: false,
      paramLoading: false,
      paramList: [],
      currentStepId: null,
      currentStepName: '',
      paramTitle: '',
      paramFormOpen: false,
      paramSubmitLoading: false,
      paramForm: {},
      paramRules: {
        paramName: [{ required: true, message: "参数名称不能为空", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // ==================== 工艺路线方法 ====================
    /** 查询工艺路线列表 */
    getList() {
      this.loading = true;
      listProcessRoute(this.queryParams).then(response => {
        this.routeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        routeId: null,
        routeCode: null,
        routeName: null,
        matCode: null,
        routeVersion: 'V1.0',
        routeStatus: '0',
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
      this.ids = selection.map(item => item.routeId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增工艺路线";
    },
    handleUpdate(row) {
      this.reset();
      const routeId = row.routeId || this.ids;
      getProcessRoute(routeId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改工艺路线";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.submitLoading = true;
          if (this.form.routeId != null) {
            updateProcessRoute(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => { this.submitLoading = false; });
          } else {
            addProcessRoute(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => { this.submitLoading = false; });
          }
        }
      });
    },
    handleDelete(row) {
      const routeIds = row.routeId || this.ids;
      this.$modal.confirm('删除工艺路线将同时删除其下所有工序和参数，是否确认？').then(function() {
        return delProcessRoute(routeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('base/processRoute/export', { ...this.queryParams }, `processRoute_${new Date().getTime()}.xlsx`);
    },

    // ==================== 工序方法 ====================
    /** 查看工序详情 */
    handleDetail(row) {
      this.currentRouteCode = row.routeCode;
      this.stepDialogOpen = true;
      this.getStepList();
    },
    getStepList() {
      this.stepLoading = true;
      listProcessStep({ routeCode: this.currentRouteCode, pageNum: 1, pageSize: 100 }).then(response => {
        this.stepList = response.rows;
        this.stepLoading = false;
      });
    },
    resetStepForm() {
      this.stepForm = {
        stepId: null,
        routeCode: this.currentRouteCode,
        stepNo: this.stepList.length + 1,
        stepCode: null,
        stepName: null,
        stepType: null,
        standardHours: null,
        remark: null,
      };
    },
    handleAddStep() {
      this.resetStepForm();
      this.stepFormOpen = true;
      this.stepTitle = "新增工序";
    },
    handleUpdateStep(row) {
      this.stepForm = { ...row };
      this.stepFormOpen = true;
      this.stepTitle = "修改工序";
    },
    submitStepForm() {
      this.$refs["stepForm"].validate(valid => {
        if (valid) {
          this.stepSubmitLoading = true;
          if (this.stepForm.stepId != null) {
            updateProcessStep(this.stepForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.stepFormOpen = false;
              this.getStepList();
            }).finally(() => { this.stepSubmitLoading = false; });
          } else {
            addProcessStep(this.stepForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.stepFormOpen = false;
              this.getStepList();
            }).finally(() => { this.stepSubmitLoading = false; });
          }
        }
      });
    },
    handleDeleteStep(row) {
      this.$modal.confirm('删除工序将同时删除其下所有工艺参数，是否确认？').then(function() {
        return delProcessStep(row.stepId);
      }).then(() => {
        this.getStepList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    // ==================== 工艺参数方法 ====================
    /** 查看工艺参数 */
    handleParamDetail(row) {
      this.currentStepId = row.stepId;
      this.currentStepName = row.stepName + '(' + row.stepCode + ')';
      this.paramDialogOpen = true;
      this.getParamList();
    },
    getParamList() {
      this.paramLoading = true;
      listProcessParam({ stepId: this.currentStepId, pageNum: 1, pageSize: 100 }).then(response => {
        this.paramList = response.rows;
        this.paramLoading = false;
      });
    },
    resetParamForm() {
      this.paramForm = {
        paramId: null,
        stepId: this.currentStepId,
        paramName: null,
        paramUnit: null,
        standardValue: null,
        minValue: null,
        maxValue: null,
        isKey: '0',
        remark: null,
      };
    },
    handleAddParam() {
      this.resetParamForm();
      this.paramFormOpen = true;
      this.paramTitle = "新增工艺参数";
    },
    handleUpdateParam(row) {
      this.paramForm = { ...row };
      this.paramFormOpen = true;
      this.paramTitle = "修改工艺参数";
    },
    submitParamForm() {
      this.$refs["paramForm"].validate(valid => {
        if (valid) {
          this.paramSubmitLoading = true;
          if (this.paramForm.paramId != null) {
            updateProcessParam(this.paramForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.paramFormOpen = false;
              this.getParamList();
            }).finally(() => { this.paramSubmitLoading = false; });
          } else {
            addProcessParam(this.paramForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.paramFormOpen = false;
              this.getParamList();
            }).finally(() => { this.paramSubmitLoading = false; });
          }
        }
      });
    },
    handleDeleteParam(row) {
      this.$modal.confirm('是否确认删除该工艺参数？').then(function() {
        return delProcessParam(row.paramId);
      }).then(() => {
        this.getParamList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
  }
};
</script>

