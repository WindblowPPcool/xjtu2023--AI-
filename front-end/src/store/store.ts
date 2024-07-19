//引入vue和Vuex
import Vue from 'vue'
import Vuex from 'vuex'
import { ChatItem } from "@/ts/AiChatWebsocket";

//创建且声明一个对象
const store = new Vuex.Store({
    state:{
        userID:0,
        characterID:0,
        content:'请忘记之前的对话和设定，重新开始对话，收到请回复“已初始化”',
        playing:true,
        loading:false,
        chatingList:[] as ChatItem[],
        items:
        {
        description: '空',
        goal: '空',
        name: '空',
        role: '空',
        style: '空',
        },
        
    },
    getters: {
      chatlist: state => state.chatingList
    },
    mutations: {
        setUser (state,username) {
          state.userID=username;
        },
        setCharacter (state,cname) {
            state.characterID=cname;
          },
        changeplaying(state){
            state.playing=!state.playing;
            console.log(state.playing);
        },
        setdata(state,data)
        {
            state.items.description=data.description;
            state.items.goal=data.goal;
            state.items.name=data.name;
            state.items.role=data.role;
            state.items.style=data.style;
        },
        setLoading(state){
            state.loading=true;
        },
        reinit(state){
            state.characterID=0;
            state.content='请忘记之前的对话和设定，重新开始对话，收到请回复“已初始化”';
            state.loading=false;
            state.chatingList=[];
            state.items={
                description: '空',
                goal: '空',
                name: '空',
                role: '空',
                style: '空',
                };
        },
        relogin(state) {
          state.userID = 0;
          state.characterID = 0;
          state.content = '请忘记之前的对话和设定，重新开始对话，收到请回复“已初始化”';
          state.playing = true;
          state.loading = false;
          state.chatingList = [];
          state.items = {
            description: '空',
            goal: '空',
            name: '空',
            role: '空',
            style: '空',
          };
          console.log(state);
        },
      }
})


export default store