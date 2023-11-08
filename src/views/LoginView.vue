<template>
  <NavBar></NavBar>
  <el-container class="login-container">
    <el-main>
      <div id="particles-js"></div>
      <div class="login-child-container">
        <div class="welcome">
          <div class="pinkbox" :style="{ transform: `translateX(${boxPosition})` }">
            <div class="signup" v-show="showSignUp">
              <h1>注册</h1>
              <form autocomplete="off" @submit.prevent="signUp">
                <input type="text" placeholder="用户名" v-model="userName">
                <input type="text" placeholder="手机号码" v-model="userPhone">
                <input type="password" placeholder="密码" v-model="userPwd">
                <input type="password" placeholder="确认密码" v-model="userPwdConfirm">
                <el-button class="button" native-type="submit" :loading="isLoading" size="large">创建账号 </el-button>
              </form>
            </div>
            <div class="signin" v-show="showSignIn">
              <h1>登录</h1>
              <form class="more-padding" autocomplete="off" @submit.prevent="signIn">
                <input type="text" placeholder="手机号码" v-model="userPhone">
                <input type="password" placeholder="密码" v-model="userPwd">
                <div class="checkbox">
                  <input type="checkbox" id="remember" /><label for="remember">记住我</label>
                </div>
                <el-button class="button" native-type="submit" :loading="isLoading" size="large">登录</el-button>
              </form>
            </div>
          </div>
          <div class="leftbox">
            <h2 class="title"><span>SignUp</span>&<br>Chat</h2>
            <p class="desc">选择你的<span>登录方式</span></p>
            <img class="flower smaller" src="../assets/img/手捧脸.png" alt="" border="0">
            <p class="account">已经有账号了?</p>
            <button class="button" id="signin" @click="toggleForms">登录</button>
          </div>
          <div class="rightbox">
            <h2 class="title"><span>SignIn</span>&<br>Chat</h2>
            <p class="desc">选择你的<span>登录方式</span></p>
            <img class="flower smaller" src="../assets/img/白发少女.png"  alt=""/>
            <p class="account">还没有账号?</p>
            <button class="button" id="signup" @click="toggleForms">注册</button>
          </div>
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import NavBar from "@/components/NavBar";
import particlesJson from "@/assets/particles.json";
import {doPost, toGet} from "@/axios/httpRequest";
import md5 from "js-md5";

