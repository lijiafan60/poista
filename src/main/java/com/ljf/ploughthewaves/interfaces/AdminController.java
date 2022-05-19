package com.ljf.ploughthewaves.interfaces;

import com.ljf.ploughthewaves.domain.admin.model.vo.StuInfo;
import com.ljf.ploughthewaves.domain.admin.service.AdminService;
import com.ljf.ploughthewaves.domain.admin.service.UserService;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    public AdminService adminService;

    @Resource
    public UserService userService;

    /**
     * 管理员获取本校学生信息
     * @param openid
     * @return
     */
    @PostMapping("/getStuInfo")
    public List<StuInfo> getStuInfo(@RequestParam String openid) {
        return adminService.getStuInfo(openid);
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

    /**
     * 获取excel
     * @param id
     * @param school
     * @return
     */
    @PostMapping("/getExcel/{id}/{school}")
    public String getExcel(@PathVariable String id, @PathVariable String school) {
        return null;
    }


    /**
     * 更新校内学生统计信息
     * @param openid
     * @return
     */
    @PostMapping("/updateStuStatisticsInfo/{openid}")
    public String updateStuStatisticsInfo(@PathVariable String openid) {
        adminService.updStuStatisticsInfo(openid);
        return null;
    }
}
