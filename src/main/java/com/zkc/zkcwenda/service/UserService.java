package com.zkc.zkcwenda.service;

import com.zkc.zkcwenda.dao.UserDAO;
import com.zkc.zkcwenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zkc on 17/7/6.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    public User getUser(int id){
        return userDAO.selectById(id);
    }
}
