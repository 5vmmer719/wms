const express = require('express');
const multer = require('multer');
const axios = require('axios');
const path = require('path');
const fs = require('fs');
const open = require('open');

const app = express();
const PORT = 3100;

// 后端API地址配置（包含context-path）
const API_BASE_URL = 'http://localhost:9991/wms-api';

// 配置中间件
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

// 配置文件上传（只用于临时存储，前端解析后删除）
const upload = multer({ dest: 'uploads/' });

// 扫描历史记录
let scanHistory = [];

/**
 * API: 获取扫描历史
 */
app.get('/api/history', (req, res) => {
    res.json({ success: true, data: scanHistory.slice(0, 50) });
});

/**
 * API: 清空历史
 */
app.delete('/api/history', (req, res) => {
    scanHistory = [];
    res.json({ success: true, message: '历史已清空' });
});

/**
 * API: 添加扫描记录
 */
app.post('/api/history', (req, res) => {
    const { content, status } = req.body;
    scanHistory.unshift({
        time: new Date().toLocaleTimeString(),
        content,
        status
    });
    res.json({ success: true });
});

/**
 * API: 获取入库单信息（通过单号查询）
 * 后端接口路径: GET /stock/inOrder/m/{orderNo}
 */
app.get('/api/inOrder/:orderNo', async (req, res) => {
    try {
        console.log('查询入库单:', req.params.orderNo);
        // 使用 /m/{orderNo} 接口通过单号查询
        const response = await axios.get(`${API_BASE_URL}/stock/inOrder/m/${req.params.orderNo}`);
        console.log('入库单响应:', JSON.stringify(response.data).substring(0, 500));
        res.json(response.data);
    } catch (error) {
        console.error('获取入库单错误:', error.response?.status, error.message);
        res.status(error.response?.status || 500).json({
            code: error.response?.status || 500,
            msg: error.response?.data?.msg || '获取入库单失败: ' + error.message
        });
    }
});

/**
 * API: 获取出库单信息（通过单号查询）
 */
app.get('/api/outOrder/:orderNo', async (req, res) => {
    try {
        console.log('查询出库单:', req.params.orderNo);
        // 使用 /m/{orderNo} 接口通过单号查询
        const response = await axios.get(`${API_BASE_URL}/stock/outOrder/m/${req.params.orderNo}`);
        console.log('出库单响应:', JSON.stringify(response.data).substring(0, 500));
        res.json(response.data);
    } catch (error) {
        console.error('获取出库单错误:', error.response?.status, error.message);
        res.status(error.response?.status || 500).json({
            code: error.response?.status || 500,
            msg: error.response?.data?.msg || '获取出库单失败: ' + error.message
        });
    }
});

/**
 * API: 获取仓库列表
 */
app.get('/api/warehouse/listAll', async (req, res) => {
    try {
        console.log('正在获取仓库列表...', `${API_BASE_URL}/base/warehouse/listAll`);
        const response = await axios.get(`${API_BASE_URL}/base/warehouse/listAll`);
        console.log('仓库列表响应:', JSON.stringify(response.data).substring(0, 200));
        res.json(response.data);
    } catch (error) {
        console.error('获取仓库列表错误:', error.message);
        res.status(error.response?.status || 500).json({
            code: 500,
            msg: '获取仓库列表失败: ' + error.message
        });
    }
});

/**
 * API: 提交入库
 */
app.post('/api/submitIn', async (req, res) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/stock/inOrder/submitStockIn`, req.body, {
            headers: { 'Content-Type': 'application/json' }
        });
        res.json(response.data);
    } catch (error) {
        console.error('提交入库错误:', error.message);
        res.status(error.response?.status || 500).json({
            code: 500,
            msg: error.response?.data?.msg || '提交入库失败'
        });
    }
});

/**
 * API: 提交出库
 */
app.post('/api/submitOut', async (req, res) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/stock/outOrder/submitStockOut`, req.body, {
            headers: { 'Content-Type': 'application/json' }
        });
        res.json(response.data);
    } catch (error) {
        console.error('提交出库错误:', error.message);
        res.status(error.response?.status || 500).json({
            code: 500,
            msg: error.response?.data?.msg || '提交出库失败'
        });
    }
});

/**
 * API: 获取调拨单信息
 */
app.get('/api/allotOrder/:allotNo', async (req, res) => {
    try {
        console.log('查询调拨单:', req.params.allotNo);
        const response = await axios.get(`${API_BASE_URL}/stock/allotOrder/m/${req.params.allotNo}`);
        console.log('调拨单响应:', JSON.stringify(response.data).substring(0, 500));
        res.json(response.data);
    } catch (error) {
        console.error('获取调拨单错误:', error.response?.status, error.message);
        res.status(error.response?.status || 500).json({
            code: error.response?.status || 500,
            msg: error.response?.data?.msg || '获取调拨单失败: ' + error.message
        });
    }
});

/**
 * API: 提交调拨单
 */
