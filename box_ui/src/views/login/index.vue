<template>
  <div class="parent_div" v-if="isLogin">
    <div class="login-content">
      <div class="content">
        <div class="form">
          <h2>登录您的Box账号</h2>
          <label>
            <el-input
                v-model="loginForm.username" size="large" @keyup.enter="doLogin" ref="usernameEl"
                placeholder="用户名"/>
          </label>
          <label>
            <el-input type="password" size="large" @keyup.enter="doLogin" v-model="loginForm.password"
                      placeholder="密码"/>
          </label>

          <el-button type="primary" class="submit" :loading="loading" @click="doLogin" round>登 录</el-button>
          <el-text class="register_text" @click="goRegister" type="primary">去注册</el-text>
        </div>
      </div>
    </div>
  </div>


  <div class="parent_div_register" v-if="!isLogin">
    <div class="login-content_register">
      <div class="content_register">
        <div class="form_register">
          <h2>注册您的Box账号</h2>
          <label>
            <el-input
                v-model="registerForm.username" size="large" @keyup.enter="doRegister" ref="usernameEl"
                placeholder="用户名"/>
          </label>
          <label>
            <el-input type="password" size="large" @keyup.enter="doRegister" v-model="registerForm.password"
                      placeholder="密码"/>
          </label>

          <el-button type="primary" class="submit" :loading="loading" @click="doRegister" round>注 册</el-button>
          <el-text class="register_text" @click="goLogin" type="primary">去登录</el-text>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import boxUtil from '@/utils/common'
import {ElMessage} from 'element-plus'
import userService from '@/api/user'
import {setToken} from '@/utils/cookie'
import {useFileStore} from '@/stores/file'
import {useUserStore} from '@/stores/user'

const router = useRouter()

const loading = ref(false)
const isLogin = ref(true)

const loginForm = reactive({
  username: '',
  password: ''
})

const fileStore = useFileStore()
const userStore = useUserStore()

const {setParentId, setDefaultParentId, setDefaultParentFilename} = fileStore
const {setUsername} = userStore

const usernameEl = ref(null)
onMounted(() => {
  usernameEl.value.focus()
})

const doLogin = () => {
  if (checkLoginForm()) {
    loading.value = true
    userService.login(loginForm, res => {
      setToken(res.token)
      userService.info(res => {
        setParentId(res.data.rootFileId)
        setDefaultParentId(res.data.rootFileId)
        setDefaultParentFilename(res.data.rootFilename)
        setUsername(res.data.userName)
        router.push({name: 'Index'})
      }, res => {
        ElMessage.error(res.msg)
      })
    }, res => {
      ElMessage.error(res.msg)
      loading.value = false
    })
  }
}

const checkLoginForm = () => {
  if (!boxUtil.checkUsername(loginForm.username)) {
    ElMessage.error('请输入2-20位只包含数字和字母的用户名')
    return false
  }
  if (!boxUtil.checkPassword(loginForm.password)) {
    ElMessage.error('请输入5-20位的密码')
    return false
  }
  return true
}

const goRegister = () => {
  // router.push({name: 'Register'})
  isLogin.value = false
  history.pushState('','', "/register")
}

const registerForm = reactive({
  username: '',
  password: '',
  rePassword: ''
})

const doRegister = () => {
  if (checkRegisterForm()) {
    loading.value = true
    userService.register(registerForm, () => {
      ElMessage.success('注册成功！即将跳转至登录页面')
      setTimeout(goLogin, 1000)
      loading.value = false
      registerForm.username = ""
      registerForm.password = ""
    }, res => {
      ElMessage.error(res.msg)
      loading.value = false
    })
  }
}

const checkRegisterForm = () => {
  if (!boxUtil.checkUsername(registerForm.username)) {
    ElMessage.error('请输入2-20位只包含数字和字母的用户名')
    return false
  }
  if (!boxUtil.checkPassword(registerForm.password)) {
    ElMessage.error('请输入5-20位的密码')
    return false
  }
  return true
}

const goLogin = () => {
  // router.push({name: 'Login'})
  isLogin.value = true
  history.pushState('','', "/login")
}

</script>

<style scoped>

*, *:before, *:after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

input {
  border: none;
  outline: none;
  background: none;
  font-family: 'Open Sans', Helvetica, Arial, sans-serif;
}

.login-content {
  position: absolute;
  left: 50%;
  transform: translate(-50%, 30%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.parent_div{
  min-height: 100vh;
  background-image: url("/public/static/img2.png");
  background-size: cover; /* 背景图片按比例缩放，覆盖整个屏幕 */
  background-position: center; /* 背景图片居中 */
  background-repeat: no-repeat; /* 背景图片不重复 */
}


.content {
  display: flex;
  flex-direction: row;
  width: 400px;
  height: 330px;
  backdrop-filter: blur(6px);
  -webkit-backdrop-filter: blur(6px);
  background-color: rgba(255, 255, 255, 0.6);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  justify-content: center;
  align-items: center;
  border-radius: 15px;
}


.form {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  justify-content: center;
  padding-left: 40px;
  padding-right: 40px;
}

button {
  display: block;
  margin: 0 auto;
  width: 100%;
  height: 36px;
  border-radius: 30px;
  color: #fff;
  font-size: 15px;
  cursor: pointer;
}

h2 {
  width: 100%;
  font-size: 22px;
}

label {
  display: block;
  margin-top: 25px;
  text-align: center;
}

.submit {
  margin-top: 25px !important;
}

.register_text {
  display: flex;
  margin-top: 10px;
  width: 100%;
  cursor: pointer;
}


.login-content_register {
  position: absolute;
  left: 50%;
  transform: translate(-50%, 30%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.parent_div_register {
  height: 100vh;
  background-image: url("/public/static/img3.png");
  background-size: cover; /* 背景图片按比例缩放，覆盖整个屏幕 */
  background-position: center; /* 背景图片居中 */
  background-repeat: no-repeat; /* 背景图片不重复 */
}


.content_register {
  display: flex;
  flex-direction: row;
  width: 400px;
  height: 330px;
  backdrop-filter: blur(6px);
  -webkit-backdrop-filter: blur(6px);
  background-color: rgba(255, 255, 255, 0.6);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  justify-content: center;
  align-items: center;
  border-radius: 15px;
}


.form_register {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  justify-content: center;
  padding-left: 40px;
  padding-right: 40px;
}

</style>