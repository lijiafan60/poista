package com.ljf.ploughthewaves.domain.poista.service.crwal.impl;

import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;
import com.ljf.ploughthewaves.domain.poista.service.crwal.Crawl;
import org.springframework.stereotype.Service;

@Service
public class AtcoderCrawl implements Crawl {
    @Override
    public CrawlRes doCrawl(CrawlReq crawlReq) {
        return null;
    }
}
