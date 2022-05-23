package com.ljf.ploughthewaves.domain.poista.service.crwal;

import com.ljf.ploughthewaves.domain.poista.service.crwal.impl.AtcoderCrawl;
import com.ljf.ploughthewaves.domain.poista.service.crwal.impl.CodeforcesCrawl;
import com.ljf.ploughthewaves.domain.poista.service.crwal.impl.VjudgeCrawl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class CrawlFactory {
    @Resource
    private Crawl codeforcesCrawl;
    @Resource
    private Crawl atcoderCrawl;
    @Resource
    private Crawl vjudgeCrawl;

    public Map<Integer,Crawl> crawlConfig = new HashMap<>();

    @PostConstruct
    public void init() {
        crawlConfig.put(0, codeforcesCrawl);
        crawlConfig.put(1, atcoderCrawl);
        crawlConfig.put(2, vjudgeCrawl);
    }
}
