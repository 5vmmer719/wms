<template>
  <div class="panel-group">
    <el-row :gutter="20">
      <!-- 物料种类 -->
      <el-col :xs="12" :sm="12" :lg="6">
        <div class="stat-card card-material" @click="jumpPage('/base/mat')">
          <div class="stat-icon">
            <i class="el-icon-goods"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">
              <count-to :start-val="0" :end-val="panelGroupData.matTotal" :duration="2800" />
            </div>
            <div class="stat-label">物料种类</div>
            <div class="stat-footer stat-footer-placeholder">
              <span class="footer-label">-</span>
            </div>
          </div>
          <div class="stat-decoration"></div>
        </div>
      </el-col>

      <!-- 原料入库单 -->
      <el-col :xs="12" :sm="12" :lg="6">
        <div class="stat-card card-inbound" @click="jumpPage('/purchase/inOrder')">
          <div class="stat-icon">
            <i class="el-icon-takeaway-box"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">
              <count-to :start-val="0" :end-val="panelGroupData.purchase" :duration="3000" />
            </div>
            <div class="stat-label">原料入库单</div>
            <div class="stat-footer" @click.stop="jumpPage('/purchase/inReturn')">
              <span class="footer-label">退货单</span>
              <span class="footer-value">
                <count-to :start-val="0" :end-val="panelGroupData.purchaseReturn" :duration="3200" />
              </span>
            </div>
          </div>
          <div class="stat-decoration"></div>
        </div>
      </el-col>

      <!-- 生产领料单 -->
      <el-col :xs="12" :sm="12" :lg="6">
        <div class="stat-card card-production" @click="jumpPage('/prod/outOrder')">
          <div class="stat-icon">
            <i class="el-icon-box"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">
              <count-to :start-val="0" :end-val="panelGroupData.production" :duration="3200" />
            </div>
            <div class="stat-label">生产领料单</div>
            <div class="stat-footer" @click.stop="jumpPage('/prod/outReturn')">
              <span class="footer-label">退货单</span>
              <span class="footer-value">
                <count-to :start-val="0" :end-val="panelGroupData.productionReturn" :duration="3200" />
              </span>
            </div>
          </div>
          <div class="stat-decoration"></div>
        </div>
      </el-col>

      <!-- 销售出库单 -->
      <el-col :xs="12" :sm="12" :lg="6">
        <div class="stat-card card-sales" @click="jumpPage('/common/commonOutOrder')">
          <div class="stat-icon">
            <i class="el-icon-sell"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">
              <count-to :start-val="0" :end-val="panelGroupData.common" :duration="3400" />
            </div>
            <div class="stat-label">销售出库单</div>
            <div class="stat-footer" @click.stop="jumpPage('/common/commonOutReturn')">
              <span class="footer-label">退货单</span>
              <span class="footer-value">
                <count-to :start-val="0" :end-val="panelGroupData.commonReturn" :duration="3200" />
              </span>
            </div>
          </div>
          <div class="stat-decoration"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CountTo from 'vue-count-to'
import { statsIndexUpper } from "@/api/stats/index";

