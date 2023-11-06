import { createStore } from 'vuex';
import createPersistedState from "vuex-persistedstate";

const store = createStore({
    state: {
        user: {
            name: "邓和颖",
            account: "18978955805"
        },
        // 上一次选择的会话
        lastSelectedConversation: {
            selectedConversationIndex: "",
            messageList: []
        },
        // 会话过期时间
        sessionExpiryTime: null
    },
    getters: {
        getUser: (state) => state.user,
        getLastSelectedConversation: (state) => state.lastSelectedConversation
    },
    mutations: {
        updateUser: (state, user) => {
            state.user = user;
        },
        updateLastSelectedConversation: (state, lastSelectedConversation) => {
            state.lastSelectedConversation = lastSelectedConversation;
        },
        updateSessionExpiryTime(state, expiryTime) {
            state.sessionExpiryTime = expiryTime;
        }
    },
    actions: {
        asyncUpdateUser: (context, user) => {
            context.commit('updateUser', user);
        },
        asyncLastSelectedConversation: (context, lastSelectedConversation) => {
            context.commit("updateLastSelectedConversation", lastSelectedConversation);

            // 设置会话过期时间（假设过期时间是 30 分钟后）
            const expiryTime = Date.now() + 30 * 60 * 1000;
            context.commit('updateSessionExpiryTime', expiryTime);
        },
        // 创建一个定时器来检查会话是否过期
        startSessionExpiryTimer(context) {
            setInterval(() => {
                const currentTime = Date.now();
                if (context.state.sessionExpiryTime && currentTime >= context.state.sessionExpiryTime) {
                    // 会话过期，清除数据
                    context.commit('updateUser', {
                        name: "邓和颖",
                        account: "18978955805"
                    });
                    context.commit('updateSessionExpiryTime', null);
                    context.commit("updateLastSelectedConversation",{
                        selectedConversationIndex: "",
                        messageList: []
                    });
                }
            }, 1000); // 每秒检查一次
        }
    },
    plugins: [createPersistedState()]
});

export default store;
