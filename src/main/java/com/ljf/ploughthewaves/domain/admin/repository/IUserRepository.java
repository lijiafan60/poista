package com.ljf.ploughthewaves.domain.admin.repository;

import com.ljf.ploughthewaves.domain.admin.model.vo.StuInfo;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.infrastructure.po.Strategy;
import com.ljf.ploughthewaves.infrastructure.po.User;

import java.util.List;


public interface IUserRepository {
    User findUserByUsername(String openid);
    List<CrawlReq> getStuCrawlReq(String openid);
    void updateStatisticsInfo(String openid);
    List<StuInfo> getStuInfo(String school);
    User getUserByOpenid(String openid);
    void setStatisticsStrategy(Strategy strategy, String school);
}
