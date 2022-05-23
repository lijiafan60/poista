package com.ljf.ploughthewaves.domain.wx.repository;

import com.ljf.ploughthewaves.infrastructure.po.BindInfo;

import java.util.List;

public interface IWxRepository {
    void addUser(String openid);
    void deleteUser(String openid);
    void updateStatisticsInfo(String openid);
    void addOj1(Integer ojType,String ojUsername,String openid);
    void delOj1(Integer ojType,String ojUsername,String openid);
    void addOj2(Integer ojType,String ojUsername,String openid);
    void delOj2(Integer ojType,String ojUsername,String openid);
    Integer setDetailInfo(String openid, String name, String school);
    List<BindInfo> getAllBindInfo(String openid);
}
