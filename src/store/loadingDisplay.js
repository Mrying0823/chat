import {defineStore} from 'pinia'

export const useLoadingDisplay = defineStore('loadingDisplay', {
    state: () => {
        return {
            // 是否展示加载界面
            display: false
        }
    },
})
