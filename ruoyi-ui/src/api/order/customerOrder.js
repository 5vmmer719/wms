import request from '@/utils/request'

// 查询客户订单列表
export function listCustomerOrder(query) {
  return request({
    url: '/order/customerOrder/list',
    method: 'get',
    params: query
  })
}

// 查询客户订单详细
export function getCustomerOrder(orderId) {
  return request({
    url: '/order/customerOrder/' + orderId,
    method: 'get'
  })
}

// 查询订单详情（含明细和关联工单）
export function getCustomerOrderDetail(orderId) {
  return request({
    url: '/order/customerOrder/detail/' + orderId,
    method: 'get'
  })
}

// 新增客户订单
export function addCustomerOrder(data) {
  return request({
    url: '/order/customerOrder',
    method: 'post',
    data: data
  })
}

// 修改客户订单
export function updateCustomerOrder(data) {
  return request({
    url: '/order/customerOrder',
    method: 'put',
    data: data
  })
}

// 删除客户订单
export function delCustomerOrder(orderId) {
  return request({
    url: '/order/customerOrder/' + orderId,
    method: 'delete'
  })
}

// 确认订单
export function confirmCustomerOrder(orderId) {
  return request({
    url: '/order/customerOrder/confirm/' + orderId,
    method: 'put'
  })
}

// 生成生产工单
export function generateProdOrder(orderId) {
  return request({
    url: '/order/customerOrder/generateProdOrder/' + orderId,
    method: 'put'
  })
}

// 关闭订单
export function closeCustomerOrder(orderId) {
  return request({
    url: '/order/customerOrder/close/' + orderId,
    method: 'put'
  })
}

