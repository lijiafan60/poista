package com.ljf.ploughthewaves.interfaces;

import com.ljf.ploughthewaves.domain.admin.model.vo.StuInfo;
import com.ljf.ploughthewaves.domain.admin.service.AdminService;
import com.ljf.ploughthewaves.domain.admin.service.UserService;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.Strategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    public AdminService adminService;

    @Resource
    public UserService userService;

    @Resource
    public UserDao userDao;
    /**
     * 管理员获取本校学生信息
     * @param name
     * @return
     */
    @PostMapping("/getStuInfo")
    public List<StuInfo> getStuInfo(@RequestParam String name) {
        log.info("{}正在获取本校学生信息",name);
        log.info("正在验证{}管理员身份",name);
        String openid = userDao.queryUserByName(name).getOpenId();
        if(!adminService.isAdmin(openid)) {
            return null;
        }
        log.info("验证通过");
        return adminService.getStuInfo(openid);
    }

    /**
     * 设置统计策略
     * @param name
     * @param cfRating
     * @param cfMaxRating
     * @param cfRecentMaxRating
     * @param cfContestNumber
     * @param cfRecentContestNumber
     * @param acRating
     * @param acMaxRating
     * @param acContestNumber
     * @param allSolvedNumber
     * @return
     */
    @PostMapping("/setStatisticsStrategy/{name}")
    public String setStatisticsStrategy (@PathVariable String name,
                                        Double cfRating,Double cfMaxRating, Double cfRecentMaxRating,
                                        Double cfContestNumber, Double cfRecentContestNumber,
                                        Double acRating, Double acMaxRating, Double acContestNumber,
                                        Double allSolvedNumber) {
        String openid = userDao.queryUserByName(name).getOpenId();

        String school = userDao.queryUserByOpenid(openid).getSchool();
        log.info("{}正在设置{}的统计策略",openid,school);
        if(!userService.isAdmin(openid,school)) return "-1";
        log.info("{}用户在{}的管理员身份验证通过",openid,school);
        Strategy strategy = new Strategy();
        strategy.setSchool(school);
        strategy.setCfRating(cfRating);
        strategy.setCfMaxRating(cfMaxRating);
        strategy.setCfRecentMaxRating(cfRecentMaxRating);
        strategy.setCfContestNumber(cfContestNumber);
        strategy.setCfRecentContestNumber(cfRecentContestNumber);
        strategy.setAcRating(acRating);
        strategy.setAcMaxRating(acMaxRating);
        strategy.setAcContestNumber(acContestNumber);
        strategy.setAllSolvedNumber(allSolvedNumber);
        adminService.setStatisticsStrategy(strategy,school);
        return "1";
    }


    /**
     * 获取excel
     * @param name
     * @param school
     * @return
     */
    @PostMapping("/getExcel/{name}/{school}")
    public String getExcel(@PathVariable String name, @PathVariable String school) {
        return null;
    }


    /**
     * 更新校内学生统计信息
     * @param name
     * @return
     */
    @PostMapping("/updateStuStatisticsInfo/{name}")
    public String updateStuStatisticsInfo(@PathVariable String name) {
        String openid = userDao.queryUserByName(name).getOpenId();
        adminService.updStuStatisticsInfo(openid);
        return null;
    }
}
