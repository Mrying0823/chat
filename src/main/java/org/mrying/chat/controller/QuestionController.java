package org.mrying.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mrying.chat.model.DicValue;
import org.mrying.chat.view.RespResult;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping(path = "/questionList/publicQuestionList")
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
        }

        return respResult;
    }
}
