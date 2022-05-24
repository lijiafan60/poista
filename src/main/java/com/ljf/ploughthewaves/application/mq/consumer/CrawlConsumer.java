package com.ljf.ploughthewaves.application.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;
import com.ljf.ploughthewaves.domain.poista.repository.IDoCrawlRepository;
import com.ljf.ploughthewaves.domain.poista.service.crwal.CrawlFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CrawlConsumer {

    @Resource
    public CrawlFactory crawlFactory;

    @Resource
    public IDoCrawlRepository doCrawlRepository;

    @KafkaListener(topics = "crawl",groupId = "docrawl")
    public void onMessage(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> message = Optional.ofNullable(record.value());
        if(!message.isPresent()) {
            return;
        }
        log.info("开始消费:{}",Thread.currentThread().getName());
        //处理消息
        try {
            CrawlReq crawlReq = JSON.parseObject((String) message.get(), CrawlReq.class);
            log.info("更新的oj:{}",crawlReq.getOjType());
            CrawlRes res = new CrawlRes();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            if(crawlReq.getOjType() >= 2) {
                try {
                    crawlFactory.crawlConfig.get(crawlReq.ojType).doCrawl(crawlReq, res, countDownLatch);
                    countDownLatch.await(100, TimeUnit.SECONDS);
                    doCrawlRepository.updateOj1(res);
                    log.info("消费完成 : {}", res);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.info("消费失败 : {}", res);
                }
            } else {
                try {
                    crawlFactory.crawlConfig.get(crawlReq.ojType).doCrawl(crawlReq, res, countDownLatch);
                    countDownLatch.await(100, TimeUnit.SECONDS);
                    doCrawlRepository.updateOj2(res);
                    log.info("消费完成 : {}", res);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.info("消费失败 : {}", res);
                }
            }
            ack.acknowledge();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "codeforces",groupId = "codeforcesCrawl")
    public void onCFMessage(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> message = Optional.ofNullable(record.value());
        if(!message.isPresent()) {
            return;
        }
        log.info("开始消费:{}",Thread.currentThread().getName());
        //处理消息
        CrawlReq crawlReq = JSON.parseObject((String) message.get(), CrawlReq.class);
        log.info("更新的codeforces");
        CrawlRes res = new CrawlRes();
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            crawlFactory.crawlConfig.get(crawlReq.ojType).doCrawl(crawlReq,res,countDownLatch);
            countDownLatch.wait(100);
            doCrawlRepository.updateOj1(res);
            log.info("消费完成 : {}", res);
        } catch (Exception e) {
            //todo 异常状态入库
            e.printStackTrace();
            log.info("消费失败 : {}", res);
        }
        ack.acknowledge();
    }
}
