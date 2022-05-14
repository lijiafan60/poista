package com.ljf.ploughthewaves.domain.poista.service.crwal;

import com.ljf.ploughthewaves.domain.poista.service.crwal.impl.AtcoderCrawl;
import com.ljf.ploughthewaves.domain.poista.service.crwal.impl.CodeforcesCrawl;
import com.ljf.ploughthewaves.domain.poista.service.crwal.impl.VjudgeCrawl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class CrawlFactory {
    public Map<Integer,Crawl> crawlConfig = new HashMap<>();
    @Resource
    private CodeforcesCrawl codeforcesCrawl;
    @Resource
    private AtcoderCrawl atcoderCrawl;
    @Resource
    private VjudgeCrawl vjudgeCrawl;

    @PostConstruct
    public void init() {
        crawlConfig.put(1, codeforcesCrawl);
        crawlConfig.put(2, atcoderCrawl);
        crawlConfig.put(3, vjudgeCrawl);
    }
}
