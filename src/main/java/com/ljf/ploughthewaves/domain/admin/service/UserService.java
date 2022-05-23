package com.ljf.ploughthewaves.domain.admin.service;

import com.ljf.ploughthewaves.domain.admin.model.vo.OjInfo;
import com.ljf.ploughthewaves.domain.admin.repository.IUserRepository;
import com.ljf.ploughthewaves.domain.poista.service.util.OjFilter;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Resource
    private IUserRepository userRepository;
    @Resource
    private UserDao userDao;

    public void updateStatisticsInfo(String openid) {
        userRepository.updateStatisticsInfo(openid);
    }

    public boolean isAdmin(String openid,String school) {
        User user = userRepository.getUserByOpenid(openid);
        return (user.getIsAdmin().booleanValue() && user.getSchool().equals(school));
    }

    public List<OjInfo> getStatisticsInfo(String openid) {
        List<OjInfo> ojInfoList = userRepository.getStatisticsInfo(openid);
        for(OjInfo x : ojInfoList) {
            x.setOjName(OjFilter.TypeToName.get(x.getOjType()));
        }
        return ojInfoList;
    }

    public Integer judgeUnregisteredUser(String openid) {
        User user = userRepository.getUserByOpenid(openid);
        if(user == null) return -1;
        if(user.getPassword() != null && !user.getPassword().equals("")) {
            log.info("{}请求注册，但已有密码：{}",openid,user.getPassword());
            return -2;
        }
        return 1;
    }

    public void setPassword(String openid,String password) {
        userRepository.setPassword(openid,password);
    }

    public boolean judgeLegalUser(String openid) {
        return (userRepository.getUserByOpenid(openid) != null);
    }

    public Integer bindOjInfo(String name,Integer ojType,String ojUsername) {
        User user = userDao.queryUserByName(name);
        String openid = user.getOpenId();
        Integer uid = user.getId();
        if(userRepository.getBindInfo(uid,ojType,ojUsername) != null) {
            log.error("重复绑定");
            return -2;
        }
        if(ojType >= 2) userRepository.addOj1(ojType,ojUsername,openid);
        else userRepository.addOj2(ojType,ojUsername,openid);
        return 1;
    }

    public Integer unBindOjInfo(String name,Integer ojType,String ojUsername) {
        User user = userDao.queryUserByName(name);
        String openid = user.getOpenId();
        Integer uid = user.getId();
        if(userRepository.getBindInfo(uid,ojType,ojUsername) == null) {
            log.info("解绑的信息不存在");
            return -2;
        }
        if(ojType >= 2) userRepository.delOj1(ojType,ojUsername,openid);
        else userRepository.delOj2(ojType,ojUsername,openid);
        return 1;
    }
}
