package com.ljf.ploughthewaves.domain.poista.service.crwal;

import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.res.CommonRes;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;

import java.io.IOException;

public interface Crawl {
    CommonRes doCrawl(CrawlReq crawlReq) throws IOException;
}