export default {
  name: 'PanelGroup',
  data() {
    return {
      panelGroupData: {
        matTotal: 0,
        purchase: 0,
        purchaseReturn: 0,
        production: 0,
        productionReturn: 0,
        common: 0,
        commonReturn: 0
      },
    }
  },
  components: { CountTo },
  created() {
    this.getData();
  },
  methods: {
    getData() {
      statsIndexUpper().then(response => {
        this.panelGroupData = response.data;
      });
    },
    jumpPage(pageUrl) {
      this.$router.push({ path: pageUrl });
    },
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  .stat-card {
    position: relative;
    background: #ffffff;
    border-radius: 12px;
    padding: 20px;
    margin-bottom: 20px;
    cursor: pointer;
    transition: all 0.3s ease;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
    min-height: 130px;
    height: 130px;
    display: flex;
    flex-direction: column;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);

      .stat-icon {
        transform: scale(1.1);
      }

      .stat-decoration {
        opacity: 1;
        transform: translateX(0);
      }
    }

    .stat-icon {
      position: absolute;
      top: 16px;
      right: 16px;
      width: 48px;
      height: 48px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: transform 0.3s ease;

      i {
        font-size: 24px;
        color: #ffffff;
      }
    }

    .stat-content {
      position: relative;
      z-index: 1;
      display: flex;
      flex-direction: column;

      .stat-value {
        font-size: 32px;
        font-weight: 700;
        line-height: 1.2;
        color: #1f2937;
        font-family: 'DIN Alternate', 'Helvetica Neue', sans-serif;
      }

      .stat-label {
        font-size: 14px;
        color: #6b7280;
        margin-top: 4px;
      }

      .stat-footer {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        padding: 6px 12px;
        background: rgba(239, 68, 68, 0.1);
        border-radius: 16px;
        margin-top: 12px;
        transition: all 0.2s ease;
        width: fit-content;

        &:hover {
          background: rgba(239, 68, 68, 0.15);
        }

        .footer-label {
          font-size: 12px;
          color: #ef4444;
        }

        .footer-value {
          font-size: 14px;
          font-weight: 600;
          color: #ef4444;
        }
      }

      .stat-footer-placeholder {
        visibility: hidden;
      }
    }

    .stat-decoration {
      position: absolute;
      bottom: -20px;
      right: -20px;
      width: 100px;
      height: 100px;
      border-radius: 50%;
      opacity: 0.5;
      transform: translateX(20px);
      transition: all 0.3s ease;
    }
  }

  // 物料卡片样式
  .card-material {
    border-left: 4px solid #3b82f6;

    .stat-icon {
      background: linear-gradient(135deg, #3b82f6, #60a5fa);
    }

    .stat-decoration {
      background: linear-gradient(135deg, rgba(59, 130, 246, 0.15), rgba(96, 165, 250, 0.1));
    }

    &:hover {
      border-left-color: #2563eb;
    }
  }

  // 入库卡片样式
  .card-inbound {
    border-left: 4px solid #10b981;

    .stat-icon {
      background: linear-gradient(135deg, #10b981, #34d399);
    }

    .stat-decoration {
      background: linear-gradient(135deg, rgba(16, 185, 129, 0.15), rgba(52, 211, 153, 0.1));
    }

    &:hover {
      border-left-color: #059669;
    }
  }

  // 生产卡片样式
  .card-production {
    border-left: 4px solid #f59e0b;

    .stat-icon {
      background: linear-gradient(135deg, #f59e0b, #fbbf24);
    }

    .stat-decoration {
      background: linear-gradient(135deg, rgba(245, 158, 11, 0.15), rgba(251, 191, 36, 0.1));
    }

    &:hover {
      border-left-color: #d97706;
    }
  }

  // 销售卡片样式
  .card-sales {
    border-left: 4px solid #8b5cf6;

    .stat-icon {
      background: linear-gradient(135deg, #8b5cf6, #a78bfa);
    }

    .stat-decoration {
      background: linear-gradient(135deg, rgba(139, 92, 246, 0.15), rgba(167, 139, 250, 0.1));
    }

    &:hover {
      border-left-color: #7c3aed;
    }
  }
}

// 响应式布局
@media (max-width: 768px) {
  .panel-group {
    .stat-card {
      padding: 16px;
      min-height: 110px;
      height: 110px;

      .stat-icon {
        width: 40px;
        height: 40px;

        i {
          font-size: 20px;
        }
      }

      .stat-content {
        .stat-value {
          font-size: 26px;
        }

        .stat-label {
          font-size: 13px;
        }

        .stat-footer {
          padding: 4px 10px;
          margin-top: 8px;

          .footer-label {
            font-size: 11px;
          }

          .footer-value {
            font-size: 12px;
          }
        }
      }
    }
  }
}
</style>
