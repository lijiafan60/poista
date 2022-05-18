package com.ljf.ploughthewaves.interfaces;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    /**
     * 管理员获取本校学生信息
     * @param id
     * @param school
     * @return
     */
    @GetMapping("/getStuInfo")
    public String getStuInfo(@RequestParam("id") String id,@RequestParam("school") String school) {
       //todo
        return null;
    }

    /**
     * 设置统计策略
     * @param id
     * @param school
     * @return
     */
    @PostMapping("/setStatisticsStrategy/{id}/{school}")
    public String setStatisticsStrategy(@PathVariable String id, @PathVariable String school) {
        return null;
    }

    @PostMapping("/getExcel/{id}/{school}")
    public String getExcel(@PathVariable String id, @PathVariable String school) {
        return null;
    }
}
