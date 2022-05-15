package com.ljf.ploughthewaves.infrastructure.repository;

import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj1Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj2Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.User;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj1;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class WxRepository implements IWxRepository {
    @Resource
    private UserDao userDao;
    @Resource
    private UserAndOj1Dao userAndOj1Dao;
    @Resource
    private UserAndOj2Dao userAndOj2Dao;

    /**
     * 用户订阅时添加用户
     * @param openid
     */
    @Override
    public void addUser(String openid) {
        User user = new User();
        user.setOpenId(openid);
        user.setIsAdmin(false);
        user.setIsPublic(false);
        userDao.insert(user);
    }

    /**
     * 取消订阅删除用户
     * @param openid
     */
    @Override
    public void deleteUser(String openid) {
        Integer id = userDao.queryUserIdByOpenId(openid);
        log.info("删除的用户id为：{}",id);
        userDao.delete(id);
        log.info("user表删除成功");
        userAndOj1Dao.deleteByUid(id);
        log.info("userAndOj1表删除成功");
        userAndOj2Dao.deleteByUid(id);
        log.info("userAndOj2表删除成功");
    }

    /**
     * 更新统计信息
     * @param openid
     */
    @Override
    public void updateStatisticsInfo(String openid) {
        User user = new User();
        user.setOpenId(openid);
        userDao.update(user);
    }

    /**
     * 获取统计信息
     * @param openid
     * @return map --> 转换成xml
     */
    @Override
    public Map<String, String> getStatisticsInfo(String openid) {
        return null;
    }

    /**
     * 设置详细信息
     * @param openid
     * @param name
     * @param school
     */
    @Override
    public void setDetailInfo(String openid, String name, String school) {
        User user = new User();
        user.setOpenId(openid);
        user.setName(name);
        user.setSchool(school);
        user.setIsPublic(Boolean.TRUE);
        userDao.setDetailInfo(user);
    }

    @Override
    public List<Map<String, String>> getAllBindInfo(String openid) {

        //todo
        return null;
    }

    /**
     * 添加oj信息
     * @param ojType
     * @param ojUsername
     * @param openid
     */
    @Override
    public void addOj1(Integer ojType, String ojUsername, String openid) {
        log.info("{}正在绑定oj : ojType:{},username:{}",openid,ojType,ojUsername);
        UserAndOj1 userAndOj1 = new UserAndOj1();
        userAndOj1.setOjType(ojType);
        userAndOj1.setOjUsername(ojUsername);
        Integer uid = userDao.queryUserIdByOpenId(openid);
        userAndOj1.setUid(uid);
        userAndOj1Dao.insert(userAndOj1);
        log.info("{}插入成功",openid);
    }

    /**
     * 解绑oj
     * @param ojType
     * @param ojUsername
     * @param openid
     */
    @Override
    public void delOj1(Integer ojType, String ojUsername, String openid) {
        userAndOj1Dao.deleteByOpenidOjTypeUsername(ojType,ojUsername,openid);
    }

    /**
     * 添加oj信息
     * @param ojType
     * @param ojUsername
     * @param openid
     */
    @Override
    public void addOj2(Integer ojType, String ojUsername, String openid) {
        log.info("{}正在绑定oj : ojType:{},username:{}",openid,ojType,ojUsername);
        UserAndOj2 userAndOj2 = new UserAndOj2();
        userAndOj2.setOjType(ojType);
        userAndOj2.setOjUsername(ojUsername);
        Integer uid = userDao.queryUserIdByOpenId(openid);
        userAndOj2.setUid(uid);
        userAndOj2Dao.insert(userAndOj2);
        log.info("{}插入成功",openid);
    }

    /**
     * 解绑oj
     * @param ojType
     * @param ojUsername
     * @param openid
     */
    @Override
    public void delOj2(Integer ojType, String ojUsername, String openid) {
        userAndOj2Dao.deleteByOpenidOjTypeUsername(ojType,ojUsername,openid);
    }


}