export default {
  name: "LoginView",
  components: {NavBar},
  data() {
    return {
      // 初始位置
      boxPosition: "0%",

      // 显示登录
      showSignIn: true,

      // 显示注册
      showSignUp: false,

      userPhone: "",

      userPwd: "",

      isLoading: false,

      // 手机号码输入错误或未注册
      isPhoneErrExistSignIn: true,

      // 手机号码输入错误或未注册
      isPhoneErrExistSignUp: true,

      // 密码输入错误
      isPwdErrExist: true,

      // 用户名
      userName: "",

      // 确认密码
      userPwdConfirm: "",

      // 前后密码确认
      isPwdConfirm: false,

      // 用户名是否为空
      isNameBlank: true
    }
  },
  methods: {
    // 消息提示的方法
    showMessage(type, message) {
      this.$message({
        type: type,
        center: true,
        message: message
      });
    },

    toggleForms() {
      this.showSignIn = !this.showSignIn;
      this.showSignUp = !this.showSignUp;

      // 切换框位置
      this.boxPosition = this.showSignIn ? "0%" : "80%";
    },

    // 检查手机号的方法
    checkPhone() {

      // 验证手机号格式正则表达式
      // 不需要加双引号，加双引号无法识别为正则表达式
      const phoneRegExp = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;

      if(this.userPhone === "" || this.userPhone === undefined) {
        this.showMessage("warning","请输入手机号");
      }else if(!phoneRegExp.test(this.userPhone)) {
        this.showMessage("warning","手机号格式不正确");
      }else {

        // 向服务器发起请求，验证手机号是否可以登录
        toGet("/v1/user/phoneExists",{phone: this.userPhone}).then(response => {
          if(response && response.data.code === 400) {
            this.isPhoneErrExistSignIn = false;
          }else if(response && response.data.code === 200) {
            this.showMessage("warning","手机号未注册");
            this.isPhoneErrExistSignUp = false;
          }
          else {
            this.showMessage("error","服务繁忙，请稍后重试");
          }
        });
      }
    },

    // 检查密码的方法
    checkPwd() {
      if(this.userPwd === "" || this.userPwd === undefined) {
        this.showMessage("warning","请输入密码");
      }else {
        this.isPwdErrExist = false;
      }
    },

    // 用户登录
    signIn() {
      this.checkPhone();
      this.checkPwd();

      // 等待 dom 容器更新后
      this.$nextTick(() => {
        if(!this.isPhoneErrExist && !this.isPwdErrExist) {
          this.isLoading = true;

          // 使用 MD5 加密密码
          let newPasswd = md5(this.userPwd);

          // 发起登录请求
          doPost("/v1/user/login", {
            phone: this.userPhone,
            password: newPasswd,
          }).then(response => {
            if (response && response.data.code === 200) {

              // 登录成功，存储 accessToken 至 localStorage，只能存字符串
              window.localStorage.setItem("accessToken", response.data.accessToken);

              const userInfo = response.data.retData;

              this.$store.dispatch('asyncUpdateUser', {name: userInfo.name, phone: userInfo.phone, id: userInfo.id});

              this.$store.dispatch('startSessionExpiryTimer');

              // 用户名不为空，跳转至用户中心
              this.showMessage("success", "登录成功，自动跳转至首页");
              this.isLoading = false;
              this.$router.push({
                path: "/"
              });
            }
            else {
              this.showMessage("error", response.data.msg);
              this.isLoading = false;
            }
          });
        }
      });
    },

    // 检查前后密码是否一致
    checkPwdConfim() {
      if(this.userPwdConfirm === "" || this.userPwdConfirm === undefined) {
        this.showMessage("warning","请再次输入密码");
      }else if(this.userPwd !== this.userPwdConfirm) {
        this.showMessage("warning","前后密码不一致");
      }else {
        this.isPwdConfirm = true;
      }
    },

    // 检查用户名是否为空
    checkName() {
      if(this.userName === "" || this.userName === undefined) {
        this.showMessage("warning","请输入用户名");
      }else {
        this.isNameBlank = false;
      }
    },

    // 用户注册
    signUp() {
      this.checkPhone();
      this.checkPwd();
      this.checkPwdConfim();
      this.checkName();

      // 等待 dom 容器更新后
      this.$nextTick(() => {
        if(!this.isPhoneErrExistSignUp && !this.isPwdErrExist && this.isPwdConfirm && !this.isNameBlank) {
          this.isLoading = true;

          // 数据正确，向后端发起注册请求
          // 使用 MD5 加密密码
          let newPasswd = md5(this.userPwd);

          doPost("/v1/user/register",{
            phone: this.userPhone,
            password: newPasswd,
            userName: this.userName
          }).then(response => {
            if(response && response.data.code === 200) {
              this.showMessage("success","注册成功");
              this.signIn();
            }else {
              this.showMessage("error",response.data.msg);
              this.isLoading = false;
            }
          });
        }
      });
    }
  }
  ,
  mounted() {
    require('particles.js')
    // eslint-disable-next-line no-undef
    particlesJS('particles-js', particlesJson)
  }
}
</script>

<style scoped>
.login-container {
  width: 100%; /* 设置容器宽度为100% */
  box-sizing: border-box; /* 设置盒模型为border-box，确保内边距和边框不会增加容器的宽度 */
  height: 100vh;
  border: 1px solid #ccc;
  box-shadow: 2px 2px 10px #ccc;
  font-family: "Source Code Pro Light", sans-serif;
  position: fixed;
  background-color: #FFE7D4;
}

/* div box styling */
.login-child-container {
  margin: auto;
  width: 650px;
  height: 550px;
  position: relative;
}

