<script lang="ts" setup>
import { UserFilled } from "@element-plus/icons-vue";
import HeaderView from "@/components/ChatHead.vue";
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from "vue";
import {
    getWebsocketUrl,
    ChatItem,
    WSReqParams,
    WSResParams,
    wsSendMsgFormat,
} from "@/ts/AiChatWebsocket";
import { sparkConfig } from "@/config/config";
import { ElMessage } from "element-plus";
import axios from 'axios';
import store from "@/store/store"
import VantaBackground from './PortfolioView.vue';
//定义websocket 实例
let sparkWS: WebSocket;
//会话列表
const chatList = ref(store.getters.chatlist);
//let chatList = ref<ChatItem[]>([]);
//加载当前回答的index
let loadingIndex = ref<number | null | undefined>();
//提问问题内容
let problemText = ref<string>("");
//websocket 响应数据的状态
let wsMsgReceiveStatus = ref<"receiveIng" | "receiveFinished">();
//提问的最大字数
const maxCharCount = ref<number>(300);
//发送按钮的禁用状态
const sendBtnDisabled = ref(false);


//发送问题的函数
const sendQuestion = () => {
    if (sendBtnDisabled.value) {
        //阻止内容发送
        return;
    }
    if (problemText.value?.trim()?.length <= 0) {
        //输入问题文字是空字符串的提示
        ElMessage.warning({ message: "请输入您想要了解的内容..." });
        return;
    }



    //不在接受消息的时候才可以发送问题
    if (wsMsgReceiveStatus.value !== "receiveIng") {
        chatList.value.push({
            role: "user",
            content: problemText.value,
        });
        sendBtnDisabled.value = true;
        //调用连接星火认知大模型并发送问题的函数
        askSpark();
    }
};
//连接星火认知大模型并发送问题
const askSpark = () => {
    //1. 生成鉴权URL
    let wsUrl = getWebsocketUrl(sparkConfig);
    //2.判断浏览器是否websocket
    if ("WebSocket" in window) {
        //创建websocket 实例
        sparkWS = new WebSocket(wsUrl as string);
    } else {
        ElMessage.error("当前浏览器不支持websocket");
        return;
    }
    //3. 建立websocket连接
    //3.1 打开连接
    sparkWS.onopen = () => {
        //发送数据
        const sendData: WSReqParams = wsSendMsgFormat(sparkConfig, chatList.value);
        sparkWS.send(JSON.stringify(sendData));

        chatList.value.push({
            role: "assistant",
            content: "",
        });
        loadingIndex.value = chatList.value.length - 1;
    };
    //3.2 消息监听
    sparkWS.onmessage = (res: MessageEvent) => {
        //响应消息
        let resObj: WSResParams = JSON.parse(res.data);
        if (resObj.header.code !== 0) {
            ElMessage.error("提问失败");
            console.error(
                `提问失败：${resObj.header.code} - ${resObj.header.message}`
            );
            //连接关闭
            sparkWS.close();
        } else {
            //处理响应回来的数据
            wsMsgReceiveHandle(resObj);
        }
    };
    //3.3 连接异常
    sparkWS.onerror = (e) => {
        ElMessage.error("WebSocket 连接失败" + e);
        console.error(`WebSocket 连接失败,连接的URL${wsUrl}`);
    };
    //3.4 连接关闭
    sparkWS.onclose = () => { };
};
/**
 * 处理websocket 返回的数据
 * @param res WSResParams
 */
const wsMsgReceiveHandle = (res: WSResParams) => {
    let dataArray = res?.payload?.choices?.text || [];
    for (let i = 0; i < dataArray.length; i++) {
        chatList.value[chatList.value.length - 1].content += dataArray[i].content;
    }
    //开始接受消息
    if (res.payload.choices.status === 0) {
        problemText.value = "";
        wsMsgReceiveStatus.value = "receiveIng";
    }
    //继续接受消息
    if (res.payload.choices.status === 1) {
        wsMsgReceiveStatus.value = "receiveIng";
    }
    //接受消息完毕
    if (res.payload.choices.status === 2) {
        wsMsgReceiveStatus.value = "receiveFinished";
        loadingIndex.value = null;
        sendBtnDisabled.value = false;
        sparkWS.close();
        console.log(time);
        console.log(nowDate(time));
        let params = {
            user_id: store.state.userID,
            character_id: store.state.characterID,
            question: chatList.value[chatList.value.length - 2].content,
            answer: chatList.value[chatList.value.length - 1].content,
            time: nowDate(time)
        };
        var url = "http://localhost:8088/history/store";
        axios.post(url, params).catch((error: Error) => {
            console.error('Failed to send response to backend:', error);
        });
    }
};

