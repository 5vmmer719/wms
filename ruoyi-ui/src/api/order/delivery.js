import request from '@/utils/request'

// 查询交付记录列表
export function listDelivery(query) {
  return request({
    url: '/order/delivery/list',
    method: 'get',
    params: query
  })
}

// 查询交付详情
export function getDeliveryDetail(deliveryId) {
  return request({
    url: '/order/delivery/detail/' + deliveryId,
    method: 'get'
  })
}

// 新增交付记录
export function addDelivery(data) {
  return request({
    url: '/order/delivery',
    method: 'post',
    data: data
  })
}

// 修改交付记录
export function updateDelivery(data) {
  return request({
    url: '/order/delivery',
    method: 'put',
    data: data
  })
}

// 删除交付记录
export function delDelivery(deliveryIds) {
  return request({
    url: '/order/delivery/' + deliveryIds,
    method: 'delete'
  })
}

// 发货
export function shipDelivery(deliveryId) {
  return request({
    url: '/order/delivery/ship/' + deliveryId,
    method: 'put'
  })
}

// 签收
export function receiveDelivery(deliveryId) {
  return request({
    url: '/order/delivery/receive/' + deliveryId,
    method: 'put'
  })
}

