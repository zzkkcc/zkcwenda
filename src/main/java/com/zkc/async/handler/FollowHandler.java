package com.zkc.async.handler;

import com.zkc.async.EventHandler;
import com.zkc.async.EventModel;
import com.zkc.async.EventType;
import com.zkc.model.EntityType;
import com.zkc.model.Message;
import com.zkc.model.User;
import com.zkc.service.MessageService;
import com.zkc.service.UserService;
import com.zkc.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by zkc on 17/7/25.
 */
@Component
public class FollowHandler implements EventHandler{
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        if(model.getEntityType() == EntityType.ENTITY_QUESTION) {
            message.setContent("User " + user.getName() + " pay attention" +
                    " to your questions, http://127.0.0.1:8080/question/" + model.getEntityId());
        }else if(model.getEntityType() == EntityType.ENTITY_USER){
            message.setContent("User " + user.getName() + " pay attention" +
                    " to you, http://127.0.0.1:8080/user/" + model.getActorId());
        }
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
