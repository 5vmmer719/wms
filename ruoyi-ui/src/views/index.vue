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
              <i class="el-icon-date"></i>
              日历
            </span>
          </div>
          <div class="card-body calendar-card-body">
            <el-calendar v-model="today">
              <template slot="dateCell" slot-scope="{date, data}">
                <div class="calendar-day">
                  {{ data.day.split('-')[2] }}
                </div>
              </template>
            </el-calendar>
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

export default {
  name: 'Index',
  components: { PanelGroup, BarChart },
  data() {
    return {
      today: new Date(),
      logininforList: [],
      recordList: [],
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

// 日历样式优化
.calendar-card-body {
  height: 280px;
  overflow: hidden;
  padding: 10px 16px !important;

  ::v-deep .el-calendar {
    background: transparent;

    .el-calendar__header {
      padding: 6px 8px;
      border-bottom: 1px solid #f0f0f0;

      .el-calendar__title {
        font-weight: 600;
        color: #1f2937;
        font-size: 13px;
      }

      .el-calendar__button-group {
        .el-button {
          padding: 3px 8px;
          font-size: 11px;
        }
      }
    }

    .el-calendar__body {
      padding: 6px;

      th {
        font-weight: 500;
        color: #6b7280;
        font-size: 10px;
        padding: 2px 0;
      }
    }

    .el-calendar-table {
      thead th {
        padding: 2px 0;
      }

      .el-calendar-day {
        padding: 0;
        height: 30px;
        line-height: 30px;
        text-align: center;
        font-size: 11px;
      }

      tbody tr td {
        border: none;

        .calendar-day {
          display: inline-block;
          text-align: center;
          line-height: 22px;
          width: 22px;
          height: 22px;
          border-radius: 50%;
          transition: all 0.2s;
          font-size: 11px;

          &:hover {
            background: #e0f2fe;
          }
        }
      }

      .is-selected {
        background: transparent;

        .el-calendar-day {
          background: transparent;
        }

        .calendar-day {
          background: #3b82f6;
          color: #ffffff;
        }
      }

      .is-today {
        .calendar-day {
          background: #dbeafe;
          color: #3b82f6;
          font-weight: 600;
        }
      }

      .prev-month,
      .next-month {
        .calendar-day {
          color: #d1d5db;
        }
      }
    }
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
