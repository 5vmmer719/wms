<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="客户编码" prop="customerCode">
        <el-input v-model="queryParams.customerCode" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="客户名称" prop="customerName">
        <el-input v-model="queryParams.customerName" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="客户等级" prop="customerLevel">
        <el-select v-model="queryParams.customerLevel" placeholder="全部" clearable>
          <el-option label="A级" value="A" />
          <el-option label="B级" value="B" />
          <el-option label="C级" value="C" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="customerStatus">
        <el-select v-model="queryParams.customerStatus" placeholder="全部" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['base:customer:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['base:customer:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['base:customer:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="customerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="客户编码" align="center" prop="customerCode" width="120" />
      <el-table-column label="客户名称" align="center" prop="customerName" min-width="160" :show-overflow-tooltip="true" />
      <el-table-column label="联系人" align="center" prop="contactPerson" width="100" />
      <el-table-column label="联系电话" align="center" prop="contactPhone" width="130" />
      <el-table-column label="邮箱" align="center" prop="email" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="地址" align="center" prop="address" min-width="200" :show-overflow-tooltip="true" />
      <el-table-column label="客户等级" align="center" prop="customerLevel" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.customerLevel === 'A' ? 'danger' : scope.row.customerLevel === 'B' ? 'warning' : 'info'" size="small">
            {{ scope.row.customerLevel }}级
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="customerStatus" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.customerStatus === '0' ? 'success' : 'danger'" size="small">
            {{ scope.row.customerStatus === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['base:customer:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['base:customer:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户编码" prop="customerCode">
              <el-input v-model="form.customerCode" placeholder="请输入客户编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="form.customerName" placeholder="请输入客户名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户等级" prop="customerLevel">
              <el-select v-model="form.customerLevel" placeholder="请选择" style="width: 100%">
                <el-option label="A级（VIP）" value="A" />
                <el-option label="B级（普通）" value="B" />
                <el-option label="C级（潜在）" value="C" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="customerStatus">
              <el-select v-model="form.customerStatus" placeholder="请选择" style="width: 100%">
                <el-option label="正常" value="0" />
                <el-option label="停用" value="1" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCustomer, getCustomer, addCustomer, updateCustomer, delCustomer } from "@/api/base/customer";

export default {
  name: "Customer",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      customerList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerCode: null,
        customerName: null,
        customerLevel: null,
        customerStatus: null,
      },
      form: {},
      rules: {
        customerCode: [{ required: true, message: "客户编码不能为空", trigger: "blur" }],
        customerName: [{ required: true, message: "客户名称不能为空", trigger: "blur" }],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listCustomer(this.queryParams).then(response => {
        this.customerList = response.rows;
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
        customerId: null,
        customerCode: null,
        customerName: null,
        contactPerson: null,
        contactPhone: null,
        email: null,
        address: null,
        customerLevel: "B",
        customerStatus: "0",
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
      this.ids = selection.map(item => item.customerId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增客户";
    },
    handleUpdate(row) {
      this.reset();
      const customerId = row.customerId || this.ids;
      getCustomer(customerId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改客户";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.customerId != null) {
            updateCustomer(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCustomer(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const customerIds = row.customerId ? [row.customerId] : this.ids;
      this.$modal.confirm('是否确认删除选中的客户？').then(() => {
        return delCustomer(customerIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('base/customer/export', { ...this.queryParams }, `客户数据_${new Date().getTime()}.xlsx`);
    }
  }
};
</script>

