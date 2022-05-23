package com.ljf.ploughthewaves;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.service.crwal.Crawl;
import com.ljf.ploughthewaves.domain.poista.service.crwal.impl.AtcoderCrawl;
import com.ljf.ploughthewaves.domain.poista.service.crwal.impl.CodeforcesCrawl;
import com.ljf.ploughthewaves.domain.poista.service.crwal.impl.VjudgeCrawl;
import com.ljf.ploughthewaves.domain.poista.service.util.OkHttpApi;
import com.ljf.ploughthewaves.domain.wx.model.MessageTextEntity;
import com.ljf.ploughthewaves.domain.wx.utils.XmlUtil;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj1Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj2Dao;
import com.ljf.ploughthewaves.infrastructure.po.User;
import com.ljf.ploughthewaves.interfaces.WxPortalController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Slf4j
@SpringBootTest
class PloughTheWavesApplicationTests {
    @Test
    void contextLoads() {
        for(int i=0;i<=3;i++) {
            User user = new User();
            log.info("{}",System.identityHashCode(user));
            user.setPassword("123");
            log.info("{}",System.identityHashCode(user));
        }
        User user = new User(1,"1","2","3",false,false,"4","111");
        log.info("{}",System.identityHashCode(user));
        user.setRole("hahaha");
        log.info("{}",System.identityHashCode(user));
    }
    @Test
    void testXmlUtil() {
        String requestBody = "<xml><ToUserName><![CDATA[gh_b4f81d822ae3]]></ToUserName><FromUserName><![CDATA[o0_qEt7Vy0ABFnjXpsm9TbKqlYlo]]></FromUserName><CreateTime>1652291116</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[h]]></Content><MsgId>23655123084782957</MsgId></xml>";
        MessageTextEntity message = XmlUtil.xmlToBean(requestBody,MessageTextEntity.class);
        System.out.println(message.getMsgType());
        System.out.println(message.getCreateTime());
    }
    @Test
    void atcoder() throws IOException {
        OkHttpApi okHttpApi = new OkHttpApi();
        String run1 = okHttpApi.run("https://kenkoooo.com/atcoder/atcoder-api/v3/user/ac_rank?user=lijiafa8");
//        String run2 = okHttpApi.run("https://atcoder.jp/users/" + "xlhfd");
        log.info(run1);
//        log.info(run2);
    }
    @Resource
    UserAndOj1Dao userAndOj1Dao;
    @Resource
    UserAndOj2Dao userAndOj2Dao;
    @Resource
    OkHttpApi okHttpApi;
    @Resource
    Crawl codeforcesCrawl;
    @Resource
    Crawl atcoderCrawl;
    @Resource
    Crawl vjudgeCrawl;
    @Test
    void testCodeforcesCrawl() throws IOException {
        CrawlReq crawlReq = new CrawlReq();
        crawlReq.setUid(11);
        crawlReq.setOjType(0);
        crawlReq.setOjUsername("lllijiafan");

    }
    @Test
    void testAtcoderCrawl() throws IOException {
        CrawlReq crawlReq = new CrawlReq();
        crawlReq.setUid(11);
        crawlReq.setOjType(1);
        crawlReq.setOjUsername("lijiafan678");

    }
    @Test
    void testVjudgeCrawl() throws IOException {
        CrawlReq crawlReq = new CrawlReq();
        crawlReq.setUid(11);
        crawlReq.setOjType(2);
        crawlReq.setOjUsername("lijiafan666");
    }
}
