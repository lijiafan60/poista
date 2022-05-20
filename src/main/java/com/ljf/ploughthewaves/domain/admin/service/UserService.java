package com.ljf.ploughthewaves.domain.admin.service;

import com.ljf.ploughthewaves.domain.admin.model.vo.OjInfo;
import com.ljf.ploughthewaves.domain.admin.repository.IUserRepository;
import com.ljf.ploughthewaves.domain.poista.service.util.OjFilter;
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
}
