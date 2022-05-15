package com.ljf.ploughthewaves.domain.poista.service.crwal.impl;

import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.res.ContestCrawlRes;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;
import com.ljf.ploughthewaves.domain.poista.service.crwal.Crawl;
import com.ljf.ploughthewaves.domain.poista.service.util.OkHttpApi;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class AtcoderCrawl implements Crawl {
    @Resource
    private OkHttpApi okHttpApi;

    @Override
    public ContestCrawlRes doCrawl(CrawlReq crawlReq) throws IOException {
        ContestCrawlRes atcoder = new ContestCrawlRes();

        atcoder.setUid(crawlReq.getUid());
        atcoder.setOjType(crawlReq.getOjType());
        atcoder.setOjUsername(crawlReq.getOjUsername());

        String run = okHttpApi.run("https://atcoder.jp/users/" + crawlReq.getOjUsername());

        return atcoder;
    }
}
