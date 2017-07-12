package com.zkc.service;

import com.zkc.dao.UserDAO;
import com.zkc.model.User;
import com.zkc.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

/**
 * Created by zkc on 17/7/6.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
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
    public User getUser(int id){
        return userDAO.selectById(id);
    }
}
