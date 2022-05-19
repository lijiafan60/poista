package com.ljf.ploughthewaves.domain.poista.service.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@Component
public class SolvedNumbersApi {

    @Resource
    private OkHttpApi okHttpApi;
    public int getSolvedNumbers(String OJ, String username) throws IOException {
        String run = okHttpApi.run("https://ojhunt.com/api/crawlers/" + OJ + "/" + username);
        if(JSONObject.parseObject(run).getString("error").equals("false")) {
            return JSONObject.parseObject(run).getJSONObject("data").getInteger("solved");
        } else {
            return -1;
        }
    }
}
