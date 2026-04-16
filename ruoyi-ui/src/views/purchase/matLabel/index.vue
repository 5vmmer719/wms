<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标签编码" prop="labelCode">
        <el-input
          v-model="queryParams.labelCode"
          placeholder="请输入标签编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料编码" prop="matCode">
        <el-input
          v-model="queryParams.matCode"
          placeholder="请输入物料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料描述" prop="matName">
        <el-input
          v-model="queryParams.matName"
          placeholder="请输入物料描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="财务编码" prop="fdCode">
        <el-input
          v-model="queryParams.fdCode"
          placeholder="请输入财务编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图号" prop="figNum">
        <el-input
          v-model="queryParams.figNum"
          placeholder="请输入图号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料组" prop="matGroup">
        <el-select v-model="queryParams.matGroup" placeholder="请选择物料组">
          <el-option
            v-for="item in groupList"
            :key="item.groupCode"
            :label="item.groupName"
            :value="item.groupCode"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="分类" prop="matClass">
        <el-select v-model="queryParams.matClass" placeholder="请选择物料分类">
          <el-option
            v-for="item in classList"
            :key="item.classCode"
            :label="item.className"
            :value="item.classCode"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="批次" prop="batch">
        <el-input
          v-model="queryParams.batch"
          placeholder="请输入批次"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="供应商" prop="supplierName">
        <el-input
          v-model="queryParams.supplierName"
          placeholder="请输入供应商"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="生产时间" prop="prodTime">
        <el-date-picker clearable
          v-model="queryParams.prodTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择生产时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.mat_label_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['stock:matLabel:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['stock:matLabel:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['stock:matLabel:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-printer"
          size="mini"
          @click="handlePrintBatch"
          v-hasPermi="['stock:matLabel:list']"
        >批量打印</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="matLabelList" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" fixed align="center" prop="labelId" />
      <el-table-column label="物料编码" fixed align="center" prop="matCode" width="100" />
      <el-table-column label="物料描述" fixed align="center" prop="matName" width="120" />
      <el-table-column label="财务编码" align="center" prop="fdCode" width="100" />
      <el-table-column label="图号" align="center" prop="figNum" width="150" />
      <el-table-column label="标签编码" align="center" prop="labelCode" width="180" />
      <el-table-column label="物料组" align="center" prop="matGroupName" width="80" />
      <el-table-column label="分类" align="center" prop="matClassName" width="80" />
      <el-table-column label="基本单位" align="center" prop="unitCode" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode"/>
        </template>
      </el-table-column>
      <el-table-column label="批次" align="center" prop="batch" width="180" />
      <el-table-column label="供应商" align="center" prop="supplierName" width="180" />
      <el-table-column label="生产时间" align="center" prop="prodTime" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.prodTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
            v-hasRole="['admin']"
          ></el-switch>
          <dict-tag v-if="!$auth.hasRole('admin')" :options="dict.type.mat_label_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['stock:matLabel:edit']"
          >编辑</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-printer"
            @click="handleComplement(scope.row)"
            v-hasPermi="['stock:matLabel:list']"
          >打印</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['stock:matLabel:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改物料标签对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="物料编码" prop="matCode">
              <el-input v-model="form.matCode" readonly placeholder="请选择物料">
                <el-button slot="append" icon="el-icon-search" @click="openSelectMatDialog"></el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物料描述" prop="matName">
              <el-input v-model="form.matName" placeholder="请输入物料描述" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="财务编码" prop="fdCode">
              <el-input v-model="form.fdCode" placeholder="请输入财务编码" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="图号" prop="figNum">
              <el-input v-model="form.figNum" placeholder="请输入图号" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="物料组" prop="matGroup">
              <el-select v-model="form.matGroup" placeholder="请选择物料组" disabled>
                <el-option
                  v-for="item in groupList"
                  :key="item.groupCode"
                  :label="item.groupName"
                  :value="item.groupCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="matClass">
              <el-select v-model="form.matClass" placeholder="请选择物料分类" disabled>
                <el-option
                  v-for="item in classList"
                  :key="item.classCode"
                  :label="item.className"
                  :value="item.classCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="基本单位" prop="unitCode">
              <el-select v-model="form.unitCode" placeholder="请选择基本单位" disabled>
                <el-option
                  v-for="dict in dict.type.base_mat_unit"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="批次" prop="batch">
              <el-input v-model="form.batch" placeholder="请输入批次" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplierName">
              <el-input v-model="form.supplierName" readonly placeholder="请选择供应商">
                <el-button slot="append" icon="el-icon-search" @click="openSelectSupplierDialog"></el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产时间" prop="prodTime">
              <el-date-picker clearable
                v-model="form.prodTime"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择生产时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="标签编码" prop="labelCode">
              <el-input v-model="form.labelCode" placeholder="自动生成" disabled />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="'选择物料'" :visible.sync="selectMatOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <selectMat @confirmSelect="confirmSelectMat"></selectMat>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelSelectMat">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="'选择供应商'" :visible.sync="selectSupplierOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <selectSupplier @confirmSelect="confirmSelectSupplier"></selectSupplier>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelSelectSupplier">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMatLabel, getMatLabel, delMatLabel, addMatLabel, updateMatLabel, printLabel, printLabelBatch, changeMatLabelStatus } from "@/api/stock/matLabel";
