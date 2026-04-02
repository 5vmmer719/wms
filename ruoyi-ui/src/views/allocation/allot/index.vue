<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="调拨单号" prop="allotNo">
        <el-input
          v-model="queryParams.allotNo"
          placeholder="请输入调拨单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="调拨原因" prop="allotReason">
        <el-input
          v-model="queryParams.allotReason"
          placeholder="请输入调拨原因"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发起仓库" prop="srcWarehouseCode">
        <el-select v-model="queryParams.srcWarehouseCode" placeholder="请选择发起仓库">
          <el-option
            v-for="item in warehouseList"
            :key="item.warehouseCode"
            :label="item.warehouseName"
            :value="item.warehouseCode"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="目标仓库" prop="destWarehouseCode">
        <el-select v-model="queryParams.destWarehouseCode" placeholder="请选择目标仓库">
          <el-option
            v-for="item in warehouseList"
            :key="item.warehouseCode"
            :label="item.warehouseName"
            :value="item.warehouseCode"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="调拨进度" prop="allotProgress">
        <el-input
          v-model="queryParams.allotProgress"
          placeholder="请输入调拨进度"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['stock:allotOrder:add']"
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
          v-hasPermi="['stock:allotOrder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['stock:allotOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="allotOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="调拨单号" align="center" prop="allotNo" />
      <el-table-column label="发起仓库" align="center" prop="srcWarehouseName" />
      <el-table-column label="目标仓库" align="center" prop="destWarehouseName" />
      <el-table-column label="调拨单状态" align="center" prop="allotStatusLabel" />
      <el-table-column label="调拨进度" align="center" prop="allotProgressLabel" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleDetail(scope.row)"
            v-hasPermi="['stock:allotOrder:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['stock:allotOrder:remove']"
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

    <!-- 添加调拨单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="发起仓库" prop="srcWarehouseCode">
              <el-select v-model="form.srcWarehouseCode" placeholder="请选择发起仓库">
                <el-option
                  v-for="item in warehouseList"
                  :key="item.warehouseCode"
                  :label="item.warehouseName"
                  :value="item.warehouseCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="目标仓库" prop="destWarehouseCode">
              <el-select v-model="form.destWarehouseCode" placeholder="请选择目标仓库">
                <el-option
                  v-for="item in warehouseList"
                  :key="item.warehouseCode"
                  :label="item.warehouseName"
                  :value="item.warehouseCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="选择物料">
              <el-button size="small" type="success" icon="el-icon-search" @click="openSelectMatLabelDialog">物料标签清单</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="调拨原因" prop="allotReason">
            <el-input v-model="form.allotReason" placeholder="请输入调拨原因" />
          </el-form-item>
        </el-row>
      </el-form>
      <el-table :data="matLabelList" style="width: 100%">
        <el-table-column label="行号" align="center" type="index" />
        <el-table-column label="物料编码" align="center" prop="matCode" width="100" />
        <el-table-column label="物料名称" align="center" prop="matName" width="120" />
        <el-table-column label="财务编码" align="center" prop="fdCode" width="100" />
        <el-table-column label="图号" align="center" prop="figNum" width="120" />
        <el-table-column label="数量" align="center" prop="quantity" width="80" />
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
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 选择物料标签弹窗 -->
    <el-dialog :title="'选择物料标签'" :visible.sync="selectMatLabelOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <selectMatLabel ref="matLabelPage" :labelIdArr="labelIdArr" :warehouseCode="form.srcWarehouseCode" :filterOrderNo="false"
        @confirmSelectArr="confirmSelectMatLabelArr" @confirmSelect="confirmSelectMatLabel">
      </selectMatLabel>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelSelectMatLabel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 调拨单详情对话框 -->
    <el-dialog class="detail-dialog" :title="'调拨单详情'" :visible.sync="allotOrderDetailOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="120px">
        <el-row>
          <el-col :span="7">
            <el-form-item label="调拨单号：">
              <span>{{form.allotNo}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="发起仓库：">
              <span>{{form.srcWarehouseName}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="目标仓库：">
              <span>{{form.destWarehouseName}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <div class="qr-code" ref="qrCodeUrl"></div>
          </el-col>
        </el-row>
        <el-row>
            <el-form-item label="调拨原因：">
              <span>{{form.allotReason}}</span>
            </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="调拨进度：">
              <span>{{form.allotProgressLabel}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建人：">
              <span>{{form.createBy}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建时间：">
              <span>{{$moment(form.createTime).format('YYYY-MM-DD HH:mm')}}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-table class="detail-table" :data="allotDetailList" style="width: 100%">
        <el-table-column label="行号" align="center" prop="lineNo" />
        <el-table-column label="物料编码" align="center" prop="matCode"/>
        <el-table-column label="物料名称" align="center" prop="matName"/>
        <el-table-column label="图号" align="center" prop="figNum"/>
        <el-table-column label="发起货位" align="center" prop="srcLocationCode"/>
        <el-table-column label="发起数" align="center" prop="quantity"/>
        <el-table-column label="接收货位" align="center" prop="destLocationCode"/>
        <el-table-column label="接收数" align="center" prop="signQuantity"/>
        <el-table-column label="单位" align="center" prop="unitCode">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode"/>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" icon="el-icon-printer" @click="confirmPrintAllotOrder">打 印</el-button>
        <el-button @click="cancelAllotDetail">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAllotOrder, getAllotOrder, delAllotOrder, addAllotOrder, printAllotOrder } from "@/api/stock/allotOrder";
import { listAllWarehouse } from "@/api/base/warehouse";
import selectMatLabel from "../../components/select-mat-label/index";

import QRCode from 'qrcodejs2'

export default {
  name: "AllotOrder",
  dicts: ['base_mat_unit'],
  components: { selectMatLabel },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      allotNos: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 调拨单表格数据
      allotOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        allotNo: null,
        allotReason: null,
        srcWarehouseCode: null,
        destWarehouseCode: null,
        allotStatus: null,
        allotProgress: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        srcWarehouseCode: [
          { required: true, message: "请选发起仓库", trigger: "blur" },
        ],
        destWarehouseCode: [
          { required: true, message: "请选目标仓库", trigger: "blur" },
        ],
        allotReason: [
          { required: true, message: "调拨原因不能为空", trigger: "blur" },
        ],
      },

      //选择仓库
      warehouseList: [],

      //详情
      allotOrderDetailOpen: false,
      allotDetailList: [],

      //选择物料标签
      selectMatLabelOpen: false,
      matLabelList: [],
      //防重复标签
      labelIdArr: [],

    };
  },
  created() {
    this.getList();
    this.getWarehouseList();
  },
  methods: {
    /** 查询调拨单列表 */
    getList() {
      this.loading = true;
      listAllotOrder(this.queryParams).then(response => {
        this.allotOrderList = response.rows;
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
        allotId: null,
        allotNo: null,
        allotReason: null,
        srcWarehouseCode: null,
        destWarehouseCode: null,
        allotStatus: "0",
        allotProgress: null,
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
      this.ids = selection.map(item => item.allotId)
      this.allotNos = selection.map(item => item.allotNo)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加调拨单";
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.reset();
      getAllotOrder(row.allotId).then(response => {
        this.form = response.data;
        this.allotDetailList = response.data.detailList;
        this.createQrCode(this.form.allotNo);
        this.allotOrderDetailOpen = true;
      });
    },
    confirmPrintAllotOrder(){
      printAllotOrder(this.form.allotId).then(response => {
        const binaryData = [];
        binaryData.push(response);
        let pdfUrl = window.URL.createObjectURL(new Blob(binaryData, { type: "application/pdf" }));
        window.open(pdfUrl);
      });
    },
    cancelAllotDetail(){
      this.allotOrderDetailOpen = false;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let that = this;
          if(!that.matLabelList || that.matLabelList.length === 0){
            that.$modal.msgError("请选择物料标签");
            return;
          }
          that.$modal.confirm('是否确认创建调拨单？').then(function() {
            that.form.detailList = that.matLabelList;
            addAllotOrder(that.form).then(response => {
              that.$modal.msgSuccess("新增成功");
              that.open = false;
              that.getList();
              that.reset();
              that.labelIdArr = [];
              that.matLabelList = [];
            });
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const allotIds = row.allotId || this.ids;
      const delAllotNos = row.allotNo || this.allotNos;
      this.$modal.confirm('是否确认删除调拨单号为 "' + delAllotNos + '" 的数据项？').then(function() {
        return delAllotOrder(allotIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('stock/allotOrder/export', {
        ...this.queryParams
      }, `allotOrder_${new Date().getTime()}.xlsx`)
    },
    //查询仓库
    getWarehouseList(){
      listAllWarehouse().then(response => {
        this.warehouseList = response;
      });
    },
    createQrCode(allotNo) {
        this.$nextTick(() => {
            var qrcode = new QRCode(this.$refs.qrCodeUrl, {
              text: 'ORDER:' + allotNo,
              width: 100,
              height: 100,
              colorDark: '#000000',
              colorLight: '#ffffff',
              correctLevel: QRCode.CorrectLevel.H
          })
        })
    },
    //选择物料标签
    openSelectMatLabelDialog(){
      if(!this.form.srcWarehouseCode){
        this.$modal.msgWarning("请先选择发起仓库");
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
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.qr-code{
  margin: -35px 0 0 30px;
}
</style>
