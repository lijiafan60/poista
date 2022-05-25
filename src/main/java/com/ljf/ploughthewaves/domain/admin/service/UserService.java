package com.ljf.ploughthewaves.domain.admin.service;

import com.ljf.ploughthewaves.domain.admin.model.vo.OjInfo;
import com.ljf.ploughthewaves.domain.admin.repository.IUserRepository;
import com.ljf.ploughthewaves.domain.poista.service.util.OjFilter;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.User;
import com.ljf.ploughthewaves.infrastructure.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Resource
    private IUserRepository userRepository;

    public void updateStatisticsInfo(Integer uid) {
        userRepository.updateStatisticsInfo(uid);
    }

    public List<OjInfo> getStatisticsInfo(String openid) {
        List<OjInfo> ojInfoList = userRepository.getStatisticsInfo(openid);
        for(OjInfo x : ojInfoList) {
            x.setOjName(OjFilter.TypeToName.get(x.getOjType()));
        }
        return ojInfoList;
    }

    public Integer judgeUnregisteredUser(User user) {
        if(user == null) return -1;
        if(user.getPassword() != null && !user.getPassword().equals("")) {
            log.info("{}请求注册，但已有密码：{}",user.getId(),user.getPassword());
            return -2;
        }
        return 1;
    }

    public void setPassword(String openid,String password) {
        userRepository.setPassword(openid,password);
    }

    public Integer bindOjInfo(String name,Integer ojType,String ojUsername) {
        User user = userRepository.findUserByUsername(name);
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
        User user = userRepository.findUserByUsername(name);
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
