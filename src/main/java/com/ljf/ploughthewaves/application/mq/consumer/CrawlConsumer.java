package com.ljf.ploughthewaves.application.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.res.CommonRes;
import com.ljf.ploughthewaves.domain.poista.model.res.ContestCrawlRes;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;
import com.ljf.ploughthewaves.domain.poista.repository.IDoCrawlRepository;
import com.ljf.ploughthewaves.domain.poista.service.crwal.CrawlFactory;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj1Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj2Dao;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj1;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class CrawlConsumer {

    @Resource
    public CrawlFactory crawlFactory;

    @Resource
    public UserAndOj1Dao userAndOj1Dao;

    @Resource
    public IDoCrawlRepository doCrawlRepository;

    @KafkaListener(topics = "crawl",groupId = "docrawl")
    public void onMessage(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> message = Optional.ofNullable(record.value());
        if(!message.isPresent()) {
            return;
        }

        //处理消息
        try {
            CrawlReq crawlReq = JSON.parseObject((String) message.get(), CrawlReq.class);
            if(crawlReq.getOjType() >= 2) {
                CrawlRes res = (CrawlRes) crawlFactory.crawlConfig.get(crawlReq.ojType).doCrawl(crawlReq);
                doCrawlRepository.updateOj1(res);
                log.info("消费完成 : {}",res.toString());
            } else {
                ContestCrawlRes res = (ContestCrawlRes) crawlFactory.crawlConfig.get(crawlReq.ojType).doCrawl(crawlReq);
                doCrawlRepository.updateOj2(res);
                log.info("消费完成 : {}",res.toString());
            }

            ack.acknowledge();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
