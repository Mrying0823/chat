import {defineStore} from 'pinia'

export const useQuestionData = defineStore('questionData', {
    state: () => {
        return {
            // 专题
            subject: "",
            // 关键词
            keyword: "",
            // 难度
            difficulty: "",
            // 查看详细
            detailDialog: false,
            // 查看问题
            question: "",
            // 内容
            content: ""
        }
    },
})