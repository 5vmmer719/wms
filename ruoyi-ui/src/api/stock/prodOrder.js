import request from '@/utils/request'

// 查询生产订单列表
export function listProdOrder(query) {
  return request({
    url: '/stock/prodOrder/list',
    method: 'get',
    params: query
  })
}

// 查询生产订单详细
export function getProdOrder(orderId) {
  return request({
    url: '/stock/prodOrder/' + orderId,
    method: 'get'
  })
}

// 新增生产订单
export function addProdOrder(data) {
  return request({
    url: '/stock/prodOrder',
    method: 'post',
    data: data
  })
}

// 修改生产订单
export function updateProdOrder(data) {
  return request({
    url: '/stock/prodOrder',
    method: 'put',
    data: data
  })
}

// 删除生产订单
export function delProdOrder(orderId) {
  return request({
    url: '/stock/prodOrder/' + orderId,
    method: 'delete'
  })
}

// 排产
export function scheduleProdOrder(data) {
  return request({
    url: '/stock/prodOrder/schedule',
    method: 'put',
    data: data
  })
}

// 开工
export function startProdOrder(orderId) {
  return request({
    url: '/stock/prodOrder/start/' + orderId,
    method: 'put'
  })
}

// 报工完工
export function completeProdOrder(data) {
  return request({
    url: '/stock/prodOrder/complete',
    method: 'put',
    data: data
  })
}

// 关闭工单
export function closeProdOrder(orderId) {
  return request({
    url: '/stock/prodOrder/close/' + orderId,
    method: 'put'
  })
}

// 查询工单详情（含关联出库单和入库单）
export function getDetailProdOrder(orderId) {
  return request({
    url: '/stock/prodOrder/detail/' + orderId,
    method: 'get'
  })
}
