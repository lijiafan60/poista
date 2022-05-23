package com.ljf.ploughthewaves.domain.poista.repository;

import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;

public interface IDoCrawlRepository {
    void updateOj1(CrawlRes crawlRes);
    void updateOj2(CrawlRes contestCrawlRes);
}
