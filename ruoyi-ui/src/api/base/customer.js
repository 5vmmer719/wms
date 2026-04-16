import request from '@/utils/request'

// ==================== 客户管理 ====================

// 查询客户列表
export function listCustomer(query) {
  return request({
    url: '/base/customer/list',
    method: 'get',
    params: query
  })
}

// 查询全部客户（用于下拉选择）
export function listAllCustomer(query) {
  return request({
    url: '/base/customer/listAll',
    method: 'get',
    params: query
  })
}

// 查询客户详细
export function getCustomer(customerId) {
  return request({
    url: '/base/customer/' + customerId,
    method: 'get'
  })
}

// 新增客户
export function addCustomer(data) {
  return request({
    url: '/base/customer',
    method: 'post',
    data: data
  })
}

// 修改客户
export function updateCustomer(data) {
  return request({
    url: '/base/customer',
    method: 'put',
    data: data
  })
}

// 删除客户
export function delCustomer(customerId) {
  return request({
    url: '/base/customer/' + customerId,
    method: 'delete'
  })
}

