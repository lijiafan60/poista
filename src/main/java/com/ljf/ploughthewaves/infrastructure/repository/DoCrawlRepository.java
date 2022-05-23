package com.ljf.ploughthewaves.infrastructure.repository;

import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;
import com.ljf.ploughthewaves.domain.poista.repository.IDoCrawlRepository;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj1Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj2Dao;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj1;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Slf4j
@Repository
public class DoCrawlRepository implements IDoCrawlRepository {
    @Resource
    public UserAndOj1Dao userAndOj1Dao;

    @Resource
    public UserAndOj2Dao userAndOj2Dao;

    public void updateOj1(CrawlRes crawlRes) {
        UserAndOj1 userAndOj1 = new UserAndOj1();
        userAndOj1.setOjUsername(crawlRes.getOjUsername());
        userAndOj1.setOjType(crawlRes.getOjType());
        userAndOj1.setUid(crawlRes.getUid());

        userAndOj1.setAllSolvedNumber(crawlRes.getAllSolvedNumber());
        userAndOj1.setUpdTime(crawlRes.getUpdTime());
        userAndOj1Dao.update(userAndOj1);
    }

    public void updateOj2(CrawlRes contestCrawlRes) {
        UserAndOj2 userAndOj2 = new UserAndOj2();
        userAndOj2.setUid(contestCrawlRes.getUid());
        userAndOj2.setOjType(contestCrawlRes.getOjType());
        userAndOj2.setOjUsername(contestCrawlRes.getOjUsername());

        userAndOj2.setAllSolvedNumber(contestCrawlRes.getAllSolvedNumber());
        userAndOj2.setRecentSolvedNumber(contestCrawlRes.getRecentSolvedNumber());


        userAndOj2.setMaxRating(contestCrawlRes.getMaxRating());
        userAndOj2.setNowRating(contestCrawlRes.getNowRating());
        userAndOj2.setRecentMaxRating(contestCrawlRes.getRecentMaxRating());

        userAndOj2.setAllContestNumber(contestCrawlRes.getAllContestNumber());
        userAndOj2.setRecentContestNumber(contestCrawlRes.getRecentContestNumber());
        userAndOj2Dao.update(userAndOj2);
    }

}
