package org.mrying.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.mrying.chat.model.NoteCategory;
import org.mrying.chat.utils.SecurityContextHolderUtils;
import org.mrying.chat.view.RespResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/11/13 10:08
 */
@Api(tags = "笔记专题相关")
@RequestMapping("/v1")
@RestController
public class NoteCategoryController extends BaseController {
    // 新建笔记专题
    @ApiOperation(value = "新建笔记专题",notes = "新建笔记专题")
    @PostMapping("/note/createNoteCategory")
    public RespResult createConversation() {
        RespResult respResult = RespResult.fail();

        NoteCategory category = new NoteCategory();

        String categoryId = noteCategoryService.createNoteCategory(category);

        if (categoryId != null) {
            respResult = RespResult.ok();
            respResult.setNoteCategoryId(categoryId);
        }else {
            respResult.setMsg("服务繁忙，请稍后重试");
        }

        return respResult;
    }

    // 获取用户创建的笔记专题
    @ApiOperation(value = "获取笔记专题列表",notes = "获取用户创建的笔记专题列表")
    @GetMapping("/note/getNoteCategory")
    public RespResult getNoteCategory() {
        RespResult respResult = RespResult.fail();

        List<NoteCategory> categoryList = noteCategoryService.queryNoteCategoryByUserId(SecurityContextHolderUtils.getUserId());

        if(categoryList.isEmpty()) {
            respResult.setMsg("还没有创建笔记专题");
        }

        respResult = RespResult.ok();
        respResult.setList(categoryList);

        return respResult;
    }

    // 删除笔记专题
    @ApiOperation(value = "删除笔记专题及关联笔记",notes = "根据笔记专题 id 删除笔记专题及关联笔记")
    @ApiImplicitParam(name = "categoryId",value = "笔记专题 Id")
    @PostMapping("/note/deleteNoteCategory")
    public RespResult deleteConversation(@RequestParam("categoryId") String categoryId) {
        RespResult respResult = RespResult.fail();

        int result = noteCategoryService.deleteNoteCategoryById(categoryId);

        if(result > 0) {
            respResult = RespResult.ok();
        }else {
            respResult.setMsg("服务繁忙，请稍后重试");
        }

        return respResult;
    }

    // 修改笔记专题标题
    @ApiOperation(value = "修改笔记专题标题",notes = "根据 categoryId 修改笔记专题标题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId",value = "笔记专题 Id",required = true),
            @ApiImplicitParam(name = "categoryTitle",value = "笔记专题标题",required = true)
    })
    @PostMapping("/note/editNoteCategoryTitle")
    public RespResult editConversationName(@RequestParam("categoryId") String categoryId,
                                           @RequestParam("categoryTitle") String categoryTitle) {
        RespResult respResult = RespResult.fail();

        int result = conversationService.editConversationNameById(categoryId,categoryTitle);

        if(result > 0) {
            respResult = RespResult.ok();
        }else {
            respResult.setMsg("服务繁忙，请稍后重试");
        }

        return respResult;
    }
}
