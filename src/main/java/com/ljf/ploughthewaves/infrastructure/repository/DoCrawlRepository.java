package com.ljf.ploughthewaves.infrastructure.repository;

import com.ljf.ploughthewaves.domain.poista.model.res.ContestCrawlRes;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;
import com.ljf.ploughthewaves.domain.poista.repository.IDoCrawlRepository;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj1Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserAndOj2Dao;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj1;
import com.ljf.ploughthewaves.infrastructure.po.UserAndOj2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class DoCrawlRepository implements IDoCrawlRepository {
    @Resource
    public UserAndOj1Dao userAndOj1Dao;

    @Resource
    public UserAndOj2Dao userAndOj2Dao;

    public void updateOj1(List<CrawlRes> crawlResList) {
        log.info("将crawlResList转换成updateOj1List");
        final List<UserAndOj1> userAndOj1List = new ArrayList<>();
        crawlResList.stream().forEach(x -> {
            UserAndOj1 userAndOj1 = new UserAndOj1();
            Integer uid = x.getUid();
            userAndOj1.setUid(uid);
            userAndOj1.setOjType(x.getOjType());
            userAndOj1.setOjUsername(x.getOjUsername());
            userAndOj1.setAllSolvedNumber(x.getAllSolvedNumber());
            userAndOj1List.add(userAndOj1);
        });
        log.info("执行落库");
        userAndOj1Dao.updateByList(userAndOj1List);
    }

    public void updateOj2(List<ContestCrawlRes> contestCrawlResList) {
        log.info("将contestCrawlResList转换成updateOj2List");
        final List<UserAndOj2> userAndOj2List = new ArrayList<>();
        contestCrawlResList.stream().forEach(x -> {
            UserAndOj2 userAndOj2 = new UserAndOj2();
            Integer uid = x.getUid();
            userAndOj2.setUid(uid);
            userAndOj2.setOjType(x.getOjType());
            userAndOj2.setOjUsername(x.getOjUsername());

            userAndOj2.setAllContestNumber(x.getAllContestNumber());
            userAndOj2.setRecentSolvedNumber(x.getRecentSolvedNumber());

            userAndOj2.setMaxRating(x.getMaxRating());
            userAndOj2.setNowRating(x.getNowRating());
            userAndOj2.setRecentMaxRating(x.getRecentMaxRating());

            userAndOj2.setAllContestNumber(x.getAllContestNumber());
            userAndOj2.setRecentContestNumber(x.getRecentContestNumber());
            userAndOj2List.add(userAndOj2);
        });
        log.info("执行落库");
        userAndOj2Dao.updateByList(userAndOj2List);
    }

}
