<!-- 参考模板：https://codepen.io/ricardoolivaalonso/full/YzyaRPN -->
<!-- 参考教程：https://blog.csdn.net/Wind_AN/article/details/125539939 -->
<!-- 参考教程：https://blog.csdn.net/qq_41045128/article/details/125651144 -->
<template>
  <div class="body">
    <div class="banner">
                    <h1>虚&ensp;拟&ensp;角&ensp;色&ensp;对&ensp;话</h1>
                    <h6>&ensp;拾&ensp;取&ensp;残&ensp;念&ensp;成&ensp;朝&ensp;花
                    </h6>
            
                </div>
    <div class="main-box">
      <div :class="['container', 'container-register', { 'is-txl': isLogin }]">
        <form>
          <h2 class="title">注册</h2>
          <div class="form__icons">
            <img class="form__icon" src="@/assets/images/wechat.png" alt="微信登录">
            <img class="form__icon" src="@/assets/images/alipay.png" alt="支付宝登录">
            <img class="form__icon" src="@/assets/images/QQ.png" alt="QQ登录">
          </div>
          <span class="text">或使用邮箱进行注册</span>
          <input class="form__input" v-model="registerForm.username" type="text" placeholder="请输入用户名"/>
          <input class="form__input" v-model="registerForm.email" type="text" placeholder="请输入邮箱"/>
          <input class="form__input" v-model="registerForm.password" type="password" placeholder="请输入密码"/>
          <input class="form__input" v-model="registerForm.confirmPassword" type="password" placeholder="请确认密码"/>
          <input class="form__input" v-model="CaptchaForm.captchavalue" type="text" placeholder="请输入验证码" v-if="isCaptch === true"/>
          <div class="form__button" @click="isCaptch === true ? captcha() : register()"> {{ isCaptch === true ? '验证邮箱码' : '立即注册' }}</div>
        </form>
      </div>
      <div :class="['container', 'container-login', { 'is-txl is-z200': isLogin }]">
        <form>
          <h2 class="title">登录</h2>
          <div class="form__icons">
            <img class="form__icon" src="@/assets/images/wechat.png" alt="微信登录">
            <img class="form__icon" src="@/assets/images/alipay.png" alt="支付宝登录">
            <img class="form__icon" src="@/assets/images/QQ.png" alt="QQ登录">
          </div>
          <span class="text">或账号密码/邮箱验证码登录</span>
          <input class="form__input" type="text" v-model="loginForm.username" placeholder="用户名/邮箱/手机号" v-if="isAlter === true"/>
          <input class="form__input" type="password" v-model="loginForm.password" placeholder="请输入密码" v-if="isAlter === true"/>
          <input class="form__input" type="text" v-model="registerForm.email" placeholder="邮箱" v-if="isAlter === false"/>
          <input class="form__input" type="text" v-model="CaptchaForm.captchavalue" placeholder="验证码已发送，请输入邮箱验证码" v-if="isEmail=== true"/>
          <span class="alter" @click="isAlter = !isAlter">{{ isAlter === true ? '使用邮箱验证码登录' : '使用账号密码登录' }}</span>
          <div class="form__button" @click="isAlter === true ? login() : emailload()" v-if="isEmail=== false"> {{ isAlter === true ? '立即登录' : '发送验证码' }}</div>
          <div class="form__button" @click="captcha()" v-if="isEmail=== true">验证邮箱码</div>
        </form>
      </div>
      <div :class="['switch', { 'login': isLogin }]">
        <div class="switch__circle"></div>
        <div class="switch__circle switch__circle_top"></div>
        <div class="switch__container">
          <h2>{{ isLogin ? '您好 !' : '欢迎回来 !' }}</h2>
          <p>
            {{
              isLogin
                  ? '如果您还没有账号，请点击下方立即注册按钮进行账号注册'
                  : '如果您已经注册过账号，请点击下方立即登录按钮进行登录'
            }}
          </p>
          <div class="form__button" @click="isLogin = !isLogin">
            {{ isLogin ? '注册' : '登录' }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  
  name: 'Login',
  mounted() {
    this.$store.commit('relogin')
  },
  data() {
    return {
      isLogin: true,
      isAlter: true,
      isCaptch: false,
      isEmail: false,
      loginForm: {
        username: '',
        password: '',
      },
      registerForm: {
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
      },
      CaptchaForm: {
        captchavalue: '',
      },
    }
  },
  methods: {

    login() {
      let params = {
        username: this.loginForm.username,
        password: this.loginForm.password
      };
      let uid=3;
      console.log(params);
      var url = "http://localhost:8088/account/signUp";
      axios.post(url, params).then(res => {
        console.log(res);
        if (res.data.code==200) {
          this.$message({
            message: res.data.msg,
            type: 'success'
          });
          uid=res.data.user_id;
          this.$store.commit('setUser', uid);
          console.log(this.$store.state.userID);
          this.$router.push({ path: '/set' });
        } else if (res.data.code==-203) {
          this.$message.error('登录失败！请检查密码');
        } else if (res.data.code==-204) {
          this.$message.error('登录失败！该账号尚未注册');
        } else {this.$message.error('未知错误！');}
      }).catch(error => {
        console.log(error);
        this.$message.error('登录失败！');
        
      });
      
    },

    register() {
      let params = {
        username: this.registerForm.username,
        password: this.registerForm.password,
        confirmPassword: this.registerForm.confirmPassword,
        email: this.registerForm.email
      };
      console.log(params);
      console.log('当前 isCaptch:', this.isCaptch);
      const username = params.username;
      const ulimit = 25;
      const password = params.password;
      const plimit = 16;
      if (username.length > ulimit ) {
      this.$message.error('用户名长度不应超过' + ulimit);
      return;
      }
      if (password.length > plimit || !/[A-Za-z]/.test(password) || !/\d/.test(password)) {
      this.$message.error('密码必须包含至少一个字母和一个数字，并且长度不超过' + plimit);
      return;
      }
      if (params.password != params.confirmPassword) {
        this.$message.error('两次密码输入不一致');
        return;
      }
      var url = "http://localhost:8088/account/signIn";
      axios.post(url, params).then(res => {
        console.log(res);
        if (res.data.code==200) {
          this.$message({
            message: res.data.msg,
            type: 'success',
          });
          this.isCaptch=true;

        } else if (res.data.code==-201) {
          this.$message.error('注册失败！用户名已存在');
        } else if (res.data.code==-202) {
          this.$message.error('注册失败！邮箱已被注册');
        } else {this.$message.error('未知错误！');}
      }).catch(error => {
        console.log(error);
        this.$message.error('注册失败！');
      });
    },
    captcha() {
      let params = {
        flag:this.isLogin,
        Captchavalue: this.CaptchaForm.captchavalue
      };
      let uid=4;
      console.log(params);
      var url = "http://localhost:8088/account/verify";
      axios.post(url, params).then(res => {
        console.log(res);
        if (res.data.code==200) {
          this.$message({
            message: res.data.msg,
            type: 'success'
          });
          uid=res.data.user_id;
          this.$store.commit('setUser', uid);
          console.log(this.$store.state.userID);
          this.$router.push({ path: '/set' });
        } else if (res.data.code==-205) {
          this.$message.error('验证失败！请输入正确验证码');
        } else {this.$message.error('未知错误！');}
      }).catch(error => {
        console.log(error);
        this.$message.error('验证失败！');
      });
    },
    emailload() {
      let params = {
        email: this.registerForm.email
      };
      console.log(params);
      var url = "http://localhost:8088/account/mailLogin";
      axios.post(url, params).then(res => {
        console.log(res);
        if (res.data.code==200) {
          this.$message({
            message: res.data.msg,
            type: 'success'
          });
          this.isEmail=true;
        } else if (res.data.code==-206) {
          this.$message.error('邮箱未注册！请输入正确邮箱');
        } else {this.$message.error('未知错误！');}
      }).catch(error => {
        console.log(error);
        this.$message.error('邮箱错误！');
      });
    },
    
  },
}
</script>


