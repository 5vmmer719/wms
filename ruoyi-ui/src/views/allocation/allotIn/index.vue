<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="入库单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入入库单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="调拨单号" prop="allotNo">
        <el-input
          v-model="queryParams.allotNo"
          placeholder="请输入调拨单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="目标仓库" prop="warehouseCode">
        <el-select v-model="queryParams.warehouseCode" placeholder="请选择目标仓库" clearable>
          <el-option
            v-for="item in warehouseList"
            :key="item.warehouseCode"
            :label="item.warehouseName"
            :value="item.warehouseCode"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="单据状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择单据状态" clearable>
          <el-option
            v-for="item in orderStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['stock:allotIn:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['stock:allotIn:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="入库单号" align="center" prop="orderNo" />
      <el-table-column label="调拨单号" align="center" prop="allotNo" />
      <el-table-column label="目标仓库" align="center" prop="warehouseName" />
      <el-table-column label="单据状态" align="center" prop="orderStatusLabel" />
      <el-table-column label="创建人" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{$moment(scope.row.createTime).format('YYYY-MM-DD HH:mm')}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
            v-hasPermi="['stock:allotIn:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-printer"
            @click="handlePrint(scope.row)"
            v-hasPermi="['stock:allotIn:export']"
          >打印</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['stock:allotIn:remove']"
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

    <!-- 调拨入库单详情对话框 -->
    <el-dialog class="detail-dialog" title="调拨入库单详情" :visible.sync="detailOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="detailForm" label-width="120px">
        <el-row>
          <el-col :span="6">
            <el-form-item label="入库单号：">
              <span>{{detailForm.orderNo}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="调拨单号：">
              <span>{{detailForm.allotNo}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="目标仓库：">
              <span>{{detailForm.warehouseName}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <div class="qr-code" ref="qrCodeUrl"></div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="单据状态：">
              <span>{{detailForm.orderStatusLabel}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建人：">
              <span>{{detailForm.createBy}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建时间：">
              <span>{{$moment(detailForm.createTime).format('YYYY-MM-DD HH:mm')}}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-divider content-position="left">物料明细</el-divider>

      <el-table class="detail-table" :data="detailList" style="width: 100%">
        <el-table-column label="行号" align="center" prop="lineNo" width="60" />
        <el-table-column label="物料编码" align="center" prop="matCode" width="120" />
        <el-table-column label="物料名称" align="center" prop="matName" width="150" />
        <el-table-column label="入库数量" align="center" prop="quantity" width="100" />
        <el-table-column label="已入库数量" align="center" prop="stockInQuantity" width="100" />
        <el-table-column label="批次" align="center" prop="batch" width="160" />
        <el-table-column label="单位" align="center" prop="unitCode" width="80">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.base_mat_unit" :value="scope.row.unitCode"/>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" icon="el-icon-printer" @click="confirmPrint">打 印</el-button>
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAllotInOrder, getAllotInOrder, delAllotInOrder, printAllotInOrder } from "@/api/allocation/allotIn";
import { listAllWarehouse } from "@/api/base/warehouse";
import QRCode from 'qrcodejs2'

export default {
  name: "AllotInOrder",
  dicts: ['base_mat_unit'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中单号数组
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
      // 详情数据
      detailForm: {},
      detailList: [],
      // 是否显示弹出层
      detailOpen: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        allotNo: null,
        warehouseCode: null,
        orderStatus: null
      },
      // 仓库列表
      warehouseList: [],
      // 单据状态选项
      orderStatusOptions: [
        {value: 'created', label: '已创建'},
        {value: 'checked', label: '已检验'},
        {value: 'entered', label: '已入库'}
      ]
    };
  },
  created() {
    this.getList();
    this.getWarehouseList();
  },
  methods: {
    /** 查询调拨入库单列表 */
    getList() {
      this.loading = true;
      listAllotInOrder(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.inOrderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询仓库列表 */
    getWarehouseList() {
      listAllWarehouse().then(response => {
        this.warehouseList = response;
      });
    },
    // 表单重置
    reset() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        allotNo: null,
        warehouseCode: null,
        orderStatus: null
      };
      this.dateRange = [];
      this.resetForm("queryForm");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.reset();
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId);
      this.orderNos = selection.map(item => item.orderNo);
      this.single = selection.length!==1;
      this.multiple = !selection.length;
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.detailForm = {};
      this.detailList = [];
      getAllotInOrder(row.orderId).then(response => {
        this.detailForm = response.data;
        this.detailList = response.data.detailList || [];
        this.createQrCode(this.detailForm.orderNo);
        this.detailOpen = true;
      });
    },
    /** 生成二维码 */
    createQrCode(orderNo) {
      // 清除旧的二维码
      if (this.$refs.qrCodeUrl) {
        this.$refs.qrCodeUrl.innerHTML = '';
      }
      this.$nextTick(() => {
        new QRCode(this.$refs.qrCodeUrl, {
          text: 'ORDER:' + orderNo,
          width: 100,
          height: 100,
          colorDark: '#000000',
          colorLight: '#ffffff',
          correctLevel: QRCode.CorrectLevel.H
        });
      });
    },
    /** 打印按钮操作 */
    handlePrint(row) {
      this.detailForm = {};
      this.detailList = [];
      getAllotInOrder(row.orderId).then(response => {
        this.detailForm = response.data;
        this.detailList = response.data.detailList || [];
        this.createQrCode(this.detailForm.orderNo);
        this.detailOpen = true;
        // 自动触发打印
        this.$nextTick(() => {
          this.confirmPrint();
        });
      });
    },
    /** 确认打印 */
    confirmPrint() {
      printAllotInOrder(this.detailForm.orderId).then(response => {
        const binaryData = [];
        binaryData.push(response);
        let pdfUrl = window.URL.createObjectURL(new Blob(binaryData, { type: "application/pdf" }));
        window.open(pdfUrl);
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const orderIds = row.orderId || this.ids;
      const orderNoStr = row.orderNo || this.orderNos.join('、');
      this.$modal.confirm('是否确认删除调拨入库单单号为"' + orderNoStr + '"的数据项？').then(function() {
        return delAllotInOrder(orderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('stock/inOrder/export', {
        ...this.queryParams,
        orderType: 'allot'
      }, `allot_in_order_${new Date().getTime()}.xlsx`);
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.qr-code{
  margin: -35px 0 0 30px;
  width: 100px;
  height: 100px;
}
</style>