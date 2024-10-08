/**
 * Created by rubin on 2020/6/6.
 */

'use strict'

import http from '@/utils/http'
import simpleHttp from '@/utils/simple-http'

let userService = {
    login: function (data, resolve, reject) {
        http({
            url: '/login',
            data: data,
            method: 'post'
        }).then(res => {
            resolve(res)
        }).catch(res => {
            reject(res)
        })
    },
    register: function (data, resolve, reject) {
        http({
            url: '/register',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(res => reject(res))
    },
    info: function (resolve, reject) {
        http({
            url: '/getInfo',
            params: {},
            method: 'get'
        }).then(res => resolve(res)).catch(res => reject(res))
    },
    checkUsername: function (data, resolve, reject) {
        http({
            url: '/user/username/check',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(res => reject(res))
    },
    checkAnswer: function (data, resolve, reject) {
        http({
            url: '/user/answer/check',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(res => reject(res))
    },
    resetPassword: function (data, resolve, reject) {
        http({
            url: '/user/password/reset',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(res => reject(res))
    },
    changePassword: function (data, resolve, reject) {
        http({
            url: '/user/resetPassword',
            data: data,
            method: 'put'
        }).then(res => resolve(res)).catch(res => reject(res))
    },
    exit: function (resolve, reject) {
        http({
            url: '/logout',
            method: 'post'
        }).then(res => resolve(res)).catch(res => reject(res))
    },
    searchHistories: function (resolve, reject) {
        http({
            url: '/user/searchHistories',
            params: {},
            method: 'get'
        }).then(res => resolve(res)).catch(res => reject(res))
    },
    checkUserLoginStatus: function (params, resolve, reject) {
        http({
            url: '/user/login/status',
            params: params,
            method: 'get'
        }).then(res => resolve(res)).catch(res => reject(res))
    },
    infoWithoutPageJump(resolve) {
        simpleHttp({
            url: '/getInfo',
            params: {},
            method: 'get'
        }).then(res => resolve(res))
    }
}

export default userService
