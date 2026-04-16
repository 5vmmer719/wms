<template>
  <div class="order-progress-container">
    <!-- 顶部统计卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :xs="12" :sm="6">
        <div class="mini-card card-order" @click="$router.push('/order/customerOrder')">
          <div class="mini-icon"><i class="el-icon-document"></i></div>
          <div class="mini-info">
            <div class="mini-value">{{ progressData.orderTotal || 0 }}</div>
            <div class="mini-label">客户订单</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="mini-card card-prod">
          <div class="mini-icon"><i class="el-icon-setting"></i></div>
          <div class="mini-info">
            <div class="mini-value">{{ progressData.prodCompletionRate || 0 }}%</div>
            <div class="mini-label">生产完成率</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="mini-card card-quality">
          <div class="mini-icon"><i class="el-icon-circle-check"></i></div>
          <div class="mini-info">
            <div class="mini-value">{{ progressData.qualityPassRate || 0 }}%</div>
            <div class="mini-label">质检合格率</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="mini-card card-delivery">
          <div class="mini-icon"><i class="el-icon-truck"></i></div>
          <div class="mini-info">
            <div class="mini-value">{{ progressData.deliveryTotal || 0 }}</div>
            <div class="mini-label">交付单</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :xs="24" :sm="12">
        <div class="chart-card">
          <div class="chart-header">
            <span class="chart-title"><i class="el-icon-pie-chart"></i> 客户订单状态分布</span>
          </div>
          <div class="chart-body" ref="orderPieChart"></div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12">
        <div class="chart-card">
          <div class="chart-header">
            <span class="chart-title"><i class="el-icon-data-board"></i> 生产工单状态分布</span>
          </div>
          <div class="chart-body" ref="prodPieChart"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-section">
      <el-col :xs="24" :sm="12">
        <div class="chart-card">
          <div class="chart-header">
            <span class="chart-title"><i class="el-icon-data-analysis"></i> 本周订单与生产趋势</span>
          </div>
          <div class="chart-body" ref="weekTrendChart"></div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12">
        <div class="chart-card">
          <div class="chart-header">
            <span class="chart-title"><i class="el-icon-s-check"></i> 质检任务状态</span>
          </div>
          <div class="chart-body" ref="qualityChart"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 预警列表 -->
    <div class="chart-card warning-section">
      <div class="chart-header">
        <span class="chart-title"><i class="el-icon-warning" style="color:#e6a23c"></i> 异常预警</span>
        <el-badge :value="warnings.length" :hidden="warnings.length === 0" class="warning-badge">
          <el-button size="mini" type="text" @click="refreshWarnings">刷新</el-button>
        </el-badge>
      </div>
      <div class="warning-body">
        <div v-if="warnings.length === 0" class="empty-warning">
          <i class="el-icon-circle-check" style="font-size:40px;color:#67c23a"></i>
          <p>暂无预警信息，一切正常</p>
        </div>
        <div v-else class="warning-list">
          <div v-for="(item, index) in warnings" :key="index"
               :class="['warning-item', 'warning-' + item.level]">
            <div class="warning-icon">
              <i :class="getWarningIcon(item.type)"></i>
            </div>
            <div class="warning-content">
              <div class="warning-title">{{ item.title }}</div>
              <div class="warning-desc">{{ item.content }}</div>
            </div>
            <el-tag size="mini" :type="item.level === 'danger' ? 'danger' : 'warning'">
              {{ item.level === 'danger' ? '紧急' : '警告' }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getOrderProgress, getOrderWarnings } from "@/api/stats/index"
import { debounce } from '@/utils'

