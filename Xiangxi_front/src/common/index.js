/**
 * common统一对外接口
 */

export { VERSION, BASE_URL, API_BASE_URL } from './constants'
export { get, post, del, put, postFormData, getBlob } from './request'
export { ElMessage as tip , ElMessage } from 'element-plus'
export { getLocalToken, setLocalToken, removeLocalToken } from './token'
export { sureLoading } from './sureLoading'

/*

    常量：
       VERSION 项目版本号
       BASE_URL 基础路径
       API_BASE_URL 服务端基础路径

    本地token(令牌)操作
        getLocalToken 获取本地token
        setLocalToken 存储本地token
        removeLocalToken 删除本地token

*/