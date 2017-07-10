package com.zkc.controller;

import com.zkc.service.WendaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Enumeration;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import com.zkc.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Created by zkc on 17/6/5.
 */
@Controller
public class IndexController {

    WendaService wendaService = new WendaService();
    /*@RequestMapping(path={"/","/index"})
    @ResponseBody
    public String index()
    {
        //model.addAttribute("User" new User("LEEE"))
        return "Hello zkc";
    }*/

    @RequestMapping(path={"/profile/{group}/{userID}"})
    @ResponseBody
    public String profile(@PathVariable("userID") int userID,
                          @PathVariable("group") String group,
                          @RequestParam(value = "key", defaultValue = "zzkkcc", required = false) String key){
        return String.format("Profile Page of %s / %d   k:%s", group, userID, key);
    }

    @RequestMapping(path={"/vm"}, method = {RequestMethod.GET})
    public String template(Model model){
        model.addAttribute("value1", "vvvv1");
        List<String> colors = Arrays.asList(new String[]{"RED","GREEN","BLUE"});
        model.addAttribute("colors", colors);

        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < 4; i++){
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);
        model.addAttribute("user",new User("LEE"));
        return "home";
    }
    @RequestMapping(path={"/request"}, method = {RequestMethod.GET})
    @ResponseBody
    public String template(Model model, HttpServletResponse response,
                           HttpServletRequest request, HttpSession httpSession){
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headNames = request.getHeaderNames();
        while(headNames.hasMoreElements()){
            String name = headNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                sb.append("Cookies:" + cookie.getName() + "  value:" + cookie.getValue());
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");
        return sb.toString();
    }
}
