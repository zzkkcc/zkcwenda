package com.zkc.service;

import com.zkc.dao.QuestionDAO;
import com.zkc.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zkc on 17/7/6.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }
}
