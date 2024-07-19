<script lang="ts" setup>
import { useRouter } from "vue-router";
import store from "@/store/store"
const router = useRouter();
//点击logo 的处理
const logoImgClickHandle = () => {
    window.location.href = "https://gitee.com/the-eight-fairies";
};
//点击导航菜单 的处理
const navClickHandle1 = () => {
  console.log( store.state.chatingList);
  router.push("/history");
};
const navClickHandle2 = () => {
  router.push("/set");
};
const navClickHandle3 = () => {
    location.href = '/';
};
</script>
<template>
  <div class="page-header">
    <div class="page-header-wrapper">
      <!-- logo -->
      <el-image src="https://static.hikarifield.co.jp/images/hf-logo.png" class="logo-image" fit="cover"
                @click="logoImgClickHandle" />
            <div class="logo-text" @click="logoImgClickHandle">
                <h1>虚拟角色对话页<span style="font-size:0.1em">By The Eight Fairies</span></h1>
               <!-- <p>凝新聚华，将美少女游戏文化推向世界！</p> --> 
            </div>
      <!-- nav -->
      <nav>
        <span class="nav-history" @click="navClickHandle1">
            <el-icon><Postcard /></el-icon>
          <span>历史对话</span>
        </span>
        <span class="nav-character" @click="navClickHandle2">
            <el-icon><Refresh /></el-icon>
          <span>重设角色</span>
        </span>
        <span class="nav-cancel" @click="navClickHandle3">
            <el-icon><SwitchButton /></el-icon>
          <span>退出登录</span>
        </span>
      </nav>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.page-header {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;

  .page-header-wrapper {
    flex: 1;
    display: flex;
    align-items: flex-end;
    justify-content: space-between;
  }

  //logo
  .logo-image {
    width: auto;
    height: 45px;
    cursor: pointer;
  }
.logo-text {
    margin-right: 1000px;
  }
  //nav
  nav {
    display: flex;
    align-items: center;
    gap:0.2em;
    margin-right:2px;
    .nav-history {
      display: flex;
      align-items: center;
      cursor: pointer;
      &:hover {
      color: #409eff;
    }
    }
    .nav-character {
      display: flex;
      align-items: center;
      cursor: pointer;
      &:hover {
      color: #409eff;
    }
    }
    .nav-cancel {
      display: flex;
      align-items: center;
      cursor: pointer;
      &:hover {
      color: #409eff;
    }
    }

    i {
      width: 24px;
      height: 24px;
    }
    
  }
}
</style>
