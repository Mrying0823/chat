import { createRouter, createWebHistory } from 'vue-router'
import GPTWindow from "@/views/GPTWindow";
import ChatGPT from "@/views/ChatGPT";
import GPTWindow3 from "@/views/GPTWindow3";

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
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
