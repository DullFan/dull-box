/**
* Created by rubin on 2020/7/5.
*/

<template>
    <div>
        <simple-header/>
        <div class="music-content">
            <div class="music-name-content">
                <p class="music-name">{{ musicName }}</p>
            </div>
            <el-divider></el-divider>
            <el-row>
                <el-col :span="18">
                    <div class="record-img-content">
                        <img class="record-img" src="@/assets/imgs/record.png">
                    </div>
                    <!--音频播放容器-->
                    <div class="music-content">
                        <audio id="music_player" :src="musicShowPath" autoplay="true" controls="true"/>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="music-list-content">
                        <el-menu class="music-list"
                                 :default-active="activeIndex"
                                 @select="selectMusic">
                            <el-menu-item v-for="(item, index) in musicList" :key="index" :index="item.fileId">
                                <i class="fa fa-music"></i>
                                <span slot="title">{{ item.filename }}</span>
                            </el-menu-item>
                        </el-menu>
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script setup>

import PanSimpleHeader from '@/components/simple-header/index.vue'
import fileService from '@/api/file'
import boxUtil from '@/utils/common'
import {useRoute} from 'vue-router'
import {onMounted, ref} from 'vue'
import {ElMessage} from 'element-plus'

const route = useRoute()
const musicName = ref('')
const musicList = ref([])
const musicShowPath = ref('')
const activeIndex = ref('0')

const renderMusicList = (dataList) => {
    musicList.value = new Array()
    dataList.forEach((item, index) => {
        item.filename = item.filename.substring(0, item.filename.lastIndexOf('.'))
        if (item.filename.length > 15) {
            item.filename = item.filename.substring(0, 16) + '...'
        }
        if (item.fileId === route.params.fileId) {
            musicName.value = item.filename
            musicShowPath.value = boxUtil.getPreviewUrl(item.fileId)
        }
        musicList.value.push(item)
    })
    activeIndex.value = route.params.fileId
}

const selectNext = () => {
    let i = '',
        currentFileId = activeIndex.value
    musicList.value.some((item, index) => {
        if (item.fileId === currentFileId) {
            i = index
            return true
        }
    })
    if (i === musicList.value.length - 1) {
        return
    }
    let item = musicList.value[++i]
    musicName.value = item.filename
    musicShowPath.value = boxUtil.getPreviewUrl(item.fileId)
    activeIndex.value = item.fileId
}

const selectMusic = (index, indexPath) => {
    activeIndex.value = index
    musicList.value.some(item => {
        if (item.fileId === index) {
            musicName.value = item.filename
            musicShowPath.value = boxUtil.getPreviewUrl(item.fileId)
            return true
        }
    })
}

const listenMusicPlayer = () => {
    document.getElementById('music_player').addEventListener('ended', () => {
        selectNext()
    }, false)
}

const init = () => {
    fileService.list({
        parentId: route.params.parentId,
        fileTypes: '8'
    }, res => {
        if (res.code === 200) {
            renderMusicList(res.data)
            listenMusicPlayer()
        } else {
            ElMessage.error(res.msg)
        }
    }, res => {
        ElMessage.error(res.msg)
    })
}

onMounted(() => {
    init()
})


</script>

<style scoped>
.music-content {
    width: 100%;
    margin-top: 62px;
    display: block;
}

.music-content .music-name-content {
    display: block;
    width: 100%;
    text-align: center;
    padding: 10px 0 0 0;
}

.music-content .music-name-content .music-name {
    color: #409EFF;
    font-size: 35px;
    font-weight: bold;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}

.music-content .record-img-content {
    display: inline-block;
    width: 100%;
    height: 100%;
    margin-top: 80px;
    text-align: center;
}

.music-content .record-img-content .record-img {
    width: 300px;
}

.music-content .music-content {
    width: 100%;
    height: 100px;
    display: block;
    margin-top: 60px;
    text-align: center;
    position: relative;
}

.music-content .music-content #music_player {
    display: inline-block;
    width: 100%;
    position: absolute;
    bottom: 10px;
    left: 0;
}

.music-content .music-list-content {
    display: block;
    margin: 0 auto;
    width: 250px;
    height: 500px;
    line-height: 500px;
    overflow: hidden;
}

.music-content .music-list-content .music-list {
    width: 100%;
    height: 100%;
    overflow: scroll;
}

.music-content .music-list i {
    margin-right: 15px;
}

</style>
