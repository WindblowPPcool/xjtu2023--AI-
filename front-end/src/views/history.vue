<script lang="ts">
import { onBeforeUnmount, defineComponent, onMounted, ref, watch, nextTick, onUnmounted } from 'vue';
import axios from 'axios';
import store from '@/store/store';
import { copyToClipboard } from "@/utils/commonUtil";
import { ElMessage } from 'element-plus';
import router from '@/router';

import * as THREE from 'three';
import WAVES from 'vanta/dist/vanta.waves.min';

const showReplyBtn = ref(true);


const changeplaying = () => {
    store.commit('changeplaying');
};
const logoImgClickHandle = () => {
    window.location.href = "https://gitee.com/the-eight-fairies";
};
//点击导航菜单 的处理
const navClickHandle1 = () => {
    if (!store.state.playing) {
        store.commit('changeplaying');
    }
    router.push("/aichat");
};

const navClickHandle2 = () => {
    location.href = '/';
};

export default defineComponent({
    name: 'history',
    setup() {
        interface ChatItem {
            role: "user" | "assistant";
            content: string;
            inum: number;
        }
        const sendBtnDisabled = ref(false);

        const mytext = ref<ChatItem[]>([]);
        const fetchChatHistory = () => {
            let params = {
                character_id: store.state.characterID,
                id: 0
            };
            console.log(params);
            const url = "http://localhost:8088/history/read";
            axios.post(url, params)
                .then(res => {
                    console.log(res);
                    let msglist = res.data.data;
                    if (res.data.code === 200) {
                        ElMessage({
                            message: "查录成功",
                            type: 'success'
                        });
                        showReplyBtn.value = false;
                        for (let i = 0; i < msglist.length; i++) {
                            const element1 = msglist[i];
                            mytext.value.push({
                                role: "user",
                                content: element1.question,
                                inum: i
                            }); // 注意使用 .value 来访问 ref 的值
                            const element2 = msglist[i];
                            mytext.value.push({
                                role: "assistant",
                                content: element2.answer,
                                inum: i
                            });
                        }
                    } else {
                        ElMessage.error('未知错误！');
                    }
                })
                .catch(error => {
                    console.log(error);
                    ElMessage.error('查录失败！');
                });
        };
        const copyRecord = (item: { content: any }, index: any) => {
            const content = item.content;
            copyToClipboard({
                content,
                success() {
                    ElMessage({
                        message: "复制成功",
                        type: "success",
                    });
                },
                error() {
                    ElMessage({
                        message: "复制失败",
                        type: "error",
                    });
                },
            });
        };
        const deleteRecord = (index: number) => {
            if (!sendBtnDisabled.value) {
                mytext.value.splice(index, 1);
            }
        };
        const keepload = (index: number) => {
            let params = {
                character_id: store.state.characterID,
                id: mytext.value[index].inum
            };
            console.log(params);
            const url = "http://localhost:8088/history/read";
            axios.post(url, params)
                .then(res => {
                    console.log(res);
                    let msglist = res.data.data;
                    if (res.data.code === 200) {
                        ElMessage({
                            message: "查录成功",
                            type: 'success'
                        });
                        for (let i = 0; i < msglist.length; i++) {
                            const element1 = msglist[i];
                            mytext.value.push({
                                role: "user",
                                content: element1.question,
                                inum: i
                            }); // 注意使用 .value 来访问 ref 的值
                            const element2 = msglist[i];
                            mytext.value.push({
                                role: "assistant",
                                content: element2.answer,
                                inum: i
                            });
                        }
                    } else {
                        ElMessage.error('未知错误！');
                    }
                })
                .catch(error => {
                    console.log(error);
                    (this as any).$message.error('登录失败！');
                });
        };


        //定义会话列表的观察对象（观察子元素的变化，当会话有变化时，自动滚动到变化的位置，用于提高用户的体验度）
        let chatListObserver: MutationObserver;
        //聊天列表引用对象
        const aiChatListRef = ref();
        /**
         * 聊天列表变化的观察对象：用于监听目标元素的高度变化
         * @param targetElement
         */
        const createMutationServer = (targetElement: Element) => {
            //创建MutationServer 实例，用于监测Dom变化
            chatListObserver = new MutationObserver((mutationList, observer) => {
                //当子元素变化发生变化时，获取元素滚动的高度
                const scrollHeight = targetElement.scrollHeight;
                //调用滚动处理函数
                scrollHandle(scrollHeight);
            });
            //启动检测器并配置所需的观察选项
            chatListObserver.observe(targetElement, { childList: true, subtree: true });
        };
        /**
         * 滚动定位处理
         * @param val
         */
        const scrollHandle = (val: number) => {
            aiChatListRef.value?.scrollTo({
                //scrollTo 把内容滚动到指定的坐标
                top: val,
                behavior: "smooth", //平滑滚动
            });
        };
       
        const login = ref<HTMLElement | null>(null);
        let vantaEffect: any;
        onMounted(async () => {
            vantaEffect = WAVES({
                el: login.value,
                THREE: THREE,
                mouseControls: true,
                touchControls: true,
                gyroControls: false,
                minHeight: 200.00,
                minWidth: 200.00,
                scale: 1.00,
                scaleMobile: 1.00
            });
            fetchChatHistory();

            nextTick(() => {
                if (aiChatListRef.value) {
                    createMutationServer(aiChatListRef.value);
                }
                
            });
        });
        onUnmounted(() => {
            if (vantaEffect) {
                vantaEffect.destroy();
            }
        });

        return {
            mytext,
            store,
            fetchChatHistory,
            sendBtnDisabled,
            copyRecord,
            deleteRecord,
            keepload,
            showReplyBtn,
            logoImgClickHandle,
            navClickHandle1,
            navClickHandle2,
            changeplaying,
            login
        };
    }
});

