<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="维护单号" prop="maintainNo">
        <el-input v-model="queryParams.maintainNo" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="设备" prop="equipmentCode">
        <el-select v-model="queryParams.equipmentCode" placeholder="请选择" clearable filterable>
          <el-option v-for="item in equipmentOptions" :key="item.equipmentCode"
            :label="item.equipmentCode + ' - ' + item.equipmentName" :value="item.equipmentCode" />
        </el-select>
      </el-form-item>
      <el-form-item label="维护类型" prop="maintainType">
        <el-select v-model="queryParams.maintainType" placeholder="请选择" clearable>
          <el-option v-for="item in maintainTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="maintainStatus">
        <el-select v-model="queryParams.maintainStatus" placeholder="请选择" clearable>
          <el-option v-for="item in maintainStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['base:maintain:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['base:maintain:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['base:maintain:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['base:maintain:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="maintainList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="维护单号" align="center" prop="maintainNo" width="150" />
      <el-table-column label="设备" align="center" width="180">
        <template slot-scope="scope">
          {{ scope.row.equipmentName ? scope.row.equipmentCode + ' - ' + scope.row.equipmentName : (scope.row.equipmentCode || '-') }}
        </template>
      </el-table-column>
      <el-table-column label="维护类型" align="center" prop="maintainType" width="100">
        <template slot-scope="scope">
          {{ getMaintainTypeLabel(scope.row.maintainType) }}
        </template>
      </el-table-column>
      <el-table-column label="维护日期" align="center" prop="maintainDate" width="110" />
      <el-table-column label="结束日期" align="center" prop="maintainEndDate" width="110" />
      <el-table-column label="维护人员" align="center" prop="maintainBy" width="100" />
      <el-table-column label="耗时(小时)" align="center" prop="maintainHours" width="100" />
      <el-table-column label="费用" align="center" prop="maintainCost" width="100" />
      <el-table-column label="状态" align="center" prop="maintainStatus" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.maintainStatus === '1' ? 'success' : 'warning'">
            {{ scope.row.maintainStatus === '1' ? '已完成' : '进行中' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="来源" align="center" prop="source" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.source === 'auto' ? 'primary' : 'info'" size="small">
            {{ scope.row.source === 'auto' ? '自动' : '手动' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250">
        <template slot-scope="scope">
          <el-button v-if="scope.row.maintainStatus === '0'" size="mini" type="text" icon="el-icon-check"
            @click="handleComplete(scope.row)" v-hasPermi="['base:maintain:edit']">完成维护</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['base:maintain:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['base:maintain:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备" prop="equipmentCode">
              <el-select v-model="form.equipmentCode" placeholder="请选择设备" style="width: 100%" filterable
                :disabled="form.maintainId != null" @change="onEquipmentChange">
                <el-option v-for="item in allEquipmentOptions" :key="item.equipmentCode"
                  :label="item.equipmentCode + ' - ' + item.equipmentName" :value="item.equipmentCode" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维护类型" prop="maintainType">
              <el-select v-model="form.maintainType" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in maintainTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="维护日期" prop="maintainDate">
              <el-date-picker v-model="form.maintainDate" type="date" value-format="yyyy-MM-dd" placeholder="选择维护日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维护人员" prop="maintainBy">
              <el-input v-model="form.maintainBy" placeholder="请输入维护人员" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="耗时(小时)" prop="maintainHours">
              <el-input-number v-model="form.maintainHours" :min="0" :precision="1" style="width: 100%" placeholder="预计耗时" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维护费用" prop="maintainCost">
              <el-input-number v-model="form.maintainCost" :min="0" :precision="2" style="width: 100%" placeholder="维护费用" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="维护内容" prop="maintainDesc">
          <el-input v-model="form.maintainDesc" type="textarea" :rows="3" placeholder="请输入维护内容描述" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMaintain, getMaintain, addMaintain, updateMaintain, delMaintain, completeMaintain, listAllEquipment } from "@/api/base/equipment";

export default {
  name: "EquipmentMaintain",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      maintainList: [],
      title: "",
      open: false,
      submitLoading: false,
      equipmentOptions: [],
      allEquipmentOptions: [],
      maintainTypeOptions: [
        { value: 'routine', label: '例行保养' },
        { value: 'repair', label: '维修' },
        { value: 'overhaul', label: '大修' },
      ],
      maintainStatusOptions: [
        { value: '0', label: '进行中' },
        { value: '1', label: '已完成' },
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        maintainNo: null,
        equipmentCode: null,
        maintainType: null,
        maintainStatus: null,
      },
      form: {},
      rules: {
        equipmentCode: [{ required: true, message: "请选择设备", trigger: "change" }],
        maintainType: [{ required: true, message: "请选择维护类型", trigger: "change" }],
        maintainDate: [{ required: true, message: "请选择维护日期", trigger: "change" }],
      },
    };
  },
  created() {
    this.getList();
    this.loadEquipmentOptions();
  },
  methods: {
    getList() {
      this.loading = true;
      listMaintain(this.queryParams).then(response => {
        this.maintainList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    loadEquipmentOptions() {
      // 搜索用：所有设备
      listAllEquipment({}).then(response => {
        this.allEquipmentOptions = response;
        // 筛选用：只显示正常状态的设备
        this.equipmentOptions = response;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        maintainId: null,
        maintainNo: null,
        equipmentCode: null,
        equipmentName: null,
        maintainType: 'routine',
        maintainDate: null,
        maintainEndDate: null,
        maintainDesc: null,
        maintainBy: null,
        maintainHours: null,
        maintainCost: null,
        maintainStatus: "0",
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
      this.ids = selection.map(item => item.maintainId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增维护记录";
    },
    handleUpdate(row) {
      this.reset();
      const maintainId = row.maintainId || this.ids;
      getMaintain(maintainId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改维护记录";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.submitLoading) return;
          this.submitLoading = true;
          if (this.form.maintainId != null) {
            updateMaintain(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => { this.submitLoading = false; });
          } else {
            addMaintain(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
              this.loadEquipmentOptions();
            }).finally(() => { this.submitLoading = false; });
          }
        }
      });
    },
    handleComplete(row) {
      this.$modal.confirm('是否确认完成维护单号为"' + row.maintainNo + '"的维护记录？完成后设备状态将恢复为正常。').then(() => {
        return completeMaintain(row.maintainId);
      }).then(() => {
        this.getList();
        this.loadEquipmentOptions();
        this.$modal.msgSuccess("维护已完成，设备状态已恢复正常");
      }).catch(() => {});
    },
    handleDelete(row) {
      const maintainIds = row.maintainId || this.ids;
      this.$modal.confirm('是否确认删除维护单号为"' + maintainIds + '"的数据项？').then(function() {
        return delMaintain(maintainIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('base/maintain/export', { ...this.queryParams }, `设备维护记录_${new Date().getTime()}.xlsx`);
    },
    onEquipmentChange(val) {
      const eq = this.allEquipmentOptions.find(e => e.equipmentCode === val);
      if (eq) {
        this.form.equipmentName = eq.equipmentName;
      }
    },
    getMaintainTypeLabel(val) {
      const item = this.maintainTypeOptions.find(i => i.value === val);
      return item ? item.label : val || '-';
    },
  }
};
</script>

