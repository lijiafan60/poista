package com.ljf.ploughthewaves.domain.poista.service.crwal;

import com.alibaba.fastjson.JSONException;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public interface Crawl {
    void doCrawl(CrawlReq crawlReq, CrawlRes crawlRes, CountDownLatch countDownLatch) throws IOException;
}
