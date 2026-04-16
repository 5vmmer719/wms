import request from '@/utils/request'

// 查询首页头部统计
export function statsIndexUpper(query) {
  return request({
    url: '/stats/indexUpper',
    method: 'get',
    params: query
  })
}

// 查询首页中部统计
export function statsIndexMiddle(query) {
  return request({
    url: '/stats/indexMiddle',
    method: 'get',
    params: query
  })
}

// 查询首页下部统计
export function statsIndexLower(query) {
  return request({
    url: '/stats/indexLower',
    method: 'get',
    params: query
  })
}

// 订单进度看板 - 综合统计数据
export function getOrderProgress() {
  return request({
    url: '/stats/orderProgress',
    method: 'get'
  })
}

// 订单预警列表
export function getOrderWarnings() {
  return request({
    url: '/stats/orderWarnings',
    method: 'get'
  })
}

// 库存预警看板数据
export function getStockWarning() {
  return request({
    url: '/stats/stockWarning',
    method: 'get'
  })
}
