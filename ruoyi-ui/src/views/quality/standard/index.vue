<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="标准编码" prop="standardCode">
        <el-input v-model="queryParams.standardCode" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="标准名称" prop="standardName">
        <el-input v-model="queryParams.standardName" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="检验类型" prop="checkType">
        <el-select v-model="queryParams.checkType" placeholder="请选择" clearable>
          <el-option v-for="item in checkTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="standardStatus">
        <el-select v-model="queryParams.standardStatus" placeholder="请选择" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['quality:standard:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['quality:standard:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['quality:standard:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['quality:standard:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="standardList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="标准编码" align="center" prop="standardCode" width="150" />
      <el-table-column label="标准名称" align="center" prop="standardName" min-width="180" :show-overflow-tooltip="true" />
      <el-table-column label="检验类型" align="center" prop="checkType" width="100">
        <template slot-scope="scope">
          <span>{{ getCheckTypeLabel(scope.row.checkType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="关联物料" align="center" prop="matName" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.matName || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="standardStatus" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.standardStatus === '0' ? 'success' : 'danger'" size="small">
            {{ scope.row.standardStatus === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime ? $moment(scope.row.createTime).format('YYYY-MM-DD HH:mm') : '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)" v-hasPermi="['quality:standard:query']">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['quality:standard:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['quality:standard:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="标准编码" prop="standardCode">
              <el-input v-model="form.standardCode" :placeholder="form.standardId != null ? '' : '留空则系统自动生成'" :disabled="form.standardId != null" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标准名称" prop="standardName">
              <el-input v-model="form.standardName" placeholder="请输入检验标准名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="检验类型" prop="checkType">
              <el-select v-model="form.checkType" placeholder="请选择检验类型" style="width: 100%">
                <el-option v-for="item in checkTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="standardStatus">
              <el-radio-group v-model="form.standardStatus">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="关联物料" prop="matCode">
              <el-input v-model="form.matCode" readonly placeholder="请选择物料">
                <el-button slot="append" icon="el-icon-search" @click="selectMatOpen = true"></el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物料名称">
              <el-input v-model="form.matName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 检验项目列表 -->
        <el-divider content-position="left">检验项目</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="addItem">添加检验项</el-button>
          </el-col>
        </el-row>
        <el-table :data="form.items" style="width: 100%" size="small">
          <el-table-column label="序号" align="center" width="60">
            <template slot-scope="scope">{{ scope.$index + 1 }}</template>
          </el-table-column>
          <el-table-column label="检验项名称" align="center" min-width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemName" placeholder="如：透光率" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="单位" align="center" width="80">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemUnit" placeholder="如：%" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="标准值" align="center" width="100">
            <template slot-scope="scope">
              <el-input v-model="scope.row.standardValue" placeholder="如：≥89" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="下限" align="center" width="90">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.minValue" :precision="4" :controls="false" size="small" style="width: 80px" />
            </template>
          </el-table-column>
          <el-table-column label="上限" align="center" width="90">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.maxValue" :precision="4" :controls="false" size="small" style="width: 80px" />
            </template>
          </el-table-column>
          <el-table-column label="检验方法" align="center" min-width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.checkMethod" placeholder="检验方法" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="关键项" align="center" width="70">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.isKey" true-label="1" false-label="0" />
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="60">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-delete" @click="removeItem(scope.$index)" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="检验标准详情" :visible.sync="detailOpen" width="900px" append-to-body>
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="标准编码">{{ detailData.standardCode }}</el-descriptions-item>
        <el-descriptions-item label="标准名称">{{ detailData.standardName }}</el-descriptions-item>
        <el-descriptions-item label="检验类型">{{ getCheckTypeLabel(detailData.checkType) }}</el-descriptions-item>
        <el-descriptions-item label="关联物料">{{ detailData.matCode ? (detailData.matCode + ' ' + (detailData.matName || '')) : '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ detailData.standardStatus === '0' ? '正常' : '停用' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">检验项目</el-divider>
      <el-table :data="detailData.items || []" size="small" style="width: 100%">
        <el-table-column label="序号" align="center" prop="itemNo" width="60" />
        <el-table-column label="检验项名称" align="center" prop="itemName" min-width="120" />
        <el-table-column label="单位" align="center" prop="itemUnit" width="80" />
        <el-table-column label="标准值" align="center" prop="standardValue" width="100" />
        <el-table-column label="下限" align="center" prop="minValue" width="90" />
        <el-table-column label="上限" align="center" prop="maxValue" width="90" />
        <el-table-column label="检验方法" align="center" prop="checkMethod" min-width="140" :show-overflow-tooltip="true" />
        <el-table-column label="关键项" align="center" width="70">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isKey === '1' ? 'danger' : 'info'" size="mini">{{ scope.row.isKey === '1' ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 物料选择弹窗 -->
    <el-dialog title="选择物料" :visible.sync="selectMatOpen" width="1200px" append-to-body>
      <selectMat @confirmSelect="confirmSelectMat"></selectMat>
    </el-dialog>
  </div>
</template>

<script>
import { listQualityStandard, getQualityStandard, addQualityStandard, updateQualityStandard, delQualityStandard } from "@/api/base/quality";
import selectMat from "../../components/select-mat/index"

export default {
  name: "QualityStandard",
  components: { selectMat },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      standardList: [],
      title: "",
      open: false,
      detailOpen: false,
      detailData: {},
      selectMatOpen: false,
      checkTypeOptions: [
        { value: 'incoming', label: '原料检验' },
        { value: 'process', label: '过程检验' },
        { value: 'final', label: '成品检验' }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        standardCode: null,
        standardName: null,
        checkType: null,
        standardStatus: null,
      },
      form: {},
      rules: {
        standardName: [{ required: true, message: "检验标准名称不能为空", trigger: "blur" }],
        checkType: [{ required: true, message: "检验类型不能为空", trigger: "change" }],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listQualityStandard(this.queryParams).then(response => {
        this.standardList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getCheckTypeLabel(value) {
      const item = this.checkTypeOptions.find(i => i.value === value);
      return item ? item.label : value;
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        standardId: null,
        standardCode: null,
        standardName: null,
        checkType: null,
        matCode: null,
        matName: null,
        standardStatus: "0",
        remark: null,
        items: [],
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
      this.ids = selection.map(item => item.standardId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.title = "新增检验标准";
      this.open = true;
    },
    handleUpdate(row) {
      this.reset();
      const standardId = row.standardId || this.ids[0];
      getQualityStandard(standardId).then(response => {
        this.form = response.data;
        if (!this.form.items) this.form.items = [];
        this.title = "修改检验标准";
        this.open = true;
      });
    },
    handleDetail(row) {
      getQualityStandard(row.standardId).then(response => {
        this.detailData = response.data;
        this.detailOpen = true;
      });
    },
    addItem() {
      if (!this.form.items) this.form.items = [];
      this.form.items.push({
        itemName: null,
        itemUnit: null,
        standardValue: null,
        minValue: null,
        maxValue: null,
        checkMethod: null,
        isKey: '0',
      });
    },
    removeItem(index) {
      this.form.items.splice(index, 1);
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.standardId != null) {
            updateQualityStandard(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addQualityStandard(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const standardIds = row.standardId ? [row.standardId] : this.ids;
      this.$modal.confirm('是否确认删除所选检验标准？').then(() => {
        return delQualityStandard(standardIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('quality/standard/export', { ...this.queryParams }, `检验标准_${new Date().getTime()}.xlsx`);
    },
    confirmSelectMat(item) {
      this.form.matCode = item.matCode;
      this.form.matName = item.matName;
      this.selectMatOpen = false;
    },
  }
};
</script>

