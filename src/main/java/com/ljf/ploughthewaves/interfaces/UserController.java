package com.ljf.ploughthewaves.interfaces;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/user")
public class UserController {

    /**
     * 普通用户获取刷题数
     * @param ojName
     * @param handle
     * @return
     */
    @GetMapping("/getSolvedNumber")
    public String getSolvedNumber(@RequestParam("ojName") String ojName,@RequestParam("handle") String handle) {
        return null;
    }
}
