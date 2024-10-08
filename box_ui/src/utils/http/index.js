
'use strict'

import axios from 'axios'
import {clearToken, getToken} from '@/utils/cookie'
import {ElMessage, ElMessageBox} from 'element-plus'
import boxUtil from '@/utils/common'
import {useBreadcrumbStore} from '@/stores/breadcrumb'
import {useFileStore} from '@/stores/file'
import {useNavbarStore} from '@/stores/navbar'
import {useUserStore} from '@/stores/user'

const toLogin = function () {
        const breadcrumbStore = useBreadcrumbStore()
        const fileStore = useFileStore()
        const navbarStore = useNavbarStore()
        const userStore = useUserStore()
        // to re-login
        ElMessageBox.confirm('您需要重新登陆', '确认退出登录', {
            confirmButtonText: '重新登陆',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            clearToken()
            fileStore.clear()
            breadcrumbStore.clear()
            navbarStore.clear()
            userStore.clear()
            window.location.reload()
        })
    },
    http = axios.create({
        baseURL: boxUtil.getUrlPrefix(),
        timeout: 1000 * 60,
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        }
    })

http.interceptors.request.use(config => {
    if (config.data) {
        config.data = JSON.stringify(config.data)
    }
    let token = getToken()
    if (token) {
        config.headers['Authorization'] = token
    }
    return config
}, error => {
    ElMessage.error('请求失败')
    return
})

http.interceptors.response.use(res => {
    if (res.data.code === 10) {
        toLogin()
        res.data.message = '您需要重新登陆'
        return Promise.reject(res.data)
    }
    if (res.data.code !== 200) {
        return Promise.reject(res.data)
    }
    return res.data
}, error => {
    ElMessage.error('请求失败')
    return
})

export default http
