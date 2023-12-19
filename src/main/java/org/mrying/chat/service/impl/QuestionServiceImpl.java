package org.mrying.chat.service.impl;

import org.mrying.chat.mapper.QuestionMapper;
import org.mrying.chat.model.Question;
import org.mrying.chat.service.QuestionService;
import org.mrying.chat.utils.DateConvertUtils;
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

        List<Question> questionList = questionMapper.selectQuestionByConditionForPage(map);

        if(questionList != null) {
            for(Question question : questionList) {
                question.setCreatedDate(DateConvertUtils.getDateTimeString(question.getCreatedAt()));
                if(question.getUpdatedAt() != null) {
                    question.setUpdatedDate(DateConvertUtils.getDateTimeString(question.getUpdatedAt()));
                }
            }
        }

        return questionList;
    }

    @Override
    public int queryCountOfQuestionByCondition(Map<String, Object> map) {
        return questionMapper.selectCountOfQuestionByCondition(map);
    }

    @Override
    public int saveUserCreateQuestion(Question question) {
        return questionMapper.insertUserCreateQuestion(question);
    }

    @Override
    public int saveUserEditQuestion(Question question) {
        return questionMapper.updateUserEditQuestion(question);
    }
}
