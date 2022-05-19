package com.ljf.ploughthewaves.domain.admin.service;

import com.ljf.ploughthewaves.domain.admin.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserService {
    @Resource
    private IUserRepository userRepository;

    public void updateStatisticsInfo(String openid) {
        userRepository.updateStatisticsInfo(openid);
    }
}
