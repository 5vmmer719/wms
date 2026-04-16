<template>
  <div class="app-container quality-trace">
    <!-- 搜索区域 -->
    <el-card shadow="hover" class="search-card">
      <div class="search-header">
        <i class="el-icon-search"></i>
        <span>质量追溯查询</span>
      </div>
      <el-form :inline="true" size="medium" @submit.native.prevent="handleTrace">
        <el-form-item>
          <el-input v-model="keyword" placeholder="输入生产工单号 / 入库单号 / 出库单号 / 批次号" clearable style="width:400px" @keyup.enter.native="handleTrace">
            <el-button slot="append" icon="el-icon-search" @click="handleTrace">追溯</el-button>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-tag type="info" size="small">支持：PO/RK/CK开头的单据号 或 批次号</el-tag>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 追溯链展示 -->
    <el-card v-if="traceData" shadow="hover" class="trace-card" v-loading="traceLoading">
      <div slot="header" class="card-header">
        <span><i class="el-icon-connection"></i> 追溯链路</span>
        <el-tag :type="traceTypeTag.type" size="small">{{ traceTypeTag.label }}</el-tag>
      </div>

      <!-- 追溯流程图 -->
      <div class="trace-flow">
        <!-- 供应商 -->
        <div class="flow-node" v-if="traceData.suppliers && traceData.suppliers.length > 0">
          <div class="node-box node-supplier">
            <div class="node-icon"><i class="el-icon-office-building"></i></div>
            <div class="node-title">供应商</div>
            <div class="node-detail" v-for="s in traceData.suppliers" :key="s.supplierCode">
              {{ s.supplierName || s.supplierCode }}
            </div>
          </div>
          <div class="flow-arrow"><i class="el-icon-right"></i></div>
        </div>

        <!-- 物料标签/批次 -->
        <div class="flow-node" v-if="traceData.matLabels && traceData.matLabels.length > 0">
          <div class="node-box node-label">
            <div class="node-icon"><i class="el-icon-price-tag"></i></div>
            <div class="node-title">物料批次</div>
            <div class="node-detail" v-for="l in traceData.matLabels.slice(0, 3)" :key="l.labelCode">
              {{ l.matName }} / {{ l.batch }}
            </div>
            <div class="node-detail" v-if="traceData.matLabels.length > 3">...共{{ traceData.matLabels.length }}条</div>
          </div>
          <div class="flow-arrow"><i class="el-icon-right"></i></div>
        </div>

        <!-- 领料出库 -->
        <div class="flow-node" v-if="traceData.outOrders && traceData.outOrders.length > 0">
          <div class="node-box node-out">
            <div class="node-icon"><i class="el-icon-top-right"></i></div>
            <div class="node-title">领料出库</div>
            <div class="node-detail" v-for="o in traceData.outOrders" :key="o.orderNo">
              {{ o.orderNo }}
            </div>
          </div>
          <div class="flow-arrow"><i class="el-icon-right"></i></div>
        </div>

        <!-- 生产工单（核心节点） -->
        <div class="flow-node" v-if="prodOrderNode">
          <div class="node-box node-prod node-highlight">
            <div class="node-icon"><i class="el-icon-setting"></i></div>
            <div class="node-title">生产工单</div>
            <div class="node-detail">{{ prodOrderNode.orderNo }}</div>
            <div class="node-detail">{{ prodOrderNode.matName }}</div>
            <div class="node-detail">数量: {{ prodOrderNode.quantity }}</div>
            <el-tag :type="prodStatusType(prodOrderNode.orderStatus)" size="mini">{{ prodStatusLabel(prodOrderNode.orderStatus) }}</el-tag>
          </div>
          <div class="flow-arrow"><i class="el-icon-right"></i></div>
        </div>

        <!-- 质检任务 -->
        <div class="flow-node" v-if="traceData.qualityTasks && traceData.qualityTasks.length > 0">
          <div class="node-box node-quality">
            <div class="node-icon"><i class="el-icon-finished"></i></div>
            <div class="node-title">质检</div>
            <div class="node-detail" v-for="t in traceData.qualityTasks" :key="t.taskNo">
              {{ t.taskNo }}
              <el-tag :type="taskStatusType(t.taskStatus)" size="mini">{{ taskStatusLabel(t.taskStatus) }}</el-tag>
            </div>
          </div>
          <div class="flow-arrow"><i class="el-icon-right"></i></div>
        </div>

        <!-- 完工入库 -->
        <div class="flow-node" v-if="traceData.inOrders && traceData.inOrders.length > 0">
          <div class="node-box node-in">
            <div class="node-icon"><i class="el-icon-bottom-left"></i></div>
            <div class="node-title">完工入库</div>
            <div class="node-detail" v-for="i in traceData.inOrders" :key="i.orderNo">
              {{ i.orderNo }}
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 追溯详情 -->
    <el-row :gutter="16" v-if="traceData" style="margin-top:16px">
      <!-- 质检结果详情 -->
      <el-col :span="24" v-if="traceData.qualityTasks && traceData.qualityTasks.length > 0">
        <el-card shadow="hover">
          <div slot="header"><i class="el-icon-document-checked"></i> 质检结果详情</div>
          <el-collapse v-model="activeTask">
            <el-collapse-item v-for="task in traceData.qualityTasks" :key="task.taskNo" :name="task.taskNo" :title="task.taskNo + ' - ' + task.matName + ' (' + taskStatusLabel(task.taskStatus) + ')'">
              <el-descriptions :column="4" size="small" border style="margin-bottom:10px">
                <el-descriptions-item label="任务编号">{{ task.taskNo }}</el-descriptions-item>
                <el-descriptions-item label="检验类型">{{ checkTypeLabel(task.checkType) }}</el-descriptions-item>
                <el-descriptions-item label="来源单号">{{ task.sourceNo }}</el-descriptions-item>
                <el-descriptions-item label="状态">
                  <el-tag :type="taskStatusType(task.taskStatus)" size="mini">{{ taskStatusLabel(task.taskStatus) }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="合格数量">{{ task.qualifiedQty || '-' }}</el-descriptions-item>
                <el-descriptions-item label="不合格数量">{{ task.unqualifiedQty || '-' }}</el-descriptions-item>
                <el-descriptions-item label="质检员">{{ task.inspectorName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="检验时间">{{ task.checkTime || '-' }}</el-descriptions-item>
              </el-descriptions>
              <el-table :data="task.resultList || []" border size="mini" v-if="task.resultList && task.resultList.length > 0">
                <el-table-column label="检验项" prop="itemName" min-width="120" />
                <el-table-column label="标准值" prop="standardValue" width="100" align="center" />
                <el-table-column label="下限" prop="minValue" width="80" align="center" />
                <el-table-column label="上限" prop="maxValue" width="80" align="center" />
                <el-table-column label="实测值" prop="actualValue" width="100" align="center" />
                <el-table-column label="判定" width="80" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.judgeResult === '0' ? 'success' : 'danger'" size="mini">
                      {{ scope.row.judgeResult === '0' ? '合格' : '不合格' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="缺陷类型" prop="defectType" width="100" align="center">
                  <template slot-scope="scope">{{ scope.row.defectType || '-' }}</template>
                </el-table-column>
                <el-table-column label="缺陷等级" width="80" align="center">
                  <template slot-scope="scope">
                    <el-tag v-if="scope.row.defectLevel === 'critical'" type="danger" size="mini">致命</el-tag>
                    <el-tag v-else-if="scope.row.defectLevel === 'major'" type="warning" size="mini">严重</el-tag>
                    <el-tag v-else-if="scope.row.defectLevel === 'minor'" type="info" size="mini">轻微</el-tag>
                    <span v-else>-</span>
                  </template>
                </el-table-column>
              </el-table>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
    </el-row>

    <!-- 领料明细 -->
    <el-card v-if="traceData && traceData.outOrders && traceData.outOrders.length > 0" shadow="hover" style="margin-top:16px">
      <div slot="header"><i class="el-icon-document"></i> 领料出库明细</div>
      <el-table :data="allOutDetails" border size="small">
        <el-table-column label="出库单号" prop="orderNo" width="140" align="center" />
        <el-table-column label="物料编码" prop="matCode" width="120" align="center" />
        <el-table-column label="物料名称" prop="matName" min-width="140" align="center" />
        <el-table-column label="批次" prop="batch" width="120" align="center" />
        <el-table-column label="数量" prop="quantity" width="100" align="center" />
        <el-table-column label="供应商" prop="supplierName" width="140" align="center">
          <template slot-scope="scope">{{ scope.row.supplierName || '-' }}</template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 质量统计图表 -->
    <el-row :gutter="16" style="margin-top:16px">
      <el-col :xs="24" :sm="12">
        <el-card shadow="hover">
          <div slot="header"><i class="el-icon-pie-chart"></i> 各类检验合格率</div>
          <div ref="passRateChart" style="height:300px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12">
        <el-card shadow="hover">
          <div slot="header"><i class="el-icon-data-analysis"></i> 缺陷等级分布</div>
          <div ref="defectChart" style="height:300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 统计卡片 -->
    <el-row :gutter="16" style="margin-top:16px" v-if="statsData.totalChecked !== undefined">
      <el-col :xs="12" :sm="6">
        <div class="stat-card stat-total">
          <div class="stat-value">{{ statsData.totalChecked || 0 }}</div>
          <div class="stat-label">已检验总数</div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card stat-pass">
          <div class="stat-value">{{ statsData.overallPassRate || 0 }}%</div>
          <div class="stat-label">总体合格率</div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card stat-fail">
          <div class="stat-value">{{ statsData.totalFailed || 0 }}</div>
          <div class="stat-label">不合格数</div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card stat-pending">
          <div class="stat-value">{{ statsData.totalPending || 0 }}</div>
          <div class="stat-label">待检验</div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { traceQuery, qualityStats } from "@/api/base/qualityTrace"
import { debounce } from '@/utils'

export default {
  name: 'QualityTrace',
  data() {
    return {
      keyword: '',
      traceData: null,
      traceLoading: false,
      statsData: {},
      activeTask: [],
      passRateChart: null,
      defectChart: null,
    }
  },
  computed: {
    prodOrderNode() {
      if (!this.traceData || !this.traceData.traceChain) return null;
      return this.traceData.traceChain.find(n => n.nodeType === 'prodOrder');
    },
    traceTypeTag() {
      if (!this.traceData) return { type: 'info', label: '' };
      const map = {
        prodOrder: { type: 'primary', label: '生产工单追溯' },
        inOrder: { type: 'success', label: '入库单追溯' },
        outOrder: { type: 'warning', label: '出库单追溯' },
        batch: { type: 'info', label: '批次追溯' },
      };
      return map[this.traceData.traceType] || { type: 'info', label: '追溯' };
    },
    allOutDetails() {
      if (!this.traceData || !this.traceData.outOrders) return [];
      let details = [];
      this.traceData.outOrders.forEach(o => {
        if (o.details) {
          o.details.forEach(d => {
            details.push({ ...d, orderNo: o.orderNo });
          });
        }
      });
      return details;
    }
  },
  created() {
    this.loadStats();
  },
  mounted() {
    this.__resizeHandler = debounce(() => {
      if (this.passRateChart) this.passRateChart.resize();
      if (this.defectChart) this.defectChart.resize();
    }, 100);
    window.addEventListener('resize', this.__resizeHandler);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.__resizeHandler);
    if (this.passRateChart) this.passRateChart.dispose();
    if (this.defectChart) this.defectChart.dispose();
  },
  methods: {
    handleTrace() {
      if (!this.keyword) {
        this.$modal.msgWarning('请输入追溯关键字');
        return;
      }
      this.traceLoading = true;
      traceQuery(this.keyword).then(response => {
        this.traceData = response.data;
        this.traceLoading = false;
        // 展开所有质检任务
        if (this.traceData.qualityTasks) {
          this.activeTask = this.traceData.qualityTasks.map(t => t.taskNo);
        }
      }).catch(() => {
        this.traceLoading = false;
      });
    },
    loadStats() {
      qualityStats().then(response => {
        this.statsData = response.data || {};
        this.$nextTick(() => {
          this.initPassRateChart();
          this.initDefectChart();
        });
      });
    },
    initPassRateChart() {
      if (!this.$refs.passRateChart) return;
      this.passRateChart = echarts.init(this.$refs.passRateChart);
      const typeStats = this.statsData.typeStats || {};
      const categories = [];
      const passedData = [];
      const failedData = [];
      Object.keys(typeStats).forEach(key => {
        const stat = typeStats[key];
        categories.push(stat.typeName);
        passedData.push(stat.passed);
        failedData.push(stat.failed);
      });
      this.passRateChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        legend: { data: ['合格', '不合格'] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: categories },
        yAxis: { type: 'value', name: '数量' },
        series: [
          { name: '合格', type: 'bar', stack: 'total', data: passedData, itemStyle: { color: '#67c23a' } },
          { name: '不合格', type: 'bar', stack: 'total', data: failedData, itemStyle: { color: '#f56c6c' } },
        ]
      });
    },
    initDefectChart() {
      if (!this.$refs.defectChart) return;
      this.defectChart = echarts.init(this.$refs.defectChart);
      const levelDist = this.statsData.defectLevelDist || {};
      const levelNames = { minor: '轻微', major: '严重', critical: '致命' };
      const levelColors = { minor: '#909399', major: '#e6a23c', critical: '#f56c6c' };
      // 固定顺序：轻微 -> 严重 -> 致命
      const levelOrder = ['minor', 'major', 'critical'];
      const data = levelOrder
        .filter(key => levelDist[key] !== undefined)
        .map(key => ({
          name: levelNames[key] || key,
          value: levelDist[key],
          itemStyle: { color: levelColors[key] || '#409eff' }
        }));
      const total = data.reduce((sum, d) => sum + d.value, 0);
      this.defectChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: function(params) {
            return '<strong>' + params.name + '</strong><br/>'
              + '数量：' + params.value + ' 件<br/>'
              + '占比：' + params.percent + '%';
          }
        },
        legend: {
          bottom: '2%',
          left: 'center',
          icon: 'circle',
          itemWidth: 10,
          itemHeight: 10,
          itemGap: 20,
          textStyle: { fontSize: 13, color: '#606266' }
        },
        graphic: total === 0 ? [{
          type: 'text',
          left: 'center',
          top: '40%',
          style: {
            text: '暂无数据',
            fontSize: 14,
            fill: '#909399',
            textAlign: 'center'
          }
        }] : [],
        series: [{
          type: 'pie',
          radius: ['35%', '60%'],
          center: ['50%', '45%'],
          avoidLabelOverlap: true,
          itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
          label: {
            show: true,
            position: 'outside',
            formatter: function(params) {
              if (params.value === 0) return '';
              return params.name + '  ' + params.value + '件';
            },
            fontSize: 12,
            color: '#606266'
          },
          labelLine: {
            show: true,
            length: 12,
            length2: 16,
            smooth: true
          },
          emphasis: {
            label: { show: true, fontSize: 14, fontWeight: 'bold' },
            scaleSize: 6
          },
          data: data
        }]
      });
    },
    prodStatusType(status) {
      const map = { planned: 'info', ongoing: 'primary', completed: 'success', closed: 'warning' };
      return map[status] || 'info';
    },
    prodStatusLabel(status) {
      const map = { planned: '已计划', ongoing: '生产中', completed: '已完工', closed: '已关闭' };
      return map[status] || status;
    },
    taskStatusType(status) {
      const map = { pending: 'info', checking: 'primary', passed: 'success', failed: 'danger' };
      return map[status] || 'info';
    },
    taskStatusLabel(status) {
      const map = { pending: '待检验', checking: '检验中', passed: '合格', failed: '不合格' };
      return map[status] || status;
    },
    checkTypeLabel(type) {
      const map = { incoming: '来料检验', process: '过程检验', final: '终检' };
      return map[type] || type;
    },
  }
}
</script>

