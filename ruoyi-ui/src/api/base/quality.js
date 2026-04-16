import request from '@/utils/request'

// ==================== 检验标准 ====================

// 查询检验标准列表
export function listQualityStandard(query) {
  return request({
    url: '/quality/standard/list',
    method: 'get',
    params: query
  })
}

// 查询所有检验标准（用于下拉框）
export function listAllQualityStandard(query) {
  return request({
    url: '/quality/standard/listAll',
    method: 'get',
    params: query
  })
}

// 查询检验标准详细（含检验项目）
export function getQualityStandard(standardId) {
  return request({
    url: '/quality/standard/' + standardId,
    method: 'get'
  })
}

// 新增检验标准
export function addQualityStandard(data) {
  return request({
    url: '/quality/standard',
    method: 'post',
    data: data
  })
}

// 修改检验标准
export function updateQualityStandard(data) {
  return request({
    url: '/quality/standard',
    method: 'put',
    data: data
  })
}

// 删除检验标准
export function delQualityStandard(standardIds) {
  return request({
    url: '/quality/standard/' + standardIds,
    method: 'delete'
  })
}

// ==================== 检验任务 ====================

// 查询检验任务列表
export function listQualityTask(query) {
  return request({
    url: '/quality/task/list',
    method: 'get',
    params: query
  })
}

// 查询所有检验任务（用于下拉选择）
export function listAllQualityTask(query) {
  return request({
    url: '/quality/task/listAll',
    method: 'get',
    params: query
  })
}

// 查询检验任务详细（含检验结果明细）
export function getQualityTask(taskId) {
  return request({
    url: '/quality/task/' + taskId,
    method: 'get'
  })
}

// 新增检验任务
export function addQualityTask(data) {
  return request({
    url: '/quality/task',
    method: 'post',
    data: data
  })
}

// 修改检验任务
export function updateQualityTask(data) {
  return request({
    url: '/quality/task',
    method: 'put',
    data: data
  })
}

// 提交检验结果
export function submitCheckResult(data) {
  return request({
    url: '/quality/task/submitResult',
    method: 'put',
    data: data
  })
}

// 删除检验任务
export function delQualityTask(taskIds) {
  return request({
    url: '/quality/task/' + taskIds,
    method: 'delete'
  })
}

// ==================== 不合格品处理 ====================

// 查询不合格品处理列表
export function listDefectHandle(query) {
  return request({
    url: '/quality/defectHandle/list',
    method: 'get',
    params: query
  })
}

// 查询不合格品处理详细
export function getDefectHandle(handleId) {
  return request({
    url: '/quality/defectHandle/' + handleId,
    method: 'get'
  })
}

// 新增不合格品处理
export function addDefectHandle(data) {
  return request({
    url: '/quality/defectHandle',
    method: 'post',
    data: data
  })
}

// 修改不合格品处理
export function updateDefectHandle(data) {
  return request({
    url: '/quality/defectHandle',
    method: 'put',
    data: data
  })
}

// 删除不合格品处理
export function delDefectHandle(handleIds) {
  return request({
    url: '/quality/defectHandle/' + handleIds,
    method: 'delete'
  })
}

