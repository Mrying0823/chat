package org.mrying.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.mrying.chat.model.Conversation;
import org.mrying.chat.utils.SecurityContextHolderUtils;
import org.mrying.chat.view.RespResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/10/27 21:21
 */
@Api(tags = "ChatGPT 会话相关")
@RequestMapping("/v1")
@RestController
public class ConversationController extends BaseController {

    // 新建 chatgpt 对话
    @ApiOperation(value = "新建 chatgpt 会话",notes = "新建 chatgpt 会话")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "conversationType",value = "会话类型"),
            @ApiImplicitParam(name = "conversationName",value = "会话名称")
    })
    @PostMapping("/chatgpt/createConversation")
    public RespResult createConversation(@RequestParam("conversationType") Integer conversationType,
                                         @RequestParam("conversationName") String conversationName) {
        RespResult respResult = RespResult.fail();

        Conversation conversation = new Conversation();

        conversation.setConversationType(conversationType);
        conversation.setConversationName(conversationName);

        String conversationId = conversationService.createConversation(conversation);

        if (conversationId != null) {
            respResult = RespResult.ok();
            respResult.setConversationId(conversationId);
        }else {
            respResult.setMsg("服务繁忙，请稍后重试");
        }

        return respResult;
    }

    // 获取用户已创建的所有 chatgpt 会话
    @ApiOperation(value = "获取 chatgpt 会话列表",notes = "获取用户已创建的所有 chatgpt 会话")
    @GetMapping("/chatgpt/getConversation")
    public RespResult getConversation() {
        RespResult respResult = RespResult.fail();

        List<Conversation> conversationList = conversationService.queryConversationByUserId(SecurityContextHolderUtils.getUserId());

        if(conversationList.isEmpty()) {
            respResult.setMsg("开启 chatGPT 对话吧");
        }

        respResult = RespResult.ok();
        respResult.setList(conversationList);

        return respResult;
    }

    // 删除会话及会话内容
    @ApiOperation(value = "删除 chatgpt 会话及会话内容",notes = "根据会话 id 删除 chatgpt 会话及会话内容")
    @ApiImplicitParam(name = "conversationId",value = "会话 Id")
    @PostMapping("/chatgpt/deleteConversation")
    public RespResult deleteConversation(@RequestParam("conversationId") String conversationId) {
        RespResult respResult = RespResult.fail();

        int result = conversationService.deleteConversationById(conversationId);

        if(result > 0) {
            respResult = RespResult.ok();
        }else {
            respResult.setMsg("服务繁忙，请稍后重试");
        }

        return respResult;
    }

    // 修改会话名称
    @ApiOperation(value = "修改 chatgpt 会话名称",notes = "根据 conversationId 查询会话并修改名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "conversationId",value = "会话 Id",required = true),
            @ApiImplicitParam(name = "conversationName",value = "会话名称",required = true)
    })
    @PostMapping("/chatgpt/editConversationName")
    public RespResult editConversationName(@RequestParam("conversationId") String conversationId,
                                           @RequestParam("conversationName") String conversationName) {
        RespResult respResult = RespResult.fail();

        int result = conversationService.editConversationNameById(conversationId,conversationName);

        if(result > 0) {
            respResult = RespResult.ok();
        }else {
            respResult.setMsg("服务繁忙，请稍后重试");
        }

        return respResult;
    }
}
