import {defineStore} from 'pinia'

export const useQuestionData = defineStore('questionData', {
    state: () => {
        return {
            // 当前页码
            pageNo: 1,
            // 当前页面大小
            pageSize: 10,
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
            content: "",
            // 创建日期
            createdDate: "",
            // 修改日期
            updatedDate: ""
        }
    },
})