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
      <el-form-item label="工令号" prop="workNo">
        <el-input
          v-model="queryParams.workNo"
          placeholder="请输入工令号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择" clearable>
          <el-option
            v-for="item in orderStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="物料编码" prop="matCode">
        <el-input
          v-model="queryParams.matCode"
          placeholder="请输入物料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料名称" prop="matName">
        <el-input
          v-model="queryParams.matName"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车间" prop="workshopCode">
        <el-select v-model="queryParams.workshopCode" placeholder="请选择车间" clearable>
          <el-option
            v-for="item in workshopList"
            :key="item.workshopCode"
            :label="item.workshopName"
            :value="item.workshopCode"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="queryParams.priority" placeholder="请选择" clearable>
          <el-option :value="0" label="普通"></el-option>
          <el-option :value="1" label="紧急"></el-option>
          <el-option :value="2" label="特急"></el-option>
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
          v-hasPermi="['stock:prodOrder:add']"
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
          v-hasPermi="['stock:prodOrder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['stock:prodOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="prodOrderList" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="单据号" fixed align="center" prop="orderNo" width="180" />
      <el-table-column label="工令号" align="center" prop="workNo" width="120" />
      <el-table-column label="车间" align="center" prop="workshopName" width="120" />
      <el-table-column label="工艺路线" align="center" prop="routeName" width="140">
        <template slot-scope="scope">
          <span>{{ scope.row.routeName || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="物料编码" align="center" prop="matCode" width="120" />
      <el-table-column label="物料名称" align="center" prop="matName" width="180" />
      <el-table-column label="计划数量" align="center" prop="quantity" width="90" />
      <el-table-column label="完成数量" align="center" prop="actualQuantity" width="90" />
      <el-table-column label="优先级" align="center" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.priority === 2" type="danger" size="small">特急</el-tag>
          <el-tag v-else-if="scope.row.priority === 1" type="warning" size="small">紧急</el-tag>
          <el-tag v-else size="small">普通</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="客户订单" align="center" prop="customerOrderNo" width="180">
        <template slot-scope="scope">
          <span v-if="scope.row.customerOrderNo" style="color: #409EFF;">{{ scope.row.customerOrderNo }}</span>
          <span v-else style="color: #909399;">-</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.orderStatus === 'planned'" type="info">待排产</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 'ongoing'" type="primary">生产中</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 'completed'" type="success">已完工</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 'closed'">已关闭</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="计划时间" align="center" width="210">
        <template slot-scope="scope">
          <span v-if="scope.row.planStartDate">{{ parseTime(scope.row.planStartDate, '{y}-{m}-{d}') }} ~ {{ parseTime(scope.row.planEndDate, '{y}-{m}-{d}') }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >详情</el-button>
          <el-dropdown trigger="click" @command="(cmd) => handleCommand(cmd, scope.row)" style="margin-left: 10px;">
            <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-if="scope.row.orderStatus === 'planned'" command="edit" icon="el-icon-edit">编辑</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.orderStatus === 'planned'" command="schedule" icon="el-icon-date">排产</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.orderStatus === 'planned'" command="start" icon="el-icon-video-play">开工</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.orderStatus === 'ongoing'" command="complete" icon="el-icon-check">报工</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.orderStatus === 'completed'" command="close" icon="el-icon-lock">关闭</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.orderStatus === 'planned'" command="delete" icon="el-icon-delete" divided>删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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

    <!-- 新增工单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="工令号">
              <el-input v-model="form.workNo" disabled placeholder="系统自动生成" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车间" prop="workshopCode">
              <el-select v-model="form.workshopCode" placeholder="请选择车间" style="width: 100%">
                <el-option
                  v-for="item in workshopList"
                  :key="item.workshopCode"
                  :label="item.workshopName"
                  :value="item.workshopCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="物料" prop="matCode">
              <el-input readonly placeholder="请选择物料" v-model="form.matName">
                <el-button slot="append" icon="el-icon-search" @click="openSelectMatDialog"></el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工艺路线" prop="routeCode">
              <el-select v-model="form.routeCode" :placeholder="form.matCode ? '请选择工艺路线' : '请先选择物料'" clearable style="width: 100%">
                <el-option
                  v-for="item in processRouteList"
                  :key="item.routeCode"
                  :label="item.routeName"
                  :value="item.routeCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="计划数量" prop="quantity">
              <el-input-number v-model="form.quantity" controls-position="right" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="form.priority" placeholder="请选择优先级" style="width: 100%">
                <el-option :value="0" label="普通"></el-option>
                <el-option :value="1" label="紧急"></el-option>
                <el-option :value="2" label="特急"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="计划开始" prop="planStartDate">
              <el-date-picker v-model="form.planStartDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划完成" prop="planEndDate">
              <el-date-picker v-model="form.planEndDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 认</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 排产对话框 -->
    <el-dialog title="生产排产" :visible.sync="scheduleOpen" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="scheduleForm" :model="scheduleForm" :rules="scheduleRules" label-width="120px">
        <el-form-item label="单据号">
          <span>{{ scheduleForm.orderNo }}</span>
        </el-form-item>
        <el-form-item label="物料">
          <span>{{ scheduleForm.matName }}</span>
        </el-form-item>
        <el-form-item label="计划数量">
          <span>{{ scheduleForm.quantity }}</span>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="scheduleForm.priority" placeholder="请选择优先级">
            <el-option :value="0" label="普通"></el-option>
            <el-option :value="1" label="紧急"></el-option>
            <el-option :value="2" label="特急"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="计划开始时间" prop="planStartDate">
          <el-date-picker v-model="scheduleForm.planStartDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="计划完成时间" prop="planEndDate">
          <el-date-picker v-model="scheduleForm.planEndDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%"></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitSchedule">确 认</el-button>
        <el-button @click="scheduleOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 报工对话框 -->
    <el-dialog title="报工完工" :visible.sync="completeOpen" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="completeForm" :model="completeForm" :rules="completeRules" label-width="120px">
        <el-form-item label="单据号">
          <span>{{ completeForm.orderNo }}</span>
        </el-form-item>
        <el-form-item label="物料">
          <span>{{ completeForm.matName }}</span>
        </el-form-item>
        <el-form-item label="计划数量">
          <span>{{ completeForm.quantity }}</span>
        </el-form-item>
        <el-form-item label="实际完成数量" prop="actualQuantity">
          <el-input-number v-model="completeForm.actualQuantity" controls-position="right" :min="0" :precision="2" style="width: 100%"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitComplete">确 认</el-button>
        <el-button @click="completeOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 工单详情对话框 -->
    <el-dialog title="工单详情" :visible.sync="detailOpen" width="1000px" append-to-body :close-on-click-modal="false">
      <el-descriptions :column="3" border size="medium" v-if="detailData">
        <el-descriptions-item label="单据号">{{ detailData.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="工令号">{{ detailData.workNo }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="detailData.orderStatus === 'planned'" type="info">待排产</el-tag>
          <el-tag v-else-if="detailData.orderStatus === 'ongoing'" type="primary">生产中</el-tag>
          <el-tag v-else-if="detailData.orderStatus === 'completed'" type="success">已完工</el-tag>
          <el-tag v-else-if="detailData.orderStatus === 'closed'">已关闭</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="物料编码">{{ detailData.matCode }}</el-descriptions-item>
        <el-descriptions-item label="物料名称">{{ detailData.matName }}</el-descriptions-item>
        <el-descriptions-item label="车间">{{ detailData.workshopName }}</el-descriptions-item>
        <el-descriptions-item label="工艺路线">{{ detailData.routeName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="计划数量">{{ detailData.quantity }}</el-descriptions-item>
        <el-descriptions-item label="实际完成数量">{{ detailData.actualQuantity || '-' }}</el-descriptions-item>
        <el-descriptions-item label="优先级">
          <el-tag v-if="detailData.priority === 2" type="danger" size="small">特急</el-tag>
          <el-tag v-else-if="detailData.priority === 1" type="warning" size="small">紧急</el-tag>
          <el-tag v-else size="small">普通</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="计划开始时间">{{ detailData.planStartDate ? parseTime(detailData.planStartDate, '{y}-{m}-{d}') : '-' }}</el-descriptions-item>
        <el-descriptions-item label="计划完成时间">{{ detailData.planEndDate ? parseTime(detailData.planEndDate, '{y}-{m}-{d}') : '-' }}</el-descriptions-item>
        <el-descriptions-item label="客户订单号">
          <span v-if="detailData.customerOrderNo" style="color: #409EFF;">{{ detailData.customerOrderNo }}</span>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime ? parseTime(detailData.createTime, '{y}-{m}-{d} {h}:{i}') : '-' }}</el-descriptions-item>
        <el-descriptions-item label="实际开始时间">{{ detailData.actualStartDate ? parseTime(detailData.actualStartDate, '{y}-{m}-{d} {h}:{i}') : '-' }}</el-descriptions-item>
        <el-descriptions-item label="实际完成时间">{{ detailData.actualEndDate ? parseTime(detailData.actualEndDate, '{y}-{m}-{d} {h}:{i}') : '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
      </el-descriptions>

      <!-- 关联出库单 -->
      <div style="margin-top: 20px;">
        <h4 style="margin-bottom: 10px;">关联出库单</h4>
        <el-table :data="detailOutOrders" size="small" border v-loading="detailLoading">
          <el-table-column label="出库单号" align="center" prop="orderNo" width="180" />
          <el-table-column label="单据状态" align="center" prop="orderStatusLabel" width="100" />
          <el-table-column label="仓库" align="center" prop="warehouseName" width="100" />
          <el-table-column label="创建人" align="center" prop="createBy" width="100" />
          <el-table-column label="创建时间" align="center" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="!detailOutOrders || detailOutOrders.length === 0" description="暂无关联出库单" :image-size="60"></el-empty>
      </div>

      <!-- 关联入库单（完工入库） -->
      <div style="margin-top: 20px;">
        <h4 style="margin-bottom: 10px;">关联入库单（完工入库）</h4>
        <el-table :data="detailInOrders" size="small" border v-loading="detailLoading">
          <el-table-column label="入库单号" align="center" prop="orderNo" width="180" />
          <el-table-column label="单据状态" align="center" prop="orderStatusLabel" width="100" />
          <el-table-column label="质检状态" align="center" prop="checkStatusLabel" width="100" />
          <el-table-column label="创建时间" align="center" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="!detailInOrders || detailInOrders.length === 0" description="暂无关联入库单" :image-size="60"></el-empty>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="'选择物料'" :visible.sync="selectMatOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <selectMatBom @confirmSelect="confirmSelectMat"></selectMatBom>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelSelectMat">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProdOrder, getProdOrder, delProdOrder, addProdOrder, updateProdOrder, scheduleProdOrder, startProdOrder, completeProdOrder, closeProdOrder, getDetailProdOrder } from "@/api/stock/prodOrder";
import { listAllWorkshop } from "@/api/base/workshop";
import { listAllProcessRoute } from "@/api/base/processRoute";
import selectMatBom from "../../components/select-mat-bom/index"

export default {
  name: "ProdOrder",
  components: { selectMatBom },
  data() {
    return {
      loading: true,
      ids: [],
      orderNos: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      prodOrderList: [],
      title: "",
      open: false,
      submitLoading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        workNo: null,
        matCode: null,
        matName: null,
        workshopCode: null,
        quantity: null,
        orderStatus: null,
        priority: null,
      },
      form: {},
      rules: {
        workshopCode: [
          { required: true, message: "请选择车间", trigger: "blur" },
        ],
        matCode: [
          { required: true, message: "请选择物料", trigger: "blur" },
        ],
        quantity: [
          { required: true, message: "请输入数量", trigger: "blur" },
        ],
      },
      workshopList: [],
      processRouteList: [],
      selectMatOpen: false,
      orderStatusOptions:[
        {value: 'planned', label: '待排产'},
        {value: 'ongoing', label: '生产中'},
        {value: 'completed', label: '已完工'},
        {value: 'closed', label: '已关闭'}
      ],
      // 排产
      scheduleOpen: false,
      scheduleForm: {},
      scheduleRules: {
        priority: [{ required: true, message: "请选择优先级", trigger: "change" }],
        planStartDate: [{ required: true, message: "请选择计划开始时间", trigger: "change" }],
        planEndDate: [{ required: true, message: "请选择计划完成时间", trigger: "change" }],
      },
      // 报工
      completeOpen: false,
      completeForm: {},
      completeRules: {
        actualQuantity: [{ required: true, message: "请输入实际完成数量", trigger: "blur" }],
      },
      // 详情
      detailOpen: false,
      detailLoading: false,
      detailData: null,
      detailOutOrders: [],
      detailInOrders: [],
    };
  },
  created() {
    this.getList();
    this.getWorkshopList();
    this.getProcessRouteList();
  },
  methods: {
    getList() {
      this.loading = true;
      listProdOrder(this.queryParams).then(response => {
        this.prodOrderList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).finally(() => {
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        orderId: null,
        orderNo: null,
        workNo: null,
        matCode: null,
        matName: null,
        workshopCode: null,
        routeCode: null,
        routeName: null,
        quantity: null,
        priority: 0,
        planStartDate: null,
        planEndDate: null,
        orderStatus: null,
        delFlag: null,
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
      this.ids = selection.map(item => item.orderId)
      this.orderNos = selection.map(item => item.orderNo)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加生产工单";
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let that = this;
          if (that.form.orderId != null) {
            // 修改
            that.$modal.confirm('是否确认修改生产工单？').then(function() {
              that.submitLoading = true;
              updateProdOrder(that.form).then(response => {
                that.$modal.msgSuccess("修改成功");
                that.open = false;
                that.getList();
              }).finally(() => {
                that.submitLoading = false;
              });
            });
          } else {
            // 新增
            that.$modal.confirm('是否确认添加生产工单？').then(function() {
              that.submitLoading = true;
              addProdOrder(that.form).then(response => {
                that.$modal.msgSuccess("新增成功");
                that.open = false;
                that.getList();
              }).finally(() => {
                that.submitLoading = false;
              });
            });
          }
        }
      });
    },
    /** 排产 */
    handleSchedule(row) {
      this.scheduleForm = {
        orderId: row.orderId,
        orderNo: row.orderNo,
        matName: row.matName,
        quantity: row.quantity,
        priority: row.priority || 0,
        planStartDate: row.planStartDate,
        planEndDate: row.planEndDate,
      };
      this.scheduleOpen = true;
    },
    submitSchedule() {
      this.$refs["scheduleForm"].validate(valid => {
        if (valid) {
          let that = this;
          that.submitLoading = true;
          scheduleProdOrder(that.scheduleForm).then(response => {
            if (response.code === 200) {
              that.$modal.msgSuccess(response.msg);
              that.scheduleOpen = false;
              that.getList();
            } else {
              that.$modal.msgError(response.msg);
            }
          }).finally(() => {
            that.submitLoading = false;
          });
        }
      });
    },
    /** 开工 */
    handleStart(row) {
      this.$modal.confirm('开工后将自动根据BOM展开生成领料出库单，是否确认开工？工单号：' + row.orderNo).then(() => {
        return startProdOrder(row.orderId);
      }).then(response => {
        if (response.code === 200) {
          this.getList();
          this.$alert(response.msg, '开工成功', {
            confirmButtonText: '知道了',
            type: 'success'
          });
        } else {
          this.$modal.msgError(response.msg);
        }
      }).catch(() => {});
    },
    /** 操作下拉菜单命令分发 */
    handleCommand(command, row) {
      switch (command) {
        case 'edit': this.handleEdit(row); break;
        case 'schedule': this.handleSchedule(row); break;
        case 'start': this.handleStart(row); break;
        case 'complete': this.handleComplete(row); break;
        case 'close': this.handleClose(row); break;
        case 'delete': this.handleDelete(row); break;
      }
    },
    /** 详情 */
    handleDetail(row) {
      this.detailLoading = true;
      this.detailOpen = true;
      this.detailData = null;
      this.detailOutOrders = [];
      this.detailInOrders = [];
      getDetailProdOrder(row.orderId).then(response => {
        if (response.code === 200) {
          this.detailData = response.data.prodOrder;
          this.detailOutOrders = response.data.outOrders || [];
          this.detailInOrders = response.data.inOrders || [];
        }
      }).finally(() => {
        this.detailLoading = false;
      });
    },
    /** 编辑（仅待排产状态可编辑） */
    handleEdit(row) {
      this.reset();
      getProdOrder(row.orderId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改生产工单";
        // 编辑时根据物料编码加载关联的工艺路线
        if (this.form.matCode) {
          listAllProcessRoute({ routeStatus: '0', matCode: this.form.matCode }).then(res => {
            if (res && res.length > 0) {
              this.processRouteList = res;
            } else {
              this.getProcessRouteList();
            }
          });
        }
      });
    },
    /** 报工 */
    handleComplete(row) {
      this.completeForm = {
        orderId: row.orderId,
        orderNo: row.orderNo,
        matName: row.matName,
        quantity: row.quantity,
        actualQuantity: null,
      };
      this.completeOpen = true;
    },
    submitComplete() {
      this.$refs["completeForm"].validate(valid => {
        if (valid) {
          let that = this;
          that.$modal.confirm('报工后将自动生成成品入库单，是否确认？').then(function() {
            that.submitLoading = true;
            completeProdOrder(that.completeForm).then(response => {
              if (response.code === 200) {
                that.completeOpen = false;
                that.getList();
                // 用通知提示入库单号，不阻塞用户操作
                that.$notify({
                  title: '报工成功',
                  message: response.msg,
                  type: 'success',
                  duration: 6000
                });
              } else {
                that.$modal.msgError(response.msg);
              }
            }).finally(() => {
              that.submitLoading = false;
            });
          });
        }
      });
    },
    /** 关闭工单 */
    handleClose(row) {
      this.$modal.confirm('是否确认关闭工单？工单号：' + row.orderNo).then(() => {
        return closeProdOrder(row.orderId);
      }).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess(response.msg);
          this.getList();
        } else {
          this.$modal.msgError(response.msg);
        }
      }).catch(() => {});
    },
    handleDelete(row) {
      const orderIds = row.orderId || this.ids;
      const delOrderNos = row.orderNo || this.orderNos;
      this.$modal.confirm('是否确认删除生产工单号为 "' + delOrderNos + '" 的数据项？').then(function() {
        return delProdOrder(orderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('stock/prodOrder/export', {
        ...this.queryParams
      }, `prodOrder_${new Date().getTime()}.xlsx`)
    },
    openSelectMatDialog(){
      this.selectMatOpen = true;
    },
    cancelSelectMat(){
      this.selectMatOpen = false;
    },
    confirmSelectMat(item){
      this.form.matCode = item.matCode;
      this.form.matName = item.matName;
      this.selectMatOpen = false;
      // 选择物料后，按物料编码筛选关联的工艺路线
      this.form.routeCode = null;
      listAllProcessRoute({ routeStatus: '0', matCode: item.matCode }).then(response => {
        if (response && response.length > 0) {
          this.processRouteList = response;
          // 如果只有一条关联路线，自动选中
          if (response.length === 1) {
            this.form.routeCode = response[0].routeCode;
          }
        } else {
          // 该物料没有关联工艺路线，加载全部供手动选择
          this.getProcessRouteList();
        }
      });
    },
    getWorkshopList(){
      listAllWorkshop().then(response => {
        this.workshopList = response;
      });
    },
    getProcessRouteList(){
      listAllProcessRoute({ routeStatus: '0' }).then(response => {
        this.processRouteList = response;
      });
    },
  }
};
</script>
