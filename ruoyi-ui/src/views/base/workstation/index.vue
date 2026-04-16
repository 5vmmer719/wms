<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="工位编码" prop="stationCode">
        <el-input v-model="queryParams.stationCode" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="工位名称" prop="stationName">
        <el-input v-model="queryParams.stationName" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="所属设备" prop="equipmentCode">
        <el-select v-model="queryParams.equipmentCode" placeholder="请选择" clearable filterable>
          <el-option v-for="item in equipmentOptions" :key="item.equipmentCode"
            :label="item.equipmentCode + ' - ' + item.equipmentName" :value="item.equipmentCode" />
        </el-select>
      </el-form-item>
      <el-form-item label="工位状态" prop="stationStatus">
        <el-select v-model="queryParams.stationStatus" placeholder="请选择" clearable>
          <el-option v-for="item in stationStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['base:workstation:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['base:workstation:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['base:workstation:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['base:workstation:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="workstationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工位编码" align="center" prop="stationCode" width="130" />
      <el-table-column label="工位名称" align="center" prop="stationName" min-width="150" show-overflow-tooltip />
      <el-table-column label="所属设备" align="center" width="150">
        <template slot-scope="scope">
          {{ scope.row.equipmentName ? scope.row.equipmentCode + ' - ' + scope.row.equipmentName : (scope.row.equipmentCode || '-') }}
        </template>
      </el-table-column>
      <el-table-column label="所属车间" align="center" prop="workshopName" width="120" />
      <el-table-column label="操作员" align="center" prop="operatorName" width="100" />
      <el-table-column label="状态" align="center" prop="stationStatus" width="90">
        <template slot-scope="scope">
          <el-tag :type="getStationStatusTagType(scope.row.stationStatus)">{{ getStationStatusLabel(scope.row.stationStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['base:workstation:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['base:workstation:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工位编码" prop="stationCode">
              <el-input v-model="form.stationCode" :placeholder="form.stationId != null ? '' : '留空则系统自动生成'" :disabled="form.stationId != null" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工位名称" prop="stationName">
              <el-input v-model="form.stationName" placeholder="请输入工位名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属设备" prop="equipmentCode">
              <el-select v-model="form.equipmentCode" placeholder="请选择设备" style="width: 100%" filterable clearable>
                <el-option v-for="item in equipmentOptions" :key="item.equipmentCode"
                  :label="item.equipmentCode + ' - ' + item.equipmentName" :value="item.equipmentCode" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属车间" prop="workshopCode">
              <el-input v-model="form.workshopCode" readonly placeholder="请选择车间">
                <el-button slot="append" icon="el-icon-search" @click="selectWorkshopOpen = true"></el-button>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="操作员" prop="operatorName">
              <el-input v-model="form.operatorName" placeholder="请输入操作员姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工位状态" prop="stationStatus">
              <el-select v-model="form.stationStatus" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in stationStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 车间选择弹窗 -->
    <el-dialog title="选择车间" :visible.sync="selectWorkshopOpen" width="800px" append-to-body>
      <selectWorkshop @confirmSelect="confirmSelectWorkshop"></selectWorkshop>
    </el-dialog>
  </div>
</template>

<script>
import { listWorkstation, getWorkstation, addWorkstation, updateWorkstation, delWorkstation, listAllEquipment } from "@/api/base/equipment";
import selectWorkshop from "../../components/select-workshop/index";

export default {
  name: "Workstation",
  components: { selectWorkshop },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      workstationList: [],
      title: "",
      open: false,
      submitLoading: false,
      selectWorkshopOpen: false,
      equipmentOptions: [],
      stationStatusOptions: [
        { value: '0', label: '空闲' },
        { value: '1', label: '生产中' },
        { value: '2', label: '维护中' },
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stationCode: null,
        stationName: null,
        equipmentCode: null,
        stationStatus: null,
      },
      form: {},
      rules: {
        stationName: [{ required: true, message: "工位名称不能为空", trigger: "blur" }],
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
      listWorkstation(this.queryParams).then(response => {
        this.workstationList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    loadEquipmentOptions() {
      listAllEquipment({ equipmentStatus: '0' }).then(response => {
        this.equipmentOptions = response;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        stationId: null,
        stationCode: null,
        stationName: null,
        equipmentCode: null,
        workshopCode: null,
        operatorId: null,
        operatorName: null,
        stationStatus: "0",
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
      this.ids = selection.map(item => item.stationId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增工位";
    },
    handleUpdate(row) {
      this.reset();
      const stationId = row.stationId || this.ids;
      getWorkstation(stationId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改工位";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.submitLoading) return;
          this.submitLoading = true;
          if (this.form.stationId != null) {
            updateWorkstation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => { this.submitLoading = false; });
          } else {
            addWorkstation(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => { this.submitLoading = false; });
          }
        }
      });
    },
    handleDelete(row) {
      const stationIds = row.stationId || this.ids;
      this.$modal.confirm('是否确认删除工位编号为"' + stationIds + '"的数据项？').then(function() {
        return delWorkstation(stationIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('base/workstation/export', { ...this.queryParams }, `工位数据_${new Date().getTime()}.xlsx`);
    },
    confirmSelectWorkshop(row) {
      this.form.workshopCode = row.workshopCode;
      this.selectWorkshopOpen = false;
    },
    getStationStatusLabel(val) {
      const item = this.stationStatusOptions.find(i => i.value === val);
      return item ? item.label : val || '-';
    },
    getStationStatusTagType(status) {
      const map = { '0': 'success', '1': 'primary', '2': 'warning' };
      return map[status] || 'info';
    },
  }
};
</script>

