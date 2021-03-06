package com.ljf.ploughthewaves.infrastructure.repository;

import com.ljf.ploughthewaves.application.mq.producer.KafkaProducer;
import com.ljf.ploughthewaves.domain.admin.model.vo.OjInfo;
import com.ljf.ploughthewaves.domain.admin.model.vo.StuInfo;
import com.ljf.ploughthewaves.domain.admin.repository.IUserRepository;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.infrastructure.dao.StrategyDao;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class UserRepository implements IUserRepository {

    @Resource
    private UserDao userDao;
    @Resource
    private UserAndOj1Dao userAndOj1Dao;
    @Resource
    private UserAndOj2Dao userAndOj2Dao;
    @Resource
    private StrategyDao strategyDao;
    @Resource
    private KafkaProducer kafkaProducer;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public User findUserByUsername(String name) {
        return userDao.queryUserByName(name);
    }

    @Override
    public List<CrawlReq> getStuCrawlReq(String openid) {
        List<CrawlReq> crawlReqList = new ArrayList<>();
        String school = userDao.queryUserByOpenid(openid).getSchool();
        List<User> userList = userDao.queryUserBySchool(school);
        for(User x : userList) {
            crawlReqList.addAll(userAndOj1Dao.getCrawlReqListByUid(x.getId()));
            crawlReqList.addAll(userAndOj2Dao.getCrawlReqListByUid(x.getId()));
        }
        return crawlReqList;
    }

    @Override
    public void updateStatisticsInfo(Integer uid) {
        log.info("??????????????????");

        List<CrawlReq> list1 = userAndOj1Dao.getCrawlReqListByUid(uid);
        List<CrawlReq> list2 = userAndOj2Dao.getCrawlReqListByUid(uid);
        list1.addAll(list2);

        for(CrawlReq crawlReq : list1) {
            ListenableFuture<SendResult<String,Object>> future = kafkaProducer.sendCrawlReq(crawlReq);
            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                @Override
                public void onFailure(Throwable ex) {
                    //todo ????????????
                    log.info("??????mq??????");
                }

                @Override
                public void onSuccess(SendResult<String, Object> result) {
                    //todo ????????????
                    log.info("??????mq??????");
                }
            });
        }
    }

    @Override
    public List<StuInfo> getStuInfo(String school) {
        List<User> userList = userDao.queryUserBySchool(school);

        log.info("{}??????????????????{}",school,userList.toString());

        List<StuInfo> stuInfoList = new ArrayList<>();
        for(User x : userList){

            StuInfo stuInfo = new StuInfo();
            stuInfo.setName(x.getName());

            int cnt = 0;
            List<UserAndOj1> l1 = userAndOj1Dao.queryByUserId(x.getId());

            log.info("??????{}???1???oj?????????{}",x.getName(),l1.toString());
            for (UserAndOj1 userAndOj1:l1) {
                cnt += userAndOj1.getAllSolvedNumber();
            }

            List<UserAndOj2> l2 = userAndOj2Dao.queryByUserId(x.getId());

            log.info("??????{}???2???oj?????????{}",x.getName(),l2.toString());

            int cfRecentMaxRating = 0,acMaxRating = 0;
            for (UserAndOj2 userAndOj2:l2) {
                cnt += userAndOj2.getAllSolvedNumber();
                int ojtype = userAndOj2.getOjType();
                if(ojtype == 0) {
                    if(userAndOj2.getRecentMaxRating() != null && userAndOj2.getRecentMaxRating() > cfRecentMaxRating) {
                        cfRecentMaxRating = userAndOj2.getRecentMaxRating();
                        stuInfo.setCfName(userAndOj2.getOjUsername());
                        stuInfo.setCfRating(userAndOj2.getNowRating());
                        stuInfo.setCfMaxRating(userAndOj2.getMaxRating());
                        stuInfo.setCfRecentMaxRating(userAndOj2.getRecentMaxRating());
                        stuInfo.setCfContestNumber(userAndOj2.getAllContestNumber());
                        stuInfo.setCfRecentContestNumber(userAndOj2.getRecentContestNumber());
                    }
                } else {
                    if(userAndOj2.getMaxRating() != null && userAndOj2.getMaxRating() > acMaxRating) {
                        acMaxRating = userAndOj2.getMaxRating();
                        stuInfo.setAcName(userAndOj2.getOjUsername());
                        stuInfo.setAcRating(userAndOj2.getNowRating());
                        stuInfo.setAcMaxRating(userAndOj2.getMaxRating());
                        stuInfo.setAcContestNumber(userAndOj2.getAllContestNumber());
                    }
                }
            }

            stuInfo.setAllSolvedNumber(cnt);

            stuInfoList.add(stuInfo);
        }
        return stuInfoList;
    }

    @Override
    public User getUserByOpenid(String openid) {
       return userDao.queryUserByOpenid(openid);
    }

    @Override
    public void setStatisticsStrategy(Strategy strategy, String school) {
        strategyDao.delStrategy(school);
        strategyDao.addStrategy(strategy,school);
    }

    @Override
    public List<OjInfo> getStatisticsInfo(String openid) {
        Integer id = userDao.queryUserIdByOpenId(openid);
        List<UserAndOj1> userAndOj1List = userAndOj1Dao.queryByUserId(id);
        List<UserAndOj2> userAndOj2List = userAndOj2Dao.queryByUserId(id);
        List<OjInfo> ojInfoList = new ArrayList<>();
        for(UserAndOj1 x : userAndOj1List) {
            OjInfo ojInfo = new OjInfo();

            ojInfo.setOjType(x.getOjType());
            ojInfo.setOjUsername(x.getOjUsername());

            ojInfo.setAllSolvedNumber(x.getAllSolvedNumber());
            ojInfo.setUpdTime(x.getUpdTime());

            ojInfoList.add(ojInfo);
        }
        for(UserAndOj2 x : userAndOj2List) {
            OjInfo ojInfo = new OjInfo();

            ojInfo.setOjType(x.getOjType());
            ojInfo.setOjUsername(x.getOjUsername());

            ojInfo.setNowRating(x.getNowRating());
            ojInfo.setMaxRating(x.getMaxRating());
            ojInfo.setRecentMaxRating(x.getRecentMaxRating());

            ojInfo.setAllContestNumber(x.getAllContestNumber());
            ojInfo.setRecentContestNumber(x.getRecentContestNumber());

            ojInfo.setAllSolvedNumber(x.getAllSolvedNumber());
            ojInfo.setUpdTime(x.getUpdTime());

            ojInfoList.add(ojInfo);
        }
        return ojInfoList;
    }

    @Override
    public void setPassword(String openid, String password) {
        userDao.setPassword(openid,password);
    }

    @Override
    public BindInfo getBindInfo(Integer uid, Integer ojType, String ojUsername) {
        if(ojType >= 2) return userAndOj1Dao.queryByUidOjTypeOjUsername(uid,ojType,ojUsername);
        else return userAndOj2Dao.queryByUidOjTypeOjUsername(uid,ojType,ojUsername);
    }

    /**
     * ??????oj??????
     * @param ojType
     * @param ojUsername
     * @param openid
     */
    @Override
    public void addOj1(Integer ojType, String ojUsername, String openid) {
        log.info("{}????????????oj : ojType:{},username:{}",openid,ojType,ojUsername);
        UserAndOj1 userAndOj1 = new UserAndOj1();
        userAndOj1.setOjType(ojType);
        userAndOj1.setOjUsername(ojUsername);
        Integer uid = userDao.queryUserIdByOpenId(openid);
        userAndOj1.setUid(uid);
        userAndOj1.setAllSolvedNumber(0);
        userAndOj1Dao.insert(userAndOj1);
        log.info("{}????????????",openid);
    }

    /**
     * ??????oj
     * @param ojType
     * @param ojUsername
     * @param openid
     */
    @Override
    public void delOj1(Integer ojType, String ojUsername, String openid) {
        userAndOj1Dao.deleteByOpenidOjTypeUsername(ojType,ojUsername,openid);
    }

    /**
     * ??????oj??????
     * @param ojType
     * @param ojUsername
     * @param openid
     */
    @Override
    public void addOj2(Integer ojType, String ojUsername, String openid) {
        log.info("{}????????????oj : ojType:{},username:{}",openid,ojType,ojUsername);
        UserAndOj2 userAndOj2 = new UserAndOj2();
        userAndOj2.setOjType(ojType);
        userAndOj2.setOjUsername(ojUsername);
        Integer uid = userDao.queryUserIdByOpenId(openid);
        userAndOj2.setUid(uid);
        userAndOj2.setAllSolvedNumber(0);
        userAndOj2Dao.insert(userAndOj2);
        log.info("{}????????????",openid);
    }

    /**
     * ??????oj
     * @param ojType
     * @param ojUsername
     * @param openid
     */
    @Override
    public void delOj2(Integer ojType, String ojUsername, String openid) {
        userAndOj2Dao.deleteByOpenidOjTypeUsername(ojType,ojUsername,openid);
    }

}