const setinit = () => {
    if (!store.state.loading) {
        chatList.value.push({
            role: "user",
            content: store.state.content,
        });
        store.commit('setLoading');
        askSpark();
    }

};

setinit();

var getTime = new Date().getTime(); //获取到当前时间戳
var time = new Date(getTime); //创建一个日期对象
function nowDate(time: Date) {
    var year = time.getFullYear(); // 年
    var month = (time.getMonth() + 1).toString().padStart(2, '0'); // 月
    var date = time.getDate().toString().padStart(2, '0'); // 日
    var hour = time.getHours().toString().padStart(2, '0'); // 时
    var minute = time.getMinutes().toString().padStart(2, '0'); // 分
    var second = time.getSeconds().toString().padStart(2, '0'); // 秒
    return (
        year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second
    )
}


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
const observer = ref<MutationObserver | null>(null);
onMounted(async () => {
    //调用监听目标元素高度变化的函数
    const loadScript = (url: string) => {
        return new Promise((resolve, reject) => {
            const script = document.createElement('script');
            script.src = url;
            script.onload = resolve;
            script.onerror = reject;
            document.head.appendChild(script);
        });
    };

    await loadScript('https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.2.0/crypto-js.min.js');
    await loadScript('https://cdn.jsdelivr.net/gh/FATEANIME/ai6@master/autoload.js');
    await nextTick();

    let targetNode = document.getElementById('waifu-tips');
    let lastSentAnswer = ''; // 新增的状态变量，用于存储上一次发送的answer值

    if (targetNode) {
        console.log('Element with ID waifu_tips found.');
        observer.value = new MutationObserver((mutationsList) => {
            for (let mutation of mutationsList) {
                const currentAnswer = (mutation.target as HTMLElement).innerHTML;

                // 检查当前的answer是否与上次发送的不同
                if (currentAnswer != lastSentAnswer && !store.state.playing) {
                    lastSentAnswer = currentAnswer; // 更新lastSentAnswer

                    let params = {
                        text: currentAnswer,
                    };
                    console.log('whatcanisay', params);
                    var url = "http://localhost:8088/espeak/textToVoice";
                    axios.post(url, params)
                        .then(response => {
                            console.log(response);
                            let base64String = response.data;
                            const binaryString = atob(base64String);
                            const len = binaryString.length;
                            const bytes = new Uint8Array(len);
                            for (let i = 0; i < len; i++) {
                                bytes[i] = binaryString.charCodeAt(i);
                            }
                            const audioBlob = new Blob([bytes], { type: 'audio/wav' });
                            const audioUrl = URL.createObjectURL(audioBlob);
                            const audio = new Audio(audioUrl);
                            audio.play().catch(error => { console.error('播放失败:', error); ElMessage.error('播放失败！'); });
                        })
                        .catch(error => { console.error('请求播放失败:', error); ElMessage.error('请求播放失败！'); });
                }
            }
        });
        const config = { attributes: true, childList: true, subtree: true };
        observer.value?.observe(targetNode, config);
    } else {
        console.error('Element with ID "waifu_tips" not found.');
    }
    createMutationServer(aiChatListRef.value);

});
//导入复制内容到剪贴板的ts库
import { copyToClipboard } from "@/utils/commonUtil";
/**
 * 复制会话内容到剪贴板
 * @param item
 * @param index
 */
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
/**
 * markdown内容复制
 */
const handleCopyCodeSuccess = () => {
    ElMessage({
        message: "复制成功",
        type: "success",
    });
};
/**
 * 删除聊天记录
 * @param index
 */
const deleteRecord = (index: number) => {
    if (!sendBtnDisabled.value) {
        chatList.value.splice(index, 1);
    }
};
/**
 * 重新回答
 */
const reReply = (index: number) => {
    if (wsMsgReceiveStatus.value !== "receiveIng") {
        //如果是最后一条内容的重新回答，直接删除最后一条回答记录并重新作答
        if (chatList.value.length - 1 === index) {
            deleteRecord(index);
            sendBtnDisabled.value = true;
            askSpark();
        } else {
            //如果不是最后一条重新回答，则在后面重新添加问题继续进行询问
            //有可能上一条回答内容已被删除，所以需要循环找到最近的一条问题记录进行作答
            let i = index - 1;
            while (i >= 0) {
                //角色为用户并且有问题内容
                if (chatList.value[i].role === "user" && chatList.value[i].content) {
                    chatList.value.push({
                        role: "user",
                        content: chatList.value[index - 1].content,
                    });
                    sendBtnDisabled.value = true;
                    askSpark();
                    break;
                }
                i--;
            }
        }
    }
};


