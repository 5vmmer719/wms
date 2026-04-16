import request from '@/utils/request'

// 查询盘点单列表
export function listCheck(query) {
  return request({
    url: '/stock/check/list',
    method: 'get',
    params: query
  })
}

// 查询盘点单详细（含明细）
export function getCheck(checkId) {
  return request({
    url: '/stock/check/' + checkId,
    method: 'get'
  })
}

// 新增盘点单
export function addCheck(data) {
  return request({
    url: '/stock/check',
    method: 'post',
    data: data
  })
}

// 修改盘点单
export function updateCheck(data) {
  return request({
    url: '/stock/check',
    method: 'put',
    data: data
  })
}

// 提交盘点结果
export function submitCheck(data) {
  return request({
    url: '/stock/check/submit',
    method: 'put',
    data: data
  })
}

// 执行盘点调整
export function adjustCheck(checkId) {
  return request({
    url: '/stock/check/adjust/' + checkId,
    method: 'put'
  })
}

// 删除盘点单
export function delCheck(checkId) {
  return request({
    url: '/stock/check/' + checkId,
    method: 'delete'
  })
}

