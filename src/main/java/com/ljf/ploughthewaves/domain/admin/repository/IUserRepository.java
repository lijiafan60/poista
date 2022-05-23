package com.ljf.ploughthewaves.domain.admin.repository;

import com.ljf.ploughthewaves.domain.admin.model.vo.OjInfo;
import com.ljf.ploughthewaves.domain.admin.model.vo.StuInfo;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.infrastructure.po.BindInfo;
import com.ljf.ploughthewaves.infrastructure.po.Strategy;
import com.ljf.ploughthewaves.infrastructure.po.User;

import java.util.List;


public interface IUserRepository {
    User findUserByUsername(String name);
    List<CrawlReq> getStuCrawlReq(String openid);
    void updateStatisticsInfo(String openid);
    List<StuInfo> getStuInfo(String school);
    User getUserByOpenid(String openid);
    void setStatisticsStrategy(Strategy strategy, String school);
    List<OjInfo> getStatisticsInfo(String openid);
    void setPassword(String openid,String password);
    BindInfo getBindInfo(Integer uid, Integer ojType, String ojUsername);
    void addOj1(Integer ojType,String ojUsername,String openid);
    void delOj1(Integer ojType,String ojUsername,String openid);
    void addOj2(Integer ojType,String ojUsername,String openid);
    void delOj2(Integer ojType,String ojUsername,String openid);
}
