<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
          v-hasPermi="['base:mat:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['base:mat:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['base:mat:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['base:mat:export']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['base:mat:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="matList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="matId" />
      <el-table-column label="物料编码" align="center" prop="matCode" />
      <el-table-column label="物料描述" align="center" prop="matName" />
      <el-table-column label="财务编码" align="center" prop="fdCode" />
      <el-table-column label="图号" align="center" prop="figNum" />
      <el-table-column label="物料组" align="center" prop="matGroupName" />
      <el-table-column label="分类" align="center" prop="matClassName" />
      <el-table-column label="基本单位" align="center" prop="unitCode">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode"/>
        </template>
      </el-table-column>
      <el-table-column label="毛重" align="center" prop="grossWeight" />
      <el-table-column label="安全库存" align="center" prop="safetyStock" />
      <el-table-column label="标准价" align="center" prop="standardPrice" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['base:mat:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['base:mat:remove']"
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

    <!-- 添加或修改物料主数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物料编码" prop="matCode">
          <el-input v-model="form.matCode" placeholder="请输入物料编码" :disabled="form.matId != null"/>
        </el-form-item>
        <el-form-item label="物料描述" prop="matName">
          <el-input v-model="form.matName" placeholder="请输入物料描述" />
        </el-form-item>
        <el-form-item label="财务编码" prop="fdCode">
          <el-input v-model="form.fdCode" placeholder="请输入财务编码" />
        </el-form-item>
        <el-form-item label="图号" prop="figNum">
          <el-input v-model="form.figNum" placeholder="请输入图号" />
        </el-form-item>
        <el-form-item label="物料组" prop="matGroup">
          <el-select v-model="form.matGroup" placeholder="请选择物料组">
            <el-option
              v-for="item in groupList"
              :key="item.groupCode"
              :label="item.groupName"
              :value="item.groupCode"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分类" prop="matClass">
          <el-select v-model="form.matClass" placeholder="请选择物料分类">
            <el-option
              v-for="item in classList"
              :key="item.classCode"
              :label="item.className"
              :value="item.classCode"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="基本单位" prop="unitCode">
          <el-select v-model="form.unitCode" placeholder="请选择基本单位">
            <el-option
              v-for="dict in dict.type.base_mat_unit"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="毛重" prop="grossWeight">
          <el-input v-model="form.grossWeight" placeholder="请输入毛重" />
        </el-form-item>
        <el-form-item label="安全库存" prop="safetyStock">
          <el-input v-model="form.safetyStock" placeholder="请输入安全库存" />
        </el-form-item>
        <el-form-item label="标准价" prop="standardPrice">
          <el-input v-model="form.standardPrice" placeholder="请输入标准价" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 物料导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body :close-on-click-modal="false">
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的物料数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMat, getMat, delMat, addMat, updateMat } from "@/api/base/mat";
import { listAllGroup } from "@/api/base/group";
import { listAllClass } from "@/api/base/class";
import { getToken } from "@/utils/auth";

export default {
  name: "Mat",
  dicts: ['base_mat_unit'],
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
      // 物料主数据表格数据
      matList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        matCode: null,
        matName: null,
        fdCode: null,
        figNum: null,
        matGroup: null,
        matClass: null,
        unitCode: null,
        grossWeight: null,
        safetyStock: null,
        standardPrice: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        matCode: [
          { required: true, message: "物料编码不能为空", trigger: "blur" },
        ],
        matName: [
          { required: true, message: "物料名称不能为空", trigger: "blur" }
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
      },

      // 物料导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/base/mat/importData"
      },

      //组、分类
      groupList: [],
      classList: [],
    };
  },
  created() {
    this.getList();
    this.getGroupList();
    this.getClassList();
  },
  methods: {
    /** 查询物料主数据列表 */
    getList() {
      this.loading = true;
      listMat(this.queryParams).then(response => {
        this.matList = response.rows;
        this.total = response.total;
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
        matId: null,
        matCode: null,
        matName: null,
        fdCode: null,
        figNum: null,
        matGroup: null,
        matClass: null,
        unitCode: null,
        grossWeight: null,
        safetyStock: null,
        standardPrice: null,
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
      this.ids = selection.map(item => item.matId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加物料主数据";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const matId = row.matId || this.ids
      getMat(matId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改物料主数据";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.matId != null) {
            updateMat(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMat(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const matIds = row.matId || this.ids;
      this.$modal.confirm('是否确认删除物料主数据编号为"' + matIds + '"的数据项？').then(function() {
        return delMat(matIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('base/mat/export', {
        ...this.queryParams
      }, `mat_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "物料导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('base/mat/downloadTemplate', {
      }, `mat_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
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
  }
};
</script>
