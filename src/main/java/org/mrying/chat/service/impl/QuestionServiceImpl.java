package org.mrying.chat.service.impl;

import org.mrying.chat.mapper.QuestionMapper;
import org.mrying.chat.model.Question;
import org.mrying.chat.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 邓和颖
 * @since 2023/12/11 12:04
 */
@Service("questionService")
public class QuestionServiceImpl implements QuestionService {

    private QuestionMapper questionMapper;

    @Autowired
    public void setQuestionMapper(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    @Override
    public List<Question> queryQuestionByConditionForPage(Map<String, Object> map) {
        return questionMapper.selectQuestionByConditionForPage(map);
    }

    @Override
    public int queryCountOfQuestionByCondition(Map<String, Object> map) {
        return questionMapper.selectCountOfQuestionByCondition(map);
    }
}
