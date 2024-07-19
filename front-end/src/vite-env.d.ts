/// <reference types="vite/client" />
declare module "*.vue" {
    import type { DefineComponent } from "vue";
    const vueComponent: DefineComponent<{}, {}, any>;
    export default vueComponent;
  }
   
  declare module 'element-plus/dist/locale/zh-cn.mjs';
  declare module '@kangc/v-md-editor/lib/preview';
  declare module '@kangc/v-md-editor/lib/theme/github.js';
  declare module '@kangc/v-md-editor/lib/plugins/copy-code/index';
  declare module 'crypto-js';
  declare module 'base-64';
   declare module 'vanta/src/vanta.fog';
   declare module 'vanta/dist/vanta.waves.min';
   declare module 'three';
   declare module 'vanta/dist/vanta.fog.min';