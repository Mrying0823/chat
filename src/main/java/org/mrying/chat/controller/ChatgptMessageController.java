package org.mrying.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.mrying.chat.model.ChatgptMessage;
import org.mrying.chat.view.RespResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/10/29 21:10
 */
@Api(tags = "ChatGPT 对话消息相关")
@RequestMapping("/v1")
@RestController
public class ChatgptMessageController extends BaseController {

    // 根据 conversationId 查询某个会话的所有对话
    @ApiOperation(value = "查询 chatgpt 对话信息",notes = "根据 conversationId 查询某个会话的所有对话")
    @ApiImplicitParam(name = "conversationId",value = "会话 Id",required = true)
    @PostMapping("/chatgpt/getMessages")
    public RespResult queryMessagesByConversationId(@RequestParam("conversationId") String conversationId) {
        RespResult respResult = RespResult.fail();

        List<ChatgptMessage> messageList = chatgptMessageService.queryAllMessagesByConversationId(conversationId);

        if (!messageList.isEmpty()) {
            respResult = RespResult.ok();
            respResult.setList(messageList);
        }

        return respResult;
    }
}
