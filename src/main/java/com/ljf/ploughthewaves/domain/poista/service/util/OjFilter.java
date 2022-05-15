package com.ljf.ploughthewaves.domain.poista.service.util;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public class OjFilter {
    public static Map<String,Integer> NameToType = new HashMap<String,Integer>() {
        {
            put("codeforces",Integer.valueOf(0));
            put("atcoder",Integer.valueOf(1));
            put("vjudge",Integer.valueOf(2));
        }
    };

    public static Map<Integer,String> TypeToName = new HashMap<Integer,String>() {
        {
            put(Integer.valueOf(0),"codeforces");
            put(Integer.valueOf(1),"atcoder");
            put(Integer.valueOf(2),"vjudge");
        }
    };

}
