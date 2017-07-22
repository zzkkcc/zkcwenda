package com.zkc.async.handler;

import com.zkc.async.EventHandler;
import com.zkc.async.EventModel;
import com.zkc.async.EventType;
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
 * Created by zkc on 17/7/22.
 */
@Component
public class LikeHandler implements EventHandler{
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        message.setContent("User " + user.getName() + " thumbs up" +
                " to your comments, http://127.0.0.1:8080/question " + model.getExt("questionId"));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
