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
import QuestionListView from "@/views/QuestionListView";
import WikiPdf from "@/components/WikiPdf"
import {useStorePageData} from "@/store/pageData";
import {useLoadingDisplay} from "@/store/loadingDisplay";

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
    component: WikiView,
    children: [
      {
        path: "/wiki/wikiPdf",
        name: "WikiPdf",
        component: WikiPdf
      }
    ]
  },
  {
    path: "/question/questionList",
    name: "QuestionListView",
    component: QuestionListView,
    meta: {
      requireAuth: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const phone = store.state.user.phone;

  const storePage = useStorePageData();

  const loadingDisplay = useLoadingDisplay();

  // 如果路由需要登录且用户未登录，重定向到登录页面
  if(to.matched.some(record => record.meta.requireAuth) && !phone) {
    next('/user/login');
  }else {
    next();
  }

  if(to.path === "/wiki/wikiPdf") {
    storePage.lastPagePath = to.path;
  }

  if(loadingDisplay.display) {
    loadingDisplay.display = false;
  }
});

export default router
