<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons')
import resize from './mixins/resize'

import { statsIndexMiddle } from "@/api/stats/index";

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '280px'
    },
  },
  data() {
    return {
      chart: null,
      barChartData: {},
    }
  },
  created() {
    this.loadDate();
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    loadDate() {
      statsIndexMiddle().then(response => {
        this.barChartData = response.data;
        this.initChart(this.barChartData);
      });
    },
    // 获取当前是周几（1-7，周一为1）
    getCurrentDayOfWeek() {
      const day = new Date().getDay();
      return day === 0 ? 7 : day; // 周日转为7
    },
    // 截断未来日期的数据
    truncateFutureData(data) {
      const currentDay = this.getCurrentDayOfWeek();
      return data.map((value, index) => {
        // index从0开始，对应周一到周日
        return index < currentDay ? value : null;
      });
    },
    initChart(barChartData) {
      const currentDay = this.getCurrentDayOfWeek();
      const purchaseData = this.truncateFutureData(barChartData.purchaseArr);
      const productionData = this.truncateFutureData(barChartData.productionArr);
      const commonData = this.truncateFutureData(barChartData.commonArr);

      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(255, 255, 255, 0.95)',
          borderColor: '#e5e7eb',
          borderWidth: 1,
          textStyle: {
            color: '#374151'
          },
          padding: [10, 15],
          extraCssText: 'box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); border-radius: 8px;'
        },
        legend: {
          data: ['原料入库单', '生产领料单', '销售出库单'],
          bottom: 0,
          icon: 'circle',
          itemWidth: 8,
          itemHeight: 8,
          itemGap: 24,
          textStyle: {
            color: '#6b7280',
            fontSize: 12
          }
        },
        grid: {
          top: 20,
          left: '3%',
          right: '4%',
          bottom: 50,
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          boundaryGap: false,
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
          axisLine: {
            lineStyle: {
              color: '#e5e7eb'
            }
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#6b7280',
            fontSize: 12
          }
        }],
        yAxis: [{
          type: 'value',
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          splitLine: {
            lineStyle: {
              color: '#f3f4f6',
              type: 'dashed'
            }
          },
          axisLabel: {
            color: '#9ca3af',
            fontSize: 11
          }
        }],
        series: [
          {
            name: '原料入库单',
            type: 'line',
            smooth: true,
            symbol: 'circle',
            symbolSize: 8,
            lineStyle: {
              width: 3,
              color: '#10b981'
            },
            itemStyle: {
              color: '#10b981',
              borderColor: '#fff',
              borderWidth: 2
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(16, 185, 129, 0.25)' },
                  { offset: 1, color: 'rgba(16, 185, 129, 0.02)' }
                ]
              }
            },
            data: purchaseData
          },
          {
            name: '生产领料单',
            type: 'line',
            smooth: true,
            symbol: 'circle',
            symbolSize: 8,
            lineStyle: {
              width: 3,
              color: '#f59e0b'
            },
            itemStyle: {
              color: '#f59e0b',
              borderColor: '#fff',
              borderWidth: 2
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(245, 158, 11, 0.25)' },
                  { offset: 1, color: 'rgba(245, 158, 11, 0.02)' }
                ]
              }
            },
            data: productionData
          },
          {
            name: '销售出库单',
            type: 'line',
            smooth: true,
            symbol: 'circle',
            symbolSize: 8,
            lineStyle: {
              width: 3,
              color: '#8b5cf6'
            },
            itemStyle: {
              color: '#8b5cf6',
              borderColor: '#fff',
              borderWidth: 2
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(139, 92, 246, 0.25)' },
                  { offset: 1, color: 'rgba(139, 92, 246, 0.02)' }
                ]
              }
            },
            data: commonData
          }
        ]
      })
    }
  }
}
</script>

<style scoped>
.chart {
  min-height: 280px;
}
</style>