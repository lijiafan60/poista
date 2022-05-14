package com.ljf.ploughthewaves.infrastructure.dao;

import com.ljf.ploughthewaves.infrastructure.po.User;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj1;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj2;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAndOj2Dao {

    void insert(UserAndOj2 userAndOj2);

    void insertList(List<UserAndOj2> list);

    void update(UserAndOj2 userAndOj2);

    void deleteByUid(Integer uid);

    void deleteByUidOjType(Integer uid,Integer ojType);

    List<UserAndOj2> queryByUserId(Integer uid);

    List<UserAndOj2> queryByUidOjType(Integer uid,Integer ojType);
}
