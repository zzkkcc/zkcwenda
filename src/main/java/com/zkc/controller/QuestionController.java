package com.zkc.controller;

import com.zkc.model.*;
import com.zkc.service.CommentService;
import com.zkc.service.LikeService;
import com.zkc.service.QuestionService;
import com.zkc.service.UserService;
import com.zkc.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zkc on 17/7/14.
 */
@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;

    @RequestMapping(value = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content){
        try{
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if(hostHolder.getUser() == null){
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
            }else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question) > 0){
                return WendaUtil.getJSONString(0);
            }
        }catch (Exception e){
            logger.error("fail to add title" + e.getMessage());
        }
        return WendaUtil.getJSONString(1, "failed");
    }
    @RequestMapping(value = "/question/{qid}", method = {RequestMethod.GET})
    public String questionDetail(@PathVariable("qid") int qid, Model model){
        Question question = questionService.selectById(qid);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUser(question.getUserId()));

        List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<ViewObject>();
        for(Comment comment:commentList){
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            if(hostHolder.getUser() == null){
                vo.set("liked", 0);
            }else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(),
                        EntityType.ENTITY_COMMENT, comment.getId()));
            }
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);
        return "detail";
    }
}
