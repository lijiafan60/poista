package com.ljf.ploughthewaves.infrastructure.repository;

import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj1Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj2Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.StatisticsInfo;
import com.ljf.ploughthewaves.infrastructure.po.User;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj1;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj2;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class WxRepository implements IWxRepository {
    @Resource
    private UserDao userDao;
    @Resource
    private UserAndOj1Dao userAndOj1Dao;
    @Resource
    private UserAndOj2Dao userAndOj2Dao;

    public void addUser(String openid) {
        User user = new User();
        UserAndOj1 userAndOj1 = new UserAndOj1();
        UserAndOj2 userAndOj2 = new UserAndOj2();
        user.setOpenId(openid);
        user.setIsAdmin(false);
        user.setIsPublic(false);
        userDao.insert(user);
    }

    public void deleteUser(String openid) {
        Integer id = userDao.queryUserIdByOpenId(openid);
        userDao.delete(id);
        userAndOj1Dao.deleteByUid(id);
        userAndOj2Dao.deleteByUid(id);
    }

    public void updateUserInfo(String openid) {
        User user = new User();
        user.setOpenId(openid);
        userDao.update(user);
    }

    @Override
    public void addOj1(Integer ojType, String ojUsername, String openid) {
        UserAndOj1 userAndOj1 = new UserAndOj1();
        userAndOj1.setOjType(ojType);
        userAndOj1.setOjUsername(ojUsername);
        Integer uid = userDao.queryUserIdByOpenId(openid);
        userAndOj1.setUid(uid);
        userAndOj1Dao.insert(userAndOj1);
    }

    @Override
    public void addOj2(Integer ojType, String ojUsername, String openid) {
        UserAndOj2 userAndOj2 = new UserAndOj2();
        userAndOj2.setOjType(ojType);
        userAndOj2.setOjUsername(ojUsername);
        Integer uid = userDao.queryUserIdByOpenId(openid);
        userAndOj2.setUid(uid);
        userAndOj2Dao.insert(userAndOj2);
    }

    @Override
    public StatisticsInfo getUserInfo(String openid) {
        return null;
    }

}
