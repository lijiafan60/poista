package com.ljf.ploughthewaves.domain.wx.repository;

import java.util.List;
import java.util.Map;

public interface IWxRepository {
    void addUser(String openid);
    void deleteUser(String openid);
    void updateStatisticsInfo(String openid);
    void addOj1(Integer ojType,String ojUsername,String openid);
    void delOj1(Integer ojType,String ojUsername,String openid);
    void addOj2(Integer ojType,String ojUsername,String openid);
    void delOj2(Integer ojType,String ojUsername,String openid);
    Map<String,String> getStatisticsInfo(String openid);
    void setDetailInfo(String openid, String name, String school);
    List<Map<String,String>> getAllBindInfo(String openid);
}
