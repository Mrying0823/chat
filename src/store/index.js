import { createStore } from 'vuex';
import createPersistedState from "vuex-persistedstate";

const store = createStore({
    state: {
        user: {
            name: "",
            phone: "",
            id: ""
        },
        // 上一次选择的会话
        lastSelectedConversation: {
            selectedConversationIndex: "",
            messageList: []
        },
        // 会话过期时间
        sessionExpiryTime: null,
        // 夜间模式
        darkMode: false

    },
    getters: {
        getUser: (state) => state.user,
        getLastSelectedConversation: (state) => state.lastSelectedConversation,
        getDarkMode: (state) => state.darkMode
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
        },
        updateDarkMode(state, darkMode) {
            state.darkMode = darkMode;
        }
    },
    actions: {
        asyncUpdateUser: (context, user) => {
            context.commit('updateUser', user);

            // 设置会话过期时间
            const expiryTime = Date.now() + 60 * 60 * 1000 * 24 * 7;
            context.commit('updateSessionExpiryTime', expiryTime);
        },
        asyncLastSelectedConversation: (context, lastSelectedConversation) => {
            context.commit("updateLastSelectedConversation", lastSelectedConversation);
        },
        // 创建一个定时器来检查会话是否过期
        startSessionExpiryTimer(context) {
            setInterval(() => {
                const currentTime = Date.now();
                if (context.state.sessionExpiryTime && currentTime >= context.state.sessionExpiryTime) {
                    // 会话过期，清除数据
                    context.commit('updateUser', {
                        name: "",
                        phone: "",
                        id: ""
                    });
                    context.commit('updateSessionExpiryTime', null);
                    context.commit("updateLastSelectedConversation",{
                        selectedConversationIndex: "",
                        messageList: []
                    });
                }
            }, 10000);
        },
        asyncClearUser(context) {
            context.commit('updateUser', {
                name: "",
                phone: "",
                id: ""
            });
            context.commit('updateSessionExpiryTime', null);
            context.commit("updateLastSelectedConversation",{
                selectedConversationIndex: "",
                messageList: []
            });
            context.commit('updateDarkMode', false);
        }
    },
    asyncDarkMode: (context, darkMode) => {
        context.commit('updateDarkMode', darkMode);
    },
    plugins: [createPersistedState()]
});

export default store;