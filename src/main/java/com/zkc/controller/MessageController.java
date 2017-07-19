package com.zkc.controller;

import com.zkc.model.*;
import com.zkc.service.MessageService;
import com.zkc.service.UserService;
import com.zkc.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zkc on 17/7/18.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @RequestMapping(path={"/msg/list"}, method = {RequestMethod.GET})
    public String getConversationList(){
        return "letter";
    }
    @RequestMapping(path={"/msg/detail"}, method = {RequestMethod.GET})
    public String getConversationDetail(Model model,
                                        @RequestParam("conversationId") String conversationId){
        try{
            List<Message> messageList = messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for(Message message: messageList){
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e){
            logger.error("failed to get details"+e.getMessage());
        }
        return "letterDetail";
    }
    @RequestMapping(path={"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam("toName") String toName,
                             @RequestParam("content") String content){
        try {
            if(hostHolder.getUser() == null){
                return WendaUtil.getJSONString(999,"未登录");
            }
            User user = userService.selectByName(toName);
            if( user == null){
                return WendaUtil.getJSONString(1,"用户不存在");
            }
            Message message = new Message();
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setContent(content);
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("failed to add comment" + e.getMessage());
            return WendaUtil.getJSONString(1,"发信失败");
        }
    }
}
