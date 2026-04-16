<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="设备编码" prop="equipmentCode">
        <el-input v-model="queryParams.equipmentCode" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="设备名称" prop="equipmentName">
        <el-input v-model="queryParams.equipmentName" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="设备类型" prop="equipmentType">
        <el-select v-model="queryParams.equipmentType" placeholder="请选择" clearable>
          <el-option v-for="item in equipmentTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="设备状态" prop="equipmentStatus">
        <el-select v-model="queryParams.equipmentStatus" placeholder="请选择" clearable>
          <el-option v-for="item in equipmentStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['base:equipment:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['base:equipment:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['base:equipment:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['base:equipment:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="equipmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="设备编码" align="center" prop="equipmentCode" width="130" />
      <el-table-column label="设备名称" align="center" prop="equipmentName" min-width="150" show-overflow-tooltip />
      <el-table-column label="设备类型" align="center" prop="equipmentType" width="100">
        <template slot-scope="scope">
          {{ getEquipmentTypeLabel(scope.row.equipmentType) }}
        </template>
      </el-table-column>
      <el-table-column label="所属车间" align="center" prop="workshopName" width="120" />
      <el-table-column label="日产能" align="center" width="120">
        <template slot-scope="scope">
          {{ scope.row.capacity ? scope.row.capacity + (scope.row.capacityUnit ? ' ' + scope.row.capacityUnit : '') : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="equipmentStatus" width="90">
        <template slot-scope="scope">
          <el-tag :type="getStatusTagType(scope.row.equipmentStatus)">{{ getEquipmentStatusLabel(scope.row.equipmentStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="上次维护" align="center" prop="lastMaintainDate" width="110" />
      <el-table-column label="维护周期" align="center" prop="maintainCycle" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.maintainCycle">{{ scope.row.maintainCycle }}天</span>
          <span v-else style="color: #909399;">未设置</span>
        </template>
      </el-table-column>
      <el-table-column label="下次维护" align="center" prop="nextMaintainDate" width="110">
        <template slot-scope="scope">
          <span :style="{ color: isOverdue(scope.row.nextMaintainDate) ? '#F56C6C' : '', fontWeight: isOverdue(scope.row.nextMaintainDate) ? 'bold' : '' }">
            {{ scope.row.nextMaintainDate || '-' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['base:equipment:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['base:equipment:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备编码" prop="equipmentCode">
              <el-input v-model="form.equipmentCode" :placeholder="form.equipmentId != null ? '' : '留空则系统自动生成'" :disabled="form.equipmentId != null" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备名称" prop="equipmentName">
              <el-input v-model="form.equipmentName" placeholder="请输入设备名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备类型" prop="equipmentType">
              <el-select v-model="form.equipmentType" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in equipmentTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
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
            <el-form-item label="日产能" prop="capacity">
              <el-input-number v-model="form.capacity" :min="0" :precision="2" style="width: 100%" placeholder="日产能" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产能单位" prop="capacityUnit">
              <el-select v-model="form.capacityUnit" placeholder="请选择" style="width: 100%" allow-create filterable>
                <el-option label="吨/天" value="吨/天" />
                <el-option label="片/天" value="片/天" />
                <el-option label="米/天" value="米/天" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备状态" prop="equipmentStatus">
              <el-select v-model="form.equipmentStatus" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in equipmentStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="购置日期" prop="purchaseDate">
              <el-date-picker v-model="form.purchaseDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="维护周期" prop="maintainCycle">
              <el-input-number v-model="form.maintainCycle" :min="0" :max="9999" :step="1" :precision="0" style="width: 100%" placeholder="天" @change="calcNextMaintainDate" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上次维护" prop="lastMaintainDate">
              <el-date-picker v-model="form.lastMaintainDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%" @change="calcNextMaintainDate" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="下次维护">
              <el-input :value="form.nextMaintainDate || '根据维护周期自动计算'" disabled style="width: 100%" />
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
import { listEquipment, getEquipment, addEquipment, updateEquipment, delEquipment } from "@/api/base/equipment";
import selectWorkshop from "../../components/select-workshop/index";

export default {
  name: "Equipment",
  components: { selectWorkshop },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      equipmentList: [],
      title: "",
      open: false,
      submitLoading: false,
      selectWorkshopOpen: false,
      equipmentTypeOptions: [
        { value: 'furnace', label: '窑炉' },
        { value: 'forming', label: '成型机' },
        { value: 'annealing', label: '退火窑' },
        { value: 'cutting', label: '切裁机' },
        { value: 'other', label: '其他' },
      ],
      equipmentStatusOptions: [
        { value: '0', label: '正常' },
        { value: '1', label: '维护中' },
        { value: '2', label: '故障' },
        { value: '3', label: '停用' },
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equipmentCode: null,
        equipmentName: null,
        equipmentType: null,
        equipmentStatus: null,
      },
      form: {},
      rules: {
        equipmentName: [{ required: true, message: "设备名称不能为空", trigger: "blur" }],
        equipmentType: [{ required: true, message: "请选择设备类型", trigger: "change" }],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listEquipment(this.queryParams).then(response => {
        this.equipmentList = response.rows;
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
        equipmentId: null,
        equipmentCode: null,
        equipmentName: null,
        equipmentType: null,
        workshopCode: null,
        capacity: null,
        capacityUnit: null,
        equipmentStatus: "0",
        purchaseDate: null,
        lastMaintainDate: null,
        nextMaintainDate: null,
        maintainCycle: null,
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
      this.ids = selection.map(item => item.equipmentId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增设备";
    },
    handleUpdate(row) {
      this.reset();
      const equipmentId = row.equipmentId || this.ids;
      getEquipment(equipmentId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.submitLoading) return;
          this.submitLoading = true;
          if (this.form.equipmentId != null) {
            updateEquipment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => { this.submitLoading = false; });
          } else {
            addEquipment(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => { this.submitLoading = false; });
          }
        }
      });
    },
    handleDelete(row) {
      const equipmentIds = row.equipmentId || this.ids;
      this.$modal.confirm('是否确认删除设备编号为"' + equipmentIds + '"的数据项？').then(function() {
        return delEquipment(equipmentIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('base/equipment/export', { ...this.queryParams }, `设备数据_${new Date().getTime()}.xlsx`);
    },
    confirmSelectWorkshop(row) {
      this.form.workshopCode = row.workshopCode;
      this.selectWorkshopOpen = false;
    },
    getEquipmentTypeLabel(val) {
      const item = this.equipmentTypeOptions.find(i => i.value === val);
      return item ? item.label : val || '-';
    },
    getEquipmentStatusLabel(val) {
      const item = this.equipmentStatusOptions.find(i => i.value === val);
      return item ? item.label : val || '-';
    },
    getStatusTagType(status) {
      const map = { '0': 'success', '1': 'warning', '2': 'danger', '3': 'info' };
      return map[status] || '';
    },
    isOverdue(dateStr) {
      if (!dateStr) return false;
      return new Date(dateStr) < new Date();
    },
    /** 根据维护周期和上次维护日期自动计算下次维护日期 */
    calcNextMaintainDate() {
      if (this.form.maintainCycle && this.form.maintainCycle > 0 && this.form.lastMaintainDate) {
        const base = new Date(this.form.lastMaintainDate);
        base.setDate(base.getDate() + this.form.maintainCycle);
        const y = base.getFullYear();
        const m = String(base.getMonth() + 1).padStart(2, '0');
        const d = String(base.getDate()).padStart(2, '0');
        this.form.nextMaintainDate = `${y}-${m}-${d}`;
      }
    },
  }
};
</script>

