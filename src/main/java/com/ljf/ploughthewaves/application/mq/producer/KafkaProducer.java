package com.ljf.ploughthewaves.application.mq.producer;

import com.alibaba.fastjson.JSON;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.kafka.support.SendResult;
import javax.annotation.Resource;

@Slf4j
@Component
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    public static final String TOPIC_CRAWL = "crawl";
    /**
     * 发送爬虫请求
     * @param crawlReq
     * @return
     */
    public ListenableFuture<SendResult<String, Object>> sendCrawlReq(CrawlReq crawlReq) {
        String obJson = JSON.toJSONString(crawlReq);
        log.info("发送爬虫请求：topic：{}，message：{}",TOPIC_CRAWL,obJson);
        return kafkaTemplate.send(TOPIC_CRAWL,obJson);
    }
}
