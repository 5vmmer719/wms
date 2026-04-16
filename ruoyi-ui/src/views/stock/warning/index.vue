<template>
  <div class="app-container">
    <!-- 顶部统计卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :xs="12" :sm="6">
        <div class="mini-card card-total">
          <div class="mini-icon"><i class="el-icon-box"></i></div>
          <div class="mini-info">
            <div class="mini-value">{{ warningData.totalCount || 0 }}</div>
            <div class="mini-label">物料总数</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="mini-card card-normal">
          <div class="mini-icon"><i class="el-icon-circle-check"></i></div>
          <div class="mini-info">
            <div class="mini-value">{{ warningData.normalCount || 0 }}</div>
            <div class="mini-label">库存正常</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="mini-card card-low">
          <div class="mini-icon"><i class="el-icon-bottom"></i></div>
          <div class="mini-info">
            <div class="mini-value">{{ warningData.lowCount || 0 }}</div>
            <div class="mini-label">库存不足</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="mini-card card-high">
          <div class="mini-icon"><i class="el-icon-top"></i></div>
          <div class="mini-info">
            <div class="mini-value">{{ warningData.highCount || 0 }}</div>
            <div class="mini-label">库存超限</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 库存不足预警 -->
    <el-card class="warning-section" shadow="hover">
      <div slot="header" class="section-header">
        <span><i class="el-icon-warning" style="color:#f56c6c"></i> 库存不足预警</span>
        <el-tag type="danger" size="small">{{ lowStockList.length }} 项</el-tag>
      </div>
      <el-table :data="lowStockList" border size="small" v-if="lowStockList.length > 0">
        <el-table-column label="物料编码" prop="matCode" width="140" align="center" />
        <el-table-column label="物料名称" prop="matName" min-width="160" align="center" />
        <el-table-column label="当前库存" prop="currentQty" width="120" align="center">
          <template slot-scope="scope">
            <span style="color:#f56c6c;font-weight:bold">{{ scope.row.currentQty }}</span>
          </template>
        </el-table-column>
        <el-table-column label="安全库存" prop="safetyStock" width="120" align="center" />
        <el-table-column label="缺口数量" prop="shortage" width="120" align="center">
          <template slot-scope="scope">
            <span style="color:#f56c6c">{{ scope.row.shortage }}</span>
          </template>
        </el-table-column>
        <el-table-column label="缺口比例" width="160" align="center">
          <template slot-scope="scope">
            <el-progress :percentage="getShortagePercent(scope.row)" :color="'#f56c6c'" :stroke-width="14" :text-inside="true" />
          </template>
        </el-table-column>
      </el-table>
      <div v-else class="empty-tip">
        <i class="el-icon-circle-check" style="color:#67c23a;font-size:40px"></i>
        <p>所有物料库存充足</p>
      </div>
    </el-card>

    <!-- 库存超限预警 -->
    <el-card class="warning-section" shadow="hover" style="margin-top:16px">
      <div slot="header" class="section-header">
        <span><i class="el-icon-warning" style="color:#e6a23c"></i> 库存超限预警</span>
        <el-tag type="warning" size="small">{{ highStockList.length }} 项</el-tag>
      </div>
      <el-table :data="highStockList" border size="small" v-if="highStockList.length > 0">
        <el-table-column label="物料编码" prop="matCode" width="140" align="center" />
        <el-table-column label="物料名称" prop="matName" min-width="160" align="center" />
        <el-table-column label="当前库存" prop="currentQty" width="120" align="center">
          <template slot-scope="scope">
            <span style="color:#e6a23c;font-weight:bold">{{ scope.row.currentQty }}</span>
          </template>
        </el-table-column>
        <el-table-column label="库存上限" prop="maxStock" width="120" align="center" />
        <el-table-column label="超出数量" prop="excess" width="120" align="center">
          <template slot-scope="scope">
            <span style="color:#e6a23c">+{{ scope.row.excess }}</span>
          </template>
        </el-table-column>
        <el-table-column label="超出比例" width="160" align="center">
          <template slot-scope="scope">
            <el-progress :percentage="getExcessPercent(scope.row)" :color="'#e6a23c'" :stroke-width="14" :text-inside="true" />
          </template>
        </el-table-column>
      </el-table>
      <div v-else class="empty-tip">
        <i class="el-icon-circle-check" style="color:#67c23a;font-size:40px"></i>
        <p>所有物料库存未超限</p>
      </div>
    </el-card>

    <!-- 库存汇总列表 -->
    <el-card class="warning-section" shadow="hover" style="margin-top:16px">
      <div slot="header" class="section-header">
        <span><i class="el-icon-s-data"></i> 库存汇总</span>
        <el-button type="text" size="small" @click="$router.push('/stock/info')">查看详情</el-button>
      </div>
      <el-form :model="statsQuery" size="small" :inline="true" style="margin-bottom:10px">
        <el-form-item label="物料编码">
          <el-input v-model="statsQuery.matCode" placeholder="物料编码" clearable style="width:150px" @keyup.enter.native="loadStatsList" />
        </el-form-item>
        <el-form-item label="物料名称">
          <el-input v-model="statsQuery.matName" placeholder="物料名称" clearable style="width:150px" @keyup.enter.native="loadStatsList" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="loadStatsList">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="statsList" border size="small" v-loading="statsLoading">
        <el-table-column label="物料编码" prop="matCode" width="140" align="center" />
        <el-table-column label="物料名称" prop="matName" min-width="160" align="center" />
        <el-table-column label="当前库存" prop="statsQuantity" width="120" align="center">
          <template slot-scope="scope">
            <span :style="getQtyStyle(scope.row)">{{ scope.row.statsQuantity }}</span>
          </template>
        </el-table-column>
        <el-table-column label="安全库存" prop="safetyStock" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.safetyStock || '-' }}</template>
        </el-table-column>
        <el-table-column label="库存上限" prop="maxStock" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.maxStock || '-' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="isLow(scope.row)" type="danger" size="mini">不足</el-tag>
            <el-tag v-else-if="isHigh(scope.row)" type="warning" size="mini">超限</el-tag>
            <el-tag v-else type="success" size="mini">正常</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="statsTotal > 0" :total="statsTotal" :page.sync="statsQuery.pageNum" :limit.sync="statsQuery.pageSize" @pagination="loadStatsList" />
    </el-card>
  </div>
