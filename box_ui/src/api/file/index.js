/**
 * Created by rubin on 2020/6/26.
 */

'use strict'

import http from '@/utils/http'

let fileService = {
    list: function (params, resolve, reject) {
        http({
            url: 'file/list',
            params: params,
            method: 'get'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    createFolder: function (data, resolve, reject) {
        http({
            url: '/file/createFolder',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    update: function (data, resolve, reject) {
        http({
            url: '/file/updateFilename',
            data: data,
            method: 'put'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    delete: function (data, resolve, reject) {
        http({
            url: '/recycle',
            data: data,
            method: 'delete'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    getFolderTree: function (resolve, reject) {
        http({
            url: '/file/folder/tree',
            params: {},
            method: 'get'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    transfer: function (data, resolve, reject) {
        http({
            url: '/file/transfer',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    copy: function (data, resolve, reject) {
        http({
            url: '/file/copy',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    search: function (params, resolve, reject) {
        http({
            url: '/file/search',
            params: params,
            method: 'get'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    getBreadcrumbs: function (params, resolve, reject) {
        http({
            url: '/file/breadcrumbs',
            params: params,
            method: 'get'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    secUpload: function (data, resolve, reject) {
        http({
            url: '/file/secUpload',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    merge: function (data, resolve, reject) {
        http({
            url: '/file/mergeFile',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    }
}

export default fileService
