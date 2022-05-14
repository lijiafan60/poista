package com.ljf.ploughthewaves.infrastructure.repository;

import com.ljf.ploughthewaves.domain.poista.model.res.ContestCrawlRes;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;
import com.ljf.ploughthewaves.domain.poista.repository.IDoCrawlRepository;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj1Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj2Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj1;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj2;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class DoCrawlRepository implements IDoCrawlRepository {
    @Resource
    public UserAndOj1Dao userAndOj1Dao;

    @Resource
    public UserAndOj2Dao userAndOj2Dao;

    @Resource
    public UserDao userDao;

    public void updateOj1(CrawlRes crawlRes) {
        UserAndOj1 userAndOj1 = new UserAndOj1();
        Integer uid = userDao.queryUserIdByOpenId(crawlRes.getOpenid());
        userAndOj1.setUid(uid);
        userAndOj1.setOjType(crawlRes.getOjType());
        userAndOj1.setOjUsername(crawlRes.getOjUsername());

        userAndOj1.setAllSolvedNumber(crawlRes.getAllSolvedNumber());

        userAndOj1Dao.update(userAndOj1);
    }

    public void updateOj2(ContestCrawlRes contestCrawlRes) {
        UserAndOj2 userAndOj2 = new UserAndOj2();
        Integer uid = userDao.queryUserIdByOpenId(contestCrawlRes.getOpenid());
        userAndOj2.setUid(uid);
        userAndOj2.setOjType(contestCrawlRes.getOjType());
        userAndOj2.setOjUsername(contestCrawlRes.getOjUsername());

        userAndOj2.setAllContestNumber(contestCrawlRes.getAllContestNumber());
        userAndOj2.setRecentSolvedNumber(contestCrawlRes.getRecentSolvedNumber());

        userAndOj2.setMaxRating(contestCrawlRes.getMaxRating());
        userAndOj2.setNowRating(contestCrawlRes.getNowRating());
        userAndOj2.setRecentMaxRating(contestCrawlRes.getRecentMaxRating());

        userAndOj2.setAllContestNumber(contestCrawlRes.getAllContestNumber());
        userAndOj2.setRecentContestNumber(contestCrawlRes.getRecentContestNumber());

        userAndOj2Dao.update(userAndOj2);
    }

}
