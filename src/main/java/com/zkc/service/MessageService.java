package com.zkc.service;

import com.zkc.dao.MessageDAO;
import com.zkc.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zkc on 17/7/18.
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;
    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message){
        message.setContent(sensitiveService.fitler(message.getContent()));
        return messageDAO.addMessage(message) > 0 ? message.getId():0;
    }
    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }
    public List<Message> getConversationList(int userId, int offset, int limit) {
        return  messageDAO.getConversationList(userId, offset, limit);
    }
    public int getConversationCount(int useId, String conversationId){
        return messageDAO.getConversationCount(useId, conversationId);
    }
}
