import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import '../index.css'
import router from './router'
import Antd from 'ant-design-vue';
import "@mobileaction/action-kit/dist/style.css"
import 'ant-design-vue/dist/reset.css';
import {createPinia} from "pinia";

const app = createApp(App)
const pinia = createPinia();

app.use(router)

app.use(Antd)

app.use(pinia); // Ensure this line is present

app.mount('#app')
