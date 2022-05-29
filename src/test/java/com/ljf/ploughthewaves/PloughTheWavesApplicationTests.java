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

}
