import { createApp } from 'vue';
import store from "@/store/store"
import ElementPLUS from 'element-plus';
import 'element-plus/dist/index.css';
import App from './App.vue';
import router from "./router";
import mitt from 'mitt';
import Particles from 'particles.vue3'
import { library } from "@fortawesome/fontawesome-svg-core";
import { faWeixin, faQq, faWeibo, faAlipay } from "@fortawesome/free-brands-svg-icons";
import { faUser, faLock, faEnvelope } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { ElMessage } from 'element-plus'


library.add(faWeixin, faQq, faWeibo, faAlipay, faUser, faLock, faEnvelope);

const app = createApp(App)
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
//import { UserFilled } from '@element-plus/icons-vue';
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
	app.component(key, component);
}
app.config.globalProperties.emitter = mitt()
app.config.globalProperties.$message = ElMessage
app.use(Particles)
app.component("font-awesome-icon", FontAwesomeIcon)
app.use(router)
app.use(ElementPLUS)
//app.use(UserFilled)

// 将 store 实例作为插件安装
app.use(store)

router.beforeEach((to, from, next) => {
	if (to.meta.requireuid) {
		if (store.state.userID > 0) {
			if (to.meta.requirecid) {
				if (store.state.characterID > 0) { next() }
				else {
					next(false)
				}
			}
			else { next() }
		} else {
			next(false)
		}
	} else {
		next()
	}
})



//md 预览
import VMdPreview from "@kangc/v-md-editor/lib/preview";
import "@kangc/v-md-editor/lib/style/preview.css";
import githubTheme from "@kangc/v-md-editor/lib/theme/github.js";
import "@kangc/v-md-editor/lib/theme/style/github.css";

//md 复制
import createCopyCodePlugin from "@kangc/v-md-editor/lib/plugins/copy-code/index";
import "@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css";

// highlightjs
import hljs from "highlight.js";

VMdPreview.use(githubTheme, {
	Hljs: hljs,
});
VMdPreview.use(createCopyCodePlugin());
app.use(VMdPreview);
app.mount('#app');