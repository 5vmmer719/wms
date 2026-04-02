<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单据号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入单据号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单据状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择">
          <el-option
            v-for="item in orderStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="检验状态" prop="checkStatus">
        <el-select v-model="queryParams.checkStatus" placeholder="请选择">
          <el-option
            v-for="item in checkStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="检验员" prop="checkBy">
        <el-input
          v-model="queryParams.checkBy"
          placeholder="请输入检验员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          v-hasPermi="['stock:inOrder:add']"
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
          v-hasPermi="['stock:inOrder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['stock:inOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="单据号" align="center" prop="orderNo" />
      <el-table-column label="单据类型" align="center" prop="orderTypeLabel" />
      <el-table-column label="单据状态" align="center" prop="orderStatusLabel" />
      <el-table-column label="检验状态" align="center" prop="checkStatusLabel" />
      <el-table-column label="检验员" align="center" prop="checkBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{$moment(scope.row.createTime).format('YYYY-MM-DD HH:mm')}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleDetail(scope.row)"
            v-hasPermi="['stock:inOrder:edit']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['stock:inOrder:remove']"
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

    <!-- 添加入库单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="入库仓库" prop="warehouseCode">
              <el-select v-model="form.warehouseCode" placeholder="请选择仓库" @change="handleWarehouseChange">
                <el-option
                  v-for="item in warehouseList"
                  :key="item.warehouseCode"
                  :label="item.warehouseName"
                  :value="item.warehouseCode"
                >
                  <span>{{ item.warehouseName }}</span>
                  <dict-tag style="float: right" :options="dict.type.warehouse_type" :value="item.warehouseType"/>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="选择物料标签">
              <el-button size="small" type="success" icon="el-icon-search" @click="openSelectMatLabelDialog">物料标签清单</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-table :data="matLabelList" style="width: 100%">
        <el-table-column label="行号" align="center" type="index" />
        <el-table-column label="物料编码" align="center" prop="matCode" width="100" />
        <el-table-column label="物料名称" align="center" prop="matName" width="120" />
        <el-table-column label="财务编码" align="center" prop="fdCode" width="100" />
        <el-table-column label="图号" align="center" prop="figNum" width="120" />
        <el-table-column label="数量" align="center" prop="quantity" width="80" />
        <el-table-column label="单价" align="center" prop="unitPrice" width="80" />
        <el-table-column label="单位" align="center" prop="unitCode" width="80">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode"/>
          </template>
        </el-table-column>
        <el-table-column label="批次" align="center" prop="batch" width="160" />
        <el-table-column label="供应商" align="center" prop="supplierName" width="180" />
        <el-table-column label="操作" align="center" width="80" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-scissors"
              @click="handleRemove(scope.$index, scope.row)"
            >去除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">创 建</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="'选择物料标签'" :visible.sync="selectMatLabelOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <selectMatLabel ref="matLabelPage" :labelIdArr="labelIdArr" :warehouseType="form.warehouseType" :filterOrderNo="false"
        @confirmSelectArr="confirmSelectMatLabelArr" @confirmSelect="confirmSelectMatLabel">
      </selectMatLabel>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelSelectMatLabel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 入库单详情对话框 -->
    <el-dialog class="detail-dialog" :title="'入库单详情'" :visible.sync="inOrderDetailOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="入库单号：">
              <span>{{form.orderNo}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库单类型：">
              <span>{{form.orderTypeLabel}}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="创建人：">
              <span>{{form.createBy}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间：">
              <span>{{$moment(form.createTime).format('YYYY-MM-DD HH:mm')}}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-table class="detail-table" :data="inOrderDetailList" style="width: 100%">
        <el-table-column label="行号" align="center" type="index" />
        <el-table-column label="物料编码" align="center" prop="matCode" width="100" />
        <el-table-column label="物料名称" align="center" prop="matName" width="120" />
        <el-table-column label="财务编码" align="center" prop="fdCode" width="100" />
        <el-table-column label="图号" align="center" prop="figNum" width="120" />
        <el-table-column label="数量" align="center" prop="quantity" width="80" />
        <el-table-column label="单价" align="center" prop="unitPrice" width="80" />
        <el-table-column label="单位" align="center" prop="unitCode" width="80">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode"/>
          </template>
        </el-table-column>
        <el-table-column label="批次" align="center" prop="batch" width="180" />
        <el-table-column label="供应商" align="center" prop="supplierName" width="180" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" icon="el-icon-printer" @click="confirmPrintInOrder">打 印</el-button>
        <el-button @click="cancelOrderDetail">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listInOrder, getInOrder, delInOrder, addInOrder, printInOrder } from "@/api/stock/inOrder";
import { listAllWarehouse } from "@/api/base/warehouse";
import selectMatLabel from "../../components/select-mat-label/index";

export default {
  name: "InOrder",
  dicts: ['base_mat_unit', 'warehouse_type'],
  components: { selectMatLabel },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      orderNos: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 入库单表格数据
      inOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        orderType: 'purchase',
        orderStatus: null,
        checkStatus: null,
        checkBy: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        warehouseCode: [
          { required: true, message: "请选择入库仓库", trigger: "blur" },
        ],
      },

      // 日期范围
      dateRange: [],

      //检验状态
      checkStatusOptions:[{value: 'un_checkout', label: '未检验'}, {value: 'checkout', label: '已检验'}],
      //单据状态
      orderStatusOptions:[
        {value: 'created', label: '已创建'},
        {value: 'printed', label: '已打印'},
        {value: 'entered', label: '已入库'},
      ],

      //仓库列表
      warehouseList: [],

      //选择物料标签
      selectMatLabelOpen: false,
      matLabelList: [],
      //防重复标签
      labelIdArr: [],

      //入库单详情
      inOrderDetailOpen: false,
      inOrderDetailList: [],
    };
  },
  created() {
    this.getList();
    this.getWarehouseList();
  },
  methods: {
    /** 查询入库单列表 */
    getList() {
      this.loading = true;
      listInOrder(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.inOrderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
      this.labelIdArr = [];
      this.matLabelList = [];
    },
    // 表单重置
    reset() {
      this.form = {
        orderId: null,
        orderNo: null,
        orderType: null,
        warehouseCode: null,
        warehouseType: null,
        orderStatus: "0",
        checkStatus: "0",
        checkBy: null,
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
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId)
      this.orderNos = selection.map(item => item.orderNo)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加入库单";
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.reset();
      getInOrder(row.orderId).then(response => {
        this.form = response.data;
        this.inOrderDetailList = response.data.detailList;
        this.inOrderDetailOpen = true;
      });
    },
    cancelOrderDetail(){
      this.reset();
      this.inOrderDetailList = [];
      this.inOrderDetailOpen = false;
    },
    /** 打印入库单 */
    confirmPrintInOrder(){
      printInOrder(this.form.orderId).then(response => {
        const binaryData = [];
        binaryData.push(response);
        let pdfUrl = window.URL.createObjectURL(new Blob(binaryData, { type: "application/pdf" }));
        window.open(pdfUrl);
      });
    },
    /** 提交按钮 */
    submitForm() {
      let that = this;
      if(!that.matLabelList || that.matLabelList.length === 0){
        that.$modal.msgError("请选择物料标签");
        return;
      }
      that.$modal.confirm('是否确认创建入库单？').then(function() {
        that.form.detailList = that.matLabelList;
        that.form.orderType = 'purchase';
        addInOrder(that.form).then(response => {
          that.$modal.msgSuccess("新增成功");
          that.open = false;
          that.getList();
          that.reset();
          that.labelIdArr = [];
          that.matLabelList = [];
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const orderIds = row.orderId || this.ids;
      const delOrderNos = row.orderNo || this.orderNos;
      this.$modal.confirm('是否确认删除入库单号为 "' + delOrderNos + '" 的数据项？').then(function() {
        return delInOrder(orderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('stock/inOrder/export', {
        ...this.queryParams
      }, `inOrder_${new Date().getTime()}.xlsx`)
    },
    //选择物料标签
    openSelectMatLabelDialog(){
      if(!this.form.warehouseCode){
        this.$modal.msgWarning("请先选择入库仓库");
        return;
      }
      this.selectMatLabelOpen = true;
      this.$nextTick(function(){
        this.$refs.matLabelPage.getList();
      })
    },
    cancelSelectMatLabel(){
      this.selectMatLabelOpen = false;
    },
    confirmSelectMatLabel(item){
      this.matLabelList.unshift(item);
      this.labelIdArr.push(item.labelId);
      this.selectMatLabelOpen = false;
    },
    confirmSelectMatLabelArr(arr){
      let that = this;
      arr && arr.length > 0 && arr.forEach(item => {
        that.labelIdArr.push(item.labelId);
        that.matLabelList.unshift(item);
      });
      that.selectMatLabelOpen = false;
    },
    //去除物料标签
    handleRemove(index, row){
      this.labelIdArr.splice(this.labelIdArr.indexOf(row.labelId), 1);
      this.matLabelList.splice(index, 1);
    },
    //查询仓库列表
    getWarehouseList(){
      listAllWarehouse().then(response => {
        this.warehouseList = response;
      });
    },
    // 仓库选择变化时设置仓库类型
    handleWarehouseChange(warehouseCode){
      const warehouse = this.warehouseList.find(item => item.warehouseCode === warehouseCode);
      if(warehouse){
        this.form.warehouseType = warehouse.warehouseType;
        // 清空已选物料标签（因为仓库类型可能变了）
        this.matLabelList = [];
        this.labelIdArr = [];
      }
    },
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.detail-dialog{
  .el-form-item{
    margin-bottom: 0px;
  }
  .detail-table{
    margin-top: 20px;
  }
}
</style>