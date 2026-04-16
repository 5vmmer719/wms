import request from '@/utils/request'

// 质量追溯查询
export function traceQuery(keyword) {
  return request({
    url: '/quality/trace/query',
    method: 'get',
    params: { keyword }
  })
}

// 质量统计数据
export function qualityStats() {
  return request({
    url: '/quality/trace/stats',
    method: 'get'
  })
}

