<template>
  <router-link to="/history" class="re-reply-btn">查询历史</router-link>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted, nextTick, ref } from 'vue';
import store from "@/store/store";
import axios from 'axios';
const observer = ref<MutationObserver | null>(null);


onMounted(async () => {
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
  await loadScript('https://cdn.jsdelivr.net/gh/FATEANIME/ai3@master/autoload.js');
  await nextTick();
  // 现在脚本应该已经加载完成，可以安全地查找元素
  /*let targetNode = document.getElementById('waifu-tips');
let lastSentAnswer=''; // 新增的状态变量，用于存储上一次发送的answer值

if (targetNode) {
  console.log('Element with ID waifu_tips found.');
  observer.value = new MutationObserver((mutationsList) => {
    for (let mutation of mutationsList) {
      const currentAnswer = (mutation.target as HTMLElement).innerHTML;
      
      // 检查当前的answer是否与上次发送的不同
      if (currentAnswer != lastSentAnswer) {
        lastSentAnswer = currentAnswer; // 更新lastSentAnswer

        let params = {
          UID:store.state.userID ,
          CID:store.state.characterID,
          answer: currentAnswer,
          time: nowDate(time)
        };
        console.log(params);

        var url = "http://localhost:8088/account/read";
        axios.post(url, params).catch((error: Error) => {
          console.error('Failed to send response to backend:', error);
        });
      }
    }
  });
  const config = { attributes: true, childList: true, subtree: true };
  observer.value?.observe(targetNode, config);
} else {
  console.error('Element with ID "waifu_tips" not found.');
}*/

 
  
});


</script>

<style scoped>
/* Add your component styles here */
</style>