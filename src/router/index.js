import { createRouter, createWebHistory } from 'vue-router'
import GPTWindow from "@/views/GPTWindow";
import ChatGPT from "@/views/ChatGPT";
import ChatGPTPost from "@/views/ChatGPTPost";
import GPTWindow3 from "@/views/GPTWindow3";
import HomePage from "@/views/HomePage";
import LoginView from "@/views/LoginView";
import store from "@/store";
import NoteView from "@/views/NoteView";
import WikiView from "@/views/WikiView";

const routes = [
  {
    path: "/gpt/gptWindow",
    name: "GPTWindow",
    component: GPTWindow
  },
  {
    path: "/gpt/chatGPT",
    name: "ChatGPT",
    component: ChatGPT,
    meta: {
      requireAuth: true
    }
  },
  {
    path: "/gpt/gptWindow3",
    name: "GPTWindow3",
    component: GPTWindow3
  },
  {
    path: "/gpt/chatGPTPost",
    name: "ChatGPTPost",
    component: ChatGPTPost
  },
  {
    path: "/",
    name: "HomePage",
    component: HomePage
  },
  {
    path: "/user/login",
    name: "LoginView",
    component: LoginView
  },
  {
    path: "/note/userNote",
    name: "NoteView",
    component: NoteView,
    meta: {
      requireAuth: true
    }
  },
  {
    path: "/wiki/wikiView",
    name: "WikiView",
    component: WikiView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const phone = store.state.user.phone;

  // 如果路由需要登录且用户未登录，重定向到登录页面
  if(to.matched.some(record => record.meta.requireAuth) && !phone) {
    next('/user/login');
  }else {
    next();
  }
});

export default router