export default {
  name: 'OrderProgress',
  data() {
    return {
      progressData: {},
      warnings: [],
      orderPieChart: null,
      prodPieChart: null,
      weekTrendChart: null,
      qualityChart: null,
    }
  },
  created() {
    this.loadData()
  },
  mounted() {
    this.__resizeHandler = debounce(() => {
      if (this.orderPieChart) this.orderPieChart.resize()
      if (this.prodPieChart) this.prodPieChart.resize()
      if (this.weekTrendChart) this.weekTrendChart.resize()
      if (this.qualityChart) this.qualityChart.resize()
    }, 100)
    window.addEventListener('resize', this.__resizeHandler)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.__resizeHandler)
    if (this.orderPieChart) this.orderPieChart.dispose()
    if (this.prodPieChart) this.prodPieChart.dispose()
    if (this.weekTrendChart) this.weekTrendChart.dispose()
    if (this.qualityChart) this.qualityChart.dispose()
  },
  methods: {
    loadData() {
      getOrderProgress().then(res => {
        this.progressData = res.data
        this.$nextTick(() => {
          this.initOrderPieChart()
          this.initProdPieChart()
          this.initWeekTrendChart()
          this.initQualityChart()
        })
      })
      this.refreshWarnings()
    },
    refreshWarnings() {
      getOrderWarnings().then(res => {
        this.warnings = res.data || []
      })
    },
    getWarningIcon(type) {
      const icons = {
        overdue: 'el-icon-time',
        urgent: 'el-icon-alarm-clock',
        quality: 'el-icon-warning-outline',
        prodDelay: 'el-icon-loading',
      }
      return icons[type] || 'el-icon-warning'
    },
    getWeekLabels() {
      const labels = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      return labels
    },
    initOrderPieChart() {
      const dom = this.$refs.orderPieChart
      if (!dom) return
      this.orderPieChart = echarts.init(dom)
      const dist = this.progressData.orderStatusDist || {}
      const statusLabels = {
        created: '已创建', confirmed: '已确认', producing: '生产中',
        completed: '已完成', delivered: '已交付', closed: '已关闭'
      }
      const colors = ['#909399', '#e6a23c', '#409eff', '#67c23a', '#0d84ff', '#c0c4cc']
      const data = Object.keys(statusLabels).map((k, i) => ({
        name: statusLabels[k], value: dist[k] || 0, itemStyle: { color: colors[i] }
      })).filter(d => d.value > 0)

      this.orderPieChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: 0, textStyle: { fontSize: 12 } },
        series: [{
          type: 'pie', radius: ['40%', '65%'], center: ['50%', '45%'],
          avoidLabelOverlap: true,
          label: { show: true, formatter: '{b}\n{c}', fontSize: 12 },
          data: data
        }]
      })
    },
    initProdPieChart() {
      const dom = this.$refs.prodPieChart
      if (!dom) return
      this.prodPieChart = echarts.init(dom)
      const dist = this.progressData.prodStatusDist || {}
      const statusLabels = {
        planned: '待排产', ongoing: '生产中', completed: '已完工', closed: '已关闭'
      }
      const colors = ['#e6a23c', '#409eff', '#67c23a', '#c0c4cc']
      const data = Object.keys(statusLabels).map((k, i) => ({
        name: statusLabels[k], value: dist[k] || 0, itemStyle: { color: colors[i] }
      })).filter(d => d.value > 0)

      this.prodPieChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: 0, textStyle: { fontSize: 12 } },
        series: [{
          type: 'pie', radius: ['40%', '65%'], center: ['50%', '45%'],
          label: { show: true, formatter: '{b}\n{c}', fontSize: 12 },
          data: data
        }]
      })
    },
    initWeekTrendChart() {
      const dom = this.$refs.weekTrendChart
      if (!dom) return
      this.weekTrendChart = echarts.init(dom)
      const labels = this.getWeekLabels()
      const orderArr = this.progressData.orderCreatedArr || [0,0,0,0,0,0,0]
      const prodArr = this.progressData.prodCompletedArr || [0,0,0,0,0,0,0]

      // 截断到当前星期几
      const today = new Date().getDay()
      const dayIndex = today === 0 ? 6 : today - 1
      const trimmedLabels = labels.slice(0, dayIndex + 1)
      const trimmedOrder = orderArr.slice(0, dayIndex + 1)
      const trimmedProd = prodArr.slice(0, dayIndex + 1)

      this.weekTrendChart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { bottom: 0, data: ['新增订单', '完工工单'], textStyle: { fontSize: 12 } },
        grid: { top: 20, right: 20, bottom: 40, left: 40 },
        xAxis: { type: 'category', data: trimmedLabels, axisLabel: { fontSize: 12 } },
        yAxis: { type: 'value', minInterval: 1, axisLabel: { fontSize: 12 } },
        series: [
          {
            name: '新增订单', type: 'bar', barWidth: '30%',
            data: trimmedOrder,
            itemStyle: { color: '#409eff', borderRadius: [4, 4, 0, 0] }
          },
          {
            name: '完工工单', type: 'bar', barWidth: '30%',
            data: trimmedProd,
            itemStyle: { color: '#67c23a', borderRadius: [4, 4, 0, 0] }
          }
        ]
      })
    },
    initQualityChart() {
      const dom = this.$refs.qualityChart
      if (!dom) return
      this.qualityChart = echarts.init(dom)
      const dist = this.progressData.qualityStatusDist || {}
      const statusLabels = {
        pending: '待检验', checking: '检验中', passed: '合格', failed: '不合格'
      }
      const colors = ['#909399', '#e6a23c', '#67c23a', '#f56c6c']
      const data = Object.keys(statusLabels).map((k, i) => ({
        name: statusLabels[k], value: dist[k] || 0, itemStyle: { color: colors[i] }
      })).filter(d => d.value > 0)

      this.qualityChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: 0, textStyle: { fontSize: 12 } },
        graphic: [{
          type: 'text', left: 'center', top: '38%',
          style: {
            text: (this.progressData.qualityPassRate || 0) + '%',
            fontSize: 22, fontWeight: 'bold', fill: '#303133', textAlign: 'center'
          }
        }, {
          type: 'text', left: 'center', top: '50%',
          style: { text: '合格率', fontSize: 12, fill: '#909399', textAlign: 'center' }
        }],
        series: [{
          type: 'pie', radius: ['45%', '65%'], center: ['50%', '45%'],
          label: { show: false },
          data: data
        }]
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.order-progress-container {
  padding: 16px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);
}

