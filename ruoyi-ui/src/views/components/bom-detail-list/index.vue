<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增BOM关系</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="selectedIds.length === 0"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="bomList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="物料编码" align="center" prop="childMatCode" />
      <el-table-column label="物料名称" align="center" prop="childMatName" />
      <el-table-column label="图号" align="center" prop="figNum" />
      <el-table-column label="数量" align="center" prop="childMatNum" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.isFictitious === 'Y'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleDetail(scope.row)"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDeleteRow(scope.row)"
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

    <!-- 新增BOM关系对话框 -->
    <el-dialog title="新增BOM关系" :visible.sync="addOpen" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="100px">
        <el-form-item label="父物料编码">
          <el-input v-model="fatherMatCode" disabled />
        </el-form-item>
        <el-form-item label="子物料编码" prop="childMatCode">
          <el-input v-model="addForm.childMatCode" placeholder="请选择子物料" readonly>
            <el-button slot="append" icon="el-icon-search" @click="openSelectMat">选择</el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="子物料名称">
          <el-input v-model="addForm.childMatName" disabled />
        </el-form-item>
        <el-form-item label="用量" prop="childMatNum">
          <el-input-number v-model="addForm.childMatNum" :min="0.001" :precision="3" :step="1" placeholder="请输入用量" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAddForm">确 定</el-button>
        <el-button @click="addOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 选择子物料对话框 -->
    <el-dialog title="选择子物料" :visible.sync="selectMatOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <selectMat @confirmSelect="handleConfirmSelectMat" />
    </el-dialog>
  </div>
</template>

<script>
import { detailListBom, addBom } from "@/api/base/bom";
import { delBomByIds } from "@/api/base/bom";
import selectMat from "../../components/select-mat/index";

export default {
  name: "BomDetailList",
  components: { selectMat },
  props: {
    fatherMatCode: {
      type: String,
      default: '',
    }
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 物料BOM表格数据
      bomList: [],
      // 选中的bomId数组
      selectedIds: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fatherMatCode: null,
        childMatCode: null,
        childMatName: null,
      },
      // 新增BOM关系对话框
      addOpen: false,
      addForm: {
        fatherMatCode: '',
        childMatCode: '',
        childMatName: '',
        childMatNum: 1,
      },
      addRules: {
        childMatCode: [
          { required: true, message: '请选择子物料', trigger: 'change' }
        ],
        childMatNum: [
          { required: true, message: '请输入用量', trigger: 'blur' }
        ],
      },
      // 选择物料对话框
      selectMatOpen: false,
    };
  },
  created() {
    this.queryParams.fatherMatCode = this.fatherMatCode;
    this.getList();
  },
  methods: {
    /** 查询物料BOM列表 */
    getList() {
      this.loading = true;
      detailListBom(this.queryParams).then(response => {
        this.bomList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.selectedIds = selection.map(item => item.bomId);
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.addForm = {
        fatherMatCode: this.fatherMatCode,
        childMatCode: '',
        childMatName: '',
        childMatNum: 1,
      };
      this.addOpen = true;
    },
    /** 打开选择子物料对话框 */
    openSelectMat() {
      this.selectMatOpen = true;
    },
    /** 确认选择子物料 */
    handleConfirmSelectMat(row) {
      this.addForm.childMatCode = row.matCode;
      this.addForm.childMatName = row.matName;
      this.addForm.figNum = row.figNum;
      this.selectMatOpen = false;
    },
    /** 提交新增BOM关系 */
    submitAddForm() {
      this.$refs['addForm'].validate(valid => {
        if (valid) {
          addBom(this.addForm).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.addOpen = false;
            this.getList();
          });
        }
      });
    },
    /** 删除按钮操作（批量） */
    handleDelete() {
      const bomIds = this.selectedIds;
      this.$modal.confirm('是否确认删除选中的BOM关系？').then(() => {
        return delBomByIds(bomIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 删除按钮操作（单行） */
    handleDeleteRow(row) {
      this.$modal.confirm('是否确认删除该BOM关系？').then(() => {
        return delBomByIds(row.bomId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 详情 */
    handleDetail(row) {
      // 可以递归查看子BOM
    },
  }
};
</script>

<style scoped>
.app-container{
  padding: 0px;
}
</style>
