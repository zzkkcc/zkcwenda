package com.zkc.controller;

import com.zkc.async.EventModel;
import com.zkc.async.EventProducer;
import com.zkc.async.EventType;
import com.zkc.model.Comment;
import com.zkc.model.EntityType;
import com.zkc.model.HostHolder;
import com.zkc.service.CommentService;
import com.zkc.service.QuestionService;
import com.zkc.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by zkc on 17/7/17.
 */
@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path={"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            commentService.addComment(comment);

            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(), count);

            eventProducer.fileEvent(new EventModel(EventType.COMMENT).setActorId(comment.getUserId())
            .setEntityId(questionId));

        }catch (Exception e){
            logger.error("failed to add comment" + e.getMessage());
        }
        return "redirect:/question/"+questionId;
    }
}
