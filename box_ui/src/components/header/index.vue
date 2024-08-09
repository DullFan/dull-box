<template>
    <div class="header-content">
        <div class="header-content-wrapper">
            <div class="title-font-content">
                <span class="title-font" @click="goHome">Box</span>
            </div>
            <div class="upload-task-list-content">
                <task-list/>
            </div>
            <div class="user-info-content">
                <user-info/>
            </div>
        </div>
    </div>
</template>

<script setup>
import userInfo from '@/components/user-info/index.vue'

const goHome = () => {
    window.location.href = '/'
}

</script>

<style scoped>

.header-content {
    top: 0;
    left: 0;
    width: 100%;
    z-index: 41;
    position: fixed;
}

.header-content .header-content-wrapper {
    height: 62px;
    line-height: 62px;
    position: relative;
    background: #fff;
    box-shadow: 0 2px 6px 0 rgba(0, 0, 0, .05);
    -webkit-transition: background 1s ease;
    -moz-transition: background 1s ease;
    -o-transition: background 1s ease;
    transition: background 1s ease;
}

.header-content .header-content-wrapper .title-font-content {
    display: inline-block;
    position: absolute;
    left: 40px;
}

.header-content-wrapper .title-font-content .title-font {
    font-size: 40px;
    font-weight: bolder;
    cursor: pointer;
    color: #46a1ff;
}

.header-content-wrapper .user-info-content {
    display: inline-block;
    position: absolute;
    right: 60px;
    top: 26.5px;
}

.header-content-wrapper .upload-task-list-content {
    display: inline-block;
    position: absolute;
    right: 210px;
    cursor: pointer;
}
</style>