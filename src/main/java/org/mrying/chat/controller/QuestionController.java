package org.mrying.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.mrying.chat.model.DicValue;
import org.mrying.chat.model.Question;
import org.mrying.chat.utils.DateConvertUtils;
import org.mrying.chat.view.RespResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation(value = "查询动态数据",notes = "查询字典表中的专题、难度")
    @GetMapping(path = "/questionList/forPublicQuestionList")
    public RespResult querySubjectAndDifficulty() {
        RespResult respResult = RespResult.fail();
        Map<String,Object> map = new HashMap<>();

        List<DicValue> subjectList = dicValueService.queryDicValueByTypeCodeAndCreatedBy("subject","admin");
        List<DicValue> difficultyList = dicValueService.queryDicValueByTypeCodeAndCreatedBy("difficulty","admin");

        if(subjectList != null && difficultyList != null) {
            respResult = RespResult.ok();

            map.put("subjectList",subjectList);
            map.put("difficultyList",difficultyList);

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

        List<Question> questionList = questionService.queryQuestionByConditionForPage(map);

        int totalRows = questionService.queryCountOfQuestionByCondition(map);

        if(questionList != null && totalRows >= 0) {
            respResult = RespResult.ok();

            Map<String,Object> retMap = new HashMap<>();

            for(Question question : questionList) {
                question.setCreatedDate(DateConvertUtils.getDateTimeString(question.getCreatedAt()));
                if(question.getUpdatedAt() != null) {
                    question.setUpdatedDate(DateConvertUtils.getDateTimeString(question.getUpdatedAt()));
                }
            }

            retMap.put("questionList",questionList);
            retMap.put("totalRows",totalRows);

            respResult.setMap(retMap);
        }else {
            respResult.setMsg("服务繁忙，请稍后重试");
        }

        return respResult;
    }
}
