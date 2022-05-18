package com.ljf.ploughthewaves.infrastructure.repository;

import com.ljf.ploughthewaves.application.mq.producer.KafkaProducer;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.res.ContestCrawlRes;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;
import com.ljf.ploughthewaves.domain.poista.repository.IDoCrawlRepository;
import com.ljf.ploughthewaves.domain.poista.service.crwal.CrawlFactory;
import com.ljf.ploughthewaves.domain.poista.service.util.OjFilter;
import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj1Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj2Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.BindInfo;
import com.ljf.ploughthewaves.infrastructure.po.User;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj1;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Repository;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
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
    private IDoCrawlRepository doCrawlRepository;
    @Resource
    private CrawlFactory crawlFactory;
    @Resource
    private KafkaProducer kafkaProducer;
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
        log.info("根据openid查询出crawlReq");
        List<CrawlReq> list1 = userAndOj1Dao.getCrawlReqListByOpenid(openid);
        List<CrawlReq> list2 = userAndOj2Dao.getCrawlReqListByOpenid(openid);
        list1.addAll(list2);
        /**
         * 发送mq
         */
        for(CrawlReq crawlReq:list1) {
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

        /*
        log.info("根据crawlReqList,DoCrawl中");
        final List<CrawlRes> res1 = new ArrayList<>();
        final List<ContestCrawlRes> res2 = new ArrayList<>();
        list1.stream().forEach(x -> {
            try {
                res1.add((CrawlRes) crawlFactory.crawlConfig.get(x.ojType).doCrawl(x));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        log.info("list1,DoCrawl成功");
        list2.stream().forEach(x -> {
            try {
                res2.add((ContestCrawlRes) crawlFactory.crawlConfig.get(x.ojType).doCrawl(x));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        log.info("list2,DoCrawl成功");
        log.info("将更新结果入库");
        doCrawlRepository.updateOj1(res1);
        doCrawlRepository.updateOj2(res2);
        log.info("落库成功");
         */
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
        userAndOj2.setRecentSolvedNumber(0);
        userAndOj2.setAllContestNumber(0);
        userAndOj2.setRecentContestNumber(0);
        userAndOj2.setNowRating(0);
        userAndOj2.setMaxRating(0);
        userAndOj2.setRecentMaxRating(0);
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