<style lang="scss" scoped>
.banner {
            position: absolute;
            margin-bottom: 700px;
            z-index: 999;
    

            h1,
            h6,
            p {
                margin: 0;
                padding: 10px 0;
            }

            h1 {
                font-size: 5em;
                font-family: "NSimSun",sans-serif;
                color: rgba(0, 0, 0);
                margin-left: 18px;
            }

            h6 {
                
                font-size: 1em;
                font-family: 'fangsong', sans-serif;
                color: rgba(27, 26, 26, 0.7);
                margin-bottom: 25px;
                margin-left: 210px;
            }
        }
.body {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: "Montserrat", sans-serif;
  font-size: 12px;
  background-image: url("@/assets/images/background.jpg");
  color: #a0a5a8;

}
.main-box {
  position: relative;
  width: 1000px;
  min-width: 1000px;
  min-height: 600px;
  height: 600px;
  padding: 25px;
  background-color: #ecf0f3;
  box-shadow: 1px 1px 100px 10PX #ecf0f3;
  border-radius: 12px;
  overflow: hidden;

  .container {
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 0;
    width: 600px;
    height: 100%;
    padding: 25px;
    background-color: #ecf0f3;
    transition: all 1.25s;

    form {
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      width: 100%;
      height: 100%;
      color: #a0a5a8;

      .form__icon {
        object-fit: contain;
        width: 30px;
        margin: 0 5px;
        opacity: .5;
        transition: .15s;

        &:hover {
          opacity: 1;
          transition: .15s;
          cursor: pointer;

        }
      }

      .title {
        font-size: 34px;
        font-weight: 700;
        line-height: 3;
        color: #181818;
      }

      .text {
        margin-top: 30px;
        margin-bottom: 12px;
      }

      .alter {
        margin-top: 2px;
        margin-bottom: 12px;
        padding-left: 250px;
        color: #297df1;
      }

      .form__input {
        width: 350px;
        height: 40px;
        margin: 4px 0;
        padding-left: 25px;
        font-size: 13px;
        letter-spacing: 0.15px;
        border: none;
        outline: none;
        // font-family: 'Montserrat', sans-serif;
        background-color: #ecf0f3;
        transition: 0.25s ease;
        border-radius: 8px;
        box-shadow: inset 2px 2px 4px #d1d9e6, inset -2px -2px 4px #f9f9f9;

        &::placeholder {
          color: #a0a5a8;
        }
      }
    }
  }

  .container-register {
    z-index: 100;
    left: calc(100% - 600px);
  }

  .container-login {
    left: calc(100% - 600px);
    z-index: 0;
  }

  .is-txl {
    left: 0;
    transition: 1.25s;
    transform-origin: right;
  }

  .is-z200 {
    z-index: 200;
    transition: 1.25s;
  }

  .switch {
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 400px;
    padding: 50px;
    z-index: 200;
    transition: 1.25s;
    background-color: #ecf0f3;
    overflow: hidden;
    box-shadow: 4px 4px 10px #d1d9e6, -4px -4px 10px #f9f9f9;
    color: #a0a5a8;

    .switch__circle {
      position: absolute;
      width: 500px;
      height: 500px;
      border-radius: 50%;
      background-color: #ecf0f3;
      box-shadow: inset 8px 8px 12px #d1d9e6, inset -8px -8px 12px #f9f9f9;
      bottom: -60%;
      left: -60%;
      transition: 1.25s;
    }

    .switch__circle_top {
      top: -30%;
      left: 60%;
      width: 300px;
      height: 300px;
    }

    .switch__container {
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      position: absolute;
      width: 400px;
      padding: 50px 55px;
      transition: 1.25s;

      h2 {
        font-size: 34px;
        font-weight: 700;
        line-height: 3;
        color: #181818;
      }

      p {
        font-size: 14px;
        letter-spacing: 0.25px;
        text-align: center;
        line-height: 1.6;
      }
    }
  }

  .login {
    left: calc(100% - 400px);

    .switch__circle {
      left: 0;
    }
  }

  .form__button {
    width: 180px;
    height: 50px;
    border-radius: 25px;
    margin-top: 50px;
    text-align: center;
    line-height: 50px;
    font-size: 14px;
    letter-spacing: 2px;
    background-color: #4b70e2;
    color: #f9f9f9;
    cursor: pointer;
    box-shadow: 8px 8px 16px #d1d9e6, -8px -8px 16px #f9f9f9;

    &:hover {
      box-shadow: 2px 2px 3px 0 rgba(255, 255, 255, 50%),
      -2px -2px 3px 0 rgba(116, 125, 136, 50%),
      inset -2px -2px 3px 0 rgba(255, 255, 255, 20%),
      inset 2px 2px 3px 0 rgba(0, 0, 0, 30%);
    }
  }
}
</style>

