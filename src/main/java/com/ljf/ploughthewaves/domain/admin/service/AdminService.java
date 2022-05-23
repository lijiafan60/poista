package com.ljf.ploughthewaves.domain.admin.service;

import com.ljf.ploughthewaves.application.mq.producer.KafkaProducer;
import com.ljf.ploughthewaves.domain.admin.model.vo.StuInfo;
import com.ljf.ploughthewaves.domain.admin.repository.IUserRepository;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.infrastructure.dao.StrategyDao;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.Strategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class AdminService {
    @Resource
    private IUserRepository userRepository;
    @Resource
    private KafkaProducer kafkaProducer;
    @Resource
    private UserDao userDao;
    @Resource
    private StrategyDao strategyDao;

    public List<StuInfo> getStuInfo(String openid) {

        String school = userDao.queryUserByOpenid(openid).getSchool();

        log.info("学校为：{}",school);

        List<StuInfo> stuInfoList = userRepository.getStuInfo(school);

        log.info("根据策略计算pt");

        Strategy strategy = strategyDao.getStrategy(school);
        for (StuInfo x : stuInfoList){
            double pt = 0;
            if(x.getAllSolvedNumber() != null && strategy.getAllSolvedNumber() != null)
                pt += x.getAllSolvedNumber() * strategy.getAllSolvedNumber();
            if(x.getCfRating() != null && strategy.getCfRating() != null)
                pt += x.getCfRating() * strategy.getCfRating();
            if(x.getCfMaxRating() != null && strategy.getCfMaxRating() != null)
                pt += x.getCfMaxRating() * strategy.getCfMaxRating();
            if(x.getCfRecentMaxRating() != null &&strategy.getCfRecentMaxRating() != null )
                pt += x.getCfRecentMaxRating() * strategy.getCfRecentMaxRating();
            if(x.getCfContestNumber() != null && strategy.getCfContestNumber() != null)
                pt += x.getCfContestNumber() * strategy.getCfContestNumber();
            if(x.getCfRecentContestNumber() != null && strategy.getCfRecentContestNumber() != null)
                pt += x.getCfRecentContestNumber() * strategy.getCfRecentContestNumber();
            if(x.getAcRating() != null && strategy.getAcRating() != null)
                pt += x.getAcRating() * strategy.getAcRating();
            if(x.getAcMaxRating() != null && strategy.getAcMaxRating() != null)
                pt += x.getAcMaxRating() * strategy.getAcMaxRating();
            if(x.getAcContestNumber() != null && strategy.getAcContestNumber() != null)
                pt += x.getAcContestNumber() * strategy.getAcContestNumber();
            x.setPt(pt);
        }
        stuInfoList.sort(new Comparator<StuInfo>() {
            @Override
            public int compare(StuInfo o1, StuInfo o2) {
                return o2.getPt().compareTo(o1.getPt());
            }
        });
        return stuInfoList;
    }

    /**
     * 更新学生池信息
     * @param openid
     */
    public void updStuStatisticsInfo(String openid) {

        List<CrawlReq> reqList = userRepository.getStuCrawlReq(openid);
        for(CrawlReq crawlReq:reqList) {
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
     * 设置统计策略
     * @param strategy
     * @param school
     */
    public void setStatisticsStrategy(Strategy strategy, String school) {
        userRepository.setStatisticsStrategy(strategy,school);
    }

    public boolean isAdmin(String openid) {
        return userDao.queryUserByOpenid(openid).getIsAdmin();
    }
}
