package com.zkc.service;

import com.zkc.dao.QuestionDAO;
import com.zkc.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by zkc on 17/7/6.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    SensitiveService sensitiveService;

    public Question selectById(int id){
        return questionDAO.selectById(id);
    }
    public int addQuestion(Question question){
        //html代码过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        //敏感词过滤
        question.setContent(sensitiveService.fitler(question.getContent()));
        question.setTitle(sensitiveService.fitler(question.getTitle()));
        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }
    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }
    public int updateCommentCount(int id, int count){
        return questionDAO.updateCommentCount(id,count);
    }
}
