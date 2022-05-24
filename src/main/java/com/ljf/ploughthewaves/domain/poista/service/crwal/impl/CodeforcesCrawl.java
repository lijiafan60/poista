package com.ljf.ploughthewaves.domain.poista.service.crwal.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.res.CrawlRes;
import com.ljf.ploughthewaves.domain.poista.service.crwal.Crawl;
import com.ljf.ploughthewaves.domain.poista.service.util.OkHttpApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;


@Slf4j
@Service
public class CodeforcesCrawl implements Crawl {

    @Resource
    private OkHttpApi okHttpApi;

    @Override
//    @Async("CodeforcesExecutor")
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

//            Thread.sleep(200);

        run = okHttpApi.run("https://codeforces.com/api/user.info?handles=" + crawlReq.getOjUsername());
        jsonArray = JSONObject.parseObject(run).getJSONArray("result");

        codeforces.setMaxRating(jsonArray.getJSONObject(0).getInteger("maxRating"));
        codeforces.setNowRating(jsonArray.getJSONObject(0).getInteger("rating"));

//            Thread.sleep(200);

        run = okHttpApi.run("https://codeforces.com/api/user.status?handle=" + crawlReq.getOjUsername());
        jsonArray = JSONObject.parseObject(run).getJSONArray("result");
        Set<String> acSet = new HashSet<>();
        for(int i=0;i<jsonArray.size();i++) {
            if (jsonArray.getJSONObject(i).getString("verdict").equals("OK")) {
                JSONArray problem = jsonArray.getJSONObject(i).getJSONArray("problem");
                acSet.add(problem.getString(0) + problem.getString(1));
            }
        }
        codeforces.setAllSolvedNumber(acSet.size());

        log.info("更新完成：{}",codeforces.toString());
        countDownLatch.countDown();
    }
}