const voiceplay = (item: { content: any }, index: any) => {
    let params = {
        text: item.content,
    };
    console.log(params);
    var url = "http://localhost:8088/espeak/textToVoice";
    axios.post(url, params)
        .then(response => {
            console.log(response);
            let base64String = response.data;
            const binaryString = atob(base64String);
            const len = binaryString.length;
            const bytes = new Uint8Array(len);
            for (let i = 0; i < len; i++) {
                bytes[i] = binaryString.charCodeAt(i);
            }
            const audioBlob = new Blob([bytes], { type: 'audio/wav' });
            const audioUrl = URL.createObjectURL(audioBlob);
            const audio = new Audio(audioUrl);
            audio.play().catch(error => { console.error('播放失败:', error); ElMessage.error('播放失败！'); });
        })
        .catch(error => { console.error('请求播放失败:', error); ElMessage.error('请求播放失败！'); });
};
/**
 * 监听问题内容长度的函数
 */
const problemTextWatcher = watch(
    () => problemText.value,
    () => {
        //限制最大字数
        if (problemText.value.length > maxCharCount.value) {
            problemText.value = problemText.value.slice(0, maxCharCount.value);
        }
    }
);
/**
 * 组件销毁之前的监听处理
 */
onBeforeUnmount(() => {
    //停止监听问题内容长度
    problemTextWatcher();
    //停止会话内容监听
    chatListObserver.disconnect();
});
</script>
<template>
    <header class="page-layout-header">
        <div class="page-layout-row">
            <!-- 引入公共的头部组件 -->
            <HeaderView />
        </div>
    </header>

    <div class="ai-chat-view">
        <div class="ai-chat-back">
            <VantaBackground>
                <div class="banner">
                    <h1>滚&ensp;滚&ensp;红&ensp;尘</h1>
                    <h6>&ensp;&ensp;&ensp;AI&ensp;&ensp;&ensp;亦&ensp;&ensp;&ensp;作&ensp;&ensp;&ensp;人
                    </h6>
                    <p>描述: {{ store.state.items.description }}&ensp;&ensp;目标: {{ store.state.items.goal }}&ensp;&ensp;姓名: {{ store.state.items.description }}&ensp;&ensp;角色: {{ store.state.items.role }}&ensp;&ensp;风格: {{ store.state.items.style }}</p>
                </div>
            </VantaBackground>
        </div>
        <ul class="ai-chat-list" ref="aiChatListRef">
            <!-- 聊天内容 -->
            
            <li class="ai-chat-item" :class="item.role + '-item'" v-for="(item, index) in chatList.slice(1)"
                :key="index">
                <!-- 头像 -->
                <div class="ai-chat-avatar">
                    <el-avatar v-if="item.role === 'user'"
                        src="https://static.hikarifield.co.jp/images/shop/otomeriron/thumb/005.jpg" :size="40" />
                    <el-avatar v-if="item.role === 'assistant'"
                        src="https://static.hikarifield.co.jp/images/shop/tsukiniyorisou/thumb/003.jpg" :size="40" />
                </div>
                <!-- 聊天内容 -->
                <div class="ai-chat-content-box" :class="item.role + '-box'" v-if="item.role === 'user'">
                    {{ item.content }}
                </div>
                <div class="ai-chat-content-box" :class="item.role + '-box'" v-if="item.role === 'assistant'">
                    角色语
                    <!-- 支持md 预览 -->
                    <v-md-preview :text="item.content" @copy-code-success="handleCopyCodeSuccess"></v-md-preview>
                    <!-- 加载图标 -->
                    <div class="loading-icon-box" v-if="loadingIndex === index">
                        <el-icon>
                            <Loading />
                        </el-icon>
                    </div>
                    <div class="ai-chat-operate">
                        <!--重新回答  -->
                        <span class="re-reply-btn" @click="reReply(index)" :class="{ disabled: sendBtnDisabled }">
                            重新回答
                        </span>

                        <div class="operate-icon-box" :class="{ disabled: sendBtnDisabled }">
                            <el-icon @click="voiceplay(item, index)">
                                <Microphone />
                            </el-icon>
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
        </ul>
        <!-- 文本发送区域 -->
        <div class="ai-chat-form-wrapper">
            <div class="ai-chat-form-box">
                <textarea v-model="problemText" :rows="4" placeholder="在此输入您想要了解的内容..."
                    @keydown.enter.exact.prevent="sendQuestion"
                    @keydown.enter.shift.exact.prevent="problemText += '\n'">
        </textarea>
                <div class="chat-form-footer">
                    <div class="btns">
                        <span class="content-tips">
                            {{ problemText.length }} / {{ maxCharCount }}
                        </span>
                        <span>
                            <el-button type="primary" :disabled="sendBtnDisabled" @click="sendQuestion">发送</el-button>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<style lang="scss" scoped>
