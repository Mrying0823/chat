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
			pageCategory: [
				{
					id: 1,
					label: 'JAVA',
					isFolder: true,
					children: [
						{
							id: 11,
							label: 'Java 程序员进阶之路',
							pageSrc: "/Java 程序员进阶之路.pdf",
							page: 1
						},
						{
							id: 12,
							label: "JavaGuide 面试突击最新版",
							pageSrc: "/JavaGuide 面试突击最新版.pdf",
							page: 1
						},
						{
							id: 13,
							label: "GitHub 上标星 115k+ 的 Java 教程",
							pageSrc: "/GitHub 上标星 115k+ 的 Java 教程.pdf",
							page: 1
						},
						{
							id: 14,
							label: "Java 技术手册",
							pageSrc: "/Java 技术手册.pdf",
							page: 1
						},{
							id: 15,
							label: "Java 软件开发复习提纲",
							pageSrc: "/Java 软件开发复习提纲.pdf",
							page: 1
						},
					]
				},
				{
					id: 2,
					label: '工具',
					isFolder: true,
					children: [
						{
							id: 21,
							label: 'Maven 入门指南松哥版',
							pageSrc: "/Maven 入门指南松哥版.pdf",
							page: 1
						},
						{
							id: 22,
							label: 'IntelliJ IDEA 简体中文专题教程（电子版-2015）',
							pageSrc: "/IntelliJ IDEA 简体中文专题教程（电子版-2015）.pdf",
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
							label: 'SpringMVC 入门指南松哥版',
							pageSrc: "/SpringMVC 入门指南松哥版.pdf",
							page: 1
						},
						{
							id: 32,
							label: 'MyBatis 从入门到精通',
							pageSrc: "/MyBatis 从入门到精通.pdf",
							page: 1
						},
						{
							id: 33,
							label: 'spring 技术手册',
							pageSrc: "/spring2.0技术手册.pdf",
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
							pageSrc: "/数据库系统基础教程.pdf",
							page: 1
						},
						{
							id: 42,
							label: 'SQL+Server+2008 实战',
							pageSrc: "/SQL+Server+2008 实战.pdf",
							page: 1
						}
					]
				},
				{
					id: 5,
					label: '深入浅出 MySQL',
					pageSrc: "/深入浅出MySQL.pdf",
					page: 1
				},
				{
					id: 6,
					label: 'Redis',
					pageSrc: "/Redis.pdf",
					page: 1
				},
				{
					id: 7,
					label: 'MongoDB',
					pageSrc: "/MongoDB.pdf",
					page: 1
				},
				{
					id: 8,
					label: '并发编程',
					pageSrc: "/并发编程.pdf",
					page: 1
				},
				{
					id: 9,
					label: 'JVM',
					isFolder: true,
					children: [
						{
							id: 91,
							label: '深入理解 Java 虚拟机总结',
							pageSrc: "/深入理解 Java 虚拟机总结.pdf",
							page: 1
						},
						{
							id: 92,
							label: '深入理解 Java 内存模型',
							pageSrc: "/深入理解 Java 内存模型.pdf",
							page: 1
						}
					]
				},
				{
					id: 10,
					label: '性能优化',
					pageSrc: "/嵩山版 Java 开发手册.pdf",
					page: 1
				}
			]
		}
	},
});
