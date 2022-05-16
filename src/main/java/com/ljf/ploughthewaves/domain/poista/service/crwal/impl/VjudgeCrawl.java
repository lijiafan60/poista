package com.ljf.ploughthewaves.domain.poista.service.crwal.impl;

import com.alibaba.fastjson.JSONObject;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;
import com.ljf.ploughthewaves.domain.poista.service.crwal.Crawl;
import com.ljf.ploughthewaves.domain.poista.service.util.OkHttpApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Service
public class VjudgeCrawl implements Crawl {

    @Resource
    private OkHttpApi okHttpApi;

    @Override
    public CrawlRes doCrawl(CrawlReq crawlReq) throws IOException {

        CrawlRes vjudge = new CrawlRes();

        vjudge.setOjType(crawlReq.getOjType());
        vjudge.setOjUsername(crawlReq.getOjUsername());
        vjudge.setUid(crawlReq.getUid());
        String run = okHttpApi.run("https://ojhunt.com/api/crawlers/vjudge/" + crawlReq.getOjUsername());
        if(JSONObject.parseObject(run).getString("error").equals("false")) {
            vjudge.setAllSolvedNumber(JSONObject.parseObject(run).getJSONObject("data").getInteger("solved"));
        }
        vjudge.setUpdTime(new Date());
        return vjudge;
    }
}
