import {defineStore} from 'pinia'

export const useStorePageData = defineStore('pageData', {
	state: () => {
		return {
			pageInfo: {},
			pageAuth: {},
			fileList: [],
			choosePageId: 0,
			optionPageId: 0,
			wikiPageList: [],
			pageIsUnLock: false,
			pageWidth: 80,
			lastPagePath: "",
			pageCategory: [
				{
					id: 1,
					label: 'JAVA',
					isFolder: true,
					children: [
						{
							id: 11,
							label: 'Java 程序员进阶之路',
							pageSrc: "/book/Java 程序员进阶之路.pdf",
							page: 1
						},
						{
							id: 12,
							label: "JavaGuide 面试突击最新版",
							pageSrc: "/book/JavaGuide 面试突击最新版.pdf",
							page: 1
						},
						{
							id: 13,
							label: "GitHub 上标星 115k+ 的 Java 教程",
							pageSrc: "/book/GitHub 上标星 115k+ 的 Java 教程.pdf",
							page: 1
						},
						{
							id: 14,
							label: "Java 技术手册",
							pageSrc: "/book/Java 技术手册.pdf",
							page: 1
						}
					]
				},
				{
					id: 2,
					label: '工具',
					isFolder: true,
					children: [
						{
							id: 21,
							label: 'IntelliJ IDEA 简体中文专题教程（电子版-2015）',
							pageSrc: "/book/IntelliJ IDEA 简体中文专题教程（电子版-2015）.pdf",
							page: 1
						}
					]
				},
				{
					id: 3,
					label: '框架',
					isFolder: true,
					children: [
						{
							id: 31,
							label: 'MyBatis 从入门到精通',
							pageSrc: "/book/《MyBatis从入门到精通》刘增辉.pdf",
							page: 1
						},
						{
							id: 32,
							label: 'spring',
							pageSrc: "/book/Spring.pdf",
							page: 1
						}
					]
				},
				{
					id: 4,
					label: '数据库',
					isFolder: true,
					children: [
						{
							id: 41,
							label: '数据库系统基础教程',
							pageSrc: "/book/数据库系统基础教程.pdf",
							page: 1
						},
						{
							id: 42,
							label: 'SQL+Server+2008 实战',
							pageSrc: "/book/SQL+Server+2008 实战.pdf",
							page: 1
						},
						{
							id: 43,
							label: '深入浅出 MySQL',
							pageSrc: "/book/深入浅出MySQL.pdf",
							page: 1
						},
						{
							id: 44,
							label: 'Redis',
							pageSrc: "/book/Redis.pdf",
							page: 1
						},
						{
							id: 45,
							label: 'MongoDB',
							pageSrc: "/book/MongoDB.pdf",
							page: 1
						}
					]
				},
				{
					id: 5,
					label: '并发编程',
					pageSrc: "/book/并发编程.pdf",
					page: 1
				},
				{
					id: 6,
					label: 'JVM',
					isFolder: true,
					children: [
						{
							id: 61,
							label: '深入理解 Java 虚拟机总结',
							pageSrc: "/book/深入理解 Java 虚拟机总结.pdf",
							page: 1
						},
						{
							id: 62,
							label: '深入理解 Java 内存模型',
							pageSrc: "/book/深入理解 Java 内存模型.pdf",
							page: 1
						}
					]
				},
				{
					id: 7,
					label: '性能优化',
					pageSrc: "/book/嵩山版 Java 开发手册.pdf",
					page: 1
				}
			]
		}
	},
});
