package com.ljf.ploughthewaves.infrastructure.repository;

import com.ljf.ploughthewaves.application.mq.producer.KafkaProducer;
import com.ljf.ploughthewaves.domain.admin.model.vo.OjInfo;
import com.ljf.ploughthewaves.domain.admin.model.vo.StuInfo;
import com.ljf.ploughthewaves.domain.admin.repository.IUserRepository;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.service.util.OjFilter;
import com.ljf.ploughthewaves.infrastructure.dao.StrategyDao;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj1Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj2Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.Strategy;
import com.ljf.ploughthewaves.infrastructure.po.User;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj1;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj2;
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

    @Override
    public User findUserByUsername(String openid) {
        return userDao.queryUserByOpenid(openid);
    }

    @Override
    public List<CrawlReq> getStuCrawlReq(String openid) {
        List<CrawlReq> crawlReqList = new ArrayList<>();
        String school = userDao.queryUserByOpenid(openid).getSchool();
        List<User> userList = userDao.queryUserBySchool(school);
        for(User x : userList) {
            crawlReqList.addAll(userAndOj1Dao.getCrawlReqListByOpenid(x.getOpenId()));
            crawlReqList.addAll(userAndOj2Dao.getCrawlReqListByOpenid(x.getOpenId()));
        }
        return crawlReqList;
    }

    @Override
    public void updateStatisticsInfo(String openid) {
        log.info("更新统计信息");
        List<CrawlReq> list1 = userAndOj1Dao.getCrawlReqListByOpenid(openid);
        List<CrawlReq> list2 = userAndOj2Dao.getCrawlReqListByOpenid(openid);
        list1.addAll(list2);

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

    @Override
    public List<StuInfo> getStuInfo(String school) {
        List<User> userList = userDao.queryUserBySchool(school);
        List<StuInfo> stuInfoList = new ArrayList<>();
        for(User x : userList){
            StuInfo stuInfo = new StuInfo();
            stuInfo.setName(x.getName());

            int cnt = 0;
            List<UserAndOj1> l1 = userAndOj1Dao.queryByUserId(x.getId());
            for (UserAndOj1 userAndOj1:l1) {
                cnt += userAndOj1.getAllSolvedNumber();
            }

            List<UserAndOj2> l2 = userAndOj2Dao.queryByUserId(x.getId());
            int cfRecentMaxRating = 0,acMaxRating = 0;
            for (UserAndOj2 userAndOj2:l2) {
                cnt += userAndOj2.getAllSolvedNumber();
                int ojtype = userAndOj2.getOjType();
                if(ojtype == 0) {
                    if(userAndOj2.getRecentMaxRating() > cfRecentMaxRating) {
                        cfRecentMaxRating = userAndOj2.getRecentMaxRating();
                        stuInfo.setCfName(userAndOj2.getOjUsername());
                        stuInfo.setCfRating(userAndOj2.getNowRating());
                        stuInfo.setCfMaxRating(userAndOj2.getMaxRating());
                        stuInfo.setCfRecentMaxRating(userAndOj2.getRecentMaxRating());
                        stuInfo.setCfContestNumber(userAndOj2.getAllContestNumber());
                        stuInfo.setCfRecentContestNumber(userAndOj2.getRecentContestNumber());
                    }
                } else {
                    if(userAndOj2.getMaxRating() > acMaxRating) {
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

            ojInfoList.add(ojInfo);
        }
        return ojInfoList;
    }
}
