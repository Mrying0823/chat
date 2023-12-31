package org.mrying.chat.controller;

import org.mrying.chat.service.*;
import org.mrying.chat.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * @author 邓和颖
 * @since 2023/10/14 17:36
 */
public class BaseController {

    @Resource
    protected ConversationService conversationService;

    // chatgpt 对话服务
    @Resource
    protected ChatgptService chatgptService;

    // chatgpt 对话记录服务
    @Resource
    protected ChatgptMessageService chatgptMessageService;

    // 用户服务
    @Resource
    protected UserService userService;

    // redis
    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    // JwtUtils
    @Resource
    protected JwtUtils jwtUtils;

    // 笔记专题服务
    @Resource
    protected NoteCategoryService noteCategoryService;

    // 笔记服务
    @Resource
    protected NoteService noteService;

    // 数据字典服务
    @Resource
    protected DicValueService dicValueService;

    // 问题列表服务
    @Resource
    protected QuestionService questionService;
}
