<template>
    <div class="content">
        <HeaderComponent/>
        <navbar/>
        <app-main/>
        <footer/>
    </div>
</template>

<script setup>
import HeaderComponent from '@/components/header/index.vue'
import navbar from '@/components/navbar/index.vue'
import appMain from '@/components/app-main/index.vue'
import footer from '@/components/footer/index.vue'
</script>

<style scoped>
.content {
    overflow: hidden;
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    min-width: 1103px;
    background: #f7f7f7;
    transition: background 1s ease;
    font: 12px/1.5 "Microsoft YaHei", arial, SimSun, "宋体";
}

div {
    display: block;
}
</style>