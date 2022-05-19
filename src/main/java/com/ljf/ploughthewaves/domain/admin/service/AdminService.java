package com.ljf.ploughthewaves.domain.admin.service;

import com.ljf.ploughthewaves.application.mq.producer.KafkaProducer;
import com.ljf.ploughthewaves.domain.admin.model.vo.StuInfo;
import com.ljf.ploughthewaves.domain.admin.repository.IUserRepository;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AdminService {
    @Resource
    private IUserRepository userRepository;
    @Resource
    private KafkaProducer kafkaProducer;

    public List<StuInfo> getStuInfo(String openid) {
        return userRepository.getStuInfo(openid);
    }

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


}
