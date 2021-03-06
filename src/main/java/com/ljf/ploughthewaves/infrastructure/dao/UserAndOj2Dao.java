package com.ljf.ploughthewaves.infrastructure.dao;

import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.infrastructure.po.BindInfo;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserAndOj2Dao {

    void insert(UserAndOj2 userAndOj2);

    void insertList(List<UserAndOj2> list);

    void update(UserAndOj2 userAndOj2);

    void deleteByUid(Integer uid);

    void deleteByOpenidOjTypeUsername(@Param("ojType") Integer ojType, @Param("ojUsername") String ojUsername, @Param("openid") String openid);

    List<UserAndOj2> queryByUserId(Integer uid);

    BindInfo queryByUidOjTypeOjUsername(@Param("uid") Integer uid,@Param("ojType") Integer ojType,@Param("ojUsername") String ojUsername);

    List<BindInfo> getBindInfo(String openid);

    List<CrawlReq> getCrawlReqListByUid(Integer uid);

    List<CrawlReq> getCrawlReqList();

    List<UserAndOj2> getStuInfo(String school);
}
