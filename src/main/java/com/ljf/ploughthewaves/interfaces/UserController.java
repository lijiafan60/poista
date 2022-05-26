package com.ljf.ploughthewaves.interfaces;

import com.ljf.ploughthewaves.domain.admin.model.vo.OjInfo;
import com.ljf.ploughthewaves.domain.admin.service.UserService;
import com.ljf.ploughthewaves.domain.poista.service.util.OjFilter;
import com.ljf.ploughthewaves.domain.poista.service.util.SolvedNumbersApi;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.User;
import com.ljf.ploughthewaves.infrastructure.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private SolvedNumbersApi solvedNumbersApi;

    @Resource
    private UserService userService;

    @Resource
    private UserDao userDao;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 匿名用户获取刷题数
     * @param ojName
     * @param handle
     * @return
     */
    @PostMapping("/getSolvedNumber")
    public Integer getSolvedNumber(String ojName, String handle) throws IOException, InterruptedException {
        log.info("匿名用户查询:{} - {}",ojName,handle);
        return solvedNumbersApi.getSolvedNumbers(ojName, handle);
    }

    /**
     * 更新统计信息
     * @param name
     * @return
     */
    @PostMapping("/updateStatisticsInfo")
    public Integer update(@RequestParam String name) {
        if(name == null) return null;
        User user = (User) redisUtil.get(name);
        if(user == null) {
            user = userDao.queryUserByName(name);
            if(user == null) return -1;
            redisUtil.set(name,user);
            redisUtil.set(user.getOpenId(),user);
        }
        log.info("用户{}正在更新统计信息",user.getId());
        userService.updateStatisticsInfo(user.getId());
        return 1;
    }

    /**
     * 获取统计信息
     * @param name
     * @return
     */
    @PostMapping("/getStatisticsInfo")
    public List<OjInfo> get(@RequestParam String name) {
        if(name == null) return null;
        User user = (User) redisUtil.get(name);
        if(user == null) {
            user = userDao.queryUserByName(name);
            if(user == null) return null;
            redisUtil.set(name,user);
            redisUtil.set(user.getOpenId(),user);
        }
        String openid = user.getOpenId();
        log.info("用户({}.{})正在获取统计信息",openid,name);
        return userService.getStatisticsInfo(openid);
    }

    /**
     * 注册
     * @param name
     * @param password
     * @return
     */
    @PostMapping("/register")
    public Integer register(String name,String password) {
        if(name == null) return -1;
        User user = (User) redisUtil.get(name);
        if(user == null) {
            user = userDao.queryUserByName(name);
            if(user == null) return -1;
            redisUtil.set(name,user);
            redisUtil.set(user.getOpenId(),user);
        }
        Integer x = userService.judgeUnregisteredUser(user);
        if(x == 1) {
            log.info("{}正在注册",user.getId());
            userService.setPassword(user.getOpenId(),password);
        }
        return x;
    }

    /**
     * 设置密码
     * @param name
     * @param password
     * @return
     */
    @PostMapping("/setPassword")
    public Integer setPassword(String name,String password) {
        if(name == null) return -1;
        User user = (User) redisUtil.get(name);
        if(user == null) {
            user = userDao.queryUserByName(name);
            if(user == null) return -1;
            user.setPassword(password);
            redisUtil.set(name,user);
            redisUtil.set(user.getOpenId(),user);
        }
        String openid = user.getOpenId();
        log.info("{}正在重设密码",openid);
        userService.setPassword(openid,password);
        return 1;
    }


    @PostMapping("/bindOjInfo")
    public Integer bindOjInfo(String name,String ojName,String ojUsername) {
        if(name == null) return -98;
        if(ojUsername == "") return -3;
        Integer ojType = OjFilter.NameToType.get(ojName);
        if(ojType == null) return -1;
        return userService.bindOjInfo(name, OjFilter.NameToType.get(ojName),ojUsername);
    }

    @PostMapping("/unBindOjInfo")
    public Integer unBindOjInfo(String name,String ojName,String ojUsername) {
        if(name == null) return -98;
        if(ojUsername == "") return -3;
        Integer ojType = OjFilter.NameToType.get(ojName);
        if(ojType == null) {
            log.error("{} oj没找到",ojName);
            return -1;
        }
        return userService.unBindOjInfo(name, OjFilter.NameToType.get(ojName),ojUsername);
    }
}
