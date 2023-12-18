package org.mrying.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.mrying.chat.model.DicValue;
import org.mrying.chat.model.Question;
import org.mrying.chat.utils.SecurityContextHolderUtils;
import org.mrying.chat.utils.UUIDUtils;
import org.mrying.chat.view.RespResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 邓和颖
 * @since 2023/12/10 20:06
 */
@Api(tags = "问题列表相关")
@RequestMapping("/v1")
@RestController
public class QuestionController extends BaseController {

    // 查询动态数据
    @ApiOperation(value = "查询动态数据",notes = "查询字典表中的专题、难度、阶段")
    @GetMapping(path = "/questionList/forPublicQuestionList")
    public RespResult querySubjectAndDifficulty() {
        RespResult respResult = RespResult.fail();
        Map<String,Object> map = new HashMap<>();

        List<DicValue> subjectList = dicValueService.queryDicValueByTypeCodeAndCreatedBy("subject","admin");
        List<DicValue> difficultyList = dicValueService.queryDicValueByTypeCodeAndCreatedBy("difficulty","admin");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCodeAndCreatedBy("stage","admin");

        if(subjectList != null && difficultyList != null) {
            respResult = RespResult.ok();

            map.put("subjectList",subjectList);
            map.put("difficultyList",difficultyList);
            map.put("stageList",stageList);

            respResult.setMap(map);
        }else {
            respResult.setMsg("服务繁忙，请稍后重试");
        }

        return respResult;
    }

    // 根据查询条件分页查询问题列表
    @ApiOperation(value = "查询问题列表",notes = "根据专题、难度、问题关键词，分页查询问题列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subject", value = "专题"),
            @ApiImplicitParam(name = "keyword", value = "关键词"),
            @ApiImplicitParam(name = "difficulty", value = "难度"),
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", required = true)
    })
    @PostMapping(path = "/questionList/queryQuestionList")
    public RespResult queryQuestionList(String subject, String keyword, String difficulty,
                                        int pageNo, int pageSize) {
        RespResult respResult = RespResult.fail();

        Map<String,Object> map = new HashMap<>();
        map.put("subject",subject);
        map.put("keyword",keyword);
        map.put("difficulty",difficulty);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("userId","admin");

        return getRespResult(respResult, map);
    }

    // 新建问题
    @ApiOperation(value = "新建问题",notes = "用户创建新问题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "question", value = "问题", required = true),
            @ApiImplicitParam(name = "stage", value = "阶段"),
            @ApiImplicitParam(name = "subject", value = "专题"),
            @ApiImplicitParam(name = "difficulty", value = "难度"),
            @ApiImplicitParam(name = "content", value = "内容", required = true)
    })
    @PostMapping(path = "/questionList/saveCreateQuestion")
    public RespResult saveCreateQuestion(String question, String stage, String subject, String difficulty, String content) {
        RespResult respResult = RespResult.fail();

        Question newQuestion = new Question();

        newQuestion.setId(UUIDUtils.getUUID());
        newQuestion.setQuestion(question);
        newQuestion.setStage(stage);
        newQuestion.setSubject(subject);
        newQuestion.setDifficulty(difficulty);
        newQuestion.setContent(content);
        newQuestion.setCreatedAt(new Date());
        newQuestion.setCreatedBy(SecurityContextHolderUtils.getUserId());

        int result = questionService.saveUserCreateQuestion(newQuestion);

        if(result > 0) {
            respResult = RespResult.ok();
        }else {
            respResult.setMsg("服务繁忙，请稍后重试");
        }

        return respResult;
    }

    // 根据查询条件分页查询问题列表
    @ApiOperation(value = "查询用户问题列表",notes = "根据专题、难度、问题关键词、创建日期、修改日期，分页查询问题列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subject", value = "专题"),
            @ApiImplicitParam(name = "keyword", value = "关键词"),
            @ApiImplicitParam(name = "difficulty", value = "难度"),
            @ApiImplicitParam(name = "createdAt", value = "创建时间"),
            @ApiImplicitParam(name = "updatedAt", value = "修改时间"),
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", required = true)
    })
    @PostMapping(path = "/questionList/queryUserQuestionList")
    public RespResult queryUserQuestionList(String subject, String keyword, String difficulty, String createdAt, String updatedAt,
                                        int pageNo, int pageSize) {
        RespResult respResult = RespResult.fail();

        Map<String,Object> map = new HashMap<>();
        map.put("subject",subject);
        map.put("keyword",keyword);
        map.put("difficulty",difficulty);
        map.put("createdAt",createdAt);
        map.put("updatedAt",updatedAt);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("userId",SecurityContextHolderUtils.getUserId());

        return getRespResult(respResult, map);
    }

    @NotNull
    private RespResult getRespResult(RespResult respResult, Map<String, Object> map) {
        List<Question> questionList = questionService.queryQuestionByConditionForPage(map);

        int totalRows = questionService.queryCountOfQuestionByCondition(map);

        if(questionList != null && totalRows >= 0) {
            respResult = RespResult.ok();

            Map<String,Object> retMap = new HashMap<>();

            retMap.put("questionList",questionList);
            retMap.put("totalRows",totalRows);

            respResult.setMap(retMap);
        }else {
            respResult.setMsg("服务繁忙，请稍后重试");
        }

        return respResult;
    }
}
