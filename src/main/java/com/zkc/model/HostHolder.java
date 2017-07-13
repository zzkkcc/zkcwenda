package com.zkc.model;

import org.springframework.stereotype.Component;

/**
 * Created by zkc on 17/7/13.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();
    public User getUser(){
        return users.get();
    }
    public void setUser(User user) {
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
