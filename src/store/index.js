import { createStore } from 'vuex';

const store = createStore({
    state: {
        user: {
            name: ''
        }
    },
    getters: {
        getUser: (state) => state.user
    },
    mutations: {
        updateUser: (state, user) => {
            state.user = user;
        }
    },
    actions: {
        asyncUpdateUser: (context, user) => {
            context.commit('updateUser', user);
        }
    }
});

export default store;
