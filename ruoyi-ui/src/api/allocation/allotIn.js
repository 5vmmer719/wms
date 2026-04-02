import request from '@/utils/request'

// 查询调拨入库单列表
export function listAllotInOrder(query) {
  return request({
    url: '/stock/inOrder/list',
    method: 'get',
    params: { ...query, orderType: 'allot' }
  })
}

// 查询调拨入库单详细
export function getAllotInOrder(orderId) {
  return request({
    url: '/stock/inOrder/' + orderId,
    method: 'get'
  })
}

// 删除调拨入库单
export function delAllotInOrder(orderId) {
  return request({
    url: '/stock/inOrder/' + orderId,
    method: 'delete'
  })
}

// 打印调拨入库单
export function printAllotInOrder(orderId) {
  return request({
    url: '/stock/inOrder/printInOrder/' + orderId,
    method: 'get',
    responseType: 'arraybuffer',
    headers: {
      'Content-Type': 'application/json'
    },
  })
}