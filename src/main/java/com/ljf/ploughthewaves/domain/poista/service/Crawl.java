package com.ljf.ploughthewaves.domain.poista.service;

import com.ljf.ploughthewaves.domain.poista.model.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.CrawlRes;

public interface Crawl {
    CrawlRes doCrawl(CrawlReq crawlReq);
}
