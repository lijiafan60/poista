package com.ljf.ploughthewaves.infrastructure.dao;

import com.ljf.ploughthewaves.infrastructure.po.UserAndOj1;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserAndOj1Dao {

    void insert(UserAndOj1 userAndOj1);

    void insertList(List<UserAndOj1> list);

    void update(UserAndOj1 userAndOj1);

    void deleteByUid(Integer uid);

    void deleteByOpenidOjTypeUsername(@Param("ojType") Integer ojType, @Param("ojUsername") String ojUsername,@Param("openid") String openid);

    List<UserAndOj1> queryByUserId(Integer id);

    List<UserAndOj1> queryByUidOjType(Integer id,Integer ojType);
}
