package com.ljf.ploughthewaves.domain.poista.repository;

import com.ljf.ploughthewaves.domain.poista.model.res.ContestCrawlRes;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;

import java.util.List;

public interface IDoCrawlRepository {
    void updateOj1(List<CrawlRes> crawlResList);
    void updateOj2(List<ContestCrawlRes> contestCrawlResList);
}
