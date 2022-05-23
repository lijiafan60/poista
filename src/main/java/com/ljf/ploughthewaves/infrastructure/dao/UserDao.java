package com.ljf.ploughthewaves.infrastructure.dao;

import com.ljf.ploughthewaves.infrastructure.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.ListIterator;

@Mapper
public interface UserDao {

    void insert(User user);

    List<User> queryUserBySchool(String school);

    List<Integer> queryUserIdBySchool(String school);

    List<Integer> queryAllUser();

    Integer queryUserIdByOpenId(String openid);

    void delete(Integer uid);

    void update(User user);

    void setDetailInfo(User user);

    User queryUserByOpenid(String openid);

    void setPassword(@Param("openid") String openid,@Param("password") String password);

    User queryUserByName(String name);
}