import { listAllGroup } from "@/api/base/group";
import { listAllClass } from "@/api/base/class";
import selectMat from "../../components/select-mat/index"
import selectSupplier from "../../components/select-supplier/index"

export default {
  name: "MatLabel",
  dicts: ['base_mat_unit', 'mat_label_status'],
  components: { selectMat, selectSupplier },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 物料标签表格数据
      matLabelList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 提交loading
      submitLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        labelType: null,
        labelCode: null,
        matCode: null,
        matName: null,
        fdCode: null,
        figNum: null,
        matGroup: null,
        matClass: null,
        unitCode: null,
        batch: null,
        supplierCode: null,
        supplierName: null,
        prodTime: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        matCode: [
          { required: true, message: "请选择物料", trigger: "blur" },
        ],
        matName: [
          { required: true, message: "物料名称不能为空", trigger: "blur" },
        ],
        fdCode: [
          { required: true, message: "财务编码不能为空", trigger: "blur" },
        ],
        figNum: [
          { required: true, message: "图号不能为空", trigger: "blur" },
        ],
        matGroup: [
          { required: true, message: "请选择物料组", trigger: "blur" },
        ],
        matClass: [
          { required: true, message: "请选择物料分类", trigger: "blur" },
        ],
        unitCode: [
          { required: true, message: "请选择基本单位", trigger: "blur" },
        ],
        batch: [
          { required: true, message: "批次不能为空", trigger: "blur" },
        ],
        supplierCode: [
          { required: true, message: "请选择供应商", trigger: "blur" },
        ],
        supplierName: [
          { required: true, message: "供应商名称不能为空", trigger: "blur" },
        ],
        prodTime: [
          { required: true, message: "请选择生产时间", trigger: "blur" },
        ],
      },

      //组、分类
      groupList: [],
      classList: [],

      //选择物料
      selectMatOpen: false,
      //选择供应商
      selectSupplierOpen: false,

    };
  },
  created() {
    this.getList();
    this.getGroupList();
    this.getClassList();
  },
  methods: {
    /** 查询物料标签列表 */
    getList() {
      this.loading = true;
      listMatLabel(this.queryParams).then(response => {
        this.matLabelList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).finally(() => {
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        labelId: null,
        labelType: null,
        labelCode: null,
        matCode: null,
        matName: null,
        fdCode: null,
        figNum: null,
        matGroup: null,
        matClass: null,
        unitCode: null,
        batch: null,
        supplierCode: null,
        supplierName: null,
        prodTime: null,
        status: '0',
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.labelId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加物料标签";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const labelId = row.labelId || this.ids
      getMatLabel(labelId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改物料标签";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.labelType = 'purchase';
          if (this.form.labelId != null) {
            this.submitLoading = true;
            updateMatLabel(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.submitLoading = false;
            });
          } else {
            this.submitLoading = true;
            addMatLabel(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.submitLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const labelIds = row.labelId || this.ids;
      this.$modal.confirm('是否确认删除物料标签编号为"' + labelIds + '"的数据项？').then(function() {
        return delMatLabel(labelIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('stock/matLabel/export', {
        ...this.queryParams
      }, `matLabel_${new Date().getTime()}.xlsx`)
    },
    /** 批量打印 */
    handlePrintBatch(row){
      const labelIds = row.labelId || this.ids;
      printLabelBatch(labelIds).then(response => {
        const binaryData = [];
        binaryData.push(response);
        let pdfUrl = window.URL.createObjectURL(new Blob(binaryData, { type: "application/pdf" }));
        window.open(pdfUrl);
      });
    },
    /** 查询组、分类 */
    getGroupList(){
      listAllGroup().then(response => {
        this.groupList = response;
      });
    },
    getClassList(){
      listAllClass().then(response => {
        this.classList = response;
      });
    },
    //选择物料
    openSelectMatDialog(){
      this.selectMatOpen = true;
    },
    cancelSelectMat(){
      this.selectMatOpen = false;
    },
    confirmSelectMat(item){
      this.form.matCode = item.matCode;
      this.form.matName = item.matName;
      this.form.fdCode = item.fdCode;
      this.form.figNum = item.figNum;
      this.form.matGroup = item.matGroup;
      this.form.matClass = item.matClass;
      this.form.unitCode = item.unitCode;
      this.form.batch = 'CG'+ this.$moment().format('YYYYMMDDHHmmss');
      // 自动生成标签编码：LB + 时间戳
      this.form.labelCode = 'LB' + this.$moment().format('YYYYMMDDHHmmss');
      this.selectMatOpen = false;
    },
    //选择供应商
    openSelectSupplierDialog(){
      this.selectSupplierOpen = true;
    },
    cancelSelectSupplier(){
      this.selectSupplierOpen = false;
    },
    confirmSelectSupplier(item){
      this.form.supplierCode = item.supplierCode;
      this.form.supplierName = item.supplierName;
      this.selectSupplierOpen = false;
    },
    //补码
    handleComplement(item){
      printLabel(item.labelId).then(response => {
        const binaryData = [];
        binaryData.push(response);
        let pdfUrl = window.URL.createObjectURL(new Blob(binaryData, { type: "application/pdf" }));
        window.open(pdfUrl);
      });
    },
    /** 标签状态修改（启用/停用） */
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要' + text + '标签编码为"' + row.labelCode + '"的标签吗？').then(() => {
        return changeMatLabelStatus(row.labelId, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(() => {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
  }
};
</script>