</template>

<script>
import { getStockWarning } from "@/api/stats/index";
import { statsList } from "@/api/stock/info";

export default {
  name: "StockWarning",
  data() {
    return {
      warningData: {},
      lowStockList: [],
      highStockList: [],
      statsLoading: false,
      statsList: [],
      statsTotal: 0,
      statsQuery: { pageNum: 1, pageSize: 10, matCode: null, matName: null },
    };
  },
  created() {
    this.loadWarningData();
    this.loadStatsList();
  },
  methods: {
    loadWarningData() {
      getStockWarning().then(response => {
        this.warningData = response.data || {};
        this.lowStockList = response.data.lowStockList || [];
        this.highStockList = response.data.highStockList || [];
      });
    },
    loadStatsList() {
      this.statsLoading = true;
      statsList(this.statsQuery).then(response => {
        this.statsList = response.rows || [];
        this.statsTotal = response.total || 0;
        this.statsLoading = false;
      });
    },
    getShortagePercent(row) {
      if (!row.safetyStock || row.safetyStock <= 0) return 0;
      return Math.min(100, Math.round((row.shortage / row.safetyStock) * 100));
    },
    getExcessPercent(row) {
      if (!row.maxStock || row.maxStock <= 0) return 0;
      return Math.min(100, Math.round((row.excess / row.maxStock) * 100));
    },
    isLow(row) {
      return row.safetyStock && row.safetyStock > 0 && (row.statsQuantity || 0) < row.safetyStock;
    },
    isHigh(row) {
      return row.maxStock && row.maxStock > 0 && (row.statsQuantity || 0) > row.maxStock;
    },
    getQtyStyle(row) {
      if (this.isLow(row)) return { color: '#f56c6c', fontWeight: 'bold' };
      if (this.isHigh(row)) return { color: '#e6a23c', fontWeight: 'bold' };
      return {};
    },
  }
};
</script>

<style scoped>
.stat-cards { margin-bottom: 16px; }
.mini-card {
  display: flex; align-items: center; padding: 16px 20px;
  border-radius: 8px; background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  cursor: default; transition: box-shadow 0.3s;
}
.mini-card:hover { box-shadow: 0 4px 16px rgba(0,0,0,0.1); }
.mini-icon { font-size: 36px; margin-right: 16px; }
.mini-value { font-size: 28px; font-weight: 700; line-height: 1.2; }
.mini-label { font-size: 13px; color: #909399; margin-top: 4px; }
.card-total .mini-icon { color: #409eff; }
.card-normal .mini-icon { color: #67c23a; }
.card-low .mini-icon { color: #f56c6c; }
.card-high .mini-icon { color: #e6a23c; }
.card-total { border-left: 4px solid #409eff; }
.card-normal { border-left: 4px solid #67c23a; }
.card-low { border-left: 4px solid #f56c6c; }
.card-high { border-left: 4px solid #e6a23c; }
.section-header { display: flex; justify-content: space-between; align-items: center; }
.empty-tip { text-align: center; padding: 30px 0; color: #909399; }
.empty-tip p { margin-top: 10px; font-size: 14px; }
</style>

