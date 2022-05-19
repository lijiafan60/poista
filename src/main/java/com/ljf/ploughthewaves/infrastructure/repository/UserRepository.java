package com.ljf.ploughthewaves.infrastructure.repository;

import com.ljf.ploughthewaves.application.mq.producer.KafkaProducer;
import com.ljf.ploughthewaves.domain.admin.model.vo.StuInfo;
import com.ljf.ploughthewaves.domain.admin.repository.IUserRepository;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.service.util.OjFilter;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj1Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj2Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
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
    private KafkaProducer kafkaProducer;

    @Override
    public User findUserByUsername(String openid) {
        return userDao.queryUserByOpenid(openid);
    }

    @Override
    public List<CrawlReq> getStuCrawlReq(String openid) {
        final List<CrawlReq> crawlReqList = new ArrayList<>();
        String school = userDao.queryUserByOpenid(openid).getSchool();
        List<User> userList = userDao.queryUserBySchool(school);
        userList.stream().forEach(x -> {
            crawlReqList.addAll(userAndOj1Dao.getCrawlReqListByOpenid(x.getOpenId()));
            crawlReqList.addAll(userAndOj2Dao.getCrawlReqListByOpenid(x.getOpenId()));
        });
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
    public List<StuInfo> getStuInfo(String openid) {
        String school = userDao.queryUserByOpenid(openid).getSchool();
        List<UserAndOj1> list1 = userAndOj1Dao.getStuInfo(school);
        List<UserAndOj2> list2 = userAndOj2Dao.getStuInfo(school);
        final List<StuInfo> stuInfoList = new ArrayList<>();
        list1.stream().forEach(x -> {
            StuInfo stu = new StuInfo();
            stu.setName(x.getName());
            stu.setOjUsername(x.getOjUsername());
            stu.setOjName(OjFilter.TypeToName.get(x.getOjType()));
            stu.setSolvedNumber(x.getAllSolvedNumber());
            stuInfoList.add(stu);
        });
        list2.stream().forEach(x -> {
            StuInfo stu = new StuInfo();
            stu.setName(x.getName());
            stu.setOjUsername(x.getOjUsername());
            stu.setOjName(OjFilter.TypeToName.get(x.getOjType()));
            stu.setSolvedNumber(x.getAllSolvedNumber());
            stuInfoList.add(stu);
        });
        return stuInfoList;
    }
}
