import request from '@/utils/request'

// 查询调拨出库单列表
export function listAllotOutOrder(query) {
  return request({
    url: '/stock/outOrder/list',
    method: 'get',
    params: { ...query, orderType: 'allot' }
  })
}

// 查询调拨出库单详细
export function getAllotOutOrder(orderId) {
  return request({
    url: '/stock/outOrder/' + orderId,
    method: 'get'
  })
}

// 删除调拨出库单
export function delAllotOutOrder(orderId) {
  return request({
    url: '/stock/outOrder/' + orderId,
    method: 'delete'
  })
}

// 打印调拨出库单
export function printAllotOutOrder(orderId) {
  return request({
    url: '/stock/outOrder/printOutOrder/' + orderId,
    method: 'get',
    responseType: 'arraybuffer',
    headers: {
      'Content-Type': 'application/json'
    },
  })
}

// 查询调拨出库单详情列表
export function listAllotOutOrderDetail(orderNo) {
  return request({
    url: '/stock/outDetail/listByOrderNo/' + orderNo,
    method: 'get'
  })
}