package org.mrying.chat.controller;

import org.mrying.chat.service.ChatgptMessageService;
import org.mrying.chat.service.ChatgptService;
import org.mrying.chat.service.ConversationService;

import javax.annotation.Resource;

/**
 * @author 邓和颖
 * @since 2023/10/14 17:36
 */
public class BaseController {

    @Resource
    protected ConversationService conversationService;

    @Resource
    protected ChatgptService chatgptService;

    @Resource
    protected ChatgptMessageService chatgptMessageService;
}
