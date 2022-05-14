package com.ljf.ploughthewaves.domain.wx.repository;

import com.ljf.ploughthewaves.infrastructure.po.StatisticsInfo;
import com.ljf.ploughthewaves.infrastructure.po.User;

public interface IWxRepository {
    void addUser(String openid);
    void deleteUser(String openid);
    void updateUserInfo(String openid);
    void addOj1(Integer ojType,String ojUsername,String openid);
    void addOj2(Integer ojType,String ojUsername,String openid);
    StatisticsInfo getUserInfo(String openid);
}
