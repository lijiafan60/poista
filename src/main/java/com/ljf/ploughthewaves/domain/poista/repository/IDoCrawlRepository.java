package com.ljf.ploughthewaves.domain.poista.repository;

import com.ljf.ploughthewaves.domain.poista.model.res.ContestCrawlRes;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;

import java.util.List;

public interface IDoCrawlRepository {
    void updateOj1(CrawlRes crawlRes);
    void updateOj2(ContestCrawlRes contestCrawlRes);
}
