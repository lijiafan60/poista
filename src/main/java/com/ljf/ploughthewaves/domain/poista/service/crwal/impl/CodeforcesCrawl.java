package com.ljf.ploughthewaves.domain.poista.service.crwal.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;
import com.ljf.ploughthewaves.domain.poista.service.crwal.Crawl;
import com.ljf.ploughthewaves.domain.poista.service.util.OkHttpApi;
import com.ljf.ploughthewaves.infrastructure.dao.TagsStatisticsDao;
import com.ljf.ploughthewaves.infrastructure.po.TagsStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;


@Slf4j
@Service
public class CodeforcesCrawl implements Crawl {

    @Resource
    private OkHttpApi okHttpApi;
    @Resource
    private TagsStatisticsDao tagsStatisticsDao;

    @Override
    public void doCrawl(CrawlReq crawlReq, CrawlRes codeforces,CountDownLatch countDownLatch) throws IOException,JSONException{
        String run;
        codeforces.setUid(crawlReq.getUid());
        codeforces.setOjType(crawlReq.getOjType());
        codeforces.setOjUsername(crawlReq.getOjUsername());
        codeforces.setAllSolvedNumber(crawlReq.getAllSolvedNumber());
        run = okHttpApi.run("https://codeforces.com/api/user.rating?handle=" + crawlReq.getOjUsername());
        if (JSONObject.parseObject(run).getString("status").equals("FAILED")) {
            log.error("用户{}的 {} {} 不存在", crawlReq.getUid(), crawlReq.getOjType(), crawlReq.getOjUsername());
        }
        JSONArray jsonArray = JSONObject.parseObject(run).getJSONArray("result");
        int nowTimeSeconds = (int) (System.currentTimeMillis()/1000) , ratingUpdateTimeSeconds , newRating;
        int cnt = 0;
        int siz = jsonArray.size();
        Integer recentMaxRating = 1200,recentContestNumber = 0;
        for(int i = siz - 1;i >= 0;i --){
            newRating = jsonArray.getJSONObject(i).getInteger("newRating");
            ratingUpdateTimeSeconds = jsonArray.getJSONObject(i).getInteger("ratingUpdateTimeSeconds");
            if(nowTimeSeconds - ratingUpdateTimeSeconds > 2592000) break;
            if(cnt < 5) {
                cnt++;
                recentMaxRating = Math.max(recentMaxRating,newRating);
            }
            recentContestNumber = recentContestNumber + 1;
        }

        codeforces.setRecentMaxRating(recentMaxRating);
        codeforces.setRecentContestNumber(recentContestNumber);
        codeforces.setAllContestNumber(JSONObject.parseObject(run).getJSONArray("result").size());


        run = okHttpApi.run("https://codeforces.com/api/user.info?handles=" + crawlReq.getOjUsername());
        jsonArray = JSONObject.parseObject(run).getJSONArray("result");

        codeforces.setMaxRating(jsonArray.getJSONObject(0).getInteger("maxRating"));
        codeforces.setNowRating(jsonArray.getJSONObject(0).getInteger("rating"));


        run = okHttpApi.run("https://codeforces.com/api/user.status?handle=" + crawlReq.getOjUsername());
        jsonArray = JSONObject.parseObject(run).getJSONArray("result");

        TagsStatistics tagsStatistics = new TagsStatistics();
        Set<String> acSet = new HashSet<>();
        int math = 0,greedy = 0,graphs = 0,bruteforce = 0,dataStructure = 0,dp = 0;
        for(int i=0;i<jsonArray.size();i++) {
            if (jsonArray.getJSONObject(i).getString("verdict").equals("OK")) {
                JSONObject problem = jsonArray.getJSONObject(i).getJSONObject("problem");
                String tmp = problem.getString("contestId") + problem.getString("index");
                if(!acSet.contains(tmp)) {
                    List<String> tags = problem.getJSONArray("tags").toJavaList(String.class);
                    for(String x : tags) {
                        if(x.equals("greedy")) greedy++;
                        else if(x.equals("math")) math++;
                        else if(x.equals("graphs")) graphs++;
                        else if(x.equals("data structures")) dataStructure++;
                        else if(x.equals("brute force")) bruteforce++;
                        else if(x.equals("dp")) dp++;
                    }
                    acSet.add(tmp);
                }
            }
        }
        codeforces.setAllSolvedNumber(acSet.size());
        tagsStatistics.setMath(math);
        tagsStatistics.setGreedy(greedy);
        tagsStatistics.setGraphs(graphs);
        tagsStatistics.setDataStructure(dataStructure);
        tagsStatistics.setBruteforce(bruteforce);
        tagsStatistics.setDp(dp);

        tagsStatisticsDao.update(tagsStatistics);
        log.info("更新完成：{}", codeforces);
        countDownLatch.countDown();
    }
}
