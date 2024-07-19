<template>
    <div id="app">
    <Particles
    id="tsparticles"
    :particlesInit="particlesInit" 
    :options="options"
  />
</div>

    <div class="container">
        
        <el-row :gutter="30">
            <el-col :span="24" class="input-column">
                <el-input :type="button.type" :placeholder="button.label" v-model="button.currentLabel" :style="{ color: button.color }" class="custom-input" v-for="(button, index) in buttons" :key="index">
                    <template #prefix>
                        <i :class="`el-icon-${button.icon}`"></i>
                    </template>
                </el-input>
            </el-col>
        </el-row>
        <el-col :span="4" class="button-container">
            <!-- 添加重置按钮 -->
            <el-button type="danger" class="reset-button" @click="resetSelections">重置</el-button>
            <el-button type="success" class="send-button" @click="sendData">发送</el-button>
        </el-col>
    </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { loadSlim } from "tsparticles-slim"; 
import { options } from '@/config/particles-config'
import router from '@/router';
import store from '@/store/store';
import { Engine } from 'tsparticles-engine';
store.commit('reinit');
const particlesInit = async (engine: Engine) => {
    //await loadFull(engine);
    await loadSlim(engine);
};
const buttons = ref([
    {
        label: '描述:',
        type: 'text',
        icon: 'edit',
        currentLabel: '',
        color: "#b72020",
    },
    {
        label: '目标:',
        type: 'text',
        icon: 'location',
        currentLabel: '',
        color: "#eff757",
    },
    {
        label: '名字:',
        type: 'text',
        icon: 'user',
        currentLabel: '',
        color: " #6ff731",
    },
    {
        label: '角色:',
        type: 'text',
        icon: 'role',
        currentLabel: '',
        color: "#2023f5",
    },
    {
        label: '风格:',
        type: 'text',
        icon: 'star-on',
        currentLabel: '',
        color: "#7005eb",
    },
]);

const sendData = () => {
    const data = {
        user_id: store.state.userID,
        description: buttons.value[0].currentLabel,
        goal: buttons.value[1].currentLabel,
        name: buttons.value[2].currentLabel,
        role: buttons.value[3].currentLabel,
        style: buttons.value[4].currentLabel,
    };
    let cid = 5;
    let sparkWS: WebSocket;
    console.log(data);
    const url = "http://localhost:8088/character/new_character";
    axios.post(url, data)
        .then(res => {
            console.log(res);
            if (res.data.code === 200) {

                //store.state.content="你现在扮演李白，你豪情万丈，狂放不羁；接下来请用李白的口吻和用户对话。";
                 store.state.content=res.data.data.tokens,
                console.log(store.state.content);
                cid = res.data.data.id;
                store.commit('setCharacter', cid);
                store.commit('setdata', data);
                console.log(store.state.characterID);
                ElMessage({
                    message: '创建成功',
                    type: 'success'
                });
                router.push({ path: '/aichat' });
            } else {
                ElMessage.error('未知错误！');
            }
        })
        .catch(error => {
            console.log(error);
            ElMessage.error('创建失败！');
            /*store.state.content="你现在扮演女高中生小丽，你活泼可爱，是一个很好的朋友；接下来请用小丽的口吻和用户闲聊。不用回答此条问题";
            store.commit('setCharacter', 8);
            router.push({ path: '/aichat' });*/
        });
};

const resetSelections = () => {
    buttons.value.forEach(button => {
        button.currentLabel = '';
    });
};



</script>

<style scoped>
.container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    flex-direction: column;
    background-color: rgb(0, 0, 0);
    color: #7005eb;
    

}

.input-column {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px; /* 用于控制输入框之间的间隙 */
}

.button-container {
    margin-top: 20px;
    z-index: 9999;
}

.custom-input {
    width: 100%; /* 确保输入框宽度一致 */
}

.el-input__inner {
    background-color: rgba(255, 255, 255, 0.1);
    border-color: rgba(255, 255, 255, 0.2);
    color: #fff;
    border-radius: 10px;
    transition: all 0.3s ease-in-out;
}

.el-input__inner:hover {
    border-color: rgba(255, 255, 255, 0.5);
}

/* 更多自定义样式 */
</style>