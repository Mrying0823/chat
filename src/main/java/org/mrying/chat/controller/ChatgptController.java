package org.mrying.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

/**
 * @author 邓和颖
 * @since 2023/10/14 16:27
 */

@CrossOrigin(value = "http://localhost:8080/gpt/chatGPT")
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

    @ApiOperation(value = "chatgpt 流式对话",notes = "chatgpt 流式对话配合 Spring SseEmitter")
    @PostMapping(path = "/chatgpt/chatPost",produces="text/event-stream;charset=utf-8")
    public SseEmitter chatPost(@RequestBody Map<String,String> map) {

        Boolean usePublicApi = "true".equals(map.get("usePublicApi"));
        String conversationId = map.get("conversationId");
        String prompt = map.get("prompt");

        return chatgptService.sendMsgBySse(usePublicApi,conversationId,prompt);
    }
}
