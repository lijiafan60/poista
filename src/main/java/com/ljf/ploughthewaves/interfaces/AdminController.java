package com.ljf.ploughthewaves.interfaces;

import com.alibaba.excel.EasyExcel;
import com.ljf.ploughthewaves.domain.admin.model.vo.StuInfo;
import com.ljf.ploughthewaves.domain.admin.model.vo.StuInfoExcel;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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

        log.info("用户({},{})正在获取本校学生信息",user.getOpenId(),name);
        return adminService.getStuInfo(user.getSchool());
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
     * @return
     */
    @GetMapping("/getExcel")
    public void getExcel(@RequestParam String name, HttpServletResponse response) throws IOException {
        if(name == null) return;
        User user = (User) redisUtil.get(name);
        if(user == null) {
            user = userRepository.findUserByUsername(name);
            if(user == null) return;
            redisUtil.set(name,user);
            redisUtil.set(user.getOpenId(),user);
        }
        if(user.getIsAdmin() == Boolean.FALSE) return;
        String school = user.getSchool();
        //设置返回的数据格式
        response.setContentType("application/vnd.ms-excel");
        //设置返回的数据编码
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 和easyexcel没有关系
        String fileName = URLEncoder.encode(school+"统计数据", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

        List<StuInfoExcel> stuInfoExcelList = new ArrayList<>();
        List<StuInfo> stuInfoList = adminService.getStuInfo(school);
        int siz = stuInfoList.size();
        for(int i=0;i<siz;i++) {
            StuInfoExcel stuInfoExcel = new StuInfoExcel();
            stuInfoExcel.setIdx(i);
            stuInfoExcel.setName(stuInfoList.get(i).getName());
            stuInfoExcel.setCfName(stuInfoList.get(i).getCfName());
            stuInfoExcel.setCfRating(stuInfoList.get(i).getCfRating());
            stuInfoExcel.setCfMaxRating(stuInfoList.get(i).getCfMaxRating());
            stuInfoExcel.setCfRecentMaxRating(stuInfoList.get(i).getCfRecentMaxRating());
            stuInfoExcel.setCfContestNumber(stuInfoList.get(i).getCfContestNumber());
            stuInfoExcel.setCfRecentContestNumber(stuInfoList.get(i).getCfRecentContestNumber());
            stuInfoExcel.setAcName(stuInfoList.get(i).getAcName());
            stuInfoExcel.setAcRating(stuInfoList.get(i).getAcRating());
            stuInfoExcel.setAcMaxRating(stuInfoList.get(i).getAcMaxRating());
            stuInfoExcel.setAcContestNumber(stuInfoList.get(i).getAcContestNumber());
            stuInfoExcel.setAllSolvedNumber(stuInfoList.get(i).getAllSolvedNumber());
            stuInfoExcel.setPt(stuInfoList.get(i).getPt());
            stuInfoExcelList.add(stuInfoExcel);
        }
        EasyExcel.write(response.getOutputStream(),StuInfoExcel.class).sheet(school+"统计数据").doWrite(stuInfoExcelList);
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
