package com.ljf.ploughthewaves.interfaces;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
    @RequestMapping("/login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/register")
    public String toRegister() {
        return "register";
    }

    @RequestMapping("/stupool")
    public String toStuPool() {
        return "stupool";
    }

    @RequestMapping("/myinfo")
    public String toInfo() {
        return "userinfo";
    }

    @RequestMapping("/modifypwd")
    public String toModifyPwd() {
        return "modifypwd";
    }
}