.ai-chat-view {
    display: flex;
    flex-direction: column;
    flex: 1;
    position: fixed;
    height: 100%;
    width: 100%;
    overflow: hidden;

    padding: 40px 15px;


    .ai-chat-back {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        z-index: -1; // 确保背景在所有其他元素下面

        .banner {
            position: absolute;
            margin-top: 140px;
            margin-left: 180px;
            
    color: rgba(27, 26, 26, 0.7);

            h1,
            h6,
            p {
                margin: 0;
                padding: 10px 0;
            }

            h1 {
                font-size: 15em;
            }

            h6 {
                margin-left: 520px;
                font-size: 1.5em;
            }

            p {
                margin-left: 900px;
                font-size: 1.2em;
            }
        }
    }

    //对话列表
    .ai-chat-list {
        display: flex;
        flex: 1;
        flex-direction: column;
        overflow-y: auto;

        //消除浏览器滚动条
        &::-webkit-scrollbar {
            display: none;
        }

        //会话项
        .ai-chat-item {
            display: flex;
            align-items: flex-start;
            margin-bottom: 40px;
        }

        //会话头像
        .ai-chat-avatar {
            margin-right: 25px;
        }

        //会话盒子
        .ai-chat-content-box {
            padding: 16px 30px;

            //加载图标盒子
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

            //会话列表初始化盒子
            &.init-box {
                width: 100%;
                background-color: #eff7ff;
                background-image: url("@/assets/anime.png");
                background-repeat: no-repeat;
                background-size: cover;
                border-radius: 10px;

                .ai-chat-title {
                    font-size: 1.125rem;
                    color: #005fdb;
                    margin-bottom: 1rem;
                }

                .ai-chat-text {
                    font-size: 0.875rem;
                    color: #0c0c0c;
                    line-height: 1.8;
                }
            }

            //会话列表用户盒子
            &.user-box {
                background-color: #fff;
                line-height: 1;
                padding-left: 1;
                padding-top: 1;
                border-radius: 10px;
                background-image: url("https://static.hikarifield.co.jp/images/shop/otomeriron/thumb/001.jpg");
            }

            //会话列表ai回复盒子
            &.assistant-box {
                border-radius: 10px;
                background: #eff7ff;
                width: 100%;
                background-image: url("@/assets/ws.jpg");
            }
        }

        //会话操作
        .ai-chat-operate {
            display: flex;
            justify-content: space-between;
            align-items: center;
            cursor: pointer;

            //重复回答
            .re-reply-btn {
                font-size: 14px;
                color: #005fdb;

                &.disabled {
                    color: #ccc;
                }
            }

            //操作图标
            .operate-icon-box {
                display: flex;
                align-items: center;

                .el-icon {
                    color: #ad1580;
                    font-size: 16px;
                    margin-left: 16px;
                    cursor: pointer;
                }

                &.disabled .el-icon {
                    color: #ccc;
                }
            }
        }
    }

}

.ai-chat-form-wrapper {
    background-color: #fff;
    padding-left: calc(0px + 0px);
    border-radius: 10px;
    width: 78%;
    /* 设置宽度为屏幕宽度的一半 */
    height: 113px;
    justify-content: flex-end;
    /* 或者使用flex布局来实现靠右显示 */
    margin-left: 350px;
    margin-bottom: 15px;
    position: relative;
    /* 使用相对定位，避免与绝对定位的背景冲突 */
    z-index: 1;

    .ai-chat-form-box {
        border: 1px solid #005fdb;
        border-radius: 10px;
        position: relative;
        height: 100%;
    }

    //文本域
    textarea {
        width: calc(110vh - 4px); // 减去滚动条的宽度
        margin-top: 2px;
        padding: 0.5rem 6rem 1rem 1.25rem;
        border: none;
        outline: none;
        resize: none;
        border-radius: 10px;
        color: #000000;
        height: 97%;

        &::-webkit-scrollbar {
            width: 3px;
        }
    }

    //发送问题表单footer
    .chat-form-footer {
        display: flex;
        justify-content: flex-end;
        margin-top: -5px;
        position: absolute;
        bottom: 1rem;
        right: 1rem;

        //内容数字提示
        .content-tips {
            margin-right: 1.25rem;
        }
    }
}

//发送问题表达</style>
