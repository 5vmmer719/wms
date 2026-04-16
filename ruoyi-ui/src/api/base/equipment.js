import request from '@/utils/request'

// ==================== 设备管理 ====================

// 查询设备列表
export function listEquipment(query) {
  return request({
    url: '/base/equipment/list',
    method: 'get',
    params: query
  })
}

// 查询所有设备（用于下拉选择）
export function listAllEquipment(query) {
  return request({
    url: '/base/equipment/listAll',
    method: 'get',
    params: query
  })
}

// 查询设备详细
export function getEquipment(equipmentId) {
  return request({
    url: '/base/equipment/' + equipmentId,
    method: 'get'
  })
}

// 新增设备
export function addEquipment(data) {
  return request({
    url: '/base/equipment',
    method: 'post',
    data: data
  })
}

// 修改设备
export function updateEquipment(data) {
  return request({
    url: '/base/equipment',
    method: 'put',
    data: data
  })
}

// 删除设备
export function delEquipment(equipmentId) {
  return request({
    url: '/base/equipment/' + equipmentId,
    method: 'delete'
  })
}

// ==================== 工位管理 ====================

// 查询工位列表
export function listWorkstation(query) {
  return request({
    url: '/base/workstation/list',
    method: 'get',
    params: query
  })
}

// 查询所有工位（用于下拉选择）
export function listAllWorkstation(query) {
  return request({
    url: '/base/workstation/listAll',
    method: 'get',
    params: query
  })
}

// 查询工位详细
export function getWorkstation(stationId) {
  return request({
    url: '/base/workstation/' + stationId,
    method: 'get'
  })
}

// 新增工位
export function addWorkstation(data) {
  return request({
    url: '/base/workstation',
    method: 'post',
    data: data
  })
}

// 修改工位
export function updateWorkstation(data) {
  return request({
    url: '/base/workstation',
    method: 'put',
    data: data
  })
}

// 删除工位
export function delWorkstation(stationId) {
  return request({
    url: '/base/workstation/' + stationId,
    method: 'delete'
  })
}

// ==================== 设备维护记录 ====================

// 查询维护记录列表
export function listMaintain(query) {
  return request({
    url: '/base/maintain/list',
    method: 'get',
    params: query
  })
}

// 查询维护记录详细
export function getMaintain(maintainId) {
  return request({
    url: '/base/maintain/' + maintainId,
    method: 'get'
  })
}

// 新增维护记录
export function addMaintain(data) {
  return request({
    url: '/base/maintain',
    method: 'post',
    data: data
  })
}

// 修改维护记录
export function updateMaintain(data) {
  return request({
    url: '/base/maintain',
    method: 'put',
    data: data
  })
}

// 完成维护
export function completeMaintain(maintainId) {
  return request({
    url: '/base/maintain/complete/' + maintainId,
    method: 'put'
  })
}

// 删除维护记录
export function delMaintain(maintainId) {
  return request({
    url: '/base/maintain/' + maintainId,
    method: 'delete'
  })
}

