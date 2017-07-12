package com.zkc.controller;

import com.zkc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zkc on 17/7/12.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    UserService userService;
    @RequestMapping(path={"/reg"}, method = {RequestMethod.POST})
    public String index(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password) {
        try {
            Map<String, String> map = userService.register(username, password);
            if (map.containsKey("msg")) {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
            return "redirect:/";
        }catch (Exception e){
            logger.error("error registration" + e.getMessage());
            return "login";
        }
    }
    @RequestMapping(path={"/relogin"}, method = {RequestMethod.GET})
    public String index(Model model) {
        return "login";
    }
}
