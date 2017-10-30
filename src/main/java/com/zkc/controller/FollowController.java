package com.zkc.controller;

import com.zkc.async.EventModel;
import com.zkc.async.EventProducer;
import com.zkc.async.EventType;
import com.zkc.model.*;
import com.zkc.service.CommentService;
import com.zkc.service.FollowService;
import com.zkc.service.QuestionService;
import com.zkc.service.UserService;
import com.zkc.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zkc on 17/7/25.
 */
@Controller
public class FollowController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;

    @RequestMapping(path={"/followUser"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String follow(@RequestParam("userId") int userId){
        if(hostHolder.getUser() == null){
            return WendaUtil.getJSONString(999);
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);

        eventProducer.fileEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
                .setEntityId(userId).setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
        return WendaUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser()
                .getId(), EntityType.ENTITY_USER)));
    }
    @RequestMapping(path={"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollow(@RequestParam("userId") int userId){
        if(hostHolder.getUser() == null){
            return WendaUtil.getJSONString(999);
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);

        eventProducer.fileEvent(new EventModel(EventType.UNFOLLOW).setActorId(hostHolder.getUser().getId())
                .setEntityId(userId).setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
        return WendaUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser()
                .getId(), EntityType.ENTITY_USER)));
    }
    @RequestMapping(path={"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser() == null){
            return WendaUtil.getJSONString(999);
        }
        Question q = questionService.getById(questionId);
        if(q == null){
            return WendaUtil.getJSONString(1,"question does not exist");
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);

        eventProducer.fileEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(q.getUserId()));

        Map<String, Object> info = new HashMap<String, Object>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return WendaUtil.getJSONString(ret ? 0 : 1, info);
    }

    @RequestMapping(path={"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser() == null){
            return WendaUtil.getJSONString(999);
        }
        Question q = questionService.getById(questionId);
        if(q == null){
            return WendaUtil.getJSONString(1,"question does not exist");
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);

        eventProducer.fileEvent(new EventModel(EventType.UNFOLLOW).setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(q.getUserId()));

        Map<String, Object> info = new HashMap<String, Object>();
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return WendaUtil.getJSONString(ret ? 0 : 1, info);
    }

    @RequestMapping(path={"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId){
        List<Integer>  followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);
        if(hostHolder.getUser() != null){
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        }else{
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }

    @RequestMapping(path={"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId){
        List<Integer>  followerIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);
        if(hostHolder.getUser() != null){
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        }else {
            model.addAttribute("followers",getUsersInfo(0,followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }
    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds){
        List<ViewObject> userInfos = new ArrayList<>();
        for(Integer uid: userIds){
            User user = userService.getUser(uid);
            if(user == null){
                continue;
            }
            ViewObject vo= new ViewObject();
            vo.set("user",user);
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, uid));
            if(localUserId != 0){
                vo.set("followed",followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            }else {
                vo.set("followed",false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
