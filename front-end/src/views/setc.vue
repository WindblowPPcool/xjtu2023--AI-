<template>
  <div class="container">
    <el-row :gutter="30" justify="center">
      <el-col :span="500" v-for="(button, index) in buttons" :key="index">
        <el-button :type="button.type" :color="button.color" @click="toggleOptions(index)">
          {{ button.label }}
          <el-popover placement="bottom-start" trigger="click" v-model="button.showOptions">
            <template #reference>
              <button class="fixed-width">{{ button.currentLabel || '点击选择' }}</button>
            </template>
            <el-scrollbar style="max-height: 150px;">
              <el-radio-group v-model="button.selectedOptions">
                <el-radio v-for="option in button.options" :key="option.value" :label="option.value"
                  @change="updateCurrentLabel(button, option)">{{ option.label }}</el-radio>
              </el-radio-group>
            </el-scrollbar>
          </el-popover>
        </el-button>
      </el-col>
    </el-row>
    <el-col :span="4" class="button-container">
      <!-- 添加重置按钮 -->
      <el-button type="danger" class="reset-button" @click="resetSelections">重置</el-button>
      <el-button type="success" class="send-button" @click="sendData(buttons)">发送</el-button>
    </el-col>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { descriptionProps, ElMessage } from 'element-plus';

const buttons = ref([
  {
    label: '描述:',
    type: 'primary',
    showOptions: false,
    selectedOptions: [],
    currentLabel: '',
    color: "#b72020",
    options: [
      { value: 'option1', label: '选项1' },
      { value: 'option2', label: '选项2' },
    ],
  },
  {
    label: '目标:',
    type: 'primary',
    showOptions: false,
    selectedOptions: [],
    currentLabel: '',
    color: "#eff757",
    options: [
      { value: 'option1', label: '选项1' },
      { value: 'option2', label: '选项2' },
    ],
  },
  {
    label: '名字:',
    type: 'primary',
    showOptions: false,
    selectedOptions: [],
    currentLabel: '',
    color: " #6ff731",
    options: [
      { value: 'option1', label: '选项1' },
      { value: 'option2', label: '选项2' },
    ],
  },
  {
    label: '角色:',
    type: 'primary',
    showOptions: false,
    selectedOptions: [],
    currentLabel: '',
    color: "#2023f5",
    options: [
      { value: 'option1', label: '选项1' },
      { value: 'option2', label: '选项2' },
    ],
  },
  {
    label: '风格:',
    type: 'primary',
    showOptions: false,
    selectedOptions: [],
    currentLabel: '',
    color: "#7005eb",
    options: [
      { value: 'option1', label: '选项1' },
      { value: 'option2', label: '选项2' },
    ],
  },
]);

const toggleOptions = (index) => {
  buttons.value[index].showOptions = !buttons.value[index].showOptions;
};

const sendData = (buttons) => {
  /*const data = buttons.value.map(button => ({
    label: button.label,
    selectedOptions: button.selectedOptions,
  }));*/
  const data = {
    description: buttons[0].currentLabel,
    goal: buttons[1].currentLabel,
    name: buttons[2].currentLabel,
    role: buttons[3].currentLabel,
    style: buttons[4].currentLabel,
  };
  console.log(data);
  var url = "http://localhost:8088/account/set";
  axios.post(url, data)
    .then(res => {
      console.log(res);
      if (res.data.code === 200) {
        ElMessage({
          message: '创建成功',
          type: 'success'
        });
      } else {
        ElMessage.error('未知错误！');
      }
    })
    .catch(error => {
      console.log(error);
      ElMessage.error('查录失败！');
    });
};

const updateCurrentLabel = (button, option) => {
  button.currentLabel = option.label;
};

const resetSelections = () => {
  buttons.value.forEach(button => {
    button.selectedOptions = null;
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

.button-container {
  margin-top: 20px;
}

.fixed-width {
  width: 100px;
  /* 设置你想要的固定宽度 */
  overflow: hidden;
  /* 隐藏超出的内容 */
  text-overflow: ellipsis;
  /* 显示省略号 */
  white-space: nowrap;
  /* 不换行 */
}
</style>