import request from '@/utils/request'

// ==================== 工艺路线 ====================

// 查询工艺路线列表
export function listProcessRoute(query) {
  return request({
    url: '/base/processRoute/list',
    method: 'get',
    params: query
  })
}

// 查询工艺路线列表（所有，用于下拉框）
export function listAllProcessRoute(query) {
  return request({
    url: '/base/processRoute/listAll',
    method: 'get',
    params: query
  })
}

// 查询工艺路线详细（含工序和参数）
export function getProcessRoute(routeId) {
  return request({
    url: '/base/processRoute/' + routeId,
    method: 'get'
  })
}

// 新增工艺路线
export function addProcessRoute(data) {
  return request({
    url: '/base/processRoute',
    method: 'post',
    data: data
  })
}

// 修改工艺路线
export function updateProcessRoute(data) {
  return request({
    url: '/base/processRoute',
    method: 'put',
    data: data
  })
}

// 删除工艺路线
export function delProcessRoute(routeIds) {
  return request({
    url: '/base/processRoute/' + routeIds,
    method: 'delete'
  })
}

// ==================== 工序 ====================

// 查询工序列表
export function listProcessStep(query) {
  return request({
    url: '/base/processRoute/step/list',
    method: 'get',
    params: query
  })
}

// 查询工序详细
export function getProcessStep(stepId) {
  return request({
    url: '/base/processRoute/step/' + stepId,
    method: 'get'
  })
}

// 新增工序
export function addProcessStep(data) {
  return request({
    url: '/base/processRoute/step',
    method: 'post',
    data: data
  })
}

// 修改工序
export function updateProcessStep(data) {
  return request({
    url: '/base/processRoute/step',
    method: 'put',
    data: data
  })
}

// 删除工序
export function delProcessStep(stepIds) {
  return request({
    url: '/base/processRoute/step/' + stepIds,
    method: 'delete'
  })
}

// ==================== 工艺参数 ====================

// 查询工艺参数列表
export function listProcessParam(query) {
  return request({
    url: '/base/processRoute/param/list',
    method: 'get',
    params: query
  })
}

// 查询工艺参数详细
export function getProcessParam(paramId) {
  return request({
    url: '/base/processRoute/param/' + paramId,
    method: 'get'
  })
}

// 新增工艺参数
export function addProcessParam(data) {
  return request({
    url: '/base/processRoute/param',
    method: 'post',
    data: data
  })
}

// 修改工艺参数
export function updateProcessParam(data) {
  return request({
    url: '/base/processRoute/param',
    method: 'put',
    data: data
  })
}

// 删除工艺参数
export function delProcessParam(paramIds) {
  return request({
    url: '/base/processRoute/param/' + paramIds,
    method: 'delete'
  })
}