app.post('/api/submitAllot', async (req, res) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/stock/allotOrder/submitAllot`, req.body, {
            headers: { 'Content-Type': 'application/json' }
        });
        res.json(response.data);
    } catch (error) {
        console.error('提交调拨错误:', error.message);
        res.status(error.response?.status || 500).json({
            code: 500,
            msg: error.response?.data?.msg || '提交调拨失败'
        });
    }
});

/**
 * API: 确认调拨单并生成出库单
 */
app.post('/api/confirmAllot', async (req, res) => {
    try {
        console.log('收到确认调拨请求:', JSON.stringify(req.body));
        console.log('转发到:', `${API_BASE_URL}/stock/allotOrder/confirmAllot`);
        const response = await axios.post(`${API_BASE_URL}/stock/allotOrder/confirmAllot`, req.body, {
            headers: { 'Content-Type': 'application/json' }
        });
        console.log('确认调拨响应:', JSON.stringify(response.data));
        res.json(response.data);
    } catch (error) {
        console.error('确认调拨错误:', error.response?.status, error.message);
        console.error('错误详情:', error.response?.data);
        res.status(error.response?.status || 500).json({
            code: error.response?.status || 500,
            msg: error.response?.data?.msg || '确认调拨失败: ' + error.message
        });
    }
});

/**
 * API: 获取入库退货单信息
 */
app.get('/api/inReturn/:returnNo', async (req, res) => {
    try {
        console.log('查询入库退货单:', req.params.returnNo);
        const response = await axios.get(`${API_BASE_URL}/stock/inReturn/m/${req.params.returnNo}`);
        console.log('入库退货单响应:', JSON.stringify(response.data).substring(0, 500));
        res.json(response.data);
    } catch (error) {
        console.error('获取入库退货单错误:', error.response?.status, error.message);
        res.status(error.response?.status || 500).json({
            code: error.response?.status || 500,
            msg: error.response?.data?.msg || '获取入库退货单失败: ' + error.message
        });
    }
});

/**
 * API: 提交入库退货
 */
app.post('/api/submitInReturn', async (req, res) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/stock/inReturn/submitInReturn`, req.body, {
            headers: { 'Content-Type': 'application/json' }
        });
        res.json(response.data);
    } catch (error) {
        console.error('提交入库退货错误:', error.message);
        res.status(error.response?.status || 500).json({
            code: 500,
            msg: error.response?.data?.msg || '提交入库退货失败'
        });
    }
});

/**
 * API: 获取出库退货单信息
 */
app.get('/api/outReturn/:returnNo', async (req, res) => {
    try {
        console.log('查询出库退货单:', req.params.returnNo);
        const response = await axios.get(`${API_BASE_URL}/stock/outReturn/m/${req.params.returnNo}`);
        console.log('出库退货单响应:', JSON.stringify(response.data).substring(0, 500));
        res.json(response.data);
    } catch (error) {
        console.error('获取出库退货单错误:', error.response?.status, error.message);
        res.status(error.response?.status || 500).json({
            code: error.response?.status || 500,
            msg: error.response?.data?.msg || '获取出库退货单失败: ' + error.message
        });
    }
});

/**
 * API: 提交出库退货
 */
app.post('/api/submitOutReturn', async (req, res) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/stock/outReturn/submitOutReturn`, req.body, {
            headers: { 'Content-Type': 'application/json' }
        });
        res.json(response.data);
    } catch (error) {
        console.error('提交出库退货错误:', error.message);
        res.status(error.response?.status || 500).json({
            code: 500,
            msg: error.response?.data?.msg || '提交出库退货失败'
        });
    }
});

/**
 * API: 获取货位物料列表
 */
app.get('/api/locationMatList', async (req, res) => {
    try {
        const { warehouseCode, locationCode } = req.query;
        console.log('查询货位物料:', warehouseCode, locationCode);
        const response = await axios.get(`${API_BASE_URL}/stock/info/locationMatList`, {
            params: { warehouseCode, locationCode }
        });
        res.json(response.data);
    } catch (error) {
        console.error('获取货位物料错误:', error.message);
        res.status(error.response?.status || 500).json({
            code: 500,
            msg: error.response?.data?.msg || '获取货位物料失败'
        });
    }
});

/**
 * API: 提交上架
 */
app.post('/api/submitPutOn', async (req, res) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/stock/info/submitPutOn`, req.body, {
            headers: { 'Content-Type': 'application/json' }
        });
        res.json(response.data);
    } catch (error) {
        console.error('提交上架错误:', error.message);
        res.status(error.response?.status || 500).json({
            code: 500,
            msg: error.response?.data?.msg || '提交上架失败'
        });
    }
});

/**
 * API: 提交下架
 */
app.post('/api/submitPutOff', async (req, res) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/stock/info/submitPutOff`, req.body, {
            headers: { 'Content-Type': 'application/json' }
        });
        res.json(response.data);
    } catch (error) {
        console.error('提交下架错误:', error.message);
        res.status(error.response?.status || 500).json({
            code: 500,
            msg: error.response?.data?.msg || '提交下架失败'
        });
    }
});

// 创建public目录
const publicDir = path.join(__dirname, 'public');
if (!fs.existsSync(publicDir)) {
    fs.mkdirSync(publicDir);
}

// 创建uploads目录
const uploadsDir = path.join(__dirname, 'uploads');
if (!fs.existsSync(uploadsDir)) {
    fs.mkdirSync(uploadsDir);
}

// 启动服务器
app.listen(PORT, () => {
    console.log(`\n========================================`);
    console.log(`  WMS扫码工具已启动`);
    console.log(`  访问地址: http://localhost:${PORT}`);
    console.log(`  后端API: ${API_BASE_URL}`);
    console.log(`========================================\n`);

    // 自动打开浏览器
    open(`http://localhost:${PORT}`);
});