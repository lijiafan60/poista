package com.ljf.ploughthewaves.interfaces;

import com.ljf.ploughthewaves.domain.admin.model.vo.StuInfo;
import com.ljf.ploughthewaves.domain.admin.repository.IUserRepository;
import com.ljf.ploughthewaves.domain.admin.service.AdminService;
import com.ljf.ploughthewaves.domain.admin.service.UserService;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.Strategy;
import com.ljf.ploughthewaves.infrastructure.po.User;
import com.ljf.ploughthewaves.infrastructure.repository.UserRepository;
import com.ljf.ploughthewaves.infrastructure.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;
    @Resource
    private IUserRepository userRepository;
    @Resource
    private RedisUtil redisUtil;
    /**
     * 管理员获取本校学生信息
     * @param name
     * @return
     */
    @PostMapping("/getStuInfo")
    public List<StuInfo> getStuInfo(@RequestParam String name) {
        if(name == null) return null;
        User user = (User) redisUtil.get(name);
        if(user == null) {
            user = userRepository.findUserByUsername(name);
            if(user == null) return null;
            redisUtil.set(name,user);
            redisUtil.set(user.getOpenId(),user);
        }
        if(user.getIsAdmin() == Boolean.FALSE) return null;
        String openid = user.getOpenId();
        if(!adminService.isAdmin(openid)) return null;
        log.info("用户({},{})正在获取本校学生信息",openid,name);
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
        if(name == null) return null;
        User user = (User) redisUtil.get(name);
        if(user == null) {
            user = userRepository.findUserByUsername(name);
            if(user == null) return null;
            redisUtil.set(name,user);
            redisUtil.set(user.getOpenId(),user);
        }
        if(user.getIsAdmin() == Boolean.FALSE) return null;
        String openid = user.getOpenId();
        String school = user.getSchool();
        log.info("{}正在设置{}学校的统计策略",openid,school);
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
        return "success";
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
        if(name == null) return null;
        User user = (User) redisUtil.get(name);
        if(user == null) {
            user = userRepository.findUserByUsername(name);
            if(user == null) return null;
            redisUtil.set(name,user);
            redisUtil.set(user.getOpenId(),user);
        }
        String openid = user.getOpenId();
        adminService.updStuStatisticsInfo(openid);
        return "success";
    }
}