</script>
<template>
    
    <header class="page-layout-header">
        <div class="page-layout-row">
            <!-- 引入公共的头部组件 -->
            <div class="page-header">
                <div class="page-header-wrapper">
                    <!-- logo -->
                    <el-image src="https://static.hikarifield.co.jp/images/hf-logo.png" class="logo-image" fit="cover"
                        @click="logoImgClickHandle" />
                    <div class="logo-text" @click="logoImgClickHandle">
                        <h1>角色历史对话<span style="font-size:0.1em">By The Eight Fairies</span></h1>
                        <!-- <p>凝新聚华，将美少女游戏文化推向世界！</p> -->
                    </div>
                    <!-- nav -->
                    <nav>
                        <span class="nav-chat" @click="navClickHandle1">
                            <el-icon>
                                <Postcard />
                            </el-icon>
                            <span>继续聊天</span>
                        </span>
                        <span class="nav-voice" @click="changeplaying()">
                            <el-icon>
                                <template v-if="store.state.playing">
                                    <VideoPlay />
                                </template>
                                <template v-else>
                                    <VideoPause />
                                </template>
                            </el-icon>
                            <span>{{ store.state.playing ? '开启助手声' : '关闭助手声' }}</span>
                        </span>
                        <span class="nav-cancel" @click="navClickHandle2">
                            <el-icon>
                                <SwitchButton />
                            </el-icon>
                            <span>退出登录</span>
                        </span>
                    </nav>
                </div>
            </div>
        </div>
    </header>
    
    <!-- 表单 -->
    <div ref="login" class="flex flex-justify-center flex-items-center h-100vh">
    <div class="container">

        <span class="re-reply-btn" v-show="showReplyBtn">暂无历史记录</span>

        <div class="ai-chat-view" v-show="!showReplyBtn">
            <ul class="ai-chat-list" ref="aiChatListRef">
                <li class="ai-chat-item" :class="item.role + '-item'" v-for="(item, index) in mytext.slice(0)"
                    :key="index">
                    <!-- 头像 -->
                    <div class="ai-chat-avatar">
                        <el-avatar v-if="item.role === 'user'"
                            src="https://static.hikarifield.co.jp/images/shop/otomeriron/thumb/005.jpg" :size="40" />
                        <el-avatar v-if="item.role === 'assistant'"
                            src="https://static.hikarifield.co.jp/images/shop/tsukiniyorisou/thumb/003.jpg"
                            :size="40" />
                    </div>
                    <!-- 聊天内容 -->
                    <div class="ai-chat-content-box" :class="item.role + '-box'" v-if="item.role === 'user'">
                        {{ item.content }}
                    </div>
                    <div class="ai-chat-content-box" :class="item.role + '-box'" v-if="item.role === 'assistant'">
                        AI回复内容
                        <!-- 支持md 预览 -->
                        <div class="ai-chat-content-box" :class="item.role + '-box'">
                            {{ item.content }}
                        </div>
                        <div class="ai-chat-operate">
                            <div class="operate-icon-box" :class="{ disabled: sendBtnDisabled }">
                                <!-- 聊天内容复制 -->
                                <el-icon @click="copyRecord(item, index)">
                                    <DocumentCopy />
                                </el-icon>
                                <!-- 删除聊天内容 -->
                                <el-icon @click="deleteRecord(index)">
                                    <Delete />
                                </el-icon>
                            </div>
                        </div>
                    </div>
                </li>



                <div class="keep__load" @click="keepload(mytext.values.length - 1)" v-if="mytext.values.length > 0">继续加载
                </div>

            </ul>

        </div>
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
        justify-content: flex-end;
        /* 或者使用flex布局来实现靠右显示 */
        margin-right: 1000px;

    }

    //nav
    nav {
        display: flex;
        align-items: center;
        gap: 0.2em;
        margin-right: 2px;

        .nav-chat {
            display: flex;
            align-items: center;
            cursor: pointer;

            &:hover {
                color: #409eff;
            }
        }

        .nav-voice {
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
.login {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: -1;
}

.container {
    position: relative;
    min-height: 100vh;
    /* 确保容器至少覆盖整个视口高度 */
    display: flex;
    justify-content: center;
    align-items: center;
}

.re-reply-btn {
    font-weight: bold;
    font-size: 2.5em;
    border: none;
    padding: 0;
    background: none;
    display: inline;
    text-decoration: none;
    color: #fcf954;
}

.ai-chat-view {
    background-color: #fff;
    padding: 40px 150px 16px;
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .ai-chat-list {
        flex: 1;
        display: flex;
        flex-direction: column;
        overflow-y: auto;
        /* Firefox */
        scrollbar-width: none;
        /* IE10+ */
        -ms-overflow-style: none;
        /* Firefox */
        overflow: -moz-scrollbars-none;

        /* webkit */
        &::-webkit-scrollbar {
            display: none;
        }

        .ai-chat-item {
            display: flex;
            align-items: flex-start;
            margin-bottom: 40px;
        }

        .ai-chat-avatar {
            margin-right: 24px;
        }

        .ai-chat-content-box {
            padding: 16px 30px;

            .loading-icon-box {
                .el-icon {
                    transform: translate(0, 0);
                    animation: rotate 3s linear infinite;
                }

                @keyframes rotate {
                    0% {
                        transform: translate(0, 0) rotate(0deg);
                    }

                    100% {
                        transform: translate(0, 0) rotate(360deg);
                    }
                }
            }

            &.init-box {
                width: 100%;
                background: #eff7ff;
                border-radius: 10px;
                background-image: url("https://ydcqoss.ydcode.cn/static/officialhome/ai-chat-init-bg.png");
                background-size: cover;
                background-repeat: no-repeat;

                .ai-chat-title {
                    font-size: 1.125rem;
                    color: #005fdb;
                    margin-bottom: 1rem;
                }

                .ai-chat-text {
                    font-size: 0.875rem;
                    color: #666666;
                    line-height: 1.8;
                }
            }

            &.user-box {
                background: #fff;
                padding-left: 0;
                padding-top: 0;
                line-height: 2;
            }

            &.assistant-box {
                width: 100%;
                background: #eff7ff;
                border-radius: 10px;
            }
        }

        .ai-chat-operate {
            display: flex;
            justify-content: space-between;
            align-items: center;
            cursor: pointer;

            .re-reply-btn {
                font-size: 14px;
                color: #2984ff;

                &.disabled {
                    color: #ccc;
                }
            }

            .operate-icon-box {
                display: flex;
                align-items: center;

                .el-icon {
                    color: #7094c4;
                    font-size: 20px;
                    margin-left: 16px;
                    cursor: pointer;
                }

                &.disabled .el-icon {
                    color: #ccc;
                }
            }
        }
    }

    .ai-chat-form-wrapper {
        padding-left: calc(40px + 24px);
        background-color: #fff;
        z-index: 1000;

        .ai-chat-form-box {
            border: 1px solid #526ef9;
            border-radius: 10px;
            position: relative;
        }

        textarea {
            width: calc(100vh - 50px);
            margin-top: 2px;
            padding: 0.5rem 6rem 1rem 1.25rem;
            padding-bottom: 0;
            border: none;
            outline: none;
            resize: none;
            background: #fbfbfb;
            border-radius: 10px;
            font-family: PingFang SC, Hiragino Sans GB, Arial, Microsoft YaHei, Helvetica Neue, sans-serif;
            color: #666;

            &::-webkit-scrollbar {
                width: 3px;
            }
        }

        .chat-form-footer {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            margin-top: -5px;
            background: #fbfbfb;
            position: absolute;
            bottom: 1rem;
            right: 1rem;

            .content-tips {
                margin-right: 1.25rem;
            }
        }
    }

    :deep(.v-md-editor-preview .github-markdown-body) {
        padding: 0;
    }
}
</style>