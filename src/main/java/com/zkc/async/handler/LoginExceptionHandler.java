package com.zkc.async.handler;

import com.zkc.async.EventHandler;
import com.zkc.async.EventModel;
import com.zkc.async.EventType;
import com.zkc.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zkc on 17/7/22.
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        //if login expection occurs
        Map<String, Object> map =  new HashMap<String, Object>();
        map.put("username",model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"),"login IP exception",
                "mails/login_exception.html",map);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
