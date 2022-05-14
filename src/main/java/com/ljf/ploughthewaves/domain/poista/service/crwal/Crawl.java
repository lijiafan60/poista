package com.ljf.ploughthewaves.domain.poista.service.crwal;

import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;

public interface Crawl {
    CrawlRes doCrawl(CrawlReq crawlReq);
}