<style scoped>
.search-card { margin-bottom: 16px; }
.search-header { font-size: 16px; font-weight: bold; margin-bottom: 12px; color: #303133; }
.search-header i { margin-right: 6px; color: #409eff; }
.card-header { display: flex; justify-content: space-between; align-items: center; }

/* 追溯流程图 */
.trace-flow {
  display: flex; align-items: flex-start; overflow-x: auto; padding: 20px 10px;
  min-height: 160px;
}
.flow-node { display: flex; align-items: center; }
.flow-arrow { font-size: 24px; color: #c0c4cc; margin: 0 8px; flex-shrink: 0; }
.node-box {
  min-width: 140px; max-width: 200px; padding: 12px 16px;
  border-radius: 8px; border: 2px solid #dcdfe6;
  background: #fff; text-align: center; transition: all 0.3s;
}
.node-box:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
.node-highlight { border-color: #409eff; background: #ecf5ff; }
.node-icon { font-size: 28px; margin-bottom: 6px; }
.node-title { font-weight: bold; font-size: 13px; margin-bottom: 4px; }
.node-detail { font-size: 12px; color: #606266; line-height: 1.6; word-break: break-all; }

.node-supplier .node-icon { color: #909399; }
.node-supplier { border-color: #909399; }
.node-label .node-icon { color: #e6a23c; }
.node-label { border-color: #e6a23c; }
.node-out .node-icon { color: #f56c6c; }
.node-out { border-color: #f56c6c; }
.node-prod .node-icon { color: #409eff; }
.node-prod { border-color: #409eff; }
.node-quality .node-icon { color: #67c23a; }
.node-quality { border-color: #67c23a; }
.node-in .node-icon { color: #409eff; }
.node-in { border-color: #409eff; }

/* 统计卡片 */
.stat-card {
  padding: 20px; border-radius: 8px; text-align: center;
  background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
.stat-value { font-size: 28px; font-weight: 700; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
.stat-total .stat-value { color: #409eff; }
.stat-pass .stat-value { color: #67c23a; }
.stat-fail .stat-value { color: #f56c6c; }
.stat-pending .stat-value { color: #e6a23c; }
</style>

