package com.ljf.ploughthewaves.interfaces;

import com.ljf.ploughthewaves.domain.admin.model.vo.OjInfo;
import com.ljf.ploughthewaves.domain.admin.service.UserService;
import com.ljf.ploughthewaves.domain.poista.service.util.OjFilter;
import com.ljf.ploughthewaves.domain.poista.service.util.SolvedNumbersApi;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

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

    /**
     * 匿名用户获取刷题数
     * @param ojName
     * @param handle
     * @return
     */
    @PostMapping("/getSolvedNumber")
    public int getSolvedNumber(String ojName, String handle) throws IOException {
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
        log.info("{}正在更新统计信息",name);
        String openid = userDao.queryUserByName(name).getOpenId();
        userService.updateStatisticsInfo(openid);
        return 1;
    }

    /**
     * 获取统计信息
     * @param name
     * @return
     */
    @PostMapping("/getStatisticsInfo")
    public List<OjInfo> get(@RequestParam String name) {
        log.info("{}正在获取统计信息",name);
        String openid = userDao.queryUserByName(name).getOpenId();
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
        String openid = userDao.queryUserByName(name).getOpenId();

        Integer x = userService.judgeUnregisteredUser(openid);
        if(x == 1) {
            log.info("{}正在注册",openid);
            userService.setPassword(openid,password);
            return 1;
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
        log.info("{}正在重设密码：{}",name,password);
        User user = userDao.queryUserByName(name);
        if(user == null) return -1;
        String openid = user.getOpenId();
        if(userService.judgeLegalUser(openid)) {
            log.info("{}正在重设密码",openid);
            userService.setPassword(openid,password);
            return 1;
        }
        return -1;
    }


    @PostMapping("/bindOjInfo")
    public Integer bindOjInfo(String name,String ojName,String ojUsername) {
        if(ojUsername == "") return -3;
        Integer ojType = OjFilter.NameToType.get(ojName);
        if(ojType == null) {
            log.error("{} oj没找到",ojName);
            return -1;
        }
        return userService.bindOjInfo(name, OjFilter.NameToType.get(ojName),ojUsername);
    }

    @PostMapping("/unBindOjInfo")
    public Integer unBindOjInfo(String name,String ojName,String ojUsername) {
        if(ojUsername == "") return -3;
        Integer ojType = OjFilter.NameToType.get(ojName);
        if(ojType == null) {
            log.error("{} oj没找到",ojName);
            return -1;
        }
        return userService.unBindOjInfo(name, OjFilter.NameToType.get(ojName),ojUsername);
    }
}
