package org.mrying.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.mrying.chat.view.RespResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

/**
 * @author 邓和颖
 * @since 2023/10/14 16:27
 */

@CrossOrigin(value = {"http://localhost:8080/gpt/chatGPT", "http://localhost:8080/note/userNote"})
@Api(tags = "ChatGPT 请求相关")
@RequestMapping("/v1")
@RestController
public class ChatgptController extends BaseController {

    // chatgpt 流式对话配合 Spring SseEmitter 使用
    // SSE（Server-Sent Event）是一种实时通知、事件推送和即时通讯的协议
    @ApiOperation(value = "chatgpt 流式对话",notes = "chatgpt 流式对话配合 Spring SseEmitter")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usePublicApi", value = "使用公用 Api-Key", required = true),
            @ApiImplicitParam(name = "conversationId", value = "会话 ID"),
            @ApiImplicitParam(name = "prompt", value = "对话内容", required = true)
    })
    @GetMapping(path = "/chatgpt/chat",produces="text/event-stream;charset=utf-8")
    public SseEmitter chat(@RequestParam("usePublicApi") Boolean usePublicApi,
                           @RequestParam("conversationId") String conversationId,
                           @RequestParam("prompt") String prompt) {

        return chatgptService.sendMsgBySse(usePublicApi,conversationId,prompt);
    }

    // chatgpt 流式对话配合 Spring SseEmitter 使用
    @ApiOperation(value = "chatgpt 流式对话，笔记用",notes = "chatgpt 流式对话配合 Spring SseEmitter，笔记用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usePublicApi", value = "使用公用 Api-Key", required = true),
            @ApiImplicitParam(name = "prompt", value = "提问", required = true)
    })
    @GetMapping(path = "/chatgpt/chatForNote",produces="text/event-stream;charset=utf-8")
    public SseEmitter chatForNote(@RequestParam("usePublicApi") Boolean usePublicApi,
                           @RequestParam("prompt") String prompt) {

        return chatgptService.sendMsgBySseForNote(usePublicApi,prompt);
    }

    @ApiOperation(value = "chatgpt 流式对话",notes = "chatgpt 流式对话配合 Spring SseEmitter")
    @PostMapping(path = "/chatgpt/chatPost",produces="text/event-stream;charset=utf-8")
    public SseEmitter chatPost(@RequestBody Map<String,String> map) {

        Boolean usePublicApi = "true".equals(map.get("usePublicApi"));
        String conversationId = map.get("conversationId");
        String prompt = map.get("prompt");

        return chatgptService.sendMsgBySse(usePublicApi,conversationId,prompt);
    }

    // 根据用户给的提示，chatgpt 生成 5 条相关问题
    @ApiOperation(value = "chatgpt 生成问题",notes = "根据用户给的提示，chatgpt 生成 10 条相关问题")
    @ApiImplicitParam(name = "prompt", value = "提示", required = true)
    @PostMapping(path = "/chatgpt/chatList")
    public RespResult chatList(@RequestParam("prompt") String prompt) {

        RespResult respResult = RespResult.fail();

        List<String> chatList = chatgptService.queryQuestionByChatgpt(prompt);

        if(chatList != null) {
            respResult = RespResult.ok();
            respResult.setList(chatList);
        }

        return respResult;
    }

    // 根据用户给的提示，chatgpt 生成 5 条更专业的问题
    @ApiOperation(value = "chatgpt 生成专业问题",notes = "根据用户给的提示，chatgpt 生成 10 条更专业的问题")
    @ApiImplicitParam(name = "prompt", value = "提示", required = true)
    @PostMapping(path = "/chatgpt/refreshChatList")
    public RespResult refreshChatList(@RequestParam("prompt") String prompt) {

        RespResult respResult = RespResult.fail();

        List<String> chatList = chatgptService.refreshQuestionByChatgpt(prompt);

        if(chatList != null) {
            respResult = RespResult.ok();
            respResult.setList(chatList);
        }

        return respResult;
    }
}
