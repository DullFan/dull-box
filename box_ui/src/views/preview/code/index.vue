<template>
  <div>
    <simple-header/>
    <div class="code-text-content">
            <pre class="layui-code code-text">
                {{ codeContent }}
            </pre>
    </div>
  </div>
</template>

<script setup>

import axios from 'axios'
import boxUtil from '@/utils/common'
import {useRoute} from 'vue-router'
import {onMounted, ref} from 'vue'

const codeContent = ref('')

const route = useRoute()

const renderCode = (fileId, filename) => {
  axios.get(boxUtil.getPreviewUrl(fileId)).then(res => {
    codeContent.value = res.data
    layui.use('code', function () {
      layui.code({
        elem: '.layui-code.code-text',
        title: filename,
        encode: false,
        about: false
      })
    })
  }).catch(() => {
    console.log("错误")
    codeContent.value = '获取资源失败'
  })
}

const init = () => {
  let fileId = route.params.fileId,
      filename = route.query.filename
  renderCode(fileId, filename)
}

onMounted(() => {
  init()
})

</script>

<style scoped>

.code-text-content {
  width: 100%;
  margin-top: 62px;
  display: block;
  text-align: center;
}

.layui-code.code-text {
  width: 60%;
  display: inline-block;
  text-align: left;
  overflow: auto;
}

</style>