.stat-cards { margin-bottom: 16px; }

.mini-card {
  display: flex; align-items: center; gap: 12px;
  background: #fff; border-radius: 10px; padding: 16px 20px;
  cursor: pointer; transition: all 0.3s; margin-bottom: 16px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  &:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.08); }
  .mini-icon {
    width: 44px; height: 44px; border-radius: 10px;
    display: flex; align-items: center; justify-content: center;
    i { font-size: 22px; color: #fff; }
  }
  .mini-info {
    .mini-value { font-size: 24px; font-weight: 700; color: #1f2937; line-height: 1.2; }
    .mini-label { font-size: 13px; color: #6b7280; margin-top: 2px; }
  }
}
.card-order .mini-icon { background: linear-gradient(135deg, #409eff, #66b1ff); }
.card-prod .mini-icon { background: linear-gradient(135deg, #e6a23c, #f0c78a); }
.card-quality .mini-icon { background: linear-gradient(135deg, #67c23a, #85ce61); }
.card-delivery .mini-icon { background: linear-gradient(135deg, #8b5cf6, #a78bfa); }

.chart-section { margin-bottom: 0; }

.chart-card {
  background: #fff; border-radius: 10px; margin-bottom: 16px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04); overflow: hidden;
  .chart-header {
    display: flex; justify-content: space-between; align-items: center;
    padding: 14px 20px; border-bottom: 1px solid #f0f0f0;
    .chart-title {
      font-size: 15px; font-weight: 600; color: #1f2937;
      i { margin-right: 6px; color: #409eff; }
    }
  }
  .chart-body { height: 300px; padding: 10px; }
}

.warning-section {
  .warning-body { padding: 16px 20px; }
  .empty-warning {
    text-align: center; padding: 30px 0; color: #909399;
    p { margin-top: 10px; font-size: 14px; }
  }
  .warning-list { max-height: 360px; overflow-y: auto; }
  .warning-item {
    display: flex; align-items: center; gap: 12px;
    padding: 12px 16px; border-radius: 8px; margin-bottom: 8px;
    transition: all 0.2s;
    &:last-child { margin-bottom: 0; }
    &:hover { transform: translateX(4px); }
    .warning-icon {
      width: 36px; height: 36px; border-radius: 8px;
      display: flex; align-items: center; justify-content: center; flex-shrink: 0;
      i { font-size: 18px; }
    }
    .warning-content { flex: 1; min-width: 0;
      .warning-title { font-size: 14px; font-weight: 600; color: #303133; }
      .warning-desc { font-size: 13px; color: #606266; margin-top: 2px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
    }
  }
  .warning-danger {
    background: #fef0f0;
    .warning-icon { background: rgba(245,108,108,0.15); i { color: #f56c6c; } }
  }
  .warning-warning {
    background: #fdf6ec;
    .warning-icon { background: rgba(230,162,60,0.15); i { color: #e6a23c; } }
  }
}

.warning-badge { margin-left: 8px; }
</style>

