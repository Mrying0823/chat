import { createRouter, createWebHistory } from 'vue-router'
import GPTWindow from "@/views/GPTWindow";
import ChatGPT from "@/views/ChatGPT";
import ChatGPTPost from "@/views/ChatGPTPost";
import GPTWindow3 from "@/views/GPTWindow3";
import HomePage from "@/views/HomePage";
import LoginView from "@/views/LoginView";

const routes = [
  {
    path: "/gpt/gptWindow",
    name: "GPTWindow",
    component: GPTWindow
  },
  {
    path: "/gpt/chatGPT",
    name: "ChatGPT",
    component: ChatGPT
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
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
