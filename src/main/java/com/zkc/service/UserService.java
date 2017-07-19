package com.zkc.service;

import com.zkc.dao.LoginTicketDAO;
import com.zkc.dao.UserDAO;
import com.zkc.model.LoginTicket;
import com.zkc.model.User;
import com.zkc.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zkc on 17/7/6.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User selectByName(String name){
        return userDAO.selectByName(name);
    }
    public Map<String, String> register(String username, String password){
        Map<String, String> map = new HashMap<String, String>();
        if(StringUtils.isBlank(username)){
            map.put("msg","username cannot be empty");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg", "password cannot be empty");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msg", "username has been used");
            return map;
        }
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);
        return map;
    }
    public Map<String, String> login(String username, String password){
        Map<String, String> map = new HashMap<String, String>();
        if(StringUtils.isBlank(username)){
            map.put("msg","username cannot be empty");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg", "password cannot be empty");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user == null){
            map.put("msg", "username does not exist");
            return map;
        }
        if(!WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msg", "Wrong Password");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }
    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(3600*24*30 + now.getTime());
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket, 1);
    }
    public User getUser(int id){
        return userDAO.selectById(id);
    }
}
