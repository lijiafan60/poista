package com.ljf.ploughthewaves.domain.poista.service.crwal.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ljf.ploughthewaves.domain.poista.model.req.CrawlReq;
import com.ljf.ploughthewaves.domain.poista.model.res.ContestCrawlRes;
import com.ljf.ploughthewaves.domain.poista.service.crwal.Crawl;
import com.ljf.ploughthewaves.domain.poista.service.util.OkHttpApi;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class AtcoderCrawl implements Crawl {
    @Resource
    private OkHttpApi okHttpApi;

    @Override
    public ContestCrawlRes doCrawl(CrawlReq crawlReq) throws IOException {
        ContestCrawlRes atcoder = new ContestCrawlRes();

        atcoder.setUid(crawlReq.getUid());
        atcoder.setOjType(crawlReq.getOjType());
        atcoder.setOjUsername(crawlReq.getOjUsername());

        log.info("time1 : {}",new Date());
        /**
         * 计算总刷题数
         */
        String run = okHttpApi.run("https://kenkoooo.com/atcoder/atcoder-api/v3/user/ac_rank?user=" + crawlReq.getOjUsername());
        if(run == "") {
            log.error("用户{}的 {} {} 不存在",crawlReq.getUid(),crawlReq.getOjType(),crawlReq.getOjUsername());
            atcoder.setUpdTime(new Date());
            return atcoder;
        }
        atcoder.setAllSolvedNumber(Integer.valueOf(JSONObject.parseObject(run).getString("count")));
        /** 计算一个月内刷题数
         *
         */
        int nowTimeSeconds = (int) (System.currentTimeMillis()/1000);
        nowTimeSeconds -= 2592000;
        run = okHttpApi.run("https://kenkoooo.com/atcoder/atcoder-api/v3/user/submissions?user=" + crawlReq.getOjUsername() + "&from_second=" + nowTimeSeconds);
        JSONArray jsonArray = JSON.parseArray(run);
        System.out.println(jsonArray.size());
        List<String> acList = new LinkedList<>();
        for(int i=0;i<jsonArray.size();i++) {
            if (jsonArray.getJSONObject(i).getString("result").equals("AC")) {
                acList.add(jsonArray.getJSONObject(i).getString("problem_id"));
            }
        }
        Set<String> acSet = new HashSet<>(acList);
        atcoder.setRecentSolvedNumber(acSet.size());
        log.info("time2 : {}",new Date());
        /**
         * 爬取atcoder网页
         */
        String url = "https://atcoder.jp/users/" + crawlReq.getOjUsername();
        Document document = Jsoup.connect(url).get();
        Element element = document.select("table.dl-table").select("tbody").get(1);
        Elements el = element.select("tr:contains(Rating)");

        atcoder.setNowRating(Integer.parseInt(el.get(0).select("td span").get(0).text()));

        atcoder.setMaxRating(Integer.parseInt(el.get(1).select("td span").get(0).text()));

        el = element.select("tr:contains(Rated Matches)");

        log.info("time3 : {}",new Date());
        atcoder.setAllContestNumber(Integer.parseInt(el.get(0).select("td").get(0).text()));
        atcoder.setUpdTime(new Date());
        return atcoder;
    }
}
