package com.ljf.ploughthewaves.infrastructure.repository;

import com.ljf.ploughthewaves.application.mq.producer.KafkaProducer;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.service.util.OjFilter;
import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.infrastructure.dao.TagsStatisticsDao;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj1Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj2Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.*;
import com.ljf.ploughthewaves.infrastructure.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Repository;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Repository
public class WxRepository implements IWxRepository {
    @Resource
    private UserDao userDao;
    @Resource
    private UserAndOj1Dao userAndOj1Dao;
    @Resource
    private UserAndOj2Dao userAndOj2Dao;
    @Resource
    private KafkaProducer kafkaProducer;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private TagsStatisticsDao tagsStatisticsDao;

    /**
     * 用户订阅时添加用户
     * @param openid
     */
    @Override
    public void addUser(String openid) {

        User user = userDao.queryUserByOpenid(openid);
        if(user != null) userDao.delete(user.getId());

        user = new User();
        user.setOpenId(openid);
        user.setName(openid.substring(5));
        user.setIsAdmin(false);
        user.setIsPublic(false);
        user.setRole("ROLE_user");

        userDao.insert(user);

        user = userDao.queryUserByOpenid(openid);
        redisUtil.set(openid, user);
        redisUtil.set(user.getName(), user);

        TagsStatistics tagsStatistics = new TagsStatistics();
        tagsStatistics.setUid(user.getId());
        tagsStatistics.setBruteforce(0);
        tagsStatistics.setDataStructure(0);
        tagsStatistics.setDp(0);
        tagsStatistics.setGraphs(0);
        tagsStatistics.setGreedy(0);
        tagsStatistics.setMath(0);

        tagsStatisticsDao.insert(tagsStatistics);
    }

    /**
     * 取消订阅删除用户
     * @param openid
     */
    @Override
    public void deleteUser(String openid) {
        User user = (User) redisUtil.get(openid);
        if(user == null) {
            user = userDao.queryUserByOpenid(openid);
        }
        String name = user.getName();
        Integer id = user.getId();
        redisUtil.del(openid,name);
        userDao.delete(id);
        userAndOj1Dao.deleteByUid(id);
        userAndOj2Dao.deleteByUid(id);
        tagsStatisticsDao.deleteByUid(id);
        log.info("用户{}信息清理完成",id);
    }

    /**
     * 更新统计信息
     * @param openid
     */
    @Override
    public void updateStatisticsInfo(String openid) {
        User user = userDao.queryUserByOpenid(openid);
        log.info("更新{}的统计信息",user.getName());
        List<CrawlReq> list1 = userAndOj1Dao.getCrawlReqListByUid(user.getId());
        List<CrawlReq> list2 = userAndOj2Dao.getCrawlReqListByUid(user.getId());
        list1.addAll(list2);

        /**
         * 发送mq
         */
        for(CrawlReq crawlReq : list1) {
            ListenableFuture<SendResult<String,Object>> future = kafkaProducer.sendCrawlReq(crawlReq);
            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                @Override
                public void onFailure(Throwable ex) {
                    //todo 任务入库
                    log.info("发送mq失败");
                }

                @Override
                public void onSuccess(SendResult<String, Object> result) {
                    //todo 任务入库
                    log.info("发送mq成功");
                }
            });
        }
    }

    /**
     * 设置详细信息
     * @param openid
     * @param name
     * @param school
     * @return
     */
    @Override
    public Integer setDetailInfo(String openid, String name, String school) {
        User user0 = userDao.queryUserByName(name);
        if(user0 != null) return -1;
        User user = userDao.queryUserByOpenid(openid);
        redisUtil.del(user.getName(),openid);
        user.setName(name);
        user.setSchool(school);
        userDao.setDetailInfo(user);
        return 1;
    }

    @Override
    public List<BindInfo> getAllBindInfo(String openid) {
        List<BindInfo> res1 = userAndOj1Dao.getBindInfo(openid);
        log.info("{}成功查询1类oj信息",openid);
        List<BindInfo> res2 = userAndOj2Dao.getBindInfo(openid);
        log.info("{}成功查询2类oj信息",openid);
        List<BindInfo> res = Stream.of(res1,res2).flatMap(Collection::stream).collect(Collectors.toList());
        res.stream().forEach(x -> x.ojName = OjFilter.TypeToName.get(x.ojType));
        log.info("list合并转换成功");
        return res;
    }

    @Override
    public BindInfo getBindInfo(Integer uid, Integer ojType, String ojUsername) {
        if(ojType >= 2) return userAndOj1Dao.queryByUidOjTypeOjUsername(uid,ojType,ojUsername);
        else return userAndOj2Dao.queryByUidOjTypeOjUsername(uid,ojType,ojUsername);
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
        userAndOj1.setAllSolvedNumber(0);
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
        userAndOj2.setAllSolvedNumber(0);
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
