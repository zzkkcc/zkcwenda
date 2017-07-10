package com.zkc.controller;

import com.zkc.model.Question;
import com.zkc.service.QuestionService;
import com.zkc.service.UserService;
import com.zkc.service.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zkc on 17/7/6.
 */
public class HomeController {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(HomeController.class);
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;

    @RequestMapping(path={"/","/index"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index(Model model)
    {
        List<Question> questionList = questionService.getLatestQuestions(0,0,10);
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for(Question question : questionList){
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
        }
        model.addAttribute("vos",vos);
        return "index";
    }
}
