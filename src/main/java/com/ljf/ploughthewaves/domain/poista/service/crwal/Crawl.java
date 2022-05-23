package com.ljf.ploughthewaves.domain.poista.service.crwal;

import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;

import java.io.IOException;

public interface Crawl {
    void doCrawl(CrawlReq crawlReq, CrawlRes crawlRes) throws IOException;
}
