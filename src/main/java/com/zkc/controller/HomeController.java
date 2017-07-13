package com.zkc.controller;

import com.zkc.model.HostHolder;
import com.zkc.model.Question;
import com.zkc.service.QuestionService;
import com.zkc.service.UserService;
import com.zkc.model.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zkc on 17/7/6.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path={"/user/{userId}"}, method = {RequestMethod.GET})
    public String index(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId,0,10));
        return "index";
    }

    @RequestMapping(path={"/","/index"}, method = {RequestMethod.GET})
    public String index(Model model)
    {
        model.addAttribute("vos", getQuestions(0,0,10));
        hostHolder.getUser();
        return "index";
    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit){
        List<Question> questionList = questionService.getLatestQuestions(userId,offset,limit);
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for(Question question : questionList){
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
