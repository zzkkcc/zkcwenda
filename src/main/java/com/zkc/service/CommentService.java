package com.zkc.service;

import com.zkc.dao.CommentDAO;
import com.zkc.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by zkc on 17/7/17.
 */
@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;
    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentByEntity(int entityId, int entityType){
        return commentDAO.selectCommentByEntity(entityId, entityType);
    }

    public int addComment(Comment comment){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.fitler(comment.getContent()));
        return commentDAO.addComment(comment) > 0 ? comment.getId(): 0;
    }
    public int getCommentCount(int entityId, int entityType){
        return commentDAO.getCommentCount(entityId, entityType);
    }

    public boolean deleteComment(int commentId){
        return commentDAO.updateStatus(commentId, 1) > 0;
    }
    public Comment getCommentById(int id){
        return commentDAO.getCommentById(id);
    }
}
