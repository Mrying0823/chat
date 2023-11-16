package org.mrying.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.mrying.chat.model.Note;
import org.mrying.chat.utils.SecurityContextHolderUtils;
import org.mrying.chat.view.RespResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/11/15 21:32
 */
@Api(tags = "笔记相关")
@RequestMapping("/v1")
@RestController
public class NoteController extends BaseController {
    // 新建笔记专题
    @ApiOperation(value = "新建笔记",notes = "新建笔记")
    @ApiImplicitParam(name = "categoryId",value = "笔记专题 Id",required = true)
    @PostMapping("/note/createNewNote")
    public RespResult createNewNote(@RequestParam("categoryId") String categoryId) {
        RespResult respResult = RespResult.fail();

        Note note = new Note();

        note.setCategoryId(categoryId);

        String noteId = noteService.createNewNote(note);

        if (categoryId != null) {
            respResult = RespResult.ok();
            respResult.setNoteId(noteId);
        }else {
            respResult.setMsg("服务繁忙，请稍后重试");
        }

        return respResult;
    }

    // 获取笔记专题下的所有笔记
    @ApiOperation(value = "获取笔记专题下的所有笔记",notes = "获取用户选择的笔记专题下所有笔记")
    @ApiImplicitParam(name = "categoryId",value = "笔记专题 Id",required = true)
    @GetMapping("/note/getNote")
    public RespResult getNote(@RequestParam("categoryId") String categoryId) {
        RespResult respResult = RespResult.fail();

        List<Note> noteList = null;

        if(SecurityContextHolderUtils.getUserId() != null) {
            noteList = noteService.queryNoteByCategoryId(categoryId);
        }

        if(noteList == null) {
            respResult.setMsg("还没有创建笔记");
        }

        respResult = RespResult.ok();
        respResult.setList(noteList);

        return respResult;
    }

    // 删除笔记
    @ApiOperation(value = "删除笔记",notes = "根据笔记 id 删除笔记")
    @ApiImplicitParam(name = "noteId",value = "笔记 Id")
    @PostMapping("/note/deleteNote")
    public RespResult deleteNote(@RequestParam("noteId") String noteId) {
        RespResult respResult = RespResult.fail();

        int result = noteService.deleteNoteById(noteId);

        if(result > 0) {
            respResult = RespResult.ok();
        }else {
            respResult.setMsg("服务繁忙，请稍后重试");
        }

        return respResult;
    }

    // 修改笔记标题
    @ApiOperation(value = "修改笔记标题",notes = "根据 noteId 修改笔记标题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "noteId",value = "笔记 Id",required = true),
            @ApiImplicitParam(name = "noteTitle",value = "笔记标题",required = true)
    })
    @PostMapping("/note/editNoteTitle")
    public RespResult editConversationName(@RequestParam("noteId") String noteId,
                                           @RequestParam("noteTitle") String noteTitle) {
        RespResult respResult = RespResult.fail();

        int result = noteService.editNoteTitleById(noteId,noteTitle);

        if(result > 0) {
            respResult = RespResult.ok();
        }else {
            respResult.setMsg("服务繁忙，请稍后重试");
        }

        return respResult;
    }
}
