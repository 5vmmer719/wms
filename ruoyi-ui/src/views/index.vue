<template>
  <div class="dashboard-container">
    <!-- 顶部统计卡片 -->
    <panel-group />

    <!-- 数据图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :sm="24" :lg="16">
        <div class="chart-card">
          <div class="card-header">
            <span class="card-title">
              <i class="el-icon-data-analysis"></i>
              本周业务统计
            </span>
          </div>
          <div class="card-body chart-body">
            <bar-chart />
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-card">
          <div class="card-header">
            <span class="card-title">
              <i class="el-icon-warning" style="color:#e6a23c"></i>
              异常预警
            </span>
            <el-link type="primary" @click="$router.push('/orderProgress')">
              查看看板 <i class="el-icon-d-arrow-right"></i>
            </el-link>
          </div>
          <div class="card-body warning-card-body">
            <div v-if="warningList.length === 0" class="empty-warning">
              <i class="el-icon-circle-check" style="font-size:36px;color:#67c23a"></i>
              <p>暂无预警，一切正常</p>
            </div>
            <div v-else class="warning-scroll">
              <div v-for="(item, index) in warningList.slice(0, 6)" :key="index"
                   :class="['warning-item', 'warning-' + item.level]">
                <i :class="item.level === 'danger' ? 'el-icon-warning' : 'el-icon-alarm-clock'"
                   :style="{color: item.level === 'danger' ? '#f56c6c' : '#e6a23c'}"></i>
                <span class="warning-text">{{ item.content }}</span>
              </div>
              <div v-if="warningList.length > 6" class="warning-more" @click="$router.push('/orderProgress')">
                还有 {{ warningList.length - 6 }} 条预警...
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 数据表格区域 -->
    <el-row :gutter="20" class="table-row">
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-card">
          <div class="card-header">
            <span class="card-title">
              <i class="el-icon-document"></i>
              最新库存流水
            </span>
            <el-link type="primary" @click="jumpToRecord">
              查看更多 <i class="el-icon-d-arrow-right"></i>
            </el-link>
          </div>
          <div class="card-body">
            <el-table
              :data="recordList"
              style="width: 100%"
              :header-cell-style="{background: '#f8fafc', color: '#475569', fontWeight: '600'}"
              :row-style="{height: '52px'}"
              cell-class-name="table-cell"
            >
              <el-table-column prop="createTime" label="时间" width="140">
                <template slot-scope="scope">
                  <span class="time-text">{{$moment(scope.row.createTime).format('MM-DD HH:mm')}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="orderNo" label="单据号" min-width="130">
                <template slot-scope="scope">
                  <span class="order-no">{{ scope.row.orderNo || '-' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="matName" label="物料" min-width="120" show-overflow-tooltip>
                <template slot-scope="scope">
                  <span>{{ scope.row.matName || '-' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="quantity" label="数量" width="90" align="right">
                <template slot-scope="scope">
                  <span :class="['quantity', scope.row.quantity > 0 ? 'positive' : 'negative']">
                    {{ scope.row.quantity > 0 ? '+' : '' }}{{ scope.row.quantity }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="recordTypeLabel" label="类型" width="90" align="center">
                <template slot-scope="scope">
                  <el-tag size="mini" :type="getRecordTagType(scope.row.recordType)">
                    {{ scope.row.recordTypeLabel }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-card">
          <div class="card-header">
            <span class="card-title">
              <i class="el-icon-user"></i>
              登录日志
            </span>
          </div>
          <div class="card-body">
            <el-table
              :data="logininforList"
              style="width: 100%"
              :header-cell-style="{background: '#f8fafc', color: '#475569', fontWeight: '600'}"
              :row-style="{height: '52px'}"
            >
              <el-table-column prop="loginTime" label="登录时间" width="140">
                <template slot-scope="scope">
                  <span class="time-text">{{$moment(scope.row.loginTime).format('MM-DD HH:mm')}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="userName" label="账号" width="100">
                <template slot-scope="scope">
                  <span class="user-name">{{ scope.row.userName || '-' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="ipaddr" label="IP地址" width="120">
                <template slot-scope="scope">
                  <span class="ipaddr">{{ scope.row.ipaddr || '-' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="msg" label="状态">
                <template slot-scope="scope">
                  <span class="status-text">{{ scope.row.msg }}</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import BarChart from './dashboard/BarChart'

import { getLogininfor } from "@/api/login";
import { listRecord } from "@/api/stock/record";
import { getOrderWarnings } from "@/api/stats/index";

export default {
  name: 'Index',
  components: { PanelGroup, BarChart },
  data() {
    return {
      today: new Date(),
      logininforList: [],
      recordList: [],
      warningList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 5,
        status: 0,
      },
    }
  },
  created() {
    this.loginData();
    this.recordData();
    this.loadWarnings();
  },
  methods: {
    loginData() {
      getLogininfor(this.queryParams).then(response => {
        this.logininforList = response.data;
      });
    },
    recordData() {
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows;
      });
    },
    loadWarnings() {
      getOrderWarnings().then(response => {
        this.warningList = response.data || [];
      }).catch(() => {
        this.warningList = [];
      });
    },
    jumpToRecord() {
      this.$router.push('/stock/record');
    },
    getRecordTagType(type) {
      const typeMap = {
        'in': 'success',
        'out': 'danger',
        'allot_in': 'warning',
        'allot_out': 'warning',
        'return_in': 'info',
        'return_out': 'info'
      };
      return typeMap[type] || 'info';
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);
}

.chart-row {
  margin-top: 20px;
}

.table-row {
  margin-top: 20px;
}

.chart-card {
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  margin-bottom: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
    border-bottom: 1px solid #f0f0f0;

    .card-title {
      font-size: 16px;
      font-weight: 600;
      color: #1f2937;

      i {
        margin-right: 8px;
        color: #3b82f6;
      }
    }

    .el-link {
      font-size: 13px;
    }
  }

  .card-body {
    padding: 16px 20px;
  }

  .chart-body {
    height: 280px;
  }
}

// 预警卡片样式
.warning-card-body {
  height: 280px;
  overflow: hidden;
  padding: 12px 16px !important;

  .empty-warning {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    color: #909399;
    p { margin-top: 10px; font-size: 14px; }
  }

  .warning-scroll {
    height: 100%;
    overflow-y: auto;
  }

  .warning-item {
    display: flex;
    align-items: flex-start;
    gap: 8px;
    padding: 10px 12px;
    border-radius: 6px;
    margin-bottom: 6px;
    font-size: 13px;
    line-height: 1.4;

    i { margin-top: 2px; flex-shrink: 0; }
    .warning-text { flex: 1; color: #303133; word-break: break-all; }
  }

  .warning-danger { background: #fef0f0; }
  .warning-warning { background: #fdf6ec; }

  .warning-more {
    text-align: center;
    padding: 8px;
    color: #409eff;
    cursor: pointer;
    font-size: 13px;
    &:hover { text-decoration: underline; }
  }
}


// 表格样式优化
.table-cell {
  padding: 12px 10px;
}

.time-text {
  color: #6b7280;
  font-size: 13px;
}

.order-no {
  font-family: 'Monaco', 'Menlo', monospace;
  font-size: 12px;
  color: #4b5563;
}

.quantity {
  font-weight: 600;
  font-size: 14px;

  &.positive {
    color: #10b981;
  }

  &.negative {
    color: #ef4444;
  }
}

.user-name {
  font-weight: 500;
  color: #374151;
}

.ipaddr {
  font-family: 'Monaco', 'Menlo', monospace;
  font-size: 12px;
  color: #6b7280;
}

.status-text {
  font-size: 13px;
}

// 响应式布局
@media (max-width: 768px) {
  .dashboard-container {
    padding: 12px;
  }

  .chart-card {
    .card-header {
      padding: 12px 16px;

      .card-title {
        font-size: 15px;
      }
    }

    .card-body {
      padding: 12px 16px;
    }
  }
}
</style>