.welcome {
  background: #f6f6f6;
  width: 650px;
  height: 415px;
  position: absolute;
  top: 25%;
  border-radius: 5px;
  box-shadow: 5px 5px 5px rgba(0, 0, 0, 0.1);
}

.pinkbox {
  position: absolute;
  top: -10%;
  left: 5%;
  background: #EAC7CC;
  width: 320px;
  height: 500px;
  border-radius: 5px;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  transition: all 0.5s ease-in-out;
  z-index: 2;
}

.nodisplay {
  display: none;
  transition: all 0.5s ease;
}

.leftbox, .rightbox {
  position: absolute;
  width: 50%;
  transition: 1s all ease;
}

.leftbox {
  left: -2%;
}

.rightbox {
  right: -2%;
}

/* font & button styling */
h1 {
  font-family: "Source Code Pro Light", sans-serif;
  text-align: center;
  margin-top: 95px;
  text-transform: uppercase;
  color: #f6f6f6;
  font-size: 2em;
  letter-spacing: 8px;
}

.title {
  font-family: "Source Code Pro Light", serif;
  color: #8E9AAF;
  font-size: 1.8em;
  line-height: 1.1em;
  letter-spacing: 3px;
  text-align: center;
  font-weight: 300;
  margin-top: 20%;
}

.desc {
  margin-top: -8px;
}

.account {
  margin-top: 45%;
  font-size: 10px;
}

p {
  font-family: "Source Code Pro Light", sans-serif;
  font-size: 0.7em;
  letter-spacing: 2px;
  color: #8E9AAF;
  text-align: center;
}

span {
  color: #EAC7CC;
}

.flower {
  position: absolute;
  width: 120px;
  height: 120px;
  top: 46%;
  left: 29%;
  opacity: 0.7;
}

.smaller {
  width: 100px;
  height: 100px;
  top: 48%;
  left: 38%;
  opacity: 0.9;
}

button {
  padding: 12px;
  font-family: "Source Code Pro Light", sans-serif;
  text-transform: uppercase;
  letter-spacing: 3px;
  font-size: 11px;
  border-radius: 10px;
  margin: auto;
  outline: none;
  display: block;
}

button:hover {
  background: #EAC7CC;
  color: #f6f6f6;
  transition: background-color 1s ease-out;
}

.button {
  margin-top: 3%;
  background: #f6f6f6;
  color: #ce7d88;
  border: solid 1px #EAC7CC;
}

/* form styling */
form {
  display: flex;
  align-items: center;
  flex-direction: column;
  padding-top: 7px;
}

.more-padding {
  padding-top: 35px;
}

.more-padding input {
  padding: 12px;
}

.more-padding .submit {
  margin-top: 45px;
}

.submit {
  margin-top: 25px;
  padding: 12px;
  border-color: #ce7d88;
}

.submit:hover {
  background: #CBC0D3;
  border-color: #bfb1c9;
}

input {
  background: #EAC7CC;
  width: 65%;
  color: #ce7d88;
  border: none;
  border-bottom: 1px solid rgba(246, 246, 246, 0.5);
  padding: 9px;
  margin: 7px;
}

input::placeholder {
  color: #f6f6f6;
  letter-spacing: 2px;
  font-size: 1.3em;
  font-weight: 100;
}

input:focus {
  color: #ce7d88;
  outline: none;
  border-bottom: 1.2px solid rgba(206, 125, 136, 0.7);
  font-size: 1em;
  transition: 0.8s all ease;
}

input:focus::placeholder {
  opacity: 0;
}

label {
  font-family: "Open Sans", sans-serif;
  color: #ce7d88;
  font-size: 0.8em;
  letter-spacing: 1px;
}

.checkbox {
  display: inline;
  white-space: nowrap;
  position: relative;
  left: -5vw;
  top: 0.5vh;
}

input[type=checkbox] {
  width: 0.8vw;
  height: 1.6vh;
  background: #ce7d88;
}

.checkbox input[type=checkbox]:checked + label {
  color: #ce7d88;
  transition: 0.5s all ease;
}
</style>