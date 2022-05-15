package com.ljf.ploughthewaves.infrastructure.dao;

import com.ljf.ploughthewaves.infrastructure.po.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {

    void insert(User user);

    List<User> queryUserBySchool(String school);

    List<Integer> queryUserIdBySchool(String school);

    Integer queryUserIdByOpenId(String openid);

    void delete(Integer uid);

    void update(User user);

    void setDetailInfo(User user);
}
