import request from '@/utils/request'

// 查询生产计划列表
export function listProdPlan(query) {
  return request({
    url: '/stock/prodPlan/list',
    method: 'get',
    params: query
  })
}

// 查询生产计划详细
export function getProdPlan(planId) {
  return request({
    url: '/stock/prodPlan/' + planId,
    method: 'get'
  })
}

// 新增生产计划
export function addProdPlan(data) {
  return request({
    url: '/stock/prodPlan',
    method: 'post',
    data: data
  })
}

// 修改生产计划
export function updateProdPlan(data) {
  return request({
    url: '/stock/prodPlan',
    method: 'put',
    data: data
  })
}

// 删除生产计划
export function delProdPlan(planId) {
  return request({
    url: '/stock/prodPlan/' + planId,
    method: 'delete'
  })
}

// 确认计划
export function confirmProdPlan(planId) {
  return request({
    url: '/stock/prodPlan/confirm/' + planId,
    method: 'put'
  })
}

// 生成工单
export function generateOrders(planId) {
  return request({
    url: '/stock/prodPlan/generateOrders/' + planId,
    method: 'put'
  })
}

// 完成计划
export function completeProdPlan(planId) {
  return request({
    url: '/stock/prodPlan/complete/' + planId,
    method: 'put'
  })
}

// 取消计划
export function cancelProdPlan(planId) {
  return request({
    url: '/stock/prodPlan/cancel/' + planId,
    method: 'put'
  })
}

